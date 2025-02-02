package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;
import ru.ibs.gasu.client.widgets.componens.ToolbarButton;
import ru.ibs.gasu.common.models.Circumstance;
import ru.ibs.gasu.common.models.CircumstanceStage;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.SortableTreeNode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class CircumstancesAdditionalCostsWidget implements IsWidget {


    private VerticalLayoutContainer container;
    private TreeGrid<Circumstance> treeGrid;
    private TreeStore<Circumstance> treeStore;
    private HBoxLayoutContainer buttonsContainer;
    private ToolbarButton addCircumstanceButton;
    private List<SimpleIdNameModel> dicPrepare;
    private List<SimpleIdNameModel> dicBuild;
    private List<SimpleIdNameModel> dicExploitation;

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

    public void setTreeStore(TreeStore<Circumstance> treeStore) {
        this.treeStore = treeStore;
    }

    public HBoxLayoutContainer getButtonsContainer() {
        return buttonsContainer;
    }

    public void setButtonsContainer(HBoxLayoutContainer buttonsContainer) {
        this.buttonsContainer = buttonsContainer;
    }

    public void setDicPrepare(List<SimpleIdNameModel> dicPrepare) {
        this.dicPrepare = dicPrepare;
    }

    public void setDicBuild(List<SimpleIdNameModel> dicBuild) {
        this.dicBuild = dicBuild;
    }

    public void setDicExploitation(List<SimpleIdNameModel> dicExploitation) {
        this.dicExploitation = dicExploitation;
    }

    public void setDictionaries(Long circumstancesStageId, List<SimpleIdNameModel> dic) {
        if (circumstancesStageId == 1L) {
            setDicPrepare(dic);
        } else if (circumstancesStageId == 2L) {
            setDicBuild(dic);
        } else if (circumstancesStageId == 3L) {
            setDicExploitation(dic);
        }
    }

    static {
        StyleInjector.inject(".investments__aggregate-row { background-color: rgb(100, 145, 255, 0.2) !important; }");
    }

    public CircumstancesAdditionalCostsWidget() {
        initButtonContainer();
        initGrid();
        setUpBaseIndicators();

        container.add(buttonsContainer, STD_VC_LAYOUT);
        container.add(treeGrid, STD_VC_LAYOUT);
    }

    private void initButtonContainer() {
        addCircumstanceButton = new ToolbarButton("Добавить", "fa-list-ul");
        addCircumstanceButton.disable();
        container = new VerticalLayoutContainer();

        buttonsContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        buttonsContainer.add(addCircumstanceButton, boxLayoutData);
        addCircumstanceButton.addSelectHandler(event -> {
            Circumstance item = treeGrid.getSelectionModel().getSelectedItem();
            if (item != null) {
                if (item.getGid() == 2L) {
                    createDicWindow(item, dicPrepare);
                } else if (item.getGid() == 3L) {
                    createDicWindow(item, dicBuild);
                } else if (item.getGid() == 4L) {
                    createDicWindow(item, dicExploitation);
                }
            }
        });
    }

    private void initGrid() {
        treeStore = new TreeStore<>(new ModelKeyProvider<Circumstance>() {
            @Override
            public String getKey(Circumstance item) {
                return "p-" + item.getGid();
            }
        });
        treeStore.setAutoCommit(true);

        treeStore.addSortInfo(new Store.StoreSortInfo<>(SortableTreeNode.getComparator(), SortDir.ASC));

        ColumnConfig<Circumstance, String> name = new ColumnConfig<>(new ValueProvider<Circumstance, String>() {
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
                return "name";
            }
        }, 150, "Наименование обстоятельства");
        name.setMenuDisabled(true);
        name.setSortable(false);

        ColumnConfig<Circumstance, String> sum = new ColumnConfig<>(new ValueProvider<Circumstance, String>() {
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
                return "sum";
            }
        }, 150, "Сумма выплаченной компенсации, тыс. руб.");
        sum.setMenuDisabled(true);
        sum.setSortable(false);

        ColumnConfig<Circumstance, String> year = new ColumnConfig<>(new ValueProvider<Circumstance, String>() {
            @Override
            public String getValue(Circumstance object) {
                return object.getYears();
            }

            @Override
            public void setValue(Circumstance object, String value) {
                if (value == null) {
                    object.setYears(null);
                    onValueChange();
                    return;
                }
                try {
                    object.setYears(value);
                } catch (Exception ex) {
                }
                onValueChange();
            }

            @Override
            public String getPath() {
                return "years";
            }
        }, 100, "Год(ы) выплаты");
        year.setMenuDisabled(true);
        year.setSortable(false);

        ColumnModel<Circumstance> cm = new ColumnModel<>(Arrays.asList(name, sum, year));
        treeGrid = new TreeGrid<>(treeStore, cm, name);
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

        final GridInlineEditing<Circumstance> editing = new GridInlineEditing<Circumstance>(treeGrid);
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(sum, WidgetUtils.createEditField());
        editing.addEditor(name, WidgetUtils.createEditField());
        editing.addEditor(year, WidgetUtils.createEditField());
        editing.addBeforeStartEditHandler(event -> {
            Circumstance item = event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem();
            if (item != null && treeStore.getParent(item) == null && item.getGid() != 1) {
                addCircumstanceButton.enable();
            } else {
                addCircumstanceButton.disable();
            }
            if (event.getEditCell().getCol() == 0) event.setCancelled(true);
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
                for (Circumstance circumstance : indicatorList) {
                    if (totalIndYear.getName().equals(circumstance.getName())) totalIndYearList.add(circumstance);
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

    public void addChildToTreeStore(Circumstance parent, Circumstance child) {
        child.setParent((CircumstanceStage) parent);
        treeStore.add(parent, child);
    }

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

    private String getCurrentYear() {
        return DateTimeFormat.getFormat("d-M-yyyy").format(new Date()).split("-")[2];
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

        GWT.log("id" + id + " ");

        if (!treeStore.getRootItems().stream().map(i -> i.getGid()).collect(Collectors.toList()).contains(id)) {
            GWT.log("id" + id + " model found");
            if (1L == id) {
                treeStore.add(CircumstanceStage1);
            }

            if (2L == id) {
                treeStore.add(CircumstanceStage2);
            }

            if (3L == id) {
                treeStore.add(CircumstanceStage3);
            }

            if (4L == id) {
                treeStore.add(CircumstanceStage4);
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

    private void createDicWindow(Circumstance item, List<SimpleIdNameModel> dic) {
        List<SimpleIdNameModel> selected = new ArrayList<>();
        for (Circumstance modelInTreeStore : treeStore.getChildren(item)) {
            for (SimpleIdNameModel modelInDic : dic) {
                if (modelInTreeStore.getName().equals(modelInDic.getName())) {
                    selected.add(modelInDic);
                }
            }
        }
        new CircumstancesAdditionalCostsDicWindow(dic, selected) {
            @Override
            protected void onSave(List<SimpleIdNameModel> picked) {
                removeFromTreeStore(picked, selected, item);
                Long gid = getRandId();
                for (SimpleIdNameModel model : picked) {
                    Circumstance newItem = new Circumstance(getRandId(), model.getName());
                    if (!treeStore.getAllChildren(item).contains(newItem)) {
                        newItem.setGid(gid);
                        treeStore.add(item, newItem);
                        gid++;
                    }
                }
                treeGrid.getView().refresh(false);
            }
        }.show();
    }

    private void removeFromTreeStore(List<SimpleIdNameModel> picked, List<SimpleIdNameModel> selectInTreeStore, Circumstance parent) {
        for (SimpleIdNameModel modelInTreeStore : selectInTreeStore) {
            treeGrid.collapseAll();
            if (!picked.contains(modelInTreeStore)) {
                for (Circumstance c : treeStore.getAllChildren(parent)) {
                    if (c.getName().equals(modelInTreeStore.getName())) {
                        treeStore.remove(c);
                    }
                }
                treeGrid.expandAll();
            }
        }
    }
}
