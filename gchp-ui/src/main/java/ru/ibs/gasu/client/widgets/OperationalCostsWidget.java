package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import ru.ibs.gasu.common.models.PlanFactIndicator;
import ru.ibs.gasu.common.models.PlanFactYear;
import ru.ibs.gasu.common.models.SortableTreeNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class OperationalCostsWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private TreeGrid<PlanFactYear> treeGrid;
    private MultiSelectComboBox<String> yearBox;
    private DateRangeWidget dateRangeWidget;
    private HorizontalLayoutContainer dateRangeWidgetContainer;
    private TreeStore<PlanFactYear> treeStore;
    private HBoxLayoutContainer buttonsContainer;

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

    static {
        StyleInjector.inject(".investments__aggregate-row { background-color: rgb(100, 145, 255, 0.2) !important; }");
    }

    public OperationalCostsWidget() {
        initButtonContainer();
        initGrid();
        setUpBaseIndicators();

        yearBox.setWidth(150);
        container.add(buttonsContainer, STD_VC_LAYOUT);
        container.add(treeGrid, STD_VC_LAYOUT);
    }

    public OperationalCostsWidget(boolean hideFact) {
        this();
        if (hideFact) {
            treeGrid.getColumnModel().getColumns().get(2).setHidden(true);
        }
    }

    private void initButtonContainer() {
        container = new VerticalLayoutContainer();
        yearBox = initYearComboBox();
        dateRangeWidget = new DateRangeWidget(yearBox);
        dateRangeWidgetContainer = new HorizontalLayoutContainer();
        dateRangeWidgetContainer.add(dateRangeWidget);

        buttonsContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        buttonsContainer.add(yearBox, boxLayoutData);
        buttonsContainer.add(dateRangeWidgetContainer, boxLayoutData);
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
        }, 150, "Показатель, тыс. руб./Год");
        nameOrYear.setMenuDisabled(true);
        nameOrYear.setSortable(false);

        ColumnConfig<PlanFactYear, String> info = new ColumnConfig<>(new ValueProvider<PlanFactYear, String>() {
            @Override
            public String getValue(PlanFactYear object) {
                return object.getInfo();
            }

            @Override
            public void setValue(PlanFactYear object, String value) {
                object.setInfo(value);
            }

            @Override
            public String getPath() {
                return "info";
            }
        }, 50, "");
        info.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String s, SafeHtmlBuilder sb) {
                if (s != null) {
                    sb.appendHtmlConstant(s);
                }
            }
        });
        info.setMenuDisabled(true);
        info.setSortable(false);

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
        }, 100, "План");
        plan.setMenuDisabled(true);
        plan.setSortable(false);

        ColumnConfig<PlanFactYear, String> fact = new ColumnConfig<>(new ValueProvider<PlanFactYear, String>() {
            @Override
            public String getValue(PlanFactYear object) {
                return WidgetUtils.doubleToString(object.getFact());
            }

            @Override
            public void setValue(PlanFactYear object, String value) {
                GWT.log("fact set");
                if (value == null) {
                    object.setFact(null);
                    onValueChange();
                    return;
                }
                try {
                    object.setFact(Double.parseDouble(value));
                } catch (Exception ex) {
                }
                onValueChange();
            }

            @Override
            public String getPath() {
                return "fact";
            }
        }, 100, "Факт");
        fact.setMenuDisabled(true);
        fact.setSortable(false);

        ColumnModel<PlanFactYear> cm = new ColumnModel<>(Arrays.asList(nameOrYear, info, plan, fact));
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
        editing.addEditor(fact, WidgetUtils.createEditField());

        editing.addBeforeStartEditHandler(event -> {
            PlanFactYear item = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            if (isIndAggregateSum(item))
                event.setCancelled(true);
        });

        editing.addCompleteEditHandler(event -> {
            PlanFactYear editedItem = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            PlanFactYear parent = treeStore.getParent(editedItem);
            if (parent != null) {
                List<PlanFactYear> children = treeStore.getChildren(parent);
                sum(parent, treeStore.getChildren(parent).toArray(new PlanFactYear[children.size()]));
            }

            PlanFactYear totalInd = getInd(13L);
            List<PlanFactYear> indicatorList = new ArrayList<>();
            if (totalInd != null) {
                indicatorList.addAll(treeStore.getRootItems());
                sum(totalInd, indicatorList.toArray(new PlanFactYear[0]));
            }
            for (PlanFactYear totalIndYear : treeStore.getChildren(totalInd)) {
                sum(totalIndYear,
                        getIndYear(14L, totalIndYear.getNameOrYear()),
                        getIndYear(15L, totalIndYear.getNameOrYear()),
                        getIndYear(16L, totalIndYear.getNameOrYear())
                );
            }
        });
    }

    private boolean isIndAggregate(PlanFactYear planFactYear) {
        return LongStream.of(13L).anyMatch(x ->
                planFactYear.getGid() == x ||
                        (treeStore.getParent(planFactYear) != null && treeStore.getParent(planFactYear).getGid() == x)
        );
    }

    private boolean isIndAggregateSum(PlanFactYear planFactYear) {
        if (isIndAggregate(planFactYear)) return true;
        for (PlanFactYear plan : treeStore.getChildren(planFactYear)) {
            if (plan.getPlan() != null && plan.getPlan() >= 0D
                    || (plan.getFact() != null && plan.getFact() >= 0D)
            ) {
                return true;
            }
        }
        return false;
    }

    public void addChildToTreeStore(PlanFactYear parent, PlanFactYear child) {
        child.setParent((PlanFactIndicator) parent);
        treeStore.add(parent, child);
    }

    public void sum(PlanFactYear accumulator, PlanFactYear... indicators) {
        accumulator.setFact(0D);
        accumulator.setPlan(0D);
        for (PlanFactYear indicator : indicators) {
            if (indicator == null) continue;
            if (indicator.getFact() != null)
                accumulator.setFact((BigDecimal.valueOf(accumulator.getFact()).add(BigDecimal.valueOf(indicator.getFact())).doubleValue()));
            if (indicator.getPlan() != null)
                accumulator.setPlan((BigDecimal.valueOf(accumulator.getPlan()).add(BigDecimal.valueOf(indicator.getPlan())).doubleValue()));
        }
        treeStore.update(accumulator);
    }

    public PlanFactYear getInd(Long rootGId) {
        PlanFactYear modelWithKey = treeStore.findModelWithKey("p-" + rootGId);
        if (modelWithKey == null)
            return null;
        return modelWithKey;
    }

    public PlanFactYear getIndYear(Long rootGId, String year) {
        PlanFactYear modelWithKey = treeStore.findModelWithKey("p-" + rootGId);
        if (modelWithKey == null) return null;
        for (PlanFactYear child : treeStore.getChildren(modelWithKey)) {
            if (year.equals(child.getNameOrYear()))
                return child;
        }
        return null;
    }

    /**
     * Id в gid должны соответствовать id справочника в БД.
     */
    protected void setUpBaseIndicators() {

        getYearBox().deselectAll();

        //Поле setOrder установлено для сортировки для полседующего отображения на форме,
        //где можно изменить порядок отображения элементов

        PlanFactIndicator planFactIndicator1 = new PlanFactIndicator();
        planFactIndicator1.setGid(13L);
        planFactIndicator1.setOrder(1L);
        planFactIndicator1.setNameOrYear("Всего расходы концессионера/частного партнера");

        PlanFactIndicator planFactIndicator2 = new PlanFactIndicator();
        planFactIndicator2.setGid(14L);
        planFactIndicator2.setOrder(2L);
        planFactIndicator2.setNameOrYear("Условно-постоянные расходы концессионера/частного партнера");
        planFactIndicator2.setInfo(
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                        "title='Амортизация, аренда, фонд оплаты труда, оплата процентов и комиссий по кредитам и пр.'></i>");

        PlanFactIndicator planFactIndicator3 = new PlanFactIndicator();
        planFactIndicator3.setGid(15L);
        planFactIndicator3.setOrder(3L);
        planFactIndicator3.setNameOrYear("Условно-переменные расходы концессионера/частного партнера");
        planFactIndicator3.setInfo(
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                        "title='Материальные затраты и пр.'></i>");

        PlanFactIndicator planFactIndicator4 = new PlanFactIndicator();
        planFactIndicator4.setGid(16L);
        planFactIndicator4.setOrder(4L);
        planFactIndicator4.setNameOrYear("Капитальный ремонт");

        treeStore.add(planFactIndicator1);
        treeStore.add(planFactIndicator2);
        treeStore.add(planFactIndicator3);
        treeStore.add(planFactIndicator4);
    }

    public TreeStore<PlanFactYear> getTreeStore() {
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

    public void removeRootRowById(Long id) {
        for (PlanFactYear rootItem : treeStore.getRootItems()) {
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

    public Double calcShare() {
        Double res = null;
        for (PlanFactYear rootItem : treeStore.getRootItems()) {
            if (rootItem.getGid() == 13L && rootItem.getFact() != null && rootItem.getPlan() != null) {
                res = rootItem.getFact() / rootItem.getPlan();
                return Math.round(res * 100.0) / 100.0;
            }
        }
        return res;
    }

    public void addRootRowById(Long id) {
        PlanFactIndicator planFactIndicator1 = new PlanFactIndicator();
        planFactIndicator1.setGid(13L);
        planFactIndicator1.setOrder(1L);
        planFactIndicator1.setNameOrYear("Всего расходы концессионера/частного партнера");

        PlanFactIndicator planFactIndicator2 = new PlanFactIndicator();
        planFactIndicator2.setGid(14L);
        planFactIndicator2.setOrder(2L);
        planFactIndicator2.setNameOrYear("Условно-постоянные расходы концессионера/частного партнера");

        PlanFactIndicator planFactIndicator3 = new PlanFactIndicator();
        planFactIndicator3.setGid(15L);
        planFactIndicator3.setOrder(3L);
        planFactIndicator3.setNameOrYear("Условно-переменные расходы концессионера/частного партнера");

        PlanFactIndicator planFactIndicator4 = new PlanFactIndicator();
        planFactIndicator4.setGid(16L);
        planFactIndicator4.setOrder(4L);
        planFactIndicator4.setNameOrYear("Капитальный ремонт ");

        GWT.log("id" + id + " ");

        if (!treeStore.getRootItems().stream().map(i -> i.getGid()).collect(Collectors.toList()).contains(id)) {
            GWT.log("id" + id + " model found");
            if (13L == id) {
                treeStore.add(planFactIndicator1);
                addCurrentYearsToRootIndicator(planFactIndicator1);
            }

            if (14L == id) {
                treeStore.add(planFactIndicator2);
                addCurrentYearsToRootIndicator(planFactIndicator2);
            }

            if (15L == id) {
                treeStore.add(planFactIndicator3);
                addCurrentYearsToRootIndicator(planFactIndicator3);
            }

            if (16L == id) {
                treeStore.add(planFactIndicator4);
                addCurrentYearsToRootIndicator(planFactIndicator4);
            }
        }
    }

    public boolean isNotFilled() {
        for (PlanFactYear rootItem : treeStore.getRootItems()) {
            if (rootItem.getPlan() == null)
                return true;
        }
        return false;
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

    public void toggle(boolean toggle) {
        if (toggle) {
            container.show();
        } else {
            container.hide();
        }
    }

    public boolean isRoot(PlanFactYear planFactYear) {
        return treeStore.getParent(planFactYear) == null;
    }
}
