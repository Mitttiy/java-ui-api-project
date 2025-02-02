package ru.ibs.gasu.client.widgets;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.ClicksToEdit;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import org.fusesource.restygwt.client.Method;
import ru.ibs.gasu.client.api.GchpApi;
import ru.ibs.gasu.client.widgets.componens.DefaultCallback;
import ru.ibs.gasu.common.models.PrivatePartnersOwningPartModel;
import ru.ibs.gasu.common.soap.generated.gchpdicts.EgrulDomain;
import ru.ibs.gasu.common.soap.generated.gchpdicts.SimpleEgrulDomainWithCapital;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class PrivatePartnersOwningPartsView implements IsWidget {

    private VerticalLayoutContainer container;
    private Grid<PrivatePartnersOwningPartModel> grid;
    private FieldLabel egrulFullNameLabel;
    private FieldLabel egrulRoAddressLabel;
    private FieldLabel egrulFullRoAddressLabel;
    private FieldLabel egrulFioTopManagerLabel;
    private FieldLabel egrulCapitalInfoLabel;
    private FieldLabel egrulCapitalValueLabel;
    private List<VerticalLayoutContainer> itemsToMask;

    public void setItemsToMask(List<VerticalLayoutContainer> itemsToMask) {
        this.itemsToMask = itemsToMask;
    }

    public FieldLabel getFullEgrulRoAddressLabel() {
        return egrulFullRoAddressLabel;
    }

    public FieldLabel getEgrulRoAddressLabel() {
        return egrulRoAddressLabel;
    }

    public FieldLabel getEgrulFullNameLabel() {
        return egrulFullNameLabel;
    }

    public Grid<PrivatePartnersOwningPartModel> getGrid() {
        return grid;
    }

    public PrivatePartnersOwningPartsView() {
        initWidget();
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();

        egrulFullNameLabel = new FieldLabel();
        egrulRoAddressLabel = new FieldLabel();
        egrulFullRoAddressLabel = new FieldLabel();
        egrulFioTopManagerLabel = new FieldLabel();
        egrulCapitalInfoLabel = new FieldLabel();
        egrulCapitalValueLabel = new FieldLabel();

        egrulFullNameLabel.setLabelSeparator("");
        egrulRoAddressLabel.setLabelSeparator("");
        egrulFullRoAddressLabel.setLabelSeparator("");
        egrulFioTopManagerLabel.setLabelSeparator("");
        egrulCapitalInfoLabel.setLabelSeparator("");
        egrulCapitalValueLabel.setLabelSeparator("");

        egrulFullNameLabel.setBorders(true);
        egrulRoAddressLabel.setBorders(true);
        egrulFioTopManagerLabel.setBorders(true);
        egrulCapitalValueLabel.setBorders(true);

//        container.add(createFieldLabelTop(egrulFullNameLabel, "Полное наименование"), STD_VC_LAYOUT);
//        container.add(createFieldLabelTop(egrulRoAddressLabel, "Адрес (место нахождения) ЮЛ/ИП"), STD_VC_LAYOUT);
        container.add(createFieldLabelTop(egrulFioTopManagerLabel, "Генеральный директор"), STD_VC_LAYOUT);
//        container.add(createFieldLabelTop(egrulCapitalInfoLabel, "Вид капитала"), STD_VC_LAYOUT);
        container.add(createFieldLabelTop(egrulCapitalValueLabel, "Сведения об уставном капитале (складочном капитале, уставном фонде, паевых взносах)"), STD_VC_LAYOUT);

        ListStore<PrivatePartnersOwningPartModel> store = new ListStore<>(item -> String.valueOf(item.getGid()));

        ColumnConfig<PrivatePartnersOwningPartModel, String> orgName = new ColumnConfig<>(new ValueProvider<PrivatePartnersOwningPartModel, String>() {
            @Override
            public String getValue(PrivatePartnersOwningPartModel object) {
                return object.getOrgName();
            }

            @Override
            public void setValue(PrivatePartnersOwningPartModel object, String value) {
                object.setOrgName(value);
            }

            @Override
            public String getPath() {
                return "orgName";
            }
        }, 300, "ФИО/ЮЛ//ИП");
        orgName.setCell(new AbstractCell<String>() {
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
        orgName.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

        ColumnConfig<PrivatePartnersOwningPartModel, String> value = new ColumnConfig<>(new ValueProvider<PrivatePartnersOwningPartModel, String>() {
            @Override
            public String getValue(PrivatePartnersOwningPartModel object) {
                return object.getOrgCapitalValue();
            }

            @Override
            public void setValue(PrivatePartnersOwningPartModel object, String value) {
                object.setOrgCapitalValue(value);
            }

            @Override
            public String getPath() {
                return "value";
            }
        }, 100, "Номинальная доля стоимости (в рублях)");

        ColumnConfig<PrivatePartnersOwningPartModel, String> percent = new ColumnConfig<>(new ValueProvider<PrivatePartnersOwningPartModel, String>() {
            @Override
            public String getValue(PrivatePartnersOwningPartModel object) {
                return WidgetUtils.doubleToString(object.getPercent());
            }

            @Override
            public void setValue(PrivatePartnersOwningPartModel object, String value) {
                if (value == null) {
                    object.setPercent(null);
                    return;
                }
                try {
                    object.setPercent(Double.parseDouble(value));
                } catch (Exception ex) {
                }
            }

            @Override
            public String getPath() {
                return "percent";
            }
        }, 50, "%");

        ColumnModel<PrivatePartnersOwningPartModel> cm = new ColumnModel<>(
                Arrays.asList(orgName, value));
        store.setAutoCommit(true);

        grid = new Grid<>(store, cm);
        grid.getView().setForceFit(true);
        grid.getView().setEmptyText(GRID_EMPTY_TEXT_VAR1);
        grid.getView().setStripeRows(true);

        final GridInlineEditing<PrivatePartnersOwningPartModel> editing = new GridInlineEditing<PrivatePartnersOwningPartModel>(grid) {
            @Override
            protected void onScroll(ScrollEvent event) {
            }
        };
        editing.setClicksToEdit(ClicksToEdit.ONE);
//        editing.addEditor(percent, WidgetUtils.createEditField());
        editing.addEditor(value, WidgetUtils.createEditField());
        grid.getStore().addSortInfo(new Store.StoreSortInfo<PrivatePartnersOwningPartModel>(new Comparator<PrivatePartnersOwningPartModel>() {
            @Override
            public int compare(PrivatePartnersOwningPartModel o1, PrivatePartnersOwningPartModel o2) {
                if (o1.getGid() > o2.getGid()) return 1;
                else if (o1.getGid() < o2.getGid()) return -1;
                else return 0;
            }
        }, SortDir.DESC));

        container.add(createHtml("<br> "));
        container.add(createHtml("Сведения об учредителях (участниках) юридического лица"));
        container.add(grid, STD_VC_LAYOUT);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    private void setEgrulLabels(EgrulDomain egrul, boolean setGridStore) {

        String postalCode = egrul.getPostalCode() == null ? "" : (egrul.getPostalCode() + ", ");
        String regionType = egrul.getRegionType() == null ? "" : (egrul.getRegionType() + " ");
        String regionName = egrul.getRegionName() == null ? "" : (egrul.getRegionName() + ", ");
        String streetType = egrul.getStreetType() == null ? "" : (egrul.getStreetType() + " ");
        String streetName = egrul.getStreetName() == null ? "" : (egrul.getStreetName() + ", ");
        String building = egrul.getBuilding() == null ? "" : (egrul.getBuilding());
        String office = egrul.getOffice() == null ? "" : (", " + egrul.getOffice());
        String address = postalCode + regionType + regionName + streetType + streetName + building + office;

        String fullName = egrul.getFullName() == null ? "" : egrul.getFullName();
        String topManager = "";
        if (egrul.getFioTopManagers().size() != 0) {
            topManager = egrul.getFioTopManagers().get(egrul.getFioTopManagers().size()-1);
        }
        boolean showAddress = true;
        if (fullName.equals(topManager)) {
            showAddress = false;
        }

        egrulFullNameLabel.setText(fullName);
        egrulRoAddressLabel.setText(showAddress ? address : "");
        egrulFioTopManagerLabel.setText(egrul.getFioTopManagers() == null ? "" : topManager);
        egrulCapitalInfoLabel.setText(egrul.getCapitalInfo() == null ? "" : egrul.getCapitalInfo());
        egrulCapitalValueLabel.setText(egrul.getCapitalValue() == null ? "" : new BigDecimal(egrul.getCapitalValue()).toPlainString().concat(" руб."));
        if (setGridStore) {
            grid.getStore().clear();
            List<PrivatePartnersOwningPartModel> founders = egrul.getFounders().stream().map(PrivatePartnersOwningPartsView.this::convert).collect(Collectors.toList());
            if (founders.size() != 0) {
                grid.getStore().addAll(founders);
            }
        }
    }

    public void setEgrulLabels(String inn, boolean setGridStore) {
        if (inn == null || inn.isEmpty()) {
            return;
        }
        itemsToMask.forEach(item -> item.mask("Загрузка"));
        GchpApi.GCHP_API.getEgrulDomain(inn, new DefaultCallback<EgrulDomain>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Ошибка", "Во время загрузки произошла ошибка").show();
                itemsToMask.forEach(Component::unmask);
            }

            @Override
            public void onSuccess(Method method, EgrulDomain response) {
                setEgrulLabels(response, setGridStore);
                itemsToMask.forEach(Component::unmask);
            }
        });
    }

    private PrivatePartnersOwningPartModel convert(SimpleEgrulDomainWithCapital egrulDomain) {
        PrivatePartnersOwningPartModel model = new PrivatePartnersOwningPartModel();
        model.setId(egrulDomain.getId());
        model.setGid(getRandId());
        model.setOrgName(egrulDomain.getFullName());
        model.setOrgCapitalValue(egrulDomain.getCapitalValue());
        if (egrulDomain.getCapitalPercent() != null) {
            model.setPercent(Double.parseDouble(egrulDomain.getCapitalPercent()));
        }
        return model;
    }
}
