package ru.ibs.gasu.client.widgets;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class ActualProjectsListView implements IsWidget {

    private VerticalLayoutContainer mainContainer;

    private void initWidget() {
        mainContainer = new VerticalLayoutContainer();
        mainContainer.add(new HTML("ActualProjectsListView"));
    }

    @Override
    public Widget asWidget() {
        initWidget();
        return mainContainer;
    }
}
