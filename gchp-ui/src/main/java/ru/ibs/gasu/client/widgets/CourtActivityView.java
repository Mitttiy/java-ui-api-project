package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
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
import ru.ibs.gasu.common.models.CourtActivityModel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class CourtActivityView implements IsWidget {

    private VerticalLayoutContainer container;
    private Grid<CourtActivityModel> grid;

    public Grid<CourtActivityModel> getGrid() {
        return grid;
    }

    public CourtActivityView() {
        initWidget();
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        HBoxLayoutContainer buttonBar = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        ToolbarButton addButton = new ToolbarButton("Добавить активность", "fas fa-plus");
        ToolbarButton removeButton = new ToolbarButton("Удалить активность", "fas fa-trash");
        buttonBar.add(addButton, boxLayoutData);
        buttonBar.add(removeButton, boxLayoutData);

        ListStore<CourtActivityModel> store = new ListStore<>(new ModelKeyProvider<CourtActivityModel>() {
            @Override
            public String getKey(CourtActivityModel item) {
                return String.valueOf(item.getGid());
            }
        });

        ColumnConfig<CourtActivityModel, String> date = new ColumnConfig<>(new ValueProvider<CourtActivityModel, String>() {
            @Override
            public String getValue(CourtActivityModel object) {
                return WidgetUtils.formatDate(object.getDate(), "dd.MM.yyyy");
            }

            @Override
            public void setValue(CourtActivityModel object, String value) {
                object.setDate(WidgetUtils.parseDateToLong(value));
            }

            @Override
            public String getPath() {
                return "date";
            }
        }, 100, "Дата");


        ColumnConfig<CourtActivityModel, String> disputeSubject = new ColumnConfig<>(new ValueProvider<CourtActivityModel, String>() {
            @Override
            public String getValue(CourtActivityModel object) {
                return object.getDisputeSubject();
            }

            @Override
            public void setValue(CourtActivityModel object, String value) {
                object.setDisputeSubject(value);
            }

            @Override
            public String getPath() {
                return "disputeSubject";
            }
        }, 200, "Предмет спора");

        ColumnConfig<CourtActivityModel, String> judgement = new ColumnConfig<>(new ValueProvider<CourtActivityModel, String>() {
            @Override
            public String getValue(CourtActivityModel object) {
                return object.getJudgment();
            }

            @Override
            public void setValue(CourtActivityModel object, String value) {
                object.setJudgment(value);
            }

            @Override
            public String getPath() {
                return "judgement";
            }
        }, 200, "Судебное решение");

        ColumnConfig<CourtActivityModel, String> kadArbitrRuUrl = new ColumnConfig<>(new ValueProvider<CourtActivityModel, String>() {
            @Override
            public String getValue(CourtActivityModel object) {
                return object.getKadArbitrRuUrl();
            }

            @Override
            public void setValue(CourtActivityModel object, String value) {
                object.setKadArbitrRuUrl(value);
            }

            @Override
            public String getPath() {
                return "kadArbitrRuUrl";
            }
        }, 200, "Ссылка на kad.arbitr.ru");

        ColumnConfig<CourtActivityModel, String> comment = new ColumnConfig<>(new ValueProvider<CourtActivityModel, String>() {
            @Override
            public String getValue(CourtActivityModel object) {
                return object.getComment();
            }

            @Override
            public void setValue(CourtActivityModel object, String value) {
                object.setComment(value);
            }

            @Override
            public String getPath() {
                return "comment";
            }
        }, 200, "Комментарий");
        comment.setCell(new AbstractCell<String>() {
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
        comment.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);


        ColumnModel<CourtActivityModel> cm = new ColumnModel<>(
                Arrays.asList(date, disputeSubject, judgement, kadArbitrRuUrl, comment));
        store.setAutoCommit(true);

        grid = new Grid<>(store, cm);
        grid.getView().setForceFit(true);
        grid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        grid.getView().setStripeRows(true);

        final GridInlineEditing<CourtActivityModel> editing = new GridInlineEditing<CourtActivityModel>(grid) {
            @Override
            protected void onScroll(ScrollEvent event) {
            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(date, WidgetUtils.createStringDateConverter(), new DateField());
        editing.addEditor(disputeSubject, new TextField());
        editing.addEditor(judgement, new TextField());
        editing.addEditor(kadArbitrRuUrl, new TextField());
        editing.addEditor(comment, new TextField());
        grid.getStore().addSortInfo(new Store.StoreSortInfo<CourtActivityModel>(new Comparator<CourtActivityModel>() {
            @Override
            public int compare(CourtActivityModel o1, CourtActivityModel o2) {
                if (o1.getGid() > o2.getGid()) return 1;
                else if (o1.getGid() < o2.getGid()) return -1;
                else return 0;
            }
        }, SortDir.DESC));
        addButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                CourtActivityModel eventModel = new CourtActivityModel();
                eventModel.setGid(getRandId());
                grid.getStore().add(eventModel);
            }
        });

        removeButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                grid.getStore().remove(grid.getSelectionModel().getSelectedItem());
            }
        });
        container.add(buttonBar, STD_VC_LAYOUT);
        container.add(grid, STD_VC_LAYOUT);

    }


    @Override
    public Widget asWidget() {
        return container;
    }
}
