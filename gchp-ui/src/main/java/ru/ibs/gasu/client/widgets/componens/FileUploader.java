package ru.ibs.gasu.client.widgets.componens;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;
import org.fusesource.restygwt.client.JsonEncoderDecoder;
import ru.ibs.gasu.client.widgets.WidgetUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUploader implements IsWidget {
    private FilesUploadHandler filesUploadHandler;

    public interface GridFileModelCodec extends JsonEncoderDecoder<GridFilesModel> {
    }

    private GridFileModelCodec codec = GWT.create(GridFileModelCodec.class);

    public static class GridFilesModel {
        private List<GridFileModel> files = new ArrayList<>();

        public List<GridFileModel> getFiles() {
            return files;
        }

        public void setFiles(List<GridFileModel> files) {
            this.files = files;
        }
    }

    public static class GridFileModel {
        private String fileName;
        private Long fileVersionId;

        public GridFileModel() {
        }

        public GridFileModel(String fileName, Long fileVersionId) {
            this.fileName = fileName;
            this.fileVersionId = fileVersionId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public Long getFileVersionId() {
            return fileVersionId;
        }

        public void setFileVersionId(Long fileVersionId) {
            this.fileVersionId = fileVersionId;
        }
    }

    public FileUploader() {
        initWidget();
    }

    public interface FilesUploadHandler {
        void onSuccess(String data);

        void onError(String code, String response);
    }

    public void setFilesUploadHandler(FilesUploadHandler filesUploadHandler) {
        this.filesUploadHandler = filesUploadHandler;
    }

    private VerticalLayoutContainer container;
    private String uploadUrl;
    private MultipleFilesSimpleUploader uploader;
    private Grid<GridFileModel> grid;
    private ListStore<GridFileModel> store;
    private HTML headingTextHtml;
    private String fileDownloadUrl;

    public void setFileDownloadUrl(String fileDownloadUrl) {
        this.fileDownloadUrl = fileDownloadUrl;
    }

    public void setHeadingText(String headingText) {
        headingTextHtml.setHTML(headingText);
    }

    public void setHeadingText(SafeHtml headingText) {
        headingTextHtml.setHTML(headingText);
    }

    private void initWidget() {
        initGrid();
        container = new VerticalLayoutContainer();
        uploader = new MultipleFilesSimpleUploader(uploadUrl) {
            @Override
            public void onSuccess(String response) {
                JSONValue jsonValue = JSONParser.parseStrict(response);
                GridFilesModel files = codec.decode(jsonValue);
                grid.getStore().addAll(files.getFiles());
            }

            @Override
            public void onError(String code, String response) {
                super.onError(code, response);
            }
        };
        headingTextHtml = new HTML();
        container.add(headingTextHtml);
        headingTextHtml.getElement().getStyle().setFontSize(13, Style.Unit.PX);
        headingTextHtml.getElement().getStyle().setPaddingTop(10, Style.Unit.PX);
        container.add(uploader);
        container.add(grid);
    }

    public MultipleFilesSimpleUploader getUploader() {
        return uploader;
    }

    public List<GridFileModel> getCurrentFiles() {
        return grid.getStore().getAll();
    }

    public void setFiles(List<GridFileModel> files) {
        grid.getStore().clear();
        grid.getStore().addAll(files);
    }

    private void initGrid() {
        store = new ListStore<>(item -> String.valueOf(item.getFileVersionId()));
        ColumnConfig<GridFileModel, String> fileNameCol =
                new ColumnConfig<GridFileModel, String>(WidgetUtils.createReadOnlyVP(gridFileModel -> gridFileModel.getFileName(), "fileNameCol"), 300);
        ColumnConfig<GridFileModel, String> downloadFileCol =
                new ColumnConfig<GridFileModel, String>(WidgetUtils.createReadOnlyVP(gridFileModel -> null, "downloadFileCol"), 50);
        downloadFileCol.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<i class='fa fa-download' aria-hidden='true' style='cursor: pointer;' title='Скачать файл'></i>");
            }
        });
        ColumnConfig<GridFileModel, String> deleteFileCol =
                new ColumnConfig<GridFileModel, String>(WidgetUtils.createReadOnlyVP(gridFileModel -> null, "deleteFileCol"), 50);
        deleteFileCol.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<i class='fa fa-trash-alt' aria-hidden='true' style='cursor: pointer;' title='Удалить файл'></i>");
            }
        });
        List<ColumnConfig<GridFileModel, ?>> list = Arrays.asList(fileNameCol, downloadFileCol, deleteFileCol);
        ColumnModel<GridFileModel> cm = new ColumnModel<>(list);
        grid = new Grid<>(store, cm);
        grid.setHideHeaders(true);
        grid.addAttachHandler(event -> grid.getView().getScroller().getFirstChildElement().setAttribute("style", "border-style: none"));
        grid.addCellClickHandler(event -> {
            int index = event.getCellIndex();
            GridFileModel selected = grid.getSelectionModel().getSelectedItem();
            if (index == 1) {
                Window.open(fileDownloadUrl + "/" + selected.getFileVersionId(), "_blank", "");
            } else if (index == 2) {
                grid.getStore().remove(selected);
            }
        });
        grid.getView().setEmptyText("Отсутствуют прикрепленные файлы");
    }
    @Override
    public Widget asWidget() {
        return container;
    }

    public void toggle(boolean toggle) {
        if (toggle) {
            container.show();
        } else {
            container.hide();
        }
    }
}
