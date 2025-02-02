package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.*;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import ru.ibs.gasu.client.widgets.componens.DateFieldFullDate;
import ru.ibs.gasu.client.widgets.componens.FileUploader;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.client.widgets.componens.TwinTriggerComboBox;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.view_attr.ViewAttrState;
import ru.ibs.gasu.common.models.view_attr.ViewAttrStateColumns;
import ru.ibs.gasu.common.soap.generated.gchpdicts.SimpleEgrulDomain;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sencha.gxt.cell.core.client.form.TextAreaInputCell.Resizable.VERTICAL;
import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

/**
 * Раздел "Подготовка проекта".
 */
public class ProjectPreparationView implements IsWidget {

    private VerticalLayoutContainer container;
    private ContentPanel cp2;
    private ContentPanel cp3;
    private ContentPanel cp4;
    private ContentPanel cp5;
    private ContentPanel cp6;

    public ContentPanel getCp7() {
        return cp7;
    }

    private ContentPanel cp7;
    private ContentPanel cp8;

    private VerticalLayoutContainer cp1V;
    private VerticalLayoutContainer cp7V;
    private VerticalLayoutContainer competitionBidDateContainer;
    private VerticalLayoutContainer competitionTenderDateContainer;
    private VerticalLayoutContainer competitionResultDateContainer;

    private CheckBox isObjInListWithConcessionalAgreements;

    private TextField objectsListUrl;
    private VerticalLayoutContainer objectsListUrlContainer;

    private VerticalLayoutContainer supposedPrivatePartnerSelectButtonContainer;

    private TextField supposedPrivatePartnerName;
    private VerticalLayoutContainer supposedPrivatePartnerNameContainer;

    private TextField supposedPrivatePartnerInn;
    private VerticalLayoutContainer supposedPrivatePartnerInnContainer;

    private CheckBox isForeignInvestor;
    private CheckBox isMcpSubject;

    private ComboBox<SimpleIdNameModel> agreementsSet;
    private VerticalLayoutContainer agreementsSetContainer;

    private DateField supposedAgreementStartDate;
    private VerticalLayoutContainer supposedAgreementStartDateContainer;

    private DateField supposedAgreementEndDate;
    private VerticalLayoutContainer supposedAgreementEndDateContainer;

    private DateField deliveryTimeOfGoodsWorkDate;
    private VerticalLayoutContainer deliveryTimeOfGoodsWorkDateContainer;

    private DoubleField supposedValidityYears;
    private VerticalLayoutContainer supposedValidityYearsContainer;

    private FileUploader projectAgreementFileUploader;

    private TwinTriggerComboBox<SimpleIdNameModel> groundsOfAgreementConclusion;
    private VerticalLayoutContainer groundsOfAgreementConclusionContainer;

    private TextField actNumber;
    private VerticalLayoutContainer actNumberContainer;

    private DateField actDate;
    private VerticalLayoutContainer actDateContainer;

    private FileUploader actFileUploader;
    private FileUploader leaseAgreementUploader;
    private CheckBox isProjectAssignedStatus;

    private TextField decisionNumber;
    private VerticalLayoutContainer decisionNumberContainer;

    private DateField decisionDate;
    private VerticalLayoutContainer decisionDateContainer;

    private FileUploader decisionFileUploader;

    private VerticalLayoutContainer proposalContainer;
    private VerticalLayoutContainer agreementSigned;

    private DateField proposalPublishDate;
    private VerticalLayoutContainer proposalPublishDateContainer;

    private FileUploader proposalTextFileUploader;

    private TextField torgiGovRuUrl;
    private VerticalLayoutContainer torgiGovRuUrlContainer;

    private FileUploader protocolFileUploader;
    private CheckBox isReadinessRequestReceived;
    private CheckBox isDecisionMadeToConcludeAnAgreement;

    private TextField conclustionActNumber;
    private VerticalLayoutContainer conclustionActNumberContainer;

    private DateField conclusionActDate;
    private VerticalLayoutContainer conclusionActDateContainer;

    private FileUploader conclusionFileUploader;

    private DateField investmentStageDurationDate;
    private VerticalLayoutContainer investmentStageDurationDateContainer;

    private CheckBox isAgreementSigned;
    private CheckBox jointCompetition;

    private TextArea anotherProjectsInfo;
    private TextField anotherProjectsIdentifier;

    private DateField competitionBidCollEndPlanDate;
    private DateField competitionBidCollEndFactDate;
    private DateField competitionTenderOfferEndPlanDate;
    private DateField competitionTenderOfferEndFactDate;
    private DateField competitionResultsPlanDate;
    private DateField competitionResultsFactDate;
    private FileUploader competitionTextFileUploader;
    private CheckBox competitionIsElAuction;
    private CheckBox competitionHasResults;

    private ComboBox<SimpleIdNameModel> competitionResults;

    private TextField competitionResultsProtocolNum;
    private DateField competitionResultsProtocolDate;
    private IntegerField competitionResultsParticipantsNum;
//    private HorizontalLayoutContainer competitionResultsHRow;

    private FileUploader competitionResultsProtocolFileUploader;
    private FileUploader competitionResultsDocFileUploader;

    private ComboBox<SimpleIdNameModel> competitionResultsSignStatus;
    private VerticalLayoutContainer competitionResultsSignStatusContainer;

    private ComboBox<SimpleIdNameModel> contractPriceOrder;

    private TextField contractPriceFormula;
    private VerticalLayoutContainer contractPriceFormulaContainer;

    private DoubleField contractPricePrice;
    private VerticalLayoutContainer contractPricePriceContainer;

    private CheckBox ndsCheck;
    private ComboBox<SimpleIdNameModel> measureType;
    private DateField dateField;

    private ComboBox<SimpleIdNameModel> contractPriceOffer;
    private DoubleField contractPriceOfferValue;
    HorizontalLayoutContainer contractPriceOfferRow;

    private ComboBox<SimpleIdNameModel> winnerContractPriceOffer;
    private VerticalLayoutContainer winnerContractPriceOfferContainer;

    private DateField contractPriceSavingStartDate;
    private DateField contractPriceSavingEndDate;
    private HorizontalLayoutContainer contractPriceSavingRow;
    private VerticalLayoutContainer contractPriceSavingContainer;

    private InvestmentsWidget creationInvestments;
    private InvestmentsWidget investments;

    private FileUploader financialModelFileUpload;

    private CheckBox ppImplementProject;
    private TextField ppConcludeAgreementLink;
    private ComboBox<SimpleIdNameModel> ppResultsOfPlacing;
    private FieldLabel ppResultsOfPlacingLabel;
    private FieldLabel actNumberLabel;
    private FieldLabel actDateLabel;
    private FieldLabel ppConcludeAgreementLinkLabel;

    /**
     * Способ возмещения обязательств частного партнера
     */
    private ComboBox<SimpleIdNameModel> publicPartnerCostRecoveryMethod;
    private VerticalLayoutContainer publicPartnerCostRecoveryMethodContainer;

    private DoubleField advancePaymentAmount;
    private VerticalLayoutContainer advancePaymentAmountContainer;

    private DateField firstObjectOperationDate;
    private VerticalLayoutContainer firstObjectOperationDateContainer;

    private DateField lastObjectCommissioningDate;
    private VerticalLayoutContainer lastObjectCommissioningDateContainer;

    private FileUploader supportingDocumentsFileUploader;

    private DoubleField budgetExpendituresAgreementOnGchpMchp;
    private DoubleField budgetExpendituresGovContract;
    private VerticalLayoutContainer budgetExpendituresContainer;

    private DoubleField obligationsInCaseOfRisksAgreementOnGchpMchp;
    private DoubleField obligationsInCaseOfRisksGovContract;
    private VerticalLayoutContainer isObligationsInCaseOfRisksContainer;

    private DoubleField indicatorAssessmentComparativeAdvantage;
    private VerticalLayoutContainer indicatorAssessmentComparativeAdvantageContainer;

    private FileUploader conclusionUOTextFileUploader;
    private FileUploader financialModelTextFileUploader;

    private ComboBox<SimpleIdNameModel> methodOfExecuteObligation;
    private VerticalLayoutContainer methodOfExecuteObligationContainer;

    private TextField linkToClauseAgreement;
    private VerticalLayoutContainer linkToClauseAgreementContainer;

    private CheckBox isPrivateLiabilityProvided;

    private ComboBox<SimpleIdNameModel> otherGovSupports;
    private VerticalLayoutContainer otherGovSupportsContainer;

    private TextField linkToClauseAgreementLiabilityProvided;
    private VerticalLayoutContainer linkToClauseAgreementLiabilityProvidedContainer;

    private HorizontalLayoutContainer TreeHRow;

    public ProjectPreparationView() {
        initWidget();
    }

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public CheckBox getIsObjInListWithConcessionalAgreements() {
        return isObjInListWithConcessionalAgreements;
    }

    public TextField getObjectsListUrl() {
        return objectsListUrl;
    }

    public TextField getSupposedPrivatePartnerName() {
        return supposedPrivatePartnerName;
    }

    public TextField getSupposedPrivatePartnerInn() {
        return supposedPrivatePartnerInn;
    }

    public CheckBox getIsForeignInvestor() {
        return isForeignInvestor;
    }

    public CheckBox getIsMcpSubject() {
        return isMcpSubject;
    }

    public ComboBox<SimpleIdNameModel> getAgreementsSet() {
        return agreementsSet;
    }

    public DateField getSupposedAgreementStartDate() {
        return supposedAgreementStartDate;
    }

    public DateField getSupposedAgreementEndDate() {
        return supposedAgreementEndDate;
    }

    public DateField getDeliveryTimeOfGoodsWorkDate() {
        return deliveryTimeOfGoodsWorkDate;
    }

    public DoubleField getSupposedValidityYears() {
        return supposedValidityYears;
    }

    public FileUploader getProjectAgreementFileUploader() {
        return projectAgreementFileUploader;
    }

    public TwinTriggerComboBox<SimpleIdNameModel> getGroundsOfAgreementConclusion() {
        return groundsOfAgreementConclusion;
    }

    public TextField getActNumber() {
        return actNumber;
    }

    public DateField getActDate() {
        return actDate;
    }

    public FileUploader getActFileUploader() {
        return actFileUploader;
    }

    public FileUploader getLeaseAgreementUploader() {
        return leaseAgreementUploader;
    }

    public CheckBox getIsProjectAssignedStatus() {
        return isProjectAssignedStatus;
    }

    public TextField getDecisionNumber() {
        return decisionNumber;
    }

    public DateField getDecisionDate() {
        return decisionDate;
    }

    public FileUploader getDecisionFileUploader() {
        return decisionFileUploader;
    }

    public DateField getProposalPublishDate() {
        return proposalPublishDate;
    }

    public FileUploader getProposalTextFileUploader() {
        return proposalTextFileUploader;
    }

    public TextField getTorgiGovRuUrl() {
        return torgiGovRuUrl;
    }

    public FileUploader getProtocolFileUploader() {
        return protocolFileUploader;
    }

    public CheckBox getIsReadinessRequestReceived() {
        return isReadinessRequestReceived;
    }

    public CheckBox getIsDecisionMadeToConcludeAnAgreement() {
        return isDecisionMadeToConcludeAnAgreement;
    }

    public TextField getConclustionActNumber() {
        return conclustionActNumber;
    }

    public DateField getConclusionActDate() {
        return conclusionActDate;
    }

    public FileUploader getConclusionFileUploader() {
        return conclusionFileUploader;
    }

    public DateField getInvestmentStageDurationDate() {
        return investmentStageDurationDate;
    }

    public CheckBox getIsAgreementSigned() {
        return isAgreementSigned;
    }

    public void setIsAgreementSigned(boolean isAgreementSigned) {
        this.isAgreementSigned.setValue(isAgreementSigned);
    }

    public CheckBox getJointCompetition() {
        return jointCompetition;
    }

    public TextArea getAnotherProjectsInfo() {
        return anotherProjectsInfo;
    }

    public TextField getAnotherProjectsIdentifier() {
        return anotherProjectsIdentifier;
    }

    public DateField getCompetitionBidCollEndPlanDate() {
        return competitionBidCollEndPlanDate;
    }

    public DateField getCompetitionBidCollEndFactDate() {
        return competitionBidCollEndFactDate;
    }

    public DateField getCompetitionTenderOfferEndPlanDate() {
        return competitionTenderOfferEndPlanDate;
    }

    public DateField getCompetitionTenderOfferEndFactDate() {
        return competitionTenderOfferEndFactDate;
    }

    public DateField getCompetitionResultsPlanDate() {
        return competitionResultsPlanDate;
    }

