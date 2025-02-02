package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
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
import ru.ibs.gasu.common.models.FinancialIndicatorModel;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.models.view_attr.ViewAttrState;
import ru.ibs.gasu.common.models.view_attr.ViewAttrStateColumns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sencha.gxt.cell.core.client.form.TextAreaInputCell.Resizable.VERTICAL;
import static ru.ibs.gasu.client.widgets.WidgetUtils.*;

/**
 * Раздел "Прекращение"
 */
public class TerminationView implements IsWidget {

    private VerticalLayoutContainer container;
    private TwinTriggerComboBox<SimpleIdNameModel> cause;

    private DateField actPlanDate;
    private DateField actFactDate;
    private HorizontalLayoutContainer actDateRow;
    private VerticalLayoutContainer actDateContainer;

    private DateField planDate;
    private DateField factDate;
    private HorizontalLayoutContainer planFactRow;
    private VerticalLayoutContainer planFactContainer;

    private TextField actNumber;
    private DateField actDate;
    private FileUploader actTextFileUpload;
    private TextArea causeDescription;
    private FileUploader taActFileUpload;

    private CheckBox propertyJointProvided;
    private VerticalLayoutContainer propertyJointProvidedContainer;

    private DoubleField tmPropertyJointPrivatePercent;
    private DoubleField tmPropertyJointPublicPercent;
    private HorizontalLayoutContainer propertyJointsRow;

    private CheckBox compensationPayed;

    private BigDecimalField compensationValue;
    private VerticalLayoutContainer compensationValueContainer;

    private FileUploader compensationTextFileUpload;

    private ComboBox<SimpleIdNameModel> aftermath;
    private VerticalLayoutContainer aftermathContainer;
    private VerticalLayoutContainer tmCompositionOfCompensationGrantorFaultContainer;
    private VerticalLayoutContainer compositionOfCompensationViewContainer;

    private TextField terminationActNumber;
    private DateField terminationActDate;
    private TextField projectId;
    private HorizontalLayoutContainer terminationActRow;

    private DoubleField publicSideShare;
    private VerticalLayoutContainer publicSideShareContainer;

    private CheckBox rfShare;

    private TextField tmAnotherDescription;
    private TextField tmClausesOfAgreement;
    private CheckBox tmCompensationLimit;
    private CheckBox tmAgreementTerminated;
    private DoubleField tmCompensationSum;
    private VerticalLayoutContainer tmCompensationSumContainer;

    private CheckBox tmNdsCheck;
    private ComboBox<SimpleIdNameModel> tmMeasureType;
    private DateField tmDateField;
    private HorizontalLayoutContainer treeHRow;

    private DoubleField tmCompositionOfCompensation;
    private FileUploader tmSupportingDocuments;
    private MultiSelectComboBox<SimpleIdNameModel> tmCompositionOfCompensationGrantorFault;
    private FieldLabel tmCompositionOfCompensationLabel;
    private FieldLabel causeLabel;
    private FieldLabel tmAnotherDescriptionLabel;
    private FieldLabel tmClausesOfAgreementLabel;
    private CompositionOfCompensationWidget compositionOfCompensationView;
    private List<FinancialIndicatorModel> indicatorModels;
    private boolean compensationPayedHidenForOtherForms;

    public VerticalLayoutContainer getContainer() {
        return container;
    }

    public TwinTriggerComboBox<SimpleIdNameModel> getCause() {
        return cause;
    }

    public DateField getActPlanDate() {
        return actPlanDate;
    }

    public DateField getActFactDate() {
        return actFactDate;
    }

    public TextField getActNumber() {
        return actNumber;
    }

    public DateField getActDate() {
        return actDate;
    }

    public FileUploader getActTextFileUpload() {
        return actTextFileUpload;
    }

    public TextArea getCauseDescription() {
        return causeDescription;
    }

    /**
     * Дата прекращения права владения - факт
     *
     * @return
     */
    public DateField getPlanDate() {
        return planDate;
    }

