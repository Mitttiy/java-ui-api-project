package ru.ibs.gasu.client.widgets;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import ru.ibs.gasu.client.api.WindowDictsApi;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.WorkTypeModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorkTypeDicWindow extends Window {

    private Grid<WorkTypeModel> grid;
    private ListStore<WorkTypeModel> store;

    private void fetchDict() {
        grid.mask("Загрузка словаря");
        WindowDictsApi.WINDOW_DICTS_API.getAllPrivateServiceTypes(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Ошибка", "Не удалось загрузить виды работ").show();
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                store.addAll(
                        response.stream()
                                .map(i -> new WorkTypeModel() {{setId(Long.parseLong(i.getId())); setName(i.getName()); }})
                                .collect(Collectors.toList()));
                grid.unmask();
            }
        });
    }

    private void fetchDict1() {
        grid.mask("Загрузка словаря");
        WindowDictsApi.WINDOW_DICTS_API.getAllPublicServiceTypes(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Ошибка", "Не удалось загрузить виды работ").show();
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                store.addAll(
                        response.stream()
                                .map(i -> new WorkTypeModel() {{setId(Long.parseLong(i.getId())); setName(i.getName()); }})
                                .collect(Collectors.toList()));
                grid.unmask();
            }
        });
    }

    public WorkTypeDicWindow(String dictType) {
        this.setModal(true);
        this.setBorders(false);
        this.setHeaderVisible(false);
        this.setBlinkModal(true);
        this.setWidth((int) (com.google.gwt.user.client.Window.getClientWidth() * 0.4));
        this.setHeight((int) (com.google.gwt.user.client.Window.getClientHeight() * 0.6));
        SafeHtml headerText = new SafeHtmlBuilder()
                .appendHtmlConstant("<b><h3>Выберете вид работ</h3></b>").toSafeHtml();

        HTML header = new HTML(headerText);

        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(header);
        container.add(createGrid(), new VerticalLayoutContainer.VerticalLayoutData(-1, 0.9));

        saveButton = new IconButton(
                "Сохранить", "fas fa-save", 1) {
            @Override
            protected void onClick(Event event) {
                onSave(grid.getSelectionModel().getSelectedItems());
                WorkTypeDicWindow.this.hide();
            }
        };
        saveButton.setWidth(100);

        IconButton cancelButton = new IconButton(
                "Отменить", "fa-times", 1) {
            @Override
            protected void onClick(Event event) {
                WorkTypeDicWindow.this.hide();
            }
        };
        cancelButton.setWidth(100);
        cancelButton.setStyleName("x-toolbar-mark");

        buttonBar.add(saveButton);
        buttonBar.add(cancelButton);
        buttonBar.setPadding(new Padding(0, 20, 20, 0));

        if ("private".equals(dictType)) {
            fetchDict();
        } else if ("public".equals(dictType)) {
            fetchDict1();
        }

        add(container, new MarginData(0, 10, 0, 10));
    }

    private IconButton saveButton;

    private Grid<WorkTypeModel> createGrid() {

        store = new ListStore<>(new ModelKeyProvider<WorkTypeModel>() {
            @Override
            public String getKey(WorkTypeModel item) {
                return String.valueOf(item.getId());// == null ? -1 * Random.nextInt() : item.getId());
            }
        });

        final CheckBoxSelectionModel<WorkTypeModel> selectionModel = new CheckBoxSelectionModel<WorkTypeModel>();
        selectionModel.setSelectionMode(Style.SelectionMode.SIMPLE);

        ColumnConfig<WorkTypeModel, String> nameColumn = new ColumnConfig<>(new ValueProvider<WorkTypeModel, String>() {
            @Override
            public String getValue(WorkTypeModel object) {
                return object.getName() == null ? "" : object.getName();
            }

            @Override
            public void setValue(WorkTypeModel object, String value) {
                object.setName(value);
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 80, "Наименование");


        List<ColumnConfig<WorkTypeModel, ?>> columnConfigs = new ArrayList<>();

        columnConfigs.add(selectionModel.getColumn());
        columnConfigs.add(nameColumn);
        columnConfigs.forEach(c -> {
            c.setMenuDisabled(true);
        });

        grid = new Grid<>(store, new ColumnModel<>(columnConfigs));
        grid.setHeight(300);
        grid.getView().setEmptyText("Данные не найдены");
        grid.getView().setForceFit(true);
        grid.getView().setStripeRows(true);

        grid.setSelectionModel(selectionModel);

        grid.setLoadMask(true);
        store.setAutoCommit(true);

        return grid;
    }

    protected void onSave(List<WorkTypeModel> picked) {
    }

}
