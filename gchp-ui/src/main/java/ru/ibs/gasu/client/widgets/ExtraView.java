package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
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
import ru.ibs.gasu.client.widgets.componens.TwinTriggerComboBox;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.view_attr.ViewAttrState;
import ru.ibs.gasu.common.models.view_attr.ViewAttrStateColumns;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sencha.gxt.cell.core.client.form.TextAreaInputCell.Resizable.VERTICAL;
import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

public class ExtraView implements IsWidget {

    private VerticalLayoutContainer container;
    private EventsView eventsView;
    private ThirdPartyOrgsWidget privateOrganizationsInvolvementView;
    private ThirdPartyOrgsWidget publicOrganizationsInvolvementView;
    private FinancialEconomicsIndView financialEconomicsIndView;
    private ComboBox<SimpleIdNameModel> taxRelief;
    private SanctionsView sanctionsView;
    private CourtActivityView courtActivityView;
    private ComboBox<SimpleIdNameModel> opf;
    private TextField concessionaireName;
    private TextField concessionaireInn;
    private TwinTriggerComboBox<SimpleIdNameModel> regimeType;
    private TextField creditRating;
    private DateField creditRatingSetDate;
    private DateField creditRatingReviewDate;
    private TextField creditRatingAgency;
    private PrivatePartnersOwningPartsView privatePartnersOwningPartsView = new PrivatePartnersOwningPartsView();

    public ContentPanel getCp11() {
        return cp11;
    }

    private ContentPanel cp11;

    public ContentPanel getCp12() {
        return cp12;
    }

    private ContentPanel cp12;
    private FinancialStructureWidget financialStructureView;
    private InvestmentsCriteriaBooleanWidget investmentsCriteriaBooleanWidget;
    private DoubleField unforeseenExpences;
    private DoubleField workPlacesCount;
    private TextArea unforeseenExpencesComment;
    private CheckBox adsIsThirdPartyOrgsProvided;
    private CheckBox adsIsRegInvestmentProject;
    private CheckBox hasIncomeTax;
    private ComboBox<SimpleIdNameModel> incomeTaxRate;
    private CheckBox hasLandTax;
    private ComboBox<SimpleIdNameModel> landTaxRate;
    private CheckBox hasPropertyTax;
    private ComboBox<SimpleIdNameModel> propertyTaxRate;
    private CheckBox hasBenefitClarificationTax;
    private TwinTriggerComboBox<SimpleIdNameModel> benefitClarificationRate;
    private TextField benefitDescription;

    public ContentPanel getCp5() {
        return cp5;
    }

    private ContentPanel cp5;

    public ContentPanel getCp6() {
        return cp6;
    }

    private ContentPanel cp6;
    private CheckBox adsIsTreasurySupport;
    private FileUploader decisionTextFileUpload;
    private VerticalLayoutContainer cp7V;
    private VerticalLayoutContainer cp8V;

    private MultiSelectComboBox<SimpleIdNameModel> competitionCriteria;
    private MultiSelectComboBox<SimpleIdNameModel> financialRequirement;
    private MultiSelectComboBox<SimpleIdNameModel> nonFinancialRequirement;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public EventsView getEventsView() {
        return eventsView;
    }

    public DoubleField getWorkPlacesCount() {
        return workPlacesCount;
    }

    public ThirdPartyOrgsWidget getPrivateOrganizationsInvolvementView() {
        return privateOrganizationsInvolvementView;
    }

    public ThirdPartyOrgsWidget getPublicOrganizationsInvolvementView() {
        return publicOrganizationsInvolvementView;
    }

    public FinancialEconomicsIndView getFinancialEconomicsIndView() {
        return financialEconomicsIndView;
    }

    public FinancialStructureWidget getFinancialStructureView() {
        return financialStructureView;
    }

    public CheckBox getIsTreasurySupport() {
        return adsIsTreasurySupport;
    }

    public FileUploader getDecisionTextFileUpload() {
        return decisionTextFileUpload;
    }

    public ComboBox<SimpleIdNameModel> getTaxRelief() {
        return taxRelief;
    }

    public SanctionsView getSanctionsView() {
        return sanctionsView;
    }

    public CourtActivityView getCourtActivityView() {
        return courtActivityView;
    }