    /**
     * Дата прекращения права владения - факт
     *
     * @return
     */
    public DateField getFactDate() {
        return factDate;
    }

    public FileUploader getTaActFileUpload() {
        return taActFileUpload;
    }

    public CheckBox getPropertyJointProvided() {
        return propertyJointProvided;
    }

    public DoubleField getTmPropertyJointPrivatePercent() {
        return tmPropertyJointPrivatePercent;
    }

    public DoubleField getTmPropertyJointPublicPercent() {
        return tmPropertyJointPublicPercent;
    }

    public CheckBox getCompensationPayed() {
        return compensationPayed;
    }

    public BigDecimalField getCompensationValue() {
        return compensationValue;
    }

    public FileUploader getCompensationTextFileUpload() {
        return compensationTextFileUpload;
    }

    public ComboBox<SimpleIdNameModel> getAftermath() {
        return aftermath;
    }

    public TextField getTerminationActNumber() {
        return terminationActNumber;
    }

    public DateField getTerminationActDate() {
        return terminationActDate;
    }

    public TextField getProjectId() {
        return projectId;
    }

    public DoubleField getPublicSideShare() {
        return publicSideShare;
    }

    public CheckBox getRfShare() {
        return rfShare;
    }

    public TextField getTmAnotherDescription() {
        return tmAnotherDescription;
    }

    public TextField getTmClausesOfAgreement() {
        return tmClausesOfAgreement;
    }

    public CheckBox getTmCompensationLimit() {
        return tmCompensationLimit;
    }

    public CheckBox getTmAgreementTerminated() {
        return tmAgreementTerminated;
    }

    public DoubleField getTmCompensationSum() {
        return tmCompensationSum;
    }

    public CheckBox getTmNdsCheck() {
        return tmNdsCheck;
    }

    public ComboBox<SimpleIdNameModel> getTmMeasureType() {
        return tmMeasureType;
    }

    public DateField getTmDateField() {
        return tmDateField;
    }

    public DoubleField getTmCompositionOfCompensation() {
        return tmCompositionOfCompensation;
    }

    public FileUploader getTmSupportingDocuments() {
        return tmSupportingDocuments;
    }

    public MultiSelectComboBox<SimpleIdNameModel> getTmCompositionOfCompensationGrantorFault() {
        return tmCompositionOfCompensationGrantorFault;
    }

    public CompositionOfCompensationWidget getCompositionOfCompensationView() {
        return compositionOfCompensationView;
    }

    public TerminationView() {
        initWidget();
    }