    public DateField getCompetitionResultsFactDate() {
        return competitionResultsFactDate;
    }

    public FileUploader getCompetitionTextFileUploader() {
        return competitionTextFileUploader;
    }

    public CheckBox getCompetitionIsElAuction() {
        return competitionIsElAuction;
    }

    public CheckBox getCompetitionHasResults() {
        return competitionHasResults;
    }

    public ComboBox<SimpleIdNameModel> getCompetitionResults() {
        return competitionResults;
    }

    public TextField getCompetitionResultsProtocolNum() {
        return competitionResultsProtocolNum;
    }

    public DateField getCompetitionResultsProtocolDate() {
        return competitionResultsProtocolDate;
    }

    public IntegerField getCompetitionResultsParticipantsNum() {
        return competitionResultsParticipantsNum;
    }

    public FileUploader getCompetitionResultsProtocolFileUploader() {
        return competitionResultsProtocolFileUploader;
    }

    public FileUploader getCompetitionResultsDocFileUploader() {
        return competitionResultsDocFileUploader;
    }

    public ComboBox<SimpleIdNameModel> getCompetitionResultsSignStatus() {
        return competitionResultsSignStatus;
    }

    public ComboBox<SimpleIdNameModel> getContractPriceOrder() {
        return contractPriceOrder;
    }

    public TextField getContractPriceFormula() {
        return contractPriceFormula;
    }

    public DoubleField getContractPricePrice() {
        return contractPricePrice;
    }

    public CheckBox getNdsCheck() {
        return ndsCheck;
    }

    public ComboBox<SimpleIdNameModel> getMeasureType() {
        return measureType;
    }

    public DateField getDateField() {
        return dateField;
    }

    public ComboBox<SimpleIdNameModel> getContractPriceOffer() {
        return contractPriceOffer;
    }

    public DoubleField getContractPriceOfferValue() {
        return contractPriceOfferValue;
    }

    public ComboBox<SimpleIdNameModel> getOtherGovSupports() {
        return otherGovSupports;
    }

    public DateField getContractPriceSavingStartDate() {
        return contractPriceSavingStartDate;
    }

    public DateField getContractPriceSavingEndDate() {
        return contractPriceSavingEndDate;
    }

    public InvestmentsWidget getInvestments() {
        return investments;
    }

    public FileUploader getFinancialModelFileUpload() {
        return financialModelFileUpload;
    }

    public ComboBox<SimpleIdNameModel> getPublicPartnerCostRecoveryMethod() {
        return publicPartnerCostRecoveryMethod;
    }

    public DoubleField getAdvancePaymentAmount() {
        return advancePaymentAmount;
    }

    public DateField getFirstObjectOperationDate() {
        return firstObjectOperationDate;
    }

    public DateField getLastObjectCommissioningDate() {
        return lastObjectCommissioningDate;
    }

    public FileUploader getSupportingDocumentsFileUploader() {
        return supportingDocumentsFileUploader;
    }

    public InvestmentsWidget getCreationInvestments() {
        return creationInvestments;
    }

    public DoubleField getBudgetExpendituresAgreementOnGchpMchp() {
        return budgetExpendituresAgreementOnGchpMchp;
    }

    public DoubleField getBudgetExpendituresGovContract() {
        return budgetExpendituresGovContract;
    }

    public DoubleField getObligationsInCaseOfRisksAgreementOnGchpMchp() {
        return obligationsInCaseOfRisksAgreementOnGchpMchp;
    }

    public DoubleField getObligationsInCaseOfRisksGovContract() {
        return obligationsInCaseOfRisksGovContract;
    }

    public DoubleField getIndicatorAssessmentComparativeAdvantage() {
        return indicatorAssessmentComparativeAdvantage;
    }

    public FileUploader getConclusionUOTextFileUploader() {
        return conclusionUOTextFileUploader;
    }

    public FileUploader getFinancialModelTextFileUploader() {
        return financialModelTextFileUploader;
    }

    public ComboBox<SimpleIdNameModel> getMethodOfExecuteObligation() {
        return methodOfExecuteObligation;
    }

    public TextField getLinkToClauseAgreement() {
        return linkToClauseAgreement;
    }

    public CheckBox getIsPrivateLiabilityProvided() {
        return isPrivateLiabilityProvided;
    }

    public TextField getLinkToClauseAgreementLiabilityProvided() {
        return linkToClauseAgreementLiabilityProvided;
    }

    public ComboBox<SimpleIdNameModel> getWinnerContractPriceOffer() {
        return winnerContractPriceOffer;
    }

    public ComboBox<SimpleIdNameModel> getPpResultsOfPlacing() {
        return ppResultsOfPlacing;
    }

    public TextField getPpConcludeAgreementLink() {
        return ppConcludeAgreementLink;
    }

    public CheckBox getPpImplementProject() {
        return ppImplementProject;
    }

    public ContentPanel getCp2() {
        return cp2;
    }

    private SelectEvent.SelectHandler createEgrulButtonsEvent(boolean isEgrip) {
        return event -> {
            new EgrulGridWindow() {
                @Override
                protected boolean isEgrip() {
                    return isEgrip;
                }

                @Override
                protected void onSelect(SimpleEgrulDomain egrulDomain) {
                    supposedPrivatePartnerName.setValue(egrulDomain.getShortName());
                    supposedPrivatePartnerInn.setValue(egrulDomain.getInn());
                }
            }.show();
        };
    }

    public ContentPanel getCp3() {
        return cp3;
    }

    public ContentPanel getCp4() {
        return cp4;
    }

