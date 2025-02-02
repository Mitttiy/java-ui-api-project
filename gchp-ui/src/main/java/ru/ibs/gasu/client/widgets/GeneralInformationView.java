package ru.ibs.gasu.client.widgets;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.*;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import ru.ibs.gasu.client.widgets.componens.FileUploader;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.client.widgets.componens.TwinTriggerComboBox;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.view_attr.ViewAttrState;
import ru.ibs.gasu.common.models.view_attr.ViewAttrStateColumns;
import ru.ibs.gasu.common.soap.generated.gchpdocs.SvrOrg;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sencha.gxt.cell.core.client.form.TextAreaInputCell.Resizable.VERTICAL;
import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

/**
 * Раздел "Общие сведения".
 */
public class GeneralInformationView implements IsWidget {
    private VerticalLayoutContainer container;

    private static final String REGION_RF = "Российская Федерация";
    private TextArea projectNameField;
    private ComboBox<SimpleIdNameModel> realizationForm;
    private ComboBox<SimpleIdNameModel> initMethod;
    private FieldLabel initMethodLabel;
    private ComboBox<SimpleIdNameModel> implementationLvl;
    private CheckBox isRFPartOfAgreement;
    private CheckBox isRegionPartOfAgreement;
    private CheckBox isMunicipalityPartOfAgreement;
    private ComboBox<SimpleIdNameModel> region;
    private TwinTriggerComboBox<SimpleIdNameModel> municipality;
    private ComboBox<SvrOrg> ppk;

    private TextField inn;
    private  VerticalLayoutContainer innContainer;

    private TextField balanceHolder;
    private VerticalLayoutContainer balanceHolderContainer;

    private TextField implementer;
    private TextField implementerInn;
    private ComboBox<SimpleIdNameModel> opf;
    private CheckBox isForeignInvestor;
    private CheckBox isMcpSubject;
    private CheckBox isSpecialProjectCompany;
    private CheckBox hasInvestmentProperty;

    private DoubleField publicSharePercentage;
    private VerticalLayoutContainer publicSharePercentageContainer;

    private CheckBox isRFHasShare;
    private ComboBox<SimpleIdNameModel> implementationSphere;
    private ComboBox<SimpleIdNameModel> implementationSector;
    //  private ComboBox<SimpleIdNameModel> objectType;
    private MultiSelectComboBox<SimpleIdNameModel> objectType;

    private TextField objectLocation;
    private MultiSelectComboBox<SimpleIdNameModel> agreementSubject;
    private ComboBox<SimpleIdNameModel> realizationStatus;
    private FileUploader completedTemplateFileUpload;
    private HTML manual ;
    @Override
    public Widget asWidget() {
        return container;
    }

    public GeneralInformationView() {
        initWidget();
    }

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public TextArea getProjectNameField() {
        return projectNameField;
    }

    public ComboBox<SimpleIdNameModel> getRealizationForm() {
        return realizationForm;
    }

    public ComboBox<SimpleIdNameModel> getInitMethod() {
        return initMethod;
    }

    public ComboBox<SimpleIdNameModel> getImplementationLvl() {
        return implementationLvl;
    }

    public CheckBox getIsRFPartOfAgreement() {
        return isRFPartOfAgreement;
    }

    public CheckBox getIsRegionPartOfAgreement() {
        return isRegionPartOfAgreement;
    }

    public CheckBox getIsMunicipalityPartOfAgreement() {
        return isMunicipalityPartOfAgreement;
    }

    public ComboBox<SimpleIdNameModel> getRegion() {
        return region;
    }

    public TwinTriggerComboBox<SimpleIdNameModel> getMunicipality() {
        return municipality;
    }

    public ComboBox<SvrOrg> getPpk() {
        return ppk;
    }

    public TextField getInn() {
        return inn;
    }

    public TextField getBalanceHolder() {
        return balanceHolder;
    }

    public TextField getImplementer() {
        return implementer;
    }

