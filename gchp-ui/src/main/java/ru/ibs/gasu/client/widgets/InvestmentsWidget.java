package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.*;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
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

public class InvestmentsWidget implements IsWidget {

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
    private TextButton expandButton;
    private TextButton collapseButton;
    private DateField dateField;
    private PlanFactIndicator planFactIndicator2;
    private PlanFactIndicator planFactIndicator3;
    private PlanFactIndicator planFactIndicator7;
    private PlanFactIndicator planFactIndicator8;
    private MultiSelectComboBox<String> multiSelectComboBox;
    private List<PlanFactYear> rootItems;

    public List<PlanFactYear> getRootItems() {
        return rootItems;
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

    public void setDateField(DateField dateField) {
        this.dateField = dateField;
    }

    static {
        StyleInjector.inject(".investments__aggregate-row { background-color: rgb(100, 145, 255, 0.2) !important; }");
    }

    public InvestmentsWidget() {
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

    public InvestmentsWidget(boolean projectPreparationView) {
        this();
        if (projectPreparationView) {
            treeGrid.getColumnModel().getColumns().get(4).setHidden(true);
            treeStore.remove(planFactIndicator7);
            treeStore.remove(planFactIndicator8);
            rootItems.removeAll(Arrays.asList(planFactIndicator7,planFactIndicator8));
            planFactIndicator3.setNameOrYear("Объем бюджетных расходов");
        }
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
//        dateRangeWidgetContainer.disable();
        ndsCheck = new CheckBox();
        ndsCheck.setBoxLabel("Включая НДС");
        measureType = WidgetUtils.createCommonFilterModelComboBox("Выберите тип измерения");
        dateField = new DateFieldFullDate();
        FieldLabel measureTypeLabel = createFieldLabelLeft(measureType, "Тип измерения");

        buttonsContainer = new HBoxLayoutContainer();
        buttonsSecondRowContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(5, 5, 0, 0));

        expandButton = new ToolbarButton("Развернуть", "fas fa-expand");
        collapseButton = new ToolbarButton("Свернуть", "fa-compress");

        buttonsContainer.add(collapseButton, boxLayoutData);
        buttonsContainer.add(expandButton, boxLayoutData);
        buttonsContainer.add(ndsCheck, boxLayoutData);
        buttonsContainer.add(yearBox, boxLayoutData);
        buttonsContainer.add(dateRangeWidgetContainer, boxLayoutData);

        buttonsSecondRowContainer.add(measureType, boxLayoutData);
        buttonsSecondRowContainer.add(dateField, boxLayoutData);

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
        }, 200, "Показатель, тыс. руб./Год");
        nameOrYear.setMenuDisabled(true);
        nameOrYear.setSortable(false);

