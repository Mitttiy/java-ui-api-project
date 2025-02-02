package ru.ibs.gasu.client.widgets;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import ru.ibs.gasu.client.utils.ClientUtils;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.BalanceOfDebt;

import java.util.Arrays;
import java.util.Comparator;

import static ru.ibs.gasu.client.widgets.WidgetUtils.GRID_EMPTY_TEXT_VAR1;
import static ru.ibs.gasu.client.widgets.WidgetUtils.STD_VC_LAYOUT;

public class BalanceOfDebtWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private Grid<BalanceOfDebt> balanceOfDebtGrid;
    private TextButton addYearButton;
    private TextButton removeYearButton;
    private MultiSelectComboBox<String> yearBox;
    private DateRangeWidget dateRangeWidget;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public void setContainer(VerticalLayoutContainer container) {
        this.container = container;
    }

    public Grid<BalanceOfDebt> getBalanceOfDebtGrid() {
        return balanceOfDebtGrid;
    }

    public void setBalanceOfDebtGrid(Grid<BalanceOfDebt> balanceOfDebtGrid) {
        this.balanceOfDebtGrid = balanceOfDebtGrid;
    }

    public TextButton getAddYearButton() {
        return addYearButton;
    }

    public void setAddYearButton(TextButton addYearButton) {
        this.addYearButton = addYearButton;
    }

    public TextButton getRemoveYearButton() {
        return removeYearButton;
    }

    public void setRemoveYearButton(TextButton removeYearButton) {
        this.removeYearButton = removeYearButton;
    }

    public MultiSelectComboBox<String> getYearBox() {
        return yearBox;
    }

    public void setYearBox(MultiSelectComboBox<String> yearBox) {
        this.yearBox = yearBox;
    }

    public BalanceOfDebtWidget() {
        initWidget();
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        yearBox = initYearComboBox();
        yearBox.setEditable(false);
        dateRangeWidget = new DateRangeWidget(yearBox);

        HBoxLayoutContainer multiboxContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        multiboxContainer.add(yearBox, boxLayoutData);
        multiboxContainer.add(dateRangeWidget, boxLayoutData);
        ListStore<BalanceOfDebt> balanceOfDebtStore = new ListStore<>(BalanceOfDebt::getPeriod);
        balanceOfDebtStore.setAutoCommit(true);
        ColumnConfig<BalanceOfDebt, String> period = new ColumnConfig<>(new ValueProvider<BalanceOfDebt, String>() {
            @Override
            public String getValue(BalanceOfDebt object) {
                return object.getPeriod();
            }

            @Override
            public void setValue(BalanceOfDebt object, String value) {
                // не редактируемое
            }

            @Override
            public String getPath() {
                return "period";
            }
        }, 10, "Отчетный период");
        ColumnConfig<BalanceOfDebt, String> plan = new ColumnConfig<>(new ValueProvider<BalanceOfDebt, String>() {
            @Override
            public String getValue(BalanceOfDebt object) {
                return WidgetUtils.doubleToString(object.getFactValue());
            }

            @Override
            public void setValue(BalanceOfDebt object, String value) {
                if (value == null) {
                    object.setFactValue(null);
                    return;
                }
                try {
                    object.setFactValue(Double.parseDouble(value));
                } catch (Exception ex) {
                }
            }

            @Override
            public String getPath() {
                return "fact";
            }
        }, 10, "Факт");

        ColumnModel<BalanceOfDebt> balanceOfDebtCm = new ColumnModel<>(Arrays.asList(period, plan));
        balanceOfDebtGrid = new Grid<>(balanceOfDebtStore, balanceOfDebtCm);
        balanceOfDebtGrid.getView().setForceFit(true);
        balanceOfDebtGrid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        balanceOfDebtGrid.getView().setStripeRows(true);
        balanceOfDebtGrid.getStore().addSortInfo(new Store.StoreSortInfo<>(Comparator.comparing(BalanceOfDebt::getPeriod), SortDir.ASC));
        final GridInlineEditing<BalanceOfDebt> editing = new GridInlineEditing<BalanceOfDebt>(balanceOfDebtGrid) {
            @Override
            protected void onScroll(ScrollEvent event) {
            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(plan, WidgetUtils.createEditField());
        container.add(multiboxContainer, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));
        container.add(balanceOfDebtGrid, STD_VC_LAYOUT);
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
            //если в комбобоксе не выделен год
            for(BalanceOfDebt model : balanceOfDebtGrid.getStore().getAll()) {
                if(!selectedItems.contains(model.getPeriod())) {
                    balanceOfDebtGrid.getStore().remove(model);
                }
            }
            //добавляем год
            for (String year : selectedItems) {
                if (balanceOfDebtGrid.getStore().getAll().stream().noneMatch(i -> year.equals(i.getPeriod()))) {
                    BalanceOfDebt model = new BalanceOfDebt();
                    model.setPeriod(year);
                    balanceOfDebtGrid.getStore().add(model);
                    balanceOfDebtGrid.getStore().applySort(true);
                }
            }
        });
        return multiSelectComboBox;
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
