package ru.ibs.gasu.client.widgets;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;
import ru.ibs.gasu.client.utils.ClientUtils;
import ru.ibs.gasu.client.widgets.componens.DateFieldFullDate;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.SimpleYearIndicatorModel;

import java.math.BigDecimal;
import java.util.Arrays;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class SimpleYearTableWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private HBoxLayoutContainer buttonContainer;
    private TreeGrid<SimpleYearIndicatorModel> treeGrid;
    private MultiSelectComboBox<String> yearBox;
    private DateRangeWidget dateRangeWidget;
    private HorizontalLayoutContainer dateRangeWidgetContainer;
    private CheckBox ndsCheck;
    private ComboBox<SimpleIdNameModel> measureType;
    private DateField dateField;
    private HorizontalLayoutContainer treeHRow;
    private TreeStore<SimpleYearIndicatorModel> treeStore;
    private Runnable runnable;


    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public void setContainer(VerticalLayoutContainer container) {
        this.container = container;
    }

    public TreeGrid<SimpleYearIndicatorModel> getTreeGrid() {
        return treeGrid;
    }

    public void setTreeGrid(TreeGrid<SimpleYearIndicatorModel> treeGrid) {
        this.treeGrid = treeGrid;
    }

    public MultiSelectComboBox<String> getYearBox() {
        return yearBox;
    }

    public CheckBox getNdsCheck() {
        return ndsCheck;
    }

    public void setNdsCheck(CheckBox ndsCheck) {
        this.ndsCheck = ndsCheck;
    }

    public ComboBox<SimpleIdNameModel> getMeasureType() {
        return measureType;
    }

    public void setMeasureType(ComboBox<SimpleIdNameModel> measureType) {
        this.measureType = measureType;
    }

    public DateField getDateField() {
        return dateField;
    }

    public void setDateField(DateField dateField) {
        this.dateField = dateField;
    }

    public void setYearBox(MultiSelectComboBox<String> yearBox) {
        this.yearBox = yearBox;
    }

    public void setTreeStore(TreeStore<SimpleYearIndicatorModel> treeStore) {
        this.treeStore = treeStore;
    }

    static {
        StyleInjector.inject(".investments__aggregate-row { background-color: rgb(100, 145, 255, 0.2) !important; }");
    }

    public SimpleYearTableWidget(Boolean withNdsChek) {
        initButtonContainer();
        initGrid();
        container.add(buttonContainer, STD_VC_LAYOUT);
        if (withNdsChek) {
            initNdsCheck();
            container.add(treeHRow, HEIGHT60_VC_LAYOUT);
        }
        container.add(treeGrid, STD_VC_LAYOUT);
    }

    private void initButtonContainer() {
        container = new VerticalLayoutContainer();
        yearBox = initYearComboBox();
        dateRangeWidget = new DateRangeWidget(yearBox);
        dateRangeWidgetContainer = new HorizontalLayoutContainer();
        dateRangeWidgetContainer.add(dateRangeWidget);
        buttonContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));
        buttonContainer.add(yearBox, boxLayoutData);
        buttonContainer.add(dateRangeWidgetContainer, boxLayoutData);
    }

    private void initNdsCheck() {
        ndsCheck = new CheckBox();
        ndsCheck.setBoxLabel("Включая НДС");
        measureType = createCommonFilterModelComboBox("Выберите тип измерения");
        measureType.getStore().add(new SimpleIdNameModel("1", "В ценах соответствующих лет"));
        measureType.getStore().add(new SimpleIdNameModel("2", "На дату"));
        dateField = new DateFieldFullDate();

        measureType.addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                if ("2".equals(event.getSelectedItem().getId())) {
                    dateField.show();
                } else if ("1".equals(event.getSelectedItem().getId())) {
                    dateField.clear();
                    dateField.hide();
                }
            }
        });

        treeHRow = createThreeFieldWidgetsNdsDateRow(ndsCheck, measureType, dateField);
    }

    private void initGrid() {
        treeStore = new TreeStore<>(new ModelKeyProvider<SimpleYearIndicatorModel>() {
            @Override
            public String getKey(SimpleYearIndicatorModel item) {
                return "by-" + item.getGid();
            }
        });
        treeStore.setAutoCommit(true);
        treeStore.addSortInfo(new Store.StoreSortInfo<>(SimpleYearIndicatorModel.getComparator(), SortDir.ASC));

        ColumnConfig<SimpleYearIndicatorModel, String> year = new ColumnConfig<>(new ValueProvider<SimpleYearIndicatorModel, String>() {
            @Override
            public String getValue(SimpleYearIndicatorModel object) {
                return object.getName();
            }

            @Override
            public void setValue(SimpleYearIndicatorModel object, String value) {
                object.setName(value);
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 50, "Год");
        year.setMenuDisabled(true);
        year.setSortable(false);

        ColumnConfig<SimpleYearIndicatorModel, String> plan = new ColumnConfig<>(new ValueProvider<SimpleYearIndicatorModel, String>() {
            @Override
            public String getValue(SimpleYearIndicatorModel object) {
                return WidgetUtils.doubleToString(object.getPlan());
            }

            @Override
            public void setValue(SimpleYearIndicatorModel object, String value) {
                if (value == null) {
                    object.setPlan(null);
                    onValueChange();
                    return;
                }
                try {
                    object.setPlan(Double.parseDouble(value));
                } catch (Exception ex) {
                }
                onValueChange();
            }

            @Override
            public String getPath() {
                return "plan";
            }
        }, 100, "тыс. руб.");
        plan.setMenuDisabled(true);
        plan.setSortable(false);

        ColumnModel<SimpleYearIndicatorModel> cm = new ColumnModel<>(Arrays.asList(year, plan));
        treeGrid = new TreeGrid<>(treeStore, cm, year);
        treeGrid.setExpandOnDoubleClick(false);
        treeGrid.getView().setForceFit(true);
        treeGrid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        treeGrid.getView().setStripeRows(true);
        final GridInlineEditing<SimpleYearIndicatorModel> editing = new GridInlineEditing<SimpleYearIndicatorModel>(treeGrid);
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(plan, WidgetUtils.createEditField());
        editing.addBeforeStartEditHandler(event -> {
            if (event.getEditCell().getCol() == 0) {
                event.setCancelled(true);
            }
        });
        treeGrid.getView().setViewConfig(new GridViewConfig<SimpleYearIndicatorModel>() {
            @Override
            public String getColStyle(SimpleYearIndicatorModel model, ValueProvider<? super SimpleYearIndicatorModel, ?> valueProvider, int rowIndex, int colIndex) {
                if (model != null) {
                    if (model.getName().equals("Всего")) {
                        return "investments__aggregate-row";
                    }
                }
                return "";
            }

            @Override
            public String getRowStyle(SimpleYearIndicatorModel model, int rowIndex) {
                return "";
            }
        });
        editing.addBeforeStartEditHandler(event -> {
            SimpleYearIndicatorModel item = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            if (item.getName().equals("Всего") && calculate() > 0D)
                event.setCancelled(true);
        });
        editing.addCompleteEditHandler(event -> {
            SimpleYearIndicatorModel editedItem = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            if (!editedItem.getName().equals("Всего")) {
                sum();
            }
        });
    }

    public TreeStore<SimpleYearIndicatorModel> getTreeStore() {
        return treeStore;
    }

    private MultiSelectComboBox<String> initYearComboBox() {
        MultiSelectComboBox<String> multiSelectComboBox = new MultiSelectComboBox<>(
                x -> x,
                x -> x,
                null,
                false);
        multiSelectComboBox.setEmptyText(ClientUtils.getCurrentYear());
        multiSelectComboBox.setEditable(false);
        multiSelectComboBox.addAll(ClientUtils.generateComboBoxYearValues());
        multiSelectComboBox.addSelectionHandler(selectedItems -> {
            //бежим по всем годам в гриде
            for (SimpleYearIndicatorModel year : treeStore.getRootItems()) {
                //если в комбобоксе не выделен год из грида
                if (!selectedItems.contains(year.getName())) {
                    treeStore.remove(year);
                }
            }
            //бежим по выделенным годам в комбобоксе
            for (String item : selectedItems) {
                //если в годах грида нет выделенного комбобокса
                if (treeStore.getRootItems().stream().noneMatch(year -> year.getName().equals(item))) {
                    SimpleYearIndicatorModel i = new SimpleYearIndicatorModel();
                    i.setGid(getRandId());
                    i.setName(item);
                    treeStore.add(i);
                }
            }
        });
        return multiSelectComboBox;
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    public void removeRootRowById(Long id) {
        for (SimpleYearIndicatorModel rootItem : treeStore.getRootItems()) {
            if (rootItem.getGid().equals(id)) {
                treeStore.remove(rootItem);
            }
        }
    }

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void onValueChange() {
        if (runnable != null) {
            runnable.run();
        }
    }

    public void toggle(boolean toggle) {
        if (toggle) {
            container.show();
        } else {
            container.hide();
        }
    }

    public boolean canGridSelectedItemHasYear(SimpleYearIndicatorModel simpleYearIndicatorModel) {
        return treeStore.getParent(simpleYearIndicatorModel) == null;
    }

    private SimpleYearIndicatorModel result;
    public void sum() {
        if (treeStore.getRootItems() != null) {
            Double sum = 0D;
            for (SimpleYearIndicatorModel item : treeStore.getRootItems()) {
                if (!item.getName().equals("Всего")) {
                    sum += item.getPlan();
                } else {
                    result = item;
                }
            }
            result.setPlan(sum);
            treeStore.update(result);
        }
    }

    public Double calculate() {
        if (treeStore.getRootItems() != null) {
            Double sum = 0D;
            for (SimpleYearIndicatorModel item : treeStore.getRootItems()) {
                if (!item.getName().equals("Всего")) {
                    sum += item.getPlan();
                } else {
                    result = item;
                }
            }
            return sum;
        }
        return 0D;
    }
}