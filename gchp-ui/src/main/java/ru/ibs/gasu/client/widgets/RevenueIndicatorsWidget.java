package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
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
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;
import ru.ibs.gasu.client.utils.ClientUtils;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.client.widgets.componens.ToolbarButton;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.RevenueByYear;
import ru.ibs.gasu.common.models.RevenueMain;
import ru.ibs.gasu.common.models.SortableTreeNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class RevenueIndicatorsWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private TreeGrid<RevenueByYear> treeGrid;
    private MultiSelectComboBox<String> yearBox;
    private DateRangeWidget dateRangeWidget;
    private HorizontalLayoutContainer dateRangeWidgetContainer;
    private TreeStore<RevenueByYear> treeStore;
    private HBoxLayoutContainer buttonsContainer;
    private TextButton addIndicatorButton;
    private TextButton removeIndicatorButton;
    private TextButton expandButton;
    private TextButton collapseButton;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public void setContainer(VerticalLayoutContainer container) {
        this.container = container;
    }

    public TreeGrid<RevenueByYear> getTreeGrid() {
        return treeGrid;
    }

    public void setTreeGrid(TreeGrid<RevenueByYear> treeGrid) {
        this.treeGrid = treeGrid;
    }

    public MultiSelectComboBox<String> getYearBox() {
        return yearBox;
    }

    public void setYearBox(MultiSelectComboBox<String> yearBox) {
        this.yearBox = yearBox;
    }

    public void setTreeStore(TreeStore<RevenueByYear> treeStore) {
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

    static {
        StyleInjector.inject(".investments__aggregate-row { background-color: rgb(100, 145, 255, 0.2) !important; }");
    }

    public RevenueIndicatorsWidget() {
        initButtonContainer();
        initGrid();
        container.add(buttonsContainer, STD_VC_LAYOUT);
        container.add(treeGrid, STD_VC_LAYOUT);
    }

    public RevenueIndicatorsWidget(boolean hideFact) {
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
        dateRangeWidgetContainer.disable();
        buttonsContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));


        addIndicatorButton = new ToolbarButton("Добавить", "fa-list-ul");
        removeIndicatorButton = new ToolbarButton("Удалить", "fas fa-trash");
        expandButton = new ToolbarButton("Развернуть", "fas fa-expand");
        collapseButton = new ToolbarButton("Свернуть", "fa-compress");

        buttonsContainer.add(addIndicatorButton, boxLayoutData);
        buttonsContainer.add(removeIndicatorButton, boxLayoutData);
        buttonsContainer.add(collapseButton, boxLayoutData);
        buttonsContainer.add(expandButton, boxLayoutData);

        buttonsContainer.add(yearBox, boxLayoutData);
        buttonsContainer.add(dateRangeWidgetContainer, boxLayoutData);

        addIndicatorButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                RevenueByYear object = new RevenueByYear();
                RevenueByYear volumeOfServices = new RevenueByYear();
                RevenueByYear revenueOfService = new RevenueByYear();
                object.setGid(getRandId());
                object.setName("Наименование услуги");
                volumeOfServices.setGid(getRandId());
                volumeOfServices.setName("Объем оказываемой услуги");
                volumeOfServices.setInfo(
                        "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                                "title='В натуральном выражении '></i>");
                volumeOfServices.setUm("");
                revenueOfService.setGid(getRandId());
                revenueOfService.setName("Выручка от оказания услуги");
                revenueOfService.setInfo(
                        "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                                "title='В денежном выражении (тыс.руб.)'></i>");
                revenueOfService.setUm("тыс. руб.");
                treeStore.add(object);
                treeStore.add(object, volumeOfServices);
                treeStore.add(object, revenueOfService);
            }
        });

        removeIndicatorButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (treeGrid.getSelectionModel().getSelectedItem() instanceof RevenueByYear) {
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
        treeStore = new TreeStore<>(new ModelKeyProvider<RevenueByYear>() {
            @Override
            public String getKey(RevenueByYear item) {
                String key = (item instanceof RevenueMain ? "p-" : "by-") + item.getGid();
                return key;
            }
        });
        treeStore.setAutoCommit(true);

        treeStore.addSortInfo(new Store.StoreSortInfo<>(SortableTreeNode.getComparator(), SortDir.ASC));

        ColumnConfig<RevenueByYear, String> nameOrYear = new ColumnConfig<>(new ValueProvider<RevenueByYear, String>() {
            @Override
            public String getValue(RevenueByYear object) {
                return object.getName();
            }

            @Override
            public void setValue(RevenueByYear object, String value) {
                object.setName(value);
            }

            @Override
            public String getPath() {
                return "nameOrYear";
            }
        }, 65, "Показатель");
        nameOrYear.setMenuDisabled(true);
        nameOrYear.setSortable(false);

        ColumnConfig<RevenueByYear, String> info = new ColumnConfig<>(new ValueProvider<RevenueByYear, String>() {
            @Override
            public String getValue(RevenueByYear object) {
                return object.getInfo();
            }

            @Override
            public void setValue(RevenueByYear object, String value) {
                object.setInfo(value);
            }

            @Override
            public String getPath() {
                return "info";
            }
        }, 55, "");
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

        ColumnConfig<RevenueByYear, String> um = new ColumnConfig<>(new ValueProvider<RevenueByYear, String>() {
            @Override
            public String getValue(RevenueByYear object) {
                if(object.getUm() != null) {
                    return object.getUm();
                } else {
                    return "";
                }
            }

            @Override
            public void setValue(RevenueByYear object, String value) {
                    object.setUm(value);
            }

            @Override
            public String getPath() {
                return "um";
            }
        }, 30, "Ед. измерения");
        um.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant(value);
            }
        });

        ColumnConfig<RevenueByYear, String> plan = new ColumnConfig<>(new ValueProvider<RevenueByYear, String>() {
            @Override
            public String getValue(RevenueByYear object) {
                return WidgetUtils.doubleToString(object.getPlan());
            }

            @Override
            public void setValue(RevenueByYear object, String value) {
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

        ColumnConfig<RevenueByYear, String> fact = new ColumnConfig<>(new ValueProvider<RevenueByYear, String>() {
            @Override
            public String getValue(RevenueByYear object) {
                return WidgetUtils.doubleToString(object.getFact());
            }

            @Override
            public void setValue(RevenueByYear object, String value) {
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

        ColumnModel<RevenueByYear> cm = new ColumnModel<>(Arrays.asList(nameOrYear, info, um, plan, fact));
        treeGrid = new TreeGrid<>(treeStore, cm, nameOrYear);
        treeGrid.setExpandOnDoubleClick(false);
        treeGrid.getView().setForceFit(true);
        treeGrid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        treeGrid.getView().setStripeRows(true);
        treeGrid.getSelectionModel().addSelectionHandler(event -> {
            if (treeStore.getParent(event.getSelectedItem()) == null ||
                    !canGridSelectedItemHasYear(event.getSelectedItem()) || isDeprecationIndicator(event.getSelectedItem())) {
                yearBox.disable();
                dateRangeWidgetContainer.disable();
                yearBox.deselectAll(true);
                yearBox.setText(ClientUtils.getCurrentYear());
            } else {
                List<String> children = treeStore.getChildren(event.getSelectedItem()).stream().map(RevenueByYear::getName).collect(Collectors.toList());
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
            if(event.getSelectedItem().getGid() == 13L) {
                removeIndicatorButton.disable();
            } else {
                removeIndicatorButton.enable();
            }
        });
        final GridInlineEditing<RevenueByYear> editing = new GridInlineEditing<RevenueByYear>(treeGrid);
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(um, WidgetUtils.createEditTextField());
        editing.addEditor(nameOrYear, WidgetUtils.createEditTextField());
        editing.addEditor(plan, WidgetUtils.createEditField());
        editing.addEditor(fact, WidgetUtils.createEditField());

        editing.addBeforeStartEditHandler(event -> {
            RevenueByYear item = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            if (item.getGid() == 13
                    || (event.getEditCell().getCol() != 0 && isEditingSumCell(item))
                    || (treeStore.getChildren(item).size() != 0 && event.getEditCell().getCol() != 0)
                    || treeStore.getParent(item) != null && event.getEditCell().getCol() == 0) {
                event.setCancelled(true);
            }
            if(event.getEditCell().getCol() == 2) {
                if (item.getName().equals("Объем оказываемой услуги")) {
                    event.setCancelled(false);
                } else {
                    event.setCancelled(true);
                }
            }
        });

        editing.addCompleteEditHandler(event -> {
            RevenueByYear editedItem = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            if(event.getEditCell().getCol() == 2){
                for (RevenueByYear year : treeStore.getChildren(editedItem)){
                    year.setUm(editedItem.getUm());
                }
                treeGrid.getView().refresh(false);
            }
            RevenueByYear parent = treeStore.getParent(editedItem);
            if (parent != null && !isRoot(parent)) {
                List<RevenueByYear> children = treeStore.getChildren(parent);
                sum(parent, treeStore.getChildren(parent).toArray(new RevenueByYear[children.size()]));
            }

            RevenueByYear totalInd = getInd(13L);
            List<RevenueByYear> objectInvestmentsList = new ArrayList<>();
            if (totalInd != null) {
                for (RevenueByYear rootInd : treeStore.getRootItems()) {
                    for(RevenueByYear child : treeStore.getChildren(rootInd)) {
                        if (!isDeprecationIndicator(child))
                            objectInvestmentsList.add(child);
                    }
                }
                sum(totalInd, objectInvestmentsList.toArray(new RevenueByYear[0]));
            }
        });
    }

    private boolean isIndAggregate(RevenueByYear RevenueByYear) {
        return LongStream.of(13L).anyMatch(x ->
                RevenueByYear.getGid() == x ||
                        (RevenueByYear.getParent() != null && RevenueByYear.getParent().getGid() == x));
    }

    private boolean isIndAggregateSum(RevenueByYear RevenueByYear) {
        if (isIndAggregate(RevenueByYear)) return true;
        for (RevenueByYear plan : treeStore.getChildren(RevenueByYear)) {
            if (plan.getPlan() != null && plan.getPlan() >= 0D) {
                return true;
            }
        }
        return false;
    }

    private boolean isEditingSumCell(RevenueByYear RevenueByYear) {
        for (RevenueByYear plan : treeStore.getChildren(RevenueByYear)) {
            if (plan.getPlan() != null && plan.getPlan() >= 0D) {
                return true;
            }
        }
        return false;
    }

    public void addChildToTreeStore(RevenueByYear parent, RevenueByYear child) {
        child.setParent((RevenueMain) parent);
        treeStore.add(parent, child);
    }

    public void sum(RevenueByYear accumulator, RevenueByYear... indicators) {
        accumulator.setFact(0D);
        accumulator.setPlan(0D);
        for (RevenueByYear indicator : indicators) {
            if (indicator == null) continue;
            if (indicator.getFact() != null)
                accumulator.setFact((BigDecimal.valueOf(accumulator.getFact()).add(BigDecimal.valueOf(indicator.getFact())).doubleValue()));
            if (indicator.getPlan() != null)
                accumulator.setPlan((BigDecimal.valueOf(accumulator.getPlan()).add(BigDecimal.valueOf(indicator.getPlan())).doubleValue()));
        }
        treeStore.update(accumulator);
    }

    public RevenueByYear getInd(Long rootGId) {
        RevenueByYear modelWithKey = treeStore.findModelWithKey("p-" + rootGId);
        if (modelWithKey == null)
            return null;
        return modelWithKey;
    }

    public RevenueByYear getIndYear(Long rootGId, String year) {
        RevenueByYear modelWithKey = treeStore.findModelWithKey("p-" + rootGId);
        if (modelWithKey == null) return null;
        for (RevenueByYear child : treeStore.getChildren(modelWithKey)) {
            if (year.equals(child.getName()))
                return child;
        }
        return null;
    }

    public TreeStore<RevenueByYear> getTreeStore() {
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
                for (RevenueByYear child : treeStore.getChildren(treeGrid.getSelectionModel().getSelectedItem())) {
                    //если в комбобоксе не выделен год из грида
                    if (!selectedItems.contains(child.getName())) {
                        treeStore.remove(child);
                    }
                }
                //бежим по выделенным годам в комбобоксе
                if(canGridSelectedItemHasYear(treeGrid.getSelectionModel().getSelectedItem())) {
                    for (String item : selectedItems) {
                        //если в годах грида нет выделенного комбобокса
                        if (treeStore.getChildren(treeGrid.getSelectionModel().getSelectedItem()).stream().noneMatch(child -> child.getName().equals(item))) {
                            RevenueByYear t = new RevenueByYear();
                            t.setGid(getRandId());
                            t.setName(item);
                            t.setUm(treeGrid.getSelectionModel().getSelectedItem().getUm());
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
        for (RevenueByYear rootItem : treeStore.getRootItems()) {
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
        for (RevenueByYear rootItem : treeStore.getRootItems()) {
            if (rootItem.getGid() == 1L && rootItem.getFact() != null && rootItem.getPlan() != null) {
                res = rootItem.getFact() / rootItem.getPlan();
                return Math.round(res * 100.0) / 100.0;
            }
        }
        return res;
    }

    public void addRootRowById(Long id) {
        RevenueMain RevenueMain1 = new RevenueMain();
        RevenueMain1.setGid(13L);
        RevenueMain1.setOrder(1L);
        RevenueMain1.setName("Всего инвестиции, тыс. руб.");

        treeStore.add(RevenueMain1);

        GWT.log("id" + id + " ");

        if (!treeStore.getRootItems().stream().map(i -> i.getGid()).collect(Collectors.toList()).contains(id)) {
            GWT.log("id" + id + " model found");
            if (13L == id) {
                treeStore.add(RevenueMain1);
                addCurrentYearsToRootIndicator(RevenueMain1);
            }
        }
    }

    public boolean isNotFilled() {
        for (RevenueByYear rootItem : treeStore.getRootItems()) {
            if (rootItem.getPlan() == null)
                return true;
        }
        return false;
    }

    public void addCurrentYearsToRootIndicator(RevenueByYear root) {
        for (String year : yearBox.getSelectedItems()) {
            RevenueByYear t = new RevenueByYear();
            t.setGid(WidgetUtils.getRandId());
            t.setParent((RevenueMain) root);
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

    public boolean isRoot(RevenueByYear RevenueByYear) {
        return treeStore.getParent(RevenueByYear) == null;
    }

    public boolean canGridSelectedItemHasYear(RevenueByYear RevenueByYearByYear) {
        return treeStore.getParent(RevenueByYearByYear) != null && treeStore.getParent(treeStore.getParent(RevenueByYearByYear)) == null;
    }

    private boolean isDeprecationIndicator(RevenueByYear RevenueByYear) {
        return RevenueByYear.getName().equals("Срок амортизации объекта, лет");
    }
}