    public ContentPanel getCp6() {
        return cp6;
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();

        isObjInListWithConcessionalAgreements = new CheckBox();

        isObjInListWithConcessionalAgreements.setBoxLabel(WidgetUtils.wrapString("Объект соглашения  включен в перечень объектов, " +
                "в отношении которых планируется заключение концессионных соглашений"));

        isObjInListWithConcessionalAgreements.setValue(false);
        objectsListUrl = new TextField();

        FieldLabel objectsListUrlLabel = createFieldLabelTop(objectsListUrl, "Ссылка на перечень объектов");
        objectsListUrlContainer = new VerticalLayoutContainer();
        objectsListUrlContainer.add(objectsListUrlLabel, STD_VC_LAYOUT);


        supposedPrivatePartnerName = new TextField();
        supposedPrivatePartnerName.setName("Наименование юридического лица, выступившего с предложением о заключении соглашения");
        supposedPrivatePartnerName.addValidator(new MaxLengthValidator(255));
        FieldLabel supposedPublicPartnerNameLabel = createFieldLabelTop(supposedPrivatePartnerName, "Наименование юридического лица, выступившего с предложением о заключении соглашения");
        supposedPrivatePartnerNameContainer = new VerticalLayoutContainer();
        supposedPrivatePartnerNameContainer.add(supposedPublicPartnerNameLabel, STD_VC_LAYOUT);

        supposedPrivatePartnerInn = new TextField();
        supposedPrivatePartnerInn.setName("ИНН");
        supposedPrivatePartnerInn.addValidator(new MaxLengthValidator(255));
        FieldLabel supposedPublicPartnerInnLabel = createFieldLabelTop(supposedPrivatePartnerInn, "ИНН");
        supposedPrivatePartnerInnContainer = new VerticalLayoutContainer();
        supposedPrivatePartnerInnContainer.add(supposedPublicPartnerInnLabel, STD_VC_LAYOUT);

        IconButton selectEgrulButton = new IconButton("Выбрать юридическое лицо", "fa fa-cog");
        IconButton selectEgripButton = new IconButton("Выбрать индивидуального предпринимателя", "fa fa-cog");

        HorizontalLayoutContainer egrulButtonsContainer = new HorizontalLayoutContainer();
        egrulButtonsContainer.add(selectEgrulButton, new HorizontalLayoutContainer.HorizontalLayoutData(0.3, 30, new Margins(15, 20, 35, 0)));
        egrulButtonsContainer.add(new Label("или"), new HorizontalLayoutContainer.HorizontalLayoutData(0.1, 30, new Margins(24, 20, 0, 38)));
        egrulButtonsContainer.add(selectEgripButton, new HorizontalLayoutContainer.HorizontalLayoutData(0.3, 30, new Margins(15, 20, 35, 0)));

        selectEgrulButton.addSelectHandler(createEgrulButtonsEvent(false));
        selectEgripButton.addSelectHandler(createEgrulButtonsEvent(true));

        supposedPrivatePartnerSelectButtonContainer = new VerticalLayoutContainer();
        supposedPrivatePartnerSelectButtonContainer.add(egrulButtonsContainer, new VerticalLayoutContainer.VerticalLayoutData(-1, 60));

        isForeignInvestor = new CheckBox();
        isForeignInvestor.setBoxLabel(wrapString("Иностранный инвестор"));

        isMcpSubject = new CheckBox();
        isMcpSubject.setBoxLabel("Субъект МСП");

        agreementsSet = createCommonFilterModelComboBox("Выберите комплекс соглашений, заключаемых в рамках проекта");
        FieldLabel agreementsSetLabel = createFieldLabelTop(agreementsSet, "Комплекс соглашений, заключаемых в рамках проекта");
        agreementsSetContainer = new VerticalLayoutContainer();
        agreementsSetContainer.add(agreementsSetLabel, STD_VC_LAYOUT);

        supposedAgreementStartDate = new DateFieldFullDate();
        FieldLabel supposedAgreementStartDateLabel = createFieldLabelTop(supposedAgreementStartDate, "Предполагаемая дата заключения соглашения (контракта)");
        supposedAgreementStartDateContainer = new VerticalLayoutContainer();
        supposedAgreementStartDateContainer.add(supposedAgreementStartDateLabel, STD_VC_LAYOUT);

        supposedAgreementEndDate = new DateFieldFullDate();
        FieldLabel supposedAgreementEndDateLabel = createFieldLabelTop(supposedAgreementEndDate, "Предполагаемая дата окончания соглашения (контракта)");
        supposedAgreementEndDateContainer = new VerticalLayoutContainer();
        supposedAgreementEndDateContainer.add(supposedAgreementEndDateLabel, STD_VC_LAYOUT);

        supposedValidityYears = new DoubleField();
        FieldLabel supposedValidityYearsLabel = createFieldLabelTop(supposedValidityYears, "Предполагаемый срок действия соглашения, лет");
        supposedValidityYearsContainer = new VerticalLayoutContainer();
        supposedValidityYearsContainer.add(supposedValidityYearsLabel, STD_VC_LAYOUT);

        deliveryTimeOfGoodsWorkDate = new DateFieldFullDate();
        FieldLabel deliveryTimeOfGoodsWorkDateLabel = createFieldLabelTop(deliveryTimeOfGoodsWorkDate, "Срок поставки товара/выполнения работ  ");
        deliveryTimeOfGoodsWorkDateContainer = new VerticalLayoutContainer();
        deliveryTimeOfGoodsWorkDateContainer.add(deliveryTimeOfGoodsWorkDateLabel, STD_VC_LAYOUT);

        projectAgreementFileUploader = new FileUploader();
        projectAgreementFileUploader.setHeadingText("Обосновывающие документы:");

        groundsOfAgreementConclusion = createCommonFilterModelTwinTriggerComboBox("Выберите основание заключения соглашения");
        FieldLabel groundsOfAgreementConclusionLabel = createFieldLabelTop(groundsOfAgreementConclusion, "Способ реализации проекта");
        groundsOfAgreementConclusionContainer = new VerticalLayoutContainer();
        groundsOfAgreementConclusionContainer.add(groundsOfAgreementConclusionLabel, STD_VC_LAYOUT);

        actNumber = new TextField();
        actNumber.setName("Номер решения");
        actNumber.addValidator(new MaxLengthValidator(255));
        actNumberLabel = createFieldLabelTop(actNumber, "Номер решения");
        actNumberContainer = new VerticalLayoutContainer();
        actNumberContainer.add(actNumberLabel, STD_VC_LAYOUT);

        actDate = new DateFieldFullDate();
        actDateLabel = createFieldLabelTop(actDate, "Дата решения");
        actDateContainer = new VerticalLayoutContainer();
        actDateContainer.add(actDateLabel, STD_VC_LAYOUT);

        actFileUploader = new FileUploader();
        actFileUploader.setHeadingText("Текст решения");

        leaseAgreementUploader = new FileUploader();
        leaseAgreementUploader.setHeadingText("Текст договора аренды/ постановления о присвоении ЕТО");

        isProjectAssignedStatus = new CheckBox();
        isProjectAssignedStatus.setBoxLabel(wrapString("Проекту присвоен статус приоритетного/масштабного/ стратегического/комплексного инвестиционного проекта либо инвестиционного проекта, имеющего региональное значение"));

        decisionNumber = new TextField();
        FieldLabel decisionNumberLabel = createFieldLabelTop(decisionNumber, "Номер решения");
        decisionNumberContainer = new VerticalLayoutContainer();
        decisionNumberContainer.add(decisionNumberLabel, STD_VC_LAYOUT);

        decisionDate = new DateFieldFullDate();
        FieldLabel decisionDateLabel = createFieldLabelTop(decisionDate, "Дата решения");
        decisionDateContainer = new VerticalLayoutContainer();
        decisionDateContainer.add(decisionDateLabel, STD_VC_LAYOUT);

        decisionFileUploader = new FileUploader();
        decisionFileUploader.setHeadingText("Текст решения");

        proposalPublishDate = new DateFieldFullDate();
        FieldLabel proposalPublishDateLabel = createFieldLabelTop(proposalPublishDate, "Дата размещения предложения на torgi.gov.ru");
        proposalPublishDateContainer = new VerticalLayoutContainer();
        proposalPublishDateContainer.add(proposalPublishDateLabel, STD_VC_LAYOUT);

        proposalTextFileUploader = new FileUploader();
        proposalTextFileUploader.setHeadingText("Текст предложения:");

        torgiGovRuUrl = new TextField();
        torgiGovRuUrl.setName("Ссылка на torgi.gov.ru, где размещено сообщение о конкурсе");
        torgiGovRuUrl.addValidator(new MaxLengthValidator(1000));
        FieldLabel torgiGovRuUrlLabel = createFieldLabelTop(torgiGovRuUrl, "Ссылка на torgi.gov.ru, где размещено сообщение о конкурсе");
        torgiGovRuUrlContainer = new VerticalLayoutContainer();
        torgiGovRuUrlContainer.add(torgiGovRuUrlLabel, STD_VC_LAYOUT);

        protocolFileUploader = new FileUploader();
        protocolFileUploader.setHeadingText("Протокол рассмотрения заявок от иных лиц:");

        isReadinessRequestReceived = new CheckBox();
        isReadinessRequestReceived.setBoxLabel(wrapString("Поступили заявки о готовности участия в конкурсе"));

        isDecisionMadeToConcludeAnAgreement = new CheckBox();
        isDecisionMadeToConcludeAnAgreement.setBoxLabel(wrapString("Принято решение о заключении соглашения"));

        conclustionActNumber = new TextField();
        FieldLabel conclustionActNumberLabel = createFieldLabelTop(conclustionActNumber, "Номер решения");
        conclustionActNumberContainer = new VerticalLayoutContainer();
        conclustionActNumberContainer.add(conclustionActNumberLabel, STD_VC_LAYOUT);

        conclusionActDate = new DateFieldFullDate();
        FieldLabel conclusionActDateLabel = createFieldLabelTop(conclusionActDate, "Дата решения");
        conclusionActDateContainer = new VerticalLayoutContainer();
        conclusionActDateContainer.add(conclusionActDateLabel, STD_VC_LAYOUT);

        conclusionFileUploader = new FileUploader();
        conclusionFileUploader.setHeadingText("Текст решения:");

        investmentStageDurationDate = new DateFieldFullDate();
        FieldLabel investmentStageDurationDateLabel = createFieldLabelTop(investmentStageDurationDate, "Срок инвестиционной стадии");
        investmentStageDurationDateContainer = new VerticalLayoutContainer();
        investmentStageDurationDateContainer.add(investmentStageDurationDateLabel, STD_VC_LAYOUT);

        isAgreementSigned = new CheckBox();
        isAgreementSigned.setBoxLabel(wrapString("Соглашение подписано"));


        jointCompetition = new CheckBox();
        jointCompetition.setBoxLabel(wrapString("Совместный конкурс"));

        anotherProjectsInfo = new TextArea();
        anotherProjectsInfo.setResizable(VERTICAL);
//        anotherProjectsInfo.setHeight(100);
        anotherProjectsInfo.setEmptyText("Введите информацию об иных проектах в совместном конкурсе");

        anotherProjectsIdentifier = new TextField();
        anotherProjectsIdentifier.setName("Введите идентификаторы иных проектов в совместном конкурсе");
        anotherProjectsIdentifier.addValidator(new MaxLengthValidator(255));
        anotherProjectsIdentifier.setEmptyText("Введите идентификаторы иных проектов в совместном конкурсе");

        competitionBidCollEndPlanDate = new DateFieldFullDate();
        competitionBidCollEndFactDate = new DateFieldFullDate();
        FieldLabel competitionBidCollEndFactDateLabel = createFieldLabelTop(competitionBidCollEndFactDate, "Дата окончания сбора заявок на участие в конкурсе");
        competitionTenderOfferEndPlanDate = new DateFieldFullDate();
        competitionTenderOfferEndFactDate = new DateFieldFullDate();
        FieldLabel competitionTenderOfferEndFactDateLabel = createFieldLabelTop(competitionTenderOfferEndFactDate, "Дата окончания представления конкурсных предложений");
        competitionResultsPlanDate = new DateFieldFullDate();
        competitionResultsFactDate = new DateFieldFullDate();
        FieldLabel competitionResultsFactDateLabel = createFieldLabelTop(competitionResultsFactDate, "Дата подведения итогов конкурса");

        competitionTextFileUploader = new FileUploader();
        competitionTextFileUploader.setHeadingText("Текст сообщения о проведении конкурса:");

        competitionIsElAuction = new CheckBox();
        competitionIsElAuction.setBoxLabel(wrapString("Конкурс является аукционом в электронной форме (только по ценовому критерию)"));

        competitionHasResults = new CheckBox();
        competitionHasResults.setBoxLabel(wrapString("Итоги конкурса подведены"));

        competitionResults = createCommonFilterModelComboBox("Результаты проведения конкурса");

        // атрибут удален
        competitionResultsProtocolNum = new TextField();
        FieldLabel competitionResultsProtocolNumLabel = createFieldLabelTop(competitionResultsProtocolNum, noWrapString("Номер протокола"));
        competitionResultsProtocolNumLabel.setId("competitionResultsProtocolNum");
        /////////////////////////

        competitionResultsProtocolDate = new DateFieldFullDate();
        FieldLabel competitionResultsProtocolDateLabel = createFieldLabelTop(competitionResultsProtocolDate, noWrapString("Дата протокола"));
        competitionResultsProtocolDateLabel.setId("competitionResultsProtocolDate");
        competitionResultsParticipantsNum = new IntegerField();
        FieldLabel competitionResultsParticipantsNumLabel = createFieldLabelTop(competitionResultsParticipantsNum, noWrapString("Количество участников"));
        competitionResultsParticipantsNumLabel.setId("competitionResultsParticipantsNum");
//        competitionResultsHRow = createTwoFieldWidgetsRow(competitionResultsProtocolDateLabel, competitionResultsParticipantsNumLabel);

        competitionResultsProtocolFileUploader = new FileUploader();
        competitionResultsProtocolFileUploader.setHeadingText("Текст протокола конкурсной комиссии/решения о признании конкурса несостоявшимся");
        competitionResultsDocFileUploader = new FileUploader();
        competitionResultsDocFileUploader.setHeadingText("Текст конкурсной документации:");

        competitionResultsSignStatus = createCommonFilterModelComboBox("Выберите статус подписания соглашения");
        FieldLabel competitionResultsSignStatusLabel = createFieldLabelTop(competitionResultsSignStatus, "Подписание соглашения");
        competitionResultsSignStatusContainer = new VerticalLayoutContainer();
        competitionResultsSignStatusContainer.add(competitionResultsSignStatusLabel, STD_VC_LAYOUT);

        contractPriceOrder = createCommonFilterModelComboBox("Выберите порядок расчета цены контракта");
        contractPriceFormula = new TextField();
        contractPriceFormula.setName("Описание формулы");
        contractPriceFormula.addValidator(new MaxLengthValidator(255));
        FieldLabel contractPriceFormulaLabel = createFieldLabelTop(contractPriceFormula, "Описание формулы");
        contractPriceFormulaContainer = new VerticalLayoutContainer();
        contractPriceFormulaContainer.add(contractPriceFormulaLabel, STD_VC_LAYOUT);
        contractPricePrice = new DoubleField();
        FieldLabel contractPricePriceLabel = createFieldLabelTop(contractPricePrice, "Цена контракта");
        contractPricePriceContainer = new VerticalLayoutContainer();
        contractPricePriceContainer.add(contractPricePriceLabel, STD_VC_LAYOUT);

        ndsCheck = new CheckBox();
        ndsCheck.setBoxLabel("Включая НДС");

        measureType = WidgetUtils.createCommonFilterModelComboBox("Выберите тип измерения");
        measureType.getStore().add(new SimpleIdNameModel("1", "В ценах соответствующих лет"));
        measureType.getStore().add(new SimpleIdNameModel("2", "На дату"));
        dateField = new DateFieldFullDate();

        measureType.addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                if ("2".equals(event.getSelectedItem().getId())) {
                    dateField.show();
                } else if ("1".equals(event.getSelectedItem().getId())) {
                    dateField.clear();
                    dateField.hide();
                }
            }
        });
        TreeHRow = createThreeFieldWidgetsNdsDateRow(ndsCheck, measureType, dateField);
        contractPricePriceContainer.add(TreeHRow, HEIGHT60_VC_LAYOUT);

        contractPriceOffer = createCommonFilterModelComboBox("Выберите предложение");
        FieldLabel contractPriceOfferLabel = createFieldLabelTop(contractPriceOffer, noWrapString("Предложение Заказчика"));
        contractPriceOfferLabel.setId("contractPriceOffer");
        contractPriceOfferValue = new DoubleField();
        contractPriceOfferValue.setEmptyText("Размер предложения");
        FieldLabel contractPriceOfferValueLabel = createFieldLabelTop(contractPriceOfferValue, "");
        contractPriceOfferValueLabel.setId("contractPriceOfferValue");

        winnerContractPriceOffer = createCommonFilterModelComboBox("Выберите Предложение победителя конкурса");
        FieldLabel winnerContractPriceOfferLabel = createFieldLabelTop(winnerContractPriceOffer, "Предложение победителя конкурса");
        winnerContractPriceOfferContainer = new VerticalLayoutContainer();
        winnerContractPriceOfferContainer.add(winnerContractPriceOfferLabel, STD_VC_LAYOUT);

        contractPriceOfferRow = createTwoFieldWidgetsRow(contractPriceOfferLabel, contractPriceOfferValueLabel);

        contractPriceSavingStartDate = new DateFieldFullDate();
        FieldLabel contractPriceSavingStartDateLabel = createFieldLabelTop(contractPriceSavingStartDate, "- начальный");
        contractPriceSavingEndDate = new DateFieldFullDate();
        FieldLabel contractPriceSavingEndDateLabel = createFieldLabelTop(contractPriceSavingEndDate, "- конечный");
        contractPriceSavingRow = createTwoFieldWidgetsRow(contractPriceSavingStartDateLabel, contractPriceSavingEndDateLabel);

        creationInvestments = new InvestmentsWidget(true);
        investments = new InvestmentsWidget(true);

        financialModelFileUpload = new FileUploader();
        financialModelFileUpload.setHeadingText("Обосновывающие документы");
        publicPartnerCostRecoveryMethod = createCommonFilterModelComboBox("Выберите способ возмещения расходов частного партнера");
        FieldLabel publicPartnerCostRecoveryMethodLabel = createFieldLabelTop(publicPartnerCostRecoveryMethod, "Способ возмещения расходов частного партнера");
        publicPartnerCostRecoveryMethodContainer = new VerticalLayoutContainer();
        publicPartnerCostRecoveryMethodContainer.add(publicPartnerCostRecoveryMethodLabel, STD_VC_LAYOUT);//

        advancePaymentAmount = new DoubleField();
        FieldLabel advancePaymentAmountLabel = createFieldLabelTop(advancePaymentAmount, "Размер авансового платежа");
        advancePaymentAmountContainer = new VerticalLayoutContainer();
        advancePaymentAmountContainer.add(advancePaymentAmountLabel, STD_VC_LAYOUT);

        budgetExpendituresAgreementOnGchpMchp = new DoubleField();
        FieldLabel budgetExpendituresAgreementOnGchpMchpLabel = createFieldLabelTop(budgetExpendituresAgreementOnGchpMchp, "Соглашение о ГЧП/МЧП");

        budgetExpendituresGovContract = new DoubleField();
        FieldLabel budgetExpendituresGovContractLabel = createFieldLabelTop(budgetExpendituresGovContract, "Государственный контракт");
        HorizontalLayoutContainer budgetExpendituresRow = createTwoFieldWidgetsRow(budgetExpendituresAgreementOnGchpMchpLabel, budgetExpendituresGovContractLabel);

        obligationsInCaseOfRisksAgreementOnGchpMchp = new DoubleField();
        FieldLabel obligationsInCaseOfRisksAgreementOnGchpMchpLabel = createFieldLabelTop(obligationsInCaseOfRisksAgreementOnGchpMchp, "Соглашение о ГЧП/МЧП");

        obligationsInCaseOfRisksGovContract = new DoubleField();
        FieldLabel obligationsInCaseOfRisksGovContractLabel = createFieldLabelTop(obligationsInCaseOfRisksGovContract, "Государственный контракт");
        HorizontalLayoutContainer obligationsInCaseOfRisksRow = createTwoFieldWidgetsRow(obligationsInCaseOfRisksAgreementOnGchpMchpLabel, obligationsInCaseOfRisksGovContractLabel);

        firstObjectOperationDate = new DateFieldFullDate();
        FieldLabel firstObjectOperationDateLabel = createFieldLabelTop(firstObjectOperationDate, "Дата начала создания / реконструкции первого объекта соглашения");
        firstObjectOperationDateContainer = new VerticalLayoutContainer();
        firstObjectOperationDateContainer.add(firstObjectOperationDateLabel, STD_VC_LAYOUT);

        lastObjectCommissioningDate = new DateFieldFullDate();
        FieldLabel lastObjectCommissioningDateLabel = createFieldLabelTop(lastObjectCommissioningDate, "Дата ввода последнего объекта в эксплуатацию");
        lastObjectCommissioningDateContainer = new VerticalLayoutContainer();
        lastObjectCommissioningDateContainer.add(lastObjectCommissioningDateLabel, STD_VC_LAYOUT);

        supportingDocumentsFileUploader = new FileUploader();
        supportingDocumentsFileUploader.setHeadingText("Основание (обосновывающие документы):");

        indicatorAssessmentComparativeAdvantage = new DoubleField();
        FieldLabel indicatorAssessmentComparativeAdvantageLabel = createFieldLabelTop(indicatorAssessmentComparativeAdvantage, "Значение показателя оценки сравнительного преимущества");
        indicatorAssessmentComparativeAdvantageContainer = new VerticalLayoutContainer();
        indicatorAssessmentComparativeAdvantageContainer.add(indicatorAssessmentComparativeAdvantageLabel, STD_VC_LAYOUT);

        conclusionUOTextFileUploader = new FileUploader();
        conclusionUOTextFileUploader.setHeadingText(new SafeHtmlBuilder().appendHtmlConstant("Заключение уполномоченного органа " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer;' " +
                "title='Прикладывается заключение об эффективности проекта и его сравнительном преимуществе / неэффективности проекта и (или) об отсутствии его сравнительного преимущества'></i>:").toSafeHtml());

        financialModelTextFileUploader = new FileUploader();
        financialModelTextFileUploader.setHeadingText("Финансовая модель:");

        methodOfExecuteObligation = createCommonFilterModelComboBox("Выберите обязательства частной стороны, предусмотренные соглашением");
        FieldLabel methodOfExecuteObligationLabel = createFieldLabelTop(methodOfExecuteObligation, "Обязательства частной стороны, предусмотренные соглашением");
        methodOfExecuteObligationContainer = new VerticalLayoutContainer();
        methodOfExecuteObligationContainer.add(methodOfExecuteObligationLabel, STD_VC_LAYOUT);

        linkToClauseAgreement = new TextField();
        FieldLabel linkToClauseAgreementLabel = createFieldLabelTop(linkToClauseAgreement, "Ссылка на пункт соглашения");
        linkToClauseAgreementContainer = new VerticalLayoutContainer();
        linkToClauseAgreementContainer.add(linkToClauseAgreementLabel, STD_VC_LAYOUT);

        otherGovSupports = createCommonFilterModelComboBox("Выберите иные меры господдержки, предоставляемые в рамках СПИК");
        FieldLabel otherGovSupportsLabel = createFieldLabelTop(otherGovSupports, "Иные меры господдержки, предоставляемые в рамках СПИК");
        otherGovSupportsContainer = new VerticalLayoutContainer();
        otherGovSupportsContainer.add(otherGovSupportsLabel, STD_VC_LAYOUT);

        isPrivateLiabilityProvided = new CheckBox();
        isPrivateLiabilityProvided.setBoxLabel(wrapString("Договором предусмотрена ответственность частной стороны за несоблюдение вышеуказанных обязательств"));

        ppImplementProject = new CheckBox();
        ppConcludeAgreementLink = new TextField();
        ppConcludeAgreementLinkLabel = createFieldLabelTop(ppConcludeAgreementLink, "Ссылка на torgi.gov.ru, где размещено предложение о заключении соглашения");
        ppResultsOfPlacing = createCommonFilterModelComboBox("Выберите результат размещения предложения на torgi.gov.ru");
        ppResultsOfPlacingLabel = createFieldLabelTop(ppResultsOfPlacing, "Результаты размещения предложения на torgi.gov.ru");
        ppConcludeAgreementLink.setName("Ссылка на torgi.gov.ru, где размещено предложение о заключении соглашения");
        ppConcludeAgreementLink.addValidator(new MaxLengthValidator(255));


        linkToClauseAgreementLiabilityProvided = new TextField();
        FieldLabel linkToClauseAgreementLiabilityProvidedLabel = createFieldLabelTop(linkToClauseAgreementLiabilityProvided, "Ссылка на пункт соглашения");
        linkToClauseAgreementLiabilityProvidedContainer = new VerticalLayoutContainer();
        linkToClauseAgreementLiabilityProvidedContainer.add(linkToClauseAgreementLiabilityProvidedLabel, STD_VC_LAYOUT);

        AccordionLayoutContainer.AccordionLayoutAppearance appearance = GWT.<AccordionLayoutContainer.AccordionLayoutAppearance>create(AccordionLayoutContainer.AccordionLayoutAppearance.class);
        MarginData cpMargin = new MarginData(10, 10, 10, 20);
        ContentPanel cp1 = new ContentPanel(appearance);

        proposalContainer = new VerticalLayoutContainer();
        agreementSigned = new VerticalLayoutContainer();
        proposalContainer.add(proposalPublishDateContainer, STD_VC_LAYOUT);
        proposalContainer.add(ppConcludeAgreementLinkLabel, STD_VC_LAYOUT);
        proposalContainer.add(proposalTextFileUploader, STD_VC_LAYOUT);

        cp1.setAnimCollapse(false);
        cp1.setHeading("Общая информация");
        cp1V = new VerticalLayoutContainer();