    public ComboBox<SimpleIdNameModel> getOpf() {
        return opf;
    }

    public TextField getConcessionaireName() {
        return concessionaireName;
    }

    public TextField getConcessionaireInn() {
        return concessionaireInn;
    }

    public TwinTriggerComboBox<SimpleIdNameModel> getRegimeType() {
        return regimeType;
    }

    public TextField getCreditRating() {
        return creditRating;
    }

    public DateField getCreditRatingSetDate() {
        return creditRatingSetDate;
    }

    public DateField getCreditRatingReviewDate() {
        return creditRatingReviewDate;
    }

    public TextField getCreditRatingAgency() {
        return creditRatingAgency;
    }

    public PrivatePartnersOwningPartsView getPrivatePartnersOwningPartsView() {
        privatePartnersOwningPartsView.setItemsToMask(Arrays.asList(cp7V, cp8V));
        return privatePartnersOwningPartsView;
    }

    public InvestmentsCriteriaBooleanWidget getInvestmentsCriteriaBooleanWidget() {
        return investmentsCriteriaBooleanWidget;
    }

    public DoubleField getUnforeseenExpences() {
        return unforeseenExpences;
    }

    public TextArea getUnforeseenExpencesComment() {
        return unforeseenExpencesComment;
    }

    public CheckBox getIsThirdPartyOrgsProvided() {
        return adsIsThirdPartyOrgsProvided;
    }

    public CheckBox getIsRegInvestmentProject() {
        return adsIsRegInvestmentProject;
    }

    public CheckBox getHasIncomeTax() {
        return hasIncomeTax;
    }

    public ComboBox<SimpleIdNameModel> getIncomeTaxRate() {
        return incomeTaxRate;
    }

    public CheckBox getHasLandTax() {
        return hasLandTax;
    }

    public ComboBox<SimpleIdNameModel> getLandTaxRate() {
        return landTaxRate;
    }

    public CheckBox getHasPropertyTax() {
        return hasPropertyTax;
    }

    public ComboBox<SimpleIdNameModel> getPropertyTaxRate() {
        return propertyTaxRate;
    }

    public CheckBox getHasBenefitClarificationTax() {
        return hasBenefitClarificationTax;
    }

    public TwinTriggerComboBox<SimpleIdNameModel> getBenefitClarificationRate() {
        return benefitClarificationRate;
    }

    public TextField getBenefitDescription() {
        return benefitDescription;
    }

    public MultiSelectComboBox<SimpleIdNameModel> getCompetitionCriteria() {
        return competitionCriteria;
    }

    public MultiSelectComboBox<SimpleIdNameModel> getFinancialRequirement() {
        return financialRequirement;
    }

    public MultiSelectComboBox<SimpleIdNameModel> getNonFinancialRequirement() {
        return nonFinancialRequirement;
    }

    public ContentPanel getCp7() {
        return cp7;
    }

    private ContentPanel cp7;

    public ExtraView() {
        initWidget();
    }