    public TextField getImplementerInn() {
        return implementerInn;
    }

    public ComboBox<SimpleIdNameModel> getOpf() {
        return opf;
    }

    public CheckBox getIsSpecialProjectCompany() {
        return isSpecialProjectCompany;
    }

    public CheckBox getHasInvestmentProperty() {
        return hasInvestmentProperty;
    }

    public DoubleField getPublicSharePercentage() {
        return publicSharePercentage;
    }

    public CheckBox getIsRFHasShare() {
        return isRFHasShare;
    }

    public ComboBox<SimpleIdNameModel> getImplementationSphere() {
        return implementationSphere;
    }

    public ComboBox<SimpleIdNameModel> getImplementationSector() {
        return implementationSector;
    }

    public MultiSelectComboBox<SimpleIdNameModel> getObjectType() {
        return objectType;
    }

    public TextField getObjectLocation() {
        return objectLocation;
    }

    public MultiSelectComboBox<SimpleIdNameModel> getAgreementSubject() {
        return agreementSubject;
    }

    public ComboBox<SimpleIdNameModel> getRealizationStatus() {
        return realizationStatus;
    }

    public FileUploader getCompletedTemplateFileUpload() {
        return completedTemplateFileUpload;
    }

    public CheckBox getIsForeignInvestor() {
        return isForeignInvestor;
    }

