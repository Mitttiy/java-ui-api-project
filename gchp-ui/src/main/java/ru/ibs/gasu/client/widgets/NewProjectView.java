package ru.ibs.gasu.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.theme.base.client.listview.ListViewCustomAppearance;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewSelectionModel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.*;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import ru.ibs.gasu.client.presenters.GchpNewDocumentPanelPresenter;
import ru.ibs.gasu.client.widgets.componens.IconButton;
import ru.ibs.gasu.common.models.Project;
import ru.ibs.gasu.common.models.SimpleIdNameModel;
import ru.ibs.gasu.common.soap.generated.gchpdicts.SimpleEgrulDomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class NewProjectView implements IsWidget, GchpNewDocumentPanelPresenter.Display {
    private Resource resources;
    private Renderer renderer;

    private Window w;
    private ListView<SimpleIdNameModel, SimpleIdNameModel> listView;
    private SimpleIdNameModel listViewSelectedItem;
    private CssFloatLayoutContainer container;

    private Project managedProject;
    private ProjectPassportView projectPassportView;
    private GeneralInformationView generalInformationView;
    private ObjectDescriptionView objectDescriptionView;
    private ProjectPreparationView projectPreparationView;
    private CreationView creationView;
    private FinancialAndEconomicIndicatorsView financialAndEconomicIndicatorsView;
    private ExploitationView exploitationView;
    private TerminationView terminationView;
    private ContingentBudgetaryCommitmentsView contingentBudgetaryCommitmentsView;
    private ConditionChangeView conditionChangeView;
    private ExtraView extraView;
    private EditionsView editionsView;
    private CommentsView commentsView;
    private IconButton fixButton;
    private boolean isMenuFixed = true;
    private IconButton nextButton;
    private IconButton saveButton;
    private IconButton signButton;
    private IconButton cancelButton;

    private VerticalLayoutContainer panelContainer;
    private VerticalLayoutContainer rcont;

    private final int INIT_WINDOW_MARGINS = 30;
    private final int STEPS_PANEL_WIDTH = 200;
    private final int BUTTON_WIDTH = 180;
    private final int BUTTON_PANEL_HEIGHT = 100;
    private final String REALIZATION_STATUS_ID_FOR_HIDE_STEP_VIEW = "3";
    private LinkedHashMap<String, IsWidget> stepViewBinding;
    private LinkedHashMap<String, String> iconsBinding;
    private final List<String> formRealzationIdsForHide = Arrays.asList("101", "151");

    public enum Mode {
        NEW, EDIT, VIEW
    }

    public void setManagedProject(Project managedProject) {
        this.managedProject = managedProject;
    }

    @Override
    public void show() {
        w.show();
    }

    @Override
    public void hideAllFiles() {
        generalInformationView.getCompletedTemplateFileUpload().toggle(false);
        objectDescriptionView.getFileUploader().toggle(false);

        projectPreparationView.getProjectAgreementFileUploader().toggle(false);
        projectPreparationView.getActFileUploader().toggle(false);
        projectPreparationView.getLeaseAgreementUploader().toggle(false);
        projectPreparationView.getDecisionFileUploader().toggle(false);
        projectPreparationView.getProposalTextFileUploader().toggle(false);
        projectPreparationView.getProtocolFileUploader().toggle(false);
        projectPreparationView.getConclusionFileUploader().toggle(false);
        projectPreparationView.getCompetitionTextFileUploader().toggle(false);
        projectPreparationView.getCompetitionResultsProtocolFileUploader().toggle(false);
        projectPreparationView.getCompetitionResultsDocFileUploader().toggle(false);

        creationView.getAgreementFileUpload().toggle(false);
        creationView.getAgreementTextFiles().toggle(false);
        creationView.getFinancialClosingFileUpload().toggle(false);
        creationView.getFirstObjectCompleteActFileUpload().toggle(false);
        creationView.getLastObjectCompleteActFileUpload().toggle(false);
        creationView.getInvestmentVolumeStagOfCreationFileUpload().toggle(false);
        creationView.getActFileUpload().toggle(false);
        creationView.getReferenceFileUpload().toggle(false);
        creationView.getLandActTextFileUpload().toggle(false);
        creationView.getConfirmationDocFileUpload().toggle(false);

        exploitationView.getLastObjectActFileUpload().toggle(false);
        exploitationView.getInvestmentVolumeStagOfExploitationFileUpload().toggle(false);
        exploitationView.getFinancialModelFileUpload().toggle(false);
        exploitationView.getExFinModelFVIds().toggle(false);
        exploitationView.getExSupportDocFVIds().toggle(false);
        exploitationView.getExSupportCompensDocFVIds().toggle(false);
        exploitationView.getExAgreementFVIds().toggle(false);
        exploitationView.getExAcceptActFVIds().toggle(false);
        exploitationView.getExAcceptActAAMFVIds().toggle(false);

        terminationView.getActTextFileUpload().toggle(false);
        terminationView.getTaActFileUpload().toggle(false);
        terminationView.getCompensationTextFileUpload().toggle(false);
        terminationView.getTmSupportingDocuments().toggle(false);

        conditionChangeView.getActFileUpload().toggle(false);

        extraView.getDecisionTextFileUpload().toggle(false);
    }

    @Override
    public void hide() {
        w.hide();
    }

    @Override
    public ProjectPassportView getProjectPassportView() {
        return projectPassportView;
    }

    @Override
    public GeneralInformationView getGeneralInformationView() {
        return generalInformationView;
    }

    @Override
    public ObjectDescriptionView getObjectDescriptionView() {
        return objectDescriptionView;
    }

    @Override
    public ProjectPreparationView getProjectPreparationView() {
        return projectPreparationView;
    }

    @Override
    public CreationView getCreationView() {
        return creationView;
    }

    @Override
    public FinancialAndEconomicIndicatorsView getFinancialAndEconomicIndicatorsView() {
        return financialAndEconomicIndicatorsView;
    }

    @Override
    public ExploitationView getExploitationView() {
        return exploitationView;
    }

    @Override
    public TerminationView getTerminationView() {
        return terminationView;
    }

    @Override
    public ContingentBudgetaryCommitmentsView getContingentBudgetaryCommitmentsView() {
        return contingentBudgetaryCommitmentsView;
    }

    @Override
    public ConditionChangeView getConditionChangeView() {
        return conditionChangeView;
    }

    @Override
    public ExtraView getExtraView() {
        return extraView;
    }

    @Override
    public EditionsView getEditionsView() {
        return editionsView;
    }

    @Override
    public CommentsView getCommentsView() {
        return commentsView;
    }

    @Override
    public TextButton getNextButton() {
        return nextButton;
    }

    @Override
    public TextButton getSaveButton() {
        return saveButton;
    }

    public TextButton getSignButton() {
        return signButton;
    }

    @Override
    public TextButton getCancelButton() {
        return cancelButton;
    }

    public void mask() {
        mask("Загрузка проекта и справочников...");
    }

    @Override
    public void mask(String message) {
        container.mask(message);
    }

    @Override
    public void unmask() {
        container.unmask();
    }

    @Override
    public void setHeader(String header) {
        w.setHeading(header);
    }

    public NewProjectView() {
        resources = GWT.create(Resource.class);
        renderer = GWT.create(Renderer.class);
        StyleInjectorHelper.ensureInjected(resources.style(), true);
        StyleInjectorHelper.ensureInjected(resources.navigationStyle(), true);

        container = new CssFloatLayoutContainer();
        container.getElement().getStyle().setBackgroundColor("white");
        panelContainer = new VerticalLayoutContainer();

        initWindow();
        initStepsViews();
        initStepViewBinding();
        initIcons();
        initStepsList();

        rcont = new VerticalLayoutContainer();
        rcont.setScrollMode(ScrollSupport.ScrollMode.AUTO);
        rcont.setAdjustForScroll(true);
        rcont.getElement().getStyle().setPaddingRight(10, Style.Unit.PX);

        BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
        borderLayoutContainer.getElement().getStyle().setBackgroundColor("white");

        borderLayoutContainer.setCenterWidget(rcont);

        fixButton = new IconButton("Открепить меню", "fas fa-thumbtack fa-rotate-90");
        fixButton.setWidth(BUTTON_WIDTH);
        fixButton.hide();

        nextButton = new IconButton("Далее", "fa-angle-double-right ");
        nextButton.setWidth(BUTTON_WIDTH);
        nextButton.hide();

        saveButton = new IconButton("Сохранить", "fa-save");
        saveButton.setWidth(BUTTON_WIDTH);

        signButton = new IconButton("Подписать и сохранить", "fa-file-signature");
        signButton.setWidth(BUTTON_WIDTH);

        cancelButton = new IconButton("Отмена", "fa-times");
        cancelButton.setWidth(BUTTON_WIDTH);

        HBoxLayoutContainer bc = new HBoxLayoutContainer();
        bc.setPack(BoxLayoutContainer.BoxLayoutPack.END);
        bc.add(fixButton, new HBoxLayoutContainer.BoxLayoutData(new Margins(10, 5, 0, 0)));
        bc.add(nextButton, new HBoxLayoutContainer.BoxLayoutData(new Margins(10, 5, 0, 0)));
        bc.add(saveButton, new HBoxLayoutContainer.BoxLayoutData(new Margins(10, 5, 0, 0)));
        bc.add(signButton, new HBoxLayoutContainer.BoxLayoutData(new Margins(10, 5, 0, 0)));
        bc.add(cancelButton, new HBoxLayoutContainer.BoxLayoutData(new Margins(10, 20, 0, 0)));

        borderLayoutContainer.setSouthWidget(bc, new BorderLayoutContainer.BorderLayoutData());

        panelContainer.add(borderLayoutContainer, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

        container.add(listView);
        container.add(panelContainer);

        w.add(container);

        fixButton.addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (!isMenuFixed) {
                    showMenu();
                    isMenuFixed = !isMenuFixed;
                } else {
                    isMenuFixed = false;
                    hideMenu();
                }
                fixButton.redraw();
                String icon = isMenuFixed ? "fas fa-thumbtack fa-rotate-90" : "fas fa-thumbtack fa-rotate-0";
                String text = isMenuFixed ? "Открепить меню" : "Закрепить меню";
                fixButton.setText(text, icon);
            }
        });

        showMenu();
    }

    private void initWindow() {
        w = new Window();
        w.setWidth(com.google.gwt.user.client.Window.getClientWidth() - 5);
        w.setHeight(windowHeight());
        w.setMinWidth(1000);
        w.setResizable(true);
        w.addResizeHandler(event -> panelContainer.setHeight(event.getHeight() - w.getHeader().getOffsetHeight() + 20));
    }

    private int windowHeight() {
        return com.google.gwt.user.client.Window.getClientHeight() - 5;
    }

    private void initStepsViews() {
        projectPassportView = new ProjectPassportView();
        generalInformationView = new GeneralInformationView();
        objectDescriptionView = new ObjectDescriptionView();
        projectPreparationView = new ProjectPreparationView();
        creationView = new CreationView();
        financialAndEconomicIndicatorsView = new FinancialAndEconomicIndicatorsView();
        exploitationView = new ExploitationView();
        terminationView = new TerminationView();
        contingentBudgetaryCommitmentsView = new ContingentBudgetaryCommitmentsView();
        conditionChangeView = new ConditionChangeView();
        extraView = new ExtraView();
        editionsView = new EditionsView();
        commentsView = new CommentsView();

//При выборе  Egrul вставляем значения в атрибуты раздела создание и Дополнительно

        creationView.getSelectEgrulButton().addSelectHandler(createEgrulButtonsEvent(false));
        creationView.getSelectEgripButton().addSelectHandler(createEgrulButtonsEvent(true));
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
                    creationView.getConcessionaire().setValue(egrulDomain.getShortName());
                    creationView.getConcessionaireInn().setValue(egrulDomain.getInn());

                    extraView.getPrivatePartnersOwningPartsView().setEgrulLabels(egrulDomain.getInn(), true);
                }
            }.show();
        };
    }

    interface Resource extends ClientBundle {
        @Source("stepspanel.gss")
        WidgetStyles style();

        @Source("NavigationStyle.gss")
        NavigationStyle navigationStyle();
    }

    interface Renderer extends XTemplates {
        @XTemplate(source = "NavigationItem.html")
        SafeHtml renderItem(String iconStyle, String title, NavigationStyle style, String disableStyle);
    }

    interface WidgetStyles extends CssResource {
        String itemOver();

        String itemSelected();

        String itemWrap();
    }

    private int currentStep = 0;
    private ListViewSelectionModel<SimpleIdNameModel> selectionModel;

    private void initStepsList() {
        ListStore<SimpleIdNameModel> store = new ListStore<>(SimpleIdNameModel::getId);
        ListViewCustomAppearance<SimpleIdNameModel> appearance = new ListViewCustomAppearance<SimpleIdNameModel>(
                "." + resources.navigationStyle().itemWrap(), resources.navigationStyle().itemOver(), resources.navigationStyle().itemSelected()) {
            @Override
            public void renderItem(SafeHtmlBuilder builder, SafeHtml content) {
                builder.appendHtmlConstant("<div class='" + resources.navigationStyle().itemWrap() + "'>");
                builder.append(content);
                addHintsToTitles(builder, content);
                builder.appendHtmlConstant("</div>");
            }

            @Override
            public void renderEnd(SafeHtmlBuilder builder) {
                String markup = "<div class=\"" +
                        CommonStyles.get().clear() + "\"></div>";
                builder.appendHtmlConstant(markup);
            }
        };
        listView = new ListView<>(store, new IdentityValueProvider<>(), appearance);

        listView.setCell(new SimpleSafeHtmlCell<>(new AbstractSafeHtmlRenderer<SimpleIdNameModel>() {
            @Override
            public SafeHtml render(SimpleIdNameModel object) {
                String disableStyle = /*isDisableBySeveralObject(object) ||*/ isDisableByAgreementSigned(object) || isDisableListViewElement(object) || isDisableListViewRealizationForm(object) || isDisableListViewFederalImplementationLvl(object) ? resources.navigationStyle().mainMenuDisableElement() + " " + resources.navigationStyle().disable() : "";
                return renderer.renderItem(iconsBinding.get(object.getId()), object.getName(), resources.navigationStyle(), disableStyle);
            }
        }));

        selectionModel = new ListViewSelectionModel<>();
        selectionModel.bindList(listView);
        selectionModel.addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                if (isDisableListViewElement(event.getSelectedItem())) {
                    selectionModel.select(listViewSelectedItem, true);
                    selectionModel.deselect(event.getSelectedItem());
                    new MessageBox("Внимание", "В общих сведениях выбрана стадия реализации 'Подготовка проекта'. Редактирование раздела '" + event.getSelectedItem().getName() + "' закрыто.").show();
                    return;
                }

                if (isDisableByAgreementSigned(event.getSelectedItem())) {
                    selectionModel.select(listViewSelectedItem, true);
                    selectionModel.deselect(event.getSelectedItem());
                    new MessageBox("Внимание", "В разделе 'Подготовка проекта' не установлен флаг 'Соглашение подписано'. Редактирование раздела '" + event.getSelectedItem().getName() + "' закрыто.").show();
                    return;
                }

//                if (isDisableListViewRealizationForm(event.getSelectedItem())) {
//                    selectionModel.select(listViewSelectedItem, true);
//                    selectionModel.deselect(event.getSelectedItem());
//                    new MessageBox("Внимание", "В общих сведениях выбрана форма реализации " + generalInformationView.getRealizationForm().getValue().getName() + ". Редактирование раздела '" + event.getSelectedItem().getName() + "' закрыто.").show();
//                    return;
//                }
//                if (isDisableListViewFederalImplementationLvl(event.getSelectedItem())) {
//                    selectionModel.select(listViewSelectedItem, true);
//                    selectionModel.deselect(event.getSelectedItem());
//                    new MessageBox("Внимание", "В общих сведениях выбран уровень реализации " + generalInformationView.getImplementationLvl().getValue().getName() + ". Редактирование раздела '" + event.getSelectedItem().getName() + "' закрыто.").show();
//                    return;
//                }
                listViewSelectedItem = event.getSelectedItem();
                rcont.clear();
                rcont.add(stepViewBinding.get(event.getSelectedItem().getName()),
                        new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(0, 10, 0, 0)));
                rcont.forceLayout();
            }
        });
        listView.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent event) {
                selectionModel.select(currentStep, false);
            }
        });


        listView.setStyleName(resources.navigationStyle().mainMenu());
        panelContainer.setStyleName(resources.navigationStyle().mainDisplay());

        listView.addDomHandler(event -> showMenu(), MouseOverEvent.getType());
        listView.addDomHandler(event -> hideMenu(), MouseOutEvent.getType());

        generalInformationView.getRealizationStatus().addSelectionHandler(event -> {
            redrawListView();
        });
        generalInformationView.getRealizationStatus().addValueChangeHandler(event -> {
            redrawListView();
        });

        generalInformationView.getRealizationForm().addSelectionHandler(event -> {
            redrawListView();
        });

        generalInformationView.getRealizationForm().addValueChangeHandler(event -> {
            redrawListView();
        });

        generalInformationView.getInitMethod().addValueChangeHandler(event -> {
            redrawListView();
        });

        generalInformationView.getInitMethod().addSelectionHandler(event -> {
            redrawListView();
        });

        projectPreparationView.getIsAgreementSigned().addValueChangeHandler(event -> {
            redrawListView();
        });