    private TwinTriggerComboBox<SimpleIdNameModel> createRateComboBox() {
        TwinTriggerComboBox<SimpleIdNameModel> box = createCommonFilterModelTwinTriggerComboBox("Укажите ставку");
        box.getStore().add(new SimpleIdNameModel("1", "Нулевая"));
        box.getStore().add(new SimpleIdNameModel("2", "Пониженная"));
        return box;
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();

        eventsView = new EventsView();

        adsIsThirdPartyOrgsProvided = new CheckBox();
        adsIsThirdPartyOrgsProvided.setBoxLabel(wrapString("Привлечение организаций к реализации проекта"));

        privateOrganizationsInvolvementView = new ThirdPartyOrgsWidget("private");
        publicOrganizationsInvolvementView = new ThirdPartyOrgsWidget("public");

        financialEconomicsIndView = new FinancialEconomicsIndView();

        adsIsRegInvestmentProject = new CheckBox();
        adsIsRegInvestmentProject.setBoxLabel(wrapString("Региональный инвестиционный проект"));

        hasIncomeTax = new CheckBox();
        hasIncomeTax.setBoxLabel(wrapString("Налог на прибыль"));
        incomeTaxRate = createRateComboBox();
        HorizontalLayoutContainer incomeTaxRow = createTwoFieldWidgetsRow(hasIncomeTax, incomeTaxRate, 0.15, 0.2);

        hasLandTax = new CheckBox();
        hasLandTax.setBoxLabel("Земельный налог");
        landTaxRate = createRateComboBox();
        HorizontalLayoutContainer landTaxRow = createTwoFieldWidgetsRow(hasLandTax, landTaxRate, 0.15, 0.2);

        hasPropertyTax = new CheckBox();
        hasPropertyTax.setBoxLabel("Налог на имущество");
        propertyTaxRate = createRateComboBox();
        HorizontalLayoutContainer propertyTaxRow = createTwoFieldWidgetsRow(hasPropertyTax, propertyTaxRate, 0.15, 0.2);

        hasBenefitClarificationTax = new CheckBox();
        hasBenefitClarificationTax.setBoxLabel("Уточнение льготы");
        benefitClarificationRate = createRateComboBox();
        benefitDescription = new TextField();
        benefitDescription.setName("Описание льготы");
        benefitDescription.addValidator(new MaxLengthValidator(255));
        benefitDescription.setEmptyText("Описание льготы");
        HorizontalLayoutContainer benefitClarificationRow = createTwoFieldWidgetsRow(hasBenefitClarificationTax, benefitDescription, 0.15, 0.2);

        decisionTextFileUpload = new FileUploader();
        decisionTextFileUpload.setHeadingText("Решение");

        taxRelief = WidgetUtils.createCommonFilterModelComboBox("Выберите налоговые льготы");
        FieldLabel taxReliefLabel = createFieldLabelTop(taxRelief, "Налоговые льготы");

        sanctionsView = new SanctionsView();

        courtActivityView = new CourtActivityView();

        opf = createCommonFilterModelComboBox("ОПФ");
        FieldLabel opfLabel = createFieldLabelTop(opf, "ОПФ");
        concessionaireName = new TextField();
        FieldLabel concessionaireNameLabel = createFieldLabelTop(concessionaireName, "Полное наименование");
        concessionaireInn = new TextField();
        FieldLabel concessionaireInnLabel = createFieldLabelTop(concessionaireInn, "ИНН");
        regimeType = createCommonFilterModelTwinTriggerComboBox("Выберите виды режима");
        FieldLabel regimeTypeLabel = createFieldLabelTop(regimeType, "Вид режима");

        creditRating = new TextField();
        creditRating.setName("Рейтинг");
        creditRating.addValidator(new MaxLengthValidator(255));
        FieldLabel creditRatingLabel = createFieldLabelTop(creditRating, "Рейтинг");
        creditRatingSetDate = new DateFieldFullDate();
        FieldLabel creditRatingSetDateLabel = createFieldLabelTop(creditRatingSetDate, "Дата присвоения");
        creditRatingReviewDate = new DateFieldFullDate();
        FieldLabel creditRatingReviewDateLabel = createFieldLabelTop(creditRatingReviewDate, "Дата отзыва");
        creditRatingAgency = new TextField();
        creditRatingAgency.setName("Агентство");
        creditRatingAgency.addValidator(new MaxLengthValidator(255));
        FieldLabel creditRatingAgencyLabel = createFieldLabelTop(creditRatingAgency, "Агентство");

        financialStructureView = new FinancialStructureWidget();

        adsIsTreasurySupport = new CheckBox();
        adsIsTreasurySupport.setBoxLabel(wrapString("Осуществление казначейского сопровождения"));

        investmentsCriteriaBooleanWidget = new InvestmentsCriteriaBooleanWidget();

        unforeseenExpences = new DoubleField();
        unforeseenExpences.setEditable(false);
        FieldLabel unforeseenExpencesLabel = createFieldLabelTop(unforeseenExpences, "Доля непредвиденных расходов от всех расходов на создание");

        workPlacesCount = new DoubleField();
        FieldLabel workPlacesCountLabel = createFieldLabelTop(workPlacesCount, "Количество рабочих мест");

        unforeseenExpencesComment = new TextArea();
        unforeseenExpencesComment.setResizable(VERTICAL);
//        unforeseenExpencesComment.setHeight(100);
        unforeseenExpencesComment.setName("Комментарий");
        unforeseenExpencesComment.addValidator(new MaxLengthValidator(255));
        FieldLabel unforeseenExpencesCommentLabel = createFieldLabelTop(unforeseenExpencesComment, "Комментарий");

        competitionCriteria = WidgetUtils.createCommonFilterModelMultiSelectComboBox("Выберите критерии конкурса");
        FieldLabel competitionCriteriaLabel = createFieldLabelTop(competitionCriteria, "Критерии конкурса");
        financialRequirement = WidgetUtils.createCommonFilterModelMultiSelectComboBox("Выберите финансовые требования");
        FieldLabel financialRequirementLabel = createFieldLabelTop(financialRequirement, "Требования к участникам конкурса");
        nonFinancialRequirement = WidgetUtils.createCommonFilterModelMultiSelectComboBox("Выберите нефинансовые требования");

        AccordionLayoutContainer.AccordionLayoutAppearance appearance = GWT.<AccordionLayoutContainer.AccordionLayoutAppearance>create(AccordionLayoutContainer.AccordionLayoutAppearance.class);
        MarginData cpMargin = new MarginData(10, 10, 10, 20);

        ContentPanel cp1 = new ContentPanel(appearance);
        cp1.setAnimCollapse(false);
        cp1.setHeading("События проекта");
        VerticalLayoutContainer cp1V = new VerticalLayoutContainer();
        cp1V.add(eventsView, STD_VC_LAYOUT);
//        cp1V.add(adsIsThirdPartyOrgsProvided, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(5, 0, 5, 0)));
        cp1.add(cp1V, cpMargin);

//        ContentPanel cp2 = new ContentPanel(appearance);
//        cp2.setAnimCollapse(false);
//        cp2.setHeading("Со стороны частного партнера");
//        VerticalLayoutContainer cp2V = new VerticalLayoutContainer();
////        cp2V.add(privateOrganizationsInvolvementView, STD_VC_LAYOUT);
//        cp2.add(cp2V, cpMargin);

//        ContentPanel cp3 = new ContentPanel(appearance);
//        cp3.setAnimCollapse(false);
//        cp3.setHeading("Со стороны публичного партнера");
//        VerticalLayoutContainer cp3V = new VerticalLayoutContainer();
////        cp3V.add(publicOrganizationsInvolvementView, STD_VC_LAYOUT);
//        cp3.add(cp3V, cpMargin);

        ContentPanel cp4 = new ContentPanel(appearance);
        cp4.setAnimCollapse(false);
        cp4.setHeading("Финансово экономические показатели");
        VerticalLayoutContainer cp4V = new VerticalLayoutContainer();
        cp4V.add(financialEconomicsIndView, STD_VC_LAYOUT);
//        cp4V.add(adsIsRegInvestmentProject, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(5, 0, 5, 0)));

        cp4.add(cp4V, cpMargin);

        financialEconomicsIndView.getGrid().addViewReadyHandler(viewReadyEvent -> {
            if (financialEconomicsIndView.getStore().getAll().stream()
                    .anyMatch(ind -> ind.getValue() != null && ind.getValue() != 0))
                cp4.expand();
        });


        cp5 = new ContentPanel(appearance);
        cp5.setAnimCollapse(false);
        cp5.setHeading("Санкции в отношении частного партнера");
        VerticalLayoutContainer cp5V = new VerticalLayoutContainer();
        cp5V.add(sanctionsView, STD_VC_LAYOUT);
        cp5.add(cp5V, cpMargin);

        cp6 = new ContentPanel(appearance);
        cp6.setAnimCollapse(false);
        cp6.setHeading("Судебная активность по проекту");
        VerticalLayoutContainer cp6V = new VerticalLayoutContainer();
        cp6V.add(courtActivityView, STD_VC_LAYOUT);
        cp6.add(cp6V, cpMargin);

        cp7 = new ContentPanel(appearance);
        cp7.setAnimCollapse(false);
        cp7.setHeading("Концессионер");
        cp7V = new VerticalLayoutContainer();
//        cp7V.add(opfLabel, STD_VC_LAYOUT);
//        cp7V.add(concessionaireNameLabel, STD_VC_LAYOUT);
        cp7V.add(createFieldLabelTop(privatePartnersOwningPartsView.getEgrulFullNameLabel(), "Полное наименование"), STD_VC_LAYOUT);
        cp7V.add(createFieldLabelTop(privatePartnersOwningPartsView.getEgrulRoAddressLabel(), "Адрес (место нахождения) ЮЛ/ИП"), STD_VC_LAYOUT);
        cp7V.add(regimeTypeLabel, STD_VC_LAYOUT);
//        cp7V.add(concessionaireInnLabel, STD_VC_LAYOUT);
        cp7V.add(createHtml("<br> "));
        cp7V.add(createHtml("Кредитный рейтинг организации"));
        cp7V.add(creditRatingAgencyLabel, STD_VC_LAYOUT);
        cp7V.add(creditRatingLabel, STD_VC_LAYOUT);
        cp7V.add(creditRatingSetDateLabel, STD_VC_LAYOUT);
        cp7V.add(creditRatingReviewDateLabel, STD_VC_LAYOUT);
        cp7.add(cp7V, cpMargin);

        ContentPanel cp8 = new ContentPanel(appearance);
        cp8.setAnimCollapse(false);
        cp8.setHeading("Структура управления частным партнером");
        cp8V = new VerticalLayoutContainer();
//        cp8V.add(createFieldLabelTop(privatePartnersOwningPartsView.getFullEgrulRoAddressLabel(), "Адрес (место нахождения) ЮЛ/ИП"), STD_VC_LAYOUT);
        cp8V.add(privatePartnersOwningPartsView, STD_VC_LAYOUT);
        cp8.add(cp8V, cpMargin);

        privatePartnersOwningPartsView.getGrid().addViewReadyHandler(viewReadyEvent -> {
            if (privatePartnersOwningPartsView.getGrid().getStore().getAll().size() > 0)
                cp8.expand();
        });

        ContentPanel cp9 = new ContentPanel(appearance);
        cp9.setAnimCollapse(false);
        cp9.setHeading("Структура финансирования проекта");
        VerticalLayoutContainer cp9V = new VerticalLayoutContainer();
        cp9V.add(financialStructureView, STD_VC_LAYOUT);
        cp9V.add(adsIsTreasurySupport, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(5, 0, 5, 0)));
        cp9V.add(decisionTextFileUpload, STD_VC_LAYOUT);
        cp9.add(cp9V, cpMargin);

