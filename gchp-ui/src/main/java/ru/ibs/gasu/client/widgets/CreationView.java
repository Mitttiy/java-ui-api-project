package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.*;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import ru.ibs.gasu.client.widgets.componens.DateFieldFullDate;
import ru.ibs.gasu.client.widgets.componens.FileUploader;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.view_attr.ViewAttrState;
import ru.ibs.gasu.common.models.view_attr.ViewAttrStateColumns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

/**
 * Раздел "Создание".
 */
public class CreationView implements IsWidget {

    private VerticalLayoutContainer container;
    private ComboBox<SimpleIdNameModel> agreementComplex;
    /**
     * Дата заключения соглашения
     */
    private DateField agreementStartDate;

    /**
     * Дата окончания действия соглашения
     */
    private DateField agreementEndDate;
    private DoubleField agreementValidity;

    /**
     * Срок поставки товара/выполнения работ
     */
    private DateField jobDoneTerm;
    /**
     * Начальный срок достижения экономии
     */
    private DateField savingStartDate;

    /**
     * Конечный срок достижения экономии
     */
    private DateField savingEndDate;

    /**
     * Срок инвестиционной стадии
     */
    private DoubleField investmentStageTerm;


    private FileUploader agreementFileUpload;

    private CheckBox isAutoProlongationProvided;
    private DateField agreementEndDateAfterProlongation;
    private DoubleField agreementValidityAfterProlongation;
    private FileUploader agreementTextFiles;

    private HTML concessionaireHeader;
    private TextField concessionaire;
    private ComboBox<SimpleIdNameModel> opf;
    private TextField concessionaireInn;
    private CheckBox isForeignInvestor;
    private CheckBox isMcpSubject;

    private CheckBox financialClosingProvided;
    private DateField financialClosingDate;
    private DoubleField financialClosingValue;
    private FileUploader financialClosingFileUpload;
    private CheckBox financialClosingIsMutualAgreement;
    /**
     * Дата создания первого объекта соглашения - план
     */
    private DateField firstObjectCreationPlanDate;
    private DateField firstObjectCreationFactDate;
    private FileUploader firstObjectCompleteActFileUpload;

    private CheckBox isRegionPartyAgreement;

    private CheckBox isSeveralObjects;
    /**
     * Плановая дата завершения создания/реконструкции первого объекта соглашения
     */
    private DateField firstSeveralObjectPlanDate;
    private CheckBox isFirstSeveralObject;

    /**
     * Плановая дата завершения создания/реконструкции первого объекта соглашения
     */
    private DateField lastSeveralObjectPlanDate;
    private CheckBox isLastSeveralObject;

    /**
     * Плановая дата завершения создания/реконструкции первого объекта соглашения
     */
    private DateField severalObjectPlanDate;
    private CheckBox isSeveralObjectInOperation;


    private TextField investCostsGrantor;

    private CheckBox isFormulasInvestCosts;

    private FileUploader calcInvestCostsFileUpload;

    private DoubleField actualCostsValue;

    private DoubleField averageInterestRateValue;


    /**
     * Дата создания последнего объекта соглашения - план
     */
    private DateField lastObjectCreationPlanDate;
    private DateField lastObjectCreationFactDate;
    private FileUploader lastObjectCompleteActFileUpload;
    private CreationInvestmentsWidget investments;
    private RemainingDebtWidget balanceOfDebtWidget;
    private TextField expectedRepaymentYear;
    private FileUploader investmentVolumeStagOfCreationFileUpload;
    private VerticalLayoutContainer investmentVolumeStagOfCreationFileUploadContainer;
    private CheckBox isObjectTransferProvided;
    private DateField objectRightsPlanDate;
    private DateField objectRightsFactDate;
    private FileUploader actFileUpload;
    private CheckBox isRenewableBankGuarantee;
    private CheckBox isGuaranteeVariesByYear;
    private BankGuaranteeWidget bankGuaranteeByYears;

    private DoubleField objectValue;
    private FileUploader referenceFileUpload;
    private CheckBox landProvided;
    private CheckBox landIsConcessionaireOwner;
    private DateField landActStartPlanDate;
    private DateField landActStartFactDate;
    private DateField landActEndPlanDate;
    private DateField landActEndFactDate;
    private FileUploader landActTextFileUpload;

    private CheckBox isObligationExecuteOnCreationStage;
    private MethodsOfExecuteObligationsView landMethodOfExecuteObligation;
    private MultiSelectComboBox<SimpleIdNameModel> govSupport;
    private FileUploader confirmationDocFileUpload;

    private ContentPanel cp2;
    private ContentPanel cp4;

    IconButton selectEgrulButton;
    IconButton selectEgripButton;

    public IconButton getSelectEgrulButton() {
        return selectEgrulButton;
    }

    public IconButton getSelectEgripButton() {
        return selectEgripButton;
    }

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public DateField getAgreementStartDate() {
        return agreementStartDate;
    }

    public DateField getAgreementEndDate() {
        return agreementEndDate;
    }

    public DoubleField getAgreementValidity() {
        return agreementValidity;
    }

    public FileUploader getAgreementFileUpload() {
        return agreementFileUpload;
    }

    public TextField getConcessionaire() {
        return concessionaire;
    }

    public TextField getConcessionaireInn() {
        return concessionaireInn;
    }

    public CheckBox getFinancialClosingProvided() {
        return financialClosingProvided;
    }

    public DateField getFinancialClosingDate() {
        return financialClosingDate;
    }

    public DoubleField getFinancialClosingValue() {
        return financialClosingValue;
    }

    public FileUploader getFinancialClosingFileUpload() {
        return financialClosingFileUpload;
    }

    public CheckBox getFinancialClosingIsMutualAgreement() {
        return financialClosingIsMutualAgreement;
    }

    public DateField getFirstObjectCreationPlanDate() {
        return firstObjectCreationPlanDate;
    }

    public DateField getFirstObjectCreationFactDate() {
        return firstObjectCreationFactDate;
    }

    public FileUploader getFirstObjectCompleteActFileUpload() {
        return firstObjectCompleteActFileUpload;
    }

    public CheckBox getIsRegionPartyAgreement() {
        return isRegionPartyAgreement;
    }

    public CheckBox getIsSeveralObjects() {
        return isSeveralObjects;
    }

    public DateField getFirstSeveralObjectPlanDate() {
        return firstSeveralObjectPlanDate;
    }

    public CheckBox getIsFirstSeveralObject() {
        return isFirstSeveralObject;
    }

    public DateField getLastSeveralObjectPlanDate() {
        return lastSeveralObjectPlanDate;
    }

    public CheckBox getIsLastSeveralObject() {
        return isLastSeveralObject;
    }

    public DateField getSeveralObjectPlanDate() {
        return severalObjectPlanDate;
    }

    public CheckBox getIsSeveralObjectInOperation() {
        return isSeveralObjectInOperation;
    }

    public TextField getInvestCostsGrantor() {
        return investCostsGrantor;
    }

    public CheckBox getIsFormulasInvestCosts() {
        return isFormulasInvestCosts;
    }

    public FileUploader getCalcInvestCostsFileUpload() {
        return calcInvestCostsFileUpload;
    }

    public DoubleField getActualCostsValue() {
        return actualCostsValue;
    }

    public DoubleField getAverageInterestRateValue() {
        return averageInterestRateValue;
    }

    public DateField getLastObjectCreationPlanDate() {
        return lastObjectCreationPlanDate;
    }

    public DateField getLastObjectCreationFactDate() {
        return lastObjectCreationFactDate;
    }

    public FileUploader getLastObjectCompleteActFileUpload() {
        return lastObjectCompleteActFileUpload;
    }

    public CreationInvestmentsWidget getInvestments() {
        return investments;
    }

    public RemainingDebtWidget getBalanceOfDebtWidget() {
        return balanceOfDebtWidget;
    }

    public TextField getExpectedRepaymentYear() {
        return expectedRepaymentYear;
    }

    public FileUploader getInvestmentVolumeStagOfCreationFileUpload() {
        return investmentVolumeStagOfCreationFileUpload;
    }

    public CheckBox getIsObjectTransferProvided() {
        return isObjectTransferProvided;
    }

    public DateField getObjectRightsPlanDate() {
        return objectRightsPlanDate;
    }

    public DateField getObjectRightsFactDate() {
        return objectRightsFactDate;
    }

    public FileUploader getActFileUpload() {
        return actFileUpload;
    }

    public CheckBox getIsRenewableBankGuarantee() {
        return isRenewableBankGuarantee;
    }