    private void initWidget() {
        container = new VerticalLayoutContainer();
        cause = createCommonFilterModelTwinTriggerComboBox("Выберите причину прекращения действия соглашения");
        causeLabel = createFieldLabelTop(cause, "Причина прекращения");

        // план теперь не используется
        actPlanDate = new DateFieldFullDate();
        FieldLabel actPlanDateLabel = createFieldLabelTop(actPlanDate, "план");
        actPlanDateLabel.setId("actPlanDate");

        actFactDate = new DateFieldFullDate();
        FieldLabel actFactDateLabel = createFieldLabelTop(actFactDate, "факт");
        actFactDateLabel.setId("actFactDate");
        actDateRow = createTwoFieldWidgetsRow(actPlanDateLabel, actFactDateLabel);
        actDateContainer = new VerticalLayoutContainer();
        actDateContainer.add(createHtml("Дата прекращения соглашения"));
        actDateContainer.add(actFactDate, STD_VC_LAYOUT);

        // теперь их нет
        actNumber = new TextField();
        FieldLabel actNumberLabel = createFieldLabelTop(actNumber, "Номер соглашения");
        actDate = new DateFieldFullDate();
        FieldLabel actDateLabel = createFieldLabelTop(actDate, "Дата соглашения");
        HorizontalLayoutContainer actNumDateRow = createTwoFieldWidgetsRow(actNumberLabel, actDateLabel);
        indicatorModels = new ArrayList<>();

        actTextFileUpload = new FileUploader();
        actTextFileUpload.setHeadingText("Документ о прекращении соглашения");

        causeDescription = new TextArea();
        causeDescription.setResizable(VERTICAL);
//        causeDescription.setHeight(100);
        FieldLabel causeDescriptionLabel = createFieldLabelTop(causeDescription, "Описание причины расторжения");

        // план больше не используется
        planDate = new DateFieldFullDate();
        FieldLabel planDateLabel = createFieldLabelTop(planDate, "план");
        planDateLabel.setId("planDate");
        factDate = new DateFieldFullDate();
        FieldLabel factDateLabel = createFieldLabelTop(factDate, "факт");
        factDateLabel.setId("factDate");
        planFactRow = createTwoFieldWidgetsRow(planDateLabel, factDateLabel);
        planFactContainer = new VerticalLayoutContainer();
        planFactContainer.add(createHtml("Дата прекращения права владения и пользования у частной стороны"));
        planFactContainer.add(factDateLabel, STD_VC_LAYOUT);

        taActFileUpload = new FileUploader();
        taActFileUpload.setHeadingText("Основание (акт приема-передачи)");

        propertyJointProvided = new CheckBox();
        propertyJointProvided.setBoxLabel(wrapString("Предусмотрено разделение права собственности на объект соглашения"));
        propertyJointProvidedContainer = new VerticalLayoutContainer();
        propertyJointProvidedContainer.add(propertyJointProvided, STD_VC_LAYOUT);
        propertyJointProvidedContainer.add(createHtml("Разделение права сообственности на объект соглашения"));

        tmPropertyJointPrivatePercent = new DoubleField();
        FieldLabel tmPropertyJointPrivatePercentLabel = createFieldLabelTop(tmPropertyJointPrivatePercent, "% собственности частной стороне");
        tmPropertyJointPrivatePercentLabel.setId("tmPropertyJointPrivatePercent");
        tmPropertyJointPublicPercent = new DoubleField();
        FieldLabel tmPropertyJointPublicPercentLabel = createFieldLabelTop(tmPropertyJointPublicPercent, "% сообственности публичной стороне");
        tmPropertyJointPublicPercentLabel.setId("tmPropertyJointPublicPercent");
        propertyJointsRow = createTwoFieldWidgetsRow(tmPropertyJointPrivatePercentLabel, tmPropertyJointPublicPercentLabel);

        compensationPayed = new CheckBox();
        compensationPayed.setBoxLabel(wrapString("Произведена компенсация концессионеру/частному партнеру при прекращении соглашения"));

        compensationValue = new BigDecimalField();
        FieldLabel compensationValueLabel = createFieldLabelTop(compensationValue, "Сумма компенсации");
        compensationValueContainer = new VerticalLayoutContainer();
        compensationValueContainer.add(compensationValueLabel, STD_VC_LAYOUT);

        compensationTextFileUpload = new FileUploader();
        compensationTextFileUpload.setHeadingText("Обосновывающие документы");

        aftermath = createCommonFilterModelComboBox("Выберите последствия прекращения договора аренды");
        FieldLabel aftermathLabel = createFieldLabelTop(aftermath, "Последствия прекращения договора");
        aftermathContainer = new VerticalLayoutContainer();
        aftermathContainer.add(aftermathLabel, STD_VC_LAYOUT);

        terminationActNumber = new TextField();
        FieldLabel terminationActNumberLabel = createFieldLabelTop(terminationActNumber, "Номер договора");
        terminationActNumberLabel.setId("terminationActNumber");
        terminationActDate = new DateFieldFullDate();
        FieldLabel terminationActDateLabel = createFieldLabelTop(terminationActDate, "Дата договора");
        terminationActDateLabel.setId("terminationActDate");
        projectId = new TextField();
        FieldLabel projectIdLabel = createFieldLabelTop(projectId, "ID проекта");
        projectIdLabel.setId("projectId");
        terminationActRow = createThreeFieldWidgetsRow(terminationActNumberLabel, terminationActDateLabel, projectIdLabel);

        publicSideShare = new DoubleField();
        FieldLabel publicSideShareLabel = createFieldLabelTop(publicSideShare, "Доля публичной стороны в капитале совместного юридического лица (совместного предприятия), реализующего проект, после реализации проекта, %");
        publicSideShareContainer = new VerticalLayoutContainer();
        publicSideShareContainer.add(publicSideShareLabel, STD_VC_LAYOUT);
        rfShare = new CheckBox();
        rfShare.setBoxLabel("Долей в совместном предприятии, реализующем проект, прямо/косвенно владеет Российская Федерация");

        tmAnotherDescription = new TextField();
        tmAnotherDescription.setName("Описание иного вида компенсации");
        tmAnotherDescription.addValidator(new MaxLengthValidator(255));
        tmAnotherDescriptionLabel = createFieldLabelTop(tmAnotherDescription, "Описание иного вида компенсации");
        tmClausesOfAgreement = new TextField();
        tmClausesOfAgreement.setName("Пункт (ы) соглашения");
        tmClausesOfAgreement.addValidator(new MaxLengthValidator(255));
        tmClausesOfAgreementLabel = createFieldLabelTop(tmClausesOfAgreement, "Пункт (ы) соглашения");
        tmCompensationLimit = new CheckBox();
        tmCompensationLimit.setBoxLabel(wrapString("Установлен предел компенсации концессионеру/частному партнеру при прекращении соглашения"));
        tmAgreementTerminated = new CheckBox();
        tmAgreementTerminated.setBoxLabel(wrapString("Соглашение прекращено"));
        tmCompensationSum = new DoubleField();
        tmCompensationSumContainer = new VerticalLayoutContainer();
        FieldLabel tmCompensationSumLabel = createFieldLabelTop(tmCompensationSum, "Предельная сумма компенсации концессионеру/частному  при прекращении соглашения, тыс.руб");
        tmCompensationSumContainer.add(tmCompensationSumLabel, STD_VC_LAYOUT);

        tmNdsCheck = new CheckBox();
        tmNdsCheck.setBoxLabel("Включая НДС");

        tmMeasureType = WidgetUtils.createCommonFilterModelComboBox("Выберите тип измерения");
        tmMeasureType.getStore().add(new SimpleIdNameModel("1", "В ценах соответствующих лет"));
        tmMeasureType.getStore().add(new SimpleIdNameModel("2", "На дату"));
        tmDateField = new DateFieldFullDate();

        tmMeasureType.addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                if ("2".equals(event.getSelectedItem().getId())) {
                    tmDateField.show();
                } else if ("1".equals(event.getSelectedItem().getId())) {
                    tmDateField.clear();
                    tmDateField.hide();
                }
            }
        });
        treeHRow = createThreeFieldWidgetsNdsDateRow(tmNdsCheck, tmMeasureType, tmDateField);
        tmCompensationSumContainer.add(treeHRow, HEIGHT60_VC_LAYOUT);

        tmCompositionOfCompensation = new DoubleField();
        tmCompositionOfCompensationLabel = createFieldLabelTop(tmCompositionOfCompensation, "Состав компенсации при прекращении соглашения");
        tmSupportingDocuments = new FileUploader();
        tmSupportingDocuments.setHeadingText(new SafeHtmlBuilder().appendHtmlConstant("Обосновывающие документы " +
                "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                "title='Документ, подтверждающий выплату в пользу концессионера / частного партнера'></i>").toSafeHtml());

        tmCompositionOfCompensationGrantorFault = WidgetUtils.createCommonFilterModelMultiSelectComboBox("Состав компенсации");
        FieldLabel tmCompositionOfCompensationGrantorFaultLabel = createFieldLabelTop(tmCompositionOfCompensationGrantorFault,
                "Состав компенсации при прекращении соглашения по вине концедента/публичного партнера в связи с наступлением особого обстоятельства/обстоятельства непреодолимой силы");
        tmCompositionOfCompensationGrantorFaultContainer = new VerticalLayoutContainer();
        tmCompositionOfCompensationGrantorFaultContainer.add(tmCompositionOfCompensationGrantorFaultLabel, STD_VC_LAYOUT);

        compositionOfCompensationView = new CompositionOfCompensationWidget();
        compositionOfCompensationViewContainer = new VerticalLayoutContainer();
        compositionOfCompensationViewContainer.add(compositionOfCompensationView, STD_VC_LAYOUT);

        AccordionLayoutContainer.AccordionLayoutAppearance appearance = GWT.<AccordionLayoutContainer.AccordionLayoutAppearance>create(AccordionLayoutContainer.AccordionLayoutAppearance.class);
        MarginData cpMargin = new MarginData(10, 10, 10, 20);

        ContentPanel cp1 = new ContentPanel(appearance);
        cp1.setAnimCollapse(false);
        cp1.setHeading("Общая информация");
        VerticalLayoutContainer cp1V = new VerticalLayoutContainer();
//        cp1V.add(causeDescriptionLabel, STD_VC_LAYOUT);
//        cp1V.add(planFactContainer, STD_VC_LAYOUT);
//        cp1V.add(taActFileUpload, STD_VC_LAYOUT);
//        cp1V.add(propertyJointProvidedContainer);
//        cp1V.add(propertyJointsRow, HEIGHT60_VC_LAYOUT);
//        cp1V.add(compensationValueContainer);
//        cp1V.add(compensationTextFileUpload, STD_VC_LAYOUT);
//        cp1V.add(aftermathContainer);
//        cp1V.add(terminationActRow, HEIGHT60_VC_LAYOUT);
//        cp1V.add(publicSideShareContainer);
//        cp1V.add(rfShare, STD_VC_LAYOUT);
        cp1V.add(tmCompositionOfCompensationGrantorFaultContainer, STD_VC_LAYOUT); //1
        cp1V.add(tmAnotherDescriptionLabel, STD_VC_LAYOUT); //2
        cp1V.add(tmClausesOfAgreementLabel, STD_VC_LAYOUT); //3
        cp1V.add(tmCompensationLimit, STD_VC_LAYOUT); //4
        cp1V.add(tmCompensationSumContainer, STD_VC_LAYOUT); //5
        cp1V.add(tmAgreementTerminated, STD_VC_LAYOUT); //6
        cp1V.add(causeLabel, STD_VC_LAYOUT); //7
        cp1V.add(actDateContainer); //8
        cp1V.add(actTextFileUpload, STD_VC_LAYOUT); //9
        cp1V.add(compensationPayed, new VerticalLayoutContainer.VerticalLayoutData(1, 30)); //10
        cp1V.add(compositionOfCompensationViewContainer, STD_VC_LAYOUT); //11
        cp1V.add(tmSupportingDocuments, STD_VC_LAYOUT);//12
        cp1.add(cp1V, cpMargin);

        AccordionLayoutContainer accordion = new AccordionLayoutContainer();
        accordion.setExpandMode(AccordionLayoutContainer.ExpandMode.MULTI);
        accordion.add(cp1);
        accordion.setActiveWidget(cp1);

        container.add(accordion, STD_VC_LAYOUT);

        tmAgreementTerminated.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    if(!compensationPayedHidenForOtherForms){
                        if(compensationPayed.getValue() != null && compensationPayed.getValue()){
                            compositionOfCompensationViewContainer.show();
                        }
                        tmSupportingDocuments.toggle(true);
                        compensationPayed.show();
                    }
                    actTextFileUpload.toggle(true);
                    causeLabel.show();
                    actDateContainer.show();
                    container.forceLayout();
                } else {
                    compositionOfCompensationViewContainer.hide();
                    tmSupportingDocuments.toggle(false);
                    actTextFileUpload.toggle(false);
                    causeLabel.hide();
                    actDateContainer.hide();
                    compensationPayed.hide();
                }
            }
        });

        compensationPayed.addValueChangeHandler(new ValueChangeHandler<Boolean>(){
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if(event.getValue() != null && event.getValue()){
                    compositionOfCompensationViewContainer.show();
                } else {
                    compositionOfCompensationViewContainer.hide();
                }
            }
        });

        tmCompositionOfCompensationGrantorFault.addSelectionHandler(selectedItems -> {
            indicatorModels.clear();
            boolean modelAdded;
            for (SimpleIdNameModel simpleIdNameModel : selectedItems){
                modelAdded = false;
                for(FinancialIndicatorModel financialIndicatorModel : compositionOfCompensationView.getStore().getAll()){
                    if (financialIndicatorModel.getGid().equals(Long.valueOf(simpleIdNameModel.getId()))){
                        indicatorModels.add(financialIndicatorModel);
                        modelAdded = true;
                    }
                }
                if(!modelAdded){
                    FinancialIndicatorModel financialIndicatorModel = new FinancialIndicatorModel();
                    financialIndicatorModel.setGid(Long.valueOf(simpleIdNameModel.getId()));
                    financialIndicatorModel.setName(simpleIdNameModel.getName());
                    indicatorModels.add(financialIndicatorModel);
                }
            }
            if(selectedItems.stream().anyMatch(item->item.getId().equals("6"))){
                tmAnotherDescription.show();
                tmAnotherDescriptionLabel.show();
            } else {
                tmAnotherDescription.hide();
                tmAnotherDescriptionLabel.hide();
            }
            compositionOfCompensationView.getStore().replaceAll(indicatorModels);
        });

        compensationValueContainer.hide();
        compensationTextFileUpload.toggle(false);
        compensationValue.setValue(null);
        compensationTextFileUpload.setFiles(new ArrayList<>());

        tmCompensationLimit.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue() != null && event.getValue()) {
                    compensationValueContainer.show();
                    tmCompensationSumContainer.show();
                    compensationTextFileUpload.toggle(true);
                    container.forceLayout();
                } else {
                    compensationValueContainer.hide();
                    tmCompensationSumContainer.hide();
                    compensationTextFileUpload.toggle(false);
                    compensationValue.setValue(null);
                    compensationTextFileUpload.setFiles(new ArrayList<>());
                }
            }
        });

    }

    @Override
    public Widget asWidget() {
        return container;
    }

    private static ViewAttrState viewAttrState;

    static {
        viewAttrState = new ViewAttrState();

        viewAttrState.getColumns().addAll(ViewAttrStateColumns.getFormIds());

        viewAttrState.getState().put("cause", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("actPlanDate", Arrays.asList(1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("actFactDate", Arrays.asList(1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("actNumber", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("actDate", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("actTextFileUpload", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("causeDescription", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("7.8_duplicate_of_7.6", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)); // TODO - can be missed, always show
        viewAttrState.getState().put("planDate", Arrays.asList(1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("taActFileUpload", Arrays.asList(1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("factDate", Arrays.asList(1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("7.12_duplicate_of_7.10", Arrays.asList(1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("propertyJointProvided", Arrays.asList(0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("tmPropertyJointPrivatePercent", Arrays.asList(0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("tmPropertyJointPublicPercent", Arrays.asList(0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("compensationPayed", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("compensationValue", Arrays.asList(1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("compensationTextFileUpload", Arrays.asList(0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("aftermath", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("terminationActNumber", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("terminationActDate", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("projectId", Arrays.asList(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("publicSideShare", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0));
        viewAttrState.getState().put("rfShare", Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0));
        viewAttrState.getState().put("tmAgreementTerminated", Arrays.asList(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1));
        viewAttrState.getState().put("tmAnotherDescription", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("tmClausesOfAgreement", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("tmCompensationLimit", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("tmCompensationSum", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("tmCompositionOfCompensation", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("tmSupportingDocuments", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("tmCompositionOfCompensationGrantorFault", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
        viewAttrState.getState().put("composition", Arrays.asList(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
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

        if (viewAttrState.isAttrActive(formIdStr, "actFactDate")) {
            actDateContainer.show();
        } else {
            actDateContainer.hide();
        }

        if (viewAttrState.isAttrActive(formIdStr, "factDate")) {
            planFactContainer.show();
        } else {
            planFactContainer.hide();
        }

        // file taActFileUpload
        if (viewAttrState.isAttrActive(formIdStr, "taActFileUpload")) {
            taActFileUpload.toggle(true);
        } else {
            taActFileUpload.toggle(false);
            taActFileUpload.setFiles(new ArrayList<>());
        }

        if (viewAttrState.isAttrActive(formIdStr, "tmSupportingDocuments")) {
            tmSupportingDocuments.toggle(true);
        } else {
            tmSupportingDocuments.toggle(false);
            tmSupportingDocuments.setFiles(new ArrayList<>());
        }

        // checkbox propertyJointProvided
        if (viewAttrState.isAttrActive(formIdStr, "propertyJointProvided")) {
            propertyJointProvidedContainer.show();
        } else {
            propertyJointProvidedContainer.hide();
            propertyJointProvided.setValue(null);
        }

        // 2 columns double tm property joint percent
        boolean isTmPropertyJointPrivatePercent = viewAttrState.isAttrActive(formIdStr, "tmPropertyJointPrivatePercent");
        if (isTmPropertyJointPrivatePercent) {
            toggleHorizontalLayoutContainerColumn(propertyJointsRow, "tmPropertyJointPrivatePercent", true);
        } else {
            toggleHorizontalLayoutContainerColumn(propertyJointsRow, "tmPropertyJointPrivatePercent", false);
            tmPropertyJointPrivatePercent.setValue(null);
        }
        boolean isTmPropertyJointPublicPercent = viewAttrState.isAttrActive(formIdStr, "tmPropertyJointPublicPercent");
        if (isTmPropertyJointPublicPercent) {
            toggleHorizontalLayoutContainerColumn(propertyJointsRow, "tmPropertyJointPublicPercent", true);
        } else {
            toggleHorizontalLayoutContainerColumn(propertyJointsRow, "tmPropertyJointPublicPercent", false);
            tmPropertyJointPublicPercent.setValue(null);
        }

        // checkbox compensationPayed
        if (viewAttrState.isAttrActive(formIdStr, "compensationPayed")) {
            compensationPayed.show();
            compensationPayedHidenForOtherForms = false;
        } else {
            compensationPayed.hide();
            compensationPayed.setValue(null);
            compensationPayedHidenForOtherForms= true;
        }

        // file compensationTextFileUpload
        if (viewAttrState.isAttrActive(formIdStr, "compensationTextFileUpload")) {
            compensationTextFileUpload.toggle(true);
            ValueChangeEvent.fire(compensationPayed, compensationPayed.getValue());
        } else {
            compensationTextFileUpload.toggle(false);
            compensationTextFileUpload.setFiles(new ArrayList<>());
        }

        // double compensationValue
        if (viewAttrState.isAttrActive(formIdStr, "compensationValue")) {
            compensationValueContainer.show();
            ValueChangeEvent.fire(compensationPayed, compensationPayed.getValue());
        } else {
            compensationValueContainer.hide();
            compensationValue.setValue(null);
        }

        // combobox aftermath
        if (viewAttrState.isAttrActive(formIdStr, "aftermath")) {
            aftermathContainer.show();
        } else {
            aftermathContainer.hide();
            aftermath.setValue(null);
        }

        // 3 columns doc data
        if (viewAttrState.isAttrActive(formIdStr, "terminationActNumber")) {
            toggleHorizontalLayoutContainerColumn(terminationActRow, "terminationActNumber", true);
        } else {
            toggleHorizontalLayoutContainerColumn(terminationActRow, "terminationActNumber", false);
            terminationActNumber.setValue(null);
        }
        if (viewAttrState.isAttrActive(formIdStr, "terminationActDate")) {
            toggleHorizontalLayoutContainerColumn(terminationActRow, "terminationActDate", true);
        } else {
            toggleHorizontalLayoutContainerColumn(terminationActRow, "terminationActDate", false);
            terminationActDate.setValue(null);
        }
        if (viewAttrState.isAttrActive(formIdStr, "projectId")) {
            toggleHorizontalLayoutContainerColumn(terminationActRow, "projectId", true);
        } else {
            toggleHorizontalLayoutContainerColumn(terminationActRow, "projectId", false);
            projectId.setValue(null);
        }

        // double publicSideShare
        if (viewAttrState.isAttrActive(formIdStr, "publicSideShare")) {
            publicSideShareContainer.show();
        } else {
            publicSideShareContainer.hide();
            publicSideShare.setValue(null);
        }

        // checkbox rfShare
        if (viewAttrState.isAttrActive(formIdStr, "rfShare")) {
            rfShare.show();
        } else {
            rfShare.hide();
            rfShare.setValue(null);
        }

        if (viewAttrState.isAttrActive(formIdStr, "composition")) {
            tmCompositionOfCompensationGrantorFaultContainer.show();
            tmAnotherDescription.show();
            tmClausesOfAgreement.show();
            tmCompensationLimit.show();
            tmCompensationSumContainer.show();
            tmAnotherDescriptionLabel.show();
            tmClausesOfAgreementLabel.show();
            compositionOfCompensationViewContainer.show();
            tmSupportingDocuments.toggle(true);
        } else {
            tmCompositionOfCompensationGrantorFaultContainer.hide();
            tmAnotherDescription.hide();
            tmClausesOfAgreement.hide();
            tmCompensationLimit.hide();
            tmCompensationSumContainer.hide();
            tmAnotherDescriptionLabel.hide();
            tmClausesOfAgreementLabel.hide();
            compositionOfCompensationViewContainer.hide();
            tmSupportingDocuments.toggle(false);
        }

        if (!tmAnotherDescriptionShow()) {
            tmAnotherDescription.hide();
            tmAnotherDescriptionLabel.hide();
        }

        if (tmAgreementTerminated.getValue() != null && tmAgreementTerminated.getValue()) {

            if (viewAttrState.isAttrActive(formIdStr, "compensationPayed")) {
                if(compensationPayed.getValue() != null && compensationPayed.getValue()){
                    compositionOfCompensationViewContainer.show();
                }else {
                    compositionOfCompensationViewContainer.hide();
                }
                tmSupportingDocuments.toggle(true);
                compensationPayed.show();
            }

            actTextFileUpload.toggle(true);
            causeLabel.show();
            actDateContainer.show();
            container.forceLayout();
        } else {
            compositionOfCompensationViewContainer.hide();
            tmSupportingDocuments.toggle(false);
            actTextFileUpload.toggle(false);
            causeLabel.hide();
            actDateContainer.hide();
            compensationPayed.hide();
        }

        if (tmCompensationLimit.getValue() != null && tmCompensationLimit.getValue()) {
            compensationValueContainer.show();
            tmCompensationSumContainer.show();
            compensationTextFileUpload.toggle(true);
            container.forceLayout();
        } else {
            compensationValueContainer.hide();
            tmCompensationSumContainer.hide();
            compensationTextFileUpload.toggle(false);
        }

        container.forceLayout();
    }

    public boolean tmAnotherDescriptionShow() {
        for (int i = 0; i < getTmCompositionOfCompensationGrantorFault().getSelectedItems().size(); i++) {
            if (getTmCompositionOfCompensationGrantorFault().getSelectedItems().get(i).getId().equals(String.valueOf(6))) {
                return true;
            }
        }
        return false;
    }
}

