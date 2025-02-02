package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.DoubleField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;

import java.util.Arrays;

import static ru.ibs.gasu.client.widgets.WidgetUtils.STD_VC_LAYOUT;
import static ru.ibs.gasu.client.widgets.WidgetUtils.wrapString;

public class FinancialEconomicsIndView implements IsWidget {

    enum Inds {
        NPV("Плановое значение чистой приведенной стоимости проекта (NPV), тыс. руб."),
        IRR("Плановое значение внутренней нормы доходности проекта (IRR), %"),
        PB("Период окупаемости проекта (простой), лет"),
        PBDiscounted("Период окупаемости проекта (дисконтированный), лет"),
        EBIDTA("Прибыль до вычета процентов, налогов и амортизации (EBITDA), тыс. руб."),
        WACC("Средневзвешенная стоимость капитала (WACC), %");

        private String name;

        Inds(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }

    static class TableModel {
        private Inds ind;
        private Double value;

        TableModel(Inds ind) {
            this.ind = ind;
        }

        public Inds getInd() {
            return ind;
        }

        public void setInd(Inds ind) {
            this.ind = ind;
        }

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }
    }

    private VerticalLayoutContainer container;

    public Grid<TableModel> getGrid() {
        return grid;
    }

    private Grid<TableModel> grid;

    public ListStore<TableModel> getStore() {
        return store;
    }

    private ListStore<TableModel> store;

    public FinancialEconomicsIndView() {
        container = new VerticalLayoutContainer();

        store = new ListStore<>(new ModelKeyProvider<TableModel>() {
            @Override
            public String getKey(TableModel item) {
                return String.valueOf(item.getInd().getName());
            }
        });

        ColumnConfig<TableModel, String> name = new ColumnConfig<>(new ValueProvider<TableModel, String>() {
            @Override
            public String getValue(TableModel object) {
                return object.getInd().getName();
            }

            @Override
            public void setValue(TableModel object, String value) {
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 400, "Наименование показателя");
        name.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                if (grid.getColumnModel().getColumns().get(context.getColumn()).isHidden()) {
                    sb.append(Util.NBSP_SAFE_HTML);
                    return;
                }
                if (value == null) {
                    sb.append(Util.NBSP_SAFE_HTML);
                } else {
                    sb.append(wrapString(value));
                }
            }
        });
        name.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

        ColumnConfig<TableModel, Double> value = new ColumnConfig<>(new ValueProvider<TableModel, Double>() {
            @Override
            public Double getValue(TableModel object) {
                return object.getValue();
            }

            @Override
            public void setValue(TableModel object, Double value) {
                object.setValue(value);
            }

            @Override
            public String getPath() {
                return "value";
            }
        }, 100, "Значение");

        ColumnModel<TableModel> cm = new ColumnModel<>(Arrays.asList(name, value/*, removeColumn*/));
        store.setAutoCommit(true);
        grid = new Grid<>(store, cm);
        grid.getView().setForceFit(true);
        grid.getView().setStripeRows(true);

        final GridInlineEditing<TableModel> editing = new GridInlineEditing<TableModel>(grid) {
            @Override
            protected void onScroll(ScrollEvent event) {
                // Suppress default action, which may result in canceling edit
            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(value, new DoubleField());
        for (Inds ind : Inds.values()) {
            store.add(new TableModel(ind));
        }
        container.add(grid, STD_VC_LAYOUT);
    }

    private TableModel getModel(Inds ind) {
        return store.findModel(new TableModel(ind));
    }

    private void updateModel(Inds ind, Double value) {
        TableModel model = getModel(ind);
        model.setValue(value);
        store.update(model);
    }

    public Double getNPV() {
        return getModel(Inds.NPV).getValue();
    }

    public Double getIRR() {
        return getModel(Inds.IRR).getValue();
    }

    public Double getPB() {
        return getModel(Inds.PB).getValue();
    }

    public Double getPBDiscounted() {
        return getModel(Inds.PBDiscounted).getValue();
    }

    public Double getEBIDTA() {
        return getModel(Inds.EBIDTA).getValue();
    }

    public Double getWACC() {
        return getModel(Inds.WACC).getValue();
    }

    public void setNPV(Double value) {
        updateModel(Inds.NPV, value);
    }

    public void setIRR(Double value) {
        updateModel(Inds.IRR, value);
    }

    public void setPB(Double value) {
        updateModel(Inds.PB, value);
    }
    public void setPBDiscounted(Double value) {
        updateModel(Inds.PBDiscounted, value);
    }

    public void setEBIDTA(Double value) {
        updateModel(Inds.EBIDTA, value);
    }

    public void setWACC(Double value) {
        updateModel(Inds.WACC, value);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
