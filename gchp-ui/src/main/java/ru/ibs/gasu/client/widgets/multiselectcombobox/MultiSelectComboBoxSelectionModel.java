package ru.ibs.gasu.client.widgets.multiselectcombobox;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;

import java.util.ArrayList;
import java.util.List;

public class MultiSelectComboBoxSelectionModel<M> extends CheckBoxSelectionModel<M> {

    public void deselectAllSuppressEvent(boolean suppressEvent) {
        doDeselect(new ArrayList<>(selected), suppressEvent);
    }

    public void selectSuppressEvent(List<M> items, boolean suppressEvent) {
        this.doSelect(items, true, suppressEvent);
    }

    public MultiSelectComboBoxSelectionModel() {
        super();
    }

    public MultiSelectComboBoxSelectionModel(ValueProvider<M, M> valueProvider) {
        super(valueProvider);
    }
}
