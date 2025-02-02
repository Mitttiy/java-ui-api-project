package ru.ibs.gasu.client.widgets;


import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import ru.ibs.gasu.client.utils.GridUtils;
import ru.ibs.gasu.client.widgets.componens.IconButton;

@SuppressWarnings("GwtToHtmlReferences")
public abstract class SelectObjectGridWindow<T> extends Dialog {
    private IconButton selectButton;
    private IconButton cancelButton;
    private Grid<T> grid;

    public SelectObjectGridWindow() {
        setModal(true);
        setHeading(createHeading());
        setHeight(750);
        setWidth(950);
        setClosable(false);
        setResizable(false);
        setShadow(false);
        setClosable(true);
        createButtonsContainer();
        add(createGridContainer(), new MarginData(5));
    }

    protected String createHeading() {
        return "Выберите объект";
    }

    protected abstract ListStore<T> createStore();

    protected abstract PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> createLoader();


    protected abstract ColumnModel<T> createColumnModel();

    protected GridFilters<T> createFilters(PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader) {
        GridFilters<T> filters = new GridFilters<>(loader);
        return filters;
    }

    private VerticalLayoutContainer createGridContainer() {
        ListStore<T> store = createStore();

        PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader = createLoader();
        loader.addLoadHandler(new LoadResultListStoreBinding<>(store));
        loader.setRemoteSort(true);
        loader.addBeforeLoadHandler(event -> grid.mask("Загрузка"));
        loader.addLoadHandler(event -> grid.unmask());

        ColumnModel<T> columnModel = createColumnModel();

        grid = new Grid<T>(store, columnModel);
        grid.setHeight(600);
        grid.addRowClickHandler(rowClickEvent -> {
            updateButtonState();
        });
        grid.addRowDoubleClickHandler(event -> select());
        createFilters(loader).initPlugin(grid);
        grid.setAllowTextSelection(true);
        grid.getView().setForceFit(true);

        VerticalLayoutContainer gridContainer = new VerticalLayoutContainer();
        gridContainer.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, -1));


        HorizontalLayoutContainer toolBarContainer = GridUtils.createToolBarContainer(loader);
        gridContainer.add(toolBarContainer, new VerticalLayoutContainer.VerticalLayoutData(1, 50, new Margins(0, 0, 0, 0)));

        VerticalLayoutContainer container = new VerticalLayoutContainer();
        container.add(gridContainer);
        loader.load();
        return container;
    }

    private void createButtonsContainer() {
        getButtonBar().clear();
        selectButton = new IconButton("Выбрать", "fa fa-check");
        selectButton.setWidth(100);
        selectButton.addSelectHandler(event -> select());
        selectButton.disable();

        cancelButton = new IconButton("Отмена", "fa fa-times");
        cancelButton.addSelectHandler(event -> cancel());
        cancelButton.setWidth(100);

        getButtonBar().add(selectButton);
        getButtonBar().add(cancelButton);
    }

    private void select() {
        onSelect(grid.getSelectionModel().getSelectedItem());
        SelectObjectGridWindow.this.hide();
    }

    private void cancel() {
        onCancel();
        SelectObjectGridWindow.this.hide();
    }

    protected void onSelect(T object) {
    }

    protected void onCancel() {
    }

    private void updateButtonState() {
        if (!selectButton.isEnabled()) {
            selectButton.enable();
        }
    }
}
