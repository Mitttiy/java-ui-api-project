package ru.ibs.gasu.client.widgets.componens;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.FieldCell;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.cell.core.client.form.TwinTriggerFieldCell;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.theme.triton.client.base.field.Css3TwinTriggerFieldAppearance;
import com.sencha.gxt.themebuilder.base.client.config.FieldDetails;
import com.sencha.gxt.widget.core.client.event.TwinTriggerClickEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class TwinTriggerComboBox<T> extends ComboBox<T> implements TwinTriggerClickEvent.HasTwinTriggerClickHandlers {

    private String savedEmptyText;

    public TwinTriggerComboBox(ListStore<T> store, LabelProvider<? super T> labelProvider, SafeHtmlRenderer<T> renderer, TriggerFieldCell.TriggerFieldAppearance appearance) {
        this(new CMCell<T>(store, labelProvider, renderer, appearance));
        addTwinTriggerClickHandler(event -> TwinTriggerComboBox.this.clear());
    }

    public TwinTriggerComboBox(ListStore<T> store, LabelProvider<? super T> labelProvider, SafeHtmlRenderer<T> renderer) {
        this(store, labelProvider, renderer, new CustomTwinTriggerFieldAppearance());
    }

    public TwinTriggerComboBox(ComboBoxCell<T> cell) {
        super(cell);
    }

    @Override
    public HandlerRegistration addTwinTriggerClickHandler(TwinTriggerClickEvent.TwinTriggerClickHandler handler) {
        return addHandler(handler, TwinTriggerClickEvent.getType());
    }

    public void maskLoad() {
        this.savedEmptyText = getEmptyText();
        setEmptyText("Загрузка...");
        super.mask();
    }

    public void unmaskLoad() {
        setEmptyText(this.savedEmptyText);
        super.unmask();
    }

    public static class CustomTwinTriggerFieldAppearance extends Css3TwinTriggerFieldAppearance {
        public interface CustomTwinTriggerFieldResources extends Css3TwinTriggerFieldResources {
            @Override
            @Source({"Css3ValueBaseField.gss", "Css3TextField.gss", "Css3TriggerField.gss", "CustomTwinTriggerField.gss"})
            CustomTwinTriggerFieldStyle style();

            @Override
            @Source("spinnerDown.png")
            ImageResource triggerArrow();

            @Override
            @Source("spinnerDownOver.png")
            ImageResource triggerArrowOver();

            @Override
            @Source("spinnerDownClick.png")
            ImageResource triggerArrowClick();

            @Source("clearTrigger.png")
            ImageResource twinTriggerArrow();

            @Source("clearTriggerOver.png")
            ImageResource twinTriggerArrowOver();

            @Source("clearTriggerClick.png")
            ImageResource twinTriggerArrowClick();
        }

        interface CustomTwinTriggerFieldStyle extends Css3TwinTriggerFieldStyle {

        }

        private final CustomTwinTriggerFieldAppearance.CustomTwinTriggerFieldResources resources;

        public CustomTwinTriggerFieldAppearance() {
            this(GWT.<CustomTwinTriggerFieldResources>create(CustomTwinTriggerFieldResources.class));
        }

        public CustomTwinTriggerFieldAppearance(CustomTwinTriggerFieldAppearance.CustomTwinTriggerFieldResources resources) {
            super(resources);
            this.resources = resources;
        }

        @Override
        protected int getTriggerWrapHeight() {
            return resources.triggerArrow().getHeight() + resources.twinTriggerArrow().getHeight();
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
            sb.appendHtmlConstant("<div style='width:" + width + "px;'>");
            if (hideTrigger) {
                sb.appendHtmlConstant("<div class='" + resources.style().wrap() + "'>");
                renderInput(sb, value, inputStylesBuilder.toSafeStyles(), options);
            } else {
                FieldDetails fieldDetails = getResources().theme().field();
                int rightPadding = fieldDetails.padding().right();
                sb.appendHtmlConstant("<div class='" + resources.style().wrap() + "' style='padding-right:" + 40 + "px;'>");
                renderInput(sb, value, inputStylesBuilder.toSafeStyles(), options);
                if (!GXT.isTouch()) {
                    int triggerWrapTopMargin = -(getTriggerWrapHeight() / 2);
                    sb.appendHtmlConstant("<div class='" + getStyle().triggerWrap() + "' style='margin-top:" + 0 + "px;'>");
                    sb.appendHtmlConstant("<div class='" + getStyle().trigger() + "'></div>");
                    sb.appendHtmlConstant("<div class='" + getStyle().twinTrigger() + "'></div>");
                    sb.appendHtmlConstant("</div>");
                } else {
                    // for touch devices, we want the trigger's to cover the whole vertical area - each one should take up 50%.
                    int triggerHeight = fieldDetails.height() / 2;
                    // override top here, otherwise triggers will be pushed off the bottom of the input
                    sb.appendHtmlConstant("<div class='" + getStyle().triggerWrap() + "' style='top:0%;'>");
                    sb.appendHtmlConstant("<div class='" + getStyle().trigger() + "' style='height:" + triggerHeight + "px;'></div>");
                    sb.appendHtmlConstant("<div class='" + getStyle().twinTrigger() + "' style='height:" + triggerHeight + "px;'></div>");
                    sb.appendHtmlConstant("</div>");
                }
            }
            sb.appendHtmlConstant("</div></div>");
        }
    }

    public static class CMCell<T> extends ComboBoxCell<T> {
        public CMCell(ListStore<T> store, LabelProvider<? super T> labelProvider, SafeHtmlRenderer<T> renderer, TriggerFieldAppearance appearance) {
            super(store, labelProvider, renderer, appearance);
        }

        @Override
        public TwinTriggerFieldCell.TwinTriggerFieldAppearance getAppearance() {
            return (TwinTriggerFieldCell.TwinTriggerFieldAppearance) super.getAppearance();
        }

        @Override
        protected void onClick(Context context, XElement parent, NativeEvent event, T value, ValueUpdater<T> updater) {
            Element target = event.getEventTarget().cast();

            if (!isReadOnly() && getAppearance().twinTriggerIsOrHasChild(parent, target)) {
                onTwinTriggerClick(context, parent, event, value, updater);
            }
            if (!isReadOnly() && (!isEditable() && getInputElement(parent).isOrHasChild(target))
                    || getAppearance().triggerIsOrHasChild(parent.<XElement>cast(), target)) {
                onTriggerClick(context, parent, event, value, updater);
            }
            if (!isEditable()) {
                event.preventDefault();
                event.stopPropagation();
            }
        }

        @Override
        protected void onTap(TouchData t, Context context, Element parent, T value, ValueUpdater<T> valueUpdater) {
            XElement target = t.getStartElement().asElement().cast();

            if (!isReadOnly() && getAppearance().twinTriggerIsOrHasChild(parent.<XElement>cast(), target)) {
                onTwinTriggerClick(context, parent.<XElement>cast(), null, value, valueUpdater);
                t.getLastNativeEvent().preventDefault();
            } else if (!isReadOnly() && getAppearance().triggerIsOrHasChild(parent.<XElement>cast(), target)) {
                onTriggerClick(context, parent.<XElement>cast(), null, value, valueUpdater);
                t.getLastNativeEvent().preventDefault();
            } else {
                getInputElement(parent).focus();
            }
        }

        @Override
        protected void onMouseDown(XElement parent, NativeEvent event) {
            super.onMouseDown(parent, event);
            Element target = event.getEventTarget().cast();
            if (!isReadOnly() && (!isEditable() && getInputElement(parent).isOrHasChild(target))
                    || getAppearance().twinTriggerIsOrHasChild(parent, target)) {
                getAppearance().onTwinTriggerClick(parent, true);
                event.preventDefault();
            }
        }

        protected void onTwinTriggerClick(Context context, XElement parent, NativeEvent event, T value,
                                          ValueUpdater<T> updater) {
            fireEvent(context, new TwinTriggerClickEvent());
            getAppearance().onTwinTriggerClick(parent, false);
        }

        @Override
        protected void onMouseOver(XElement parent, NativeEvent event) {
            super.onMouseOver(parent, event);
            XElement target = event.getEventTarget().cast();
            if (!isReadOnly() && getAppearance().twinTriggerIsOrHasChild(parent, target)) {
                getAppearance().onTwinTriggerOver(parent, true);
            }
        }

        @Override
        protected void onMouseOut(XElement parent, NativeEvent event) {
            super.onMouseOut(parent, event);
            XElement target = event.getEventTarget().cast();
            if (!isReadOnly() && getAppearance().twinTriggerIsOrHasChild(parent, target)) {
                getAppearance().onTwinTriggerOver(parent, false);
            }
        }
    }
}
