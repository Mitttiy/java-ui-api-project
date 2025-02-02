package ru.ibs.gasu.client.widgets.componens;

import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;

public class DateFieldFullDate extends DateField {
    public DateFieldFullDate() {
        super();
        this.setPropertyEditor(new DateTimePropertyEditor("dd.MM.yyyy"));
    }
}
