package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.*;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import ru.ibs.gasu.client.widgets.componens.DateFieldFullDate;
import ru.ibs.gasu.client.widgets.componens.FileUploader;
import ru.ibs.gasu.client.widgets.componens.TwinTriggerComboBox;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.SimpleIdNameModel;

import java.util.Arrays;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class ContingentBudgetaryCommitmentsView implements IsWidget {

    private VerticalLayoutContainer container;
    private ContentPanel cp1;

    private VerticalLayoutContainer subsection1;
    private VerticalLayoutContainer subsection2;
    private VerticalLayoutContainer subsection3;
    private VerticalLayoutContainer subsection4;
    private CheckBox minimumGuaranteedExist; //11.1
    private CheckBox projectBudgetObligationMissing; //11.0
    private TwinTriggerComboBox<SimpleIdNameModel> minimumGuaranteedIncomeForm; //11.2
    private VerticalLayoutContainer minimumGuaranteedIncomeFormContainer;
    private SimpleYearTableWidget minimumGuaranteedAmount;//11.3
    private YearTableWidget minimumGuaranteedAmountNew;//11.3
    private CheckBox cbcNdsCheck;
    private ComboBox<SimpleIdNameModel> cbcMeasureType;
    private DateField cbcDateField;
    private HorizontalLayoutContainer treeHRow;
    private VerticalLayoutContainer minimumGuaranteedAmountContainer;
    private FileUploader minimumGuaranteedFileUpload;//11.4
    private VerticalLayoutContainer minimumGuaranteedFileUploadContainer;
    private TextField minimumGuaranteedClauseAgreement;//11.5
    private CheckBox compensationMinimumGuaranteedExist;//11.6
    private SimpleYearTableWidget compensationMinimumGuaranteedAmount;//11.7
    private YearTableWidget compensationMinimumGuaranteedAmountNew;//11.7
    private VerticalLayoutContainer compensationMinimumGuaranteedAmountContainer;
    private FileUploader nonPaymentConsumersFileUpload;//11.8
    private CheckBox nonPaymentConsumersGoodsProvidedExist;//11.9
    private TwinTriggerComboBox<SimpleIdNameModel> nonPaymentConsumersGoodsProvidedForm;//11.10
    private TextField nonPaymentConsumersGoodsProvidedClauseAgreement;//11.10.1
    private VerticalLayoutContainer nonPaymentConsumersGoodsProvidedFormContainer;
    private CheckBox limitNonPaymentConsumersGoodsProvidedExist;//11.11
    private TextField limitNonPaymentConsumersGoodsProvidedClauseAgreement;//11.12
    private VerticalLayoutContainer limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer;
    private CheckBox compensationLimitNonPaymentConsumersGoodsProvidedExist;//11.13
    private VerticalLayoutContainer compensationLimitNonPaymentConsumersGoodsProvidedExistContainer;
    private SimpleYearTableWidget compensationLimitNonPaymentAmount;//11.14
    private YearTableWidget compensationLimitNonPaymentAmountNew;//11.14
    private VerticalLayoutContainer compensationLimitNonPaymentAmountContainer;
    private FileUploader arisingProvisionOfBenefitFileUpload;//11.15
    private VerticalLayoutContainer arisingProvisionOfBenefitFileUploadContainer;
    private CheckBox arisingProvisionOfBenefitsExist;//11.16
    private TextField cbcArisingProvisionOfBenefitsClauseAgreement;//11.16.1
    private CheckBox compensationArisingProvisionOfBenefitsExist;//11.17
    private VerticalLayoutContainer compensationArisingProvisionOfBenefitsExistContainer;
    private SimpleYearTableWidget compensationArisingProvisionOfBenefitsAmount;//11.18
    private YearTableWidget compensationArisingProvisionOfBenefitsAmountNew;//11.18
    private VerticalLayoutContainer compensationArisingProvisionOfBenefitsAmountContainer;
    private TextField compensationArisingProvisionOfBenefitsClauseAgreement;//11.19
    private VerticalLayoutContainer compensationArisingProvisionOfBenefitsClauseAgreementContainer;
    private FileUploader compensationArisingProvisionOfBenefitsFileUpload;//11.20
    private VerticalLayoutContainer compensationArisingProvisionOfBenefitsFileUploadContainer;
    private CheckBox dueToOnsetOfCertainCircumstancesExist;//11.21
    private TextField cbcDueToOnsetOfCertainCircumstancesClauseAgreement;//11.21.1
    private CheckBox limitCompensationAdditionalCostsAgreementExist;//11.22
    private VerticalLayoutContainer limitCompensationAdditionalCostsAgreementExistContainer;
    private LimitCompensationAdditionalCostsWidget limitCompensationAdditionalCostsAmount;//11.23
    private VerticalLayoutContainer limitCompensationAdditionalCostsAmountContainer;
    private TextField limitCompensationAdditionalClauseAgreement;//11.24
    private VerticalLayoutContainer limitCompensationAdditionalClauseAgreementContainer;
    private VerticalLayoutContainer nameOfCircumstanceAdditionalCosContainer;
    private MultiSelectComboBox<SimpleIdNameModel> nameOfCircumstanceAdditionalCostPrepare;//11.25
    private TextField specifyOtherCircumstancesPrepare;//11.25.1
    private VerticalLayoutContainer specifyOtherCircumstancesPrepareContainer;
    private MultiSelectComboBox<SimpleIdNameModel> nameOfCircumstanceAdditionalCostBuild;//11.25
    private TextField specifyOtherCircumstancesBuild;//11.25.1
    private VerticalLayoutContainer specifyOtherCircumstancesBuildContainer;
    private MultiSelectComboBox<SimpleIdNameModel> nameOfCircumstanceAdditionalCostExploitation;//11.25
    private TextField specifyOtherCircumstancesExploitation;//11.25.1
    private VerticalLayoutContainer specifyOtherCircumstancesExploitationContainer;
    private CheckBox compensationAdditionalCostsAgreementExist;//11.26
    private VerticalLayoutContainer compensationAdditionalCostsAgreementExistContainer;
    private CircumstancesAdditionalCostsWidget circumstancesAdditionalCostsAmount = new CircumstancesAdditionalCostsWidget();//11.27 таблица
    private VerticalLayoutContainer circumstancesAdditionalCostsWidgetContainer;
    private FileUploader compensationAdditionalCostsAgreementFileUpload;//11.28

    public ContingentBudgetaryCommitmentsView() {
        initWidget();
    }

    public CheckBox getMinimumGuaranteedExist() {
        return minimumGuaranteedExist;
    }

    public TwinTriggerComboBox<SimpleIdNameModel> getMinimumGuaranteedIncomeForm() {
        return minimumGuaranteedIncomeForm;
    }

    public TextField getNonPaymentConsumersGoodsProvidedClauseAgreement() {
        return nonPaymentConsumersGoodsProvidedClauseAgreement;
    }

    public TextField getCbcArisingProvisionOfBenefitsClauseAgreement() {
        return cbcArisingProvisionOfBenefitsClauseAgreement;
    }

    public TextField getCbcDueToOnsetOfCertainCircumstancesClauseAgreement() {
        return cbcDueToOnsetOfCertainCircumstancesClauseAgreement;
    }

    public FileUploader getMinimumGuaranteedFileUpload() {
        return minimumGuaranteedFileUpload;
    }

    public TextField getMinimumGuaranteedClauseAgreement() {
        return minimumGuaranteedClauseAgreement;
    }

    public CheckBox getCompensationMinimumGuaranteedExist() {
        return compensationMinimumGuaranteedExist;
    }

    public CheckBox getProjectBudgetObligationMissing() {
        return projectBudgetObligationMissing;
    }

    public FileUploader getNonPaymentConsumersFileUpload() {
        return nonPaymentConsumersFileUpload;
    }

    public CheckBox getNonPaymentConsumersGoodsProvidedExist() {
        return nonPaymentConsumersGoodsProvidedExist;
    }

    public TwinTriggerComboBox<SimpleIdNameModel> getNonPaymentConsumersGoodsProvidedForm() {
        return nonPaymentConsumersGoodsProvidedForm;
    }

    public CheckBox getLimitNonPaymentConsumersGoodsProvidedExist() {
        return limitNonPaymentConsumersGoodsProvidedExist;
    }

    public TextField getLimitNonPaymentConsumersGoodsProvidedClauseAgreement() {
        return limitNonPaymentConsumersGoodsProvidedClauseAgreement;
    }

    public CheckBox getCompensationLimitNonPaymentConsumersGoodsProvidedExist() {
        return compensationLimitNonPaymentConsumersGoodsProvidedExist;
    }

    public FileUploader getArisingProvisionOfBenefitFileUpload() {
        return arisingProvisionOfBenefitFileUpload;
    }

    public CheckBox getArisingProvisionOfBenefitsExist() {
        return arisingProvisionOfBenefitsExist;
    }

    public CheckBox getCompensationArisingProvisionOfBenefitsExist() {
        return compensationArisingProvisionOfBenefitsExist;
    }

    public TextField getCompensationArisingProvisionOfBenefitsClauseAgreement() {
        return compensationArisingProvisionOfBenefitsClauseAgreement;
    }

    public FileUploader getCompensationArisingProvisionOfBenefitsFileUpload() {
        return compensationArisingProvisionOfBenefitsFileUpload;
    }

    public CheckBox getDueToOnsetOfCertainCircumstancesExist() {
        return dueToOnsetOfCertainCircumstancesExist;
    }

    public CheckBox getLimitCompensationAdditionalCostsAgreementExist() {
        return limitCompensationAdditionalCostsAgreementExist;
    }

    public TextField getLimitCompensationAdditionalClauseAgreement() {
        return limitCompensationAdditionalClauseAgreement;
    }

    public MultiSelectComboBox<SimpleIdNameModel> getNameOfCircumstanceAdditionalCostPrepare() {
        return nameOfCircumstanceAdditionalCostPrepare;
    }

    public MultiSelectComboBox<SimpleIdNameModel> getNameOfCircumstanceAdditionalCostBuild() {
        return nameOfCircumstanceAdditionalCostBuild;
    }

    public MultiSelectComboBox<SimpleIdNameModel> getNameOfCircumstanceAdditionalCostExploitation() {
        return nameOfCircumstanceAdditionalCostExploitation;
    }

    public TextField getSpecifyOtherCircumstancesPrepare() {
        return specifyOtherCircumstancesPrepare;
    }

    public TextField getSpecifyOtherCircumstancesBuild() {
        return specifyOtherCircumstancesBuild;
    }

    public TextField getSpecifyOtherCircumstancesExploitation() {
        return specifyOtherCircumstancesExploitation;
    }

    public CheckBox getCompensationAdditionalCostsAgreementExist() {
        return compensationAdditionalCostsAgreementExist;
    }

    public CircumstancesAdditionalCostsWidget getCircumstancesAdditionalCostsAmount() {
        return circumstancesAdditionalCostsAmount;
    }

    public FileUploader getCompensationAdditionalCostsAgreementFileUpload() {
        return compensationAdditionalCostsAgreementFileUpload;
    }

    public SimpleYearTableWidget getMinimumGuaranteedAmount() {
        return minimumGuaranteedAmount;
    }

    public YearTableWidget getMinimumGuaranteedAmountNew() {
        return minimumGuaranteedAmountNew;
    }

    public YearTableWidget getCompensationMinimumGuaranteedAmountNew() {
        return compensationMinimumGuaranteedAmountNew;
    }

    public YearTableWidget getCompensationLimitNonPaymentAmountNew() {
        return compensationLimitNonPaymentAmountNew;
    }

    public YearTableWidget getCompensationArisingProvisionOfBenefitsAmountNew() {
        return compensationArisingProvisionOfBenefitsAmountNew;
    }

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public CheckBox getCbcNdsCheck() {
        return cbcNdsCheck;
    }

    public ComboBox<SimpleIdNameModel> getCbcMeasureType() {
        return cbcMeasureType;
    }

    public DateField getCbcDateField() {
        return cbcDateField;
    }

    public SimpleYearTableWidget getCompensationMinimumGuaranteedAmount() {
        return compensationMinimumGuaranteedAmount;
    }

    public SimpleYearTableWidget getCompensationLimitNonPaymentAmount() {
        return compensationLimitNonPaymentAmount;
    }

    public SimpleYearTableWidget getCompensationArisingProvisionOfBenefitsAmount() {
        return compensationArisingProvisionOfBenefitsAmount;
    }

    public LimitCompensationAdditionalCostsWidget getLimitCompensationAdditionalCostsAmount() {
        return limitCompensationAdditionalCostsAmount;
    }

    public VerticalLayoutContainer getNameOfCircumstanceAdditionalCosContainer() {
        return nameOfCircumstanceAdditionalCosContainer;
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        AccordionLayoutContainer.AccordionLayoutAppearance appearance = GWT.<AccordionLayoutContainer.AccordionLayoutAppearance>create(AccordionLayoutContainer.AccordionLayoutAppearance.class);
        MarginData cpMargin = new MarginData(10, 10, 10, 20);
        cp1 = new ContentPanel(appearance);
        cp1.setAnimCollapse(false);
        cp1.setHeading("Основания для выплаты компенсации");
        VerticalLayoutContainer cp1V = new VerticalLayoutContainer();
        subsection1 = new VerticalLayoutContainer();
        subsection2 = new VerticalLayoutContainer();
        subsection3 = new VerticalLayoutContainer();
        subsection4 = new VerticalLayoutContainer();

        projectBudgetObligationMissing = new CheckBox();
        projectBudgetObligationMissing.setBoxLabel(wrapString("Условные бюджетные обязательства по проекту отсутствуют"));
        cp1V.add(projectBudgetObligationMissing, STD_VC_LAYOUT);

        minimumGuaranteedExist = new CheckBox();
        minimumGuaranteedExist.setBoxLabel(wrapString("Наличие в соглашении условий по выплате недополученных доходов, обеспечивающих концессионеру/частному партнеру минимальный гарантированный доход (выручку)"));
        cp1V.add(minimumGuaranteedExist, STD_VC_LAYOUT);

        minimumGuaranteedIncomeForm = createCommonFilterModelTwinTriggerComboBox("Выберите вид минимального гарантированного дохода");
        FieldLabel groundsOfAgreementConclusionLabel = createFieldLabelTop(minimumGuaranteedIncomeForm, "Минимальный гарантированный доход установлен в виде");
        minimumGuaranteedIncomeFormContainer = new VerticalLayoutContainer();
        minimumGuaranteedIncomeFormContainer.add(groundsOfAgreementConclusionLabel);
        subsection1.add(minimumGuaranteedIncomeFormContainer, STD_VC_LAYOUT);

        minimumGuaranteedAmountContainer = new VerticalLayoutContainer();
        minimumGuaranteedAmountContainer.add(createHtml("<br> "));
        minimumGuaranteedAmountContainer.add(createHtml("Предельная сумма компенсации концессионеру/частному партнеру по обеспечению его минимального гарантированного дохода " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='При наличии в соглашении такого предела'></i>"));
        minimumGuaranteedAmount = new SimpleYearTableWidget(true);
        minimumGuaranteedAmountNew = new YearTableWidget();
        minimumGuaranteedAmountContainer.add(minimumGuaranteedAmountNew);
        subsection1.add(minimumGuaranteedAmountContainer);

        minimumGuaranteedFileUpload = new FileUploader();
        minimumGuaranteedFileUpload.setHeadingText(new SafeHtmlBuilder().appendHtmlConstant("Расчет предельной суммы компенсации концессионеру/ частному партнеру по обеспечению его минимального гарантированного дохода " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Расчёт содержит разницу между прогнозным доходом и его минимально гарантированной величиной'></i>").toSafeHtml());
        minimumGuaranteedFileUploadContainer = new VerticalLayoutContainer();
        minimumGuaranteedFileUploadContainer.add(minimumGuaranteedFileUpload);
        subsection1.add(minimumGuaranteedFileUploadContainer);

        minimumGuaranteedClauseAgreement = new TextField();
        minimumGuaranteedClauseAgreement.setName("Пункт(ы) соглашения");
        minimumGuaranteedClauseAgreement.addValidator(new MaxLengthValidator(255));
        FieldLabel clauseAgreementLabel = createFieldLabelTop(minimumGuaranteedClauseAgreement, noWrapString("Пункт(ы) соглашения"));
        subsection1.add(clauseAgreementLabel, STD_VC_LAYOUT);

        compensationMinimumGuaranteedExist = new CheckBox();
        compensationMinimumGuaranteedExist.setBoxLabel(wrapString("Произведена компенсация концессионеру/частному партнеру недополученных доходов, обеспечивающих концессионеру/частному партнеру минимальный гарантированный доход (выручку)"));
        subsection1.add(compensationMinimumGuaranteedExist, STD_VC_LAYOUT);

        compensationMinimumGuaranteedAmountContainer = new VerticalLayoutContainer();
        compensationMinimumGuaranteedAmountContainer.add(createHtml("<br> "));
        compensationMinimumGuaranteedAmountContainer.add(createHtml("Размер фактически выплаченной компенсации концессионеру/ частному партнеру по обеспечению его минимального гарантированного дохода"));
        compensationMinimumGuaranteedAmount = new SimpleYearTableWidget(false);
        compensationMinimumGuaranteedAmountNew = new YearTableWidget();
        compensationMinimumGuaranteedAmountContainer.add(compensationMinimumGuaranteedAmountNew);
        subsection1.add(compensationMinimumGuaranteedAmountContainer);

        nonPaymentConsumersFileUpload = new FileUploader();
        nonPaymentConsumersFileUpload.setHeadingText("Обосновывающие документы (в части фактических выплат и пр.)");
        subsection1.add(nonPaymentConsumersFileUpload, STD_VC_LAYOUT);

        cp1V.add(subsection1, STD_VC_LAYOUT);

        nonPaymentConsumersGoodsProvidedExist = new CheckBox();
        nonPaymentConsumersGoodsProvidedExist.setBoxLabel(wrapString("Наличие в соглашении условий по выплате концессионеру/частному партнеру недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг"));
        cp1V.add(nonPaymentConsumersGoodsProvidedExist, STD_VC_LAYOUT);

        nonPaymentConsumersGoodsProvidedForm = createCommonFilterModelTwinTriggerComboBox("Выберите вид выплаты");
        FieldLabel nonPaymentConsumersGoodsProvidedFormLabel = createFieldLabelTop(nonPaymentConsumersGoodsProvidedForm, "Включается ли в соответствии с условиями соглашения выплата концессионеру/частному партнеру недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг, в состав компенсации концессионеру/частному партнеру недополученных доходов, обеспечивающих минимальный гарантированный доход (выручку)");
        nonPaymentConsumersGoodsProvidedFormContainer = new VerticalLayoutContainer();
        nonPaymentConsumersGoodsProvidedFormContainer.add(nonPaymentConsumersGoodsProvidedFormLabel);
        subsection2.add(nonPaymentConsumersGoodsProvidedFormContainer, STD_VC_LAYOUT);

        nonPaymentConsumersGoodsProvidedClauseAgreement = new TextField();
        nonPaymentConsumersGoodsProvidedClauseAgreement.setName("Пункт(ы) соглашения");
        nonPaymentConsumersGoodsProvidedClauseAgreement.addValidator(new MaxLengthValidator(255));
        FieldLabel nonPaymentConsumersGoodsProvidedClauseAgreementLabel = createFieldLabelTop(nonPaymentConsumersGoodsProvidedClauseAgreement, noWrapString("Пункт(ы) соглашения"));
        subsection2.add(nonPaymentConsumersGoodsProvidedClauseAgreementLabel, STD_VC_LAYOUT);

        limitNonPaymentConsumersGoodsProvidedExist = new CheckBox();
        limitNonPaymentConsumersGoodsProvidedExist.setBoxLabel(wrapString("Установлен предел компенсации концессионеру/частному партнеру недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг"));
        subsection2.add(limitNonPaymentConsumersGoodsProvidedExist, STD_VC_LAYOUT);

        limitNonPaymentConsumersGoodsProvidedClauseAgreement = new TextField();
        limitNonPaymentConsumersGoodsProvidedClauseAgreement.setName("Пункт(ы) соглашения");
        limitNonPaymentConsumersGoodsProvidedClauseAgreement.addValidator(new MaxLengthValidator(255));
        FieldLabel limitNonPaymentConsumersGoodsProvidedClauseAgreementLabel = createFieldLabelTop(limitNonPaymentConsumersGoodsProvidedClauseAgreement, noWrapString("Пункт(ы) соглашения"));
        limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer = new VerticalLayoutContainer();
        limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer.add(limitNonPaymentConsumersGoodsProvidedClauseAgreementLabel);
        subsection2.add(limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer, STD_VC_LAYOUT);

        compensationLimitNonPaymentConsumersGoodsProvidedExist = new CheckBox();
        compensationLimitNonPaymentConsumersGoodsProvidedExist.setBoxLabel(wrapString("Произведена компенсация концессионеру/частному партнеру недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг"));
        compensationLimitNonPaymentConsumersGoodsProvidedExistContainer = new VerticalLayoutContainer();
        compensationLimitNonPaymentConsumersGoodsProvidedExistContainer.add(compensationLimitNonPaymentConsumersGoodsProvidedExist);
        subsection2.add(compensationLimitNonPaymentConsumersGoodsProvidedExistContainer, STD_VC_LAYOUT);

        compensationLimitNonPaymentAmountContainer = new VerticalLayoutContainer();
        compensationLimitNonPaymentAmountContainer.add(createHtml("<br> "));
        compensationLimitNonPaymentAmountContainer.add(createHtml("Размер фактически выплаченной концессионеру/частному партнеру компенсации недополученных доходов, возникающих в связи с неоплатой потребителями предоставленных товаров, услуг"));
        compensationLimitNonPaymentAmount = new SimpleYearTableWidget(false);
        compensationLimitNonPaymentAmountNew = new YearTableWidget();
        compensationLimitNonPaymentAmountContainer.add(compensationLimitNonPaymentAmountNew);
        subsection2.add(compensationLimitNonPaymentAmountContainer);

        arisingProvisionOfBenefitFileUpload = new FileUploader();
        arisingProvisionOfBenefitFileUpload.setHeadingText("Обосновывающие документы (в части фактических выплат и пр.)");
        arisingProvisionOfBenefitFileUploadContainer = new VerticalLayoutContainer();
        arisingProvisionOfBenefitFileUploadContainer.add(arisingProvisionOfBenefitFileUpload);
        subsection2.add(arisingProvisionOfBenefitFileUploadContainer, STD_VC_LAYOUT);

        cp1V.add(subsection2, STD_VC_LAYOUT);

        arisingProvisionOfBenefitsExist = new CheckBox();
        arisingProvisionOfBenefitsExist.setBoxLabel(wrapString("Наличие в соглашении условий по выплате концессионеру/частному партнеру недополученных доходов, возникающих при предоставлении льгот потребителям услуг, а также при предоставлении товаров/услуг для нужд концедента"));
        cp1V.add(arisingProvisionOfBenefitsExist, STD_VC_LAYOUT);


        cbcArisingProvisionOfBenefitsClauseAgreement = new TextField();
        cbcArisingProvisionOfBenefitsClauseAgreement.setName("Пункт(ы) соглашения");
        cbcArisingProvisionOfBenefitsClauseAgreement.addValidator(new MaxLengthValidator(255));
        FieldLabel cbcArisingProvisionOfBenefitsClauseAgreementLabel = createFieldLabelTop(cbcArisingProvisionOfBenefitsClauseAgreement, noWrapString("Пункт(ы) соглашения"));
        subsection3.add(cbcArisingProvisionOfBenefitsClauseAgreementLabel, STD_VC_LAYOUT);

        cp1V.add(subsection3, STD_VC_LAYOUT);

        compensationArisingProvisionOfBenefitsExist = new CheckBox();
        compensationArisingProvisionOfBenefitsExist.setBoxLabel(wrapString("Произведена компенсация концессионеру/частному партнеру недополученных доходов, возникающих при предоставлении льгот потребителям услуг, а также при предоставлении товаров/услуг для нужд концедента"));
        compensationArisingProvisionOfBenefitsExistContainer = new VerticalLayoutContainer();
        compensationArisingProvisionOfBenefitsExistContainer.add(compensationArisingProvisionOfBenefitsExist);
        subsection3.add(compensationArisingProvisionOfBenefitsExistContainer, STD_VC_LAYOUT);

        compensationArisingProvisionOfBenefitsAmountContainer = new VerticalLayoutContainer();
        compensationArisingProvisionOfBenefitsAmountContainer.add(createHtml("<br> "));
        compensationArisingProvisionOfBenefitsAmountContainer.add(createHtml("Размер фактически выплаченной компенсации концессионеру/частному партнеру льгот потребителям услуг, а также при предоставлении товаров/услуг для нужд концедента"));
        compensationArisingProvisionOfBenefitsAmount = new SimpleYearTableWidget(false);
        compensationArisingProvisionOfBenefitsAmountNew = new YearTableWidget();
        compensationArisingProvisionOfBenefitsAmountContainer.add(compensationArisingProvisionOfBenefitsAmountNew);
        subsection3.add(compensationArisingProvisionOfBenefitsAmountContainer);

        compensationArisingProvisionOfBenefitsClauseAgreement = new TextField();
        compensationArisingProvisionOfBenefitsClauseAgreement.setName("Пункт(ы) соглашения");
        compensationArisingProvisionOfBenefitsClauseAgreement.addValidator(new MaxLengthValidator(255));
        FieldLabel compensationArisingProvisionOfBenefitsClauseAgreementLabel = createFieldLabelTop(compensationArisingProvisionOfBenefitsClauseAgreement, noWrapString("Пункт(ы) соглашения"));
        compensationArisingProvisionOfBenefitsClauseAgreementContainer = new VerticalLayoutContainer();
        compensationArisingProvisionOfBenefitsClauseAgreementContainer.add(compensationArisingProvisionOfBenefitsClauseAgreementLabel);
        subsection3.add(compensationArisingProvisionOfBenefitsClauseAgreementContainer, STD_VC_LAYOUT);

        compensationArisingProvisionOfBenefitsFileUpload = new FileUploader();
        compensationArisingProvisionOfBenefitsFileUpload.setHeadingText("Обосновывающие документы (в части фактических выплат и пр.)");
        compensationArisingProvisionOfBenefitsFileUploadContainer = new VerticalLayoutContainer();
        compensationArisingProvisionOfBenefitsFileUploadContainer.add(compensationArisingProvisionOfBenefitsFileUpload);
        subsection3.add(compensationArisingProvisionOfBenefitsFileUploadContainer, STD_VC_LAYOUT);

        cp1V.add(subsection3, STD_VC_LAYOUT);

        dueToOnsetOfCertainCircumstancesExist = new CheckBox();
        dueToOnsetOfCertainCircumstancesExist.setBoxLabel(wrapString("Наличие в соглашении условий по компенсации концессионеру/частному партнеру дополнительных расходов/недополученных доходов в связи с наступлением определенных обстоятельств, предусмотренных соглашением(за исключением обстоятельств, указанных ранее)"));
        cp1V.add(dueToOnsetOfCertainCircumstancesExist, STD_VC_LAYOUT);

        cbcDueToOnsetOfCertainCircumstancesClauseAgreement = new TextField();
        cbcDueToOnsetOfCertainCircumstancesClauseAgreement.setName("Пункт(ы) соглашения");
        cbcDueToOnsetOfCertainCircumstancesClauseAgreement.addValidator(new MaxLengthValidator(255));
        FieldLabel cbcDueToOnsetOfCertainCircumstancesClauseAgreementLabel = createFieldLabelTop(cbcDueToOnsetOfCertainCircumstancesClauseAgreement, noWrapString("Пункт(ы) соглашения"));
        subsection4.add(cbcDueToOnsetOfCertainCircumstancesClauseAgreementLabel, STD_VC_LAYOUT);

        limitCompensationAdditionalCostsAgreementExist = new CheckBox();
        limitCompensationAdditionalCostsAgreementExist.setBoxLabel(wrapString("Установлен предел  компенсации концессионеру/частному партнеру его\n дополнительных расходов/недополученных доходов по соглашению"));
        limitCompensationAdditionalCostsAgreementExistContainer = new VerticalLayoutContainer();
        limitCompensationAdditionalCostsAgreementExistContainer.add(limitCompensationAdditionalCostsAgreementExist);
        subsection4.add(limitCompensationAdditionalCostsAgreementExist, STD_VC_LAYOUT);

        limitCompensationAdditionalCostsAmountContainer = new VerticalLayoutContainer();
        limitCompensationAdditionalCostsAmountContainer.add(createHtml("<br> "));
        limitCompensationAdditionalCostsAmountContainer.add(createHtml("Предельная сумма компенсации концессионеру/частному партнеру его дополнительных расходов/недополученных доходов по соглашению"));
        limitCompensationAdditionalCostsAmount = new LimitCompensationAdditionalCostsWidget();

        cbcNdsCheck = new CheckBox();
        cbcNdsCheck.setBoxLabel("Включая НДС");
        cbcMeasureType = createCommonFilterModelComboBox("Выберите тип измерения");
        cbcMeasureType.getStore().add(new SimpleIdNameModel("1", "В ценах соответствующих лет"));
        cbcMeasureType.getStore().add(new SimpleIdNameModel("2", "На дату"));
        cbcDateField = new DateFieldFullDate();

        cbcMeasureType.addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                if ("2".equals(event.getSelectedItem().getId())) {
                    cbcDateField.show();
                } else if ("1".equals(event.getSelectedItem().getId())) {
                    cbcDateField.clear();
                    cbcDateField.hide();
                }
            }
        });
        treeHRow = createThreeFieldWidgetsNdsDateRow(cbcNdsCheck, cbcMeasureType, cbcDateField);

        limitCompensationAdditionalCostsAmountContainer.add(treeHRow, HEIGHT60_VC_LAYOUT);
        limitCompensationAdditionalCostsAmountContainer.add(limitCompensationAdditionalCostsAmount);
        subsection4.add(limitCompensationAdditionalCostsAmountContainer);

        limitCompensationAdditionalClauseAgreement = new TextField();
        limitCompensationAdditionalClauseAgreement.setName("Пункт(ы) соглашения");
        limitCompensationAdditionalClauseAgreement.addValidator(new MaxLengthValidator(255));
        FieldLabel limitCompensationAdditionalClauseAgreementLabel = createFieldLabelTop(limitCompensationAdditionalClauseAgreement, noWrapString("Пункт(ы) соглашения"));
        limitCompensationAdditionalClauseAgreementContainer = new VerticalLayoutContainer();
        limitCompensationAdditionalClauseAgreementContainer.add(limitCompensationAdditionalClauseAgreementLabel);
        subsection4.add(limitCompensationAdditionalClauseAgreementContainer, STD_VC_LAYOUT);

        nameOfCircumstanceAdditionalCosContainer = new VerticalLayoutContainer();
        nameOfCircumstanceAdditionalCosContainer.add(createHtml("Наименование обстоятельства, возникновение которого может повлечь за собой обязательство концедента/публичного партнера по компенсации дополнительных расходов/недополученных доходов концессионера/частного партнера"));
        nameOfCircumstanceAdditionalCostPrepare = createCommonFilterModelMultiSelectComboBox("Выберите обстоятельства");
        FieldLabel nameOfCircumstanceAdditionalCostPrepareLabel = createFieldLabelTop(nameOfCircumstanceAdditionalCostPrepare, "Этап подготовки проекта");

        specifyOtherCircumstancesPrepare = new TextField();
        specifyOtherCircumstancesPrepare.setName("Указать прочие обстоятельства");
        specifyOtherCircumstancesPrepare.addValidator(new MaxLengthValidator(4000));
        FieldLabel specifyOtherCircumstancesPrepareLabel = createFieldLabelTop(specifyOtherCircumstancesPrepare, noWrapString("Указать прочие обстоятельства"));
        specifyOtherCircumstancesPrepareContainer = new VerticalLayoutContainer();
        specifyOtherCircumstancesPrepareContainer.add(specifyOtherCircumstancesPrepareLabel, STD_VC_LAYOUT);

        nameOfCircumstanceAdditionalCostBuild = createCommonFilterModelMultiSelectComboBox("Выберите обстоятельства");
        FieldLabel nameOfCircumstanceAdditionalCostBuildLabel = createFieldLabelTop(nameOfCircumstanceAdditionalCostBuild, "Этап строительства проекта");

        specifyOtherCircumstancesBuild = new TextField();
        specifyOtherCircumstancesBuild.setName("Указать прочие обстоятельства");
        specifyOtherCircumstancesBuild.addValidator(new MaxLengthValidator(4000));
        FieldLabel specifyOtherCircumstancesBuildLabel = createFieldLabelTop(specifyOtherCircumstancesBuild, noWrapString("Указать прочие обстоятельства"));
        specifyOtherCircumstancesBuildContainer = new VerticalLayoutContainer();
        specifyOtherCircumstancesBuildContainer.add(specifyOtherCircumstancesBuildLabel, STD_VC_LAYOUT);

        nameOfCircumstanceAdditionalCostExploitation = createCommonFilterModelMultiSelectComboBox("Выберите обстоятельства");
        FieldLabel nameOfCircumstanceAdditionalCostExploitationLabel = createFieldLabelTop(nameOfCircumstanceAdditionalCostExploitation, "Этап эксплуатации проекта");

        specifyOtherCircumstancesExploitation = new TextField();
        specifyOtherCircumstancesExploitation.setName("Указать прочие обстоятельства");
        specifyOtherCircumstancesExploitation.addValidator(new MaxLengthValidator(4000));
        FieldLabel specifyOtherCircumstancesExploitationLabel = createFieldLabelTop(specifyOtherCircumstancesExploitation, noWrapString("Указать прочие обстоятельства"));
        specifyOtherCircumstancesExploitationContainer = new VerticalLayoutContainer();
        specifyOtherCircumstancesExploitationContainer.add(specifyOtherCircumstancesExploitationLabel, STD_VC_LAYOUT);

        nameOfCircumstanceAdditionalCosContainer.add(nameOfCircumstanceAdditionalCostPrepareLabel, STD_VC_LAYOUT);
        nameOfCircumstanceAdditionalCosContainer.add(specifyOtherCircumstancesPrepareContainer, STD_VC_LAYOUT);
        nameOfCircumstanceAdditionalCosContainer.add(nameOfCircumstanceAdditionalCostBuildLabel, STD_VC_LAYOUT);
        nameOfCircumstanceAdditionalCosContainer.add(specifyOtherCircumstancesBuildContainer, STD_VC_LAYOUT);
        nameOfCircumstanceAdditionalCosContainer.add(nameOfCircumstanceAdditionalCostExploitationLabel, STD_VC_LAYOUT);
        nameOfCircumstanceAdditionalCosContainer.add(specifyOtherCircumstancesExploitationContainer, STD_VC_LAYOUT);
        subsection4.add(nameOfCircumstanceAdditionalCosContainer, STD_VC_LAYOUT);

        compensationAdditionalCostsAgreementExist = new CheckBox();
        compensationAdditionalCostsAgreementExist.setBoxLabel(wrapString("Произведена компенсация концессионеру/частному партнеру его дополнительных расходов/недополученных доходов по соглашению"));
        compensationAdditionalCostsAgreementExistContainer = new VerticalLayoutContainer();
        compensationAdditionalCostsAgreementExistContainer.add(compensationAdditionalCostsAgreementExist);
        subsection4.add(compensationAdditionalCostsAgreementExistContainer, STD_VC_LAYOUT);

        circumstancesAdditionalCostsWidgetContainer = new VerticalLayoutContainer();
        circumstancesAdditionalCostsWidgetContainer.add(createHtml("<br> "));
        circumstancesAdditionalCostsWidgetContainer.add(createHtml("Обстоятельства, возникновение которого повлекло за собой обязательство концедента/публичного партнера по компенсации дополнительных расходов/недополученных доходов концессионера/частного партнера"));
        circumstancesAdditionalCostsWidgetContainer.add(circumstancesAdditionalCostsAmount);
        subsection4.add(circumstancesAdditionalCostsWidgetContainer);

        compensationAdditionalCostsAgreementFileUpload = new FileUploader();
        compensationAdditionalCostsAgreementFileUpload.setHeadingText("Обосновывающие документы (в части фактических выплат и пр.)");
        subsection4.add(compensationAdditionalCostsAgreementFileUpload, STD_VC_LAYOUT);

        specifyOtherCircumstancesPrepareContainer.hide();
        specifyOtherCircumstancesBuildContainer.hide();
        specifyOtherCircumstancesExploitationContainer.hide();

        cp1V.add(subsection4, STD_VC_LAYOUT);

        cp1.add(cp1V, cpMargin);

        AccordionLayoutContainer accordion = new AccordionLayoutContainer();
        accordion.setExpandMode(AccordionLayoutContainer.ExpandMode.MULTI);
        accordion.add(cp1);

        accordion.setActiveWidget(cp1);
        container.add(accordion, STD_VC_LAYOUT);
        setupRelations();
    }

    /**
     * Обновить форму (скрыть + очистить/показать поля) в зависимости от формы реализации проекта
     * и способа инициации проекта.
     *
     * @param formId - форма реализации проекта
     * @param initId - способ инициации проекта
     */
    public void update(Long formId, Long initId) {
        if (minimumGuaranteedExist.getValue()) {
            subsection1.show();
        } else {
            subsection1.hide();
        }
        if (compensationMinimumGuaranteedExist.getValue()) {
            compensationMinimumGuaranteedAmountContainer.show();
        } else {
            compensationMinimumGuaranteedAmountContainer.hide();
        }
        if (nonPaymentConsumersGoodsProvidedExist.getValue()) {
            subsection2.show();
        } else {
            subsection2.hide();
        }
        if (limitNonPaymentConsumersGoodsProvidedExist.getValue()) {
            limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer.show();
        } else {
            limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer.hide();
        }
        if (compensationLimitNonPaymentConsumersGoodsProvidedExist.getValue()) {
            compensationLimitNonPaymentAmountContainer.show();
            arisingProvisionOfBenefitFileUploadContainer.show();
        } else {
            compensationLimitNonPaymentAmountContainer.hide();
            arisingProvisionOfBenefitFileUploadContainer.hide();
        }
        if (arisingProvisionOfBenefitsExist.getValue()) {
            subsection3.show();
        } else {
            subsection3.hide();
        }
        if (compensationArisingProvisionOfBenefitsExist.getValue()) {
            compensationArisingProvisionOfBenefitsAmountContainer.show();
            compensationArisingProvisionOfBenefitsClauseAgreementContainer.show();
            compensationArisingProvisionOfBenefitsFileUploadContainer.show();
        } else {
            compensationArisingProvisionOfBenefitsAmountContainer.hide();
            compensationArisingProvisionOfBenefitsClauseAgreementContainer.hide();
            compensationArisingProvisionOfBenefitsFileUploadContainer.hide();
        }
        if (dueToOnsetOfCertainCircumstancesExist.getValue()) {
            subsection4.show();
        } else {
            subsection4.hide();
        }
        if (limitCompensationAdditionalCostsAgreementExist.getValue()) {
            limitCompensationAdditionalCostsAmountContainer.show();
            limitCompensationAdditionalClauseAgreementContainer.show();
        } else {
            limitCompensationAdditionalCostsAmountContainer.hide();
            limitCompensationAdditionalClauseAgreementContainer.hide();
        }
        if (compensationAdditionalCostsAgreementExist.getValue()) {
            circumstancesAdditionalCostsWidgetContainer.show();
        } else {
            circumstancesAdditionalCostsWidgetContainer.hide();
        }
        if (minimumGuaranteedIncomeForm.getValue() != null && minimumGuaranteedIncomeForm.getValue().getId().equals("2")) {
            minimumGuaranteedFileUploadContainer.show();
            container.forceLayout();
        } else {
            minimumGuaranteedFileUploadContainer.hide();
        }
        if (nonPaymentConsumersGoodsProvidedForm.getValue() != null && nonPaymentConsumersGoodsProvidedForm.getValue().getId().equals("1")) {
            compensationLimitNonPaymentConsumersGoodsProvidedExistContainer.hide();
            limitNonPaymentConsumersGoodsProvidedExist.hide();
            limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer.hide();
        } else {
            compensationLimitNonPaymentConsumersGoodsProvidedExistContainer.show();
            limitNonPaymentConsumersGoodsProvidedExist.show();
            if (limitNonPaymentConsumersGoodsProvidedExist.getValue()) {
                limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer.show();
            } else {
                limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer.hide();
            }
            container.forceLayout();
        }
        container.forceLayout();
    }

    private void setupRelations() {
        addHandlerToCheckbox(minimumGuaranteedExist, subsection1);
        addHandlerToCheckbox(compensationMinimumGuaranteedExist, compensationMinimumGuaranteedAmountContainer);
        addHandlerToCheckbox(nonPaymentConsumersGoodsProvidedExist, subsection2);
        addHandlerToCheckbox(limitNonPaymentConsumersGoodsProvidedExist, limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer);
        addHandlerToCheckbox(compensationLimitNonPaymentConsumersGoodsProvidedExist, compensationLimitNonPaymentAmountContainer, arisingProvisionOfBenefitFileUploadContainer);
        addHandlerToCheckbox(arisingProvisionOfBenefitsExist, subsection3);
        addHandlerToCheckbox(compensationArisingProvisionOfBenefitsExist, compensationArisingProvisionOfBenefitsAmountContainer, compensationArisingProvisionOfBenefitsClauseAgreementContainer, compensationArisingProvisionOfBenefitsFileUploadContainer);
        addHandlerToCheckbox(dueToOnsetOfCertainCircumstancesExist, subsection4);
        addHandlerToCheckbox(limitCompensationAdditionalCostsAgreementExist, limitCompensationAdditionalCostsAmountContainer, limitCompensationAdditionalClauseAgreementContainer);
        addHandlerToCheckbox(compensationAdditionalCostsAgreementExist, circumstancesAdditionalCostsWidgetContainer);

        minimumGuaranteedIncomeForm.addBeforeSelectionHandler(event -> {
            if (event.getItem() != null && event.getItem().getId().equals("2")) {
                minimumGuaranteedFileUploadContainer.show();
                container.forceLayout();
            } else {
                minimumGuaranteedFileUploadContainer.hide();
            }
        });

        nonPaymentConsumersGoodsProvidedForm.addBeforeSelectionHandler(event -> {
            if (event.getItem() != null && event.getItem().getId().equals("1")) {
                compensationLimitNonPaymentConsumersGoodsProvidedExistContainer.hide();
                limitNonPaymentConsumersGoodsProvidedExist.hide();
                limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer.hide();
            } else {
                compensationLimitNonPaymentConsumersGoodsProvidedExistContainer.show();
                limitNonPaymentConsumersGoodsProvidedExist.show();
                if (limitNonPaymentConsumersGoodsProvidedExist.getValue()) {
                    limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer.show();
                } else {
                    limitNonPaymentConsumersGoodsProvidedClauseAgreementContainer.hide();
                }
                container.forceLayout();
            }
        });

        nameOfCircumstanceAdditionalCostPrepare.addSelectionHandler(selectedItems -> {
            SimpleIdNameModel other = new SimpleIdNameModel();
            other.setId("10");
            other.setName("Прочие");
            if (selectedItems.contains(other)) {
                specifyOtherCircumstancesPrepareContainer.show();
                container.forceLayout();
            } else {
                specifyOtherCircumstancesPrepareContainer.hide();
            }
        });
        nameOfCircumstanceAdditionalCostBuild.addSelectionHandler(selectedItems -> {
            SimpleIdNameModel other = new SimpleIdNameModel();
            other.setId("23");
            other.setName("Прочие");
            if (selectedItems.contains(other)) {
                specifyOtherCircumstancesBuildContainer.show();
                container.forceLayout();
            } else {
                specifyOtherCircumstancesBuildContainer.hide();
            }
        });
        nameOfCircumstanceAdditionalCostExploitation.addSelectionHandler(selectedItems -> {
            SimpleIdNameModel other = new SimpleIdNameModel();
            other.setId("33");
            other.setName("Прочие");
            if (selectedItems.contains(other)) {
                specifyOtherCircumstancesExploitationContainer.show();
                container.forceLayout();
            } else {
                specifyOtherCircumstancesExploitationContainer.hide();
            }
        });
    }

    private void addHandlerToCheckbox(CheckBox checkBox, VerticalLayoutContainer... items) {
        checkBox.addValueChangeHandler(event -> {
            if (event.getValue() != null && event.getValue()) {
                Arrays.asList(items).forEach(VerticalLayoutContainer::show);
                container.forceLayout();
            } else {
                Arrays.asList(items).forEach(VerticalLayoutContainer::hide);
            }
        });
    }

    public void addNameOfCircumstanceAdditionalCosContainerHandler(String implementationLvlId) {
        dueToOnsetOfCertainCircumstancesExist.addValueChangeHandler(event -> {
            if (event.getValue() != null && event.getValue() && implementationLvlId.equals("1")) {
                nameOfCircumstanceAdditionalCosContainer.show();
                container.forceLayout();
            } else {
                nameOfCircumstanceAdditionalCosContainer.hide();
            }
        });

    }
}
