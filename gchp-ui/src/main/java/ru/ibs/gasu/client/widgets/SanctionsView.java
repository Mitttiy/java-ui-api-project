package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import ru.ibs.gasu.client.widgets.componens.ToolbarButton;
import ru.ibs.gasu.common.models.SanctionModel;
import ru.ibs.gasu.common.models.SanctionTypeModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class SanctionsView implements IsWidget {

    private VerticalLayoutContainer container;
    private Grid<SanctionModel> grid;

    public Grid<SanctionModel> getGrid() {
        return grid;
    }

    public SanctionsView() {
        initWidget();
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();

        HBoxLayoutContainer buttonBar = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        ToolbarButton addEventButton = new ToolbarButton("Добавить санкцию", "fas fa-plus");
        ToolbarButton removeEventButton = new ToolbarButton("Удалить санкцию", "fas fa-trash");
        buttonBar.add(addEventButton, boxLayoutData);
        buttonBar.add(removeEventButton, boxLayoutData);

        ListStore<SanctionModel> store = new ListStore<>(new ModelKeyProvider<SanctionModel>() {
            @Override
            public String getKey(SanctionModel item) {
                return String.valueOf(item.getGid());
            }
        });

        ColumnConfig<SanctionModel, SanctionModel> sanctionTypes = new ColumnConfig<>(new ValueProvider<SanctionModel, SanctionModel>() {
            @Override
            public SanctionModel getValue(SanctionModel object) {
                return object;
            }

            @Override
            public void setValue(SanctionModel object, SanctionModel value) {
            }

            @Override
            public String getPath() {
                return "workTypes";
            }
        }, 200, "Виды санкций");
        ActionCell.Delegate<SanctionModel> sanctionTypesDelegate = new ActionCell.Delegate<SanctionModel>() {
            @Override
            public void execute(SanctionModel object) {
                new SanctionTypeDicWindow() {
                    @Override
                    protected void onSave(List<SanctionTypeModel> picked) {
                        object.getSanctionType().clear();
                        object.getSanctionType().addAll(picked);
                        grid.getView().refresh(false);
                    }
                }.show();
            }
        };

        sanctionTypes.setCell(new ActionCell<SanctionModel>("", sanctionTypesDelegate) {
            @Override
            public void render(Context context, SanctionModel value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span style='cursor: pointer;'>");
                if (value.getSanctionType().size() == 0)
                    sb.appendHtmlConstant("Выберите виды санкций");
                else
                    sb.appendHtmlConstant(value.getSanctionType().stream().map(i -> i.getName())
                            .collect(Collectors.joining("; ")));
                sb.appendHtmlConstant("</span>");
            }
        });

        ColumnConfig<SanctionModel, String> date = new ColumnConfig<>(new ValueProvider<SanctionModel, String>() {
            @Override
            public String getValue(SanctionModel object) {
                return WidgetUtils.formatDate(object.getDate(), "dd.MM.yyyy");
            }

            @Override
            public void setValue(SanctionModel object, String value) {
                object.setDate(parseDateToLong(value));
            }

            @Override
            public String getPath() {
                return "date";
            }
        }, 200, "Дата");


        ColumnConfig<SanctionModel, String> sum = new ColumnConfig<>(new ValueProvider<SanctionModel, String>() {
            @Override
            public String getValue(SanctionModel object) {
                return doubleToString(object.getValue());
            }

            @Override
            public void setValue(SanctionModel object, String value) {
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
                return "sum";
            }
        }, 200, "Сумма, тыс. руб");

        ColumnConfig<SanctionModel, String> cause = new ColumnConfig<>(new ValueProvider<SanctionModel, String>() {
            @Override
            public String getValue(SanctionModel object) {
                return object.getCause();
            }

            @Override
            public void setValue(SanctionModel object, String value) {
                object.setCause(value);
            }

            @Override
            public String getPath() {
                return "cause";
            }
        }, 200, "Причина");
        cause.setCell(new AbstractCell<String>() {
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
        cause.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);


        ColumnModel<SanctionModel> cm = new ColumnModel<>(Arrays.asList(sanctionTypes, date, sum, cause));
        store.setAutoCommit(true);

        grid = new Grid<>(store, cm);
        grid.getView().setForceFit(true);
        grid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        grid.getView().setStripeRows(true);


        final GridInlineEditing<SanctionModel> editing = new GridInlineEditing<SanctionModel>(grid) {
            @Override
            protected void onScroll(ScrollEvent event) {
            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(date, createStringDateConverter(), new DateField());
        editing.addEditor(sum, WidgetUtils.createEditField());
        editing.addEditor(cause, new TextField());
        grid.getStore().addSortInfo(new Store.StoreSortInfo<SanctionModel>(new Comparator<SanctionModel>() {
            @Override
            public int compare(SanctionModel o1, SanctionModel o2) {
                if (o1.getGid() > o2.getGid()) return 1;
                else if (o1.getGid() < o2.getGid()) return -1;
                else return 0;
            }
        }, SortDir.DESC));
        addEventButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                SanctionModel eventModel = new SanctionModel();
                eventModel.setGid(getRandId());
                grid.getStore().add(eventModel);

            }
        });

        removeEventButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                grid.getStore().remove(grid.getSelectionModel().getSelectedItem());
            }
        });

        container.add(buttonBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        container.add(grid, STD_VC_LAYOUT);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
