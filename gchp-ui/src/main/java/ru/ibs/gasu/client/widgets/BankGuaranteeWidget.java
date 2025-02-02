package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.StringComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;
import ru.ibs.gasu.client.widgets.componens.ToolbarButton;
import ru.ibs.gasu.common.models.PlanFactIndicator;
import ru.ibs.gasu.common.models.PlanFactYear;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.LongStream;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class BankGuaranteeWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private TreeGrid<PlanFactYear> treeGrid;
    private TextButton addYearButton;
    private TextButton removeYearButton;
    private StringComboBox yearBox;
    private TreeStore<PlanFactYear> treeStore;
    private HBoxLayoutContainer buttonsContainer;
    private List<Integer> currentYears = new ArrayList<>();
    private TextButton expandButton;
    private TextButton collapseButton;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public TreeGrid<PlanFactYear> getTreeGrid() {
        return treeGrid;
    }

    public TextButton getAddYearButton() {
        return addYearButton;
    }

    public TextButton getRemoveYearButton() {
        return removeYearButton;
    }

    public StringComboBox getYearBox() {
        return yearBox;
    }

    public HBoxLayoutContainer getButtonsContainer() {
        return buttonsContainer;
    }

    public List<Integer> getCurrentYears() {
        return currentYears;
    }

    public TextButton getExpandButton() {
        return expandButton;
    }

    public TextButton getCollapseButton() {
        return collapseButton;
    }


    public BankGuaranteeWidget() {
        initButtonContainer();
        initGrid();
        setUpBaseIndicators();
        container.add(buttonsContainer, STD_VC_LAYOUT);
        container.add(treeGrid, STD_VC_LAYOUT);
    }

    private void initButtonContainer() {
        container = new VerticalLayoutContainer();
        yearBox = new StringComboBox(generateComboBoxYears());
        yearBox.setValue(getCurrentYear());
        yearBox.setEditable(false);
        yearBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);

        addYearButton = new ToolbarButton("Добавить год", "fas fa-calendar-plus-o");
        removeYearButton = new ToolbarButton("Удалить год", "fas fa-calendar-minus-o");
        expandButton = new ToolbarButton("Развернуть", "fas fa-expand");
        collapseButton = new ToolbarButton("Свернуть", "fa-compress");

        buttonsContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));
        buttonsContainer.add(collapseButton, boxLayoutData);
        buttonsContainer.add(expandButton, boxLayoutData);
        SeparatorToolItem separator = new SeparatorToolItem();
        separator.setHeight(ToolbarButton.DEFAULT_HEIGHT);
        buttonsContainer.add(separator, boxLayoutData);
        buttonsContainer.add(addYearButton, boxLayoutData);
        buttonsContainer.add(removeYearButton, boxLayoutData);
        separator = new SeparatorToolItem();
        separator.setHeight(ToolbarButton.DEFAULT_HEIGHT);
        buttonsContainer.add(separator, boxLayoutData);
        buttonsContainer.add(yearBox);

        addYearButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Integer selectedYear = Integer.valueOf(yearBox.getValue());
                if (currentYears.contains(selectedYear)) {
                    new MessageBox("Ошибка", "Данный год уже есть в показателях").show();
                    return;
                }
                for (PlanFactYear rootItem : treeStore.getRootItems()) {
                    PlanFactYear t = new PlanFactYear();
                    t.setGid(getRandId());
                    t.setNameOrYear(String.valueOf(selectedYear));
                    treeStore.add(rootItem, t);
                }
                currentYears.add(selectedYear);
            }
        });

        removeYearButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Integer selectedYear = Integer.valueOf(yearBox.getValue());
                for (PlanFactYear rootItem : treeStore.getRootItems()) {
                    for (PlanFactYear child : treeStore.getChildren(rootItem)) {
                        if (child.getNameOrYear().equals(yearBox.getValue())) {
                            treeStore.remove(child);
                        }
                    }
                }
                currentYears.remove(selectedYear);
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
        treeStore = new TreeStore<>(new ModelKeyProvider<PlanFactYear>() {
            @Override
            public String getKey(PlanFactYear item) {
                String key = (item instanceof PlanFactIndicator ? "p-" : "by-") + item.getGid();
                return key;
            }
        });
        treeStore.setAutoCommit(true);

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
        }, 10, "Размер банковской гарантии по годам, тыс.руб.");
        ColumnConfig<PlanFactYear, String> plan = new ColumnConfig<>(new ValueProvider<PlanFactYear, String>() {
            @Override
            public String getValue(PlanFactYear object) {
                return WidgetUtils.doubleToString(object.getPlan());
            }

            @Override
            public void setValue(PlanFactYear object, String value) {
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
                    return;
                }
                ;
                object.setFact(Double.parseDouble(value));
            }

            @Override
            public String getPath() {
                return "fact";
            }
        }, 10, "Значение");

        ColumnModel<PlanFactYear> cm = new ColumnModel<>(Arrays.asList(nameOrYear, fact));
        treeGrid = new TreeGrid<>(treeStore, cm, nameOrYear);
        treeGrid.setExpandOnDoubleClick(false);
        treeGrid.getView().setForceFit(true);
        treeGrid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        treeGrid.getView().setStripeRows(true);

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
        });
    }

    protected void setUpBaseIndicators() {
        currentYears.clear();
        PlanFactIndicator planFactIndicator1 = new PlanFactIndicator();
        planFactIndicator1.setGid(1L);
        planFactIndicator1.setNameOrYear("Всего");

        treeStore.add(planFactIndicator1);
    }

    public TreeStore<PlanFactYear> getTreeStore() {
        return treeStore;
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

    private boolean isIndAggregate(PlanFactYear planFactYear) {
        return LongStream.of(1L).anyMatch(x -> planFactYear.getGid() == x);
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
    private String getCurrentYear() {
        return DateTimeFormat.getFormat("d-M-yyyy").format(new Date()).split("-")[2];
    }

    private List<String> generateComboBoxYears() {
        Integer currentYear = Integer.parseInt(getCurrentYear());
        List<String> res = new ArrayList<>();
        for (int i = currentYear - 10; i <= currentYear + 20; i++) {
            res.add(String.valueOf(i));
        }
        return res;
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
}
