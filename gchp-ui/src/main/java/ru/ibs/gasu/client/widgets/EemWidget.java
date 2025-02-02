package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.StringComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import ru.ibs.gasu.client.widgets.componens.CellFileUploader;
import ru.ibs.gasu.client.widgets.componens.ToolbarButton;
import ru.ibs.gasu.common.models.EemModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static ru.ibs.gasu.client.widgets.WidgetUtils.GRID_EMPTY_TEXT_VAR1;
import static ru.ibs.gasu.client.widgets.WidgetUtils.STD_VC_LAYOUT;

public class EemWidget implements IsWidget {
    private VerticalLayoutContainer container;
    private Grid<EemModel> eemGrid;
    private TextButton addYearButton;
    private TextButton removeYearButton;
    private StringComboBox yearBox;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public void setContainer(VerticalLayoutContainer container) {
        this.container = container;
    }

    public Grid<EemModel> getEemGrid() {
        return eemGrid;
    }

    public void setEemGrid(Grid<EemModel> eemGrid) {
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

    public EemWidget() {
        initWidget();
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        yearBox = new StringComboBox(generateComboBoxYears());
        yearBox.setValue(getCurrentYear());
        yearBox.setEditable(false);
        yearBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);

        HBoxLayoutContainer buttonsContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        addYearButton = new ToolbarButton("Добавить год", "fas fa-calendar-plus-o");
        removeYearButton = new ToolbarButton("Удалить год", "fas fa-calendar-minus-o");

        buttonsContainer.add(addYearButton, boxLayoutData);
        buttonsContainer.add(removeYearButton, boxLayoutData);

        SeparatorToolItem separator = new SeparatorToolItem();
        separator.setHeight(ToolbarButton.DEFAULT_HEIGHT);
        buttonsContainer.add(separator, boxLayoutData);

        buttonsContainer.add(yearBox, boxLayoutData);

        ListStore<EemModel> eemStore = new ListStore<>(new ModelKeyProvider<EemModel>() {
            @Override
            public String getKey(EemModel item) {
                return String.valueOf(item.getPeriod());
            }
        });

        eemStore.setAutoCommit(true);
        ColumnConfig<EemModel, Integer> period = new ColumnConfig<>(new ValueProvider<EemModel, Integer>() {
            @Override
            public Integer getValue(EemModel object) {
                return object.getPeriod();
            }

            @Override
            public void setValue(EemModel object, Integer value) {
                // не редактируемое
            }

            @Override
            public String getPath() {
                return "period";
            }
        }, 10, "Отчетный период");
        ColumnConfig<EemModel, String> plan = new ColumnConfig<>(new ValueProvider<EemModel, String>() {
            @Override
            public String getValue(EemModel object) {
                return WidgetUtils.doubleToString(object.getPlanValue());
            }

            @Override
            public void setValue(EemModel object, String value) {
                if (value == null) {
                    object.setPlanValue(null);
                    return;
                }
                try {
                    object.setPlanValue(Double.parseDouble(value));
                } catch (Exception ex) {
                }
            }

            @Override
            public String getPath() {
                return "plan";
            }
        }, 10, "Итог по плану экономии");

        ColumnConfig<EemModel, String> fact = new ColumnConfig<>(new ValueProvider<EemModel, String>() {
            @Override
            public String getValue(EemModel object) {
                return WidgetUtils.doubleToString(object.getFactValue());
            }

            @Override
            public void setValue(EemModel object, String value) {
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
        }, 10, "Итог по факту экономии");


        ColumnConfig<EemModel, EemModel> file = new ColumnConfig<>(new ValueProvider<EemModel, EemModel>() {
            @Override
            public EemModel getValue(EemModel object) {
                return object;
            }

            @Override
            public void setValue(EemModel object, EemModel value) {
            }

            @Override
            public String getPath() {
                return "file";
            }
        }, 300, "Акт о приемке ЭЭМ");
        file.setFixed(true);
        file.setCell(new AbstractCell<EemModel>() {
            @Override
            public void render(Context context, EemModel value, SafeHtmlBuilder sb) {
                if (value != null && value.getFileModel() != null && value.getFileModel().getName() != null) {
                    sb.appendHtmlConstant(value.getFileModel().getName());
                } else {
                    sb.appendHtmlConstant("Файл не выбран");
                }
            }
        });

        ColumnModel<EemModel> eemCm = new ColumnModel<>(Arrays.asList(period, plan, fact, file/*, deleteFile*/));
        eemGrid = new Grid<>(eemStore, eemCm);
        eemGrid.getView().setForceFit(true);
        eemGrid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        eemGrid.getView().setStripeRows(true);

        final GridInlineEditing<EemModel> editing = new GridInlineEditing<EemModel>(eemGrid) {
            @Override
            protected void onScroll(ScrollEvent event) {
            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(plan, WidgetUtils.createEditField());
        editing.addEditor(fact, WidgetUtils.createEditField());
        editing.addEditor(file, new CellFileUploader(eemGrid, editing));
        container.add(buttonsContainer, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));
        container.add(eemGrid, STD_VC_LAYOUT);

        addYearButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                Integer selectedYear = Integer.valueOf(yearBox.getValue());
                if (eemGrid.getStore().getAll().stream().anyMatch(i -> selectedYear.equals(i.getPeriod()))) {
                    new MessageBox("Ошибка", "Данный год уже есть в показателях").show();
                    return;
                }
                EemModel model = new EemModel();
                model.setPeriod(selectedYear);
                eemGrid.getStore().add(model);
            }
        });

        removeYearButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                EemModel modelByYear = eemGrid.getStore().findModelWithKey(yearBox.getValue());
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

    public void toggle(boolean toggle) {
        if (toggle) {
            container.show();
        } else {
            container.hide();
        }
    }
}
