package ru.ibs.gasu.client.widgets.multiselectcombobox;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.*;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowMouseDownEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.grid.*;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

import java.util.ArrayList;
import java.util.List;

import static ru.ibs.gasu.client.widgets.WidgetUtils.GRID_EMPTY_TEXT_VAR1;

/**
 * Custom GXT component allowing selection of several elements in a combobox along with filtering of values
 * Custom rendering of values can be defined by providing a custom AbstractCell
 */
public class MultiSelectComboBox<T> extends VerticalLayoutContainer {

    /**
     * Number of values that will be displayed in the drop down list before adding a vertical scrollbar
     */
    private static final int NB_VALUES_WITHOUT_SCROLL = 7;

    private static final int SINGLE_ENTRY_HEIGHT = 33;

    private static final int PAGE_COUNT = 50;

    /**
     * Field displaying label of every checked values
     */
    private MultiComboBoxTriggerField selectedItemsField = new MultiComboBoxTriggerField();

    /**
     * Menu contains the "select all checkbox" and the filtering text field
     */
    private Menu menu = new Menu();

    /**
     * Grid with only one column containing the combobox elements
     */
    private Grid<T> grid;

    private ModelKeyProvider<T> keyProvider;

    private LabelProvider<T> labelProvider;

    private MultiSelectComboBoxSelectionModel<T> sm;

    private ColumnConfig<T, String> columnConfig;

    private boolean showPaging = true;

    /**
     * Cell used for rendering of values in the combobox drop down list
     */
    private AbstractCell cell;

    private boolean autoLoad;

    private boolean onSelectionChangeEnabled = true;

    public boolean getOnSelectionChangeEnabled() {
        return onSelectionChangeEnabled;
    }

    public ListStore<T> getStore() {
        return grid.getStore();
    }

    public interface MultiSelectComboBoxSelectionChangeHandler<U> {
        public void handleSelectionChange(List<U> selectedItems);
    }

    private MultiSelectComboBoxSelectionChangeHandler<T> selectionChangeHandler;

    public void addSelectionHandler(MultiSelectComboBoxSelectionChangeHandler<T> handler) {
        this.selectionChangeHandler = handler;
    }

    public void setOnSelectionChangeEnabled(boolean onSelectionChangeEnabled) {
        this.onSelectionChangeEnabled = onSelectionChangeEnabled;
    }

    /**
     * This default cell is the one used in case no custom cell has been specified in constructor
     */
    private AbstractCell defaultCell = new AbstractCell() {
        @Override
        public void render(Context context, Object value, SafeHtmlBuilder sb) {
            String label = labelProvider.getLabel(grid.getStore().get(context.getIndex()));
            sb.append(SafeHtmlUtils.fromTrustedString(label != null ? "<div style=\"font-size: 12px; white-space: normal;\">" + label + "</div>" : ""));
        }
    };

    private PagingLoader loader;
    private int currGridHeight = 60;
    private Style.SelectionMode selectionMode = Style.SelectionMode.MULTI;


    public MultiSelectComboBox(ModelKeyProvider<T> keyProvider, LabelProvider<T> labelProvider, PagingLoader loader) {
        this(keyProvider, labelProvider, loader, true);
    }

    public MultiSelectComboBox(ModelKeyProvider<T> keyProvider, LabelProvider<T> labelProvider, PagingLoader loader, boolean autoLoad) {
        this(keyProvider, labelProvider, loader, autoLoad, true, Style.SelectionMode.MULTI);
    }

    public MultiSelectComboBox(ModelKeyProvider<T> keyProvider, LabelProvider<T> labelProvider, PagingLoader loader, boolean autoLoad, boolean showPaging) {
        this(keyProvider, labelProvider, loader, autoLoad, showPaging, Style.SelectionMode.MULTI);
    }

