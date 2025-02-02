package ru.ibs.gasu.client.widgets.componens;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

import java.util.Arrays;
import java.util.List;

public class PageToolBar implements IsWidget {

    private HBoxLayoutContainer mainVerticalContainer;
    private PagingToolBar pagingToolBar;
    private ComboBox<Integer> pageSizeTreeComboBox;
    private FieldLabel pageSizeTreeLabel;
    private final List<Integer> defaultPageSizes = Arrays.asList(10, 20, 30, 40, 50);

    public PageToolBar() {
        initRecordsPerPagePicker();
        initStorage();
        initToolBar();
        initWidget();
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public void setCustomPageSizes(List<Integer> pageSizes) {
        pageSizeTreeComboBox.getStore().clear();
        pageSizeTreeComboBox.getStore().addAll(pageSizes);
    }

    @Override
    public Widget asWidget() {
        return mainVerticalContainer;
    }

    private void initToolBar() {
        pagingToolBar = new PagingToolBar(defaultPageSizes.get(0)) {
            @Override
            public void first() {
                doLoadRequest(0, pageSize);
            }
        };
        pagingToolBar.setBorders(false);
        pagingToolBar.setShowToolTips(false);
        pagingToolBar.setWidth(500);
    }

    private void initStorage() {
        pageSizeTreeComboBox.getStore().addAll(defaultPageSizes);
    }

    private void initRecordsPerPagePicker() {
        ListStore<Integer> store = new ListStore<>(String::valueOf);
        pageSizeTreeComboBox = new ComboBox<>(store, String::valueOf);
        pageSizeTreeComboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        pageSizeTreeComboBox.setEditable(false);
        pageSizeTreeComboBox.setWidth(70);
        pageSizeTreeLabel = new FieldLabel(pageSizeTreeComboBox, "Отображать по");
    }

    private void initWidget() {
        mainVerticalContainer = new HBoxLayoutContainer();
        mainVerticalContainer.setWidth(800);
        mainVerticalContainer.add(pageSizeTreeLabel, new BoxLayoutContainer.BoxLayoutData(new Margins(5, 5, 0, 0)));
        mainVerticalContainer.add(pagingToolBar);
        pageSizeTreeComboBox.setValue(pageSizeTreeComboBox.getStore().get(0));
        pageSizeTreeComboBox.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                pagingToolBar.setPageSize(event.getSelectedItem());
                pagingToolBar.first();
            }
        });
    }
}
