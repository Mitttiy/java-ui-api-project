package ru.ibs.gasu.client.widgets;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import ru.ibs.gasu.common.models.FinancialIndicatorModel;

import java.util.Arrays;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class CompositionOfCompensationWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private Grid<FinancialIndicatorModel> grid;
    private ListStore<FinancialIndicatorModel> store;

    public Grid<FinancialIndicatorModel> getGrid() {
        return grid;
    }

    public CompositionOfCompensationWidget() {
        initGrid();
//        setUpBaseIndicators();
        container.add(grid, STD_VC_LAYOUT);
    }

    private void initGrid() {
        container = new VerticalLayoutContainer();
        store = new ListStore<>(new ModelKeyProvider<FinancialIndicatorModel>() {
            @Override
            public String getKey(FinancialIndicatorModel item) {
                return String.valueOf(item.getGid());
            }
        });
        store.setAutoCommit(true);

        ColumnConfig<FinancialIndicatorModel, String> name = new ColumnConfig<>(new ValueProvider<FinancialIndicatorModel, String>() {
            @Override
            public String getValue(FinancialIndicatorModel object) {
                return object.getName();
            }

            @Override
            public void setValue(FinancialIndicatorModel object, String value) {
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 300, "Состав компенсации при прекращении соглашения");
        name.setSortable(false);

        ColumnConfig<FinancialIndicatorModel, String> value = new ColumnConfig<>(new ValueProvider<FinancialIndicatorModel, String>() {
            @Override
            public String getValue(FinancialIndicatorModel object) {
                return WidgetUtils.doubleToString(object.getValue());
            }

            @Override
            public void setValue(FinancialIndicatorModel object, String value) {
                if (value == null) {
                    object.setValue(null);
                    return;
                }
                try {
                    object.setValue(Double.parseDouble(value));
                } catch (Exception ex) {
                }
            }

            @Override
            public String getPath() {
                return "value";
            }
        }, 400, "Сумма выплаченной компенсации при прекращении (накопленным итогом), тыс. руб.");
        value.setSortable(false);

        ColumnModel<FinancialIndicatorModel> cm = new ColumnModel<>(Arrays.asList(name, value));
        grid = new Grid<>(store, cm);
        grid.getView().setForceFit(true);
        grid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        grid.getView().setStripeRows(true);

        final GridInlineEditing<FinancialIndicatorModel> editing = new GridInlineEditing<FinancialIndicatorModel>(grid) {
            @Override
            protected void onScroll(ScrollEvent event) {

            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(value, WidgetUtils.createEditField());
    }

    protected void setUpBaseIndicators() {
        FinancialIndicatorModel p1 = new FinancialIndicatorModel();
        p1.setGid(1L);
        p1.setName("Возврат долгового финансирования");

        FinancialIndicatorModel p2 = new FinancialIndicatorModel();
        p2.setGid(2L);
        p2.setName("Возврат процентов по долговому финансированию");

        FinancialIndicatorModel p3 = new FinancialIndicatorModel();
        p3.setGid(3L);
        p3.setName("Возврат собственных средств концессионера/частного партнера");

        FinancialIndicatorModel p4 = new FinancialIndicatorModel();
        p4.setGid(4L);
        p4.setName("Компенсация доходности концессионера/частного партнера");

        FinancialIndicatorModel p5 = new FinancialIndicatorModel();
        p5.setGid(5L);
        p5.setName("Компенсация расходов на прекращение");

        FinancialIndicatorModel p6 = new FinancialIndicatorModel();
        p6.setGid(6L);
        p6.setName("Иное");

        store.add(p1);
        store.add(p2);
        store.add(p3);
        store.add(p4);
        store.add(p5);
        store.add(p6);
    }

    public ListStore<FinancialIndicatorModel> getStore() {
        return store;
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
