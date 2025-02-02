package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
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
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;
import ru.ibs.gasu.client.utils.ClientUtils;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.Circumstance;
import ru.ibs.gasu.common.models.CircumstanceStage;
import ru.ibs.gasu.common.models.SortableTreeNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class LimitCompensationAdditionalCostsWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private TreeGrid<Circumstance> treeGrid;
    private MultiSelectComboBox<String> yearBox;
    private TreeStore<Circumstance> treeStore;
    private HBoxLayoutContainer buttonsContainer;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public void setContainer(VerticalLayoutContainer container) {
        this.container = container;
    }

    public TreeGrid<Circumstance> getTreeGrid() {
        return treeGrid;
    }

    public void setTreeGrid(TreeGrid<Circumstance> treeGrid) {
        this.treeGrid = treeGrid;
    }

    public MultiSelectComboBox<String> getYearBox() {
        return yearBox;
    }

    public void setYearBox(MultiSelectComboBox<String> yearBox) {
        this.yearBox = yearBox;
    }

    public void setTreeStore(TreeStore<Circumstance> treeStore) {
        this.treeStore = treeStore;
    }

    public HBoxLayoutContainer getButtonsContainer() {
        return buttonsContainer;
    }

    public void setButtonsContainer(HBoxLayoutContainer buttonsContainer) {
        this.buttonsContainer = buttonsContainer;
    }

    static {
        StyleInjector.inject(".investments__aggregate-row { background-color: rgb(100, 145, 255, 0.2) !important; }");
    }

    public LimitCompensationAdditionalCostsWidget() {
        initButtonContainer();
        initGrid();
        setUpBaseIndicators();

        container.add(buttonsContainer, STD_VC_LAYOUT);
        container.add(treeGrid, STD_VC_LAYOUT);
    }

    private void initButtonContainer() {
        container = new VerticalLayoutContainer();
        yearBox = initYearComboBox();

        buttonsContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        buttonsContainer.add(yearBox, boxLayoutData);
        yearBox.hide();
    }

    private void initGrid() {
        ToolBar toolBarContainer = new ToolBar();
        TextButton addYearButton = new IconButton("Добавить год", "fas fa-calendar-plus-o");
        toolBarContainer.add(addYearButton);
        treeStore = new TreeStore<>(new ModelKeyProvider<Circumstance>() {
            @Override
            public String getKey(Circumstance item) {
                return "p-" + item.getGid();
            }
        });
        treeStore.setAutoCommit(true);

        treeStore.addSortInfo(new Store.StoreSortInfo<>(SortableTreeNode.getComparator(), SortDir.ASC));

        ColumnConfig<Circumstance, String> nameOrYear = new ColumnConfig<>(new ValueProvider<Circumstance, String>() {
            @Override
            public String getValue(Circumstance object) {
                return object.getName();
            }

            @Override
            public void setValue(Circumstance object, String value) {
                object.setName(value);
            }

            @Override
            public String getPath() {
                return "nameOrYear";
            }
        }, 100, "Наименование");
        nameOrYear.setMenuDisabled(true);
        nameOrYear.setSortable(false);


        ColumnConfig<Circumstance, String> plan = new ColumnConfig<>(new ValueProvider<Circumstance, String>() {
            @Override
            public String getValue(Circumstance object) {
                return WidgetUtils.doubleToString(object.getSum());
            }

            @Override
            public void setValue(Circumstance object, String value) {
                if (value == null) {
                    object.setSum(null);
                    onValueChange();
                    return;
                }
                try {
                    object.setSum(Double.parseDouble(value));
                } catch (Exception ex) {
                }
                onValueChange();
            }

            @Override
            public String getPath() {
                return "plan";
            }
        }, 100, "тыс.руб");
        plan.setMenuDisabled(true);
        plan.setSortable(false);

        ColumnModel<Circumstance> cm = new ColumnModel<>(Arrays.asList(nameOrYear, plan));
        treeGrid = new TreeGrid<>(treeStore, cm, nameOrYear);
        treeGrid.setExpandOnDoubleClick(false);
        treeGrid.getView().setForceFit(true);
        treeGrid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        treeGrid.getView().setStripeRows(true);
        treeGrid.getView().setViewConfig(new GridViewConfig<Circumstance>() {
            @Override
            public String getColStyle(Circumstance model, ValueProvider<? super Circumstance, ?> valueProvider, int rowIndex, int colIndex) {
                if (model != null) {
                    if (isIndAggregate(model)) {
                        return "investments__aggregate-row";
                    }
                }
                return "";
            }

            @Override
            public String getRowStyle(Circumstance model, int rowIndex) {
                return "";
            }
        });
        treeGrid.getSelectionModel().addSelectionHandler(event -> {
            if (isRoot(event.getSelectedItem())) {
                List<String> children = treeStore.getChildren(event.getSelectedItem()).stream().map(Circumstance::getName).collect(Collectors.toList());
                if (!yearBox.isEnabled()) yearBox.enable();
                yearBox.deselectAll(true);
                yearBox.select(children, true);
            } else {
                yearBox.disable();
                yearBox.deselectAll(true);
            }
            yearBox.setValuesToTextField();
        });
        final GridInlineEditing<Circumstance> editing = new GridInlineEditing<Circumstance>(treeGrid);
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(plan, WidgetUtils.createEditField());

        editing.addBeforeStartEditHandler(event -> {
            Circumstance item = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            if (isIndAggregateSum(item))
                event.setCancelled(false);

        });

        editing.addCompleteEditHandler(event -> {
            Circumstance editedItem = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            Circumstance parent = treeStore.getParent(editedItem);
            if (parent != null) {
                List<Circumstance> children = treeStore.getChildren(parent);
                sum(parent, treeStore.getChildren(parent).toArray(new Circumstance[children.size()]));
            }

            Circumstance totalInd = getInd(1L);
            List<Circumstance> indicatorList = new ArrayList<>();
            if (totalInd != null) {
                for (Circumstance ind : treeStore.getRootItems()) {
                    indicatorList.add(ind);
                }
                sum(totalInd, indicatorList.toArray(new Circumstance[0]));
            }

            for (Circumstance totalIndYear : treeStore.getChildren(totalInd)) {
                List<Circumstance> totalIndYearList = new ArrayList<>();
                for (Circumstance pfy : indicatorList) {
                    if (totalIndYear.getName().equals(pfy.getName())) totalIndYearList.add(pfy);
                }
                sum(totalIndYear, totalIndYearList.toArray(new Circumstance[totalIndYearList.size()]));
            }
        });
    }

    private boolean isIndAggregate(Circumstance Circumstance) {
        return LongStream.of(1L).anyMatch(x ->
                Circumstance.getGid() == x
        );
    }

    private boolean isIndAggregateSum(Circumstance selectedItem) {
        if (selectedItem.getGid() != 1) {
            return true;
        } else {
            for (Circumstance plan : treeStore.getRootItems()) {
                if ((plan.getGid() != 1 && plan.getSum() != null && plan.getSum() >= 0D)) {
                    return false;
                }
            }
        }
        return false;
    }