//        cp1V.add(isObjInListWithConcessionalAgreements, STD_VC_LAYOUT);
//        cp1V.add(objectsListUrlContainer, STD_VC_LAYOUT);
        cp1V.add(groundsOfAgreementConclusionContainer, STD_VC_LAYOUT); //1
        cp1V.add(supposedPrivatePartnerSelectButtonContainer, STD_VC_LAYOUT); // вместе с инн и именем
        cp1V.add(supposedPrivatePartnerNameContainer, STD_VC_LAYOUT); //2
        cp1V.add(supposedPrivatePartnerInnContainer, STD_VC_LAYOUT); //3
        cp1V.add(supposedValidityYearsContainer, STD_VC_LAYOUT);
        cp1V.add(ppImplementProject, STD_VC_LAYOUT);
        cp1V.add(ppResultsOfPlacingLabel, STD_VC_LAYOUT);
        cp1V.add(actNumberContainer, STD_VC_LAYOUT);
        cp1V.add(actDateContainer, STD_VC_LAYOUT);
        cp1V.add(actFileUploader, STD_VC_LAYOUT);
        cp1V.add(torgiGovRuUrlContainer, STD_VC_LAYOUT);
        cp1V.add(isAgreementSigned, STD_VC_LAYOUT);
//        cp1V.add(isForeignInvestor, STD_VC_LAYOUT);
//        cp1V.add(isMcpSubject, STD_VC_LAYOUT);
//        cp1V.add(agreementsSetContainer, STD_VC_LAYOUT);
//        cp1V.add(supposedAgreementStartDateContainer, STD_VC_LAYOUT);
//        cp1V.add(supposedAgreementEndDateContainer, STD_VC_LAYOUT);
//        cp1V.add(deliveryTimeOfGoodsWorkDateContainer, STD_VC_LAYOUT);
//        cp1V.add(projectAgreementFileUploader, STD_VC_LAYOUT);
//        cp1V.add(leaseAgreementUploader, STD_VC_LAYOUT);
//        cp1V.add(isProjectAssignedStatus, STD_VC_LAYOUT);
//        cp1V.add(decisionNumberContainer, STD_VC_LAYOUT);
//        cp1V.add(decisionDateContainer, STD_VC_LAYOUT);
//        cp1V.add(decisionFileUploader, STD_VC_LAYOUT);
//        cp1V.add(protocolFileUploader, STD_VC_LAYOUT);
//        cp1V.add(isReadinessRequestReceived, STD_VC_LAYOUT);
//        cp1V.add(isDecisionMadeToConcludeAnAgreement, STD_VC_LAYOUT);
        cp1.add(cp1V, cpMargin);

        cp2 = new ContentPanel(appearance);
        cp2.setAnimCollapse(false);
        cp2.setHeading("Совместный конкурс");
        VerticalLayoutContainer cp2V = new VerticalLayoutContainer();
//        cp2V.add(conclustionActNumberContainer, STD_VC_LAYOUT);
//        cp2V.add(conclusionActDateContainer, STD_VC_LAYOUT);
//        cp2V.add(conclusionFileUploader, STD_VC_LAYOUT);
//        cp2V.add(investmentStageDurationDateContainer, STD_VC_LAYOUT);
        cp2V.add(jointCompetition, STD_VC_LAYOUT);
//        cp2V.add(anotherProjectsInfo, STD_VC_LAYOUT);
        cp2V.add(anotherProjectsIdentifier, STD_VC_LAYOUT);
        cp2.add(cp2V, cpMargin);

        cp3 = new ContentPanel(appearance);
        cp3.setAnimCollapse(false);
        cp3.setHeading("Проведение конкурса");
        VerticalLayoutContainer cp3V = new VerticalLayoutContainer();
        cp3V.add(competitionResultsDocFileUploader, STD_VC_LAYOUT);
        competitionBidDateContainer = new VerticalLayoutContainer();
        competitionBidDateContainer.add(competitionBidCollEndFactDateLabel, STD_VC_LAYOUT);
        cp3V.add(competitionBidDateContainer);
        competitionTenderDateContainer = new VerticalLayoutContainer();
        competitionTenderDateContainer.add(competitionTenderOfferEndFactDateLabel, STD_VC_LAYOUT);
        cp3V.add(competitionTenderDateContainer);
        competitionResultDateContainer = new VerticalLayoutContainer();
        competitionResultDateContainer.add(competitionResultsFactDateLabel, STD_VC_LAYOUT);
        cp3V.add(competitionResultDateContainer);
        // атрибут был удален
        // cp3V.add(competitionTextFileUploader, STD_VC_LAYOUT);
//        cp3V.add(competitionIsElAuction, STD_VC_LAYOUT);
        cp3V.add(competitionHasResults, STD_VC_LAYOUT);
        cp3.add(cp3V, cpMargin);

        cp4 = new ContentPanel(appearance);
        cp4.setAnimCollapse(false);
        cp4.setHeading("Результаты проведения конкурса");
        VerticalLayoutContainer cp4V = new VerticalLayoutContainer();
        cp4V.add(competitionResults, STD_VC_LAYOUT);
        cp4V.add(competitionResultsProtocolDateLabel, STD_VC_LAYOUT);
        cp4V.add(competitionResultsProtocolFileUploader, STD_VC_LAYOUT);
        cp4V.add(competitionResultsSignStatusContainer, STD_VC_LAYOUT);
        cp4.add(cp4V, cpMargin);

        cp5 = new ContentPanel(appearance);
        cp5.setAnimCollapse(false);
        cp5.setHeading("Цена контракта");
        VerticalLayoutContainer cp5V = new VerticalLayoutContainer();
        cp5V.add(contractPriceOrder, STD_VC_LAYOUT);
        cp5V.add(contractPriceFormulaContainer, STD_VC_LAYOUT);
