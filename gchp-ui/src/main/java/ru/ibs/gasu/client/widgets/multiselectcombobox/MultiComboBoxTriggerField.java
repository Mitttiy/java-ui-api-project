package ru.ibs.gasu.client.widgets.multiselectcombobox;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.TriggerField;

/**
 * Trigger Field for multi select combo box
 */
public class MultiComboBoxTriggerField extends TriggerField<String> implements ExpandEvent.HasExpandHandlers, CollapseEvent.HasCollapseHandlers {

    public MultiComboBoxTriggerField() {
        this(new TriggerFieldCell<>(), new MultiComboBoxPropertyEditor());
    }

    public MultiComboBoxTriggerField(TriggerFieldCell<String> cell) {
        this(cell, new MultiComboBoxPropertyEditor());
    }

    protected MultiComboBoxTriggerField(TriggerFieldCell<String> cell, PropertyEditor<String> propertyEditor) {
        super(cell, propertyEditor);
    }

    @Override
    public HandlerRegistration addExpandHandler(ExpandEvent.ExpandHandler handler) {
        return addHandler(handler, ExpandEvent.getType());
    }

    @Override
    public HandlerRegistration addCollapseHandler(CollapseEvent.CollapseHandler handler) {
        return addHandler(handler, CollapseEvent.getType());
    }
}