    public CheckBox getIsMcpSubject() {
        return isMcpSubject;
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        VerticalLayoutContainer.VerticalLayoutData layoutData = new VerticalLayoutContainer.VerticalLayoutData(1, -1);

        projectNameField = new TextArea();
        projectNameField.setResizable(VERTICAL);
//        projectNameField.setHeight(100);
        projectNameField.setName("Наименование проекта");
        projectNameField.addValidator(new MaxLengthValidator(5000));
        FieldLabel projectNameFieldLabel = createFieldLabelTop(projectNameField, new SafeHtmlBuilder().appendHtmlConstant("Наименование проекта " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Пример: Концессионное соглашение по реконструкции объектов теплоснабжения Михайловского муниципльного образования'></i>").toSafeHtml());

        realizationForm = createCommonFilterModelComboBox("Выберите форму реализации проекта");
        FieldLabel implementationFormLabel = createFieldLabelTop(realizationForm, "Форма реализации");

        initMethod = createCommonFilterModelComboBox("Выберите способ инициации");
        initMethodLabel = createFieldLabelTop(initMethod, new SafeHtmlBuilder().appendHtmlConstant("Способ инициации проекта " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Способ инциации проекта \"без конкурсных процедур (частная инициатива)\" не меняется если по итогам рассмотрения частной инициативы был объявлен конкурс'></i>").toSafeHtml());

        implementationLvl = createCommonFilterModelComboBox("Выберите уровень реализации");
        FieldLabel implementationLvlLabel = createFieldLabelTop(implementationLvl, "Уровень реализации");

        isRFPartOfAgreement = new CheckBox();
        isRFPartOfAgreement.setBoxLabel(wrapString("Российская Федерация является стороной соглашения"));

        isRegionPartOfAgreement = new CheckBox();
        isRegionPartOfAgreement.setBoxLabel(wrapString("Регион является стороной соглашения"));

        isMunicipalityPartOfAgreement = new CheckBox();
        isMunicipalityPartOfAgreement.setBoxLabel(wrapString("Муниципальное образование является стороной соглашения"));

        region = createCommonFilterModelComboBox("Выберите субъект РФ");
        FieldLabel regionLabel = createFieldLabelTop(region, "Субъект РФ");

        municipality = createCommonFilterModelTwinTriggerComboBox("Выберите МО");
        FieldLabel municipalityLabel = createFieldLabelTop(municipality, "Муниципальное образование");

        ppk = createSvrOrgsModelComboBox("Выберите наименование публичного партнера / Концедента / Заказчика");
        FieldLabel ppkLabel = createFieldLabelTop(ppk, "Концедент/ Публичная сторона");

        inn = new TextField();
        FieldLabel innLabel = createFieldLabelTop(inn, "ИНН заказчика");
        innContainer = new VerticalLayoutContainer();
        innContainer.add(innLabel, layoutData);

        balanceHolder = new TextField();
        FieldLabel balanceHolderLabel = createFieldLabelTop(balanceHolder, "Балансодержатель");
        balanceHolderContainer = new VerticalLayoutContainer();
        balanceHolderContainer.add(balanceHolderLabel, layoutData);

        implementer = new TextField();
        FieldLabel implementerLabel = createFieldLabelTop(implementer, "Концессионер/ Частная сторона");

        implementerInn = new TextField();
        FieldLabel implementerInnLabel = createFieldLabelTop(implementerInn, "ИНН Концессионера/Частной стороны");

        opf = createCommonFilterModelComboBox("Выберите организационно-правовую форму Концессионера/ Частной стороны");
        FieldLabel opfLabel = createFieldLabelTop(opf, "ОПФ");

        isForeignInvestor = new CheckBox();
        isForeignInvestor.setBoxLabel(wrapString("Иностранный инвестор"));

        isMcpSubject = new CheckBox();
        isMcpSubject.setBoxLabel("Субъект МСП");

        isSpecialProjectCompany = new CheckBox();
        isSpecialProjectCompany.setBoxLabel(wrapString("Совместное юридическое лицо является специальной проектной компанией"));

        hasInvestmentProperty = new CheckBox();
        hasInvestmentProperty.setBoxLabel(wrapString("Совместное юридическое лицо владеет недвижимым и (или) движимым имуществом, в отношении которого предполагается осуществление инвестиционных мероприятий"));

        publicSharePercentage = new DoubleField();
        FieldLabel publicSharePercentageLabel = createFieldLabelTop(publicSharePercentage, "Доля публичной стороны в капитале совместного юридического лица (совместного предприятия), реализующего проект, до реализации проекта, %");
        publicSharePercentageContainer = new VerticalLayoutContainer();
        publicSharePercentageContainer.add(publicSharePercentageLabel, layoutData);

        isRFHasShare = new CheckBox();
        isRFHasShare.setBoxLabel(wrapString("Долей в совместном предприятии, реализующем проект, прямо/косвенно владеет Российская Федерация"));

        implementationSphere = createCommonFilterModelComboBox("Выберите сферу реализации проекта");
        FieldLabel implementationSphereLabel = createFieldLabelTop(implementationSphere, "Сфера реализации");

        implementationSector = createCommonFilterModelComboBox("Выберите отрасль реализации");
        FieldLabel implementationSectorLabel = createFieldLabelTop(implementationSector, "Отрасль реализации");

        objectType = createCommonFilterModelMultiSelectComboBox("Выберите вид объекта");
        FieldLabel objectTypeLabel = createFieldLabelTop(objectType, "Вид объекта");

        objectLocation = new TextField();
        FieldLabel objectLocationLabel = createFieldLabelTop(objectLocation, "Место нахождения объекта");
        objectLocation.setName("Место нахождения объекта");
        objectLocation.addValidator(new MaxLengthValidator(5000));

        agreementSubject = createCommonFilterModelMultiSelectComboBox("Предмет соглашения");
        FieldLabel agreementSubjectLabel = createFieldLabelTop(agreementSubject, "Предмет соглашения");

        realizationStatus = createCommonFilterModelComboBox("Стадия реализации проекта");
        FieldLabel realizationStatusLabel = createFieldLabelTop(realizationStatus, "Стадия реализации проекта");

        completedTemplateFileUpload = new FileUploader();
        completedTemplateFileUpload.setHeadingText("Сведения по условным и безусловным обязательствам:");

        manual = new HTML(new SafeHtmlBuilder().appendHtmlConstant("<a style=\"text-decoration:none\" href=\"https://gasu-office.roskazna.ru/rest/files/download/453957459\">\n" +
                "    <i style=\"padding: 5px; cursor: pointer; color:blue;\" class=\"fa fa-file-excel-o btn fa-lg\" aria-hidden=\"true\"></i>\n" +
                "    <span style=\"color:blue; font-style: italic;\">Скачать шаблон</span>\n" +
                "</a>").toSafeHtml());

        container.add(projectNameFieldLabel, layoutData);
        container.add(implementationFormLabel, layoutData);
        container.add(initMethodLabel, layoutData);
        container.add(implementationLvlLabel, layoutData);
//        container.add(isRFPartOfAgreement, layoutData);
//        container.add(isRegionPartOfAgreement, layoutData);
//        container.add(isMunicipalityPartOfAgreement, layoutData);
        container.add(regionLabel, layoutData);
        container.add(municipalityLabel, layoutData);
        container.add(ppkLabel, layoutData);
//        container.add(innContainer, layoutData);
//        container.add(balanceHolderContainer, layoutData);
//        container.add(implementerLabel, layoutData);
//        container.add(implementerInnLabel, layoutData);
//        container.add(opfLabel, layoutData);
//        container.add(isForeignInvestor, layoutData);
//        container.add(isMcpSubject, layoutData);
//        container.add(isSpecialProjectCompany, layoutData);
//        container.add(hasInvestmentProperty, layoutData);
//        container.add(publicSharePercentageContainer, layoutData);
//        container.add(isRFHasShare, layoutData);
        container.add(implementationSphereLabel, layoutData);
        container.add(implementationSectorLabel, layoutData);
        container.add(objectTypeLabel, layoutData);
        container.add(objectLocationLabel, layoutData);
        container.add(agreementSubjectLabel, layoutData);
        container.add(realizationStatusLabel, layoutData);
//        container.add(manual, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(20, 0, 0, 0)));
//        container.add(completedTemplateFileUpload, layoutData);

    }

    private TwinTriggerComboBox<SvrOrg> createSvrOrgsModelComboBox(String emptyText) {
        TwinTriggerComboBox<SvrOrg> comboBox = new TwinTriggerComboBox<>(
                new ListStore<>(item -> item.getId()),
                item -> item.getName(),
                new AbstractSafeHtmlRenderer<SvrOrg>() {
                    @Override
                    public SafeHtml render(SvrOrg object) {
                        return wrapString(object.getName());
                    }
                });
        comboBox.setEmptyText(emptyText);
        comboBox.setEditable(false);
        comboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        return comboBox;
    }


    private static ViewAttrState viewAttrState;
    static {
        viewAttrState = new ViewAttrState();

        viewAttrState.getColumns().addAll(ViewAttrStateColumns.getFormIds());

        viewAttrState.getState().put("projectIdField",                      Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("projectNameField",                    Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("realizationForm",                     Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("initMethod",                          Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be missed, always show
        viewAttrState.getState().put("implementationLvl",                   Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("isRegionPartOfAgreement",             Arrays.asList(1, 0, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0));
        viewAttrState.getState().put("isRFPartOfAgreement",                 Arrays.asList(0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("isMunicipalityPartOfAgreement",       Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0));
        viewAttrState.getState().put("region",                              Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("municipality",                        Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("ppk",                                 Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("inn",                                 Arrays.asList(0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 1));
        viewAttrState.getState().put("balanceHolder",                       Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("implementer",                         Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("opf",                                 Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("implementerInn",                      Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("isForeignInvestor",                   Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1));
        viewAttrState.getState().put("isMcpSubject",                        Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1));
        viewAttrState.getState().put("isSpecialProjectCompany",             Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0));
        viewAttrState.getState().put("hasInvestmentProperty",               Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0));
        viewAttrState.getState().put("publicSharePercentage",               Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0));
        viewAttrState.getState().put("isRFHasShare",                        Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0));
        viewAttrState.getState().put("implementationSphere",                Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("implementationSector",                Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("objectType",                          Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("objectLocation",                      Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("agreementSubject",                    Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("realizationStatus",                   Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("completedTemplateFileUpload",         Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be missed, always show
    }

    /**
     * Обновить форму (скрыть + очистить/показать поля) в зависимости от формы реализации проекта
     * и способа инициации проекта.
     * @param formId - форма реализации проекта
     * @param initId - способ инициации проекта
     */
    public void update(Long formId, Long initId) {
        String formIdStr = (formId == null) ? null : String.valueOf(formId);

        // checkbox isRegionPartOfAgreement
        if (viewAttrState.isAttrActive(formIdStr, "isRegionPartOfAgreement")) {
            isRegionPartOfAgreement.show();
        } else {
            isRegionPartOfAgreement.hide();
            isRegionPartOfAgreement.setValue(null);
        }

        // checkbox isRFPartOfAgreement
        if (viewAttrState.isAttrActive(formIdStr, "isRFPartOfAgreement")) {
            isRFPartOfAgreement.show();
        } else {
            isRFPartOfAgreement.hide();
            isRFPartOfAgreement.setValue(null);
        }

        // checkbox isMunicipalityPartOfAgreement
        if (viewAttrState.isAttrActive(formIdStr, "isMunicipalityPartOfAgreement")) {
            isMunicipalityPartOfAgreement.show();
        } else {
            isMunicipalityPartOfAgreement.hide();
            isMunicipalityPartOfAgreement.setValue(null);
        }

        // text inn
        if (viewAttrState.isAttrActive(formIdStr, "inn")) {
            innContainer.show();
        } else {
            innContainer.hide();
            inn.setValue(null);
        }

        // text balanceHolder
        if (viewAttrState.isAttrActive(formIdStr, "balanceHolder")) {
            balanceHolderContainer.show();
        } else {
            balanceHolderContainer.hide();
            balanceHolder.setValue(null);
        }

        // checkbox isForeignInvestor
        if (viewAttrState.isAttrActive(formIdStr, "isForeignInvestor")) {
            isForeignInvestor.show();
        } else {
            isForeignInvestor.hide();
            isForeignInvestor.setValue(null);
        }

        // checkbox isMcpSubject
        if (viewAttrState.isAttrActive(formIdStr, "isMcpSubject")) {
            isMcpSubject.show();
        } else {
            isMcpSubject.hide();
            isMcpSubject.setValue(null);
        }

        // checkbox isSpecialProjectCompany
        if (viewAttrState.isAttrActive(formIdStr, "isSpecialProjectCompany")) {
            isSpecialProjectCompany.show();
        } else {
            isSpecialProjectCompany.hide();
            isSpecialProjectCompany.setValue(null);
        }

        // checkbox hasInvestmentProperty
        if (viewAttrState.isAttrActive(formIdStr, "hasInvestmentProperty")) {
            hasInvestmentProperty.show();
        } else {
            hasInvestmentProperty.hide();
            hasInvestmentProperty.setValue(null);
        }

        // double publicSharePercentage
        if (viewAttrState.isAttrActive(formIdStr, "publicSharePercentage")) {
            publicSharePercentageContainer.show();
        } else {
            publicSharePercentageContainer.hide();
            publicSharePercentage.setValue(null);
        }
        // checkbox isRFHasShare
        if (viewAttrState.isAttrActive(formIdStr, "isRFHasShare")) {
            isRFHasShare.show();
        } else {
            isRFHasShare.hide();
            isRFHasShare.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "completedTemplateFileUpload") && region.getValue() != null && region.getValue().getName().equals(REGION_RF)) {
            completedTemplateFileUpload.toggle(true);
            manual.setVisible(true);
        } else {
            completedTemplateFileUpload.toggle(false);
            completedTemplateFileUpload.setFiles(new ArrayList<>());
            manual.setVisible(false);
        }

        if (viewAttrState.isAttrActive(formIdStr, "initMethod")) {
            initMethod.show();
            initMethodLabel.show();
        } else {
            initMethod.hide();
            initMethodLabel.hide();
            initMethod.setValue(null);
        }

        container.forceLayout();
    }


}