//        cp5V.add(contractPricePriceContainer, STD_VC_LAYOUT);
        cp5V.add(contractPriceOfferRow, HEIGHT60_VC_LAYOUT);
        cp5V.add(winnerContractPriceOfferContainer, STD_VC_LAYOUT);
        contractPriceSavingContainer = new VerticalLayoutContainer();
        contractPriceSavingContainer.add(createHtml("Плановый срок достижения экономии"));
        contractPriceSavingContainer.add(contractPriceSavingRow, new VerticalLayoutContainer.VerticalLayoutData(1, 70, new Margins(0, 0, 10, 0)));
        cp5V.add(contractPriceSavingContainer);
//        cp5V.add(publicPartnerCostRecoveryMethodContainer, STD_VC_LAYOUT);
//        cp5V.add(advancePaymentAmountContainer, STD_VC_LAYOUT);
//        cp5V.add(firstObjectOperationDateContainer, STD_VC_LAYOUT);
//        cp5V.add(methodOfExecuteObligationContainer, STD_VC_LAYOUT);
//        cp5V.add(linkToClauseAgreementContainer, STD_VC_LAYOUT);
//        cp5V.add(isPrivateLiabilityProvided, STD_VC_LAYOUT);
//        cp5V.add(linkToClauseAgreementLiabilityProvidedContainer, STD_VC_LAYOUT);
//        cp5V.add(otherGovSupportsContainer, STD_VC_LAYOUT);
        cp5V.add(WidgetUtils.createVerticalGap(20));
        cp5.add(cp5V, cpMargin);

        cp6 = new ContentPanel(appearance);
        cp6.setAnimCollapse(false);
        cp6.setHeading(new SafeHtmlBuilder().appendHtmlConstant("Планируемый объем инвестиций " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Финансовые показатели указываются в тысячах рублей.'></i>").toSafeHtml());
        VerticalLayoutContainer cp6V = new VerticalLayoutContainer();
//        cp6V.add(createHtml("Планируемый объем инвестиций на стадии создания/реконструкции"));
        cp6V.add(creationInvestments, STD_VC_LAYOUT);
//        cp6V.add(createHtml("Планируемый объем инвестиций на стадии эксплуатации"));
//        cp6V.add(investments, STD_VC_LAYOUT);
//        cp6V.add(financialModelFileUpload, STD_VC_LAYOUT);
        cp6.add(cp6V, cpMargin);
        creationInvestments.getTreeGrid().addViewReadyHandler(viewReadyEvent -> {
            creationInvestments.getTreeGrid().setExpanded(creationInvestments.getTreeStore().getChild(2), true);
            if (creationInvestments.getTreeStore().getAll().stream()
                    .anyMatch(planFactYear -> planFactYear.getPlan() != null && planFactYear.getPlan() != 0
                            || planFactYear.getFact() != null && planFactYear.getFact() != 0))
                cp6.expand();
        });

        cp7 = new ContentPanel(appearance);
        cp7.setAnimCollapse(false);
        cp7.setHeading("Оценка сравнительного преимущества");
        cp7V = new VerticalLayoutContainer();
        cp7V.add(conclusionUOTextFileUploader, STD_VC_LAYOUT);
        cp7V.add(financialModelTextFileUploader, STD_VC_LAYOUT);
        budgetExpendituresContainer = new VerticalLayoutContainer();
        budgetExpendituresContainer.add(createHtml("Чистый дисконтированый расход бюджетных средств"));
        budgetExpendituresContainer.add(budgetExpendituresRow, new VerticalLayoutContainer.VerticalLayoutData(1, 60));
//        cp7V.add(budgetExpendituresContainer);
        isObligationsInCaseOfRisksContainer = new VerticalLayoutContainer();
        isObligationsInCaseOfRisksContainer.add(createHtml("Объем принимаемых публичным партнером обязательств в случае возникновения рисков"));
        isObligationsInCaseOfRisksContainer.add(obligationsInCaseOfRisksRow, new VerticalLayoutContainer.VerticalLayoutData(1, 60));
//        cp7V.add(isObligationsInCaseOfRisksContainer);
//        cp7V.add(indicatorAssessmentComparativeAdvantageContainer, STD_VC_LAYOUT);
        cp7V.add(WidgetUtils.createVerticalGap(20));
        cp7.add(cp7V, cpMargin);

//        cp8 = new ContentPanel(appearance);
//        cp8.setAnimCollapse(false);
//        cp8.setHeading("Ввод объектов в эксплуатацию");
//        VerticalLayoutContainer cp8V = new VerticalLayoutContainer();
//        cp8V.add(lastObjectCommissioningDateContainer, STD_VC_LAYOUT); ///////////////////
//        cp8V.add(supportingDocumentsFileUploader, STD_VC_LAYOUT); ////////////////////
//        cp8.add(cp8V, cpMargin);

        AccordionLayoutContainer accordion1 = new AccordionLayoutContainer();
        accordion1.setExpandMode(AccordionLayoutContainer.ExpandMode.MULTI);
        accordion1.add(cp1);
        accordion1.add(cp2);
        accordion1.add(cp3);
        accordion1.add(cp4);
//        accordion1.add(cp8);
        accordion1.setActiveWidget(cp1);

        cp2.collapse();
        cp3.collapse();
        cp4.collapse();
//        cp8.collapse();
        AccordionLayoutContainer accordion2 = new AccordionLayoutContainer();
        accordion2.setExpandMode(AccordionLayoutContainer.ExpandMode.MULTI);
        accordion2.add(cp5);
        accordion2.add(cp7);
        accordion2.add(cp6);
        cp5.collapse();
        cp7.collapse();
        cp6.collapse();

        container.add(proposalContainer);
        container.add(accordion1, STD_VC_LAYOUT);
        container.add(agreementSigned);
        container.add(accordion2, STD_VC_LAYOUT);

        jointCompetition.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    anotherProjectsInfo.show();
                    anotherProjectsIdentifier.show();
                    container.forceLayout();

                } else {
                    anotherProjectsInfo.hide();
                    anotherProjectsIdentifier.hide();
                    anotherProjectsInfo.setValue(null);
                    anotherProjectsIdentifier.setValue(null);
                }

            }
        });
        anotherProjectsInfo.hide();
        anotherProjectsIdentifier.hide();

        competitionHasResults.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    cp4.show();
                    competitionResultsProtocolFileUploader.toggle(true);
                    competitionResultsSignStatusContainer.show();
                    competitionResultsProtocolDateLabel.show();
                    container.forceLayout();
                } else {
                    cp4.hide();

                    competitionResultsProtocolDateLabel.hide();
                    competitionResultsProtocolFileUploader.toggle(false);
                    competitionResultsSignStatusContainer.hide();

                    competitionResults.setValue(null);
                    competitionResultsProtocolDate.setValue(null);
                    competitionResultsParticipantsNum.setValue(null);
                    competitionResultsProtocolFileUploader.setFiles(new ArrayList<>());
                    competitionResultsDocFileUploader.setFiles(new ArrayList<>());
                    competitionResultsSignStatus.setValue(null);
                }

            }
        });

        cp4.hide();
        competitionResultsProtocolDateLabel.hide();
        competitionResultsProtocolFileUploader.toggle(false);
        competitionResultsDocFileUploader.toggle(false);
        competitionResultsSignStatusContainer.hide();

        isDecisionMadeToConcludeAnAgreement.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    conclustionActNumberContainer.show();
                    conclusionActDateContainer.show();
                    conclusionFileUploader.toggle(true);
                    container.forceLayout();
                } else {
                    conclustionActNumberContainer.hide();
                    conclusionActDateContainer.hide();
                    conclusionFileUploader.toggle(false);

                    conclustionActNumber.setValue(null);
                    conclusionActDate.setValue(null);
                    conclusionFileUploader.setFiles(new ArrayList<>());
                }
            }
        });
        // для создания нового документа сразу скрываем, т.к. галки нет.
        conclustionActNumberContainer.hide();
        conclusionActDateContainer.hide();
        conclusionFileUploader.toggle(false);

        isObjInListWithConcessionalAgreements.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue() != null && valueChangeEvent.getValue()) {
                objectsListUrlContainer.show();
                container.forceLayout();
            } else {
                objectsListUrlContainer.hide();
                objectsListUrl.setValue(null);
            }
        });
        objectsListUrlContainer.hide();

        ppResultsOfPlacing.addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                if (event.getSelectedItem() != null && "2".equals(event.getSelectedItem().getId())) {
                    cp2.show();
                    cp3.show();
                    cp4.show();
                    torgiGovRuUrlContainer.show();
                } else {
                    cp2.hide();
                    cp3.hide();
                    cp4.hide();
                    torgiGovRuUrlContainer.hide();
                }
                if (event.getSelectedItem() != null && "3".equals(event.getSelectedItem().getId()) && !ppImplementProject.getValue()) {
                    actNumber.hide();
                    actNumberLabel.hide();
                    actDate.hide();
                    actDateLabel.hide();
                    actFileUploader.toggle(false);
                } else {
                    actNumber.show();
                    actNumberLabel.show();
                    actDate.show();
                    actDateLabel.show();
                    actFileUploader.toggle(true);
                    container.forceLayout();
                }
            }
        });

        ppResultsOfPlacing.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue() != null && valueChangeEvent.getValue().getId().equals("3") && !ppImplementProject.getValue()) {
                actNumber.hide();
                actNumberLabel.hide();
                actDateLabel.hide();
                actDate.hide();
                actFileUploader.toggle(false);
            } else {
                actNumber.show();
                actDate.show();
                actFileUploader.toggle(true);
                actDateLabel.show();
                actNumberLabel.show();
                container.forceLayout();
            }
        });

        ppImplementProject.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue() != null && ppResultsOfPlacing.getValue().getId().equals("3") && !ppImplementProject.getValue()) {
                actNumber.hide();
                actNumberLabel.hide();
                actDateLabel.hide();
                actDate.hide();
                actFileUploader.toggle(false);
            } else {
                actNumber.show();
                actDate.show();
                actFileUploader.toggle(true);
                actDateLabel.show();
                actNumberLabel.show();
                container.forceLayout();
            }
        });

        ppImplementProject.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue() != null && ppResultsOfPlacing.getValue() == null && !ppImplementProject.getValue()) {
                actNumber.hide();
                actNumberLabel.hide();
                actDateLabel.hide();
                actDate.hide();
                actFileUploader.toggle(false);
            } else {
                actNumber.show();
                actDate.show();
                actFileUploader.toggle(true);
                actDateLabel.show();
                actNumberLabel.show();
                container.forceLayout();
            }
        });

        actNumber.hide();
        actNumberLabel.hide();
        actDateLabel.hide();
        actDate.hide();
        actFileUploader.toggle(false);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    private static ViewAttrState viewAttrState;
    private static ViewAttrState viewAttrStateByFormIdOnly;

    static {
        viewAttrState = new ViewAttrState();

        viewAttrState.getColumns().addAll(ViewAttrStateColumns.getPreparationFormImplIds());

        viewAttrState.getState().put("isObjInListWithConcessionalAgreements", Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("objectsListUrl", Arrays.asList(1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("supposedPrivatePartnerName", Arrays.asList(1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("supposedPrivatePartnerInn", Arrays.asList(1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("isForeignInvestor", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0));
        viewAttrState.getState().put("isMcpSubject", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0));
        viewAttrState.getState().put("agreementsSet", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0));
        viewAttrState.getState().put("supposedAgreementStartDate", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0));
        viewAttrState.getState().put("supposedAgreementEndDate", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0));
        viewAttrState.getState().put("supposedValidityYears", Arrays.asList(1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("deliveryTimeOfGoodsWorkDate", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("projectAgreementFileUploader", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("groundsOfAgreementConclusion", Arrays.asList(1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("actNumber", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("actDate", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("actFileUploader", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("leaseAgreementUploader", Arrays.asList(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("isProjectAssignedStatus", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("decisionNumber", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("decisionDate", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("decisionFileUploader", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("isAgreementSigned", Arrays.asList(1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("creationInvestments_total", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("creationInvestments_private", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("creationInvestments_budget", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("investments_total", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("investments_private", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("investments_budget", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("financialModelFileUpload", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("publicPartnerCostRecoveryMethod", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("advancePaymentAmount", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("firstObjectOperationDate", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("lastObjectCommissioningDate", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("supportingDocumentsFileUploader", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("budgetExpendituresAgreementOnGchpMchp_budgetExpendituresGovContract", Arrays.asList(0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("obligationsInCaseOfRisksAgreementOnGchpMchp_obligationsInCaseOfRisksGovContract", Arrays.asList(0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("indicatorAssessmentComparativeAdvantage", Arrays.asList(0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("conclusionUOTextFileUploader", Arrays.asList(0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("financialModelTextFileUploader", Arrays.asList(0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("methodOfExecuteObligation", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("linkToClauseAgreement", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("isPrivateLiabilityProvided", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("linkToClauseAgreementLiabilityProvided", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("otherGovSupports", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("proposalPublishDate", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("proposalTextFileUploader", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("torgiGovRuUrl", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("protocolFileUploader", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("isReadinessRequestReceived", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("isDecisionMadeToConcludeAnAgreement", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("conclustionActNumber", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("conclusionActDate", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("conclusionFileUploader", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("investmentStageDurationDate", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("jointCompetition", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("anotherProjectsInfo", Arrays.asList(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("anotherProjectsIdentifier", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionBidCollEndPlanDate_competitionBidCollEndFactDate", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionTenderOfferEndPlanDate_competitionTenderOfferEndFactDate", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionResultsPlanDate_competitionResultsFactDate", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionTextFileUploader", Arrays.asList(0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionIsElAuction", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionHasResults", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionResults", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionResultsProtocolNum", Arrays.asList(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1));
        viewAttrState.getState().put("competitionResultsProtocolDate", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionResultsParticipantsNum", Arrays.asList(0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 0, 1, 0, 1, 0, 1));
        viewAttrState.getState().put("competitionResultsProtocolFileUploader", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionResultsDocFileUploader", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("competitionResultsSignStatus", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("contractPriceOrder", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("contractPriceFormula", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("contractPricePrice", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("contractPriceOffer", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("contractPriceOfferValue", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("winnerContractPriceOffer", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("contractPriceSavingStartDate", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("contractPriceSavingEndDate", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("ppImplementProject", Arrays.asList(1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("ppResultsOfPlacing", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("ppConcludeAgreementLink", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("proposalContainer", Arrays.asList(0, 1, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("isAgreementSignedInAgreementSigned", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("jointCompetitionContainer151", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("torgiGovRuUrlContainer201", Arrays.asList(0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("jointCompetitionContainer201", Arrays.asList(0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("initId_151", Arrays.asList(0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("101_201", Arrays.asList(0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("151_201", Arrays.asList(0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));

        viewAttrStateByFormIdOnly = new ViewAttrState();

        viewAttrStateByFormIdOnly.getColumns().addAll(ViewAttrStateColumns.getFormIds());

        viewAttrStateByFormIdOnly.getState().put("supposedPrivatePartnerName",   Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("supposedPrivatePartnerInn",    Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("supposedValidityYears",        Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("groundsOfAgreementConclusion", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("ppImplementProject",           Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("ppResultsOfPlacing",           Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("apartFromOtherForms",          Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("cp2",                          Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("cp3",                          Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("cp4", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("cp5", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("cp6", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrStateByFormIdOnly.getState().put("cp7", Arrays.asList(0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
    }

    /**
     * Обновить форму (скрыть + очистить/показать поля) в зависимости от формы реализации проекта
     * и способа инициации проекта.
     *
     * @param formId - форма реализации проекта
     * @param initId - способ инициации проекта
     */
    public void update(Long formId, Long initId) {
        String formIdStr = (formId == null || initId == null) ? null : formId + "_" + initId;
        String formIdStrWithoutInitId = (formId == null) ? null : String.valueOf(formId);

        // checkbox isObjInListWithConcessionalAgreements
//        if (viewAttrState.isAttrActive(formIdStr, "isObjInListWithConcessionalAgreements")) {
//            isObjInListWithConcessionalAgreements.show();
//        } else {
//            isObjInListWithConcessionalAgreements.hide();
//            isObjInListWithConcessionalAgreements.setValue(null);
//        }

        // text objectsListUrl
//        if (viewAttrState.isAttrActive(formIdStr, "objectsListUrl")) {
//            objectsListUrlContainer.show();
//            ValueChangeEvent.fire(isObjInListWithConcessionalAgreements, isObjInListWithConcessionalAgreements.getValue());
//        } else {
//            objectsListUrlContainer.hide();
//            objectsListUrl.setValue(null);
//        }

        boolean showSupposedPrivatePartnerNameContainer = false;
        supposedPrivatePartnerSelectButtonContainer.hide();

        // text supposedPrivatePartnerName
        if ((viewAttrState.isAttrActive(formIdStr, "supposedPrivatePartnerName"))
                && (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "supposedPrivatePartnerName"))) {
            supposedPrivatePartnerNameContainer.show();
            showSupposedPrivatePartnerNameContainer = true;
        } else {
            supposedPrivatePartnerNameContainer.hide();
            supposedPrivatePartnerName.setValue(null);
        }

        // text supposedPrivatePartnerInn
        if ((viewAttrState.isAttrActive(formIdStr, "supposedPrivatePartnerInn"))
                && (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "supposedPrivatePartnerInn"))) {
            supposedPrivatePartnerInnContainer.show();
            if (showSupposedPrivatePartnerNameContainer) {
                supposedPrivatePartnerSelectButtonContainer.show();
            }
        } else {
            supposedPrivatePartnerInnContainer.hide();
            supposedPrivatePartnerInn.setValue(null);
        }

        // file actFileUploader
        if (viewAttrState.isAttrActive(formIdStr, "actFileUploader")) {
            actFileUploader.toggle(true);
        } else {
            actFileUploader.toggle(false);
            actFileUploader.setFiles(new ArrayList<>());
        }

        if ((ppResultsOfPlacing.getCurrentValue() != null
                && "3".equals(ppResultsOfPlacing.getCurrentValue().getId())
                && !ppImplementProject.getValue())
                || (ppResultsOfPlacing.getCurrentValue() == null && !ppImplementProject.getValue())) {
            actNumber.hide();
            actNumberLabel.hide();
            actDateLabel.hide();
            actDate.hide();
            actFileUploader.toggle(false);
        } else {
            actNumber.show();
            actDate.show();
            actFileUploader.toggle(true);
            actDateLabel.show();
            actNumberLabel.show();
            container.forceLayout();
        }

        if (viewAttrState.isAttrActive(formIdStr, "ppConcludeAgreementLink")) {
            ppConcludeAgreementLinkLabel.show();
            ppConcludeAgreementLink.show();
        } else {
            ppConcludeAgreementLinkLabel.hide();
            ppConcludeAgreementLink.hide();
            ppConcludeAgreementLink.setValue(null);
        }
//        // checkbox isForeignInvestor
//        if (viewAttrState.isAttrActive(formIdStr, "isForeignInvestor")) {
//            isForeignInvestor.show();
//        } else {
//            isForeignInvestor.hide();
//            isForeignInvestor.setValue(null);
//        }

//        // checkbox isMcpSubject
//        if (viewAttrState.isAttrActive(formIdStr, "isMcpSubject")) {
//            isMcpSubject.show();
//        } else {
//            isMcpSubject.hide();
//            isMcpSubject.setValue(null);
//        }

//        // combobox agreementsSet
//        if (viewAttrState.isAttrActive(formIdStr, "agreementsSet")) {
//            agreementsSetContainer.show();
//        } else {
//            agreementsSetContainer.hide();
//            agreementsSet.setValue(null);
//        }

        // date supposedAgreementStartDate
//        if (viewAttrState.isAttrActive(formIdStr, "supposedAgreementStartDate")) {
//            supposedAgreementStartDateContainer.show();
//        } else {
//            supposedAgreementStartDateContainer.hide();
//            supposedAgreementStartDate.setValue(null);
//        }

        // text supposedAgreementEndDate
//        if (viewAttrState.isAttrActive(formIdStr, "supposedAgreementEndDate")) {
//            supposedAgreementEndDateContainer.show();
//        } else {
//            supposedAgreementEndDateContainer.hide();
//            supposedAgreementEndDate.setValue(null);
//        }

        // combobox groundsOfAgreementConclusion
        if ((viewAttrState.isAttrActive(formIdStr, "groundsOfAgreementConclusion"))
                && (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "groundsOfAgreementConclusion"))) {
            groundsOfAgreementConclusionContainer.show();
        } else {
            groundsOfAgreementConclusionContainer.hide();
            groundsOfAgreementConclusion.setValue(null);
        }

        // text actNumber
        if (viewAttrState.isAttrActive(formIdStr, "actNumber")) {
            actNumberContainer.show();
        } else {
            actNumberContainer.hide();
            actNumber.setValue(null);
        }

        // date actDate
        if (viewAttrState.isAttrActive(formIdStr, "actDate")) {
            actDateContainer.show();
        } else {
            actDateContainer.hide();
            actDate.setValue(null);
        }

        // date deliveryTimeOfGoodsWorkDate
//        if (viewAttrState.isAttrActive(formIdStr, "deliveryTimeOfGoodsWorkDate")) {
//            deliveryTimeOfGoodsWorkDateContainer.show();
//        } else {
//            deliveryTimeOfGoodsWorkDateContainer.hide();
//            deliveryTimeOfGoodsWorkDate.setValue(null);
//        }

        // double supposedValidityYears
        if ((viewAttrState.isAttrActive(formIdStr, "supposedValidityYears"))
                && (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "supposedValidityYears"))) {
            supposedValidityYearsContainer.show();
        } else {
            supposedValidityYearsContainer.hide();
            supposedValidityYears.setValue(null);
        }

        // checkbox isProjectAssignedStatus
//        if (viewAttrState.isAttrActive(formIdStr, "isProjectAssignedStatus")) {
//            isProjectAssignedStatus.show();
//        } else {
//            isProjectAssignedStatus.hide();
//            isProjectAssignedStatus.setValue(null);
//        }

        // checkbox isReadinessRequestReceived
//        if (viewAttrState.isAttrActive(formIdStr, "isReadinessRequestReceived")) {
//            isReadinessRequestReceived.show();
//        } else {
//            isReadinessRequestReceived.hide();
//            isReadinessRequestReceived.setValue(null);
//        }

        // checkbox isDecisionMadeToConcludeAnAgreement
//        if (viewAttrState.isAttrActive(formIdStr, "isDecisionMadeToConcludeAnAgreement")) {
//            isDecisionMadeToConcludeAnAgreement.show();
//        } else {
//            isDecisionMadeToConcludeAnAgreement.hide();
//            isDecisionMadeToConcludeAnAgreement.setValue(null);
//        }

        // file projectAgreementFileUploader
//        if (viewAttrState.isAttrActive(formIdStr, "projectAgreementFileUploader")) {
//            projectAgreementFileUploader.toggle(true);
//        } else {
//            projectAgreementFileUploader.toggle(false);
//            projectAgreementFileUploader.setFiles(new ArrayList<>());
//        }


        // file leaseAgreementUploader
//        if (viewAttrState.isAttrActive(formIdStr, "leaseAgreementUploader")) {
//            leaseAgreementUploader.toggle(true);
//        } else {
//            leaseAgreementUploader.toggle(false);
//            leaseAgreementUploader.setFiles(new ArrayList<>());
//        }

        // file decisionFileUploader
//        if (viewAttrState.isAttrActive(formIdStr, "decisionFileUploader")) {
//            decisionFileUploader.toggle(true);
//        } else {
//            decisionFileUploader.toggle(false);
//            decisionFileUploader.setFiles(new ArrayList<>());
//        }

        // file proposalTextFileUploader
        if (viewAttrState.isAttrActive(formIdStr, "proposalTextFileUploader")) {
            proposalTextFileUploader.toggle(true);
        } else {
            proposalTextFileUploader.toggle(false);
            proposalTextFileUploader.setFiles(new ArrayList<>());
        }

        // file protocolFileUploader
//        if (viewAttrState.isAttrActive(formIdStr, "protocolFileUploader")) {
//            protocolFileUploader.toggle(true);
//        } else {
//            protocolFileUploader.toggle(false);
//            protocolFileUploader.setFiles(new ArrayList<>());
//        }

        // text decisionNumber
//        if (viewAttrState.isAttrActive(formIdStr, "decisionNumber")) {
//            decisionNumberContainer.show();
//        } else {
//            decisionNumberContainer.hide();
//            decisionNumber.setValue(null);
//        }

        // date decisionDate
//        if (viewAttrState.isAttrActive(formIdStr, "decisionDate")) {
//            decisionDateContainer.show();
//        } else {
//            decisionDateContainer.hide();
//            decisionDate.setValue(null);
//        }

        // date proposalPublishDate
        if (viewAttrState.isAttrActive(formIdStr, "proposalPublishDate")) {
            proposalPublishDateContainer.show();
        } else {
            proposalPublishDateContainer.hide();
            proposalPublishDate.setValue(null);
        }

        // text torgiGovRuUrl
        if ((viewAttrState.isAttrActive(formIdStr, "torgiGovRuUrl"))
                && (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "apartFromOtherForms"))) {
            torgiGovRuUrlContainer.show();
        } else {
            torgiGovRuUrlContainer.hide();
            torgiGovRuUrl.setValue(null);
        }

        // Accordion 2
        // text conclustionActNumber
        boolean isConclustionActNumber = viewAttrState.isAttrActive(formIdStr, "conclustionActNumber");
//        if (isConclustionActNumber) {
//            conclustionActNumberContainer.show();
//            ValueChangeEvent.fire(isDecisionMadeToConcludeAnAgreement, isDecisionMadeToConcludeAnAgreement.getValue());
//        } else {
//            conclustionActNumberContainer.hide();
//            conclustionActNumber.setValue(null);
//        }

        // date conclusionActDate
        boolean isConclusionActDate = viewAttrState.isAttrActive(formIdStr, "conclusionActDate");
//        if (isConclusionActDate) {
//            conclusionActDateContainer.show();
//            ValueChangeEvent.fire(isDecisionMadeToConcludeAnAgreement, isDecisionMadeToConcludeAnAgreement.getValue());
//        } else {
//            conclusionActDateContainer.hide();
//            conclusionActDate.setValue(null);
//        }

        // file conclusionFileUploader
        boolean isConclusionFileUploader = viewAttrState.isAttrActive(formIdStr, "conclusionFileUploader");
//        if (isConclusionFileUploader) {
//            conclusionFileUploader.toggle(true);
//            ValueChangeEvent.fire(isDecisionMadeToConcludeAnAgreement, isDecisionMadeToConcludeAnAgreement.getValue());
//        } else {
//            conclusionFileUploader.toggle(false);
//            conclusionFileUploader.setFiles(new ArrayList<>());
//        }

        // date investmentStageDurationDate
        boolean isInvestmentStageDurationDate = viewAttrState.isAttrActive(formIdStr, "investmentStageDurationDate");
//        if (isInvestmentStageDurationDate) {
//            investmentStageDurationDateContainer.show();
//        } else {
//            investmentStageDurationDateContainer.hide();
//            investmentStageDurationDate.setValue(null);
//        }

        // checkbox isAgreementSigned
        boolean isAgreementSignedBool = viewAttrState.isAttrActive(formIdStr, "isAgreementSigned");
        if (isAgreementSignedBool) {
            isAgreementSigned.show();
        } else {
            isAgreementSigned.hide();
            isAgreementSigned.setValue(null);
        }

        // checkbox jointCompetition
        boolean isJointCompetition = viewAttrState.isAttrActive(formIdStr, "jointCompetition");
        if (isJointCompetition) {
            jointCompetition.show();
        } else {
            jointCompetition.hide();
            jointCompetition.setValue(null);
        }

        // textarea anotherProjectsInfo
        boolean isAnotherProjectsInfo = viewAttrState.isAttrActive(formIdStr, "anotherProjectsInfo");
//        if (isAnotherProjectsInfo) {
//            anotherProjectsInfo.show();
//        } else {
//            anotherProjectsInfo.hide();
//            anotherProjectsInfo.setValue(null);
//        }

        // text anotherProjectsIdentifier
//        boolean isAnotherProjectsIdentifier = viewAttrState.isAttrActive(formIdStr, "anotherProjectsIdentifier");
//        if (isAnotherProjectsIdentifier) {
//            anotherProjectsIdentifier.show();
//        } else {
//            anotherProjectsIdentifier.hide();
//            anotherProjectsIdentifier.setValue(null);
//        }

        boolean isJointCompetitionContainer151 = viewAttrState.isAttrActive(formIdStr, "jointCompetitionContainer151");
        boolean isJointCompetitionContainer201 = viewAttrState.isAttrActive(formIdStr, "jointCompetitionContainer201");
        if ((isJointCompetitionContainer151 && ppResultsOfPlacing.getCurrentValue() != null && ppResultsOfPlacing.getCurrentValue().getId().equals("2"))
                || isJointCompetitionContainer201) {
            if (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "cp2")) {
                cp2.show();
                torgiGovRuUrlContainer.show();
            } else {
                cp2.hide();
            }
        } else {
            torgiGovRuUrlContainer.hide();
            cp2.hide();
        }

        // Accordion 3
        // 2 row date competitionBidDate
        boolean isCcompetitionBidDate = viewAttrState.isAttrActive(formIdStr, "competitionBidCollEndPlanDate_competitionBidCollEndFactDate");
        if (isCcompetitionBidDate) {
            competitionBidDateContainer.show();
        } else {
            competitionBidDateContainer.hide();

            competitionBidCollEndPlanDate.setValue(null);
            competitionBidCollEndFactDate.setValue(null);
        }

        // 2 row date competitionTenderDate
        boolean isCompetitionTenderDate = viewAttrState.isAttrActive(formIdStr, "competitionTenderOfferEndPlanDate_competitionTenderOfferEndFactDate");
        if (isCompetitionTenderDate) {
            competitionTenderDateContainer.show();
        } else {
            competitionTenderDateContainer.hide();

            competitionTenderOfferEndPlanDate.setValue(null);
            competitionTenderOfferEndFactDate.setValue(null);
        }

        // 2 row date competitionResultDate
        boolean isCompetitionResultDate = viewAttrState.isAttrActive(formIdStr, "competitionResultsPlanDate_competitionResultsFactDate");
        if (isCompetitionResultDate) {
            competitionResultDateContainer.show();
        } else {
            competitionResultDateContainer.hide();

            competitionResultsPlanDate.setValue(null);
            competitionResultsFactDate.setValue(null);
        }

        // file competitionTextFileUploader
        boolean isCompetitionTextFileUploader = viewAttrState.isAttrActive(formIdStr, "competitionTextFileUploader");
//        if (isCompetitionTextFileUploader) {
//            this.competitionTextFileUploader.toggle(true);
//        } else {
//            this.competitionTextFileUploader.toggle(false);
//            this.competitionTextFileUploader.setFiles(new ArrayList<>());
//        }

        // checkbox competitionIsElAuction
        boolean isCompetitionIsElAuction = viewAttrState.isAttrActive(formIdStr, "competitionIsElAuction");
//        if (isCompetitionIsElAuction) {
//            this.competitionIsElAuction.show();
//        } else {
//            this.competitionIsElAuction.hide();
//            this.competitionIsElAuction.setValue(null);
//        }

        // checkbox competitionHasResults
        boolean isCompetitionHasResults = viewAttrState.isAttrActive(formIdStr, "competitionHasResults");
        if (isCompetitionHasResults) {
            this.competitionHasResults.show();
        } else {
            this.competitionHasResults.hide();
            this.competitionHasResults.setValue(null);
        }

        if ((isCcompetitionBidDate || isCompetitionTenderDate || isCompetitionResultDate
                || isCompetitionTextFileUploader || isCompetitionIsElAuction || isCompetitionHasResults)
                && ((isJointCompetitionContainer151 && ppResultsOfPlacing.getValue() != null && ppResultsOfPlacing.getValue().getId().equals("2"))
                || isJointCompetitionContainer201)
                && (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "cp3"))) {
            cp3.show();
        } else {
            cp3.hide();
        }

        // Accordion 4
        // combobox competitionResults
        boolean isCompetitionResults = viewAttrState.isAttrActive(formIdStr, "competitionResults");
        if (isCompetitionResults) {
            competitionResults.show();
        } else {
            competitionResults.hide();
            competitionResults.setValue(null);
        }

        boolean isCompetitionResultsProtocolDate = viewAttrState.isAttrActive(formIdStr, "competitionResultsProtocolDate");
//        if (isCompetitionResultsProtocolDate) {
//            toggleHorizontalLayoutContainerColumn(competitionResultsHRow, "competitionResultsProtocolDate", true);
//        } else {
//            toggleHorizontalLayoutContainerColumn(competitionResultsHRow, "competitionResultsProtocolDate", false);
//            competitionResultsProtocolDate.setValue(null);
//        }

        boolean isCompetitionResultsParticipantsNum = viewAttrState.isAttrActive(formIdStr, "competitionResultsParticipantsNum");
//        if (isCompetitionResultsParticipantsNum) {
//            toggleHorizontalLayoutContainerColumn(competitionResultsHRow, "competitionResultsParticipantsNum", true);
//        } else {
//            toggleHorizontalLayoutContainerColumn(competitionResultsHRow, "competitionResultsParticipantsNum", false);
//            competitionResultsParticipantsNum.setValue(null);
//        }

        // file competitionResultsProtocolFileUploader
        boolean isCompetitionResultsProtocolFileUploader = viewAttrState.isAttrActive(formIdStr, "competitionResultsProtocolFileUploader");
        if (isCompetitionResultsProtocolFileUploader) {
            competitionResultsProtocolFileUploader.toggle(true);
        } else {
            competitionResultsProtocolFileUploader.toggle(false);
            competitionResultsProtocolFileUploader.setFiles(new ArrayList<>());
        }

        // file competitionResultsDocFileUploader
        boolean isCompetitionResultsDocFileUploader = viewAttrState.isAttrActive(formIdStr, "competitionResultsDocFileUploader");
        if (isCompetitionResultsDocFileUploader) {
            competitionResultsDocFileUploader.toggle(true);
        } else {
            competitionResultsDocFileUploader.toggle(false);
            competitionResultsDocFileUploader.setFiles(new ArrayList<>());
        }

        // combobox competitionResultsSignStatus
        boolean isCompetitionResultsSignStatus = viewAttrState.isAttrActive(formIdStr, "competitionResultsSignStatus");
        if (isCompetitionResultsSignStatus) {
            competitionResultsSignStatusContainer.show();
        } else {
            competitionResultsSignStatusContainer.hide();
            competitionResultsSignStatus.setValue(null);
        }

        if ((isCompetitionResults || isCompetitionResultsProtocolDate || isCompetitionResultsParticipantsNum
                || isCompetitionResultsProtocolFileUploader || isCompetitionResultsDocFileUploader || isCompetitionResultsSignStatus)
                && ((isJointCompetitionContainer151 && ppResultsOfPlacing.getValue() != null && ppResultsOfPlacing.getValue().getId().equals("2"))
                || isJointCompetitionContainer201)
                && (competitionHasResults.getValue() != null && competitionHasResults.getValue())
                && (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "cp4"))) {
            cp4.show();
        } else {
            cp4.hide();
        }

        // Accordion 5
        // combobox contractPriceOrder
        if (viewAttrState.isAttrActive(formIdStr, "contractPriceOrder")) {
            contractPriceOrder.show();
        } else {
            contractPriceOrder.hide();
            contractPriceOrder.setValue(null);
        }

        // text contractPriceFormula
        if (viewAttrState.isAttrActive(formIdStr, "contractPriceFormula")) {
            contractPriceFormulaContainer.show();
        } else {
            contractPriceFormulaContainer.hide();
            contractPriceFormula.setValue(null);
        }

        // double contractPricePrice
        if (viewAttrState.isAttrActive(formIdStr, "contractPricePrice")) {
            contractPricePriceContainer.show();
            TreeHRow.show();
        } else {
            contractPricePriceContainer.hide();
            TreeHRow.hide();
            ndsCheck.setValue(null);
            measureType.setValue(null);
            dateField.setValue(null);
            contractPricePrice.setValue(null);
        }

        // combobox contractPriceOffer
        if (viewAttrState.isAttrActive(formIdStr, "contractPriceOffer")) {
            toggleHorizontalLayoutContainerColumn(contractPriceOfferRow, "contractPriceOffer", true);
        } else {
            toggleHorizontalLayoutContainerColumn(contractPriceOfferRow, "contractPriceOffer", false);
            contractPriceOffer.setValue(null);
        }

        // text contractPriceOfferValue
        if (viewAttrState.isAttrActive(formIdStr, "contractPriceOfferValue")) {
            toggleHorizontalLayoutContainerColumn(contractPriceOfferRow, "contractPriceOfferValue", true);
        } else {
            toggleHorizontalLayoutContainerColumn(contractPriceOfferRow, "contractPriceOfferValue", false);
            contractPriceOfferValue.setValue(null);
        }

        // combobox winnerContractPriceOffer
        if (viewAttrState.isAttrActive(formIdStr, "winnerContractPriceOffer")) {
            winnerContractPriceOfferContainer.show();
        } else {
            winnerContractPriceOfferContainer.hide();
            winnerContractPriceOffer.setValue(null);
        }

        // 2 columns date contractPriceSaving
        boolean isContractPriceSavingStartDate = viewAttrState.isAttrActive(formIdStr, "contractPriceSavingStartDate");
        if (isContractPriceSavingStartDate) {
            toggleHorizontalLayoutContainerColumn(contractPriceSavingRow, "contractPriceSavingStartDate", true);
        } else {
            toggleHorizontalLayoutContainerColumn(contractPriceSavingRow, "contractPriceSavingStartDate", false);
            contractPriceSavingStartDate.setValue(null);
        }
        boolean isContractPriceSavingEndDate = viewAttrState.isAttrActive(formIdStr, "contractPriceSavingEndDate");
        if (isContractPriceSavingEndDate) {
            toggleHorizontalLayoutContainerColumn(contractPriceSavingRow, "contractPriceSavingEndDate", true);
        } else {
            toggleHorizontalLayoutContainerColumn(contractPriceSavingRow, "contractPriceSavingEndDate", false);
            contractPriceSavingEndDate.setValue(null);
        }
        if (!isContractPriceSavingStartDate && !isContractPriceSavingEndDate) {
            contractPriceSavingContainer.hide();
        } else {
            contractPriceSavingContainer.show();
        }

        // file financialModelFileUpload
//        if (viewAttrState.isAttrActive(formIdStr, "financialModelFileUpload")) {
//            financialModelFileUpload.toggle(true);
//        } else {
//            financialModelFileUpload.toggle(false);
//            financialModelFileUpload.setFiles(new ArrayList<>());
//        }

        // text publicPartnerCostRecoveryMethod
//        if (viewAttrState.isAttrActive(formIdStr, "publicPartnerCostRecoveryMethod")) {
//            publicPartnerCostRecoveryMethodContainer.show();
//        } else {
//            publicPartnerCostRecoveryMethodContainer.hide();
//            publicPartnerCostRecoveryMethod.setValue(null);
//        }

        // text advancePaymentAmount
//        if (viewAttrState.isAttrActive(formIdStr, "advancePaymentAmount")) {
//            advancePaymentAmountContainer.show();
//        } else {
//            advancePaymentAmountContainer.hide();
//            advancePaymentAmount.setValue(null);
//        }

        // text firstObjectOperationDate
//        if (viewAttrState.isAttrActive(formIdStr, "firstObjectOperationDate")) {
//            firstObjectOperationDateContainer.show();
//        } else {
//            firstObjectOperationDateContainer.hide();
//            firstObjectOperationDate.setValue(null);
//        }

        // text lastObjectCommissioningDate
//        if (viewAttrState.isAttrActive(formIdStr, "lastObjectCommissioningDate")) {
//            lastObjectCommissioningDateContainer.show();
//        } else {
//            lastObjectCommissioningDateContainer.hide();
//            lastObjectCommissioningDate.setValue(null);
//        }

        // file supportingDocumentsFileUploader
//        if (viewAttrState.isAttrActive(formIdStr, "supportingDocumentsFileUploader")) {
//            supportingDocumentsFileUploader.toggle(true);
//        } else {
//            supportingDocumentsFileUploader.toggle(false);
//            supportingDocumentsFileUploader.setFiles(new ArrayList<>());
//        }

        // text indicatorAssessmentComparativeAdvantage
//        if (viewAttrState.isAttrActive(formIdStr, "indicatorAssessmentComparativeAdvantage")) {
//            indicatorAssessmentComparativeAdvantageContainer.show();
//        } else {
//            indicatorAssessmentComparativeAdvantageContainer.hide();
//            indicatorAssessmentComparativeAdvantage.setValue(null);
//        }

        // file conclusionUOTextFileUploader
        if (viewAttrState.isAttrActive(formIdStr, "conclusionUOTextFileUploader")) {
            conclusionUOTextFileUploader.toggle(true);
        } else {
            conclusionUOTextFileUploader.toggle(false);
            conclusionUOTextFileUploader.setFiles(new ArrayList<>());
        }

        // file financialModelTextFileUploader
        if (viewAttrState.isAttrActive(formIdStr, "financialModelTextFileUploader")) {
            financialModelTextFileUploader.toggle(true);
        } else {
            financialModelTextFileUploader.toggle(false);
            financialModelTextFileUploader.setFiles(new ArrayList<>());
        }

        // text methodOfExecuteObligation
//        if (viewAttrState.isAttrActive(formIdStr, "methodOfExecuteObligation")) {
//            methodOfExecuteObligationContainer.show();
//        } else {
//            methodOfExecuteObligationContainer.hide();
//            methodOfExecuteObligation.setValue(null);
//        }

        // text linkToClauseAgreement
//        if (viewAttrState.isAttrActive(formIdStr, "linkToClauseAgreement")) {
//            linkToClauseAgreementContainer.show();
//        } else {
//            linkToClauseAgreementContainer.hide();
//            linkToClauseAgreement.setValue(null);
//        }

        // checkbox isPrivateLiabilityProvided
//        if (viewAttrState.isAttrActive(formIdStr, "isPrivateLiabilityProvided")) {
//            isPrivateLiabilityProvided.show();
//        } else {
//            isPrivateLiabilityProvided.hide();
//            isPrivateLiabilityProvided.setValue(null);
//        }

        // text linkToClauseAgreementLiabilityProvided
//        if (viewAttrState.isAttrActive(formIdStr, "linkToClauseAgreementLiabilityProvided")) {
//            linkToClauseAgreementLiabilityProvidedContainer.show();
//        } else {
//            linkToClauseAgreementLiabilityProvidedContainer.hide();
//            linkToClauseAgreementLiabilityProvided.setValue(null);
//        }

        // text otherGovSupports
//        if (viewAttrState.isAttrActive(formIdStr, "otherGovSupports")) {
//            otherGovSupportsContainer.show();
//        } else {
//            otherGovSupportsContainer.hide();
//            otherGovSupports.setValue(null);
//        }
        container.forceLayout();

        if ((viewAttrState.isAttrActive(formIdStr, "contractPriceOrder") ||
                viewAttrState.isAttrActive(formIdStr, "contractPriceFormula") ||
                viewAttrState.isAttrActive(formIdStr, "contractPricePrice") ||
                viewAttrState.isAttrActive(formIdStr, "contractPriceOffer") ||
                viewAttrState.isAttrActive(formIdStr, "contractPriceOfferValue") ||
                viewAttrState.isAttrActive(formIdStr, "winnerContractPriceOffer") ||
                isContractPriceSavingStartDate ||
                isContractPriceSavingEndDate ||
                viewAttrState.isAttrActive(formIdStr, "publicPartnerCostRecoveryMethod") ||
                viewAttrState.isAttrActive(formIdStr, "advancePaymentAmount") ||
                viewAttrState.isAttrActive(formIdStr, "firstObjectOperationDate") ||
                viewAttrState.isAttrActive(formIdStr, "methodOfExecuteObligation") ||
                viewAttrState.isAttrActive(formIdStr, "isPrivateLiabilityProvided") ||
                viewAttrState.isAttrActive(formIdStr, "linkToClauseAgreementLiabilityProvided") ||
                viewAttrState.isAttrActive(formIdStr, "otherGovSupports"))
                && viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "cp5")
        ) {
            cp5.show();
        } else {
            cp5.hide();
        }

        // Accordion 6
        if (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "cp6")) {
            cp6.show();
        } else {
            cp6.hide();
        }

        if (viewAttrState.isAttrActive(formIdStr, "creationInvestments_total")) {
            creationInvestments.addRootRowById(1L);
        } else {
            creationInvestments.removeRootRowById(1L);
        }

        if (viewAttrState.isAttrActive(formIdStr, "creationInvestments_private")) {
            creationInvestments.addRootRowById(2L);
        } else {
            creationInvestments.removeRootRowById(2L);
        }

        if (viewAttrState.isAttrActive(formIdStr, "creationInvestments_budget")) {
            creationInvestments.addRootRowById(3L);
            creationInvestments.addRootRowById(7L);
            creationInvestments.addRootRowById(8L);
            creationInvestments.addRootRowById(9L);
        } else {
            creationInvestments.removeRootRowById(3L);
            creationInvestments.removeRootRowById(7L);
            creationInvestments.removeRootRowById(8L);
            creationInvestments.removeRootRowById(9L);
        }

        if (viewAttrState.isAttrActive(formIdStr, "investments_total")) {
            investments.addRootRowById(1L);
        } else {
            investments.removeRootRowById(1L);
        }

        if (viewAttrState.isAttrActive(formIdStr, "investments_private")) {
            investments.addRootRowById(2L);
        } else {
            investments.removeRootRowById(2L);
        }

        if (viewAttrState.isAttrActive(formIdStr, "investments_budget")) {
            investments.addRootRowById(3L);
            investments.addRootRowById(7L);
            investments.addRootRowById(8L);
            investments.addRootRowById(9L);
        } else {
            investments.removeRootRowById(3L);
            investments.removeRootRowById(7L);
            investments.removeRootRowById(8L);
            investments.removeRootRowById(9L);
        }

        // Accordion 7
        // 2 row text budgetExpenditures
        boolean isBudgetExpenditures = viewAttrState.isAttrActive(formIdStr, "budgetExpendituresAgreementOnGchpMchp_budgetExpendituresGovContract");
        if (isBudgetExpenditures) {
            budgetExpendituresContainer.show();
        } else {
            budgetExpendituresContainer.hide();

            budgetExpendituresAgreementOnGchpMchp.setValue(null);
            budgetExpendituresGovContract.setValue(null);
        }

        // 2 row text obligationsInCaseOfRisks
        boolean isObligationsInCaseOfRisks = viewAttrState.isAttrActive(formIdStr, "obligationsInCaseOfRisksAgreementOnGchpMchp_obligationsInCaseOfRisksGovContract");
        if (isObligationsInCaseOfRisks) {
            isObligationsInCaseOfRisksContainer.show();
        } else {
            isObligationsInCaseOfRisksContainer.hide();

            obligationsInCaseOfRisksAgreementOnGchpMchp.setValue(null);
            obligationsInCaseOfRisksGovContract.setValue(null);
        }

        if ((
                isBudgetExpenditures ||
                        isObligationsInCaseOfRisks ||
                        viewAttrState.isAttrActive(formIdStr, "indicatorAssessmentComparativeAdvantage") ||
                        viewAttrState.isAttrActive(formIdStr, "conclusionUOTextFileUploader") ||
                        viewAttrState.isAttrActive(formIdStr, "financialModelTextFileUploader")) &&
                (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "cp7"))
        ) {
            cp7.show();
        } else {
            cp7.hide();
        }

        if ((viewAttrState.isAttrActive(formIdStr, "proposalContainer"))
                && (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "apartFromOtherForms"))) {
            proposalContainer.show();
            if (viewAttrState.isAttrActive(formIdStr, "torgiGovRuUrlContainer201")) {
                proposalContainer.add(torgiGovRuUrlContainer, STD_VC_LAYOUT);
            }
        } else {
            proposalContainer.hide();
        }

        if ((viewAttrState.isAttrActive(formIdStr, "isAgreementSignedInAgreementSigned"))
                && (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "apartFromOtherForms"))) {
            agreementSigned.add(isAgreementSigned, STD_VC_LAYOUT);
            cp1V.add(torgiGovRuUrlContainer, STD_VC_LAYOUT);
        } else {
            cp1V.add(isAgreementSigned, STD_VC_LAYOUT);
        }

        // checkbox ppImplementProject
        if (viewAttrState.isAttrActive(formIdStr, "ppImplementProject")) {
            ppImplementProject.show();
        } else {
            ppImplementProject.hide();
            ppImplementProject.setValue(null);
        }

        // checkbox ppImplementProject
        if (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "ppImplementProject")) {
            setImplementProjectLabel(false);
        } else {
            setImplementProjectLabel(true);
        }

        if ((viewAttrState.isAttrActive(formIdStr, "ppResultsOfPlacing"))
                && (viewAttrStateByFormIdOnly.isAttrActive(formIdStrWithoutInitId, "ppResultsOfPlacing"))) {
            ppResultsOfPlacing.show();
            ppResultsOfPlacingLabel.show();
        } else {
            ppResultsOfPlacing.hide();
            ppResultsOfPlacingLabel.hide();
            ppResultsOfPlacing.setValue(null);
        }
        container.forceLayout();

    }

    public void setImplementProjectLabel(Boolean otherForm) {
        if (otherForm) {
            ppImplementProject.setBoxLabel(wrapString("Наличие решения о реализации проекта"));
        } else {
            ppImplementProject.setBoxLabel(wrapString("Принято решение о заключении соглашения"));
        }
    }
}

