package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import ru.ibs.gasu.client.widgets.componens.ToolbarButton;
import ru.ibs.gasu.common.models.EnsureMethodModel;
import ru.ibs.gasu.common.models.SimpleIdNameModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class MethodsOfExecuteObligationsView implements IsWidget {

    private VerticalLayoutContainer container;
    private Grid<EnsureMethodModel> grid;

    public Grid<EnsureMethodModel> getGrid() {
        return grid;
    }

    public MethodsOfExecuteObligationsView() {
        initWidget();
    }

    public boolean containsMethod(Long methodId) {
        for (EnsureMethodModel model : grid.getStore().getAll()) {
            if (model.getEnsureMethodId().equals(methodId))
                return true;
        }
        return false;
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();

        HBoxLayoutContainer buttonBar = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        ToolbarButton addEventButton = new ToolbarButton("Добавить способ обеспечения", "fas fa-plus");
        ToolbarButton removeEventButton = new ToolbarButton("Удалить способ обеспечения", "fas fa-trash");
        buttonBar.add(addEventButton, boxLayoutData);
        buttonBar.add(removeEventButton, boxLayoutData);

        ListStore<EnsureMethodModel> store = new ListStore<>(new ModelKeyProvider<EnsureMethodModel>() {
            @Override
            public String getKey(EnsureMethodModel item) {
                return String.valueOf(item.getGid());
            }
        });

        ColumnConfig<EnsureMethodModel, EnsureMethodModel> methodType = new ColumnConfig<>(new ValueProvider<EnsureMethodModel, EnsureMethodModel>() {
            @Override
            public EnsureMethodModel getValue(EnsureMethodModel object) {
                return object;
            }

            @Override
            public void setValue(EnsureMethodModel object, EnsureMethodModel value) {
            }

            @Override
            public String getPath() {
                return "methodType";
            }
        }, 200, "Способ обеспечения обязательств концессионера/частного партнера");

        ActionCell.Delegate<EnsureMethodModel> methodTypeDelegate = new ActionCell.Delegate<EnsureMethodModel>() {
            @Override
            public void execute(EnsureMethodModel object) {
                new MethodsOfExeObligationDicWindow() {
                    @Override
                    protected void onSave(List<SimpleIdNameModel> picked) {
                        if (picked.size() != 0) {
                            object.setEnsureMethodId(Long.parseLong(picked.get(0).getId()));
                            object.setEnsureMethodName(picked.get(0).getName());
                            object.setSubmissionDate(null);
                            object.setRiskType(null);
                            object.setInfo(getInfoString(picked.get(0).getId()));
                        }
                        grid.getView().refresh(false);
                    }
                }.show();
            }
        };

        methodType.setCell(new ActionCell<EnsureMethodModel>("", methodTypeDelegate) {
            @Override
            public void render(Context context, EnsureMethodModel value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span style='cursor: pointer;'>");
                if (value == null || value.getEnsureMethodName() == null)
                    sb.appendHtmlConstant("Выберите способы обеспечения");
                else
                    sb.appendHtmlConstant(value.getEnsureMethodName());
                sb.appendHtmlConstant("</span>");
            }
        });


        ColumnConfig<EnsureMethodModel, String> riskType = new ColumnConfig<>(new ValueProvider<EnsureMethodModel, String>() {
            @Override
            public String getValue(EnsureMethodModel object) {
                Long ensureMethodId = object.getEnsureMethodId();
                if (ensureMethodId == null || ensureMethodId == 2L || ensureMethodId == 3L || ensureMethodId == 4L) {
                    return "-";
                }
                return object.getRiskType();
            }

            @Override
            public void setValue(EnsureMethodModel object, String value) {
                object.setRiskType(value);
            }

            @Override
            public String getPath() {
                return "riskType";
            }
        }, 200, "Тип риска / форма обеспечения");

        ColumnConfig<EnsureMethodModel, String> info = new ColumnConfig<>(new ValueProvider<EnsureMethodModel, String>() {
            @Override
            public String getValue(EnsureMethodModel object) {
                return object.getInfo();
            }

            @Override
            public void setValue(EnsureMethodModel object, String value) {
                object.setInfo(value);
            }

            @Override
            public String getPath() {
                return "info";
            }
        }, 20, "");
        info.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String s, SafeHtmlBuilder sb) {
                if (s != null) {
                    sb.appendHtmlConstant(s);
                }
            }
        });
        info.setMenuDisabled(true);
        info.setSortable(false);

        ColumnConfig<EnsureMethodModel, String> date = new ColumnConfig<>(new ValueProvider<EnsureMethodModel, String>() {
            @Override
            public String getValue(EnsureMethodModel object) {
                return object.getTerm();
            }

            @Override
            public void setValue(EnsureMethodModel object, String value) {
                object.setTerm(value);
            }

            @Override
            public String getPath() {
                return "date";
            }
        }, 200, "Срок обеспечения");

        ColumnConfig<EnsureMethodModel, String> submissionDate = new ColumnConfig<>(new ValueProvider<EnsureMethodModel, String>() {
            @Override
            public String getValue(EnsureMethodModel object) {
                if (object.getEnsureMethodId() == null || object.getEnsureMethodId() != 2L) {
                    return "-";
                }
                return WidgetUtils.formatDate(object.getSubmissionDate(), "dd.MM.yyyy");
            }


            @Override
            public void setValue(EnsureMethodModel object, String value) {
                object.setSubmissionDate(parseDateToLong(value));
            }

            @Override
            public String getPath() {
                return "submissionDate";
            }
        }, 200, "Планируемая дата представления");


        ColumnConfig<EnsureMethodModel, String> sum = new ColumnConfig<>(new ValueProvider<EnsureMethodModel, String>() {
            @Override
            public String getValue(EnsureMethodModel object) {
                return doubleToString(object.getValue());
            }

            @Override
            public void setValue(EnsureMethodModel object, String value) {
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
        }, 200, "Размер обеспечения, тыс. рублей");

//        ColumnModel<EnsureMethodModel> cm = new ColumnModel<>(Arrays.asList(methodType, riskType, date, sum, submissionDate));
        ColumnModel<EnsureMethodModel> cm = new ColumnModel<>(Arrays.asList(methodType, info, date, sum));
        store.setAutoCommit(true);

        grid = new Grid<>(store, cm);
        grid.getView().setForceFit(true);
        grid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        grid.getView().setStripeRows(true);

        final GridInlineEditing<EnsureMethodModel> editing = new GridInlineEditing<EnsureMethodModel>(grid) {
            @Override
            protected void onScroll(ScrollEvent event) {
            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(date, new TextField());
//        editing.addEditor(riskType, new TextField());
        editing.addEditor(sum, WidgetUtils.createEditField());
//        editing.addEditor(riskType, new TextField());
//        editing.addEditor(submissionDate, createStringDateConverter(), new DateField());

        grid.getStore().addSortInfo(new Store.StoreSortInfo<EnsureMethodModel>(new Comparator<EnsureMethodModel>() {
            @Override
            public int compare(EnsureMethodModel o1, EnsureMethodModel o2) {
                if (o1.getGid() > o2.getGid()) return 1;
                else if (o1.getGid() < o2.getGid()) return -1;
                else return 0;
            }
        }, SortDir.DESC));
        addEventButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                EnsureMethodModel eventModel = new EnsureMethodModel();
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

        editing.addBeforeStartEditHandler(new BeforeStartEditEvent.BeforeStartEditHandler<EnsureMethodModel>() {
            @Override
            public void onBeforeStartEdit(BeforeStartEditEvent<EnsureMethodModel> event) {
                EnsureMethodModel selectedItem = event.getSource().getEditableGrid()
                        .getSelectionModel().getSelectedItem();
                Long ensureMethodId = selectedItem.getEnsureMethodId();
                if (ensureMethodId == null) {
                    event.setCancelled(true);
                    return;
                }
                if ((ensureMethodId == 2L || ensureMethodId == 3L || ensureMethodId == 4L) && event.getEditCell().getCol() == 1) {
                    event.setCancelled(true);
                }
                if (ensureMethodId != 2L && event.getEditCell().getCol() == 4) {
                    event.setCancelled(true);
                }
            }
        });

        container.add(buttonBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        container.add(grid, STD_VC_LAYOUT);
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

    public String getInfoString(String ensureMethodId) {
        switch (ensureMethodId) {
            case "2":
                return "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                        "title='Указывается сумма, на которую предоставлена безотзывная банковская гарантия'></i>";
            case "3":
                return "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                        "title='Указывается сумма, на которую предоставлен залог прав по договору банковского счёта'></i>";
            case "4":
                return "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                        "title='Указывается сумма страхового возмещения, которая может быть получена в случае  случайного повреждения (гибели) объекта соглашения'></i>";
            case "5":
                return "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                        "title='Указывается сумма страхового возмещения, которая может быть получена в случае наступления иных страховых случаев'></i>";
            case "6":
                return "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                        "title='Указывается наименование иного способа обеспечения обязательств концессионера / частного партнёра и сумма такого обеспечения'></i>";
            default:
                return "";
        }
    }
}
