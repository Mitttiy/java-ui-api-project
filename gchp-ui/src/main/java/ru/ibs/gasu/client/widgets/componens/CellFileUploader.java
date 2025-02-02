package ru.ibs.gasu.client.widgets.componens;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import ru.ibs.gasu.client.api.GchpApi;
import ru.ibs.gasu.client.widgets.WidgetUtils;
import ru.ibs.gasu.common.models.EemModel;
import ru.ibs.gasu.common.models.FileModel;

import java.util.List;

/**
 * Странноватый загрузчик файлов для ячейки таблицы
 */
public class CellFileUploader extends VerticalLayoutContainer implements IsField<EemModel> {

    private Grid<EemModel> eemGrid;

    private MultipleFilesSimpleUploader uploader;
    private EemModel model;
    private GridInlineEditing<EemModel> editing;

    private FileUploader.GridFileModelCodec codec = GWT.create(FileUploader.GridFileModelCodec.class);

    public CellFileUploader() {
        uploader = new MultipleFilesSimpleUploader() {
            @Override
            public void onSuccess(String response) {
                JSONValue jsonValue = JSONParser.parseStrict(response);
                FileUploader.GridFilesModel decode = codec.decode(jsonValue);
                if (model.getFileModel() == null)
                    model.setFileModel(new FileModel());
                model.getFileModel().setName(decode.getFiles().get(0).getFileName());
                model.getFileModel().setId(decode.getFiles().get(0).getFileVersionId());
                eemGrid.getView().refresh(false);
                eemGrid.unmask();
                editing.cancelEditing();
                this.clearInput();
            }

            @Override
            public void onError(String code, String response) {
                super.onError(code, response);
            }
        };
        uploader.setUploadUrl(GchpApi.FILE_UPLOAD_PATH);
        add(uploader, WidgetUtils.STD_VC_LAYOUT);
    }


    public CellFileUploader(Grid<EemModel> eemGrid, GridInlineEditing<EemModel> editing) {
        this();
        this.eemGrid = eemGrid;
        this.editing = editing;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<EemModel> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setValue(EemModel value) {
        this.model = value;
    }


    @Override
    public void clear() {
    }

    @Override
    public void clearInvalid() {
    }

    @Override
    public void finishEditing() {
    }

    @Override
    public List<EditorError> getErrors() {
        return null;
    }

    @Override
    public boolean isValid(boolean preventMark) {
        return true;
    }

    @Override
    public void reset() {
    }

    @Override
    public boolean validate(boolean preventMark) {
        return true;
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
    }

    @Override
    public EemModel getValue() {
        return null;
    }

    @Override
    protected void onBlur(Event event) {
    }
}