    public CheckBox getIsGuaranteeVariesByYear() {
        return isGuaranteeVariesByYear;
    }

    public BankGuaranteeWidget getBankGuaranteeByYears() {
        return bankGuaranteeByYears;
    }

    public DoubleField getObjectValue() {
        return objectValue;
    }

    public FileUploader getReferenceFileUpload() {
        return referenceFileUpload;
    }

    public CheckBox getLandProvided() {
        return landProvided;
    }

    public CheckBox getLandIsConcessionaireOwner() {
        return landIsConcessionaireOwner;
    }

    public DateField getLandActStartPlanDate() {
        return landActStartPlanDate;
    }

    public DateField getLandActStartFactDate() {
        return landActStartFactDate;
    }

    public DateField getLandActEndPlanDate() {
        return landActEndPlanDate;
    }

    public DateField getLandActEndFactDate() {
        return landActEndFactDate;
    }

    public FileUploader getLandActTextFileUpload() {
        return landActTextFileUpload;
    }

    public CheckBox getIsObligationExecuteOnCreationStage() {
        return isObligationExecuteOnCreationStage;
    }

    public MethodsOfExecuteObligationsView getLandMethodOfExecuteObligation() {
        return landMethodOfExecuteObligation;
    }

    public MultiSelectComboBox<SimpleIdNameModel> getGovSupport() {
        return govSupport;
    }

    public FileUploader getConfirmationDocFileUpload() {
        return confirmationDocFileUpload;
    }

    public ComboBox<SimpleIdNameModel> getAgreementComplex() {
        return agreementComplex;
    }

    public DateField getJobDoneTerm() {
        return jobDoneTerm;
    }

    public DateField getSavingStartDate() {
        return savingStartDate;
    }

    public DateField getSavingEndDate() {
        return savingEndDate;
    }

    public DoubleField getInvestmentStageTerm() {
        return investmentStageTerm;
    }

    public CheckBox getIsAutoProlongationProvided() {
        return isAutoProlongationProvided;
    }

    public DateField getAgreementEndDateAfterProlongation() {
        return agreementEndDateAfterProlongation;
    }

    public DoubleField getAgreementValidityAfterProlongation() {
        return agreementValidityAfterProlongation;
    }

    public FileUploader getAgreementTextFiles() {
        return agreementTextFiles;
    }

    public ComboBox<SimpleIdNameModel> getOpf() {
        return opf;
    }

    public CheckBox getIsForeignInvestor() {
        return isForeignInvestor;
    }

    public CheckBox getIsMcpSubject() {
        return isMcpSubject;
    }

    public CreationView() {
        initWidget();
    }

    private VerticalLayoutContainer agreementComplexContainer;
    private HorizontalLayoutContainer agreementRow;
    private VerticalLayoutContainer jobDoneTermContainer;
    private VerticalLayoutContainer savingStartDateContainer;
    private VerticalLayoutContainer savingEndDateContainer;
    private VerticalLayoutContainer investmentStageTermContainer;
    private VerticalLayoutContainer agreementEndDateAfterProlongationContainer;
    private VerticalLayoutContainer agreementValidityAfterProlongationContainer;
    private VerticalLayoutContainer firstObjectCreationPlanFactDateFileContainer;
    private VerticalLayoutContainer lastObjectCreationPlanFactDateFileContainer;
    private VerticalLayoutContainer calcInvestCostsFileContainer;
    private VerticalLayoutContainer firstSeveralObjectPlanDateContainer;
    private VerticalLayoutContainer lastSeveralObjectPlanDateContainer;
    private VerticalLayoutContainer severalObjectPlanDateContainer;
    private HorizontalLayoutContainer concessionaireRow;
    private VerticalLayoutContainer opfContainer;
    private VerticalLayoutContainer concessionaireContainer;
    private VerticalLayoutContainer investCostsGrantorContainer;
    private VerticalLayoutContainer financialClosingDateContainer;
    private VerticalLayoutContainer financialClosingValueContainer;
    private VerticalLayoutContainer actualCostsValueContainer;
    private VerticalLayoutContainer averageInterestRateValueContainer;
    private HorizontalLayoutContainer expectedRepaymentYearContainer;
    private VerticalLayoutContainer balanceofDebtContainer;
    private VerticalLayoutContainer objectValueContainer;
    private VerticalLayoutContainer objectRightsPlanFacrDateContainer;
    private VerticalLayoutContainer landActDatesContainer;
    private VerticalLayoutContainer govSupportContainer;
    private VerticalLayoutContainer confirmationDocFileUploadContainer;
    private ContentPanel cp3;
    private ContentPanel cp7;
    private ContentPanel cp6;
    private ContentPanel cp5;
    private ContentPanel cp8;

    public VerticalLayoutContainer getConcessionaireContainer() {
        return concessionaireContainer;
    }

    public VerticalLayoutContainer getInvestmentVolumeStagOfCreationFileUploadContainer() {
        return investmentVolumeStagOfCreationFileUploadContainer;
    }

    public VerticalLayoutContainer getCalcInvestCostsFileContainer() {
        return calcInvestCostsFileContainer;
    }

    public VerticalLayoutContainer getActualCostsValueContainer() {
        return actualCostsValueContainer;
    }

    public HorizontalLayoutContainer getExpectedRepaymentYearContainer() {
        return expectedRepaymentYearContainer;
    }

    public VerticalLayoutContainer getAverageInterestRateValueContainer() {
        return averageInterestRateValueContainer;
    }

    public VerticalLayoutContainer getBalanceofDebtContainer() {
        return balanceofDebtContainer;
    }

    public HTML getConcessionaireHeader() {
        return concessionaireHeader;
    }

    public ContentPanel getCp8() {
        return cp8;
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        agreementComplex = createCommonFilterModelComboBox("Выберите комплекс соглашений");
        FieldLabel agreementComplexLabel = createFieldLabelTop(agreementComplex, "Комплекс соглашений, заключенных в рамках проекта");
        agreementComplexContainer = new VerticalLayoutContainer();
        agreementComplexContainer.add(agreementComplexLabel, STD_VC_LAYOUT);

        agreementStartDate = new DateFieldFullDate();
        FieldLabel agreementStartDateLabel = createFieldLabelTop(agreementStartDate, noWrapString("Дата заключения соглашения"));
        agreementStartDateLabel.setId("agreementStartDate");
        agreementEndDate = new DateFieldFullDate();
        FieldLabel agreementEndDateLabel = createFieldLabelTop(agreementEndDate, new SafeHtmlBuilder().appendHtmlConstant("Дата окончания действия соглашения " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='В случае досрочного прекращения соглашения дата не меняется'></i>").toSafeHtml());

        agreementEndDateLabel.setId("agreementEndDate");
        agreementValidity = new DoubleField();
        FieldLabel agreementValidityLabel = createFieldLabelTop(agreementValidity, noWrapString("Срок действия соглашения, лет"));
        agreementValidityLabel.setId("agreementValidity");
        agreementRow = createThreeFieldWidgetsRow(agreementStartDateLabel, agreementEndDateLabel, agreementValidityLabel);

        agreementStartDate.setEditable(true);
        agreementEndDate.setEditable(true);
        agreementValidity.setEditable(true);

        agreementStartDate.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                Date startDate = event.getValue();
                Date endDate = agreementEndDate.getValue();
                agreementValidity.setValue(
                        calcYearsBetweenDates(endDate, startDate)
                );
            }
        });

