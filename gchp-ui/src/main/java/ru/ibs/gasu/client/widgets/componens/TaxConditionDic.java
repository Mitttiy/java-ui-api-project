package ru.ibs.gasu.client.widgets.componens;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.cell.core.client.form.FieldCell;
import com.sencha.gxt.cell.core.client.form.TextAreaInputCell;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.core.client.*;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.theme.triton.client.base.field.Css3TextFieldAppearance;
import com.sencha.gxt.theme.triton.client.base.field.Css3ValueBaseFieldAppearance;
import com.sencha.gxt.themebuilder.base.client.config.FieldDetails;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.RowClickEvent;
import com.sencha.gxt.widget.core.client.event.RowMouseDownEvent;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;
import ru.ibs.gasu.client.widgets.WidgetUtils;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiComboBoxTriggerField;
import ru.ibs.gasu.common.models.TaxConditionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaxConditionDic  extends VerticalLayoutContainer implements IsField<List<TaxConditionModel>> {

    public static class MultiFieldAppearance extends Css3ValueBaseFieldAppearance implements TriggerFieldCell.TriggerFieldAppearance {
        public interface Css3TriggerFieldResources extends Css3ValueBaseFieldResources, ClientBundle {
            @Override
            @Source({"Css3ValueBaseField.gss", "Css3TextField.gss", "Css3TriggerField.gss"})
            TaxConditionDic.MultiFieldAppearance.Css3TriggerFieldStyle style();

            @Source("icon-select-gasu.png")
            ImageResource triggerArrow();

            @Source("icon-select-gasu.png")
            ImageResource triggerArrowClick();

            @Source("icon-select-gasu-over.png")
            ImageResource triggerArrowOver();
        }

        public interface Css3TriggerFieldStyle extends Css3TextFieldAppearance.Css3TextFieldStyle {
            String click();

            String noedit();

            String over();

            String trigger();
        }

        private final TaxConditionDic.MultiFieldAppearance.Css3TriggerFieldResources resources;
        private final TaxConditionDic.MultiFieldAppearance.Css3TriggerFieldStyle style;

        public MultiFieldAppearance() {
            this(GWT.<TaxConditionDic.MultiFieldAppearance.Css3TriggerFieldResources>create(TaxConditionDic.MultiFieldAppearance.Css3TriggerFieldResources.class));
        }

        public MultiFieldAppearance(TaxConditionDic.MultiFieldAppearance.Css3TriggerFieldResources resources) {
            super(resources);

            this.resources = resources;
            this.style = resources.style();
        }

        @Override
        public XElement getInputElement(Element parent) {
            return parent.<XElement>cast().selectNode("input");
        }

        @Override
        public void onFocus(Element parent, boolean focus) {
            parent.<XElement>cast().setClassName(getResources().style().focus(), focus);
        }

        @Override
        public void onResize(XElement parent, int width, int height, boolean hideTrigger) {
            if (width != -1) {
                width = Math.max(0, width);
                parent.getFirstChildElement().getStyle().setPropertyPx("width", width);
            }
        }

        @Override
        public void onTriggerClick(XElement parent, boolean click) {
            parent.setClassName(getResources().style().click(), click);
        }

        @Override
        public void onTriggerOver(XElement parent, boolean over) {
            parent.setClassName(getResources().style().over(), over);
        }

        @Override
        public void render(SafeHtmlBuilder sb, String value, FieldCell.FieldAppearanceOptions options) {
            int width = options.getWidth();
            boolean hideTrigger = options.isHideTrigger();

            if (width == -1) {
                width = 150;
            }

            SafeStylesBuilder inputStylesBuilder = new SafeStylesBuilder();
            inputStylesBuilder.appendTrustedString("width:100%;");

            // outer div needed for widgets like comboBox that need the full width to set for listview width
            sb.appendHtmlConstant("<div style='width:" + width + "px;'>");

            if (hideTrigger) {
                sb.appendHtmlConstant("<div class='" + style.wrap() + "'>");
                renderInput(sb, value, inputStylesBuilder.toSafeStyles(), options);
            } else {
                FieldDetails fieldDetails = getResources().theme().field();
                int rightPadding = fieldDetails.padding().right();
                sb.appendHtmlConstant("<div class='" + style.wrap() + "' style='padding-right:" + (getResources().triggerArrow().getWidth() + rightPadding) + "px;'>");
                renderInput(sb, value, inputStylesBuilder.toSafeStyles(), options);

                int fieldHeight = fieldDetails.height();
                int right = fieldDetails.borderWidth() + 1;

                StringBuilder triggerStyleSB = new StringBuilder();
                // default height to the height of the input element for both desktop and touch
                triggerStyleSB.append("height:").append(fieldHeight).append("px;");
                // default right position for both desktop and touch
                triggerStyleSB.append("right:").append(right).append("px;");
                /*
                 * The height/width of the trigger is generated based off the dimensions of the image, which can negatively impact
                 * user experience on touch devices. For touch devices, we're going to use the height of the input element to create
                 * a large square around the trigger.
                 */
                if (GXT.isTouch()) {
                    // default width to height of input element to give touch users some extra width to work with
                    triggerStyleSB.append("width:").append(fieldHeight).append("px;");
                    // now that we've widened the trigger field, need to apply a margin so that it's positioned correctly
                    int deltaWidth = fieldHeight - getResources().triggerArrow().getWidth();
                    int rightMargin = -1 * (deltaWidth / 2);
                    triggerStyleSB.append("margin-right:").append(rightMargin).append("px;");
                }
                SafeStyles triggerStyle = SafeStylesUtils.fromTrustedString(triggerStyleSB.toString());
                sb.appendHtmlConstant("<div class='" + getStyle().trigger() + "' style='" + triggerStyle.asString() + "'></div>");
            }

            sb.appendHtmlConstant("</div></div>");
        }

        @Override
        public void setEditable(XElement parent, boolean editable) {
            getInputElement(parent).setClassName(getStyle().noedit(), !editable);
        }

        @Override
        public boolean triggerIsOrHasChild(XElement parent, Element target) {
            return parent.isOrHasChild(target) && target.<XElement>cast().is("." + getStyle().trigger());
        }


        protected TaxConditionDic.MultiFieldAppearance.Css3TriggerFieldResources getResources() {
            return resources;
        }

        protected TaxConditionDic.MultiFieldAppearance.Css3TriggerFieldStyle getStyle() {
            return style;
        }


        protected void renderInput(SafeHtmlBuilder shb, String value, SafeStyles inputStyles, FieldCell.FieldAppearanceOptions options) {
            StringBuilder sb = new StringBuilder();
            sb.append("<input ");

            if (options.isDisabled()) {
                sb.append("disabled=true ");
            }

            if (options.getName() != null) {
                sb.append("name='").append(SafeHtmlUtils.htmlEscape(options.getName())).append("' ");
            }

            if (options.isReadonly() || !options.isEditable()) {
                sb.append("readonly ");
            }

            if (inputStyles != null) {
                sb.append("style='").append(inputStyles.asString()).append("' ");
            }

            sb.append("class='").append(getStyle().field()).append(" ").append(getStyle().text());

            String placeholder = options.getEmptyText() != null ? " placeholder='" + SafeHtmlUtils.htmlEscape(options.getEmptyText()) + "' " : "";

            if ("".equals(value) && options.getEmptyText() != null) {
                sb.append(" ").append(getStyle().empty());
                if (GXT.isIE8() || GXT.isIE9()) {
                    value = options.getEmptyText();
                }
            }

            if (!options.isEditable()) {
                sb.append(" ").append(getStyle().noedit());
            }

            sb.append("' ");
            sb.append(placeholder);

            sb.append("type='text' value='").append(SafeHtmlUtils.htmlEscape(value)).append("' ");

            sb.append("/>");

            shb.append(SafeHtmlUtils.fromTrustedString(sb.toString()));
        }

    }


    public static class CloseButton extends TextButton {
        public CloseButton() {
            setPixelSize(25, 30);
            getCell().setHTML((new SafeHtmlBuilder())
                    .appendHtmlConstant("<div style='height:30px;right: -10px;top: 6px;position: absolute;width: 30px;" +
                            "background: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAY0lEQ" +
                            "VR42mNgGFRg6tSpPD09PSK45Pv7+wVwyoM0T5w4cTcQn8emCKR5woQJp3HJIyv4j64InxxBQ4jWjMsQkjTjMIQ0zR" +
                            "QbQJEXKApEiqORUEJBkr8PTHQSpCdVqDxOzQMGAPtxymx/ulYJAAAAAElFTkSuQmCC) -0px -0px no-repeat;'></div>")
                    .toSafeHtml());
            addStyleName("x-toolbar-mark");
        }
    }

    private TaxConditionDic.Templates templates = GWT.create(TaxConditionDic.Templates.class);

    interface Templates extends XTemplates {
        @XTemplate("<h3 style='font-family: \"Roboto\", sans-serif;'>Выбор налогового условия</h3>")
        SafeHtml header();

        @XTemplate("<i class='fa {icon}' aria-hidden='true'></i> {text}")
        SafeHtml button(String text, String icon);
    }

    private static int WINDOW_HEIGHT = 630;

    private TextArea textValues;

    private boolean showTextValues = true;

    private List<TaxConditionModel> values = new ArrayList<>();

    private Window window;

    private Grid<TaxConditionModel> grid;
    private List<ColumnConfig<TaxConditionModel, ?>> columns;
    private VerticalLayoutContainer container;
    //private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<TaxConditionModel>> loader;
    private ListStore<TaxConditionModel> store;
    private boolean isShowOnAttach = false;

    private void createWindow() {

        window = new Window();

        window.setWidth(800);
        window.setHeight(WINDOW_HEIGHT);

        VerticalLayoutContainer container = new VerticalLayoutContainer();

        HBoxLayoutContainer headerContainer = new HBoxLayoutContainer();

        BoxLayoutContainer.BoxLayoutData flex = new BoxLayoutContainer.BoxLayoutData(new Margins());
        flex.setFlex(1);
        headerContainer.add(new HTML(templates.header()), flex);

        TaxConditionDic.CloseButton closeButton = new TaxConditionDic.CloseButton() {
            @Override
            protected void onClick(Event event) {
                super.onClick(event);
                values = new ArrayList<>(oldValues);
                ValueChangeEvent.fire(TaxConditionDic.this, oldValues);
                window.hide();
            }
        };
        headerContainer.add(closeButton, new BoxLayoutContainer.BoxLayoutData(new Margins(10, 5, 0, 0)));
        container.add(headerContainer, new VerticalLayoutData(1, -1, new Margins(0, 0, 0, 10)));

        container.add(getGrid(), new VerticalLayoutData(1, -1, new Margins(0, 5, 0, 5)));

        if (showTextValues) {
            textValues = new TextArea();
            textValues.setResizable(TextAreaInputCell.Resizable.NONE);
            textValues.setReadOnly(true);
            textValues.setHeight(100);
            container.add(textValues, new VerticalLayoutData(1, -1, new Margins(10, 5, 0, 10)));
        }

        HBoxLayoutContainer buttonContainer = new HBoxLayoutContainer();
        buttonContainer.setPack(BoxLayoutContainer.BoxLayoutPack.END);
        buttonContainer.add(new Label(), flex);

        TextButton cancel = new TextButton() {
            @Override
            protected void onClick(Event event) {
                values = new ArrayList<>(oldValues);
                ValueChangeEvent.fire(TaxConditionDic.this, oldValues);
                window.hide();
            }
        };
        cancel.getCell().setHTML(templates.button("Отмена", "fa-ban"));
        cancel.setWidth(100);
        cancel.addStyleName("x-toolbar-mark");

        TextButton apply = new TextButton() {
            @Override
            protected void onClick(Event event) {
                onSelectionChange();
                ValueChangeEvent.fire(TaxConditionDic.this, getSelectedItems());
                window.hide();
            }
        };
        apply.setWidth(100);
        apply.getCell().setHTML(templates.button("Применить", "fa-check"));

        buttonContainer.add(apply, new BoxLayoutContainer.BoxLayoutData(new Margins(0, 15, 0, 0)));
        buttonContainer.add(cancel, new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0)));
        container.add(buttonContainer, new VerticalLayoutData(1, -1, new Margins(10, 0, 0, 0)));

        window.add(container, new VerticalLayoutData(-1, -1));
        window.setShadow(true);
        window.setModal(true);
        window.setBorders(false);
        window.setHeaderVisible(false);

        selectedItemsField.addTriggerClickHandler(event -> window.show());
        selectedItemsField.setEditable(false);

        add(selectedItemsField, new VerticalLayoutData(1, -1));
    }

    private MultiComboBoxTriggerField selectedItemsField = new MultiComboBoxTriggerField(new TriggerFieldCell<>(GWT.<TaxConditionDic.MultiFieldAppearance>create(TaxConditionDic.MultiFieldAppearance.class))) {
        @Override
        protected void onAttach() {
            super.onAttach();
            if (isShowOnAttach) {
                window.show();
            }
        }
    };

    public void setFilters(List<String> types) {
        clearStore();
        //loader.load();
    }

    public void clearFilters() {
        clearStore();
    }

    public TaxConditionDic() {
        createWindow();
    }

    public TaxConditionDic(Style.SelectionMode selectionMode, boolean isShowOnAttach) {
        this(selectionMode, isShowOnAttach, selectionMode == Style.SelectionMode.MULTI);
    }

    public TaxConditionDic(Style.SelectionMode selectionMode, boolean isShowOnAttach, boolean showTextValues) {
        this.isShowOnAttach = isShowOnAttach;
        this.selectionMode = selectionMode;
        this.showTextValues = showTextValues;
        createWindow();
        //loader.load();
    }

    private PagingToolBar toolBar;

    private void selectStore() {
        if (values.size() > 0) {
            for (TaxConditionModel participant : values) {
                select(participant);
            }
        }
    }

    public void clearStore() {
        selectionModel.deselectAll();
        grid.getStore().clear();
        values.clear();
        setValuesToTextField();
    }

    public void onLoad() {

    }

    private boolean loading = false;

    private VerticalLayoutContainer getGrid() {
        store = new ListStore<>(item -> String.valueOf(item.getId()));
        grid = new Grid<>(store, getColumnModel());
        grid.setLoadMask(true);
        grid.setSelectionModel(selectionModel);
        grid.getView().setEmptyText("Нет данных для отображения");
        grid.getView().setForceFit(true);
        grid.getView().setStripeRows(true);
        grid.setHeight(showTextValues ? 300 : 400);
        toolBar = new PagingToolBar(50);
        toolBar.setBorders(false);

        container = new VerticalLayoutContainer();
        container.add(grid, new VerticalLayoutData(1, -1, new Margins(0, 0, 0, 5)));
        container.add(toolBar, new VerticalLayoutData(1, -1, new Margins(5, 0, 0, 0)));

        return container;
    }

    private ColumnModel<TaxConditionModel> getColumnModel() {

        ColumnConfig<TaxConditionModel, String> nameColumn = new ColumnConfig<>(new ValueProvider<TaxConditionModel, String>() {
            @Override
            public String getValue(TaxConditionModel object) {
                return object.getName();
            }

            @Override
            public void setValue(TaxConditionModel object, String value) {

            }

            @Override
            public String getPath() {
                return null;
            }
        }, 500, WidgetUtils.wrapString("Налоговое условие"));
        nameColumn.setMenuDisabled(true);
        nameColumn.setSortable(false);

        IdentityValueProvider<TaxConditionModel> identity = new IdentityValueProvider<TaxConditionModel>();
        selectionModel = new CheckBoxSelectionModel<TaxConditionModel>(identity) {
            @Override
            protected void onRowClick(RowClickEvent event) {
                XElement target = event.getEvent().getEventTarget().cast();
                boolean left = event.getEvent().getButton() == Event.BUTTON_LEFT;
                if (left && !this.getAppearance().isRowChecker(target)) {
                    controlSelection(listStore.get(event.getRowIndex()));
                }
            }

            @Override
            protected void onRowMouseDown(RowMouseDownEvent event) {
                XElement target = event.getEvent().getEventTarget().cast();
                boolean left = event.getEvent().getButton() == Event.BUTTON_LEFT;
                if (left && this.getAppearance().isRowChecker(target)) {
                    controlSelection(listStore.get(event.getRowIndex()));
                }
            }
        };

        selectionModel.setSelectionMode(selectionMode);
        selectionModel.addSelectionChangedHandler(simpleValueSelectionChangedEvent -> {
            if (!loading) {
                updateSelection();
            }
        });

        columns = new ArrayList<>();
        if (selectionMode.equals(Style.SelectionMode.MULTI)) {
            columns.add(selectionModel.getColumn());
        }
        columns.add(nameColumn);

        return new ColumnModel<>(columns);
    }

    private Style.SelectionMode selectionMode = Style.SelectionMode.SINGLE;

    public void select(TaxConditionModel item) {
        select(Long.toString(item.getId()));
    }

    public void selectAll(List<TaxConditionModel> items) {
        List<TaxConditionModel> selected = new ArrayList<>(items);
        selectionModel.deselectAll();
        selected.forEach(item -> select(Long.toString(item.getId())));
        updateSelection();
    }

    public void select(String key) {
        TaxConditionModel item = store.findModelWithKey(key);
        if (item != null && !selectionModel.isSelected(item)) {
            selectionModel.select(true, item);
        }
    }

    private CheckBoxSelectionModel<TaxConditionModel> selectionModel;

    private void updateSelection() {
        if (selectionMode == Style.SelectionMode.SINGLE) {
            values.clear();
            if (selectionModel.getSelectedItem() != null) {
                values.add(selectionModel.getSelectedItem());
            }
        } else {
            Map<Long, TaxConditionModel> prevSelection = values.stream().collect(Collectors.toMap(item -> item.getId(), item -> item));
            for (TaxConditionModel item : grid.getStore().getAll()) {
                if (!prevSelection.containsKey(item.getId()) && selectionModel.isSelected(item)) {
                    values.add(item);
                }
                if (prevSelection.containsKey(item.getId()) && !selectionModel.isSelected(item)) {
                    values.remove(prevSelection.get(item.getId()));
                }
            }
        }
        setValuesToTextField();
    }

    public void controlSelection(TaxConditionModel item) {
        if (item != null) {
            if (selectionModel.isSelected(item)) {
                selectionModel.deselect(item);
            } else {
                selectionModel.select(item, true);
            }
        }
    }

    private void setValuesToTextField() {
        String textValue = values.stream().map(um -> um.getName()).collect(Collectors.joining("; "));
        setValueAndTooltip(selectedItemsField, textValue);
        if (showTextValues) {
            setValueAndTooltip(textValues, textValue);
        }
    }

    private void setValueAndTooltip(ValueBaseField<String> field, String textValue) {
        field.setValue(textValue);
        if (textValue == null || textValue.isEmpty()) {
            field.removeToolTip();
        } else {
            field.setToolTip(textValue);
        }
    }

    public List<TaxConditionModel> getSelectedItems() {
        return values;
    }

    public TaxConditionModel getFirstValue() {
        if (values != null && values.size() > 0) return values.get(0);
        return null;
    }

    public void setSingleValue(TaxConditionModel umDomain) {
        if (umDomain != null) {
            values.clear();
            values.add(umDomain);
            onSelectionChange();
            setValuesToTextField();
        }
    }

    public void onSelectionChange() {
        if (values != null && values.size() > 0) {
            selectedItemsField.setValue(values.get(0).getName());
        }
    }

    public void setEmptyText(String text) {
        selectedItemsField.setEmptyText(text);
    }

    public ListStore<TaxConditionModel> getStore() {
        return store;
    }

    public void setStore(ListStore<TaxConditionModel> store) {
        this.store = store;
    }

    @Override
    public void clearInvalid() {
    }

    @Override
    public void finishEditing() {
    }

    @Override
    public List<EditorError> getErrors() {
        return null;
    }

    @Override
    public boolean isValid(boolean preventMark) {
        return true;
    }

    @Override
    public void reset() {
    }

    @Override
    public boolean validate(boolean preventMark) {
        return true;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<TaxConditionModel>> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    private List<TaxConditionModel> oldValues = new ArrayList<>();

    @Override
    public void setValue(List<TaxConditionModel> value) {
        oldValues = new ArrayList<>();
        values.clear();
        if (value.size() > 0) {
            values.addAll(value);
            oldValues.addAll(value);
        }
        Scheduler.get().scheduleDeferred(() -> selectAll(value));
    }

    @Override
    public List<TaxConditionModel> getValue() {
        return getSelectedItems();
    }

    @Override
    protected void onBlur(Event event) {
        // событие потери фокуса для Editor нужно игнорировать, иначе это приводит к спецэффектам в IE
        // (клик в любой область вокруг модального окна = blur => это приводит к отмене редактирования поля)
        // см. https://jira.ibs.ru/browse/DHGASU-2612
    }
}
