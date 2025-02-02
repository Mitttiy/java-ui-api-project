package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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

import static ru.ibs.gasu.client.widgets.WidgetUtils.GRID_EMPTY_TEXT_VAR1;
import static ru.ibs.gasu.client.widgets.WidgetUtils.STD_VC_LAYOUT;

public class FinancialStructureWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private Grid<FinancialIndicatorModel> grid;
    private ListStore<FinancialIndicatorModel> store;

    public Grid<FinancialIndicatorModel> getGrid() {
        return grid;
    }

    public FinancialStructureWidget() {
        initButtonContainer();
        initGrid();
        setUpBaseIndicators();
        container.add(grid, STD_VC_LAYOUT);
    }

    private void initButtonContainer() {
        container = new VerticalLayoutContainer();
    }

    private void initGrid() {
        store = new ListStore<>(new ModelKeyProvider<FinancialIndicatorModel>() {
            @Override
            public String getKey(FinancialIndicatorModel item) {
                return String.valueOf(item.getGid());
            }
        });
        store.setAutoCommit(true);

        ColumnConfig<FinancialIndicatorModel, String> serialNum = new ColumnConfig<>(new ValueProvider<FinancialIndicatorModel, String>() {
            @Override
            public String getValue(FinancialIndicatorModel object) {
                return object.getSerialNum();
            }

            @Override
            public void setValue(FinancialIndicatorModel object, String value) {
            }

            @Override
            public String getPath() {
                return "serialNum";
            }
        }, 15, "№ п/п");

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
        }, 100, "Наименование показателя, тыс. руб");

        name.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                String serialNum = getStore().get(context.getIndex()).getSerialNum();
                int count = 0;
                for (int i = 0; i < serialNum.length(); i++) {
                    if (serialNum.charAt(i) == '.')
                        count++;
                }
                sb.appendHtmlConstant("<span style='margin-left:" + count * 15 + "px'>").appendHtmlConstant(value).appendHtmlConstant("</span>");
            }
        });

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
        }, 100, "Факт");

        ColumnModel<FinancialIndicatorModel> cm = new ColumnModel<>(Arrays.asList(serialNum, name, value));
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
        p1.setSerialNum("1");
        p1.setName("Общий объем инвестиций");

        FinancialIndicatorModel p2 = new FinancialIndicatorModel();
        p2.setGid(2L);
        p2.setSerialNum("1.1");
        p2.setName("Объем частных средств");

        FinancialIndicatorModel p3 = new FinancialIndicatorModel();
        p3.setGid(3L);
        p3.setSerialNum("1.1.1");
        p3.setName("Собственные");

        FinancialIndicatorModel p4 = new FinancialIndicatorModel();
        p4.setGid(4L);
        p4.setSerialNum("1.1.1.1");
        p4.setName("Имущественный взнос");

        FinancialIndicatorModel p14 = new FinancialIndicatorModel();
        p14.setGid(14L);
        p14.setSerialNum("1.1.1.1.1");
        p14.setName("- в виде денежных средств");

        FinancialIndicatorModel p15 = new FinancialIndicatorModel();
        p15.setGid(15L);
        p15.setSerialNum("1.1.1.1.2");
        p15.setName("- в виде материальных активов");

        FinancialIndicatorModel p5 = new FinancialIndicatorModel();
        p5.setGid(5L);
        p5.setSerialNum("1.1.1.2");
        p5.setName("Акционерный заем");

        FinancialIndicatorModel p6 = new FinancialIndicatorModel();
        p6.setGid(6L);
        p6.setSerialNum("1.1.1.3");
        p6.setName("Иные формы финансирования");

        FinancialIndicatorModel p7 = new FinancialIndicatorModel();
        p7.setGid(7L);
        p7.setSerialNum("1.1.2");
        p7.setName("Заемные");

        FinancialIndicatorModel p16 = new FinancialIndicatorModel();
        p16.setGid(16L);
        p16.setSerialNum("1.1.2.1");
        p16.setName("Долговое финансирование");

        FinancialIndicatorModel p17 = new FinancialIndicatorModel();
        p17.setGid(17L);
        p17.setSerialNum("1.1.2.2");
        p17.setName("Облигационное финансирование");

        FinancialIndicatorModel p18 = new FinancialIndicatorModel();
        p18.setGid(18L);
        p18.setSerialNum("1.1.2.3");
        p18.setName("Иное заемное финансирование");

        FinancialIndicatorModel p8 = new FinancialIndicatorModel();
        p8.setGid(8L);
        p8.setSerialNum("1.2");
        p8.setName("Объем бюджетных средств");

        FinancialIndicatorModel p9 = new FinancialIndicatorModel();
        p9.setGid(9L);
        p9.setSerialNum("1.2.1");
        p9.setName("Федеральный бюджет");

        FinancialIndicatorModel p10 = new FinancialIndicatorModel();
        p10.setGid(10L);
        p10.setSerialNum("1.2.2");
        p10.setName("Региональный бюджет");

        FinancialIndicatorModel p11 = new FinancialIndicatorModel();
        p11.setGid(11L);
        p11.setSerialNum("1.2.2.1");
        p11.setName("- из них предоставлено в виде межбюджетных трансфертов из федерального бюджета");

        FinancialIndicatorModel p12 = new FinancialIndicatorModel();
        p12.setGid(12L);
        p12.setSerialNum("1.2.3");
        p12.setName("Местный бюджет");

        FinancialIndicatorModel p13 = new FinancialIndicatorModel();
        p13.setGid(13L);
        p13.setSerialNum("1.2.3.1");
        p13.setName(" - из них предоставлено в виде межбюджетных трансфертов из регионального бюджета");

        store.add(p1);
        store.add(p2);
        store.add(p3);
        store.add(p4);
        store.add(p14);
        store.add(p15);
        store.add(p5);
        store.add(p6);
        store.add(p7);
        store.add(p16);
        store.add(p17);
        store.add(p18);
        store.add(p8);
        store.add(p9);
        store.add(p10);
        store.add(p11);
        store.add(p12);
        store.add(p13);
    }

    public ListStore<FinancialIndicatorModel> getStore() {
        return store;
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
