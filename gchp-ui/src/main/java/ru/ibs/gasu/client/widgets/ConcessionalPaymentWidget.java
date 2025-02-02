package ru.ibs.gasu.client.widgets;

import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.StringComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.common.models.PaymentByYearModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static ru.ibs.gasu.client.widgets.WidgetUtils.GRID_EMPTY_TEXT_VAR1;
import static ru.ibs.gasu.client.widgets.WidgetUtils.STD_VC_LAYOUT;

public class ConcessionalPaymentWidget implements IsWidget {
    private VerticalLayoutContainer container;
    private Grid<PaymentByYearModel> eemGrid;
    private TextButton addYearButton;
    private TextButton removeYearButton;
    private StringComboBox yearBox;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public void setContainer(VerticalLayoutContainer container) {
        this.container = container;
    }

    public Grid<PaymentByYearModel> getEemGrid() {
        return eemGrid;
    }

    public void setEemGrid(Grid<PaymentByYearModel> eemGrid) {
        this.eemGrid = eemGrid;
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

    public StringComboBox getYearBox() {
        return yearBox;
    }

    public void setYearBox(StringComboBox yearBox) {
        this.yearBox = yearBox;
    }

    public ConcessionalPaymentWidget() {
        initWidget();
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        yearBox = new StringComboBox(generateComboBoxYears());
        yearBox.setValue(getCurrentYear());
        yearBox.setEditable(false);
        yearBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        ToolBar buttonsContainer = new ToolBar();
        addYearButton = new IconButton("Добавить год", "fas fa-calendar-plus-o");
        removeYearButton = new IconButton("Удалить год", "fas fa-calendar-minus-o");
        buttonsContainer.add(yearBox);
        buttonsContainer.add(addYearButton);
        buttonsContainer.add(removeYearButton);

        ListStore<PaymentByYearModel> eemStore = new ListStore<>(new ModelKeyProvider<PaymentByYearModel>() {
            @Override
            public String getKey(PaymentByYearModel item) {
                return String.valueOf(item.getYear());
            }
        });

        eemStore.setAutoCommit(true);
        ColumnConfig<PaymentByYearModel, Integer> period = new ColumnConfig<>(new ValueProvider<PaymentByYearModel, Integer>() {
            @Override
            public Integer getValue(PaymentByYearModel object) {
                return object.getYear();
            }

            @Override
            public void setValue(PaymentByYearModel object, Integer value) {
                // не редактируемое
            }

            @Override
            public String getPath() {
                return "period";
            }
        }, 10, "Год");
        ColumnConfig<PaymentByYearModel, String> plan = new ColumnConfig<>(new ValueProvider<PaymentByYearModel, String>() {
            @Override
            public String getValue(PaymentByYearModel object) {
                return WidgetUtils.doubleToString(object.getPlan());
            }

            @Override
            public void setValue(PaymentByYearModel object, String value) {
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

        ColumnConfig<PaymentByYearModel, String> fact = new ColumnConfig<>(new ValueProvider<PaymentByYearModel, String>() {
            @Override
            public String getValue(PaymentByYearModel object) {
                return WidgetUtils.doubleToString(object.getFact());
            }

            @Override
            public void setValue(PaymentByYearModel object, String value) {
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


        ColumnModel<PaymentByYearModel> eemCm = new ColumnModel<>(Arrays.asList(period, plan, fact));

        eemGrid = new Grid<>(eemStore, eemCm);
        eemGrid.getView().setForceFit(true);
        eemGrid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        eemGrid.getView().setStripeRows(true);

        final GridInlineEditing<PaymentByYearModel> editing = new GridInlineEditing<PaymentByYearModel>(eemGrid) {
            @Override
            protected void onScroll(ScrollEvent event) {
            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(plan, WidgetUtils.createEditField());
        editing.addEditor(fact, WidgetUtils.createEditField());
        container.add(buttonsContainer, STD_VC_LAYOUT);
        container.add(eemGrid, STD_VC_LAYOUT);

        addYearButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Integer selectedYear = Integer.valueOf(yearBox.getValue());
                if (eemGrid.getStore().getAll().stream().anyMatch(i -> selectedYear.equals(i.getYear()))) {
                    new MessageBox("Ошибка", "Данный год уже есть в показателях").show();
                    return;
                }
                PaymentByYearModel model = new PaymentByYearModel();
                model.setYear(selectedYear);
                eemGrid.getStore().add(model);
            }
        });

        removeYearButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                PaymentByYearModel modelByYear = eemGrid.getStore().findModelWithKey(yearBox.getValue());
                if (modelByYear != null) {
                    eemGrid.getStore().remove(modelByYear);
                }
            }
        });
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
}
