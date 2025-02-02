package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style;
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
import ru.ibs.gasu.client.widgets.componens.TaxConditionDic;
import ru.ibs.gasu.client.widgets.componens.ToolbarButton;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.TaxConditionIndicatorByYear;
import ru.ibs.gasu.common.models.TaxConditionMain;
import ru.ibs.gasu.common.models.TaxConditionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class TaxConditionWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private TreeGrid<TaxConditionIndicatorByYear> treeGrid;
    private MultiSelectComboBox<String> yearBox;
    private DateRangeWidget dateRangeWidget;
    private HorizontalLayoutContainer dateRangeWidgetContainer;
    private TreeStore<TaxConditionIndicatorByYear> treeStore;
    private HBoxLayoutContainer buttonsContainer;
    private TextButton addIndicatorButton;
    private TextButton removeIndicatorButton;
    private TextButton expandButton;
    private TextButton collapseButton;
    private TaxConditionDic taxConditionDic;

    public TaxConditionDic getTaxConditionDic() {
        return taxConditionDic;
    }

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public void setContainer(VerticalLayoutContainer container) {
        this.container = container;
    }

    public TreeGrid<TaxConditionIndicatorByYear> getTreeGrid() {
        return treeGrid;
    }

    public void setTreeGrid(TreeGrid<TaxConditionIndicatorByYear> treeGrid) {
        this.treeGrid = treeGrid;
    }

    public MultiSelectComboBox<String> getYearBox() {
        return yearBox;
    }

    public void setYearBox(MultiSelectComboBox<String> yearBox) {
        this.yearBox = yearBox;
    }

    public void setTreeStore(TreeStore<TaxConditionIndicatorByYear> treeStore) {
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

    public TaxConditionWidget() {
        initButtonContainer();
        initGrid();
        container.add(buttonsContainer, STD_VC_LAYOUT);
        container.add(treeGrid, STD_VC_LAYOUT);
    }

    public TaxConditionWidget(boolean hideFact) {
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
                TaxConditionMain taxCondition = new TaxConditionMain();
                taxCondition.setGid(getRandId());
                taxCondition.setName("");
                treeStore.add(taxCondition);
            }
        });

        removeIndicatorButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (treeGrid.getSelectionModel().getSelectedItem() instanceof TaxConditionMain) {
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
        treeStore = new TreeStore<>(new ModelKeyProvider<TaxConditionIndicatorByYear>() {
            @Override
            public String getKey(TaxConditionIndicatorByYear item) {
                String key = (item instanceof TaxConditionMain ? "p-" : "by-") + item.getGid();
                return key;
            }
        });
        treeStore.setAutoCommit(true);

        treeStore.addSortInfo(new Store.StoreSortInfo<>(TaxConditionIndicatorByYear.getComparator(), SortDir.ASC));

        ColumnConfig<TaxConditionIndicatorByYear, List<TaxConditionModel>> taxCondition = new ColumnConfig<>(new ValueProvider<TaxConditionIndicatorByYear, List<TaxConditionModel>>() {
            @Override
            public List<TaxConditionModel> getValue(TaxConditionIndicatorByYear object) {
                List<TaxConditionModel> res = new ArrayList<>();
                if (object.getTaxCondition() != null)
                    res.add(object.getTaxCondition());
                return res;
            }

            @Override
            public void setValue(TaxConditionIndicatorByYear object, List<TaxConditionModel> value) {
                if (value != null && value.size() > 0)
                    object.setTaxCondition(value.get(0));
                else
                    object.setTaxCondition(null);
            }

            @Override
            public String getPath() {
                return "taxCondition";
            }
        }, 60, "Налоговое условие");
        taxCondition.setCell(new AbstractCell<List<TaxConditionModel>>() {
            @Override
            public void render(Context context, List<TaxConditionModel> value, SafeHtmlBuilder sb) {
                if (value != null && value.size() > 0)
                    sb.appendHtmlConstant(value.get(0).getName());
            }
        });
        taxCondition.setSortable(false);

        ColumnConfig<TaxConditionIndicatorByYear, String> year = new ColumnConfig<>(new ValueProvider<TaxConditionIndicatorByYear, String>() {
            @Override
            public String getValue(TaxConditionIndicatorByYear object) {
                return object.getName();
            }

            @Override
            public void setValue(TaxConditionIndicatorByYear object, String value) {
                object.setName(value);
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 20);
        year.setMenuDisabled(true);
        year.setSortable(false);

        ColumnConfig<TaxConditionIndicatorByYear, String> tax = new ColumnConfig<>(new ValueProvider<TaxConditionIndicatorByYear, String>() {
            @Override
            public String getValue(TaxConditionIndicatorByYear object) {
                return WidgetUtils.doubleToString(object.getTax());
            }

            @Override
            public void setValue(TaxConditionIndicatorByYear object, String value) {
                if (value == null) {
                    object.setTax(null);
                    onValueChange();
                    return;
                }
                try {
                    object.setTax(Double.parseDouble(value));
                } catch (Exception ex) {
                }
                onValueChange();
            }

            @Override
            public String getPath() {
                return "tax";
            }
        }, 100, "Ставка, %");
        tax.setMenuDisabled(true);
        tax.setSortable(false);

        ColumnModel<TaxConditionIndicatorByYear> cm = new ColumnModel<>(Arrays.asList(taxCondition, year, tax));
        treeGrid = new TreeGrid<>(treeStore, cm, taxCondition);
        treeGrid.setExpandOnDoubleClick(false);
        treeGrid.getView().setForceFit(true);
        treeGrid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        treeGrid.getView().setStripeRows(true);
        treeGrid.getSelectionModel().addSelectionHandler(event -> {
            if (treeStore.getParent(event.getSelectedItem()) != null) {
                yearBox.disable();
                dateRangeWidgetContainer.disable();
                yearBox.deselectAll(true);
                yearBox.setText(ClientUtils.getCurrentYear());
            } else {
                List<String> children = treeStore.getChildren(event.getSelectedItem()).stream().map(TaxConditionIndicatorByYear::getName).collect(Collectors.toList());
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
        });
        taxConditionDic = new TaxConditionDic(Style.SelectionMode.SINGLE, true);
        final GridInlineEditing<TaxConditionIndicatorByYear> editing = new GridInlineEditing<TaxConditionIndicatorByYear>(treeGrid);
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(taxCondition, taxConditionDic);
        editing.addEditor(tax, WidgetUtils.createEditField());

        editing.addBeforeStartEditHandler(event -> {
            if (event.getEditCell().getCol() == 1 || (treeStore.getParent(event.getSource().getEditableGrid()
                    .getSelectionModel().getSelectedItem()) != null && (event.getEditCell().getCol() == 0))) {
                event.setCancelled(true);
            }
        });
    }

    public TaxConditionIndicatorByYear getInd(Long rootGId) {
        TaxConditionIndicatorByYear modelWithKey = treeStore.findModelWithKey("p-" + rootGId);
        if (modelWithKey == null)
            return null;
        return modelWithKey;
    }

    public TaxConditionIndicatorByYear getIndYear(Long rootGId, String year) {
        TaxConditionIndicatorByYear modelWithKey = treeStore.findModelWithKey("p-" + rootGId);
        if (modelWithKey == null) return null;
        for (TaxConditionIndicatorByYear child : treeStore.getChildren(modelWithKey)) {
            if (year.equals(child.getName()))
                return child;
        }
        return null;
    }

    public TreeStore<TaxConditionIndicatorByYear> getTreeStore() {
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
                for (TaxConditionIndicatorByYear child : treeStore.getChildren(treeGrid.getSelectionModel().getSelectedItem())) {
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
                            TaxConditionMain t = new TaxConditionMain();
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
        for (TaxConditionIndicatorByYear rootItem : treeStore.getRootItems()) {
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

    public boolean isNotFilled() {
        for (TaxConditionIndicatorByYear rootItem : treeStore.getRootItems()) {
            if (rootItem.getTax() == null)
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

    public boolean isRoot(TaxConditionMain planFactYear) {
        return treeStore.getParent(planFactYear) == null;
    }

    public boolean canGridSelectedItemHasYear(TaxConditionIndicatorByYear taxConditionByYear) {
        return treeStore.getParent(taxConditionByYear) == null;
    }
}
