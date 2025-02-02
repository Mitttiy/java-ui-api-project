package ru.ibs.gasu.client.widgets;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import ru.ibs.gasu.client.utils.ClientUtils;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;

public class DateRangeWidget  implements IsWidget {

    private HorizontalLayoutContainer container;
    private MultiSelectComboBox<String> yearBox;
    private CheckBox changeDateRange;
    private TextField minDateRange;
    private TextField maxDateRange;

    public DateRangeWidget(MultiSelectComboBox<String> yearBox) {
        this.yearBox = yearBox;
        changeDateRange = new CheckBox();
        changeDateRange.setBoxLabel("Установить диапазон дат");
        minDateRange = new TextField();
        minDateRange.setEmptyText("от");
        maxDateRange = new TextField();
        maxDateRange.setEmptyText("до");
        minDateRange.hide();
        maxDateRange.hide();
        container = new HorizontalLayoutContainer();
        HorizontalLayoutContainer.HorizontalLayoutData boxLayoutData = new HorizontalLayoutContainer.HorizontalLayoutData(-1, 50, new Margins(0, 5, 0, 0));

        container.add(changeDateRange, boxLayoutData);
        container.add(minDateRange, boxLayoutData);
        container.add(maxDateRange, boxLayoutData);

        changeDateRange.addValueChangeHandler(event -> {
            if (event.getValue() != null && event.getValue()) {
                minDateRange.show();
                maxDateRange.show();
                container.forceLayout();
            } else {
                minDateRange.hide();
                maxDateRange.hide();
            }
        });

        minDateRange.addValueChangeHandler(event -> {
            yearBox.clearStore();
            yearBox.addAll(ClientUtils.generateComboBoxYearValues(minDateRange.getValue(), maxDateRange.getValue()));
        });

        maxDateRange.addValueChangeHandler(event -> {
            yearBox.clearStore();
            yearBox.addAll(ClientUtils.generateComboBoxYearValues(minDateRange.getValue(), maxDateRange.getValue()));
        });
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    public HorizontalLayoutContainer getContainer() {
        return container;
    }

    public CheckBox getChangeDateRange() {
        return changeDateRange;
    }

    public TextField getMinDateRange() {
        return minDateRange;
    }

    public TextField getMaxDateRange() {
        return maxDateRange;
    }
}
