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
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
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
import ru.ibs.gasu.common.models.EventModel;

import java.util.Arrays;
import java.util.Date;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class EventsView implements IsWidget {

    private VerticalLayoutContainer container;
    private Grid<EventModel> grid;

    public EventsView() {
        initWidget();
    }

    public Grid<EventModel> getGrid() {
        return grid;
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        HBoxLayoutContainer buttonBar = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        ToolbarButton addEventButton = new ToolbarButton("Добавить событие", "fas fa-plus");
        ToolbarButton removeButton = new ToolbarButton("Удалить", "fas fa-trash");
        buttonBar.add(addEventButton, boxLayoutData);
        buttonBar.add(removeButton, boxLayoutData);

        ListStore<EventModel> store = new ListStore<>(new ModelKeyProvider<EventModel>() {
            @Override
            public String getKey(EventModel item) {
                return String.valueOf(item.getGid());
            }
        });

        ColumnConfig<EventModel, String> date = new ColumnConfig<>(new ValueProvider<EventModel, String>() {
            @Override
            public String getValue(EventModel object) {
                return WidgetUtils.formatDate(object.getDate(), "dd.MM.yyyy");
            }

            @Override
            public void setValue(EventModel object, String value) {
                object.setDate(parseDateToLong(value));
            }

            @Override
            public String getPath() {
                return "date";
            }
        }, 100, "Дата");

        ColumnConfig<EventModel, String> name = new ColumnConfig<>(new ValueProvider<EventModel, String>() {
            @Override
            public String getValue(EventModel object) {
                return object.getName();
            }

            @Override
            public void setValue(EventModel object, String value) {
                object.setName(value);
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 200, "Наименования события");
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

        ColumnConfig<EventModel, String> description = new ColumnConfig<>(new ValueProvider<EventModel, String>() {
            @Override
            public String getValue(EventModel object) {
                return object.getDescription();
            }

            @Override
            public void setValue(EventModel object, String value) {
                object.setDescription(value);
            }

            @Override
            public String getPath() {
                return "description";
            }
        }, 200, "Описание события");
        description.setCell(new AbstractCell<String>() {
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
        description.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

        ColumnModel<EventModel> cm = new ColumnModel<>(Arrays.asList(date, name, description));
        store.setAutoCommit(true);

        grid = new Grid<>(store, cm);
        grid.getView().setForceFit(true);
        grid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        grid.getView().setStripeRows(true);

        final GridInlineEditing<EventModel> editing = new GridInlineEditing<EventModel>(grid) {
            @Override
            protected void onScroll(ScrollEvent event) {
            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(date, new Converter<String, Date>() {
            @Override
            public String convertFieldValue(Date object) {
                return formatDate(object, "dd.MM.yyyy");
            }

            @Override
            public Date convertModelValue(String object) {
                return parseDate(object);
            }
        }, new DateField());
        editing.addEditor(name, new TextField());
        editing.addEditor(description, new TextField());

        addEventButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                EventModel eventModel = new EventModel();
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
        container.add(buttonBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 0, 10, 0)));
        container.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 0, 100, 0)));
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
