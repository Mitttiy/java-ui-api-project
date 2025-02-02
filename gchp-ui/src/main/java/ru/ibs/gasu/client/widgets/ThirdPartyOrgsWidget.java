package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import ru.ibs.gasu.client.widgets.componens.ToolbarButton;
import ru.ibs.gasu.common.models.ThirdOrgModel;
import ru.ibs.gasu.common.models.WorkTypeModel;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class ThirdPartyOrgsWidget implements IsWidget {

    private VerticalLayoutContainer container;

    private Grid<ThirdOrgModel> grid;
    private ToolbarButton addButton;
    private ToolbarButton removeButton;
    private String dictType;

    public Grid<ThirdOrgModel> getGrid() {
        return grid;
    }

    public ThirdPartyOrgsWidget(String dictType) {
        this.dictType = dictType;
        initWidget();
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        HBoxLayoutContainer buttonsContainer = new HBoxLayoutContainer();
        BoxLayoutContainer.BoxLayoutData boxLayoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 5, 0, 0));

        addButton = new ToolbarButton("Добавить", "fas fa-plus");
        removeButton = new ToolbarButton("Удалить", "fas fa-trash");

        buttonsContainer.add(addButton, boxLayoutData);
        buttonsContainer.add(removeButton, boxLayoutData);

        ListStore<ThirdOrgModel> store = new ListStore<>(new ModelKeyProvider<ThirdOrgModel>() {
            @Override
            public String getKey(ThirdOrgModel item) {
                return String.valueOf(item.getGid());
            }
        });

        store.setAutoCommit(true);
        ColumnConfig<ThirdOrgModel, String> name = new ColumnConfig<>(new ValueProvider<ThirdOrgModel, String>() {
            @Override
            public String getValue(ThirdOrgModel object) {
                return object.getName();
            }

            @Override
            public void setValue(ThirdOrgModel object, String value) {
                object.setName(value);
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 10, "Наименование");

        ColumnConfig<ThirdOrgModel, String> inn = new ColumnConfig<>(new ValueProvider<ThirdOrgModel, String>() {
            @Override
            public String getValue(ThirdOrgModel object) {
                return object.getInn();
            }

            @Override
            public void setValue(ThirdOrgModel object, String value) {
                object.setInn(value);
            }

            @Override
            public String getPath() {
                return "inn";
            }
        }, 10, "ИНН");

        ColumnConfig<ThirdOrgModel, ThirdOrgModel> workTypes = new ColumnConfig<>(new ValueProvider<ThirdOrgModel, ThirdOrgModel>() {
            @Override
            public ThirdOrgModel getValue(ThirdOrgModel object) {
                return object;
            }

            @Override
            public void setValue(ThirdOrgModel object, ThirdOrgModel value) {
            }

            @Override
            public String getPath() {
                return "workTypes";
            }
        }, 10, "Виды работ");
        ActionCell.Delegate<ThirdOrgModel> workTypesDelegate = new ActionCell.Delegate<ThirdOrgModel>() {
            @Override
            public void execute(ThirdOrgModel object) {
                new WorkTypeDicWindow(dictType) {
                    @Override
                    protected void onSave(List<WorkTypeModel> picked) {
                        object.getWorkType().clear();
                        object.getWorkType().addAll(picked);
                        grid.getView().refresh(false);
                    }
                }.show();
            }
        };

        workTypes.setCell(new ActionCell<ThirdOrgModel>("", workTypesDelegate) {
            @Override
            public void render(Context context, ThirdOrgModel value, SafeHtmlBuilder sb) {
                sb.appendHtmlConstant("<span style='cursor: pointer;'>");
                if (value.getWorkType().size() == 0)
                    sb.appendHtmlConstant("Выберите виды работ");
                else
                    sb.appendHtmlConstant(value.getWorkType().stream().map(i -> i.getName())
                            .collect(Collectors.joining("; ")));
                sb.appendHtmlConstant("</span>");
            }
        });


        ColumnModel<ThirdOrgModel> cm = new ColumnModel<>(Arrays.asList(name, inn, workTypes));
        grid = new Grid<>(store, cm);
        grid.getView().setForceFit(true);
        grid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        grid.getView().setStripeRows(true);

        addButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                ThirdOrgModel model = new ThirdOrgModel();
                model.setGid(getRandId());
                store.add(model);
            }
        });

        removeButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                store.remove(grid.getSelectionModel().getSelectedItem());
            }
        });

        final GridInlineEditing<ThirdOrgModel> editing = new GridInlineEditing<ThirdOrgModel>(grid) {
            @Override
            protected void onScroll(ScrollEvent event) {
            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(name, new TextField());
        editing.addEditor(inn, new TextField());

        container.add(buttonsContainer, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 0, 5, 0)));
        container.add(grid, STD_VC_LAYOUT);

    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