//        creationView.getIsFirstSeveralObject().addValueChangeHandler(event -> {
//            redrawListView();
//        });
//
//        creationView.getIsFirstSeveralObject().addBeforeHideHandler(event -> {
//            redrawListView();
//        });

        creationView.getIsSeveralObjects().addValueChangeHandler(event -> {
            redrawListView();
        });

        creationView.getIsSeveralObjectInOperation().addValueChangeHandler(event -> {
            redrawListView();
        });

        generalInformationView.getImplementationLvl().addSelectionHandler(event -> redrawListView());
    }

    private void redrawListView() {
        listView.getStore().fireEvent(new StoreDataChangeEvent());
    }

    private boolean isDisableBySeveralObject(SimpleIdNameModel object) {
        if (creationView.getIsFirstSeveralObject() != null
                && !creationView.getIsFirstSeveralObject().getValue()
                && creationView.getIsSeveralObjectInOperation() != null
                && !creationView.getIsSeveralObjectInOperation().getValue()) {
            return object.getName().equals("Эксплуатация");
        }
        return false;
    }

    private boolean isDisableListViewElement(SimpleIdNameModel object) {
        //если выбран элемент комбобокса
        if (generalInformationView.getRealizationStatus().getValue() != null) {
            //если этот элемент равен «Подготовка  проекта»
            if (generalInformationView.getRealizationStatus().getCurrentValue().getId().equals(REALIZATION_STATUS_ID_FOR_HIDE_STEP_VIEW)) {
                //если объект для отрисовки имеет имя "Создание" или "Эксплуатация"
                if (object.getName().equals("Создание") || object.getName().equals("Эксплуатация")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDisableByAgreementSigned(SimpleIdNameModel object) {
        if (!projectPreparationView.getIsAgreementSigned().getValue()
                && generalInformationView.getInitMethod() != null
                && ((generalInformationView.getInitMethod().getCurrentValue() != null
                && !generalInformationView.getInitMethod().getCurrentValue().getId().equals("201"))
                || generalInformationView.getInitMethod().getCurrentValue() == null)
        ) {
            return object.getName().equals("Создание") || object.getName().equals("Эксплуатация");
        }
        return false;
    }

    private boolean isDisableListViewRealizationForm(SimpleIdNameModel object) {
        if (generalInformationView.getRealizationForm() != null
                && generalInformationView.getRealizationForm().getValue() != null) {
            //если выбраны Иные формы реализации
            if (!formRealzationIdsForHide.contains(generalInformationView.getRealizationForm().getCurrentValue().getId())) {
                //если объект для отрисовки имеет имя "Паспорт проекта","Описание объекта" ,"Изменение условий" или "Дополнительно"
                if (object.getName().equals("Паспорт проекта")
                        || object.getName().equals("Описание объекта")
                        || object.getName().equals("Изменение условий")
                        || object.getName().equals("Финансово-экономические показатели")
                        || object.getName().equals("Условные бюджетные обязательства")) {
                    return true;
                }
                if (object.getName().equals("Дополнительно")) {
                    // если текущий раздел имеет имя отличное от "Создание" или "Подготовка проекта"
                    if (selectionModel.getSelectedItem() != null && !(selectionModel.getSelectedItem().getName().equals("Создание")
                            || selectionModel.getSelectedItem().getName().equals("Подготовка проекта"))) {
                        List<SimpleIdNameModel> simpleIdNameModels = selectionModel.getListView().getStore().getAll();
                        for (int i = 0; i < simpleIdNameModels.size(); i++) {
                            if (simpleIdNameModels.get(i).getName().equals("Общие сведения")) {
                                selectionModel.select(i, false);
                            }
                        }

                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDisableListViewFederalImplementationLvl(SimpleIdNameModel object) {
        String realizationLvlId;
        if (generalInformationView.getImplementationLvl().getCurrentValue() != null) {
            realizationLvlId = generalInformationView.getImplementationLvl().getCurrentValue().getId();
        } else if(managedProject.getGiRealizationForm() != null) {
            if(managedProject.getGiRealizationLevel() != null) {
                realizationLvlId = managedProject.getGiRealizationLevel().getId();
            } else {
                realizationLvlId = "";
            }
        } else {
            realizationLvlId = "";
        }
        //если выбран уровень реализации "Федеральный"
        if(!realizationLvlId.equals("1") && object.getName().equals("Финансово-экономические показатели")){
            return true;
        }
        return false;
    }

    private String getModelId(int i) {
        return i + ".";
    }

    @Override
    public void initCreationSteps() {
        initSteps(1);
    }

    @Override
    public void initViewSteps() {
        initSteps(0);
    }

    public void initSteps(int i) {
        listView.getStore().clear();
        List<String> keys = new ArrayList<>(stepViewBinding.keySet());
        for (; i < keys.size(); i++) {
            listView.getStore().add(new SimpleIdNameModel(getModelId(i), keys.get(i)));
        }
    }

    @Override
    public int getNexStep() {
        if (++currentStep >= stepViewBinding.size()) {
            currentStep = 0;
        }
        return currentStep;
    }

    @Override
    public void selectView(int step) {
        GWT.log(step + "");
        selectionModel.select(step, false);
    }

    private void initStepViewBinding() {
        stepViewBinding = new LinkedHashMap<String, IsWidget>() {
            {
                put("Паспорт проекта", projectPassportView);
                put("Общие сведения", generalInformationView);
                put("Описание объекта", objectDescriptionView);
                put("Подготовка проекта", projectPreparationView);
                put("Создание", creationView);
                put("Финансово-экономические показатели", financialAndEconomicIndicatorsView);
                put("Эксплуатация", exploitationView);
                put("Прекращение", terminationView);
                put("Условные бюджетные обязательства", contingentBudgetaryCommitmentsView);
                put("Изменение условий", conditionChangeView);
                put("Дополнительно", extraView);
                put("Обновления", editionsView);
                put("Комментарии", commentsView);
            }
        };
    }

    private void initIcons() {
        iconsBinding = new LinkedHashMap<String, String>() {
            {
                put(getModelId(0), "fa-book fa-2x");
                put(getModelId(1), "fa-info-circle fa-2x");
                put(getModelId(2), "fa-list  fa-2x");
                put(getModelId(3), "fa-cog fa-2x");
                put(getModelId(4), "fa-cubes fa-2x");
                put(getModelId(5), "fas fa-file-invoice fa-2x");
                put(getModelId(6), "fa-cogs fa-2x");
                put(getModelId(7), "fa-window-close fa-2x");
                put(getModelId(8), "fa-calculator fa-2x");
                put(getModelId(9), "fa-pencil-square fa-2x");
                put(getModelId(10), "fa-file-text fa-2x");
                put(getModelId(11), "fa-newspaper fa-2x");
                put(getModelId(12), "fa-comments fa-2x");
            }
        };
    }

    private void hideMenu() {
        if (isMenuFixed) return;
        listView.setStyleName(resources.navigationStyle().mainMenuHover(), false);
        panelContainer.setStyleName(resources.navigationStyle().mainDisplayNavigationExpanded(), false);
        Scheduler.get().scheduleFixedDelay(() -> {
            panelContainer.forceLayout();
            return false;
        }, 200);
    }

    private void showMenu() {
        listView.setStyleName(resources.navigationStyle().mainMenuHover(), true);
        panelContainer.setStyleName(resources.navigationStyle().mainDisplayNavigationExpanded(), true);
        Scheduler.get().scheduleFixedDelay(() -> {
            panelContainer.forceLayout();
            return false;
        }, 200);
    }


    @Override
    public Widget asWidget() {
        return w;
    }

    private void addHintsToTitles(SafeHtmlBuilder builder, SafeHtml content) {
        if(content.asString().contains("Условные бюджетные обязательства") || content.asString().contains("Финансово-экономические показатели")) {
            if ((generalInformationView.getImplementationLvl().getCurrentValue() != null && generalInformationView.getImplementationLvl().getCurrentValue().getId().equals("1"))
            || (generalInformationView.getImplementationLvl().getCurrentValue() == null && managedProject != null && managedProject.getGiRealizationLevel() != null && managedProject.getGiRealizationLevel().getId().equals("1"))) {
                builder.append(new SafeHtmlBuilder().appendHtmlConstant(
                        "<i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                                "title='По вопросам заполнения полей данного раздела необходимо обращаться к следующим сотрудникам Минфина России:\n" +
                                "\n" +
                                "Силанов Никита Дмитриевич  8-495-983-38-88 доб. 1542\n" +
                                "Кашуба Карина Мацеевна 8-495-983-38-88 доб. 1563\n" +
                                "Стихарева Наталья Петровна 8-495-983-38-88 доб. 1543'></i></span>").toSafeHtml());
            } else {
                builder.append(new SafeHtmlBuilder().appendHtmlConstant(" <i class='fa fa-info-circle' aria-hidden='true' style='cursor: pointer'" +
                        "title='По вопросам заполнения полей данного раздела необходимо направлять запрос на адрес электронной почты: gasu@minfin.gov.ru'></i></span>").toSafeHtml());
            }
        } else {
            builder.append(new SafeHtmlBuilder().appendHtmlConstant("</span>").toSafeHtml());
        }
    }
}
