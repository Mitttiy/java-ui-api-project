package ru.ibs.gasu.client.widgets;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ProgressBar;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.common.models.ExportTask;
import ru.ibs.gasu.common.soap.generated.gchpdocs.DocumentsFilterPaginateCriteria;

import java.util.List;

import static ru.ibs.gasu.client.api.ExcelExportApi.EXCEL_EXPORT_API;
import static ru.ibs.gasu.client.widgets.WidgetUtils.getDate;

public class ExcelExportWidget implements IsWidget {

    private int currentNumOfTasks = 0;

    private Window w;
    private VerticalLayoutContainer container;

    private void initWindow() {
        w = new Window();
        w.setWidth(com.google.gwt.user.client.Window.getClientWidth() - 5);
        w.setWidth(800);
        w.setResizable(true);
        w.setMinHeight(300);
    }

    public void show() {
        w.show();
    }


    public DocumentsFilterPaginateCriteria getCurrentSearchCriteria() {
        // default
        DocumentsFilterPaginateCriteria criteria = new DocumentsFilterPaginateCriteria();
        criteria.setLimit(0);
        criteria.setOffset(0);
        return criteria;
    }

    private HorizontalLayoutContainer.HorizontalLayoutData layout(double width, double height) {
        return new HorizontalLayoutContainer.HorizontalLayoutData(width, height);
    }

    private Container createHeaderRow() {
        HorizontalLayoutContainer rowContainer = new HorizontalLayoutContainer();
        rowContainer.setHeight(40);

        HTML html = new HTML("Начало загрузки");
        html.getElement().setAttribute("style", "text-align: center; font-weight: bold;");
        rowContainer.add(html, layout(0.25, -1));

        HTML html2 = new HTML("Прогресс");
        html2.getElement().setAttribute("style", "text-align: center; font-weight: bold;");
        rowContainer.add(html2, layout(0.30, -1));

        HTML html3 = new HTML("Статус");
        html3.getElement().setAttribute("style", "text-align: center; font-weight: bold;");
        rowContainer.add(html3, layout(0.30, -1));

        HTML html4 = new HTML("Скачать");
        html4.getElement().setAttribute("style", "text-align: center; font-weight: bold;");
        rowContainer.add(html4, layout(0.15, -1));

        return rowContainer;
    }


    private Container createRow(ExportTask exportTask) {
        HorizontalLayoutContainer rowContainer = new HorizontalLayoutContainer();
        rowContainer.setHeight(40);

        String date = WidgetUtils.formatDate(getDate(exportTask.getStartTime()), "dd.MM.yyyy HH:mm:ss");
        HTML html = new HTML(date);
        html.getElement().setAttribute("style", "text-align: center;");
        rowContainer.add(html, layout(0.25, -1));

        ProgressBar progressBar = new ProgressBar();
        double v = Math.round(exportTask.getProgress() * 100.0) / 100.0;
        progressBar.updateProgress(exportTask.getProgress()/100D, v + "% завершено");
        rowContainer.add(progressBar, layout(0.30, -1));

        HTML html1 = new HTML(exportTask.getStatus());
        html1.getElement().setAttribute("style", "text-align: center;");
        rowContainer.add(html1, layout(0.30, -1));

        if (!exportTask.isDone())
            return rowContainer;

        HorizontalLayoutContainer downloadContainer = new HorizontalLayoutContainer();

        IconButton downloadButton = new IconButton("xlsx", "");
        downloadButton.setWidth(10);
        downloadButton.addStyleName("x-toolbar-mark");
        downloadButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                com.google.gwt.user.client.Window.open("gchpclient/rest/gchp/export/getFileByUuid/" + exportTask.getUuid(), "_blank", "");
            }
        });
        downloadContainer.add(downloadButton, layout(0.50, -1));

        IconButton downloadOdsButton = new IconButton("ods", "");
        downloadOdsButton.setWidth(10);
        downloadOdsButton.addStyleName("x-toolbar-mark");
        downloadOdsButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                com.google.gwt.user.client.Window.open("gchpclient/rest/gchp/export/getOdsFileByUuid/" + exportTask.getUuid(), "_blank", "");
            }
        });
        downloadContainer.add(downloadOdsButton, layout(0.50, -1));

        rowContainer.add(downloadContainer, layout(0.15, -1));

        return rowContainer;
    }



    public ExcelExportWidget() {
        initWindow();

        container = new VerticalLayoutContainer();
        container.add(createHeaderRow(), new VerticalLayoutContainer.VerticalLayoutData(-1, -1));

        w.add(container, new MarginData(25));
        TextButton button = new TextButton("Запустить экспорт");
        button.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                container.mask("Загрузка...");
                DocumentsFilterPaginateCriteria currentSearchCriteria = getCurrentSearchCriteria();
                EXCEL_EXPORT_API.runExcelFileGeneration(currentSearchCriteria, new MethodCallback<List<ExportTask>>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {

                    }

                    @Override
                    public void onSuccess(Method method, List<ExportTask> response) {
                        update(response);
                        container.unmask();
                    }
                });
            }
        });

        TextButton button1 = new TextButton("Удалить завершенные");
        button1.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                container.mask("Загрузка...");
                DocumentsFilterPaginateCriteria currentSearchCriteria = getCurrentSearchCriteria();
                EXCEL_EXPORT_API.clearUserDoneTasks(new MethodCallback<List<ExportTask>>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                    }

                    @Override
                    public void onSuccess(Method method, List<ExportTask> response) {
                        update(response);
                        container.unmask();
                    }
                });
            }
        });

        Timer timer = new Timer() {
            @Override
            public void run() {
                EXCEL_EXPORT_API.getAllTasksStatus(new MethodCallback<List<ExportTask>>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                    }

                    @Override
                    public void onSuccess(Method method, List<ExportTask> response) {
                        update(response);
                    }
                });
            }
        };

        w.addButton(button);
        w.addButton(button1);
        w.addHideHandler(new HideEvent.HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                timer.cancel();
            }
        });
        container.mask("Загрузка...");
        timer.scheduleRepeating(1000);
    }

    public void update(ExportTask exportTask) {
        container.add(createRow(exportTask));
    }

    public void update(List<ExportTask> tasks) {
        currentNumOfTasks = tasks.size();
        container.clear();
        container.add(createHeaderRow());
        for (ExportTask task : tasks) {
            update(task);
        }
        container.unmask();
        container.forceLayout();
    }

    @Override
    public Widget asWidget() {
        return w;
    }
}
