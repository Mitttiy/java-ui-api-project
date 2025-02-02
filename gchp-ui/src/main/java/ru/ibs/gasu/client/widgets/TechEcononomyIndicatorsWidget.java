package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Util;
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
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;
import ru.ibs.gasu.client.utils.ClientUtils;
import ru.ibs.gasu.client.widgets.componens.ToolbarButton;
import ru.ibs.gasu.client.widgets.componens.UmDic;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class TechEcononomyIndicatorsWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private TreeGrid<TepIndicatorByYear> tepGrid;
    private TextButton addIndicatorButton;
    private TextButton removeIndicatorButton;
    private MultiSelectComboBox<String> yearBox;
    private DateRangeWidget dateRangeWidget;
    private HorizontalLayoutContainer dateRangeWidgetContainer;
    private TreeStore<TepIndicatorByYear> treeStore;
    private UmDic umDic;

    public UmDic getUmDic() {
        return umDic;
    }

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public void setContainer(VerticalLayoutContainer container) {
        this.container = container;
    }

    public TreeGrid<TepIndicatorByYear> getTepGrid() {
        return tepGrid;
    }

    public void setTepGrid(TreeGrid<TepIndicatorByYear> tepGrid) {
        this.tepGrid = tepGrid;
    }

    public TextButton getAddIndicatorButton() {
        return addIndicatorButton;
    }

    public void setAddIndicatorButton(TextButton addIndicatorButton) {
        this.addIndicatorButton = addIndicatorButton;
    }

    public MultiSelectComboBox<String> getYearBox() {
        return yearBox;
    }

    public void setYearBox(MultiSelectComboBox<String> yearBox) {
        this.yearBox = yearBox;
    }

    public TreeStore<TepIndicatorByYear> getTreeStore() {
        return treeStore;
    }

    public void setTreeStore(TreeStore<TepIndicatorByYear> treeStore) {
        this.treeStore = treeStore;
    }

    public TechEcononomyIndicatorsWidget() {
        initWidget();
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
            if (canGridSelectedItemHasYear(tepGrid.getSelectionModel().getSelectedItem())) {
                //бежим по всем годам в гриде
                for (TepIndicatorByYear child : treeStore.getChildren(tepGrid.getSelectionModel().getSelectedItem())) {
                    //если в комбобоксе не выделен год из грида
                    if (!selectedItems.contains(child.getName())) {
                        treeStore.remove(child);
                    }
                }
                //бежим по выделенным годам в комбобоксе
                for (String item : selectedItems) {
                    //если в годах грида нет выделенного комбобокса
                    if (treeStore.getChildren(tepGrid.getSelectionModel().getSelectedItem()).stream().noneMatch(child -> child.getName().equals(item))) {
                        TepIndicatorByYear t = new TepIndicatorByYear();
                        t.setGid(getRandId());
                        t.setName(item);
                        treeStore.add(tepGrid.getSelectionModel().getSelectedItem(), t);
                    }
                }
            }
        });
        multiSelectComboBox.disable();
        return multiSelectComboBox;
    }

    private void initWidget() {
        StyleInjector.inject("[class*=\"com-sencha-gxt-theme-triton-client-base-tree-Css3TreeAppearance-Css3TreeStyle-element\"] {height: auto !important;}");
        container = new VerticalLayoutContainer();
        yearBox = initYearComboBox();

        HBoxLayoutContainer tepButtonsContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        addIndicatorButton = new ToolbarButton("Добавить", "fa-list-ul");
        removeIndicatorButton = new ToolbarButton("Удалить", "fas fa-trash");

        tepButtonsContainer.add(addIndicatorButton, boxLayoutData);
        tepButtonsContainer.add(removeIndicatorButton, boxLayoutData);
        tepButtonsContainer.add(yearBox, boxLayoutData);
        dateRangeWidget = new DateRangeWidget(yearBox);
        dateRangeWidgetContainer = new HorizontalLayoutContainer();
        dateRangeWidgetContainer.add(dateRangeWidget);
        dateRangeWidgetContainer.disable();
        tepButtonsContainer.add(dateRangeWidgetContainer, boxLayoutData);

        treeStore = new TreeStore<>(item -> (item instanceof TepIndicatorMain ? "p-" : "by-") + item.getGid().toString());

        treeStore.addSortInfo(new Store.StoreSortInfo<>(SortableTreeNode.getComparator(), SortDir.ASC));

        treeStore.setAutoCommit(true);
        ColumnConfig<TepIndicatorByYear, String> nameOrYear = new ColumnConfig<>(new ValueProvider<TepIndicatorByYear, String>() {
            @Override
            public String getValue(TepIndicatorByYear object) {
                return object.getName();
            }

            @Override
            public void setValue(TepIndicatorByYear object, String value) {
                object.setName(value);
            }

            @Override
            public String getPath() {
                return "nameOrYear";
            }
        }, 30, "Объект/Показатель/Год");
        nameOrYear.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                if (tepGrid.getColumnModel().getColumns().get(context.getColumn()).isHidden()) {
                    sb.append(Util.NBSP_SAFE_HTML);
                    return;
                }
                if (value == null) {
                    sb.append(Util.NBSP_SAFE_HTML);
                } else {
                    sb.append(wrapStringForTreeGrid(value));
                }
            }
        });

        ColumnConfig<TepIndicatorByYear, String> deleteButton = new ColumnConfig<>(new ValueProvider<TepIndicatorByYear, String>() {
            @Override
            public String getValue(TepIndicatorByYear object) {
                if (!isNotRoot(object)) {
                    return "";
                } else {
                    return object.getDeleteButton();
                }
            }

            @Override
            public void setValue(TepIndicatorByYear object, String value) { }

            @Override
            public String getPath() {
                return "deleteButton";
            }
        }, 2, "");
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

        ColumnConfig<TepIndicatorByYear, List<UmModel>> um = new ColumnConfig<>(new ValueProvider<TepIndicatorByYear, List<UmModel>>() {
            @Override
            public List<UmModel> getValue(TepIndicatorByYear object) {
                List<UmModel> res = new ArrayList<>();
                if (object.getUm() != null)
                    res.add(object.getUm());
                return res;
            }

            @Override
            public void setValue(TepIndicatorByYear object, List<UmModel> value) {
                if (value != null && value.size() > 0)
                    object.setUm(value.get(0));
                else
                    object.setUm(null);
            }

            @Override
            public String getPath() {
                return "um";
            }
        }, 10, "Ед. измерения");
        um.setCell(new AbstractCell<List<UmModel>>() {
            @Override
            public void render(Context context, List<UmModel> value, SafeHtmlBuilder sb) {
                if (value != null && value.size() > 0)
                    sb.appendHtmlConstant(value.get(0).getName());
            }
        });

        ColumnConfig<TepIndicatorByYear, String> plan = new ColumnConfig<>(new ValueProvider<TepIndicatorByYear, String>() {
            @Override
            public String getValue(TepIndicatorByYear object) {
                return WidgetUtils.doubleToString(object.getPlan());
            }

            @Override
            public void setValue(TepIndicatorByYear object, String value) {
                if (value == null) {
                    object.setPlan(null);
                    return;
                }
                try {
                    object.setPlan(Double.parseDouble(value));
                } catch (Exception ex) {
                }
            }

            @Override
            public String getPath() {
                return "plan";
            }
        }, 10, "План");

        ColumnConfig<TepIndicatorByYear, String> fact = new ColumnConfig<>(new ValueProvider<TepIndicatorByYear, String>() {
            @Override
            public String getValue(TepIndicatorByYear object) {
                return WidgetUtils.doubleToString(object.getFact());
            }

            @Override
            public void setValue(TepIndicatorByYear object, String value) {
                if (value == null) {
                    object.setFact(null);
                    return;
                }
                try {
                    object.setFact(Double.parseDouble(value));
                } catch (Exception ex) {
                }
            }

            @Override
            public String getPath() {
                return "fact";
            }
        }, 10, "Факт");

        ColumnModel<TepIndicatorByYear> cm = new ColumnModel<>(Arrays.asList(nameOrYear, deleteButton, um, plan, fact));
        tepGrid = new TreeGrid<>(treeStore, cm, nameOrYear);
        tepGrid.setExpandOnDoubleClick(false);
        tepGrid.getView().setForceFit(true);
        tepGrid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        tepGrid.getView().setStripeRows(true);
        tepGrid.getSelectionModel().addSelectionHandler(new SelectionHandler<TepIndicatorByYear>() {
            @Override
            public void onSelection(SelectionEvent<TepIndicatorByYear> event) {
                if (!canGridSelectedItemHasYear(event.getSelectedItem())) {
                    yearBox.disable();
                    dateRangeWidgetContainer.disable();
                    yearBox.deselectAll(true);
                } else {
                    List<String> children = treeStore.getChildren(event.getSelectedItem()).stream().map(TepIndicatorByYear::getName).collect(Collectors.toList());
                    if (!yearBox.isEnabled()) {
                        yearBox.enable();
                        dateRangeWidgetContainer.enable();
                    }
                    yearBox.deselectAll(true);
                    yearBox.select(children, true);
                }
                yearBox.setValuesToTextField();
                if (treeStore.getParent(event.getSelectedItem()) == null) {
                    addIndicatorButton.enable();
                } else {
                    addIndicatorButton.disable();
                }
            }
        });
        yearBox.setWidth(150);
        container.add(tepButtonsContainer, STD_VC_LAYOUT);
        container.add(tepGrid, STD_VC_LAYOUT);

        umDic = new UmDic(Style.SelectionMode.SINGLE, true);

        final GridInlineEditing<TepIndicatorByYear> editing = new GridInlineEditing<TepIndicatorByYear>(tepGrid);
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(nameOrYear, new TextField());
        editing.addEditor(um, umDic);
        editing.addBeforeStartEditHandler(new BeforeStartEditEvent.BeforeStartEditHandler<TepIndicatorByYear>() {
            @Override
            public void onBeforeStartEdit(BeforeStartEditEvent<TepIndicatorByYear> event) {
                TepIndicatorByYear selectedItem = event.getSource().getEditableGrid()
                        .getSelectionModel().getSelectedItem();
                if (!(selectedItem instanceof TepIndicatorMain) && (event.getEditCell().getCol() == 0 || event.getEditCell().getCol() == 1) || (treeStore.getParent(selectedItem) == null && (event.getEditCell().getCol() == 1 || event.getEditCell().getCol() == 2 || event.getEditCell().getCol() == 3)))
                    event.setCancelled(true);
            }
        });
        editing.addEditor(plan, WidgetUtils.createEditField());
        editing.addEditor(fact, WidgetUtils.createEditField());

        addIndicatorButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                TepIndicatorMain tepIndicatorMain = new TepIndicatorMain();
                tepIndicatorMain.setGid(getRandId());
                if (tepGrid.getSelectionModel().getSelectedItem() == null) {
                    tepIndicatorMain.setName("Новый объект");
                    treeStore.add(tepIndicatorMain);
                } else if (treeStore.getParent(tepGrid.getSelectionModel().getSelectedItem()) == null) {
                    tepIndicatorMain.setName("Новый показатель");
                    treeStore.add(tepGrid.getSelectionModel().getSelectedItem(), tepIndicatorMain);
                }
            }
        });

        removeIndicatorButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (tepGrid.getSelectionModel().getSelectedItem() instanceof TepIndicatorMain) {
                    treeStore.remove(tepGrid.getSelectionModel().getSelectedItem());
                }
            }
        });

        tepGrid.addRowClickHandler(event -> {
            if (event.getColumnIndex() == 1) {
                if (isNotRoot(tepGrid.getSelectionModel().getSelectedItem())) {
                    if (tepGrid.getSelectionModel() != null && tepGrid.getSelectionModel().getSelectedItem() != null) {
                        String year = tepGrid.getSelectionModel().getSelectedItem().getName();
                        ConfirmMessageBox confirmMessageBox = new ConfirmMessageBox("Удалить год", "Вы действительно хотите удалить " + year + " год?");
                        confirmMessageBox.addDialogHideHandler(confirmEvent -> {
                            if (confirmEvent.getHideButton() == Dialog.PredefinedButton.YES) {
                                treeStore.remove(tepGrid.getSelectionModel().getSelectedItem());
                            }
                        });
                        confirmMessageBox.show();
                    }
                }
            }
        });
    }

    public boolean isNotFilled() {
        if (treeStore.getRootItems().size() == 0)
            return true;
        for (TepIndicatorByYear rootItem : treeStore.getRootItems()) {
            if (rootItem.getPlan() == null)
                return true;
        }
        return false;
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    public void toggle(boolean toggle) {
        if (toggle) {
            container.show();
        } else {
            container.hide();
        }
    }

    public boolean canGridSelectedItemHasYear(TepIndicatorByYear tepIndicatorByYear) {
        return treeStore.getParent(tepIndicatorByYear) != null && treeStore.getParent(treeStore.getParent(tepIndicatorByYear)) == null;
    }

    public boolean isNotRoot(TepIndicatorByYear tepIndicatorByYear) {
        TepIndicatorByYear firstParent = treeStore.getParent(tepIndicatorByYear);
        return firstParent != null && treeStore.getParent(firstParent) != null;
    }
}