    public void setLoader(PagingLoader loader1) {

        loader1.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, T, PagingLoadResult<T>>(store));
        loader1.addLoaderHandler(new LoaderHandler() {
            @Override
            public void onBeforeLoad(BeforeLoadEvent event) {
                MultiSelectComboBox.this.onLoaderBeforeLoad();
            }

            @Override
            public void onLoadException(LoadExceptionEvent event) {
                gridContainer.setWidth(MultiSelectComboBox.this.getOffsetWidth());
                currGridHeight = 100;
                grid.setHeight(currGridHeight);
                grid.unmask();
            }

            @Override
            public void onLoad(LoadEvent event) {
                gridContainer.setWidth(MultiSelectComboBox.this.getOffsetWidth());
                if (((PagingLoadResult) event.getLoadResult()).getTotalLength() > PAGE_COUNT) {
                    toolBar.setVisible(true);
                }
                MultiSelectComboBox.this.onLoaderLoad();
                refreshGridHeight();
                grid.unmask();
            }
        });
        toolBar.bind(loader1);
        this.loader = loader1;
        loader.load();
    }

    /**
     * Default constructor
     *
     * @param keyProvider
     * @param labelProvider
     */
    public MultiSelectComboBox(ModelKeyProvider<T> keyProvider, LabelProvider<T> labelProvider, PagingLoader loader, boolean autoLoad, boolean showPaging, Style.SelectionMode selectionMode) {
        this.keyProvider = keyProvider;
        this.labelProvider = labelProvider;
        this.autoLoad = autoLoad;
        this.showPaging = showPaging;
        this.selectionMode = selectionMode;

        cell = defaultCell;

        this.loader = loader;

        init();
    }

    private void init() {

        initGrid();

        menu.add(gridContainer);

        selectedItemsField.setReadOnly(false);

        selectedItemsField.setContextMenu(menu);

        selectedItemsField.addTriggerClickHandler(new TriggerClickEvent.TriggerClickHandler() {
            @Override
            public void onTriggerClick(TriggerClickEvent event) {
                XElement wrapper = selectedItemsField.getElement().cast();

                if (grid != null) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                        @Override
                        public void execute() {
                            grid.setWidth(wrapper.getClientWidth());
                        }
                    });
                }

                menu.setWidth(wrapper.getClientWidth());
                menu.setEnableScrolling(false);
                menu.show(wrapper, new Style.AnchorAlignment(Style.Anchor.TOP_LEFT, Style.Anchor.BOTTOM_LEFT, true), 0, 0);
            }
        });

        add(selectedItemsField, new VerticalLayoutData(1, -1));
    }

    private VerticalLayoutContainer gridContainer = new VerticalLayoutContainer();

    private PagingToolBar toolBar = new PagingToolBar(PAGE_COUNT);

    /**
     * Creation of the grid object required for rendering of values in the drop down list
     */
    private void initGrid() {
        IdentityValueProvider<T> identity = new IdentityValueProvider<>();

        // Check elements when clicking on the checkbox or on the row
        sm = new MultiSelectComboBoxSelectionModel<T>(identity) {
            @Override
            protected void onRowClick(RowClickEvent event) {
                boolean left = event.getEvent().getButton() == Event.BUTTON_LEFT;
                XElement target = event.getEvent().getEventTarget().cast();
                if (left && !this.getAppearance().isRowChecker(target)) {
                    controlSelection(listStore.get(event.getRowIndex()));
                }
            }

            @Override
            protected void onRowMouseDown(RowMouseDownEvent event) {
                boolean left = event.getEvent().getButton() == Event.BUTTON_LEFT;
                XElement target = event.getEvent().getEventTarget().cast();
                if (left && this.getAppearance().isRowChecker(target)) {
                    controlSelection(listStore.get(event.getRowIndex()));
                }
            }
        };

        // allow the user to select multiple values
        sm.setSelectionMode(selectionMode);

        // Manage selectAll check box depending on already selected values
        sm.addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<T>() {
            @Override
            public void onSelectionChanged(SelectionChangedEvent<T> simpleValueSelectionChangedEvent) {
                setValuesToTextField();
            }
        });

        ColumnModel columnModel = new ColumnModel(getColumnConfig());
        store = new ListStore<>(keyProvider);

        if (loader != null) {
            loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, T, PagingLoadResult<T>>(store));
            loader.addLoaderHandler(new LoaderHandler() {
                @Override
                public void onBeforeLoad(BeforeLoadEvent event) {
                    MultiSelectComboBox.this.onLoaderBeforeLoad();
                }

                @Override
                public void onLoadException(LoadExceptionEvent event) {
                    gridContainer.setWidth(MultiSelectComboBox.this.getOffsetWidth());
                    currGridHeight = 100;
                    grid.setHeight(currGridHeight);
                    grid.unmask();
                }

                @Override
                public void onLoad(LoadEvent event) {
                    gridContainer.setWidth(MultiSelectComboBox.this.getOffsetWidth());
                    if (((PagingLoadResult) event.getLoadResult()).getTotalLength() > PAGE_COUNT) {
                        toolBar.setVisible(true);
                    }
                    MultiSelectComboBox.this.onLoaderLoad();
                    refreshGridHeight();
                    grid.unmask();
                }
            });
        }


        grid = new Grid<T>(store, columnModel, new GridView<T>()) {
            @Override
            protected void onAfterRenderView() {
                super.onAfterRenderView();

                refreshGridHeight();
            }
        };
        grid.setSelectionModel(sm);
        grid.setAllowTextSelection(false);
        grid.setHideHeaders(true);
        grid.setBorders(false);
        grid.setLoadMask(true);
        currGridHeight = 60;
        grid.setHeight(currGridHeight);
        grid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);

        grid.getView().setColumnLines(false);
        grid.getView().setForceFit(true);

        if (showPaging) {
            toolBar.setBorders(false);
            if (loader != null) {
                toolBar.bind(loader);
            }
            toolBar.setVisible(false);
        }

        if (autoLoad) {
            grid.mask("Загрузка...");
            if (loader != null) {
                loader.load();
            }
        }

        gridContainer.add(grid, new VerticalLayoutData(1, 1));

        if (showPaging) gridContainer.add(toolBar, new VerticalLayoutData(1, -1));

        new QuickTip(grid);
    }

    private ListStore<T> store;

    public void setGridEmptyText(String text) {
        grid.getView().setEmptyText(text);
    }

    /**
     * @return the list of currently selected items
     */
    public List<T> getSelectedItems() {
        return sm.getSelectedItems();
    }

    /**
     * Add a list of checkable item in the drop down list
     *
     * @param list
     */
    public void addAll(List<T> list) {
        for (T v : list) {
            add(v);
        }
    }

    /**
     * Add a checkable item in the drop down list
     *
     * @param item the item to add
     */
    public void add(T item) {
        boolean alreadyExists = false;
        if (!alreadyExists) {
            grid.getStore().add(item);
        }
        refreshGridHeight();
    }

    /**
     * @return all items contained in the store
     */
    public List<T> getAllItems() {
        return grid.getStore().getAll();
    }

    public void clearStore() {
        grid.getStore().clear();
        refreshGridHeight();
    }

    private List<ColumnConfig> getColumnConfig() {
        List<ColumnConfig> configs = new ArrayList<>();

        // Width depends on the presence of a scrollbar
        columnConfig = new ColumnConfig(new IdentityValueProvider<T>());

        columnConfig.setCell(cell);

        columnConfig.setColumnStyle(new SafeStyles() {
            @Override
            public String asString() {
                return "border: none;";
            }
        });

        configs.add(sm.getColumn());
        configs.add(columnConfig);

        return configs;
    }

    /**
     * Adjust drop down list height depending on the number of elements in the list. A scrollbar is added if this number exceeds NB_VALUES_WITHOUT_SCROLL
     */
    public void refreshGridHeight() {

        Timer timer = new Timer() {
            @Override
            public void run() {

                try {

                    if (getAllItems().size() > NB_VALUES_WITHOUT_SCROLL) {

                        currGridHeight = NB_VALUES_WITHOUT_SCROLL * SINGLE_ENTRY_HEIGHT + (toolBar.isVisible() ? 40 : 0);
                    } else if (getAllItems().size() == 0) {

                        currGridHeight = 60;
                    } else {

                        currGridHeight = getAllItems().size() * SINGLE_ENTRY_HEIGHT + 5 + (toolBar.isVisible() ? 40 : 0);
                    }

                    grid.setHeight(currGridHeight);
                    XElement wrapper = selectedItemsField.getElement().cast();

                    grid.setWidth(wrapper.getClientWidth());
                } catch (Throwable error) {
                }

            }
        };

        timer.schedule(1);

    }

    /**
     * Switch the selection status of the given item
     *
     * @param item
     */
    private void controlSelection(T item) {
        if (item != null) {
            if (sm.isSelected(item)) {
                sm.deselect(item);
            } else {
                sm.select(item, true);
            }
        }
    }

    /**
     * Manage the label to display in the "selected item field". Label of each selected values is displayed or "All" if all elements are selected
     */
    public void setValuesToTextField() {
        if (sm.getSelectedItems().size() > 0) {
            if (isAllSelected() && sm.getSelectedItems().size() > 1) {
                selectedItemsField.setValue("Все");
                selectedItemsField.setToolTip("Все");
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < sm.getSelectedItems().size(); i++) {
                    sb.append(labelProvider.getLabel(sm.getSelectedItems().get(i)));
                    if (i != sm.getSelectedItems().size() - 1) {
                        sb.append(", ");
                    }
                }
                selectedItemsField.setValue(sb.toString());
                selectedItemsField.setToolTip(sb.toString());
            }
        } else {
            selectedItemsField.removeToolTip();
            selectedItemsField.setValue(null);
        }

        if (getOnSelectionChangeEnabled()) {
            onSelectionChange();
            if (selectionChangeHandler != null) {
                selectionChangeHandler.handleSelectionChange(sm.getSelectedItems());
            }
        }
    }

    protected void onSelectionChange() {

    }

    public void callOnSelectionChange() {
        onSelectionChange();
    }

    public void setEnabled(boolean enabled) {
        selectedItemsField.setEnabled(enabled);
    }

    public void setEditable(boolean enabled) {
        selectedItemsField.setEditable(enabled);
    }

    /**
     * Set the matching model item as checked
     *
     * @param item the item to check
     */
    public void select(T item) {
        if (!sm.isSelected(item)) {
            sm.select(true, item);
        }
    }

    public void select(String key) {
        T item = grid.getStore().findModelWithKey(key);
        if (item != null && !sm.isSelected(item)) {
            sm.select(true, item);
        }
    }

    public T getModel(String key) {
        return grid.getStore().findModelWithKey(key);
    }

    public boolean isSelected(T model) {
        return sm.isSelected(model);
    }

    /**
     * Set the matching model items as checked
     *
     * @param items the item to check
     */
    public void select(List<T> items) {
        for (T item : items) {
            this.select(item);
        }
    }

    /**
     * Check all items in the drop down list
     */
    public void selectAll() {
        for (T item : getAllItems()) {
            this.select(item);
        }
    }

    public void deselect(int index) {
        sm.deselect(sm.getSelectedItems().get(index));
    }

    public void deselect(String item) {
        sm.deselect((T) item);
    }

    /**
     * deselect all items in the drop down list
     */
    public void deselectAll() {
        sm.deselectAll();
    }

    /**
     * deselect all items in the drop down list
     * @param suppressEvent suppress event
     */
    public void deselectAll(boolean suppressEvent) {
        sm.deselectAllSuppressEvent(suppressEvent);
    }

    /**
     * Set the matching model item as checked
     *
     * @param items the item to check
     * @param suppressEvent suppress event
     */
    public void select(List<T> items, boolean suppressEvent) {
        sm.selectSuppressEvent(items, suppressEvent);
    }

    /**
     * @return true if all items are selected
     */
    public boolean isAllSelected() {
        return sm.getSelection().size() == getAllItems().size();
    }

    public void setEmptyText(String text) {
        selectedItemsField.setEmptyText(text);
    }

    public void setText(String text) {
        selectedItemsField.setValue(text);
        selectedItemsField.setToolTip(text);
    }

    public void load() {
        loader.load();
    }

    public PagingLoader getLoader() {
        return loader;
    }

    protected void onLoaderLoad() {
    }

    protected void onLoaderBeforeLoad() {
    }


}