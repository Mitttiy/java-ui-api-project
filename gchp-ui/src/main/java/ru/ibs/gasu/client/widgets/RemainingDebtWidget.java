package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
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
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;
import ru.ibs.gasu.client.utils.ClientUtils;
import ru.ibs.gasu.client.widgets.componens.DateFieldFullDate;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.PlanFactIndicator;
import ru.ibs.gasu.common.models.PlanFactYear;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.SortableTreeNode;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class RemainingDebtWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private TreeGrid<PlanFactYear> treeGrid;
    private MultiSelectComboBox<String> yearBox;
    private DateRangeWidget dateRangeWidget;
    private HorizontalLayoutContainer dateRangeWidgetContainer;
    private TreeStore<PlanFactYear> treeStore;
    private HBoxLayoutContainer buttonsContainer;
    private HBoxLayoutContainer buttonsSecondRowContainer;
    private CheckBox ndsCheck;
    private ComboBox<SimpleIdNameModel> measureType;
    private DateField dateField;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public void setContainer(VerticalLayoutContainer container) {
        this.container = container;
    }

    public TreeGrid<PlanFactYear> getTreeGrid() {
        return treeGrid;
    }

    public void setTreeGrid(TreeGrid<PlanFactYear> treeGrid) {
        this.treeGrid = treeGrid;
    }

    public MultiSelectComboBox<String> getYearBox() {
        return yearBox;
    }

    public void setYearBox(MultiSelectComboBox<String> yearBox) {
        this.yearBox = yearBox;
    }

    public void setTreeStore(TreeStore<PlanFactYear> treeStore) {
        this.treeStore = treeStore;
    }

    public HBoxLayoutContainer getButtonsContainer() {
        return buttonsContainer;
    }

    public void setButtonsContainer(HBoxLayoutContainer buttonsContainer) {
        this.buttonsContainer = buttonsContainer;
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

    static {
        StyleInjector.inject(".investments__aggregate-row { background-color: rgb(100, 145, 255, 0.2) !important; }");
    }

    public RemainingDebtWidget() {
        initButtonContainer();
        initGrid();
        setUpBaseIndicators();
        fillMeasureTypeCombo();

        measureType.addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                if ("2".equals(event.getSelectedItem().getId())) {
                    dateField.show();
                } else if ("1".equals(event.getSelectedItem().getId())) {
                    dateField.clear();
                    dateField.hide();
                }

                Scheduler.get().scheduleDeferred(() -> buttonsContainer.forceLayout());
            }
        });

        measureType.setValue(new SimpleIdNameModel("2", "На дату"));
        measureType.setWidth(220);
        yearBox.setWidth(150);
        container.add(buttonsContainer, STD_VC_LAYOUT);
        container.add(buttonsSecondRowContainer, STD_VC_LAYOUT);
        container.add(treeGrid, STD_VC_LAYOUT);
    }

    private void fillMeasureTypeCombo() {
        measureType.getStore().add(new SimpleIdNameModel("1", "В ценах соответствующих лет"));
        measureType.getStore().add(new SimpleIdNameModel("2", "На дату"));
    }

    private void initButtonContainer() {
        container = new VerticalLayoutContainer();
        yearBox = initYearComboBox();
        dateRangeWidget = new DateRangeWidget(yearBox);
        dateRangeWidgetContainer = new HorizontalLayoutContainer();
        dateRangeWidgetContainer.add(dateRangeWidget);
        ndsCheck = new CheckBox();
        ndsCheck.setBoxLabel("Включая НДС");
        measureType = WidgetUtils.createCommonFilterModelComboBox("Выберите тип измерения");
        dateField = new DateFieldFullDate();
        FieldLabel measureTypeLabel = createFieldLabelLeft(measureType, "Тип измерения");

        buttonsContainer = new HBoxLayoutContainer();
        buttonsSecondRowContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(5, 5, 0, 0));

//        buttonsContainer.add(ndsCheck, boxLayoutData);
        buttonsContainer.add(yearBox, boxLayoutData);
        buttonsContainer.add(dateRangeWidgetContainer, boxLayoutData);