        ColumnConfig<PlanFactYear, String> deleteButton = new ColumnConfig<>(new ValueProvider<PlanFactYear, String>() {
            @Override
            public String getValue(PlanFactYear object) {
                if (isRoot(object)) {
                    return "";
                } else {
                    return object.getDeleteButton();
                }
            }

            @Override
            public void setValue(PlanFactYear object, String value) { }

            @Override
            public String getPath() {
                return "deleteButton";
            }
        }, 13, "");
        deleteButton.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String s, SafeHtmlBuilder sb) {
                if (s != null) {
                    sb.appendHtmlConstant(s);
                }
            }
        });
        deleteButton.setMenuDisabled(true);
        deleteButton.setSortable(false);

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
        plan.setHeader(new SafeHtmlBuilder().appendHtmlConstant("План " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                "title='Финансовые показатели указываются в тысячах рублей'></i>").toSafeHtml());

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

        ColumnModel<PlanFactYear> cm = new ColumnModel<>(Arrays.asList(nameOrYear, deleteButton, info, plan, fact));
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
//        treeGrid.getSelectionModel().addSelectionHandler(event -> {
//            if (isRoot(event.getSelectedItem())) {
//                List<String> children = treeStore.getChildren(event.getSelectedItem()).stream().map(PlanFactYear::getNameOrYear).collect(Collectors.toList());
//                if (!yearBox.isEnabled()) {
//                    yearBox.enable();
//                    dateRangeWidgetContainer.enable();
//                }
//                yearBox.deselectAll(true);
//                yearBox.select(children, true);
//            } else {
////                yearBox.disable();
////                dateRangeWidgetContainer.disable();
//                yearBox.deselectAll(true);
//            }
//            yearBox.setValuesToTextField();
//        });
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

            PlanFactYear totalInd = getInd(1L);
            PlanFactYear privateInd = getInd(2L);
            PlanFactYear budgetInd = getInd(3L);
            if (budgetInd != null) {
                sum(budgetInd,
                        getInd(7L),
                        getInd(8L),
                        getInd(9L),
                        getInd(12L)
                );

            }

            if (privateInd != null) {
                if (!treeStore.getAll().contains(getInd(10L)) && !treeStore.getAll().contains(getInd(11L))) {
                    treeStore.update(privateInd);
                } else {
                    sum(privateInd,
                            getInd(10L),
                            getInd(11L)
                    );

                }
            }

            if (totalInd != null) {
                sum(totalInd,
                        privateInd,
                        budgetInd
                );
                if (!treeStore.getAll().contains(getInd(10L)) && !treeStore.getAll().contains(getInd(11L))) {
                    for (PlanFactYear totalIndYear : treeStore.getChildren(totalInd)) {
                        sum(totalIndYear,
                                getIndYear(2L, totalIndYear.getNameOrYear()),
                                getIndYear(7L, totalIndYear.getNameOrYear()),
                                getIndYear(8L, totalIndYear.getNameOrYear()),
                                getIndYear(9L, totalIndYear.getNameOrYear()),
                                getIndYear(12L, totalIndYear.getNameOrYear())
                        );
                    }
                } else {
                    for (PlanFactYear totalIndYear : treeStore.getChildren(totalInd)) {
                        sum(totalIndYear,
                                getIndYear(10L, totalIndYear.getNameOrYear()),
                                getIndYear(11L, totalIndYear.getNameOrYear()),
                                getIndYear(7L, totalIndYear.getNameOrYear()),
                                getIndYear(8L, totalIndYear.getNameOrYear()),
                                getIndYear(9L, totalIndYear.getNameOrYear()),
                                getIndYear(12L, totalIndYear.getNameOrYear())
                        );
                    }
                }
            }
        });

        treeGrid.addRowClickHandler(event -> {
            if (event.getColumnIndex() == 1) {
                if (!isRoot(treeGrid.getSelectionModel().getSelectedItem())) {
                    if (treeGrid.getSelectionModel() != null && treeGrid.getSelectionModel().getSelectedItem() != null) {
                        String year = treeGrid.getSelectionModel().getSelectedItem().getNameOrYear();
                        ConfirmMessageBox confirmMessageBox = new ConfirmMessageBox("Удалить год", "Вы действительно хотите удалить " + year + " год? Удаление произойдёт во всех показателях");
                        confirmMessageBox.addDialogHideHandler(confirmEvent -> {
                            if (confirmEvent.getHideButton() == Dialog.PredefinedButton.YES) {
                                multiSelectComboBox.deselect(year);
                            }
                        });
                        confirmMessageBox.show();
                    }
                }
            }
        });
    }

    private boolean isIndAggregate(PlanFactYear planFactYear) {
        return LongStream.of(1L).anyMatch(x ->
                planFactYear.getGid() == x ||
                        (treeStore.getParent(planFactYear) != null && treeStore.getParent(planFactYear).getGid() == x)
        );
    }

    private boolean isIndAggregateSum(PlanFactYear planFactYear) {
        if (isIndAggregate(planFactYear)) return true;
        for (PlanFactYear plan : treeStore.getChildren(planFactYear)) {
            if ((plan.getPlan() != null && plan.getPlan() >= 0D)
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
        planFactIndicator1.setGid(1L);
        planFactIndicator1.setOrder(1L);
        planFactIndicator1.setNameOrYear("Общий объем инвестиций, тыс. руб.");

        planFactIndicator2 = new PlanFactIndicator();
        planFactIndicator2.setGid(2L);
        planFactIndicator2.setOrder(2L);
        planFactIndicator2.setNameOrYear("Объем частных инвестиций, тыс. руб.");

        planFactIndicator3 = new PlanFactIndicator();
        planFactIndicator3.setGid(3L);
        planFactIndicator3.setOrder(5L);
        planFactIndicator3.setNameOrYear("Объем бюджетных расходов ");

        PlanFactIndicator planFactIndicator4 = new PlanFactIndicator();
        planFactIndicator4.setGid(7L);
        planFactIndicator4.setOrder(6L);
        planFactIndicator4.setNameOrYear("За счет средств федерального бюджета");

        PlanFactIndicator planFactIndicator5 = new PlanFactIndicator();
        planFactIndicator5.setGid(8L);
        planFactIndicator5.setOrder(7L);
        planFactIndicator5.setNameOrYear("За счет средств регионального бюджета");

        PlanFactIndicator planFactIndicator6 = new PlanFactIndicator();
        planFactIndicator6.setGid(9L);
        planFactIndicator6.setOrder(8L);
        planFactIndicator6.setNameOrYear("За счет средств местного бюджета");

        planFactIndicator7 = new PlanFactIndicator();
        planFactIndicator7.setGid(10L);
        planFactIndicator7.setOrder(3L);
        planFactIndicator7.setNameOrYear("Собственные инвестиции");
        planFactIndicator7.setInfo(
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                        "title='Включаются займы акционеров/участников общества'></i>");

        planFactIndicator8 = new PlanFactIndicator();
        planFactIndicator8.setGid(11L);
        planFactIndicator8.setOrder(4L);
        planFactIndicator8.setNameOrYear("Заемные средства");
        planFactIndicator8.setInfo(
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                        "title='Средства, предоставленные финансовыми организациями/средства от выпуска облигаций'></i>");

        PlanFactIndicator planFactIndicator9 = new PlanFactIndicator();
        planFactIndicator9.setGid(12L);
        planFactIndicator9.setOrder(9L);
        planFactIndicator9.setNameOrYear("За счет иных источников");
        planFactIndicator9.setInfo(
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                        "title='Иные источники, формируемые за счёт бюджетных ассигнований (например, Фонд содействия реформированию ЖКХ и пр.)'></i>");

        List<PlanFactYear> privateInvestment = Arrays.asList(planFactIndicator7, planFactIndicator8);
        List<PlanFactYear> budgetInvestments = Arrays.asList(planFactIndicator4, planFactIndicator5, planFactIndicator6, planFactIndicator9);

        treeStore.add(planFactIndicator1);
        treeStore.add(planFactIndicator2);
        for (PlanFactYear investment : privateInvestment){
            addChildToTreeStore(planFactIndicator2,investment);
        }
        treeStore.add(planFactIndicator3);
        for (PlanFactYear investment : budgetInvestments){
            addChildToTreeStore(planFactIndicator3,investment);
        }

        rootItems = new ArrayList<>();
        rootItems.add(planFactIndicator1);
        rootItems.add(planFactIndicator2);
        rootItems.addAll(privateInvestment);
        rootItems.add(planFactIndicator3);
        rootItems.addAll(budgetInvestments);
    }

    public TreeStore<PlanFactYear> getTreeStore() {
        return treeStore;
    }

    private MultiSelectComboBox<String> initYearComboBox() {
        multiSelectComboBox = new MultiSelectComboBox<>(
                x -> x,
                x -> x,
                null,
                false);
        multiSelectComboBox.setEmptyText(ClientUtils.getCurrentYear());
        multiSelectComboBox.setEditable(false);
        multiSelectComboBox.addAll(ClientUtils.generateComboBoxYearValues());
        multiSelectComboBox.addSelectionHandler(selectedItems -> {
            //бежим по показателям в гриде
            for (PlanFactYear rootItem : rootItems) {
                //бежим по всем годам в гриде
                if ((rootItems.contains(planFactIndicator7) && !rootItem.getGid().equals(3L) && !rootItem.getGid().equals(2L))
                    ||(!rootItems.contains(planFactIndicator7) && !rootItem.getGid().equals(3L))){
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
            if (rootItem.getGid() == 1L && rootItem.getFact() != null && rootItem.getPlan() != null) {
                res = rootItem.getFact() / rootItem.getPlan();
                return Math.round(res * 100.0) / 100.0;
            }
        }
        return res;
    }

    public void addRootRowById(Long id) {
        PlanFactIndicator planFactIndicator1 = new PlanFactIndicator();
        planFactIndicator1.setGid(1L);
        planFactIndicator1.setOrder(1L);
        planFactIndicator1.setNameOrYear("Общий объем инвестиций");

        PlanFactIndicator planFactIndicator2 = new PlanFactIndicator();
        planFactIndicator2.setGid(2L);
        planFactIndicator2.setOrder(2L);
        planFactIndicator2.setNameOrYear("Объем частных инвестиций");

        PlanFactIndicator planFactIndicator3 = new PlanFactIndicator();
        planFactIndicator3.setGid(3L);
        planFactIndicator3.setOrder(4L);
        planFactIndicator3.setNameOrYear("Объем бюджетных расходов");

        PlanFactIndicator planFactIndicator4 = new PlanFactIndicator();
        planFactIndicator4.setGid(7L);
        planFactIndicator4.setOrder(6L);
        planFactIndicator4.setNameOrYear("За счет средств федерального бюджета");

        PlanFactIndicator planFactIndicator5 = new PlanFactIndicator();
        planFactIndicator5.setGid(8L);
        planFactIndicator5.setOrder(7L);
        planFactIndicator5.setNameOrYear("За счет средств регионального бюджета");

        PlanFactIndicator planFactIndicator6 = new PlanFactIndicator();
        planFactIndicator6.setGid(9L);
        planFactIndicator6.setOrder(8L);
        planFactIndicator6.setNameOrYear("За счет средств местного бюджета");

        PlanFactIndicator planFactIndicator7 = new PlanFactIndicator();
        planFactIndicator7.setGid(10L);
        planFactIndicator7.setOrder(3L);
        planFactIndicator7.setNameOrYear("Собственные инвестиции");

        PlanFactIndicator planFactIndicator8 = new PlanFactIndicator();
        planFactIndicator8.setGid(11L);
        planFactIndicator8.setOrder(5L);
        planFactIndicator8.setNameOrYear("Заемные средства");

        PlanFactIndicator planFactIndicator9 = new PlanFactIndicator();
        planFactIndicator9.setGid(12L);
        planFactIndicator9.setOrder(9L);
        planFactIndicator9.setNameOrYear("За счет иных источников");

        GWT.log("id" + id + " ");

        if (!rootItems.stream().map(i -> i.getGid()).collect(Collectors.toList()).contains(id)) {
            GWT.log("id" + id + " model found");
            if (1L == id) {
                treeStore.add(planFactIndicator1);
                addCurrentYearsToRootIndicator(planFactIndicator1);
            }

            if (2L == id) {
                treeStore.add(planFactIndicator2);
                addCurrentYearsToRootIndicator(planFactIndicator2);
            }

            if (3L == id) {
                treeStore.add(planFactIndicator3);
                addCurrentYearsToRootIndicator(planFactIndicator3);
            }

            if (7L == id) {
                treeStore.add(planFactIndicator4);
                addCurrentYearsToRootIndicator(planFactIndicator4);
            }

            if (8L == id) {
                treeStore.add(planFactIndicator5);
                addCurrentYearsToRootIndicator(planFactIndicator5);
            }

            if (9L == id) {
                treeStore.add(planFactIndicator6);
                addCurrentYearsToRootIndicator(planFactIndicator6);
            }

            if (10L == id) {
                treeStore.add(planFactIndicator7);
                addCurrentYearsToRootIndicator(planFactIndicator7);
            }

            if (11L == id) {
                treeStore.add(planFactIndicator8);
                addCurrentYearsToRootIndicator(planFactIndicator8);
            }

            if (12L == id) {
                treeStore.add(planFactIndicator9);
                addCurrentYearsToRootIndicator(planFactIndicator9);
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
        return treeStore.getParent(planFactYear) == null || rootItems.contains(planFactYear);
    }
}