        financialStructureView.getGrid().addViewReadyHandler(viewReadyEvent -> {
            if (financialStructureView.getStore().getAll().stream()
                    .anyMatch(ind -> ind.getValue() != null && ind.getValue() != 0))
                cp9.expand();
        });

        ContentPanel cp10 = new ContentPanel(appearance);
        cp10.setAnimCollapse(false);
        cp10.setHeading("Критерии качественных инфраструктурных инвестиций");
        VerticalLayoutContainer cp10V = new VerticalLayoutContainer();
        cp10V.add(investmentsCriteriaBooleanWidget, STD_VC_LAYOUT);
        cp10V.add(unforeseenExpencesLabel, STD_VC_LAYOUT);
        cp10V.add(workPlacesCountLabel, STD_VC_LAYOUT);
        cp10V.add(unforeseenExpencesCommentLabel, STD_VC_LAYOUT);
        cp10.add(cp10V, cpMargin);

        investmentsCriteriaBooleanWidget.getGrid().addViewReadyHandler(viewReadyEvent -> {
            if (investmentsCriteriaBooleanWidget.getStore().getAll().stream()
                    .anyMatch(ind -> ind.getComment() != null && !"".equals(ind.getComment())
                            || ind.getValue()))
                cp10.expand();

            boolean firstValue = investmentsCriteriaBooleanWidget.getStoreItemByGid(1L).getValue();
            if (firstValue)
                workPlacesCountLabel.show();
            else
                workPlacesCountLabel.hide();
        });

