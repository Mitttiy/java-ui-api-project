package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.state.client.GridFilterStateHandler;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.DateFilter;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import ru.ibs.gasu.common.soap.generated.gchpdocs.ProjectDetailsRevision;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static ru.ibs.gasu.client.widgets.WidgetUtils.formatDate;
import static ru.ibs.gasu.client.widgets.WidgetUtils.getDate;

public class EditionsView implements IsWidget {
    private VerticalLayoutContainer container;
    private Grid<ProjectDetailsRevision> grid;
    private GridFilters<ProjectDetailsRevision> filters;

    public Grid<ProjectDetailsRevision> getRevisions() {
        return grid;
    }

    public EditionsView() {
        container = new VerticalLayoutContainer();
        container.add(createRevisionsGrid(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    private Grid<ProjectDetailsRevision> createRevisionsGrid() {
        filters = new GridFilters<>();
        ListStore<ProjectDetailsRevision> store = new ListStore<>(revision -> String.valueOf(revision.getKey()));
        ColumnModel<ProjectDetailsRevision> columnModel = new ColumnModel<>(getColumns());

        grid = new Grid<>(store, columnModel);
        grid.getView().setEmptyText("Нет данных для отображения");
        grid.getView().setForceFit(true);
        grid.getView().setStripeRows(true);
        filters.initPlugin(grid);
        filters.setLocal(true);
        GridFilterStateHandler<ProjectDetailsRevision> handler = new GridFilterStateHandler<>(grid, filters);
        handler.loadState();
        return grid;
    }

    private List<ColumnConfig<ProjectDetailsRevision, ?>> getColumns() {
        ColumnConfig<ProjectDetailsRevision, Date> dateColumn = createDateColumn();
        ColumnConfig<ProjectDetailsRevision, String> editorColumn = createColumn(ProjectDetailsRevision::getEditor, 100, "Кем изменено");
        ColumnConfig<ProjectDetailsRevision, String> sectionColumn = createColumn(ProjectDetailsRevision::getEditedSection, 100, "Раздел");
        ColumnConfig<ProjectDetailsRevision, String> fieldColumn = createColumn(this::getEditedField, 150, "Измененое поле");
        ColumnConfig<ProjectDetailsRevision, String> prevValueColumn = createColumn(ProjectDetailsRevision::getPrevValue, 300, "Предудущее значение");
        ColumnConfig<ProjectDetailsRevision, String> currValueColumn = createColumn(ProjectDetailsRevision::getCurrentValue, 300, "Новое значение");

        return Arrays.asList(dateColumn, editorColumn, sectionColumn, fieldColumn, prevValueColumn, currValueColumn);
    }

    private String getEditedField(ProjectDetailsRevision revision) {
        if (revision.getEditedSubSection().isEmpty() || Objects.equals(revision.getEditedSubSection(), revision.getEditedField())) {
            return revision.getEditedField();
        }

        return String.join(": ", revision.getEditedSubSection(), revision.getEditedField());
    }

    private ColumnConfig<ProjectDetailsRevision, String> createColumn(Function<ProjectDetailsRevision, String> function, int width, String header) {
        ColumnConfig<ProjectDetailsRevision, String> columnConfig = new ColumnConfig<>(new ValueProvider<ProjectDetailsRevision, String>() {
            @Override
            public String getValue(ProjectDetailsRevision revision) {
                return function.apply(revision);
            }

            @Override
            public void setValue(ProjectDetailsRevision revision, String s) {
            }

            @Override
            public String getPath() {
                return header;
            }
        }, width, header);
        columnConfig.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String s, SafeHtmlBuilder safeHtmlBuilder) {
                safeHtmlBuilder.appendHtmlConstant(s);
            }
        });
        StringFilter<ProjectDetailsRevision> filter = new StringFilter<>(columnConfig.getValueProvider());
        filters.addFilter(filter);
        return columnConfig;
    }

    private ColumnConfig<ProjectDetailsRevision, Date> createDateColumn() {
        ColumnConfig<ProjectDetailsRevision, Date> columnConfig = new ColumnConfig<>(new ValueProvider<ProjectDetailsRevision, Date>() {
            @Override
            public Date getValue(ProjectDetailsRevision revision) {
                return getDate(revision.getDate());
            }

            @Override
            public void setValue(ProjectDetailsRevision revision, Date s) {
            }

            @Override
            public String getPath() {
                return "date";
            }
        }, 70, "Дата");
        columnConfig.setCell(new AbstractCell<Date>() {
            @Override
            public void render(Context context, Date s, SafeHtmlBuilder safeHtmlBuilder) {
                safeHtmlBuilder.appendHtmlConstant(formatDate(s, "dd.MM.yyyy"));
            }
        });
        DateFilter<ProjectDetailsRevision> filter = new DateFilter<>(columnConfig.getValueProvider());
        filters.addFilter(filter);
        return columnConfig;
    }
}
