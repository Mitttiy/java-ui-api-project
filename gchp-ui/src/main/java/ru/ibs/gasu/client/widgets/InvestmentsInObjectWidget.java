package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
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
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
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
import ru.ibs.gasu.client.widgets.componens.ToolbarButton;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.PlanFactIndicator;
import ru.ibs.gasu.common.models.PlanFactYear;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.SortableTreeNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class InvestmentsInObjectWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private TreeGrid<PlanFactYear> treeGrid;
    private MultiSelectComboBox<String> yearBox;
    private DateRangeWidget dateRangeWidget;
    private HorizontalLayoutContainer dateRangeWidgetContainer;
    private TreeStore<PlanFactYear> treeStore;
    private HBoxLayoutContainer buttonsContainer;
    private HBoxLayoutContainer buttonsSecondRowContainer;
    private PlanFactIndicator mainIndicator;
    private TextButton addIndicatorButton;
    private TextButton removeIndicatorButton;
    private TextButton expandButton;
    private TextButton collapseButton;
    private DateField dateField;
    private CheckBox ndsCheck;
    private ComboBox<SimpleIdNameModel> measureType;

    public InvestmentsInObjectWidget() {
        initButtonContainer();
        initGrid();
        setUpBaseIndicators();
        yearBox.setWidth(150);
        container.add(buttonsContainer, STD_VC_LAYOUT);
        container.add(buttonsSecondRowContainer, STD_VC_LAYOUT);
        container.add(treeGrid, STD_VC_LAYOUT);
    }

    public InvestmentsInObjectWidget(boolean hideFact) {
        this();
        if (hideFact) {
            treeGrid.getColumnModel().getColumns().get(2).setHidden(true);
        }
    }

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

    public TextButton getExpandButton() {
        return expandButton;
    }

    public void setExpandButton(TextButton expandButton) {
        this.expandButton = expandButton;
    }

    public TextButton getCollapseButton() {
        return collapseButton;
    }

    public void setCollapseButton(TextButton collapseButton) {
        this.collapseButton = collapseButton;
    }

    public DateField getDateField() {
        return dateField;
    }

    public CheckBox getNdsCheck() {
        return ndsCheck;
    }

    public ComboBox<SimpleIdNameModel> getMeasureType() {
        return measureType;
    }

    static {
        StyleInjector.inject(".investments__aggregate-row { background-color: rgb(100, 145, 255, 0.2) !important; }");
    }

    private void initButtonContainer() {
        container = new VerticalLayoutContainer();
        yearBox = initYearComboBox();
        dateRangeWidget = new DateRangeWidget(yearBox);
        dateRangeWidgetContainer = new HorizontalLayoutContainer();
        dateRangeWidgetContainer.add(dateRangeWidget);
        dateRangeWidgetContainer.disable();
        buttonsContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(5, 5, 5, 0));
        ndsCheck = new CheckBox();
        ndsCheck.setBoxLabel("Включая НДС");
        measureType = WidgetUtils.createCommonFilterModelComboBox("Выберите тип измерения");
        measureType.getStore().add(new SimpleIdNameModel("1", "В ценах соответствующих лет"));
        measureType.getStore().add(new SimpleIdNameModel("2", "На дату"));
        dateField = new DateFieldFullDate();

        buttonsSecondRowContainer = new HBoxLayoutContainer();

        addIndicatorButton = new ToolbarButton("Добавить", "fa-list-ul");
        removeIndicatorButton = new ToolbarButton("Удалить", "fas fa-trash");
        expandButton = new ToolbarButton("Развернуть", "fas fa-expand");
        collapseButton = new ToolbarButton("Свернуть", "fa-compress");

        buttonsContainer.add(addIndicatorButton, boxLayoutData);
        buttonsContainer.add(removeIndicatorButton, boxLayoutData);
        buttonsContainer.add(collapseButton, boxLayoutData);
        buttonsContainer.add(expandButton, boxLayoutData);
        buttonsContainer.add(ndsCheck, boxLayoutData);

        buttonsContainer.add(yearBox, boxLayoutData);
        buttonsContainer.add(dateRangeWidgetContainer, boxLayoutData);

        buttonsSecondRowContainer.add(measureType, boxLayoutData);
        buttonsSecondRowContainer.add(dateField, boxLayoutData);

        addIndicatorButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                PlanFactIndicator object = new PlanFactIndicator();
                PlanFactIndicator objectTotalInvestments = new PlanFactIndicator();
                PlanFactIndicator objectDepreciation = new PlanFactIndicator();
                Long oid = getRandId();
                object.setGid(getRandId());
                object.setOrder(oid);
                object.setNameOrYear("Новый объект");
                objectTotalInvestments.setGid(getRandId());
                objectTotalInvestments.setOrder(++oid);
                objectTotalInvestments.setNameOrYear("Общий объем инвестиций в объект, тыс. руб.");
                objectDepreciation.setGid(getRandId());
                objectDepreciation.setOrder(++oid);
                objectDepreciation.setNameOrYear("Срок амортизации объекта, лет");
                treeStore.add(mainIndicator, object);
                treeStore.add(object, objectTotalInvestments);
                treeStore.add(object, objectDepreciation);

            }
        });

        removeIndicatorButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (treeGrid.getSelectionModel().getSelectedItem() instanceof PlanFactYear) {
                    treeStore.remove(treeGrid.getSelectionModel().getSelectedItem());
                }
            }
        });
        expandButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                treeGrid.expandAll();
            }
        });
        collapseButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                treeGrid.collapseAll();
            }
        });
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
        nameOrYear.setHeader(new SafeHtmlBuilder().appendHtmlConstant("Показатель, тыс. руб./Год " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                "title='Общий размер инвестиций по соглашению (по всем объектам)'></i>").toSafeHtml());

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

        ColumnModel<PlanFactYear> cm = new ColumnModel<>(Arrays.asList(nameOrYear, plan, fact));
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
        treeGrid.getSelectionModel().addSelectionHandler(event -> {
            if (treeStore.getParent(event.getSelectedItem()) == null ||
                    !canGridSelectedItemHasYear(event.getSelectedItem()) || isDeprecationIndicator(event.getSelectedItem())) {
                yearBox.disable();
                dateRangeWidgetContainer.disable();
                yearBox.deselectAll(true);
                yearBox.setText(ClientUtils.getCurrentYear());
            } else {
                List<String> children = treeStore.getChildren(event.getSelectedItem()).stream().map(PlanFactYear::getNameOrYear).collect(Collectors.toList());
                if (!yearBox.isEnabled()) {
                    yearBox.enable();
                    dateRangeWidgetContainer.enable();
                }
                yearBox.deselectAll(true);
                yearBox.select(children, true);
                yearBox.setValuesToTextField();
            }
            if (treeStore.getParent(event.getSelectedItem()) == null) {
                addIndicatorButton.enable();
            } else {
                addIndicatorButton.disable();
            }
            if(event.getSelectedItem().getGid() == 1L || treeStore.getParent(treeStore.getParent(event.getSelectedItem())) != null) {
                removeIndicatorButton.disable();
            } else {
                removeIndicatorButton.enable();
            }
        });
        final GridInlineEditing<PlanFactYear> editing = new GridInlineEditing<PlanFactYear>(treeGrid);
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(nameOrYear, WidgetUtils.createEditTextField());
        editing.addEditor(plan, WidgetUtils.createEditField());
        editing.addEditor(fact, WidgetUtils.createEditField());

        editing.addBeforeStartEditHandler(event -> {
            PlanFactYear item = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            PlanFactYear parent = treeStore.getParent(item);
            if (isRoot(parent) && event.getEditCell().getCol() != 0) {
                event.setCancelled(true);
            }
            if (item.getGid() == 1 || (event.getEditCell().getCol() != 0 && isEditingSumCell(item)) ||
                    treeStore.getParent(treeStore.getParent(item)) != null && event.getEditCell().getCol() == 0) {
                event.setCancelled(true);
            }
        });

        editing.addCompleteEditHandler(event -> {
            PlanFactYear editedItem = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            PlanFactYear parent = treeStore.getParent(editedItem);
            if (parent != null && !isRoot(parent) && !isDeprecationIndicator(editedItem)) {
                if (editedItem.getNameOrYear().equals("Общий объем инвестиций в объект, тыс. руб.")) {
                    sum(parent,editedItem);
                } else {
                    List<PlanFactYear> children = treeStore.getChildren(parent);
                    sum(parent, treeStore.getChildren(parent).toArray(new PlanFactYear[children.size()]));
                    PlanFactYear object = treeStore.getParent(parent);
                    sum(object,parent);
                }
            }

            PlanFactYear totalInd = getInd(1L);
            if (totalInd != null) {
                //в данной конфигурации таблицы корень всегда 1
                List<PlanFactYear> children = treeStore.getChildren(totalInd);
                sum(totalInd,treeStore.getChildren(totalInd).toArray(new PlanFactYear[children.size()]));
            }
        });
    }

    private boolean isIndAggregate(PlanFactYear planFactYear) {
        return LongStream.of(1L).anyMatch(x ->
                planFactYear.getGid() == x ||
                        (planFactYear.getParent() != null && planFactYear.getParent().getGid() == x));
    }

    private boolean isIndAggregateSum(PlanFactYear planFactYear) {
        if (isIndAggregate(planFactYear)) return true;
        for (PlanFactYear plan : treeStore.getChildren(planFactYear)) {
            if (plan.getPlan() != null && plan.getPlan() >= 0D) {
                return true;
            }
        }
        return false;
    }

    private boolean isEditingSumCell(PlanFactYear planFactYear) {
        for (PlanFactYear plan : treeStore.getChildren(planFactYear)) {
            if (plan.getPlan() != null && plan.getPlan() >= 0D) {
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

        mainIndicator = new PlanFactIndicator();
        mainIndicator.setGid(1L);
        mainIndicator.setOrder(1L);
        mainIndicator.setNameOrYear("Всего инвестиции, тыс. руб.");

        treeStore.add(mainIndicator);

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
            if (treeGrid.getSelectionModel().getSelectedItem() != null) {
                //бежим по всем годам в гриде
                for (PlanFactYear child : treeStore.getChildren(treeGrid.getSelectionModel().getSelectedItem())) {
                    //если в комбобоксе не выделен год из грида
                    if (!selectedItems.contains(child.getNameOrYear())) {
                        treeStore.remove(child);
                    }
                }
                //бежим по выделенным годам в комбобоксе
                if(canGridSelectedItemHasYear(treeGrid.getSelectionModel().getSelectedItem())) {
                    for (String item : selectedItems) {
                        //если в годах грида нет выделенного комбобокса
                        if (treeStore.getChildren(treeGrid.getSelectionModel().getSelectedItem()).stream().noneMatch(child -> child.getNameOrYear().equals(item))) {
                            PlanFactYear t = new PlanFactYear();
                            t.setGid(getRandId());
                            t.setNameOrYear(item);
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
            if (rootItem.getGid() == 1L && rootItem.getFact() != null && rootItem.getPlan() != null) {
                res = rootItem.getFact() / rootItem.getPlan();
                return Math.round(res * 100.0) / 100.0;
            }
        }
        return res;
    }

    public void addRootRowById(Long id) {
        mainIndicator = new PlanFactIndicator();
        mainIndicator.setGid(1L);
        mainIndicator.setOrder(1L);
        mainIndicator.setNameOrYear("Всего инвестиции, тыс. руб.");

        treeStore.add(mainIndicator);

        GWT.log("id" + id + " ");

        if (!treeStore.getRootItems().stream().map(i -> i.getGid()).collect(Collectors.toList()).contains(id)) {
            GWT.log("id" + id + " model found");
            if (13L == id) {
                treeStore.add(mainIndicator);
                addCurrentYearsToRootIndicator(mainIndicator);
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

    public boolean canGridSelectedItemHasYear(PlanFactYear planFactYearByYear) {
        return treeStore.getParent(treeStore.getParent(planFactYearByYear)) != null && treeStore.getParent(treeStore.getParent(treeStore.getParent(planFactYearByYear))) == null;
    }

    private boolean isDeprecationIndicator(PlanFactYear planFactYear) {
        return planFactYear.getNameOrYear().equals("Срок амортизации объекта, лет");
    }
}