        agreementEndDate.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                Date startDate = agreementStartDate.getValue();
                Date endDate = event.getValue();
                agreementValidity.setValue(
                        calcYearsBetweenDates(endDate, startDate)
                );
            }
        });

        jobDoneTerm = new DateFieldFullDate();
        FieldLabel jobDoneTermLabel = createFieldLabelTop(jobDoneTerm, "Срок поставки товара/выполнения работ");
        jobDoneTermContainer = new VerticalLayoutContainer();
        jobDoneTermContainer.add(jobDoneTermLabel, STD_VC_LAYOUT);

        savingStartDate = new DateFieldFullDate();
        FieldLabel savingStartDateLabel = createFieldLabelTop(savingStartDate, "Начальный срок достижения экономии");
        savingStartDateContainer = new VerticalLayoutContainer();
        savingStartDateContainer.add(savingStartDateLabel, STD_VC_LAYOUT);

        savingEndDate = new DateFieldFullDate();
        FieldLabel savingEndDateLabel = createFieldLabelTop(savingEndDate, "Конечный срок достижения экономии");
        savingEndDateContainer = new VerticalLayoutContainer();
        savingEndDateContainer.add(savingEndDateLabel, STD_VC_LAYOUT);

        investmentStageTerm = new DoubleField();
        FieldLabel investmentStageTermLabel = createFieldLabelTop(investmentStageTerm, "Срок инвестиционной стадии");
        investmentStageTermContainer = new VerticalLayoutContainer();
        investmentStageTermContainer.add(investmentStageTermLabel, STD_VC_LAYOUT);

        agreementFileUpload = new FileUploader();
        agreementFileUpload.setHeadingText(new SafeHtmlBuilder().appendHtmlConstant("Текст соглашения " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Прикладывается подписанный экземпляр текста соглашения  со всеми приложениями и реквизитами'></i>").toSafeHtml());

        isAutoProlongationProvided = new CheckBox();
        isAutoProlongationProvided.setBoxLabel(wrapString("Договором предусмотрена автоматическая пролонгация после окончания его срока"));

        agreementEndDateAfterProlongation = new DateFieldFullDate();
        FieldLabel agreementEndDateAfterProlongationLabel = createFieldLabelTop(agreementEndDateAfterProlongation, "Дата окончания действия договора после пролонгации");
        agreementEndDateAfterProlongationContainer = new VerticalLayoutContainer();
        agreementEndDateAfterProlongationContainer.add(agreementEndDateAfterProlongationLabel, STD_VC_LAYOUT);

        agreementValidityAfterProlongation = new DoubleField();
        FieldLabel agreementValidityAfterProlongationLabel = createFieldLabelTop(agreementValidityAfterProlongation, "Срок действия договора после пролонгации, лет");
        agreementValidityAfterProlongationContainer = new VerticalLayoutContainer();
        agreementValidityAfterProlongationContainer.add(agreementValidityAfterProlongationLabel, STD_VC_LAYOUT);

        agreementTextFiles = new FileUploader();
        agreementTextFiles.setHeadingText("Текст договора:");

        concessionaireHeader = createHtml("Концессионер / Частная сторона");
        concessionaire = new TextField();
        concessionaire.setReadOnly(true);
        FieldLabel concessionaireLabel = createFieldLabelTop(concessionaire, "- наименование");
        concessionaireLabel.setId("concessionaire");
        concessionaireInn = new TextField();
        concessionaireInn.setReadOnly(true);
        FieldLabel concessionaireInnLabel = createFieldLabelTop(concessionaireInn, "- ИНН");
        concessionaireInnLabel.setId("concessionaireInn");
        concessionaireRow = createTwoFieldWidgetsRow(concessionaireInnLabel, concessionaireLabel);

        selectEgrulButton = new IconButton("Выбрать юридическое лицо", "fa fa-cog");
        selectEgripButton = new IconButton("Выбрать индивидуального предпринимателя", "fa fa-cog");

        HorizontalLayoutContainer egrulButtonsContainer = new HorizontalLayoutContainer();
        egrulButtonsContainer.add(selectEgrulButton, new HorizontalLayoutContainer.HorizontalLayoutData(0.3, 30, new Margins(15, 20, 35, 0)));
        egrulButtonsContainer.add(new Label("или"), new HorizontalLayoutContainer.HorizontalLayoutData(0.1, 30, new Margins(24, 20, 0, 38)));
        egrulButtonsContainer.add(selectEgripButton, new HorizontalLayoutContainer.HorizontalLayoutData(0.3, 30, new Margins(15, 20, 35, 0)));

        concessionaireContainer = new VerticalLayoutContainer();
        concessionaireHeader = createHtml("creationView");
        concessionaireContainer.add(concessionaireHeader, STD_VC_LAYOUT);
        concessionaireContainer.add(egrulButtonsContainer, HEIGHT60_VC_LAYOUT);
        concessionaireContainer.add(concessionaireRow, HEIGHT60_VC_LAYOUT);

        opf = createCommonFilterModelComboBox("Введите ОПФ");
        FieldLabel opfLabel = createFieldLabelTop(opf, "ОПФ");
        opfContainer = new VerticalLayoutContainer();
        opfContainer.add(opfLabel, STD_VC_LAYOUT);

        isForeignInvestor = new CheckBox();
        isForeignInvestor.setBoxLabel(wrapString("Иностранный инвестор"));
        isMcpSubject = new CheckBox();
        isMcpSubject.setBoxLabel(wrapString("Субъект МСП"));
        financialClosingProvided = new CheckBox();
        financialClosingProvided.setBoxLabel(wrapString("Соглашением предусмотрено финансовое закрытие"));
        financialClosingDate = new DateFieldFullDate();
        FieldLabel financialClosingDateLabel = createFieldLabelTop(financialClosingDate, "Дата акта финансового закрытия");
        financialClosingDateContainer = new VerticalLayoutContainer();
        financialClosingDateContainer.add(financialClosingDateLabel, STD_VC_LAYOUT);
        financialClosingValue = new DoubleField();
        FieldLabel financialClosingValueLabel = createFieldLabelTop(financialClosingValue, "Объем финансирования, тыс. рублей");
        financialClosingValueContainer = new VerticalLayoutContainer();
        financialClosingValueContainer.add(financialClosingValueLabel, STD_VC_LAYOUT);
        financialClosingFileUpload = new FileUploader();
        financialClosingFileUpload.setHeadingText("Акт финансового закрытия: ");
        financialClosingIsMutualAgreement = new CheckBox();
        financialClosingIsMutualAgreement.setBoxLabel(wrapString("Наличие соглашения между концедентом (публичным партнером), концессионером (частным партнером) и финансирующей организацией (прямое соглашение)"));
        firstObjectCreationPlanDate = new DateFieldFullDate();
        FieldLabel firstObjectCreationPlanDateLabel = createFieldLabelTop(firstObjectCreationPlanDate, "- план");
        firstObjectCreationFactDate = new DateFieldFullDate();
        FieldLabel firstObjectCreationFactDateLabel = createFieldLabelTop(firstObjectCreationFactDate, "- факт");
        HorizontalLayoutContainer firstObjectCreationRow = createTwoFieldWidgetsRow(firstObjectCreationPlanDateLabel, firstObjectCreationFactDateLabel);
        firstObjectCompleteActFileUpload = new FileUploader();
        firstObjectCompleteActFileUpload.setHeadingText("Основание (пункт соглашения, акт выполненных работ)");
        firstObjectCreationPlanFactDateFileContainer = new VerticalLayoutContainer();
        firstObjectCreationPlanFactDateFileContainer.add(createHtml("Дата создания первого объекта соглашения"));
        firstObjectCreationPlanFactDateFileContainer.add(firstObjectCreationRow, HEIGHT60_VC_LAYOUT);
        firstObjectCreationPlanFactDateFileContainer.add(firstObjectCompleteActFileUpload, STD_VC_LAYOUT);
//**************
        isRegionPartyAgreement = new CheckBox();
        isRegionPartyAgreement.setBoxLabel(new SafeHtmlBuilder().appendHtmlConstant("Регион является стороной соглашения " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Для проектов в сфере ЖКХ'></i>").toSafeHtml());
//**************

        isSeveralObjects = new CheckBox();
        isSeveralObjects.setBoxLabel(wrapString("Создаётся несколько объектов соглашения"));

        firstSeveralObjectPlanDate = new DateFieldFullDate();
        FieldLabel firstSeveralObjectPlanDateLabel = createFieldLabelTop(firstSeveralObjectPlanDate,
                "Плановая дата завершения создания/реконструкции первого объекта соглашения");
        firstSeveralObjectPlanDateContainer = new VerticalLayoutContainer();
        firstSeveralObjectPlanDateContainer.add(firstSeveralObjectPlanDateLabel, STD_VC_LAYOUT);
//
        isFirstSeveralObject = new CheckBox();
        isFirstSeveralObject.setBoxLabel(wrapString("Первый объект соглашения создан (реконструирован) / введён в эксплуатацию"));

        lastSeveralObjectPlanDate = new DateFieldFullDate();
        FieldLabel lastSeveralObjectPlanDateLabel = createFieldLabelTop(lastSeveralObjectPlanDate, "Плановая дата завершения создания/реконструкции последнего объекта соглашения");
        lastSeveralObjectPlanDateContainer = new VerticalLayoutContainer();
        lastSeveralObjectPlanDateContainer.add(lastSeveralObjectPlanDateLabel, STD_VC_LAYOUT);
//
        isLastSeveralObject = new CheckBox();
        isLastSeveralObject.setBoxLabel(wrapString("Последний объект соглашения создан (реконструирован) / введён в эксплуатацию"));

        severalObjectPlanDate = new DateFieldFullDate();
        FieldLabel severalObjectPlanDateLabel = createFieldLabelTop(severalObjectPlanDate, new SafeHtmlBuilder().appendHtmlConstant("Плановая дата завершения создания/реконструкции объекта соглашения" +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Не допускается указание даты, предшествующей дате заключения соглашения'></i>").toSafeHtml());
        severalObjectPlanDateContainer = new VerticalLayoutContainer();
        severalObjectPlanDateContainer.add(severalObjectPlanDateLabel, STD_VC_LAYOUT);
//
        isSeveralObjectInOperation = new CheckBox();
        isSeveralObjectInOperation.setBoxLabel(wrapString("Объект соглашения (последний из объектов соглашения) создан (реконструирован) / введён в эксплуатацию"));

//************************
        investCostsGrantor = new TextField();
        investCostsGrantor.setName("Пункт соглашения, в котором устанавливаются инвестиционные расходы концедента (публичного партнера)");
        investCostsGrantor.addValidator(new MaxLengthValidator(255));
        FieldLabel investCostsGrantorLabel = createFieldLabelTop(investCostsGrantor, "Пункт соглашения, в котором устанавливаются инвестиционные расходы концедента (публичного партнера)");
        investCostsGrantorContainer = new VerticalLayoutContainer();
        investCostsGrantorContainer.add(investCostsGrantorLabel, STD_VC_LAYOUT);

        isFormulasInvestCosts = new CheckBox();
        isFormulasInvestCosts.setBoxLabel(new SafeHtmlBuilder().appendHtmlConstant("Установлены формулы или порядок индексации размера бюджетных расходов " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Наличие в соглашении условий, предусматривающих индексацию размера бюджетных расходов на этапе создания (например, на размер инфляции)'></i>").toSafeHtml());
        isFormulasInvestCosts.addValueChangeHandler(event -> {
            if (event.getValue() != null && event.getValue()) {
                calcInvestCostsFileContainer.show();
                container.forceLayout();
            } else {
                calcInvestCostsFileContainer.hide();
            }
        });

        calcInvestCostsFileUpload = new FileUploader();
        calcInvestCostsFileUpload.setHeadingText(new SafeHtmlBuilder().appendHtmlConstant("Расчет планового размера бюджетных расходов " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Файл с расчетом планового размера бюджетных расходов на этапе создания с учетом индексации (по годам, если применимо)'></i>").toSafeHtml());

        calcInvestCostsFileContainer = new VerticalLayoutContainer();
        calcInvestCostsFileContainer.add(calcInvestCostsFileUpload, STD_VC_LAYOUT);

        actualCostsValue = new DoubleField();
        FieldLabel actualCostsValueLabel = createFieldLabelTop(actualCostsValue, "Фактические расходы концедента/публичного партнера на предоставление концессионеру/частному партнеру земельных участков, подготовку территории (накопленным итогом с начала срока действия соглашения), тыс. руб.");
        actualCostsValueContainer = new VerticalLayoutContainer();
        actualCostsValueContainer.add(actualCostsValueLabel, STD_VC_LAYOUT);

        averageInterestRateValue = new DoubleField();
        FieldLabel averageInterestRateValueLabel = createFieldLabelTop(averageInterestRateValue, "Средневзвешенная процентная ставка по привлекаемому заемному финансированию");
        averageInterestRateValueContainer = new VerticalLayoutContainer();
        averageInterestRateValueContainer.add(averageInterestRateValueLabel, STD_VC_LAYOUT);

        expectedRepaymentYear = new TextField();
        expectedRepaymentYear.setName("Год, в котором ожидается полное погашение привлеченного заемного финансирования");
        expectedRepaymentYear.addValidator(new MaxLengthValidator(255));
        FieldLabel expectedRepaymentYearLabel = createFieldLabelTop(expectedRepaymentYear, new SafeHtmlBuilder().appendHtmlConstant("Год, в котором ожидается полное погашение привлеченного заемного финансирования " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Плановый год возврата заёмных средств исходя из финансовой модели проекта'></i>").toSafeHtml());
        expectedRepaymentYearContainer = new HorizontalLayoutContainer();
        expectedRepaymentYearContainer.add(expectedRepaymentYearLabel);
        expectedRepaymentYearContainer.setWidth(220);

        balanceOfDebtWidget = new RemainingDebtWidget();
        balanceofDebtContainer = new VerticalLayoutContainer();
        balanceofDebtContainer.add(createHtml("Остаток задолженности по привлеченному заемному финансированию, факт"));
        balanceofDebtContainer.add(balanceOfDebtWidget);

//*********************

        lastObjectCreationPlanDate = new DateFieldFullDate();
        FieldLabel lastObjectCreationPlanDateLabel = createFieldLabelTop(lastObjectCreationPlanDate, "- план");
        lastObjectCreationFactDate = new DateFieldFullDate();
        FieldLabel lastObjectCreationFactDateLabel = createFieldLabelTop(lastObjectCreationFactDate, "- факт");
        HorizontalLayoutContainer lastObjectCreationRow = createTwoFieldWidgetsRow(lastObjectCreationPlanDateLabel, lastObjectCreationFactDateLabel);
        lastObjectCompleteActFileUpload = new FileUploader();
        lastObjectCompleteActFileUpload.setHeadingText("Основание (пункт соглашения, акт выполненных работ)");
        lastObjectCreationPlanFactDateFileContainer = new VerticalLayoutContainer();
        lastObjectCreationPlanFactDateFileContainer.add(createHtml("Дата создания последнего объекта соглашения"));
        lastObjectCreationPlanFactDateFileContainer.add(lastObjectCreationRow, HEIGHT60_VC_LAYOUT);
        lastObjectCreationPlanFactDateFileContainer.add(lastObjectCompleteActFileUpload, STD_VC_LAYOUT);
        investments = new CreationInvestmentsWidget();

        investmentVolumeStagOfCreationFileUpload = new FileUploader();
        investmentVolumeStagOfCreationFileUpload.setHeadingText("Финансовая модель");
        investmentVolumeStagOfCreationFileUploadContainer = new VerticalLayoutContainer();
        investmentVolumeStagOfCreationFileUploadContainer.add(investmentVolumeStagOfCreationFileUpload, STD_VC_LAYOUT);

        isObjectTransferProvided = new CheckBox();
        isObjectTransferProvided.setBoxLabel(wrapString("Концессионным соглашением предусмотрена передача Концессионеру существующего на момент заключения соглашения объекта соглашения"));

        // план теперь не используется
        objectRightsPlanDate = new DateFieldFullDate();
        FieldLabel objectRightsPlanDateLabel = createFieldLabelTop(objectRightsPlanDate, "- план");

        objectRightsFactDate = new DateFieldFullDate();
        FieldLabel objectRightsFactDateLabel = createFieldLabelTop(objectRightsFactDate, "- факт");

        HorizontalLayoutContainer objectRightsRow = createTwoFieldWidgetsRow(objectRightsPlanDateLabel, objectRightsFactDateLabel);
        objectRightsPlanFacrDateContainer = new VerticalLayoutContainer();
        objectRightsPlanFacrDateContainer.add(createHtml("Дата возникновения права владения и пользования объектом у частной стороны"));
        objectRightsPlanFacrDateContainer.add(objectRightsFactDateLabel, STD_VC_LAYOUT);

        actFileUpload = new FileUploader();
        actFileUpload.setHeadingText("Основание (акт приема-передачи)");
        objectValue = new DoubleField();
        FieldLabel objectValueLabel = createFieldLabelTop(objectValue, "Балансовая стоимость передаваемого объекта соглашения, тыс. рублей");
        objectValueContainer = new VerticalLayoutContainer();
        objectValueContainer.add(objectValueLabel, STD_VC_LAYOUT);

        isRenewableBankGuarantee = new CheckBox();
        isRenewableBankGuarantee.setBoxLabel(wrapString("Возобновляемая банковская гарантия"));

        isGuaranteeVariesByYear = new CheckBox();
        isGuaranteeVariesByYear.setBoxLabel(wrapString("Размер банковской гарантии изменяется по годам"));

        bankGuaranteeByYears = new BankGuaranteeWidget();

        referenceFileUpload = new FileUploader();
        referenceFileUpload.setHeadingText("Основание (ссылка на текст соглашения)");

        landProvided = new CheckBox();
        landProvided.setBoxLabel(wrapString("Соглашением предусмотрено предоставление земельного участка"));
        landIsConcessionaireOwner = new CheckBox();
        landIsConcessionaireOwner.setBoxLabel(wrapString("Земельный участок находится в собственности Концедента (Публичного партнера)"));

        // этого больше нет
        landActStartPlanDate = new DateFieldFullDate();
        FieldLabel landActPlanStartDateLabel = createFieldLabelTop(landActStartPlanDate, "плановая дата заключения договора");
        landActStartFactDate = new DateFieldFullDate();
        FieldLabel landActFactStartDateLabel = createFieldLabelTop(landActStartFactDate, "фактическая дата заключения договора");
        HorizontalLayoutContainer landActStartRow = createTwoFieldWidgetsRow(landActPlanStartDateLabel, landActFactStartDateLabel);

        // плана больше нет
        landActEndPlanDate = new DateFieldFullDate();
        FieldLabel landActEndPlanDateLabel = createFieldLabelTop(landActEndPlanDate, "плановый срок действия договора");

        landActEndFactDate = new DateFieldFullDate();
        FieldLabel landActEndFactDateLabel = createFieldLabelTop(landActEndFactDate, "фактический срок действия договора");
        HorizontalLayoutContainer landActEndRow = createTwoFieldWidgetsRow(landActEndPlanDateLabel, landActEndFactDateLabel);

        landActDatesContainer = new VerticalLayoutContainer();
        landActDatesContainer.add(createHtml("Договор о предоставлении земельного участка"));
        landActDatesContainer.add(landActEndFactDateLabel, HEIGHT60_VC_LAYOUT);

        landActTextFileUpload = new FileUploader();
        landActTextFileUpload.setHeadingText("Основание (текст договора аренды земельного участка)");

        isObligationExecuteOnCreationStage = new CheckBox();
        isObligationExecuteOnCreationStage.setBoxLabel(wrapString("Обеспечение исполнения обязательств на этапе создания"));

        landMethodOfExecuteObligation = new MethodsOfExecuteObligationsView();

        govSupport = createCommonFilterModelMultiSelectComboBox("Выберите виды государственной поддержки");
        FieldLabel govSupportLabel = createFieldLabelTop(govSupport, "Государственная поддержка, оказываемая проекту");
        govSupportContainer = new VerticalLayoutContainer();
        govSupportContainer.add(govSupportLabel, STD_VC_LAYOUT);

        confirmationDocFileUploadContainer = new VerticalLayoutContainer();
        confirmationDocFileUpload = new FileUploader();
        confirmationDocFileUpload.setHeadingText("Тексты подтверждающих документов");
        confirmationDocFileUploadContainer.add(confirmationDocFileUpload);

        AccordionLayoutContainer.AccordionLayoutAppearance appearance = GWT.<AccordionLayoutContainer.AccordionLayoutAppearance>create(AccordionLayoutContainer.AccordionLayoutAppearance.class);
        MarginData cpMargin = new MarginData(10, 10, 10, 20);
        ContentPanel cp1 = new ContentPanel(appearance);
        cp1.setAnimCollapse(false);
        cp1.setHeading("Реквизиты и копия соглашения (контракта)");
        VerticalLayoutContainer cp1V = new VerticalLayoutContainer();
//        cp1V.add(agreementComplexContainer, STD_VC_LAYOUT);
        cp1V.add(agreementRow, HEIGHT60_VC_LAYOUT);
//        cp1V.add(jobDoneTermContainer, STD_VC_LAYOUT);
//        cp1V.add(savingStartDateContainer, STD_VC_LAYOUT);
//        cp1V.add(savingEndDateContainer, STD_VC_LAYOUT);
//        cp1V.add(investmentStageTermContainer, STD_VC_LAYOUT);
        cp1V.add(agreementFileUpload, STD_VC_LAYOUT);
//        cp1V.add(isAutoProlongationProvided, STD_VC_LAYOUT);
//        cp1V.add(agreementEndDateAfterProlongationContainer, STD_VC_LAYOUT);
//        cp1V.add(agreementValidityAfterProlongationContainer, STD_VC_LAYOUT);
//        cp1V.add(agreementTextFiles, STD_VC_LAYOUT);
        cp1V.add(concessionaireContainer, STD_VC_LAYOUT);
//        cp1V.add(opfContainer, STD_VC_LAYOUT);
        cp1V.add(isForeignInvestor, STD_VC_LAYOUT);
        cp1V.add(isRegionPartyAgreement, STD_VC_LAYOUT);
//        cp1V.add(isMcpSubject, STD_VC_LAYOUT);
        cp1.add(cp1V, cpMargin);

        cp2 = new ContentPanel(appearance);
        cp2.setAnimCollapse(true);
        cp2.setHeading("Финансовое закрытие");
        VerticalLayoutContainer cp2V = new VerticalLayoutContainer();
//        cp2V.add(financialClosingProvided, STD_VC_LAYOUT);
//        cp2V.add(financialClosingDateContainer, STD_VC_LAYOUT);
//        cp2V.add(financialClosingValueContainer, STD_VC_LAYOUT);
//        cp2V.add(financialClosingFileUpload, STD_VC_LAYOUT);
//        cp2V.add(financialClosingIsMutualAgreement, STD_VC_LAYOUT);

        cp2.add(cp2V, cpMargin);


        cp3 = new ContentPanelWithHint(appearance);
        cp3.setAnimCollapse(false);
        cp3.setHeading(new SafeHtmlBuilder().appendHtmlConstant("Объем инвестиций на стадии создания  " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Финансовые показатели указываются в тысячах рублей.'></i>").toSafeHtml());

        VerticalLayoutContainer cp3V = new VerticalLayoutContainer();
        cp3V.add(investments, STD_VC_LAYOUT);
        cp3V.add(investCostsGrantorContainer, STD_VC_LAYOUT);
        cp3V.add(financialClosingIsMutualAgreement, STD_VC_LAYOUT);
        cp3V.add(investmentVolumeStagOfCreationFileUploadContainer, STD_VC_LAYOUT);
        cp3V.add(isFormulasInvestCosts, STD_VC_LAYOUT);
        cp3V.add(calcInvestCostsFileContainer, STD_VC_LAYOUT);
        cp3V.add(actualCostsValueContainer, STD_VC_LAYOUT);
        cp3V.add(expectedRepaymentYearContainer, HEIGHT60_VC_LAYOUT);
        cp3V.add(averageInterestRateValueContainer, STD_VC_LAYOUT);
        cp3V.add(balanceofDebtContainer, STD_VC_LAYOUT);
        cp3.add(cp3V, cpMargin);
        investments.getTreeGrid().addViewReadyHandler(viewReadyEvent -> {
            investments.getTreeGrid().setExpanded(investments.getTreeStore().getChild(1), true);
            investments.getTreeGrid().setExpanded(investments.getTreeStore().getChild(2), true);
            if (investments.getTreeStore().getAll().stream()
                    .anyMatch(planFactYear -> planFactYear.getPlan() != null && planFactYear.getPlan() != 0
                            || planFactYear.getFact() != null && planFactYear.getFact() != 0)) {
                cp3.expand();
            }
        });

        cp4 = new ContentPanel(appearance);
        cp4.setAnimCollapse(false);
        cp4.setHeading("Передача объекта соглашения");
        VerticalLayoutContainer cp4V = new VerticalLayoutContainer();
//        cp4V.add(isObjectTransferProvided, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(5, 0, 5, 0)));
//        cp4V.add(objectRightsPlanFacrDateContainer, STD_VC_LAYOUT);
//        cp4V.add(actFileUpload, STD_VC_LAYOUT);
//        cp4V.add(objectValueContainer, STD_VC_LAYOUT);
//        cp4V.add(referenceFileUpload);
        cp4.add(cp4V, cpMargin);
        cp4.hide(); // GASU30-1352 - hide

        cp5 = new ContentPanel(appearance);
        cp5.setAnimCollapse(false);
        cp5.setHeading("Права на земельный участок");
        VerticalLayoutContainer cp5V = new VerticalLayoutContainer();
//        cp5V.add(landProvided, STD_VC_LAYOUT);
//        cp5V.add(landIsConcessionaireOwner, STD_VC_LAYOUT);
//        cp5V.add(landActDatesContainer, STD_VC_LAYOUT);
//        cp5V.add(landActTextFileUpload, STD_VC_LAYOUT);
        cp5.add(cp5V, cpMargin);
        cp5.hide(); // GASU30-1352 - hide

        cp6 = new ContentPanel(appearance);
        cp6.setAnimCollapse(false);
        cp6.setHeading("Обеспечение");
        VerticalLayoutContainer cp6V = new VerticalLayoutContainer();
        cp6V.add(isObligationExecuteOnCreationStage, STD_VC_LAYOUT);
        cp6V.add(landMethodOfExecuteObligation, STD_VC_LAYOUT);
        cp6V.add(WidgetUtils.createVerticalGap(10));
//        cp6V.add(isRenewableBankGuarantee, STD_VC_LAYOUT);
//        cp6V.add(isGuaranteeVariesByYear, STD_VC_LAYOUT);
//        cp6V.add(bankGuaranteeByYears, STD_VC_LAYOUT);
        cp6V.add(govSupportContainer, STD_VC_LAYOUT);
        cp6V.add(confirmationDocFileUploadContainer, STD_VC_LAYOUT);
        cp6.add(cp6V, cpMargin);

//        cp7 = new ContentPanel(appearance);
//        cp7.setAnimCollapse(false);
//        cp7.setHeading("Государственная поддержка");
//        VerticalLayoutContainer cp7V = new VerticalLayoutContainer();
//        cp7V.add(govSupportContainer, STD_VC_LAYOUT);
//        cp7V.add(confirmationDocFileUpload, STD_VC_LAYOUT);
//        cp7.add(cp7V, cpMargin);

        cp8 = new ContentPanel(appearance);
        cp8.setAnimCollapse(true);
        cp8.setHeading("Создание/реконструкция объектов соглашения");
        VerticalLayoutContainer cp8V = new VerticalLayoutContainer();
        cp8V.add(isSeveralObjects, STD_VC_LAYOUT);
        cp8V.add(firstSeveralObjectPlanDateContainer, STD_VC_LAYOUT);
        cp8V.add(isFirstSeveralObject, STD_VC_LAYOUT);
        cp8V.add(lastSeveralObjectPlanDateContainer, STD_VC_LAYOUT);
        cp8V.add(isLastSeveralObject, STD_VC_LAYOUT);
        cp8V.add(severalObjectPlanDateContainer, STD_VC_LAYOUT);
        cp8V.add(isSeveralObjectInOperation, STD_VC_LAYOUT);
        cp8.add(cp8V, cpMargin);

        AccordionLayoutContainer accordion = new AccordionLayoutContainer();
        accordion.setExpandMode(AccordionLayoutContainer.ExpandMode.MULTI);
        accordion.add(cp1);
        accordion.add(cp3);
        accordion.add(cp6);
        accordion.add(cp8);

//        accordion.add(cp1);
//        accordion.add(cp2);
//        accordion.add(cp3);
//        accordion.add(cp4);
//        accordion.add(cp5);
//        accordion.add(cp6);
//        //accordion.add(cp7);
//        accordion.add(cp8);
        accordion.setActiveWidget(cp1);
        cp8.collapse();
        cp3.collapse();
        cp6.collapse();


//        cp2.collapse();
//        cp4.collapse();
//        cp5.collapse();
//        cp7.collapse();


        container.add(accordion, STD_VC_LAYOUT);

        landProvided.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    landIsConcessionaireOwner.show();
                    landActDatesContainer.show();
                    landActTextFileUpload.toggle(true);
                    container.forceLayout();

                } else {
                    landIsConcessionaireOwner.hide();
                    landActDatesContainer.hide();
                    landActTextFileUpload.toggle(false);

                    landIsConcessionaireOwner.setValue(null);
                    landActStartPlanDate.setValue(null);
                    landActStartFactDate.setValue(null);
                    landActEndPlanDate.setValue(null);
                    landActEndFactDate.setValue(null);
                    landActTextFileUpload.setFiles(new ArrayList<>());
                }
            }
        });
        landIsConcessionaireOwner.hide();
        landActDatesContainer.hide();
        landActTextFileUpload.toggle(false);

        isObligationExecuteOnCreationStage.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    landMethodOfExecuteObligation.toggle(true);
                    isRenewableBankGuarantee.show();
                    isGuaranteeVariesByYear.show();
                    container.forceLayout();
                } else {
                    landMethodOfExecuteObligation.toggle(false);
                    isRenewableBankGuarantee.hide();
                    isGuaranteeVariesByYear.hide();
                    bankGuaranteeByYears.toggle(false);

                    landMethodOfExecuteObligation.getGrid().getStore().clear();
                    isRenewableBankGuarantee.setValue(null);
                    isGuaranteeVariesByYear.setValue(null);
                    bankGuaranteeByYears.getTreeStore().clear();
                    bankGuaranteeByYears.setUpBaseIndicators();
                }
            }
        });
        landMethodOfExecuteObligation.toggle(false);
        isRenewableBankGuarantee.hide();
        isGuaranteeVariesByYear.hide();
        bankGuaranteeByYears.toggle(false);

        financialClosingProvided.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    financialClosingDateContainer.show();
                    financialClosingValueContainer.show();
                    financialClosingFileUpload.toggle(true);
                } else {
                    financialClosingDateContainer.hide();
                    financialClosingValueContainer.hide();
                    financialClosingFileUpload.toggle(false);
                    financialClosingDate.setValue(null);
                    financialClosingValue.setValue(null);
                    financialClosingFileUpload.setFiles(new ArrayList<>());
                }
            }
        });

        isRenewableBankGuarantee.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (!landMethodOfExecuteObligation.containsMethod(2L)) {
                    isRenewableBankGuarantee.setValue(null);
                }
            }
        });

        isGuaranteeVariesByYear.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    bankGuaranteeByYears.toggle(true);
                    container.forceLayout();
                } else {
                    bankGuaranteeByYears.toggle(false);
                    bankGuaranteeByYears.getTreeStore().clear();
                    bankGuaranteeByYears.setUpBaseIndicators();
                }
            }
        });

        isSeveralObjects.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (isSeveralObjects.getValue()) {
                    firstSeveralObjectPlanDateContainer.show();
                    isFirstSeveralObject.show();
                    lastSeveralObjectPlanDateContainer.show();
                    isLastSeveralObject.show();
                    severalObjectPlanDateContainer.hide();
                    isSeveralObjectInOperation.hide();
                } else {
                    firstSeveralObjectPlanDateContainer.hide();
                    isFirstSeveralObject.hide();
                    isFirstSeveralObject.setValue(null);
                    lastSeveralObjectPlanDateContainer.hide();
                    isLastSeveralObject.hide();
                    severalObjectPlanDateContainer.show();
                    isSeveralObjectInOperation.show();
                }
            }
        });

        // на новом экране сразу скрываем, т.к. галки нет.
        financialClosingDateContainer.hide();
        financialClosingValueContainer.hide();
        financialClosingFileUpload.toggle(false);
        financialClosingDate.setValue(null);
        financialClosingValue.setValue(null);
        financialClosingFileUpload.setFiles(new ArrayList<>());
        bankGuaranteeByYears.toggle(false);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    public ContentPanel getCp3() {
        return cp3;
    }

    /**
     * Обновить форму (скрыть + очистить/показать поля) в зависимости от формы реализации проекта
     * и способа инициации проекта.
     *
     * @param formId - форма реализации проекта
     * @param initId - способ инициации проекта
     */
    public void update(Long formId, Long initId) {
        String formIdStr = (formId == null) ? null : String.valueOf(formId);

        if (viewAttrState.isAttrActive(formIdStr, "agreementComplex")) {
            agreementComplexContainer.show();
        } else {
            agreementComplexContainer.hide();
            agreementComplex.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "jobDoneTerm")) {
            jobDoneTermContainer.show();
        } else {
            jobDoneTermContainer.hide();
            jobDoneTerm.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "savingStartDate")) {
            savingStartDateContainer.show();
        } else {
            savingStartDateContainer.hide();
            savingStartDate.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "savingEndDate")) {
            savingEndDateContainer.show();
        } else {
            savingEndDateContainer.hide();
            savingEndDate.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "investmentStageTerm")) {
            investmentStageTermContainer.show();
        } else {
            investmentStageTermContainer.hide();
            investmentStageTerm.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "agreementFileUpload")) {
            agreementFileUpload.toggle(true);
        } else {
            agreementFileUpload.toggle(false);
            agreementFileUpload.setFiles(new ArrayList<>());
        }

        if (viewAttrState.isAttrActive(formIdStr, "isAutoProlongationProvided")) {
            isAutoProlongationProvided.show();
        } else {
            isAutoProlongationProvided.hide();
            isAutoProlongationProvided.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "agreementEndDateAfterProlongation")) {
            agreementEndDateAfterProlongationContainer.show();
        } else {
            agreementEndDateAfterProlongationContainer.hide();
            agreementEndDateAfterProlongation.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "agreementValidityAfterProlongation")) {
            agreementValidityAfterProlongationContainer.show();
        } else {
            agreementValidityAfterProlongationContainer.hide();
            agreementValidityAfterProlongation.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "agreementTextFiles")) {
            agreementTextFiles.toggle(true);
        } else {
            agreementTextFiles.toggle(false);
            agreementTextFiles.setFiles(new ArrayList<>());
        }

        boolean isConcessionaire = viewAttrState.isAttrActive(formIdStr, "concessionaire");
        if (isConcessionaire) {
            toggleHorizontalLayoutContainerColumn(concessionaireRow, "concessionaire", true);
        } else {
            toggleHorizontalLayoutContainerColumn(concessionaireRow, "concessionaire", false);
            concessionaire.setValue(null);
        }

        boolean isConcessionaireInn = viewAttrState.isAttrActive(formIdStr, "concessionaireInn");
        if (isConcessionaireInn) {
            toggleHorizontalLayoutContainerColumn(concessionaireRow, "concessionaireInn", true);
        } else {
            toggleHorizontalLayoutContainerColumn(concessionaireRow, "concessionaireInn", false);
            concessionaireInn.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "isRegionPartyAgreement")) {
            isRegionPartyAgreement.show();
        } else {
            isRegionPartyAgreement.hide();
            isRegionPartyAgreement.setValue(null);
        }

        if (!isConcessionaire && !isConcessionaireInn) {
            concessionaireContainer.hide();
        } else {
            concessionaireContainer.show();
        }

        if (viewAttrState.isAttrActive(formIdStr, "opf")) {
            opfContainer.show();
        } else {
            opfContainer.hide();
            opf.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "isForeignInvestor")) {
            isForeignInvestor.show();
        } else {
            isForeignInvestor.hide();
            isForeignInvestor.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "isMcpSubject")) {
            isMcpSubject.show();
        } else {
            isMcpSubject.hide();
            isMcpSubject.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "financialClosingProvided")) {
            financialClosingProvided.show();
            cp2.show();
        } else {
            financialClosingProvided.hide();
            financialClosingProvided.setValue(null);
            cp2.hide();
        }

        if (viewAttrState.isAttrActive(formIdStr, "financialClosingDate")) {
            financialClosingDateContainer.show();
            ValueChangeEvent.fire(financialClosingProvided, financialClosingProvided.getValue());
        } else {
            financialClosingDateContainer.hide();
            financialClosingDate.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "financialClosingValue")) {
            financialClosingValueContainer.show();
            ValueChangeEvent.fire(financialClosingProvided, financialClosingProvided.getValue());
        } else {
            financialClosingValueContainer.hide();
            financialClosingValue.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "financialClosingFileUpload")) {
            financialClosingFileUpload.toggle(true);
            ValueChangeEvent.fire(financialClosingProvided, financialClosingProvided.getValue());
        } else {
            financialClosingFileUpload.toggle(false);
            financialClosingFileUpload.setFiles(new ArrayList<>());
        }

        if (viewAttrState.isAttrActive(formIdStr, "financialClosingIsMutualAgreement")) {
            financialClosingIsMutualAgreement.show();
        } else {
            financialClosingIsMutualAgreement.hide();
            financialClosingIsMutualAgreement.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "firstObjectCreationPlanFactDateFile")) {
            firstObjectCreationPlanFactDateFileContainer.show();
        } else {
            firstObjectCreationPlanFactDateFileContainer.hide();
            firstObjectCreationPlanDate.setValue(null);
            firstObjectCreationFactDate.setValue(null);
            firstObjectCompleteActFileUpload.setFiles(new ArrayList<>());
        }

        if (viewAttrState.isAttrActive(formIdStr, "lastObjectCreationPlanFactDateFile")) {
            lastObjectCreationPlanFactDateFileContainer.show();
        } else {
            lastObjectCreationPlanFactDateFileContainer.hide();
            lastObjectCreationPlanDate.setValue(null);
            lastObjectCreationFactDate.setValue(null);
            lastObjectCompleteActFileUpload.setFiles(new ArrayList<>());
        }