//        buttonsSecondRowContainer.add(measureType, boxLayoutData);
//        buttonsSecondRowContainer.add(dateField, boxLayoutData);

    }

    private void initGrid() {
        ToolBar toolBarContainer = new ToolBar();
        TextButton addYearButton = new IconButton("Добавить год", "fas fa-calendar-plus-o");
        toolBarContainer.add(addYearButton);
        treeStore = new TreeStore<>(new ModelKeyProvider<PlanFactYear>() {
            @Override
            public String getKey(PlanFactYear item) {
                String key = (item instanceof PlanFactIndicator ? "p-" : "by-") + item.getGid();
                return key;
            }
        });
        treeStore.setAutoCommit(true);

        treeStore.addSortInfo(new Store.StoreSortInfo<>(SortableTreeNode.getComparator(), SortDir.ASC));

        ColumnConfig<PlanFactYear, String> nameOrYear = new ColumnConfig<>(new ValueProvider<PlanFactYear, String>() {
            @Override
            public String getValue(PlanFactYear object) {
                return object.getNameOrYear();
            }

            @Override
            public void setValue(PlanFactYear object, String value) {
                object.setNameOrYear(value);
            }

            @Override
            public String getPath() {
                return "nameOrYear";
            }
        }, 50, "Отчётный период");
        nameOrYear.setMenuDisabled(true);
        nameOrYear.setSortable(false);

        ColumnConfig<PlanFactYear, String> plan = new ColumnConfig<>(new ValueProvider<PlanFactYear, String>() {
            @Override
            public String getValue(PlanFactYear object) {
                return WidgetUtils.doubleToString(object.getPlan());
            }

            @Override
            public void setValue(PlanFactYear object, String value) {
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
        }, 100, "Факт");
        plan.setMenuDisabled(true);
        plan.setSortable(false);

        ColumnModel<PlanFactYear> cm = new ColumnModel<>(Arrays.asList(nameOrYear, plan));
        treeGrid = new TreeGrid<>(treeStore, cm, nameOrYear);
        treeGrid.setExpandOnDoubleClick(false);
        treeGrid.getView().setForceFit(true);
        treeGrid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        treeGrid.getView().setStripeRows(true);
        treeGrid.getView().setViewConfig(new GridViewConfig<PlanFactYear>() {
            @Override
            public String getColStyle(PlanFactYear model, ValueProvider<? super PlanFactYear, ?> valueProvider, int rowIndex, int colIndex) {
                if (model != null) {
                    if (isIndAggregate(model)) {
                        return "investments__aggregate-row";
                    }
                }
                return "";
            }

            @Override
            public String getRowStyle(PlanFactYear model, int rowIndex) {
                return "";
            }
        });

        final GridInlineEditing<PlanFactYear> editing = new GridInlineEditing<PlanFactYear>(treeGrid);
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(plan, WidgetUtils.createEditField());

        editing.addBeforeStartEditHandler(event -> {
            PlanFactYear item = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            if (item.getGid() == 1) {
                event.setCancelled(true);
            }
        });

        editing.addCompleteEditHandler(event -> {
            PlanFactYear editedItem = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            PlanFactYear parent = treeStore.getParent(editedItem);
            if (parent != null) {
                List<PlanFactYear> children = treeStore.getChildren(parent);
//                sum(parent, treeStore.getChildren(parent).toArray(new PlanFactYear[children.size()]));
            }
        });
    }

    private boolean isIndAggregate(PlanFactYear planFactYear) {
        return treeStore.getParent(planFactYear) == null;
    }

    public void sum(PlanFactYear accumulator, PlanFactYear... indicators) {
        accumulator.setPlan(0D);
        for (PlanFactYear indicator : indicators) {
            if (indicator == null) continue;
            if (indicator.getPlan() != null)
                accumulator.setPlan((BigDecimal.valueOf(accumulator.getPlan()).add(BigDecimal.valueOf(indicator.getPlan())).doubleValue()));
        }
        treeStore.update(accumulator);
    }

    /**
     * Id в gid должны соответствовать id справочника в БД.
     */
    protected void setUpBaseIndicators() {

        getYearBox().deselectAll();

        //Поле setOrder установлено для сортировки для полседующего отображения на форме,
        //где можно изменить порядок отображения элементов

        PlanFactIndicator planFactIndicator1 = new PlanFactIndicator();
        planFactIndicator1.setGid(1L);
        planFactIndicator1.setOrder(1L);
        planFactIndicator1.setNameOrYear("Год");
        treeStore.add(planFactIndicator1);
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
            //бежим по показателям в гриде
            for (PlanFactYear rootItem : treeStore.getRootItems()) {
                //бежим по всем годам в гриде
                for (PlanFactYear child : treeStore.getChildren(rootItem)) {
                    //если в комбобоксе не выделен год из грида
                    if (!selectedItems.contains(child.getNameOrYear())) {
                        treeStore.remove(child);
                    }

                }
                //бежим по выделенным годам в комбобоксе
                for (String item : selectedItems) {
                    //если в годах грида нет выделенного комбобокса
                    if (treeStore.getChildren(rootItem).stream().noneMatch(child -> child.getNameOrYear().equals(item))) {
                        PlanFactYear t = new PlanFactYear();
                        t.setGid(getRandId());
                        t.setNameOrYear(item);
                        treeStore.add(rootItem, t);
                    }
                }
            }
        });
        return multiSelectComboBox;
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    private Runnable runnable;

    public void setRunnable(Runnable runnable) {
        this.runnable = runnable;
    }

    public void onValueChange() {
        if (runnable != null) {
            runnable.run();
        }
    }

    public void addRootRowById(Long id) {
        PlanFactIndicator planFactIndicator1 = new PlanFactIndicator();
        planFactIndicator1.setGid(1L);
        planFactIndicator1.setOrder(1L);
        planFactIndicator1.setNameOrYear("Год");


        GWT.log("id" + id + " ");

        if (!treeStore.getRootItems().stream().map(i -> i.getGid()).collect(Collectors.toList()).contains(id)) {
            GWT.log("id" + id + " model found");
            if (1L == id) {
                treeStore.add(planFactIndicator1);
                addCurrentYearsToRootIndicator(planFactIndicator1);
            }
        }
    }

    public void addCurrentYearsToRootIndicator(PlanFactYear root) {
        for (String year : yearBox.getSelectedItems()) {
            PlanFactYear t = new PlanFactYear();
            t.setGid(WidgetUtils.getRandId());
            t.setParent((PlanFactIndicator) root);
            t.setNameOrYear(year);
            treeStore.add(root, t);
        }
    }

    public boolean isRoot(PlanFactYear planFactYear) {
        return treeStore.getParent(planFactYear) == null;
    }

    public TreeStore<PlanFactYear> getTreeStore() {
        return treeStore;
    }

    public void addChildToTreeStore(PlanFactYear parent, PlanFactYear child) {
        child.setParent((PlanFactIndicator) parent);
        treeStore.add(parent, child);
    }
}