//    public void addChildToTreeStore(Circumstance parent, Circumstance child) {
//        child.setParent((CircumstanceStage) parent);
//        treeStore.add(parent, child);
//    }

    public void sum(Circumstance accumulator, Circumstance... indicators) {
        accumulator.setSum(0D);
        for (Circumstance indicator : indicators) {
            if (indicator == null) continue;
            if (indicator.getSum() != null)
                accumulator.setSum((BigDecimal.valueOf(accumulator.getSum()).add(BigDecimal.valueOf(indicator.getSum())).doubleValue()));
        }
        treeStore.update(accumulator);
    }

    public Circumstance getInd(Long rootGId) {
        Circumstance modelWithKey = treeStore.findModelWithKey("p-" + rootGId);
        if (modelWithKey == null)
            return null;
        return modelWithKey;
    }

    public Circumstance getIndYear(Long rootGId, String year) {
        Circumstance modelWithKey = treeStore.findModelWithKey("p-" + rootGId);
        if (modelWithKey == null) return null;
        for (Circumstance child : treeStore.getChildren(modelWithKey)) {
            if (year.equals(child.getName()))
                return child;
        }
        return null;
    }

    /**
     * Id в gid должны соответствовать id справочника в БД.
     */
    protected void setUpBaseIndicators() {

        getYearBox().deselectAll();

        CircumstanceStage CircumstanceStage1 = new CircumstanceStage();
        CircumstanceStage1.setGid(1L);
        CircumstanceStage1.setOrder(1L);
        CircumstanceStage1.setName("Всего");

        CircumstanceStage CircumstanceStage2 = new CircumstanceStage();
        CircumstanceStage2.setGid(2L);
        CircumstanceStage2.setOrder(2L);
        CircumstanceStage2.setName("На подготовительном этапе");

        CircumstanceStage CircumstanceStage3 = new CircumstanceStage();
        CircumstanceStage3.setGid(3L);
        CircumstanceStage3.setOrder(3L);
        CircumstanceStage3.setName("На этапе строительства");

        CircumstanceStage CircumstanceStage4 = new CircumstanceStage();
        CircumstanceStage4.setGid(4L);
        CircumstanceStage4.setOrder(4L);
        CircumstanceStage4.setName("На этапе эксплуатации");

        treeStore.add(CircumstanceStage1);
        treeStore.add(CircumstanceStage2);
        treeStore.add(CircumstanceStage3);
        treeStore.add(CircumstanceStage4);
    }

    public TreeStore<Circumstance> getTreeStore() {
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
            if (treeGrid.getSelectionModel().getSelectedItem() != null) {
                //бежим по всем годам в гриде
                for (Circumstance child : treeStore.getChildren(treeGrid.getSelectionModel().getSelectedItem())) {
                    //если в комбобоксе не выделен год из грида
                    if (!selectedItems.contains(child.getName())) {
                        treeStore.remove(child);
                    }
                }
                //бежим по выделенным годам в комбобоксе
                if (isRoot(treeGrid.getSelectionModel().getSelectedItem())) {
                    for (String item : selectedItems) {
                        //если в годах грида нет выделенного комбобокса
                        if (treeStore.getChildren(treeGrid.getSelectionModel().getSelectedItem()).stream().noneMatch(child -> child.getName().equals(item))) {
                            Circumstance t = new Circumstance();
                            t.setGid(getRandId());
                            t.setName(item);
                            treeStore.add(treeGrid.getSelectionModel().getSelectedItem(), t);
                        }
                    }
                }
            }
        });
        multiSelectComboBox.disable();
        return multiSelectComboBox;
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    public void removeRootRowById(Long id) {
        for (Circumstance rootItem : treeStore.getRootItems()) {
            if (rootItem.getGid().equals(id)) {
                treeStore.remove(rootItem);
            }
        }
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
        CircumstanceStage CircumstanceStage1 = new CircumstanceStage();
        CircumstanceStage1.setGid(1L);
        CircumstanceStage1.setOrder(1L);
        CircumstanceStage1.setName("Всего");

        CircumstanceStage CircumstanceStage2 = new CircumstanceStage();
        CircumstanceStage2.setGid(2L);
        CircumstanceStage2.setOrder(2L);
        CircumstanceStage2.setName("На подготовительном этапе");

        CircumstanceStage CircumstanceStage3 = new CircumstanceStage();
        CircumstanceStage3.setGid(3L);
        CircumstanceStage3.setOrder(3L);
        CircumstanceStage3.setName("На этапе строительства");

        CircumstanceStage CircumstanceStage4 = new CircumstanceStage();
        CircumstanceStage4.setGid(4L);
        CircumstanceStage4.setOrder(4L);
        CircumstanceStage4.setName("На этапе эксплуатации");

        treeStore.add(CircumstanceStage1);
        treeStore.add(CircumstanceStage2);
        treeStore.add(CircumstanceStage3);
        treeStore.add(CircumstanceStage4);

        GWT.log("id" + id + " ");

        if (!treeStore.getRootItems().stream().map(i -> i.getGid()).collect(Collectors.toList()).contains(id)) {
            GWT.log("id" + id + " model found");
            if (1L == id) {
                treeStore.add(CircumstanceStage1);
                addCurrentYearsToRootIndicator(CircumstanceStage1);
            }

            if (2L == id) {
                treeStore.add(CircumstanceStage2);
                addCurrentYearsToRootIndicator(CircumstanceStage2);
            }

            if (3L == id) {
                treeStore.add(CircumstanceStage3);
                addCurrentYearsToRootIndicator(CircumstanceStage3);
            }

            if (4L == id) {
                treeStore.add(CircumstanceStage4);
                addCurrentYearsToRootIndicator(CircumstanceStage4);
            }
        }
    }

    public boolean isNotFilled() {
        for (Circumstance rootItem : treeStore.getRootItems()) {
            if (rootItem.getSum() == null)
                return true;
        }
        return false;
    }

    public void addCurrentYearsToRootIndicator(Circumstance root) {
        for (String year : yearBox.getSelectedItems()) {
            Circumstance t = new Circumstance();
            t.setGid(WidgetUtils.getRandId());
            t.setParent((CircumstanceStage) root);
            t.setName(year);
            treeStore.add(root, t);
        }
    }

    public void toggle(boolean toggle) {
        if (toggle) {
            container.show();
        } else {
            container.hide();
        }
    }

    public boolean isRoot(Circumstance Circumstance) {
        return treeStore.getParent(Circumstance) == null;
    }
}
