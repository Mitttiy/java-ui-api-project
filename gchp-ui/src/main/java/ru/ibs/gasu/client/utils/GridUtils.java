package ru.ibs.gasu.client.utils;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import ru.ibs.gasu.client.widgets.WidgetUtils;

public class GridUtils {
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * Создать простую ячейку с текстом.
     */
    public static AbstractCell<String> createSimpleStringCell() {
        return new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                if (value != null) {
                    sb.appendHtmlConstant(value);
                }
            }
        };
    }

    /**
     * Создать столбец для таблицы содержащей данные показателях.
     */
    public static <T, K> ColumnConfig<T, K> createColumn(ValueProvider<T, K> valueProvider, int width, String header) {
        ColumnConfig<T, K> column = new ColumnConfig<>(valueProvider, width, WidgetUtils.wrapString(header));
        column.setSortable(true);
        column.setResizable(true);
        return column;
    }

    /**
     * Создать контейнер с toolbar
     */
    public static <T> HorizontalLayoutContainer createToolBarContainer(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader) {
        return createToolBarContainer(loader, DEFAULT_PAGE_SIZE);
    }

    /**
     * Создать контейнер с toolbar
     */
    public static <T> HorizontalLayoutContainer createToolBarContainer(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader, int defaultPageSize) {
        HorizontalLayoutContainer toolBarContainer = new HorizontalLayoutContainer();

        PagingToolBar toolBar = new PagingToolBar(defaultPageSize);
        toolBar.setBorders(false);
        toolBar.bind(loader);

        toolBarContainer.add(toolBar, new HorizontalLayoutContainer.HorizontalLayoutData(1, -1, new Margins(0, 0, 0, 0)));

        return toolBarContainer;
    }

}