//        if (viewAttrState.isAttrActive(formIdStr, "firstObjectCreationPlanFactDateFile") ||
//                viewAttrState.isAttrActive(formIdStr, "lastObjectCreationPlanFactDateFile")) {
//            cp8.show();
//        } else {
//            cp8.hide();
//        }

        if (viewAttrState.isAttrActive(formIdStr, "investments_1")) {
            investments.addRootRowById(1L);
        } else {
            investments.removeRootRowById(1L);
        }

        if (viewAttrState.isAttrActive(formIdStr, "investments_2")) {
            investments.addRootRowById(2L);
            investments.addRootRowById(10L);
            investments.addRootRowById(11L);
        } else {
            investments.removeRootRowById(2L);
            investments.removeRootRowById(10L);
            investments.removeRootRowById(11L);
        }

        if (viewAttrState.isAttrActive(formIdStr, "investments_3")) {
            investments.addRootRowById(3L);
            investments.addRootRowById(7L);
            investments.addRootRowById(8L);
            investments.addRootRowById(9L);
            investments.addRootRowById(12L);
        } else {
            investments.removeRootRowById(3L);
            investments.removeRootRowById(7L);
            investments.removeRootRowById(8L);
            investments.removeRootRowById(9L);
            investments.removeRootRowById(12L);
        }

        if (viewAttrState.isAttrActive(formIdStr, "investCostsGrantor")) {
            investCostsGrantorContainer.show();
        } else {
            investCostsGrantorContainer.hide();
        }

        if (viewAttrState.isAttrActive(formIdStr, "investmentVolumeStagOfCreationFileUpload")) {
            investmentVolumeStagOfCreationFileUploadContainer.show();
        } else {
            investmentVolumeStagOfCreationFileUploadContainer.hide();
        }

        if (viewAttrState.isAttrActive(formIdStr, "isFormulasInvestCosts")) {
            isFormulasInvestCosts.show();
        } else {
            isFormulasInvestCosts.hide();
        }

        if (viewAttrState.isAttrActive(formIdStr, "calcInvestCostsFileUpload")) {
            calcInvestCostsFileContainer.show();
        } else {
            calcInvestCostsFileContainer.hide();
        }

        if (isFormulasInvestCosts.getValue() != null && isFormulasInvestCosts.getValue()) {
            calcInvestCostsFileContainer.show();
        } else {
            calcInvestCostsFileContainer.hide();
        }