        investmentsCriteriaBooleanWidget.getStore().addStoreUpdateHandler(storeUpdateEvent -> {
            boolean firstValue = investmentsCriteriaBooleanWidget.getStoreItemByGid(1L).getValue();
            if (firstValue)
                workPlacesCountLabel.show();
            else
                workPlacesCountLabel.hide();
        });

        cp11 = new ContentPanel(appearance);
        cp11.setAnimCollapse(false);
        cp11.setHeading("Критерии конкурса");
        VerticalLayoutContainer cp11V = new VerticalLayoutContainer();
        cp11V.add(competitionCriteriaLabel, STD_VC_LAYOUT);
        cp11V.add(financialRequirementLabel, STD_VC_LAYOUT);
        cp11V.add(nonFinancialRequirement, STD_VC_LAYOUT);
        cp11.add(cp11V, cpMargin);

        cp12 = new ContentPanel(appearance);
        cp12.setAnimCollapse(false);
        cp12.setHeading("Льготы для частного партнера");
        VerticalLayoutContainer cp12V = new VerticalLayoutContainer();
        cp12V.add(incomeTaxRow, HEIGHT60_VC_LAYOUT);
        cp12V.add(landTaxRow, HEIGHT60_VC_LAYOUT);
        cp12V.add(propertyTaxRow, HEIGHT60_VC_LAYOUT);
        cp12V.add(benefitClarificationRow, HEIGHT60_VC_LAYOUT);
//        cp12V.add(createFieldLabelTop(privatePartnersOwningPartsView.getEgrulRoAddressLabel(), "Адрес (место нахождения) ЮЛ/ИП"), STD_VC_LAYOUT);
        cp12.add(cp12V, cpMargin);

