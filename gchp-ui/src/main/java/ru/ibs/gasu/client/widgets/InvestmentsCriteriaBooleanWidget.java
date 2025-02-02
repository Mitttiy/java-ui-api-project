package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.thirdparty.guava.common.base.Objects;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import ru.ibs.gasu.common.models.InvestmentsCriteriaModel;

import java.util.Arrays;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class InvestmentsCriteriaBooleanWidget implements IsWidget {

    private VerticalLayoutContainer container;
    private Grid<InvestmentsCriteriaModel> grid;
    private ListStore<InvestmentsCriteriaModel> store;

    public Grid<InvestmentsCriteriaModel> getGrid() {
        return grid;
    }

    public InvestmentsCriteriaBooleanWidget() {
        initButtonContainer();
        initGrid();
        setUpBaseIndicators();
        container.add(grid, STD_VC_LAYOUT);
    }

    private void initButtonContainer() {
        container = new VerticalLayoutContainer();
    }

    private void initGrid() {
        store = new ListStore<>(new ModelKeyProvider<InvestmentsCriteriaModel>() {
            @Override
            public String getKey(InvestmentsCriteriaModel item) {
                return String.valueOf(item.getGid());
            }
        });
        store.setAutoCommit(true);

        ColumnConfig<InvestmentsCriteriaModel, String> name = new ColumnConfig<>(new ValueProvider<InvestmentsCriteriaModel, String>() {
            @Override
            public String getValue(InvestmentsCriteriaModel object) {
                return object.getName();
            }

            @Override
            public void setValue(InvestmentsCriteriaModel object, String value) {
            }

            @Override
            public String getPath() {
                return "name";
            }
        }, 100, "Критерий");
        name.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                if (grid.getColumnModel().getColumns().get(context.getColumn()).isHidden()) {
                    sb.append(Util.NBSP_SAFE_HTML);
                    return;
                }
                if (value == null) {
                    sb.append(Util.NBSP_SAFE_HTML);
                } else {
                    sb.append(wrapString(value));
                }
            }
        });

        ColumnConfig<InvestmentsCriteriaModel, Boolean> value = new ColumnConfig<>(new ValueProvider<InvestmentsCriteriaModel, Boolean>() {
            @Override
            public Boolean getValue(InvestmentsCriteriaModel object) {
                return object.getValue();
            }

            @Override
            public void setValue(InvestmentsCriteriaModel object, Boolean value) {
                object.setValue(value);
            }

            @Override
            public String getPath() {
                return "value";
            }
        }, 100, "Значение");
        value.setFixed(true);
        value.setCell(new SimpleSafeHtmlCell<Boolean>(new AbstractSafeHtmlRenderer<Boolean>() {
            @Override
            public SafeHtml render(Boolean object) {
                return SafeHtmlUtils.fromTrustedString(object ? "Да" : "Нет");
            }
        }));

        ColumnConfig<InvestmentsCriteriaModel, String> comment = new ColumnConfig<>(new ValueProvider<InvestmentsCriteriaModel, String>() {
            @Override
            public String getValue(InvestmentsCriteriaModel object) {
                return object.getComment();
            }

            @Override
            public void setValue(InvestmentsCriteriaModel object, String value) {
                object.setComment(value);
            }

            @Override
            public String getPath() {
                return "comment";
            }
        }, 100, "Комментарий");
        comment.setCell(new AbstractCell<String>() {
            @Override
            public void render(Context context, String value, SafeHtmlBuilder sb) {
                if (grid.getColumnModel().getColumns().get(context.getColumn()).isHidden()) {
                    sb.append(Util.NBSP_SAFE_HTML);
                    return;
                }
                if (value == null) {
                    sb.append(Util.NBSP_SAFE_HTML);
                } else {
                    sb.append(wrapString(value));
                }
            }
        });

        ColumnModel<InvestmentsCriteriaModel> cm = new ColumnModel<>(Arrays.asList(name, value, comment));
        grid = new Grid<>(store, cm);
        grid.getView().setForceFit(true);
        grid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        grid.getView().setStripeRows(true);

        final GridInlineEditing<InvestmentsCriteriaModel> editing = new GridInlineEditing<InvestmentsCriteriaModel>(grid) {
            @Override
            protected void onScroll(ScrollEvent event) {

            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
        editing.addEditor(value, new CheckBox());
        editing.addEditor(comment, new TextField());
    }

    protected void setUpBaseIndicators() {
        InvestmentsCriteriaModel p1 = new InvestmentsCriteriaModel();
        p1.setGid(1L);
        p1.setName("Создаются ли новые рабочие места после ввода объекта в эксплуатацию?");

        InvestmentsCriteriaModel p2 = new InvestmentsCriteriaModel();
        p2.setGid(2L);
        p2.setName("Проходят ли сотрудники создаваемого предприятия курсы повышения квалификации?");

        InvestmentsCriteriaModel p3 = new InvestmentsCriteriaModel();
        p3.setGid(3L);
        p3.setName("Предусматривает ли проект меры по снижению негативного воздействия на окружающую среду?");

        InvestmentsCriteriaModel p4 = new InvestmentsCriteriaModel();
        p4.setGid(4L);
        p4.setName("В проекте учтены риски природных катастроф, изменения климата, чрезвычайных происшествий и прочие риски");

        InvestmentsCriteriaModel p5 = new InvestmentsCriteriaModel();
        p5.setGid(5L);
        p5.setName("Проект ориентирован на предоставление товаров и услуг местному населению");

        InvestmentsCriteriaModel p6 = new InvestmentsCriteriaModel();
        p6.setGid(6L);
        p6.setName("Граждане принимают участие в разработке проекта");

        InvestmentsCriteriaModel p7 = new InvestmentsCriteriaModel();
        p7.setGid(7L);
        p7.setName("В проекте минимизировано изъятие земельных участков и иной собственности у граждан");

        InvestmentsCriteriaModel p8 = new InvestmentsCriteriaModel();
        p8.setGid(8L);
        p8.setName("В проекте предусмотрены меры по восстановлению условий жизнедеятельности для населения, переселенного в результате реализации проекта");

        InvestmentsCriteriaModel p9 = new InvestmentsCriteriaModel();
        p9.setGid(9L);
        p9.setName("В проекте предусмотрено использование инновационных технологий");

        store.add(p1);
        store.add(p2);
        store.add(p3);
        store.add(p4);
        store.add(p5);
        store.add(p6);
        store.add(p7);
        store.add(p8);
        store.add(p9);
    }

    public ListStore<InvestmentsCriteriaModel> getStore() {
        return store;
    }

    public InvestmentsCriteriaModel getStoreItemByGid(Long gid) {
        return store.getAll().stream().filter(investmentsCriteriaModel -> investmentsCriteriaModel.getGid().equals(gid)).findFirst().orElse(null);
    }

    @Override
    public Widget asWidget() {
        return container;
    }
}