//        if (viewAttrState.isAttrActive(formIdStr, "investments_4")) {
//            investments.addRootRowById(4L);
//        } else {
//            investments.removeRootRowById(4L);
//        }

        if (viewAttrState.isAttrActive(formIdStr, "isObjectTransferProvided")) {
            isObjectTransferProvided.show();
        } else {
            isObjectTransferProvided.hide();
            isObjectTransferProvided.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "objectValue")) {
            objectValueContainer.show();
        } else {
            objectValueContainer.hide();
            objectValue.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "referenceFileUpload")) {
            referenceFileUpload.toggle(true);
        } else {
            referenceFileUpload.toggle(false);
            referenceFileUpload.setFiles(new ArrayList<>());
        }

        if (viewAttrState.isAttrActive(formIdStr, "objectRightsPlanFactDate")) {
            objectRightsPlanFacrDateContainer.show();
        } else {
            objectRightsPlanFacrDateContainer.hide();
            objectRightsFactDate.setValue(null);
            objectRightsPlanDate.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "actFileUpload")) {
            actFileUpload.toggle(true);
        } else {
            actFileUpload.toggle(false);
            actFileUpload.setFiles(new ArrayList<>());
        }

        if (viewAttrState.isAttrActive(formIdStr, "landProvided")) {
            cp5.show();
        } else {
            cp5.hide();
            landProvided.setValue(null);
            landIsConcessionaireOwner.setValue(null);
            landActStartPlanDate.setValue(null);
            landActStartFactDate.setValue(null);
            landActEndPlanDate.setValue(null);
            landActEndFactDate.setValue(null);
            landActTextFileUpload.setFiles(new ArrayList<>());
        }

        if (viewAttrState.isAttrActive(formIdStr, "isObligationExecuteOnCreationStage")) {
            isObligationExecuteOnCreationStage.show();
        } else {
            isObligationExecuteOnCreationStage.hide();
            isObligationExecuteOnCreationStage.setValue(null);
            landMethodOfExecuteObligation.getGrid().getStore().clear();
//
//            isRenewableBankGuarantee.setValue(null);
//            isGuaranteeVariesByYear.setValue(null);
//            bankGuaranteeByYears.getTreeStore().clear();
//            bankGuaranteeByYears.setUpBaseIndicators();
        }

        if (viewAttrState.isAttrActive(formIdStr, "govSupport")) {
            govSupportContainer.show();
        } else {
            govSupportContainer.hide();
            govSupport.deselectAll();
        }

        if (viewAttrState.isAttrActive(formIdStr, "confirmationDocFileUpload")) {
            confirmationDocFileUploadContainer.show();
        } else {
            govSupportContainer.hide();
            confirmationDocFileUploadContainer.hide();
            confirmationDocFileUpload.setFiles(new ArrayList<>());
        }

        if (viewAttrState.isAttrActive(formIdStr, "isObjectTransferProvided") ||
                viewAttrState.isAttrActive(formIdStr, "objectRightsPlanFactDate") ||
                viewAttrState.isAttrActive(formIdStr, "actFileUpload") ||
                viewAttrState.isAttrActive(formIdStr, "objectValue") ||
                viewAttrState.isAttrActive(formIdStr, "referenceFileUpload")) {
            cp4.show();
        } else {
            cp4.hide();
        }
        if (viewAttrState.isAttrActive(formIdStr, "isFirstSeveralObject")) {
            isFirstSeveralObject.show();
        } else {
            isFirstSeveralObject.hide();
            isFirstSeveralObject.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "isSeveralObjects")) {
            isSeveralObjects.show();
        } else {
            isSeveralObjects.hide();
            isSeveralObjects.setValue(null);
        }

        if (isSeveralObjects.getValue()) {
            firstSeveralObjectPlanDateContainer.show();
            isFirstSeveralObject.show();
            lastSeveralObjectPlanDateContainer.show();
            isLastSeveralObject.show();
            severalObjectPlanDateContainer.hide();
            isSeveralObjectInOperation.hide();
        } else {
            firstSeveralObjectPlanDateContainer.hide();
            isFirstSeveralObject.hide();
            isFirstSeveralObject.setValue(null);
            lastSeveralObjectPlanDateContainer.hide();
            isLastSeveralObject.hide();
            severalObjectPlanDateContainer.show();
            isSeveralObjectInOperation.show();
        }

        container.forceLayout();
    }


    private static ViewAttrState viewAttrState;

    static {
        viewAttrState = new ViewAttrState();

        viewAttrState.getColumns().addAll(ViewAttrStateColumns.getFormIds());

        viewAttrState.getState().put("agreementComplex", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0)); // 5.1
        viewAttrState.getState().put("agreementStartDate", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.2 // TODO - can be missed, always show
        viewAttrState.getState().put("agreementEndDate", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.3 // TODO - can be missed, always show
        viewAttrState.getState().put("agreementValidity", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.4 // TODO - can be missed, always show
        viewAttrState.getState().put("jobDoneTerm", Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.5
        viewAttrState.getState().put("savingStartDate", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0)); // 5.6
        viewAttrState.getState().put("savingEndDate", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0)); // 5.7
        viewAttrState.getState().put("investmentStageTerm", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0)); // 5.8
        viewAttrState.getState().put("agreementFileUpload", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.9 // TODO - can be missed, always show
        viewAttrState.getState().put("isAutoProlongationProvided", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0)); // 5.10
        viewAttrState.getState().put("agreementEndDateAfterProlongation", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0)); // 5.11
        viewAttrState.getState().put("agreementValidityAfterProlongation", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0)); // 5.12
        viewAttrState.getState().put("agreementTextFiles", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0)); // 5.13
        viewAttrState.getState().put("concessionaire", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.16
        viewAttrState.getState().put("opf", Arrays.asList(1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0)); // 5.16.1
        viewAttrState.getState().put("concessionaireInn", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.17
        viewAttrState.getState().put("isForeignInvestor", Arrays.asList(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.18
        viewAttrState.getState().put("isMcpSubject", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0)); // 5.19

        viewAttrState.getState().put("isRegionPartyAgreement", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.19.1

        viewAttrState.getState().put("financialClosingProvided", Arrays.asList(1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.20
        viewAttrState.getState().put("financialClosingDate", Arrays.asList(1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.21
        viewAttrState.getState().put("financialClosingValue", Arrays.asList(1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.22
        viewAttrState.getState().put("financialClosingFileUpload", Arrays.asList(1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.23
        viewAttrState.getState().put("financialClosingIsMutualAgreement", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.24
        viewAttrState.getState().put("firstObjectCreationPlanFactDateFile", Arrays.asList(1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1)); // 5.25
        viewAttrState.getState().put("lastObjectCreationPlanFactDateFile", Arrays.asList(1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1)); // 5.29

        viewAttrState.getState().put("isSeveralObjects", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.32.1

        viewAttrState.getState().put("firstSeveralObjectPlanDate", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.32.2
        viewAttrState.getState().put("isFirstSeveralObject", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.32.3

        viewAttrState.getState().put("lastSeveralObjectPlanDate", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.32.4
        viewAttrState.getState().put("isLastSeveralObject", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.32.5

        viewAttrState.getState().put("severalObjectPlanDate", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.32.6
        viewAttrState.getState().put("isSeveralObjectInOperation", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.32.7

        viewAttrState.getState().put("investments_1", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.33
        viewAttrState.getState().put("investments_2", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.34
        viewAttrState.getState().put("investments_3", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.35
        //viewAttrState.getState().put("investments_4", Arrays.asList(0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0)); // ???5.36


        viewAttrState.getState().put("investCostsGrantor", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.36.0
        viewAttrState.getState().put("investmentVolumeStagOfCreationFileUpload", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.36.1
        viewAttrState.getState().put("isFormulasInvestCosts", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.36.2
        viewAttrState.getState().put("calcInvestCostsFileUpload", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.36.3
        viewAttrState.getState().put("actualCostsValue", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.36.4
        viewAttrState.getState().put("expectedRepaymentYear", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.36.5
        viewAttrState.getState().put("averageInterestRateValue", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.36.6
        viewAttrState.getState().put("balanceOfDebtWidget", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.36.7


        // GASU30-1352 - hide
//        viewAttrState.getState().put("isObjectTransferProvided",              Arrays.asList(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.37  // Передача объекта соглашения
//        viewAttrState.getState().put("objectValue",                           Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.38
//        viewAttrState.getState().put("referenceFileUpload",                   Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.39
//        viewAttrState.getState().put("objectRightsPlanFactDate",              Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.40
//        viewAttrState.getState().put("actFileUpload",                         Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.41
        viewAttrState.getState().put("isObjectTransferProvided", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.37  // Передача объекта соглашения
        viewAttrState.getState().put("objectValue", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.38
        viewAttrState.getState().put("referenceFileUpload", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.39
        viewAttrState.getState().put("objectRightsPlanFactDate", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.40
        viewAttrState.getState().put("actFileUpload", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.41

        // GASU30-1352 - hide
        // viewAttrState.getState().put("landProvided",                          Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.44 // Права на земельный участок (раздел)
        viewAttrState.getState().put("landProvided", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.44 // Права на земельный участок (раздел)

        viewAttrState.getState().put("landIsConcessionaireOwner", Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.45
        viewAttrState.getState().put("landActDates", Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.47
        viewAttrState.getState().put("landActTextFileUpload", Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.52

        viewAttrState.getState().put("isObligationExecuteOnCreationStage", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.53 // Обеспечение (раздел)
        viewAttrState.getState().put("landMethodOfExecuteObligation", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // 5.54

        viewAttrState.getState().put("govSupport", Arrays.asList(0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.57 // Государственная поддержка (раздел)
        viewAttrState.getState().put("confirmationDocFileUpload", Arrays.asList(0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // 5.58
    }

    public void setConcessionaireHeader(String header) {
        getConcessionaireContainer().remove(0);
        getConcessionaireContainer().insert(createHtml(header), 0, STD_VC_LAYOUT);
    }

    public void setDisplayedByLvlFederal(boolean isFederal) {
        if (isFederal) {
            actualCostsValueContainer.show();
            expectedRepaymentYearContainer.show();
            averageInterestRateValueContainer.show();
            balanceofDebtContainer.show();
            container.forceLayout();
        } else {
            actualCostsValueContainer.hide();
            expectedRepaymentYearContainer.hide();
            averageInterestRateValueContainer.hide();
            balanceofDebtContainer.hide();
        }
    }
}