        AccordionLayoutContainer accordion = new AccordionLayoutContainer();
        accordion.setExpandMode(AccordionLayoutContainer.ExpandMode.MULTI);
        accordion.add(cp1);
//        accordion.add(cp2);
//        accordion.add(cp3);
        accordion.add(cp4);
        accordion.add(cp5);
        accordion.add(cp6);
        accordion.add(cp7);
        accordion.add(cp8);
        accordion.add(cp9);
        accordion.add(cp10);
        accordion.add(cp11);
        accordion.add(cp12);

        accordion.setActiveWidget(cp1);
//        cp2.collapse();
//        cp3.collapse();
        cp4.collapse();
        cp5.collapse();
        cp6.collapse();
        cp7.collapse();
        cp8.collapse();
        cp9.collapse();
        cp10.collapse();
        cp11.collapse();
        cp12.collapse();

        container.add(accordion, STD_VC_LAYOUT);

        adsIsThirdPartyOrgsProvided.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
//                    cp2.show();
//                    cp3.show();
                } else {
//                    cp2.hide();
//                    cp3.hide();
                    privateOrganizationsInvolvementView.getGrid().getStore().clear();
                    publicOrganizationsInvolvementView.getGrid().getStore().clear();
                }
            }
        });
        // на новом экране сразу скрываем, т.к. галки нет.
//        cp2.hide();
//        cp3.hide();

        adsIsTreasurySupport.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    decisionTextFileUpload.toggle(true);
                    container.forceLayout();
                } else {
                    decisionTextFileUpload.toggle(false);
                    decisionTextFileUpload.setFiles(new ArrayList<>());
                }
            }
        });

        // на новом экране сразу скрываем, т.к. галки нет.
        decisionTextFileUpload.toggle(false);
    }

    @Override
    public Widget asWidget() {
        return container;
    }

    /**
     * Обновить форму (скрыть + очистить/показать поля) в зависимости от формы реализации проекта
     * и способа инициации проекта.
     *
     * @param formId - форма реализации проекта
     * @param initId - способ инициации проекта
     */

    private static ViewAttrState viewAttrState;

    static {
        viewAttrState = new ViewAttrState();

        viewAttrState.getColumns().addAll(ViewAttrStateColumns.getFormIds());

        viewAttrState.getState().put("adsIsThirdPartyOrgsProvided", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("decisionTextFileUpload", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("competitionCriteria", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("financialRequirement", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("nonFinancialRequirement", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("adsIsRegInvestmentProject", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("hasIncomeTax", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("hasLandTax", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("hasPropertyTax", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("hasBenefitClarificationTax", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("benefitDescription", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("opf", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("regimeType", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("creditRating", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("creditRatingSetDate", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("creditRatingReviewDate", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("creditRatingAgency", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("adsIsTreasurySupport", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("unforeseenExpences", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be
        viewAttrState.getState().put("unforeseenExpencesComment", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)); // TODO - can be


    }

    public void update(Long formId, Long initId) {

    }
}
