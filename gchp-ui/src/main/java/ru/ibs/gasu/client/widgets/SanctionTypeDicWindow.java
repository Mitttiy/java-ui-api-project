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
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.common.models.SanctionTypeModel;

import java.util.ArrayList;
import java.util.List;

public class SanctionTypeDicWindow extends Window {

    private Grid<SanctionTypeModel> grid;
    private ListStore<SanctionTypeModel> store;

    private void fetchDict() {
        grid.mask("Загрузка справочника...");
        store.add(new SanctionTypeModel() {{
            setId(1L);
            setName("Штрафные санкции");
        }});
        store.add(new SanctionTypeModel() {{
            setId(2L);
            setName("Возмещение убытков публичного партнера");
        }});
        grid.unmask();
    }

    public SanctionTypeDicWindow() {
        this.setModal(true);
        this.setBorders(false);
        this.setHeaderVisible(false);
        this.setBlinkModal(true);
        this.setWidth((int) (com.google.gwt.user.client.Window.getClientWidth() * 0.4));
        this.setHeight((int) (com.google.gwt.user.client.Window.getClientHeight() * 0.6));
        SafeHtml headerText = new SafeHtmlBuilder()
                .appendHtmlConstant("<b><h3>Выберите виды санкций</h3></b>").toSafeHtml();

        HTML header = new HTML(headerText);

        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(header);
        container.add(createGrid(), new VerticalLayoutContainer.VerticalLayoutData(-1, 0.9));

        saveButton = new IconButton(
                "Сохранить", "fas fa-save", 1) {
            @Override
            protected void onClick(Event event) {
                onSave(grid.getSelectionModel().getSelectedItems());
                SanctionTypeDicWindow.this.hide();
            }
        };
        saveButton.setWidth(100);

        IconButton cancelButton = new IconButton(
                "Отменить", "fa-times", 1) {
            @Override
            protected void onClick(Event event) {
                SanctionTypeDicWindow.this.hide();
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

    private Grid<SanctionTypeModel> createGrid() {

        store = new ListStore<>(new ModelKeyProvider<SanctionTypeModel>() {
            @Override
            public String getKey(SanctionTypeModel item) {
                return String.valueOf(item.getId());// == null ? -1 * Random.nextInt() : item.getId());
            }
        });

        final CheckBoxSelectionModel<SanctionTypeModel> selectionModel = new CheckBoxSelectionModel<SanctionTypeModel>();
        selectionModel.setSelectionMode(Style.SelectionMode.SIMPLE);

        ColumnConfig<SanctionTypeModel, String> nameColumn = new ColumnConfig<>(new ValueProvider<SanctionTypeModel, String>() {
            @Override
            public String getValue(SanctionTypeModel object) {
                return object.getName() == null ? "" : object.getName();
            }

            @Override
            public void setValue(SanctionTypeModel object, String value) {
                object.setName(value);
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 80, "Наименование");


        List<ColumnConfig<SanctionTypeModel, ?>> columnConfigs = new ArrayList<>();

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

    protected void onSave(List<SanctionTypeModel> picked) {
    }

}
