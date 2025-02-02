package ru.ibs.gasu.client.widgets.multiselectcombobox;

import com.sencha.gxt.widget.core.client.form.PropertyEditor;

import java.text.ParseException;

/**
 * Property editor for multi select combo box
 */
public class MultiComboBoxPropertyEditor extends PropertyEditor<String> {

    @Override
    public String parse(CharSequence text) throws ParseException {
        return text.toString();
    }

    @Override
    public String render(String object) {
        return object;
    }
}