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
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.common.models.SimpleIdNameModel;

import java.util.ArrayList;
import java.util.List;

import static ru.ibs.gasu.client.api.WindowDictsApi.WINDOW_DICTS_API;

@SuppressWarnings("GWTStyleCheck")
public class ExMethodsOfExeObligationDicWindow extends Window {
    private Grid<SimpleIdNameModel> grid;
    private ListStore<SimpleIdNameModel> store;

    private void fetchDict() {
        grid.mask("Загрузка справочника...");
        WINDOW_DICTS_API.getAllEnsureMethods(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Ошибка", "Не удалось получить справочник способов обеспечения обязательств")
                        .show();
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                store.addAll(response);
                grid.unmask();
            }
        });
    }

    public ExMethodsOfExeObligationDicWindow() {
        this.setModal(true);
        this.setBorders(false);
        this.setHeaderVisible(false);
        this.setBlinkModal(true);
        this.setWidth((int) (com.google.gwt.user.client.Window.getClientWidth() * 0.4));
        this.setHeight((int) (com.google.gwt.user.client.Window.getClientHeight() * 0.6));
        SafeHtml headerText = new SafeHtmlBuilder()
                .appendHtmlConstant("<b><h3>Выберите способ обеспечения</h3></b>").toSafeHtml();

        HTML header = new HTML(headerText);

        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(header);
        container.add(createGrid(), new VerticalLayoutContainer.VerticalLayoutData(-1, 0.9));

        saveButton = new IconButton(
                "Сохранить", "fas fa-save", 1) {
            @Override
            protected void onClick(Event event) {
                onSave(grid.getSelectionModel().getSelectedItems());
                ExMethodsOfExeObligationDicWindow.this.hide();
            }
        };
        saveButton.setWidth(100);

        IconButton cancelButton = new IconButton(
                "Отменить", "fa-times", 1) {
            @Override
            protected void onClick(Event event) {
                ExMethodsOfExeObligationDicWindow.this.hide();
            }
        };
        cancelButton.setWidth(100);
        cancelButton.setStyleName("x-toolbar-mark");

        buttonBar.add(saveButton);
        buttonBar.add(cancelButton);
        buttonBar.setPadding(new Padding(0, 20, 20, 0));

        fetchDict();
        add(container, new MarginData(0, 10, 0, 10));
    }

    private IconButton saveButton;

    private Grid<SimpleIdNameModel> createGrid() {

        store = new ListStore<>(new ModelKeyProvider<SimpleIdNameModel>() {
            @Override
            public String getKey(SimpleIdNameModel item) {
                return String.valueOf(item.getId());// == null ? -1 * Random.nextInt() : item.getId());
            }
        });

        final CheckBoxSelectionModel<SimpleIdNameModel> selectionModel = new CheckBoxSelectionModel<SimpleIdNameModel>();
        selectionModel.setSelectionMode(Style.SelectionMode.SINGLE);

        ColumnConfig<SimpleIdNameModel, String> nameColumn = new ColumnConfig<>(new ValueProvider<SimpleIdNameModel, String>() {
            @Override
            public String getValue(SimpleIdNameModel object) {
                return object.getName() == null ? "" : object.getName();
            }

            @Override
            public void setValue(SimpleIdNameModel object, String value) {
                object.setName(value);
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 80, "Наименование");


        List<ColumnConfig<SimpleIdNameModel, ?>> columnConfigs = new ArrayList<>();

        ColumnConfig checkBoxColumn = selectionModel.getColumn();
        SafeHtml header = new SafeHtmlBuilder()
                .appendHtmlConstant("<div> </div>").toSafeHtml();
        checkBoxColumn.setHeader(header);

        columnConfigs.add(checkBoxColumn);
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

    protected void onSave(List<SimpleIdNameModel> picked) {
    }

}
