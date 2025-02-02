package ru.ibs.gasu.client.presenters;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.TwinTriggerClickEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import org.fusesource.restygwt.client.JsonEncoderDecoder;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import ru.ibs.gasu.client.api.GchpApi;
import ru.ibs.gasu.client.crypto.CertMetaData;
import ru.ibs.gasu.client.crypto.DesApi;
import ru.ibs.gasu.client.crypto.SignSelectDialog;
import ru.ibs.gasu.client.events.DocumentSuccessfullySavedEvent;
import ru.ibs.gasu.client.utils.StringUtils;
import ru.ibs.gasu.client.widgets.*;
import ru.ibs.gasu.client.widgets.componens.FileUploader;
import ru.ibs.gasu.client.widgets.multiselectcombobox.MultiSelectComboBox;
import ru.ibs.gasu.common.models.Circumstance;
import ru.ibs.gasu.common.models.*;
import ru.ibs.gasu.common.soap.generated.gchpdicts.CompetitionCriterion;
import ru.ibs.gasu.common.soap.generated.gchpdicts.FinRequirement;
import ru.ibs.gasu.common.soap.generated.gchpdicts.NoFinRequirement;
import ru.ibs.gasu.common.soap.generated.gchpdicts.*;
import ru.ibs.gasu.common.soap.generated.gchpdocs.AgreementSubject;
import ru.ibs.gasu.common.soap.generated.gchpdocs.AgreementsSetEntity;
import ru.ibs.gasu.common.soap.generated.gchpdocs.CostRecoveryMethodEntity;
import ru.ibs.gasu.common.soap.generated.gchpdocs.DicGasuSp1;
import ru.ibs.gasu.common.soap.generated.gchpdocs.GovSupport;
import ru.ibs.gasu.common.soap.generated.gchpdocs.InitiationMethod;
import ru.ibs.gasu.common.soap.generated.gchpdocs.MeasureType;
import ru.ibs.gasu.common.soap.generated.gchpdocs.MethodOfExecuteObligationEntity;
import ru.ibs.gasu.common.soap.generated.gchpdocs.Municipality;
import ru.ibs.gasu.common.soap.generated.gchpdocs.ObjectKind;
import ru.ibs.gasu.common.soap.generated.gchpdocs.OpfEntity;
import ru.ibs.gasu.common.soap.generated.gchpdocs.OtherGovSupportsEntity;
import ru.ibs.gasu.common.soap.generated.gchpdocs.ProjectStatus;
import ru.ibs.gasu.common.soap.generated.gchpdocs.RealizationForm;
import ru.ibs.gasu.common.soap.generated.gchpdocs.RealizationLevel;
import ru.ibs.gasu.common.soap.generated.gchpdocs.RealizationSectorEntity;
import ru.ibs.gasu.common.soap.generated.gchpdocs.RealizationSphereEntity;
import ru.ibs.gasu.common.soap.generated.gchpdocs.RealizationStatus;
import ru.ibs.gasu.common.soap.generated.gchpdocs.TmCompositionOfCompensationGrantorFaultEntity;
import ru.ibs.gasu.common.soap.generated.gchpdocs.UnitOfMeasure;
import ru.ibs.gasu.common.soap.generated.gchpdocs.WinnerContractPriceOfferEntity;
import ru.ibs.gasu.common.soap.generated.gchpdocs.*;

import java.lang.Exception;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static ru.ibs.gasu.client.utils.ClientUtils.getProjectStatus;
import static ru.ibs.gasu.client.widgets.WidgetUtils.*;
import static ru.ibs.gasu.common.soap.generated.gchpdocs.OrgLevel.ROIV;

public class GchpNewDocumentPanelPresenter implements Presenter, DesApi.DesApiListener {

    private final GchpApi rpcService;
    private final HandlerManager eventBus;
    private final Display view;
    private PresenterMode mode;
    private Project managedProject/* = new Project()*/;
    private DesApi desApi;
    private RolePreferences preferences;

    @Override
    public void onInitCompleteSuccess(ArrayList<String> certNameList, ArrayList<String> thumbprintList, ArrayList<String> certMetaList) {
        SignSelectDialog signDialog = new SignSelectDialog(certNameList, thumbprintList, certMetaList) {
            @Override
            public void onCertThumbprintSelect(String certName, String thumbprint, String certMeta) {
                JSONValue jsonData = codec.encode(managedProject);
                if (desApi != null) {
                    desApi.signData(thumbprint, jsonData.toString(), null, certMeta);
                }
                hide();
            }
        };
        signDialog.show();
    }

    @Override
    public void onInitCompleteFailure(String message) {
        new AlertMessageBox("Подписание документа", "Ошибка при подписании документа: " + message).show();
    }

    @Override
    public void onSignCompleteSuccess(String signedText) {
        view.mask("Идет сохранение документа...");
        managedProject.setPublished(true);

        Long currentStatusId = managedProject.getGiProjectStatus().getId();

        updateProjectFromView();

        if (!(managedProject.getGiAlwaysDraftStatus() != null && managedProject.getGiAlwaysDraftStatus())) {
            managedProject.setGiProjectStatus(getProjectStatus(GiProjectStatus.COMPLETE));
        }
        if (view.getCommentsView().getProjectStatus().getCurrentValue() != null && "3".equals(view.getCommentsView().getProjectStatus().getCurrentValue().getId()) && currentStatusId != 3L) {
            managedProject.setGiProjectStatus(getProjectStatus(GiProjectStatus.NEED_REWORK));
        }
        fillCertData();
        rpcService.saveProject(managedProject, new MethodCallback<Project>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                new AlertMessageBox("Сохранение документа", "Во время сохранения документа возникла непредвиденная ошибка.").show();
                view.unmask();
            }

            @Override
            public void onSuccess(Method method, Project response) {
                managedProject = response;
                setUpMode();
                fetchRevisions();
                setValuesToPassport();
                eventBus.fireEvent(new DocumentSuccessfullySavedEvent());
                new MessageBox("Сохранение документа", "Документ успешно сохранен.").show();
                view.unmask();
                view.hide();
            }
        });
    }

    /**
     * Заполнить данными из сертификата
     */
    private void fillCertData() {
        if (desApi != null) {
            managedProject.setCert(DesApi.getCertData());
            managedProject.setSignature(DesApi.getSignedData());

            if (DesApi.getCertData() != null) {
                CertMetaData certMetaData = DesApi.getCertificateMetaData(DesApi.getCertData());
                managedProject.setSnils(certMetaData.getSnils());
            }
        }
    }

    @Override
    public void onSignCompleteFailure(String message) {
        new AlertMessageBox("Подписание документа", "Ошибка при подписании документа: " + message).show();
    }

    public enum PresenterMode {
        NEW_DOCUMENT,
        VIEW_DOCUMENT,
        EDIT_DOCUMENT
    }

    public interface Display {
        void show();

        void hideAllFiles();

        void hide();

        Widget asWidget();

        ProjectPassportView getProjectPassportView();

        GeneralInformationView getGeneralInformationView();

        ObjectDescriptionView getObjectDescriptionView();

        ProjectPreparationView getProjectPreparationView();

        CreationView getCreationView();

        FinancialAndEconomicIndicatorsView getFinancialAndEconomicIndicatorsView();

        ExploitationView getExploitationView();

        TerminationView getTerminationView();

        ContingentBudgetaryCommitmentsView getContingentBudgetaryCommitmentsView();

        ConditionChangeView getConditionChangeView();

        ExtraView getExtraView();

        EditionsView getEditionsView();

        CommentsView getCommentsView();

        TextButton getNextButton();

        TextButton getSaveButton();

        TextButton getSignButton();

        TextButton getCancelButton();

        int getNexStep();

        void selectView(int step);

        void mask();

        void mask(String message);

        void unmask();

        void setHeader(String header);

        void initCreationSteps();

        void initViewSteps();
    }

    @Override
    public void go(HasWidgets container) {
        setUpMode();
        if (mode != PresenterMode.NEW_DOCUMENT)
            view.mask();
        bind();
        container.add(view.asWidget());
        view.show();
        fetchRealizationFormDictionary();
        fetchProjectStatusDictionary();
        fetchRegionDictionary();
        fetchAgreementSubjectDictionary();
        fetchRealizationStatusDictionary();
        fetchCompetitionResultsDictionary();
        fetchCompetitionResultSignsDictionary();
        fetchPriceOrderDictionary();
        fetchContractPriceOffersDictionary();
        fetchPrivatePartnerCostRecoveryMethodsDictionary();
        fetchGovSupportsDictionary();
        fetchIRSourcesDictionary();
        fetchPpResultsOfPlacingDictionary();
        fetchIRLevelsDictionary();
        fetchExPaymentMethodsDictionary();
        fetchExCostRecoveryMethodsDictionary();
        fetchTerminationCausesDictionary();
        fetchTerminationAftermathsDictionary();
        fetchConditionChangeDictionary();
        fetchOpfDictionary();
        fetchAgreementsSetsDictionary();
        fetchAgreementsSetDictionary();
        fetchMethodOfExecuteObligationDictionary();
        fetchOtherGovSupportsDictionary();
        fetchWinnerContractPriceOfferDictionary();
        fetchRentObjectDictionary();
        fetchСompetitionСriterionDictionary();
        fetchBusinessModeTypeDictionary();
        fetchFinRequirmentsDictionary();
        fetchNoFinRequirmentsDictionary();
        fetchTmCompositionOfCompensationGrantorFaultDictionary();
        fetchRevisions();
        fetchTaxConditionDictionary();
        fetchMinimumGuarantIncomeTypesDictionary();
        fetchEnablingPayoutsDictionary();
        fetchCircumstancesDictionaries();

        view.getCommentsView().setProjectId(managedProject.getId());

        view.getCommentsView().getIsAlwaysDraftState().setVisible(preferences.isCanDoStatusDraft());
        if (!preferences.isCanChangeStatus()) {
            view.getCommentsView().hideProjectStatus();
        }

        Timer timer = new Timer() {
            @Override
            public void run() {
                boolean allDictsLoaded = isAllDictsLoaded();
                GWT.log("Check is all dicts loaded...");
                if (allDictsLoaded) {
                    this.cancel();
                    GWT.log("Dicts loaded.");
                    if (mode != PresenterMode.NEW_DOCUMENT) {
                        GWT.log("Updating View from project...");
                        updateViewFromProject();
                        GWT.log("View updated.");

                        GWT.log("Enabling/disabling ppk, region, municipality comboboxes...");
                        if (preferences.isUser()) {
                            view.getGeneralInformationView().getPpk().setEnabled(!oneOrg());
                            view.getGeneralInformationView().getRegion().setEnabled(false);
                            view.getGeneralInformationView().getMunicipality().setEnabled(false);
                        }
                        if (!preferences.getUserRoles().contains(UserRole.GCHP_ADMINISTRATOR) && ROIV.equals(preferences.getOrgLevel())) {
                            view.getGeneralInformationView().getRegion().setEnabled(false);
                        }
                        GWT.log("Comboboxes enabled/disabled...");
                    } else {
                        updateViewFromRoleModel();
                    }
                    GWT.log("Apply form fields logic (show/hide)...");
                    if (managedProject.getGiRealizationForm() != null) {
                        updateForm(Long.parseLong(managedProject.getGiRealizationForm().getId()), managedProject.getGiInitiationMethod() != null ? Long.parseLong(managedProject.getGiInitiationMethod().getId()) : null);
                    }
                    if (managedProject.getGiRealizationForm() != null) {
                        view.getExploitationView().update(Long.parseLong(managedProject.getGiRealizationForm().getId()), null);
                    }
                    GWT.log("Form fields logic applied.");
                    Double res = view.getCreationView().getInvestments().calcShare();
                    view.getExtraView().getUnforeseenExpences().setValue(res);

                    if ((preferences.getUserRoles().contains(UserRole.MINISTRY_CURATOR_GCHP)
                            || preferences.getUserRoles().contains(UserRole.TS_CURATOR_GCHP)
                            || preferences.getUserRoles().contains(UserRole.OZ_CURATOR_GCHP)
                            || preferences.getUserRoles().contains(UserRole.GCHP_ANALYSTS))
                            && !preferences.isAdmin()) {
                        view.hideAllFiles();
                    }

                    view.unmask();
                }
            }
        };
        timer.scheduleRepeating(50);
    }

    private boolean oneOrg() {
        return preferences.isFilterByOrgDocumentForm() && preferences.getOrgIds().size() == 1;
    }

    // TODO - есть такой же метод ниже updateViews. Удалить один из них.
    private void updateForm(Long formId, Long initId) {
        if (formId == null) return;
        Long realizationFormId = formId;
        Long initiationMethodId = initId;
        view.getGeneralInformationView().update(realizationFormId, initiationMethodId);
        view.getObjectDescriptionView().update(realizationFormId, initiationMethodId);
        view.getProjectPreparationView().update(realizationFormId, initiationMethodId);
        view.getCreationView().update(realizationFormId, initiationMethodId);
        view.getFinancialAndEconomicIndicatorsView().update(realizationFormId, initId);
        view.getExploitationView().update(realizationFormId, initiationMethodId);
        view.getTerminationView().update(realizationFormId, initiationMethodId);
        view.getConditionChangeView().update(realizationFormId, initiationMethodId);
        view.getExtraView().update(realizationFormId, initiationMethodId);
        view.getCommentsView().update(realizationFormId, initiationMethodId);
        view.getContingentBudgetaryCommitmentsView().update(realizationFormId, initiationMethodId);

    }

    private void updateViewFromRoleModel() {
        if (preferences.isFilterByRegionDocumentForm()) {
            selectComboByIdFire(view.getGeneralInformationView().getRegion(), getStringValOrEmpty(() -> preferences.getRegionId()));
        }

        if (preferences.isFilterByOktmoDocumentForm()) {
            String ppkId = oneOrg() ? preferences.getOrgIds().get(0) : null;
            fetchThenSelectedFilteredMunicipalityDictionary(getStringValOrEmpty(() -> preferences.getRegionId()),
                    getStringValOrEmpty(() -> preferences.getOktmoCode()),
                    getStringValOrEmpty(() -> ppkId));
        } else {
            if (oneOrg()) {
                fetchThenSelectedPpkFilterData(preferences.getRegionId(), null, preferences.getOrgIds().get(0));
            } else {
                fetchPpkFilterData(preferences.getRegionId(), null);
            }
        }

        view.getGeneralInformationView().getPpk().setEnabled(!oneOrg());
        view.getGeneralInformationView().getRegion().setEnabled(false);
        view.getGeneralInformationView().getMunicipality().setEnabled(false);
    }

    private static boolean areAllTrue(Collection<Boolean> bools) {
        for (Boolean bool : bools) {
            if (!bool) {
                return false;
            }
        }
        return true;
    }

    private boolean isAllDictsLoaded() {
        Map<String, Boolean> dictsLoadMap = new HashMap<>();
        dictsLoadMap.put("GeneralInformationView_getRealizationForm", view.getGeneralInformationView().getRealizationForm().getStore().size() > 0);
        dictsLoadMap.put("GeneralInformationView_getAgreementSubject", view.getGeneralInformationView().getAgreementSubject().getStore().size() > 0);
        dictsLoadMap.put("GeneralInformationView_getRealizationStatus", view.getGeneralInformationView().getRealizationStatus().getStore().size() > 0);
        dictsLoadMap.put("GeneralInformationView_getOpf", view.getGeneralInformationView().getOpf().getStore().size() > 0);
        dictsLoadMap.put("ProjectPreparationView_getCompetitionResults", view.getProjectPreparationView().getCompetitionResults().getStore().size() > 0);
        dictsLoadMap.put("ProjectPreparationView_getCompetitionResultsSignStatus", view.getProjectPreparationView().getCompetitionResultsSignStatus().getStore().size() > 0);
        dictsLoadMap.put("ProjectPreparationView_getContractPriceOrder", view.getProjectPreparationView().getContractPriceOrder().getStore().size() > 0);
        dictsLoadMap.put("ProjectPreparationView_getContractPriceOffer", view.getProjectPreparationView().getContractPriceOffer().getStore().size() > 0);
        dictsLoadMap.put("ProjectPreparationView_getAgreementsSet", view.getProjectPreparationView().getAgreementsSet().getStore().size() > 0);
        dictsLoadMap.put("ProjectPreparationView_getMethodOfExecuteObligation", view.getProjectPreparationView().getMethodOfExecuteObligation().getStore().size() > 0);
        dictsLoadMap.put("ProjectPreparationView_getOtherGovSupports", view.getProjectPreparationView().getOtherGovSupports().getStore().size() > 0);
        dictsLoadMap.put("ProjectPreparationView_getWinnerContractPriceOffer", view.getProjectPreparationView().getWinnerContractPriceOffer().getStore().size() > 0);
        dictsLoadMap.put("ProjectPreparationView_getPublicPartnerCostRecoveryMethod", view.getProjectPreparationView().getPublicPartnerCostRecoveryMethod().getStore().size() > 0);
        dictsLoadMap.put("CreationView_getGovSupport", view.getCreationView().getGovSupport().getStore().size() > 0);
        dictsLoadMap.put("ExploitationView_getIrSource", view.getExploitationView().getIrSource().getStore().size() > 0);
        dictsLoadMap.put("ExploitationView_getIrBudgetLevel", view.getExploitationView().getIrBudgetLevel().getStore().size() > 0);
        dictsLoadMap.put("ExploitationView_getPaymentMethod", view.getExploitationView().getPaymentMethod().getStore().size() > 0);
        dictsLoadMap.put("ExploitationView_getCostRecoveryMethod", view.getExploitationView().getCostRecoveryMethod().getStore().size() > 0);
        dictsLoadMap.put("TerminationView_getCause", view.getTerminationView().getCause().getStore().size() > 0);
        dictsLoadMap.put("TerminationView_getAftermath", view.getTerminationView().getAftermath().getStore().size() > 0);
        dictsLoadMap.put("ConditionChangeView_getCause", view.getConditionChangeView().getCause().getStore().size() > 0);
        dictsLoadMap.put("ExtraView_getOpf", view.getExtraView().getOpf().getStore().size() > 0);
        dictsLoadMap.put("CommentsView_getProjectStatus", view.getCommentsView().getProjectStatus().getStore().size() > 0);
        dictsLoadMap.put("CreationView_getOpf", view.getCreationView().getOpf().getStore().size() > 0);
        dictsLoadMap.put("CreationView_getAgreementComplex", view.getCreationView().getAgreementComplex().getStore().size() > 0);
        dictsLoadMap.put("ExtraView_getCompetitionCriteria", view.getExtraView().getCompetitionCriteria().getStore().size() > 0);
        dictsLoadMap.put("ObjectDescriptionView_getRentObject", view.getObjectDescriptionView().getRentObject().getStore().size() > 0);
        dictsLoadMap.put("ExtraView_getRegimeType", view.getExtraView().getRegimeType().getStore().size() > 0);
        dictsLoadMap.put("ExtraView_getNonFinancialRequirement", view.getExtraView().getNonFinancialRequirement().getStore().size() > 0);
        dictsLoadMap.put("ExtraView_getFinancialRequirement", view.getExtraView().getFinancialRequirement().getStore().size() > 0);
        dictsLoadMap.put("TerminationView_getTmCompositionOfCompensationGrantorFault", view.getTerminationView().getTmCompositionOfCompensationGrantorFault().getStore().size() > 0);
        dictsLoadMap.put("ContingentBudgetaryCommitmentsView_getMinimumGuaranteedIncomeForm", view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedIncomeForm().getStore().size() > 0);
        dictsLoadMap.put("ContingentBudgetaryCommitmentsView_getNonPaymentConsumersGoodsProvidedForm", view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedForm().getStore().size() > 0);
        dictsLoadMap.put("ContingentBudgetaryCommitmentsView_getNameOfCircumstanceAdditionalCostPrepare", view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostPrepare().getStore().size() > 0);
        dictsLoadMap.put("ContingentBudgetaryCommitmentsView_getNameOfCircumstanceAdditionalCostBuild", view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostBuild().getStore().size() > 0);
        dictsLoadMap.put("ContingentBudgetaryCommitmentsView_getNameOfCircumstanceAdditionalCostExploitation", view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostExploitation().getStore().size() > 0);
        dictsLoadMap.put("ProjectPreparationView_getPpResultsOfPlacing", view.getProjectPreparationView().getPpResultsOfPlacing().getStore().size() > 0);

        for (Map.Entry<String, Boolean> booleanEntry : dictsLoadMap.entrySet()) {
            if (!booleanEntry.getValue())
                GWT.log(booleanEntry.getKey() + " dict still not loaded...");
        }
        return areAllTrue(dictsLoadMap.values());
    }


    private void setUpMode() {
        if (managedProject.getId() == null) {
            mode = PresenterMode.NEW_DOCUMENT;
            view.setHeader("Создание нового документа");
            view.initCreationSteps();
        } else {
            //если задали значение mode=view через метод setManagedProject()
            if (PresenterMode.VIEW_DOCUMENT.equals(mode)) {
                view.setHeader("Просмотр документа - " + managedProject.getGiName());
                view.getSaveButton().removeFromParent();
                view.getSignButton().removeFromParent();
            } else {
                mode = PresenterMode.EDIT_DOCUMENT;
                view.setHeader("Редактирование документа - " + managedProject.getGiName());
            }
            view.initViewSteps();
        }
    }

    public void setManagedProject(Project managedProject) {
        this.managedProject = managedProject;
    }

    public void setManagedProject(Project managedProject, Boolean isViewMode) {
        this.setManagedProject(managedProject);
        if (isViewMode) {
            this.mode = PresenterMode.VIEW_DOCUMENT;
        }
    }

    public interface GridFileModelCodec extends JsonEncoderDecoder<Project> {
    }

    private GridFileModelCodec codec = GWT.create(GridFileModelCodec.class);

    private void bind() {
        view.getNextButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                int step = view.getNexStep();
                view.selectView(step);
            }
        });

        view.getSaveButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                view.mask("Идет сохранение документа...");
                if (view.getCommentsView().getProjectStatus().getCurrentValue() != null && "3".equals(view.getCommentsView().getProjectStatus().getCurrentValue().getId())) {
                    new AlertMessageBox("Ошибка",
                            "Чтобы перевести документ в статус \"Требует доработки\" необходимо его подписать").show();
                    return;
                }
                if (managedProject.getGiProjectStatus() != null && GiProjectStatus.COMPLETE.getId().equals(managedProject.getGiProjectStatus().getId())) {
                    managedProject.setGiProjectStatus(getProjectStatus(GiProjectStatus.NEED_ACTUALIZE));
                }
                // проверка строковых полей на максимальное количество знаков
                if (validateForm().length() > 0) {
                    new AlertMessageBox("Ошибка",
                            "Превышено допустимое количество символов: \n" + validateForm()).show();
                    view.unmask();
                    return;
                }
                GWT.log("updateProjectFromView");
                updateProjectFromView();
                GWT.log("call save project");
                managedProject.setPublished(false);
                rpcService.saveProject(managedProject, new MethodCallback<Project>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        new AlertMessageBox("Сохранение документа", "Во время сохранения документа возникла непредвиденная ошибка. " + exception.getMessage()).show();
                        view.unmask();
                    }

                    @Override
                    public void onSuccess(Method method, Project response) {
                        managedProject = response;
                        setUpMode();
                        fetchRevisions();
                        eventBus.fireEvent(new DocumentSuccessfullySavedEvent());
                        new MessageBox("Сохранение документа", "Документ успешно сохранен.").show();
                        view.unmask();
                    }
                });
            }
        });

        view.getSignButton().addSelectHandler(new SelectEvent.SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                view.getGeneralInformationView().getRealizationStatus().finishEditing();
                List<String> errorsMessages = checkRequiredFields();
                List<String> flkMessages = checkFlk();
                if (errorsMessages.size() > 0 || flkMessages.size() > 0) {
                    SafeHtmlBuilder sb = new SafeHtmlBuilder();
                    if (errorsMessages.size() > 0) {
                        sb.appendHtmlConstant("Небходимо заполнить следующиие обязательные поля:")
                                .appendHtmlConstant("<br>")
                                .appendHtmlConstant("<br>");
                        for (String message : errorsMessages) {
                            sb.appendHtmlConstant(StringUtils.isEmpty(message) ? "" : " - " + message)
                                    .appendHtmlConstant("<br>");
                        }
                    }

                    if (flkMessages.size() > 0) {
                        if (errorsMessages.size() > 0)
                            sb.appendHtmlConstant("<br>");
                        for (String message : flkMessages) {
                            sb.appendHtmlConstant(StringUtils.isEmpty(message) ? "" : " - " + message)
                                    .appendHtmlConstant("<br>");
                        }
                    }

                    MessageBox mb = new MessageBox(new SafeHtmlBuilder().appendHtmlConstant("Несоответствие правилам проверки").toSafeHtml(), sb.toSafeHtml());
                    mb.setModal(false);
                    mb.setBlinkModal(false);
                    mb.setResizable(true);
                    mb.show();
                    return;
                }
                desApi = new DesApi(GchpNewDocumentPanelPresenter.this);
            }
        });

        view.getCancelButton().addSelectHandler(event -> view.hide());

        view.getGeneralInformationView().getRegion().addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                String id = event.getSelectedItem().getId();
                fetchFilteredMunicipalityDictionary(id);
                fetchPpkFilterData(id, null);
            }
        });

        view.getGeneralInformationView().getMunicipality().addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                String regionId = view.getGeneralInformationView().getRegion().getCurrentValue().getId();
                String id = event.getSelectedItem().getId();
                fetchPpkFilterData(regionId, id);
            }
        });

        view.getGeneralInformationView().getMunicipality().addTwinTriggerClickHandler(new TwinTriggerClickEvent.TwinTriggerClickHandler() {
            @Override
            public void onTwinTriggerClick(TwinTriggerClickEvent event) {
                String regionId = view.getGeneralInformationView().getRegion().getCurrentValue().getId();
                fetchPpkFilterData(regionId, null);
            }
        });

        view.getGeneralInformationView().getRealizationForm().addBeforeSelectionHandler(new BeforeSelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<SimpleIdNameModel> event) {
                SimpleIdNameModel item = event.getItem();
                event.cancel();

                final Dialog simple = new Dialog();
                simple.setHeading("Изменить Форму реализации");
                simple.setWidth(300);
                simple.setResizable(false);
                simple.setHideOnButtonClick(true);
                simple.setPredefinedButtons(Dialog.PredefinedButton.YES, Dialog.PredefinedButton.NO);
                simple.getButton(Dialog.PredefinedButton.YES).addSelectHandler(new SelectEvent.SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent event1) {
                        String realizationFormId = item.getId();
                        selectComboById(view.getGeneralInformationView().getRealizationForm(), realizationFormId);
                        SelectionEvent.fire(view.getGeneralInformationView().getRealizationForm(), item);
                    }
                });
                simple.setBodyStyleName("pad-text");
                simple.getBody().addClassName("pad-text");
                simple.add(new Label("Форма реализации будет изменена на \"" +
                        item.getName() +
                        "\". " +
                        "Все специфичные поля для выбранной формы реализации будут очищены. Продолжить?"));
                simple.setModal(true);
                simple.setBlinkModal(true);
                simple.show();
            }
        });

        view.getGeneralInformationView().getRealizationForm().addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                String realizationFormId = event.getSelectedItem().getId();
                fetchFilteredInitiationMethodDictionary(Long.parseLong(realizationFormId));
                fetchFilteredRealizationLevelDictionary(Long.parseLong(realizationFormId));
                fetchFilteredRealizationSphereDictionary(Long.parseLong(realizationFormId));
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().clear();
                updateViews(Long.valueOf(realizationFormId), null);
            }
        });

        view.getGeneralInformationView().getInitMethod().addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                String formId = view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId();
                String initMethodId = event.getSelectedItem().getId();
                fetchFilteredAgreementGroundsDictionary(Long.parseLong(formId), Long.parseLong(initMethodId));

                updateViews(Long.valueOf(formId), Long.valueOf(initMethodId));
            }
        });

        view.getGeneralInformationView().getImplementationSphere().addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                String formId = view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId();
                String sphereId = event.getSelectedItem().getId();
                GWT.log("fetch sector");
                view.getGeneralInformationView().getObjectType().clearStore();
                fetchFilteredRealizationSectorDictionary(Long.valueOf(formId), Long.valueOf(sphereId));
            }
        });

        view.getGeneralInformationView().getImplementationSector().addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                String formId = view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId();
                String sphereId = view.getGeneralInformationView().getImplementationSphere().getCurrentValue().getId();
                String sectorId = event.getSelectedItem().getId();
                fetchObjectKindFilteredDictionary(Long.valueOf(formId), Long.valueOf(sphereId), Long.valueOf(sectorId));
            }
        });

        view.getGeneralInformationView().getImplementationLvl().addSelectionHandler(new SelectionHandler<SimpleIdNameModel>() {
            @Override
            public void onSelection(SelectionEvent<SimpleIdNameModel> event) {
                hideAttributesImplementationByLvlFederal();
                addHandlerToDueToOnsetOfCertainCircumstancesExistInContingentBudgetaryCommitmentsView();
            }
        });

        hideAttributesImplementationByLvlFederal();
        setConcessionaireHeaderByRealizationForm();
        setLastObjectDateContainerHeaderByExploitationView();
        setLastObjectActFileUploadHeaderByExploitationView();
        setImplementProjectHeaderByPpView();
        addHandlerToDueToOnsetOfCertainCircumstancesExistInContingentBudgetaryCommitmentsView();

        if (managedProject != null && managedProject.getPpConcludeAgreementIsSigned() != null) {
            view.getProjectPreparationView().setIsAgreementSigned(managedProject.getPpConcludeAgreementIsSigned());
        } else {
            view.getProjectPreparationView().setIsAgreementSigned(false);
        }

        setFileApiUrlsToUploader(view.getObjectDescriptionView().getFileUploader());

        setFileApiUrlsToUploader(view.getProjectPreparationView().getProjectAgreementFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getActFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getLeaseAgreementUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getProposalTextFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getProtocolFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getConclusionFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getCompetitionTextFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getCompetitionResultsProtocolFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getCompetitionResultsDocFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getSupportingDocumentsFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getFinancialModelFileUpload());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getDecisionFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getConclusionUOTextFileUploader());
        setFileApiUrlsToUploader(view.getProjectPreparationView().getFinancialModelTextFileUploader());

        setFileApiUrlsToUploader(view.getCreationView().getAgreementFileUpload());
        setFileApiUrlsToUploader(view.getCreationView().getFinancialClosingFileUpload());
        setFileApiUrlsToUploader(view.getCreationView().getFirstObjectCompleteActFileUpload());
        setFileApiUrlsToUploader(view.getCreationView().getLastObjectCompleteActFileUpload());
        setFileApiUrlsToUploader(view.getCreationView().getActFileUpload());
        setFileApiUrlsToUploader(view.getCreationView().getInvestmentVolumeStagOfCreationFileUpload());

        setFileApiUrlsToUploader(view.getCreationView().getReferenceFileUpload());
        setFileApiUrlsToUploader(view.getCreationView().getLandActTextFileUpload());
        setFileApiUrlsToUploader(view.getCreationView().getConfirmationDocFileUpload());
        setFileApiUrlsToUploader(view.getCreationView().getAgreementTextFiles());
        setFileApiUrlsToUploader(view.getCreationView().getCalcInvestCostsFileUpload());

        setFileApiUrlsToUploader(view.getExploitationView().getFinancialModelFileUpload());
        setFileApiUrlsToUploader(view.getExploitationView().getLastObjectActFileUpload());
        setFileApiUrlsToUploader(view.getExploitationView().getInvestmentVolumeStagOfExploitationFileUpload());
        setFileApiUrlsToUploader(view.getExploitationView().getExFinModelFVIds());
        setFileApiUrlsToUploader(view.getExploitationView().getExSupportDocFVIds());
        setFileApiUrlsToUploader(view.getExploitationView().getExSupportCompensDocFVIds());
        setFileApiUrlsToUploader(view.getExploitationView().getExAgreementFVIds());
        setFileApiUrlsToUploader(view.getExploitationView().getExAcceptActFVIds());
        setFileApiUrlsToUploader(view.getExploitationView().getExAcceptActAAMFVIds());
        setFileApiUrlsToUploader(view.getExploitationView().getExCalculationPlannedAmountFVIds());

        setFileApiUrlsToUploader(view.getTerminationView().getActTextFileUpload());
        setFileApiUrlsToUploader(view.getTerminationView().getCompensationTextFileUpload());
        setFileApiUrlsToUploader(view.getTerminationView().getTaActFileUpload());
        setFileApiUrlsToUploader(view.getTerminationView().getTmSupportingDocuments());

        setFileApiUrlsToUploader(view.getConditionChangeView().getActFileUpload());

        setFileApiUrlsToUploader(view.getExtraView().getDecisionTextFileUpload());
        setFileApiUrlsToUploader(view.getGeneralInformationView().getCompletedTemplateFileUpload());
        setFileApiUrlsToUploader(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedFileUpload());
        setFileApiUrlsToUploader(view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersFileUpload());
        setFileApiUrlsToUploader(view.getContingentBudgetaryCommitmentsView().getArisingProvisionOfBenefitFileUpload());
        setFileApiUrlsToUploader(view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsFileUpload());
        setFileApiUrlsToUploader(view.getContingentBudgetaryCommitmentsView().getCompensationAdditionalCostsAgreementFileUpload());

        view.getCreationView().getInvestments().setRunnable(new Runnable() {
            @Override
            public void run() {
                Double res = view.getCreationView().getInvestments().calcShare();
                view.getExtraView().getUnforeseenExpences().setValue(res);
            }
        });
    }

    private void updateViews(Long realizationFormId, Long initiationMethodId) {
        view.getGeneralInformationView().update(realizationFormId, initiationMethodId);
        view.getObjectDescriptionView().update(realizationFormId, initiationMethodId);
        view.getProjectPreparationView().update(realizationFormId, initiationMethodId);
        view.getCreationView().update(realizationFormId, initiationMethodId);
        view.getExploitationView().update(realizationFormId, initiationMethodId);
        view.getTerminationView().update(realizationFormId, initiationMethodId);
        view.getConditionChangeView().update(realizationFormId, initiationMethodId);
        view.getExtraView().update(realizationFormId, initiationMethodId);
        view.getCommentsView().update(realizationFormId, initiationMethodId);
        view.getContingentBudgetaryCommitmentsView().update(realizationFormId, initiationMethodId);
        setConcessionaireHeaderByRealizationForm();
        setLastObjectDateContainerHeaderByExploitationView();
        setLastObjectActFileUploadHeaderByExploitationView();
        addHandlerToDueToOnsetOfCertainCircumstancesExistInContingentBudgetaryCommitmentsView();
    }

    private CreationInvestments getValueFromCreationInvestments() {
        CreationInvestments pi = new CreationInvestments();
        pi.setIncludeNds(view.getCreationView().getInvestments().getNdsCheck().getValue());
        pi.setOnDate(WidgetUtils.getDate(view.getCreationView().getInvestments().getDateField().getValue()));
        if (view.getCreationView().getInvestments().getMeasureType().getCurrentValue() != null)
            pi.setMeasureType(new MeasureType() {{
                setId(Long.valueOf(view.getCreationView().getInvestments().getMeasureType().getCurrentValue().getId()));
                setName(view.getCreationView().getInvestments().getMeasureType().getCurrentValue().getName());
            }});
        for (PlanFactYear indicator : view.getCreationView().getInvestments().getRootItems()) {
            CreationInvestmentIndicator creationInvestmentIndicator = new CreationInvestmentIndicator();
            creationInvestmentIndicator.setId(indicator.getId()); // Это id в бд
            creationInvestmentIndicator.setFact(indicator.getFact());
            creationInvestmentIndicator.setPlan(indicator.getPlan());
            creationInvestmentIndicator.setType(new InvestmentIndicatorType() {{
                setId(indicator.getGid());
            }}); // Это общие инвестиции и т.д.
            if (!indicator.getGid().equals(3L) && !indicator.getGid().equals(2L)) {
                for (PlanFactYear year : view.getCreationView().getInvestments().getTreeStore().getChildren(indicator)) {
                    CreationInvestmentIndicatorYearValue piYear = new CreationInvestmentIndicatorYearValue();
                    piYear.setId(year.getId());
                    piYear.setPlan(year.getPlan());
                    piYear.setFact(year.getFact());
                    piYear.setYear(Integer.parseInt(year.getNameOrYear()));
                    creationInvestmentIndicator.getValuesByYears().add(piYear);
                }
            }
            pi.getIndicators().add(creationInvestmentIndicator);
        }
        return pi;
    }

    private void setValueFromCreationInvestments(CreationInvestments investments) {
        if (investments == null) return;
        view.getCreationView().getInvestments().getNdsCheck().setValue(investments.isIncludeNds());
        view.getCreationView().getInvestments().getDateField().setValue(getDate(investments.getOnDate()));
        if (investments.getMeasureType() != null) {
            selectComboById(view.getCreationView().getInvestments().getMeasureType(), String.valueOf(investments.getMeasureType().getId()));
            if ("1".equals(String.valueOf(investments.getMeasureType().getId())))
                view.getCreationView().getInvestments().getDateField().hide();
        }
        // get current selected years
        // предполагается, что года у всех показателей одинаковые
        List<String> savedYears = new ArrayList<>();
        if (investments.getIndicators().size() > 0)
            savedYears = investments.getIndicators().get(0).getValuesByYears()
                    .stream().map(i -> String.valueOf(i.getYear())).collect(Collectors.toList());

        for (PlanFactYear rootItem : view.getCreationView().getInvestments().getRootItems()) {
            for (CreationInvestmentIndicator indicator : investments.getIndicators()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    if (!rootItem.getGid().equals(3L) && !rootItem.getGid().equals(2L)) {
                        for (CreationInvestmentIndicatorYearValue valByYear : indicator.getValuesByYears()) {
                            PlanFactYear pfy = new PlanFactYear();
                            pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                            pfy.setPlan(valByYear.getPlan());
                            pfy.setFact(valByYear.getFact());
                            pfy.setId(valByYear.getId()); // database id
                            pfy.setGid(getRandId());
                            view.getCreationView().getInvestments().addChildToTreeStore(rootItem, pfy);
                        }
                    }
                }
            }
        }
        view.getCreationView().getInvestments().getYearBox().deselectAll();
        view.getCreationView().getInvestments().getYearBox().select(savedYears, true);
    }

    private PlanInvestments getValueFromPlanningInvestments() {
        PlanInvestments pi = new PlanInvestments();
        pi.setIncludeNds(view.getProjectPreparationView().getInvestments().getNdsCheck().getValue());
        pi.setOnDate(getDate(view.getProjectPreparationView().getInvestments().getDateField().getValue()));
        if (view.getProjectPreparationView().getInvestments().getMeasureType().getCurrentValue() != null)
            pi.setMeasureType(new MeasureType() {{
                setId(Long.valueOf(view.getProjectPreparationView().getInvestments().getMeasureType().getCurrentValue().getId()));
                setName(view.getProjectPreparationView().getInvestments().getMeasureType().getCurrentValue().getName());
            }});

        for (PlanFactYear indicator : view.getProjectPreparationView().getInvestments().getRootItems()) {
            PlanInvestmentIndicator planInvestmentIndicator = new PlanInvestmentIndicator();
            planInvestmentIndicator.setId(indicator.getId()); // Это id в бд
            planInvestmentIndicator.setFact(indicator.getFact());
            planInvestmentIndicator.setPlan(indicator.getPlan());
            planInvestmentIndicator.setType(new InvestmentIndicatorType() {{
                setId(indicator.getGid());
            }}); // Это общие инвестиции и т.д.
            if (!indicator.getGid().equals(3L)) {
                for (PlanFactYear year : view.getProjectPreparationView().getInvestments().getTreeStore().getChildren(indicator)) {
                    PlanInvestmentIndicatorYearValue piYear = new PlanInvestmentIndicatorYearValue();
                    piYear.setId(year.getId());
                    piYear.setPlan(year.getPlan());
                    piYear.setFact(year.getFact());
                    piYear.setYear(Integer.parseInt(year.getNameOrYear()));
                    planInvestmentIndicator.getValuesByYears().add(piYear);
                }
            }
            pi.getIndicators().add(planInvestmentIndicator);
        }
        return pi;
    }

    private void setValueFromPlanningInvestments(PlanInvestments investments) {
        if (investments == null) return;
        view.getProjectPreparationView().getInvestments().getNdsCheck().setValue(investments.isIncludeNds());
        view.getProjectPreparationView().getInvestments().getDateField().setValue(getDate(investments.getOnDate()));
        if (investments.getMeasureType() != null) {
            selectComboById(view.getProjectPreparationView().getInvestments().getMeasureType(), String.valueOf(investments.getMeasureType().getId()));
            if ("1".equals(String.valueOf(investments.getMeasureType().getId())))
                view.getProjectPreparationView().getInvestments().getDateField().hide();
        }
        // get current selected years
        // предполагается, что года у всех показателей одинаковые
        List<String> savedYears = new ArrayList<>();
        if (investments.getIndicators().size() > 0)
            savedYears = investments.getIndicators().get(0).getValuesByYears()
                    .stream().map(i -> String.valueOf(i.getYear())).collect(Collectors.toList());

        for (PlanFactYear rootItem : view.getProjectPreparationView().getInvestments().getTreeStore().getRootItems()) {
            for (PlanInvestmentIndicator indicator : investments.getIndicators()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    for (PlanInvestmentIndicatorYearValue valByYear : indicator.getValuesByYears()) {
                        PlanFactYear pfy = new PlanFactYear();
                        pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                        pfy.setPlan(valByYear.getPlan());
                        pfy.setFact(valByYear.getFact());
                        pfy.setId(valByYear.getId()); // database id
                        pfy.setGid(getRandId());
                        view.getProjectPreparationView().getInvestments().addChildToTreeStore(rootItem, pfy);
                    }
                }
            }
        }
        view.getProjectPreparationView().getInvestments().getYearBox().deselectAll();
        view.getProjectPreparationView().getInvestments().getYearBox().select(savedYears, true);
    }

    private PlanCreationInvestments getValueFromPlanningCreationInvestments() {
        PlanCreationInvestments pi = new PlanCreationInvestments();
        pi.setIncludeNds(view.getProjectPreparationView().getCreationInvestments().getNdsCheck().getValue());
        pi.setOnDate(getDate(view.getProjectPreparationView().getCreationInvestments().getDateField().getValue()));
        if (view.getProjectPreparationView().getCreationInvestments().getMeasureType().getCurrentValue() != null)
            pi.setMeasureType(new MeasureType() {{
                setId(Long.valueOf(view.getProjectPreparationView().getCreationInvestments().getMeasureType().getCurrentValue().getId()));
                setName(view.getProjectPreparationView().getCreationInvestments().getMeasureType().getCurrentValue().getName());
            }});

        for (PlanFactYear indicator : view.getProjectPreparationView().getCreationInvestments().getRootItems()) {
            PlanCreationInvestmentIndicator planInvestmentIndicator = new PlanCreationInvestmentIndicator();
            planInvestmentIndicator.setId(indicator.getId()); // Это id в бд
            planInvestmentIndicator.setFact(indicator.getFact());
            planInvestmentIndicator.setPlan(indicator.getPlan());
            planInvestmentIndicator.setType(new InvestmentIndicatorType() {{
                setId(indicator.getGid());
            }}); // Это общие инвестиции и т.д.
            if (!indicator.getGid().equals(3L)) {
                for (PlanFactYear year : view.getProjectPreparationView().getCreationInvestments().getTreeStore().getChildren(indicator)) {
                    PlanCreationInvestmentIndicatorYearValue piYear = new PlanCreationInvestmentIndicatorYearValue();
                    piYear.setId(year.getId());
                    piYear.setPlan(year.getPlan());
                    piYear.setFact(year.getFact());
                    piYear.setYear(Integer.parseInt(year.getNameOrYear()));
                    planInvestmentIndicator.getValuesByYears().add(piYear);
                }
            }
            pi.getIndicators().add(planInvestmentIndicator);
        }
        return pi;
    }

    private void setValueFromPlanningCreationInvestments(PlanCreationInvestments investments) {
        if (investments == null) return;
        view.getProjectPreparationView().getCreationInvestments().getNdsCheck().setValue(investments.isIncludeNds());
        view.getProjectPreparationView().getCreationInvestments().getDateField().setValue(getDate(investments.getOnDate()));
        if (investments.getMeasureType() != null) {
            selectComboById(view.getProjectPreparationView().getCreationInvestments().getMeasureType(), String.valueOf(investments.getMeasureType().getId()));
            if ("1".equals(String.valueOf(investments.getMeasureType().getId())))
                view.getProjectPreparationView().getCreationInvestments().getDateField().hide();
        }
        // get current selected years
        // предполагается, что года у всех показателей одинаковые

        List<String> savedYears = new ArrayList<>();
        if (investments.getIndicators().size() > 0)
            savedYears = investments.getIndicators().get(0).getValuesByYears()
                    .stream().map(i -> String.valueOf(i.getYear())).collect(Collectors.toList());

        for (PlanFactYear rootItem : view.getProjectPreparationView().getCreationInvestments().getRootItems()) {
            for (PlanCreationInvestmentIndicator indicator : investments.getIndicators()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    if (!rootItem.getGid().equals(3L)) {
                        for (PlanCreationInvestmentIndicatorYearValue valByYear : indicator.getValuesByYears()) {
                            PlanFactYear pfy = new PlanFactYear();
                            pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                            pfy.setPlan(valByYear.getPlan());
                            pfy.setFact(valByYear.getFact());
                            pfy.setId(valByYear.getId()); // database id
                            pfy.setGid(getRandId());
                            view.getProjectPreparationView().getCreationInvestments().addChildToTreeStore(rootItem, pfy);
                        }
                    }
                }
            }
        }
        view.getProjectPreparationView().getCreationInvestments().getYearBox().deselectAll();
        view.getProjectPreparationView().getCreationInvestments().getYearBox().select(savedYears, true);

    }

    private void setEnergyEfficiencyPlanToView() {
        for (EnergyEfficiencyPlan plan : managedProject.getOdEnergyEfficiencyPlans()) {
            EemModel model = new EemModel();
            model.setId(plan.getId());
            model.setPlanValue(plan.getPlan());
            model.setFactValue(plan.getFact());
            model.setPeriod(plan.getYear());
            model.setFileModel(new FileModel() {{
                setId(plan.getFileVersionId());
                setName(plan.getFileName());
            }});
            view.getObjectDescriptionView().getEemWidget().getEemGrid().getStore().add(model);
        }
    }

    private void setEnergyEfficiencyPlanFromView() {
        List<EemModel> eemData = view.getObjectDescriptionView().getEemWidget().getEemGrid().getStore().getAll();
        managedProject.getOdEnergyEfficiencyPlans().clear();
        for (EemModel eemModel : eemData) {
            EnergyEfficiencyPlan plan = new EnergyEfficiencyPlan();
            plan.setYear(eemModel.getPeriod());
            plan.setPlan(eemModel.getPlanValue());
            plan.setFact(eemModel.getFactValue());
            plan.setId(eemModel.getId());
            if (eemModel.getFileModel() != null) {
                plan.setFileVersionId(eemModel.getFileModel().getId());
                plan.setFileName(eemModel.getFileModel().getName());
            }
            managedProject.getOdEnergyEfficiencyPlans().add(plan);
        }
    }

    private CbcInvestments1 getValueFromCbcInvestments1() {
        CbcInvestments1 pi = new CbcInvestments1();
        pi.setIncludeNds(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getNdsCheck().getValue());
        pi.setOnDate(getDate(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getDateField().getValue()));
        if (view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getMeasureType().getCurrentValue() != null)
            pi.setMeasureType(new MeasureType() {{
                setId(Long.valueOf(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getMeasureType().getCurrentValue().getId()));
                setName(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getMeasureType().getCurrentValue().getName());
            }});

        for (PlanFactYear indicator : view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getTreeStore().getRootItems()) {
            CbcInvestments1Indicator planInvestmentIndicator = new CbcInvestments1Indicator();
            planInvestmentIndicator.setId(indicator.getId()); // Это id в бд
            planInvestmentIndicator.setFact(indicator.getFact());
            planInvestmentIndicator.setPlan(indicator.getPlan());
            planInvestmentIndicator.setType(new InvestmentIndicatorType() {{
                setId(indicator.getGid());
            }}); // Это общие инвестиции и т.д.
            for (PlanFactYear year : view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getTreeStore().getChildren(indicator)) {
                CbcInvestments1IndicatorYearValue piYear = new CbcInvestments1IndicatorYearValue();
                piYear.setId(year.getId());
                piYear.setPlan(year.getPlan());
                piYear.setFact(year.getFact());
                piYear.setYear(Integer.parseInt(year.getNameOrYear()));
                planInvestmentIndicator.getValuesByYears().add(piYear);
            }
            pi.getIndicators().add(planInvestmentIndicator);
        }
        return pi;
    }

    private void setValueFromCbcInvestments1(CbcInvestments1 investments) {
        if (investments == null) return;
        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getNdsCheck().setValue(investments.isIncludeNds());
        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getDateField().setValue(getDate(investments.getOnDate()));
        if (investments.getMeasureType() != null) {
            selectComboById(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getMeasureType(), String.valueOf(investments.getMeasureType().getId()));
            if ("1".equals(String.valueOf(investments.getMeasureType().getId())))
                view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getDateField().hide();
        }
        // get current selected years
        // предполагается, что года у всех показателей одинаковые

        List<String> savedYears = new ArrayList<>();
        if (investments.getIndicators().size() > 0)
            savedYears = investments.getIndicators().get(0).getValuesByYears()
                    .stream().map(i -> String.valueOf(i.getYear())).collect(Collectors.toList());
        for (PlanFactYear rootItem : view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getTreeStore().getRootItems()) {
            for (CbcInvestments1Indicator indicator : investments.getIndicators()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    for (CbcInvestments1IndicatorYearValue valByYear : indicator.getValuesByYears()) {
                        PlanFactYear pfy = new PlanFactYear();
                        pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                        pfy.setPlan(valByYear.getPlan());
                        pfy.setFact(valByYear.getFact());
                        pfy.setId(valByYear.getId()); // database id
                        pfy.setGid(getRandId());
                        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().addChildToTreeStore(rootItem, pfy);
                    }
                }
            }
        }
        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getYearBox().deselectAll();
        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmountNew().getYearBox().select(savedYears, true);
    }

    private CbcInvestments2 getValueFromCbcInvestments2() {
        CbcInvestments2 pi = new CbcInvestments2();
        pi.setIncludeNds(view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getNdsCheck().getValue());
        pi.setOnDate(getDate(view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getDateField().getValue()));
        if (view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getMeasureType().getCurrentValue() != null)
            pi.setMeasureType(new MeasureType() {{
                setId(Long.valueOf(view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getMeasureType().getCurrentValue().getId()));
                setName(view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getMeasureType().getCurrentValue().getName());
            }});

        for (PlanFactYear indicator : view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getTreeStore().getRootItems()) {
            CbcInvestments2Indicator planInvestmentIndicator = new CbcInvestments2Indicator();
            planInvestmentIndicator.setId(indicator.getId()); // Это id в бд
            planInvestmentIndicator.setFact(indicator.getFact());
            planInvestmentIndicator.setPlan(indicator.getPlan());
            planInvestmentIndicator.setType(new InvestmentIndicatorType() {{
                setId(indicator.getGid());
            }}); // Это общие инвестиции и т.д.
            for (PlanFactYear year : view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getTreeStore().getChildren(indicator)) {
                CbcInvestments2IndicatorYearValue piYear = new CbcInvestments2IndicatorYearValue();
                piYear.setId(year.getId());
                piYear.setPlan(year.getPlan());
                piYear.setFact(year.getFact());
                piYear.setYear(Integer.parseInt(year.getNameOrYear()));
                planInvestmentIndicator.getValuesByYears().add(piYear);
            }
            pi.getIndicators().add(planInvestmentIndicator);
        }
        return pi;
    }

    private void setValueFromCbcInvestments2(CbcInvestments2 investments) {
        if (investments == null) return;
        view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getNdsCheck().setValue(investments.isIncludeNds());
        view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getDateField().setValue(getDate(investments.getOnDate()));
        if (investments.getMeasureType() != null) {
            selectComboById(view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getMeasureType(), String.valueOf(investments.getMeasureType().getId()));
            if ("1".equals(String.valueOf(investments.getMeasureType().getId())))
                view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getDateField().hide();
        }
        // get current selected years
        // предполагается, что года у всех показателей одинаковые

        List<String> savedYears = new ArrayList<>();
        if (investments.getIndicators().size() > 0)
            savedYears = investments.getIndicators().get(0).getValuesByYears()
                    .stream().map(i -> String.valueOf(i.getYear())).collect(Collectors.toList());
        for (PlanFactYear rootItem : view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getTreeStore().getRootItems()) {
            for (CbcInvestments2Indicator indicator : investments.getIndicators()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    for (CbcInvestments2IndicatorYearValue valByYear : indicator.getValuesByYears()) {
                        PlanFactYear pfy = new PlanFactYear();
                        pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                        pfy.setPlan(valByYear.getPlan());
                        pfy.setFact(valByYear.getFact());
                        pfy.setId(valByYear.getId()); // database id
                        pfy.setGid(getRandId());
                        view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().addChildToTreeStore(rootItem, pfy);
                    }
                }
            }
        }
        view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getYearBox().deselectAll();
        view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmountNew().getYearBox().select(savedYears, true);
    }

    private CbcInvestments3 getValueFromCbcInvestments3() {
        CbcInvestments3 pi = new CbcInvestments3();
        pi.setIncludeNds(view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getNdsCheck().getValue());
        pi.setOnDate(getDate(view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getDateField().getValue()));
        if (view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getMeasureType().getCurrentValue() != null)
            pi.setMeasureType(new MeasureType() {{
                setId(Long.valueOf(view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getMeasureType().getCurrentValue().getId()));
                setName(view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getMeasureType().getCurrentValue().getName());
            }});

        for (PlanFactYear indicator : view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getTreeStore().getRootItems()) {
            CbcInvestments3Indicator planInvestmentIndicator = new CbcInvestments3Indicator();
            planInvestmentIndicator.setId(indicator.getId()); // Это id в бд
            planInvestmentIndicator.setFact(indicator.getFact());
            planInvestmentIndicator.setPlan(indicator.getPlan());
            planInvestmentIndicator.setType(new InvestmentIndicatorType() {{
                setId(indicator.getGid());
            }}); // Это общие инвестиции и т.д.
            for (PlanFactYear year : view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getTreeStore().getChildren(indicator)) {
                CbcInvestments3IndicatorYearValue piYear = new CbcInvestments3IndicatorYearValue();
                piYear.setId(year.getId());
                piYear.setPlan(year.getPlan());
                piYear.setFact(year.getFact());
                piYear.setYear(Integer.parseInt(year.getNameOrYear()));
                planInvestmentIndicator.getValuesByYears().add(piYear);
            }
            pi.getIndicators().add(planInvestmentIndicator);
        }
        return pi;
    }

    private void setValueFromCbcInvestments3(CbcInvestments3 investments) {
        if (investments == null) return;
        view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getNdsCheck().setValue(investments.isIncludeNds());
        view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getDateField().setValue(getDate(investments.getOnDate()));
        if (investments.getMeasureType() != null) {
            selectComboById(view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getMeasureType(), String.valueOf(investments.getMeasureType().getId()));
            if ("1".equals(String.valueOf(investments.getMeasureType().getId())))
                view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getDateField().hide();
        }
        // get current selected years
        // предполагается, что года у всех показателей одинаковые

        List<String> savedYears = new ArrayList<>();
        if (investments.getIndicators().size() > 0)
            savedYears = investments.getIndicators().get(0).getValuesByYears()
                    .stream().map(i -> String.valueOf(i.getYear())).collect(Collectors.toList());
        for (PlanFactYear rootItem : view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getTreeStore().getRootItems()) {
            for (CbcInvestments3Indicator indicator : investments.getIndicators()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    for (CbcInvestments3IndicatorYearValue valByYear : indicator.getValuesByYears()) {
                        PlanFactYear pfy = new PlanFactYear();
                        pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                        pfy.setPlan(valByYear.getPlan());
                        pfy.setFact(valByYear.getFact());
                        pfy.setId(valByYear.getId()); // database id
                        pfy.setGid(getRandId());
                        view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().addChildToTreeStore(rootItem, pfy);
                    }
                }
            }
        }
        view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getYearBox().deselectAll();
        view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmountNew().getYearBox().select(savedYears, true);
    }

    private CbcInvestments4 getValueFromCbcInvestments4() {
        CbcInvestments4 pi = new CbcInvestments4();
        pi.setIncludeNds(view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getNdsCheck().getValue());
        pi.setOnDate(getDate(view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getDateField().getValue()));
        if (view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getMeasureType().getCurrentValue() != null)
            pi.setMeasureType(new MeasureType() {{
                setId(Long.valueOf(view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getMeasureType().getCurrentValue().getId()));
                setName(view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getMeasureType().getCurrentValue().getName());
            }});

        for (PlanFactYear indicator : view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getTreeStore().getRootItems()) {
            CbcInvestments4Indicator planInvestmentIndicator = new CbcInvestments4Indicator();
            planInvestmentIndicator.setId(indicator.getId()); // Это id в бд
            planInvestmentIndicator.setFact(indicator.getFact());
            planInvestmentIndicator.setPlan(indicator.getPlan());
            planInvestmentIndicator.setType(new InvestmentIndicatorType() {{
                setId(indicator.getGid());
            }}); // Это общие инвестиции и т.д.
            for (PlanFactYear year : view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getTreeStore().getChildren(indicator)) {
                CbcInvestments4IndicatorYearValue piYear = new CbcInvestments4IndicatorYearValue();
                piYear.setId(year.getId());
                piYear.setPlan(year.getPlan());
                piYear.setFact(year.getFact());
                piYear.setYear(Integer.parseInt(year.getNameOrYear()));
                planInvestmentIndicator.getValuesByYears().add(piYear);
            }
            pi.getIndicators().add(planInvestmentIndicator);
        }
        return pi;
    }

    private void setValueFromCbcInvestments4(CbcInvestments4 investments) {
        if (investments == null) return;
        view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getNdsCheck().setValue(investments.isIncludeNds());
        view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getDateField().setValue(getDate(investments.getOnDate()));
        if (investments.getMeasureType() != null) {
            selectComboById(view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getMeasureType(), String.valueOf(investments.getMeasureType().getId()));
            if ("1".equals(String.valueOf(investments.getMeasureType().getId())))
                view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getDateField().hide();
        }
        // get current selected years
        // предполагается, что года у всех показателей одинаковые

        List<String> savedYears = new ArrayList<>();
        if (investments.getIndicators().size() > 0)
            savedYears = investments.getIndicators().get(0).getValuesByYears()
                    .stream().map(i -> String.valueOf(i.getYear())).collect(Collectors.toList());
        for (PlanFactYear rootItem : view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getTreeStore().getRootItems()) {
            for (CbcInvestments4Indicator indicator : investments.getIndicators()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    for (CbcInvestments4IndicatorYearValue valByYear : indicator.getValuesByYears()) {
                        PlanFactYear pfy = new PlanFactYear();
                        pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                        pfy.setPlan(valByYear.getPlan());
                        pfy.setFact(valByYear.getFact());
                        pfy.setId(valByYear.getId()); // database id
                        pfy.setGid(getRandId());
                        view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().addChildToTreeStore(rootItem, pfy);
                    }
                }
            }
        }
        view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getYearBox().deselectAll();
        view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmountNew().getYearBox().select(savedYears, true);
    }

    private RemainingDebt getValueFromRemainingDebt() {
        RemainingDebt pi = new RemainingDebt();
        for (PlanFactYear indicator : view.getCreationView().getBalanceOfDebtWidget().getTreeStore().getRootItems()) {
            RemainingDebtIndicator planInvestmentIndicator = new RemainingDebtIndicator();
            planInvestmentIndicator.setId(indicator.getId()); // Это id в бд
            planInvestmentIndicator.setFact(indicator.getFact());
            planInvestmentIndicator.setPlan(indicator.getPlan());
            planInvestmentIndicator.setType(new InvestmentIndicatorType() {{
                setId(indicator.getGid());
            }}); // Это общие инвестиции и т.д.
            for (PlanFactYear year : view.getCreationView().getBalanceOfDebtWidget().getTreeStore().getChildren(indicator)) {
                RemainingDebtIndicatorYearValue piYear = new RemainingDebtIndicatorYearValue();
                piYear.setId(year.getId());
                piYear.setPlan(year.getPlan());
                piYear.setFact(year.getFact());
                piYear.setYear(Integer.parseInt(year.getNameOrYear()));
                planInvestmentIndicator.getValuesByYears().add(piYear);
            }
            pi.getIndicators().add(planInvestmentIndicator);
        }
        return pi;
    }

    private void setValueFromRemainingDebt(RemainingDebt investments) {
        if (investments == null) return;
        List<String> savedYears = new ArrayList<>();
        if (investments.getIndicators().size() > 0)
            savedYears = investments.getIndicators().get(0).getValuesByYears()
                    .stream().map(i -> String.valueOf(i.getYear())).collect(Collectors.toList());
        for (PlanFactYear rootItem : view.getCreationView().getBalanceOfDebtWidget().getTreeStore().getRootItems()) {
            for (RemainingDebtIndicator indicator : investments.getIndicators()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    for (RemainingDebtIndicatorYearValue valByYear : indicator.getValuesByYears()) {
                        PlanFactYear pfy = new PlanFactYear();
                        pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                        pfy.setPlan(valByYear.getPlan());
                        pfy.setFact(valByYear.getFact());
                        pfy.setId(valByYear.getId()); // database id
                        pfy.setGid(getRandId());
                        view.getCreationView().getBalanceOfDebtWidget().addChildToTreeStore(rootItem, pfy);
                    }
                }
            }
        }
        view.getCreationView().getBalanceOfDebtWidget().getYearBox().deselectAll();
        view.getCreationView().getBalanceOfDebtWidget().getYearBox().select(savedYears, true);
    }

    private ExploitationInvestments getValueFromExploitationInvestments() {
        ExploitationInvestments pi = new ExploitationInvestments();
        pi.setIncludeNds(view.getExploitationView().getInvestments().getNdsCheck().getValue());
        pi.setOnDate(WidgetUtils.getDate(view.getExploitationView().getInvestments().getDateField().getValue()));
        if (view.getExploitationView().getInvestments().getMeasureType().getCurrentValue() != null)
            pi.setMeasureType(new MeasureType() {{
                setId(Long.valueOf(view.getExploitationView().getInvestments().getMeasureType().getCurrentValue().getId()));
                setName(view.getExploitationView().getInvestments().getMeasureType().getCurrentValue().getName());
            }});

        for (PlanFactYear indicator : view.getExploitationView().getInvestments().getRootItems()) {
            ExploitationInvestmentIndicator exploitationInvestmentIndicator = new ExploitationInvestmentIndicator();
            exploitationInvestmentIndicator.setId(indicator.getId()); // Это id в бд
            exploitationInvestmentIndicator.setFact(indicator.getFact());
            exploitationInvestmentIndicator.setPlan(indicator.getPlan());
            exploitationInvestmentIndicator.setType(new InvestmentIndicatorType() {{
                setId(indicator.getGid());
            }}); // Это общие инвестиции и т.д.
            if (!indicator.getGid().equals(3L) && !indicator.getGid().equals(2L)) {
                for (PlanFactYear year : view.getExploitationView().getInvestments().getTreeStore().getChildren(indicator)) {
                    ExploitationInvestmentIndicatorYearValue piYear = new ExploitationInvestmentIndicatorYearValue();
                    piYear.setId(year.getId());
                    piYear.setPlan(year.getPlan());
                    piYear.setFact(year.getFact());
                    piYear.setYear(Integer.parseInt(year.getNameOrYear()));
                    exploitationInvestmentIndicator.getValuesByYears().add(piYear);
                }
            }
            pi.getIndicators().add(exploitationInvestmentIndicator);
        }
        return pi;
    }

    private void setValueFromExploitationInvestments(ExploitationInvestments investments) {
        if (investments == null) return;
        view.getExploitationView().getInvestments().getNdsCheck().setValue(investments.isIncludeNds());
        view.getExploitationView().getInvestments().getDateField().setValue(getDate(investments.getOnDate()));
        if (investments.getMeasureType() != null) {
            selectComboById(view.getExploitationView().getInvestments().getMeasureType(), String.valueOf(investments.getMeasureType().getId()));
            if ("1".equals(String.valueOf(investments.getMeasureType().getId())))
                view.getExploitationView().getInvestments().getDateField().hide();
        }
        // get current selected years
        // предполагается, что года у всех показателей одинаковые
        List<String> savedYears = new ArrayList<>();
        if (investments.getIndicators().size() > 1)
            savedYears = investments.getIndicators().get(1).getValuesByYears()
                    .stream().map(i -> String.valueOf(i.getYear())).collect(Collectors.toList());

        for (PlanFactYear rootItem : view.getExploitationView().getInvestments().getRootItems()) {
            for (ExploitationInvestmentIndicator indicator : investments.getIndicators()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    if (!rootItem.getGid().equals(3L)) {
                        for (ExploitationInvestmentIndicatorYearValue valByYear : indicator.getValuesByYears()) {
                            PlanFactYear pfy = new PlanFactYear();
                            pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                            pfy.setPlan(valByYear.getPlan());
                            pfy.setFact(valByYear.getFact());
                            pfy.setId(valByYear.getId()); // database id
                            pfy.setGid(getRandId());
                            view.getExploitationView().getInvestments().addChildToTreeStore(rootItem, pfy);
                        }
                    }
                }
            }
        }
        view.getExploitationView().getInvestments().getYearBox().deselectAll();
        view.getExploitationView().getInvestments().getYearBox().select(savedYears, true);

    }

    private ExploitationInvestmentsRecovery getValueFromExploitationInvestmentsRecovery() {
        ExploitationInvestmentsRecovery pi = new ExploitationInvestmentsRecovery();
        pi.setIncludeNds(view.getExploitationView().getInvestmentsRecovery().getNdsCheck().getValue());
        pi.setOnDate(getDate(view.getExploitationView().getInvestmentsRecovery().getDateField().getValue()));
        if (view.getExploitationView().getInvestmentsRecovery().getMeasureType().getCurrentValue() != null)
            pi.setMeasureType(new MeasureType() {{
                setId(Long.valueOf(view.getExploitationView().getInvestmentsRecovery().getMeasureType().getCurrentValue().getId()));
                setName(view.getExploitationView().getInvestmentsRecovery().getMeasureType().getCurrentValue().getName());
            }});

        for (PlanFactYear indicator : view.getExploitationView().getInvestmentsRecovery().getTreeStore().getRootItems()) {
            ExploitationInvestmentRecoveryIndicator ind = new ExploitationInvestmentRecoveryIndicator();
            ind.setId(indicator.getId()); // Это id в бд
            ind.setFact(indicator.getFact());
            ind.setPlan(indicator.getPlan());
            ind.setType(new InvestmentIndicatorType() {{
                setId(indicator.getGid());
            }}); // Это общие инвестиции и т.д.
            for (PlanFactYear year : view.getExploitationView().getInvestmentsRecovery().getTreeStore().getChildren(indicator)) {
                ExploitationInvestmentRecoveryIndicatorYearValue piYear = new ExploitationInvestmentRecoveryIndicatorYearValue();
                piYear.setId(year.getId());
                piYear.setPlan(year.getPlan());
                piYear.setFact(year.getFact());
                piYear.setYear(Integer.parseInt(year.getNameOrYear()));
                ind.getValuesByYears().add(piYear);
            }
            pi.getIndicators().add(ind);
        }
        return pi;
    }

    private Long getRandId() {
        return new Date().getTime() + new Random().nextInt();
    }

    private void setValueFromExploitationInvestmentsRecovery(ExploitationInvestmentsRecovery investments) {
        if (investments == null) return;
        view.getExploitationView().getInvestmentsRecovery().getNdsCheck().setValue(investments.isIncludeNds());
        view.getExploitationView().getInvestmentsRecovery().getDateField().setValue(getDate(investments.getOnDate()));
        if (investments.getMeasureType() != null) {
            selectComboById(view.getExploitationView().getInvestmentsRecovery().getMeasureType(), String.valueOf(investments.getMeasureType().getId()));
            if ("1".equals(String.valueOf(investments.getMeasureType().getId())))
                view.getExploitationView().getInvestmentsRecovery().getDateField().hide();
        }
        // get current selected years
        // предполагается, что года у всех показателей одинаковые и индикатор есть всегда (жестко установленный)
        List<String> savedYears = new ArrayList<>();
        if (investments.getIndicators().size() > 0)
            savedYears = investments.getIndicators().get(0).getValuesByYears()
                    .stream().map(i -> String.valueOf(i.getYear())).collect(Collectors.toList());

        for (PlanFactYear rootItem : view.getExploitationView().getInvestmentsRecovery().getTreeStore().getRootItems()) {
            for (ExploitationInvestmentRecoveryIndicator indicator : investments.getIndicators()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    for (ExploitationInvestmentRecoveryIndicatorYearValue valByYear : indicator.getValuesByYears()) {
                        PlanFactYear pfy = new PlanFactYear();
                        pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                        pfy.setPlan(valByYear.getPlan());
                        pfy.setFact(valByYear.getFact());
                        pfy.setId(valByYear.getId()); // database id
                        pfy.setGid(getRandId());
                        view.getExploitationView().getInvestmentsRecovery().addChildToTreeStore(rootItem, pfy);
                    }
                }
            }
        }
        view.getExploitationView().getInvestmentsRecovery().getYearBox().deselectAll();
        view.getExploitationView().getInvestmentsRecovery().getYearBox().select(savedYears, true);
    }

    private void setValueToTechEconomicsFromView() {
        managedProject.getOdTechEconomicsObjectIndicators().clear();
        for (TepIndicatorByYear objectFromView : view.getObjectDescriptionView().getTechEcononomyIndicatorsWidget().getTreeStore().getRootItems()) {
            TechEconomicsObjectIndicator object = new TechEconomicsObjectIndicator();
            object.setId(objectFromView.getId());
            object.setName(objectFromView.getName());
            for (TepIndicatorByYear indicator : view.getObjectDescriptionView().getTechEcononomyIndicatorsWidget().getTreeStore().getChildren(objectFromView)) {
                TechEconomicsIndicator ind = new TechEconomicsIndicator();
                ind.setId(indicator.getId());
                ind.setFact(indicator.getFact());
                ind.setPlan(indicator.getPlan());
                ind.setName(indicator.getName());
                if (indicator.getUm() != null)
                    ind.setUm(new UnitOfMeasure() {{
                        setId(indicator.getUm().getId());
                    }});
                object.getTechEconomicsIndicators().add(ind);
                for (TepIndicatorByYear byYear : view.getObjectDescriptionView().getTechEcononomyIndicatorsWidget().getTreeStore().getChildren(indicator)) {
                    TechEconomicsIndicatorYearValue y = new TechEconomicsIndicatorYearValue();
                    y.setId(byYear.getId());
                    y.setFact(byYear.getFact());
                    y.setPlan(byYear.getPlan());
                    y.setYear(Integer.parseInt(byYear.getName()));
                    ind.getYearValues().add(y);
                }
            }
            managedProject.getOdTechEconomicsObjectIndicators().add(object);
        }
    }

    private void setValueFromTechEconomicsToView() {
        view.getObjectDescriptionView().getTechEcononomyIndicatorsWidget().getYearBox().deselectAll();
        for (TechEconomicsObjectIndicator object : managedProject.getOdTechEconomicsObjectIndicators()) {
            TepIndicatorMain objectToView = new TepIndicatorMain();
            objectToView.setId(object.getId());
            objectToView.setGid(object.getId());
            objectToView.setName(object.getName());
            view.getObjectDescriptionView().getTechEcononomyIndicatorsWidget().getTreeStore().add(objectToView);
            for (TechEconomicsIndicator indicator : object.getTechEconomicsIndicators()) {
                TepIndicatorMain i = new TepIndicatorMain();
                i.setId(indicator.getId());
                i.setGid(indicator.getId());
                i.setPlan(indicator.getPlan());
                i.setFact(indicator.getFact());
                i.setName(indicator.getName());
                if (indicator.getUm() != null)
                    i.setUm(new UmModel() {{
                        setId(indicator.getUm().getId());
                        setName(indicator.getUm().getName());
                    }});
                view.getObjectDescriptionView().getTechEcononomyIndicatorsWidget().getTreeStore().add(objectToView, i);
                for (TechEconomicsIndicatorYearValue yearValue : indicator.getYearValues()) {
                    TepIndicatorByYear child = new TepIndicatorByYear();
                    child.setGid(getRandId());
                    child.setId(yearValue.getId());
                    child.setFact(yearValue.getFact());
                    child.setPlan(yearValue.getPlan());
                    child.setName(String.valueOf(yearValue.getYear()));
                    view.getObjectDescriptionView().getTechEcononomyIndicatorsWidget().getTreeStore().add(i, child);
                }
            }
        }
    }

//    private void setValueFromBalanceOfDebtToView() {
//        view.getCreationView().getBalanceOfDebtWidget().getYearBox().deselectAll();
//        if (managedProject.getCrBalanceOfDebt() != null && managedProject.getCrBalanceOfDebt().size() > 0) {
//            List<String> savedYears = managedProject.getCrBalanceOfDebt()
//                    .stream().map(i -> i.getPeriod()).collect(Collectors.toList());
//            view.getCreationView().getBalanceOfDebtWidget().getYearBox().select(savedYears, true);
//        }
//        for (BalanceOfDebt indicator : managedProject.getCrBalanceOfDebt()) {
//            view.getCreationView().getBalanceOfDebtWidget().getBalanceOfDebtGrid().getStore().findModelWithKey(indicator.getPeriod()).setFactValue(indicator.getFactValue());
//        }
//    }
//
//    private void setValueToBalanceOfDebtFromView() {
//        managedProject.getCrBalanceOfDebt().clear();
//        for (BalanceOfDebt indicator : view.getCreationView().getBalanceOfDebtWidget().getBalanceOfDebtGrid().getStore().getAll()) {
//            BalanceOfDebt ind = new BalanceOfDebt();
//            ind.setId(indicator.getId());
//            ind.setPeriod(indicator.getPeriod());
//            ind.setFactValue(indicator.getFactValue());
//            managedProject.getCrBalanceOfDebt().add(ind);
//        }
//    }

    private void setExploitationPaymentsFromView() {
        GWT.log("setting setExploitationPaymentsFromView");
        PlanFactYear total = view.getExploitationView().getPayments().getTreeStore().getRootItems().get(0);
        ExploitationPayment exPayment = new ExploitationPayment();
        exPayment.setId(managedProject.getId());
        exPayment.setPlan(total.getPlan());
        exPayment.setFact(total.getFact());
        for (PlanFactYear child : view.getExploitationView().getPayments().getTreeStore().getChildren(total)) {
            ExploitationPaymentByYears by = new ExploitationPaymentByYears();
            by.setFact(child.getFact());
            by.setPlan(child.getPlan());
            by.setYear(Integer.valueOf(child.getNameOrYear()));
            by.setId(child.getId());
            exPayment.getValuesByYears().add(by);
        }
        managedProject.setExPayment(exPayment);
    }

    private void setExploitationPaymentsToView() {
        ExploitationPayment exPayment = managedProject.getExPayment();
        if (exPayment == null) return;
        List<Integer> savedYears = exPayment.getValuesByYears()
                .stream().map(i -> i.getYear()).collect(Collectors.toList());
        view.getExploitationView().getPayments().getCurrentYears().clear();
        view.getExploitationView().getPayments().getCurrentYears().addAll(savedYears);

        PlanFactYear total = view.getExploitationView().getPayments().getTreeStore().getRootItems().get(0);
        total.setPlan(exPayment.getPlan());
        total.setFact(exPayment.getFact());
        total.setId(managedProject.getId());
        for (ExploitationPaymentByYears valuesByYear : exPayment.getValuesByYears()) {
            PlanFactYear y = new PlanFactYear();
            y.setNameOrYear(String.valueOf(valuesByYear.getYear()));
            y.setFact(valuesByYear.getFact());
            y.setPlan(valuesByYear.getPlan());
            y.setId(valuesByYear.getId());
            y.setGid(getRandId());
            view.getExploitationView().getPayments().getTreeStore().add(total, y);
        }
    }

    private void setExploitationCompensationsFromView() {
        GWT.log("setting setExploitationCompensationsFromView");
        PlanFactYear total = view.getExploitationView().getCompensations().getTreeStore().getRootItems().get(0);
        ExploitationCompensation exCompensation = new ExploitationCompensation();

        exCompensation.setIncludeNds(view.getExploitationView().getCompensations().getNdsCheck().getValue());
        exCompensation.setOnDate(getDate(view.getExploitationView().getCompensations().getDateField().getValue()));
        if (view.getExploitationView().getCompensations().getMeasureType().getCurrentValue() != null)
            exCompensation.setMeasureType(new MeasureType() {{
                setId(Long.valueOf(view.getExploitationView().getCompensations().getMeasureType().getCurrentValue().getId()));
                setName(view.getExploitationView().getCompensations().getMeasureType().getCurrentValue().getName());
            }});

        exCompensation.setId(managedProject.getId());
        exCompensation.setPlan(total.getPlan());
        exCompensation.setFact(total.getFact());
        for (PlanFactYear child : view.getExploitationView().getCompensations().getTreeStore().getChildren(total)) {
            ExploitationCompensationByYears by = new ExploitationCompensationByYears();
            by.setFact(child.getFact());
            by.setPlan(child.getPlan());
            by.setYear(Integer.valueOf(child.getNameOrYear()));
            by.setId(child.getId());
            exCompensation.getValuesByYears().add(by);
        }
        managedProject.setExCompensation(exCompensation);
    }

    private void setExploitationCompensationsToView() {
        ExploitationCompensation exCompensation = managedProject.getExCompensation();
        if (exCompensation == null) return;

        view.getExploitationView().getCompensations().getNdsCheck().setValue(exCompensation.isIncludeNds());
        view.getExploitationView().getCompensations().getDateField().setValue(getDate(exCompensation.getOnDate()));
        if (exCompensation.getMeasureType() != null) {
            selectComboById(view.getExploitationView().getCompensations().getMeasureType(), String.valueOf(exCompensation.getMeasureType().getId()));
            if ("1".equals(String.valueOf(exCompensation.getMeasureType().getId())))
                view.getExploitationView().getCompensations().getDateField().hide();
        }

        List<Integer> savedYears = exCompensation.getValuesByYears()
                .stream().map(i -> i.getYear()).collect(Collectors.toList());
        view.getExploitationView().getCompensations().getCurrentYears().clear();
        view.getExploitationView().getCompensations().getCurrentYears().addAll(savedYears);

        PlanFactYear total = view.getExploitationView().getCompensations().getTreeStore().getRootItems().get(0);
        total.setPlan(exCompensation.getPlan());
        total.setFact(exCompensation.getFact());
        total.setId(managedProject.getId());
        for (ExploitationCompensationByYears valuesByYear : exCompensation.getValuesByYears()) {
            PlanFactYear y = new PlanFactYear();
            y.setNameOrYear(String.valueOf(valuesByYear.getYear()));
            y.setFact(valuesByYear.getFact());
            y.setPlan(valuesByYear.getPlan());
            y.setId(valuesByYear.getId());
            y.setGid(getRandId());
            view.getExploitationView().getCompensations().getTreeStore().add(total, y);
        }
    }

    private void setBankGuaranteeFromView() {
        GWT.log("setting bank guaranee");
        PlanFactYear total = view.getCreationView().getBankGuaranteeByYears().getTreeStore().getRootItems().get(0);
        BankGuarantee bankGuarantee = new BankGuarantee();
        bankGuarantee.setId(managedProject.getId());
        bankGuarantee.setPlan(total.getPlan());
        bankGuarantee.setFact(total.getFact());
        for (PlanFactYear child : view.getCreationView().getBankGuaranteeByYears().getTreeStore().getChildren(total)) {
            BankGuaranteeByYears by = new BankGuaranteeByYears();
            by.setFact(child.getFact());
            by.setPlan(child.getPlan());
            by.setYear(Integer.valueOf(child.getNameOrYear()));
            by.setId(child.getId());
            bankGuarantee.getValuesByYears().add(by);
        }
        managedProject.setCrBankGuaranteeByYears(bankGuarantee);
    }

    private void setBankGuaranteeToView() {
        BankGuarantee bankGuarantee = managedProject.getCrBankGuaranteeByYears();
        if (bankGuarantee == null) return;

        List<Integer> savedYears = bankGuarantee.getValuesByYears()
                .stream().map(i -> i.getYear()).collect(Collectors.toList());
        view.getCreationView().getBankGuaranteeByYears().getCurrentYears().clear();
        view.getCreationView().getBankGuaranteeByYears().getCurrentYears().addAll(savedYears);

        PlanFactYear total = view.getCreationView().getBankGuaranteeByYears().getTreeStore().getRootItems().get(0);
        total.setPlan(bankGuarantee.getPlan());
        total.setFact(bankGuarantee.getFact());
        total.setId(managedProject.getId());
        for (BankGuaranteeByYears valuesByYear : bankGuarantee.getValuesByYears()) {
            PlanFactYear y = new PlanFactYear();
            y.setNameOrYear(String.valueOf(valuesByYear.getYear()));
            y.setFact(valuesByYear.getFact());
            y.setPlan(valuesByYear.getPlan());
            y.setId(valuesByYear.getId());
            y.setGid(getRandId());
            view.getCreationView().getBankGuaranteeByYears().getTreeStore().add(total, y);
        }
    }

    private void setExBankGuaranteeFromView() {
        GWT.log("setting ex bank guarantee");
        PlanFactYear total = view.getExploitationView().getBankGuaranteeByYears().getTreeStore().getRootItems().get(0);
        ExBankGuarantee bankGuarantee = new ExBankGuarantee();
        bankGuarantee.setId(managedProject.getId());
        bankGuarantee.setPlan(total.getPlan());
        bankGuarantee.setFact(total.getFact());
        for (PlanFactYear child : view.getExploitationView().getBankGuaranteeByYears().getTreeStore().getChildren(total)) {
            ExBankGuaranteeByYears by = new ExBankGuaranteeByYears();
            by.setFact(child.getFact());
            by.setPlan(child.getPlan());
            by.setYear(Integer.valueOf(child.getNameOrYear()));
            by.setId(child.getId());
            bankGuarantee.getValuesByYears().add(by);
        }
        managedProject.setExBankGuaranteeByYears(bankGuarantee);
    }

    private void setExBankGuaranteeToView() {
        ExBankGuarantee bankGuarantee = managedProject.getExBankGuaranteeByYears();
        if (bankGuarantee == null) return;

        List<Integer> savedYears = bankGuarantee.getValuesByYears()
                .stream().map(i -> i.getYear()).collect(Collectors.toList());
        view.getExploitationView().getBankGuaranteeByYears().getCurrentYears().clear();
        view.getExploitationView().getBankGuaranteeByYears().getCurrentYears().addAll(savedYears);

        PlanFactYear total = view.getExploitationView().getBankGuaranteeByYears().getTreeStore().getRootItems().get(0);
        total.setPlan(bankGuarantee.getPlan());
        total.setFact(bankGuarantee.getFact());
        total.setId(managedProject.getId());
        for (ExBankGuaranteeByYears valuesByYear : bankGuarantee.getValuesByYears()) {
            PlanFactYear y = new PlanFactYear();
            y.setNameOrYear(String.valueOf(valuesByYear.getYear()));
            y.setFact(valuesByYear.getFact());
            y.setPlan(valuesByYear.getPlan());
            y.setId(valuesByYear.getId());
            y.setGid(getRandId());
            view.getExploitationView().getBankGuaranteeByYears().getTreeStore().add(total, y);
        }
    }

    private void setExEnsureMethodsToView() {
        view.getExploitationView().getCompensMethodOfExecuteObligation().getGrid().getStore().clear();
        for (ExploitationEnsureMethod ensureMethod : managedProject.getExEnsureMethods()) {
            ExEnsureMethodModel model = new ExEnsureMethodModel();
            model.setId(ensureMethod.getId());
            model.setGid(ensureMethod.getId());
            model.setEnsureMethodId(ensureMethod.getEnsureMethodId());
            model.setEnsureMethodName(ensureMethod.getEnsureMethodName());
            model.setTerm(ensureMethod.getTerm());
            model.setValue(ensureMethod.getValue());
            model.setSubmissionDate(ensureMethod.getSubmissionDate());
            model.setRiskType(ensureMethod.getRiskType());
            view.getExploitationView().getCompensMethodOfExecuteObligation().getGrid().getStore().add(model);
        }
    }

    private void setExEnsureMethodsFromView() {
        managedProject.getExEnsureMethods().clear();
        for (ExEnsureMethodModel model : view.getExploitationView().getCompensMethodOfExecuteObligation().getGrid().getStore().getAll()) {
            ExploitationEnsureMethod e = new ExploitationEnsureMethod();
            e.setId(model.getId());
            e.setEnsureMethodId(model.getEnsureMethodId());
            e.setEnsureMethodName(model.getEnsureMethodName());
            e.setTerm(model.getTerm());
            e.setValue(model.getValue());
            e.setRiskType(model.getRiskType());
            e.setSubmissionDate(model.getSubmissionDate());
            managedProject.getExEnsureMethods().add(e);
        }
    }

    private void updateProjectFromView() {
        managedProject.setGiName(view.getGeneralInformationView().getProjectNameField().getCurrentValue());

        String realizationFormId = getStringIdOrNull(view.getGeneralInformationView().getRealizationForm().getValue());
        if (realizationFormId != null) {
            managedProject.setGiRealizationForm(new RealizationForm() {{
                setId(realizationFormId);
            }});
        } else {
            managedProject.setGiRealizationForm(null);
        }

        String initiationMethod = getStringIdOrNull(view.getGeneralInformationView().getInitMethod().getCurrentValue());
        if (initiationMethod != null) {
            managedProject.setGiInitiationMethod(new InitiationMethod() {{
                setId(initiationMethod);
            }});
        } else {
            managedProject.setGiInitiationMethod(null);
        }

        String implementationLvl = getStringIdOrNull(view.getGeneralInformationView().getImplementationLvl().getCurrentValue());
        if (implementationLvl != null) {
            managedProject.setGiRealizationLevel(new RealizationLevel() {{
                setId(implementationLvl);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setGiRealizationLevel(null);
//        }

        managedProject.setGiIsRFPartOfAgreement(view.getGeneralInformationView().getIsRFPartOfAgreement().getValue());
        managedProject.setGiIsRegionPartOfAgreement(view.getGeneralInformationView().getIsRegionPartOfAgreement().getValue());
        managedProject.setGiIsMunicipalityPartOfAgreement(view.getGeneralInformationView().getIsMunicipalityPartOfAgreement().getValue());

        String rfRegionId = getStringIdOrNull(view.getGeneralInformationView().getRegion().getCurrentValue());
        if (rfRegionId != null) {
            managedProject.setGiRegion(new DicGasuSp1() {{
                setId(rfRegionId);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setGiRegion(null);
//        }

        String municipalityId = getStringIdOrNull(view.getGeneralInformationView().getMunicipality().getCurrentValue());
        if (municipalityId != null) {
            managedProject.setGiMunicipality(new Municipality() {{
                setId(municipalityId);
            }});
        } else {
            managedProject.setGiMunicipality(null);
        }

        SvrOrg ppk = view.getGeneralInformationView().getPpk().getCurrentValue();
        if (ppk != null) {
            managedProject.setGiPublicPartner(new SvrOrg() {{
                setId(ppk.getId());
            }});
            managedProject.setGiPublicPartnerOgrn(ppk.getOgrn());
        } else {
            managedProject.setGiPublicPartner(null);
            managedProject.setGiPublicPartnerOgrn(null);
        }

        managedProject.setGiInn(view.getGeneralInformationView().getInn().getValue());
        managedProject.setGiBalanceHolder(view.getGeneralInformationView().getBalanceHolder().getValue());

        managedProject.setGiImplementer(view.getGeneralInformationView().getImplementer().getValue());
        managedProject.setGiImplementerInn(view.getGeneralInformationView().getImplementerInn().getValue());

        String opfId = getStringIdOrNull(view.getGeneralInformationView().getOpf().getValue());
        if (opfId != null) {
            managedProject.setGiOPF(new OpfEntity() {{
                setId(opfId);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setGiOPF(null);
//        }

        managedProject.setGiIsForeignInvestor(view.getGeneralInformationView().getIsForeignInvestor().getValue());
        managedProject.setGiIsMcpSubject(view.getGeneralInformationView().getIsMcpSubject().getValue());
        managedProject.setGiIsSpecialProjectCompany(view.getGeneralInformationView().getIsSpecialProjectCompany().getValue());
        managedProject.setGiHasInvestmentProperty(view.getGeneralInformationView().getHasInvestmentProperty().getValue());
        managedProject.setGiPublicSharePercentage(view.getGeneralInformationView().getPublicSharePercentage().getCurrentValue());
        managedProject.setGiIsRFHasShare(view.getGeneralInformationView().getIsRFHasShare().getValue());

        String realizationSphereId = getStringIdOrNull(view.getGeneralInformationView().getImplementationSphere().getCurrentValue());
        if (realizationSphereId != null) {
            managedProject.setGiRealizationSphere(new RealizationSphereEntity() {{
                setId(realizationSphereId);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setGiRealizationSphere(null);
//        }

        String realizationSectorId = getStringIdOrNull(view.getGeneralInformationView().getImplementationSector().getCurrentValue());
        if (realizationSectorId != null) {
            managedProject.setGiRealizationSector(new RealizationSectorEntity() {{
                setId(realizationSectorId);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setGiRealizationSector(null);
//        }

        managedProject.getGiObjectType().clear();
        managedProject.getGiObjectType().addAll(view.getGeneralInformationView().getObjectType().getSelectedItems().stream().map(i -> {
            ObjectKind objectKind = new ObjectKind();
            objectKind.setId(i.getId());
            return objectKind;
        }).collect(Collectors.toList()));

        managedProject.setGiObjectLocation(view.getGeneralInformationView().getObjectLocation().getCurrentValue());

        managedProject.getGiAgreementSubject().clear();
        managedProject.getGiAgreementSubject().addAll(view.getGeneralInformationView().getAgreementSubject().getSelectedItems().stream().map(i -> {
            AgreementSubject agreementSubject = new AgreementSubject();
            agreementSubject.setId(i.getId());
            return agreementSubject;
        }).collect(Collectors.toList()));

        String realizationStatusId = getStringIdOrNull(view.getGeneralInformationView().getRealizationStatus().getCurrentValue());
        if (realizationStatusId != null) {
            managedProject.setGiRealizationStatus(new RealizationStatus() {{
                setId(realizationStatusId);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setGiRealizationStatus(null);
//        }

        managedProject.setOdObjectName(view.getObjectDescriptionView().getObjectNameField().getCurrentValue());
        managedProject.setOdObjectDescription(view.getObjectDescriptionView().getObjectCharacteristic().getCurrentValue());
        managedProject.setOdIsPropertyStaysWithPrivateSide(view.getObjectDescriptionView().getIsPropertyStaysWithPrivateSide().getValue());
        managedProject.setOdIsPropertyStaysWithPrivateSide(view.getObjectDescriptionView().getIsPropertyStaysWithPrivateSide().getValue());
        managedProject.setOdIsNewPropertyBeGivenToPrivateSide(view.getObjectDescriptionView().getIsNewPropertyBeGivenToPrivateSide().getValue());
        managedProject.setOdIsObjectImprovementsGiveAway(view.getObjectDescriptionView().getIsObjectImprovementsGiveAway().getValue());
        managedProject.setOdRentObject(getLongIdOrNull(view.getObjectDescriptionView().getRentObject().getValue()));

        setValueToTechEconomicsFromView();
        setEnergyEfficiencyPlanFromView();

        managedProject.getOdRentPassportFileVersionId().clear();
        managedProject.getOdRentPassportFileVersionId().addAll(
                view.getObjectDescriptionView().getFileUploader().getCurrentFiles()
                        .stream().map(i -> new RentPassportFilesEntity() {{
                            setId(i.getFileVersionId());
                            setFileName(i.getFileName());
                        }}).collect(Collectors.toList()));

        managedProject.setPpIsObjInListWithConcessionalAgreements(view.getProjectPreparationView().getIsObjInListWithConcessionalAgreements().getValue());
        managedProject.setPpObjectsListUrl(view.getProjectPreparationView().getObjectsListUrl().getValue());
        managedProject.setPpSupposedPrivatePartnerName(view.getProjectPreparationView().getSupposedPrivatePartnerName().getCurrentValue());
        managedProject.setPpSupposedPrivatePartnerInn(view.getProjectPreparationView().getSupposedPrivatePartnerInn().getCurrentValue());
        managedProject.setPpIsForeignInvestor(view.getProjectPreparationView().getIsForeignInvestor().getValue());
        managedProject.setPpIsMspSubject(view.getProjectPreparationView().getIsMcpSubject().getValue());

        Long agreementsSetId = getLongIdOrNull(view.getProjectPreparationView().getAgreementsSet().getValue());
        if (agreementsSetId != null) {
            managedProject.setPpAgreementsSet(new AgreementsSetEntity() {{
                setId(agreementsSetId);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setPpAgreementsSet(null);
//        }

        managedProject.setPpSupposedAgreementStartDate(getDate(view.getProjectPreparationView().getSupposedAgreementStartDate().getValue()));
        managedProject.setPpSupposedAgreementEndDate(getDate(view.getProjectPreparationView().getSupposedAgreementEndDate().getValue()));
        managedProject.setPpSupposedValidityYears(view.getProjectPreparationView().getSupposedValidityYears().getValue());
        managedProject.setPpDeliveryTimeOfGoodsWorkDate(getDate(view.getProjectPreparationView().getDeliveryTimeOfGoodsWorkDate().getValue()));
        managedProject.setPpGroundsOfAgreementConclusion(getLongIdOrNull(view.getProjectPreparationView().getGroundsOfAgreementConclusion().getCurrentValue()));
        managedProject.setPpActNumber(view.getProjectPreparationView().getActNumber().getCurrentValue());
        managedProject.setPpActDate(getDate(view.getProjectPreparationView().getActDate().getValue()));
        managedProject.setPpProposalPublishDate(getDate(view.getProjectPreparationView().getProposalPublishDate().getValue()));
        managedProject.setPpTorgiGovRuUrl(view.getProjectPreparationView().getTorgiGovRuUrl().getCurrentValue());
        managedProject.setPpIsReadinessRequestReceived(view.getProjectPreparationView().getIsReadinessRequestReceived().getValue());
        managedProject.setPpIsDecisionMadeToConcludeAnAgreement(view.getProjectPreparationView().getIsDecisionMadeToConcludeAnAgreement().getValue());
        managedProject.setPpConcludeAgreementActNum(view.getProjectPreparationView().getConclustionActNumber().getValue());
        managedProject.setPpConcludeAgreementActDate(getDate(view.getProjectPreparationView().getConclusionActDate().getValue()));
        managedProject.setPpInvestmentStageDurationDate(getDate(view.getProjectPreparationView().getInvestmentStageDurationDate().getValue()));
        managedProject.setPpConcludeAgreementIsSigned(view.getProjectPreparationView().getIsAgreementSigned().getValue());
        managedProject.setPpConcludeAgreementIsJoint(view.getProjectPreparationView().getJointCompetition().getValue());
        managedProject.setPpConcludeAgreementOtherPjInfo(view.getProjectPreparationView().getAnotherProjectsInfo().getValue());
        managedProject.setPpConcludeAgreementOtherPjIdent(view.getProjectPreparationView().getAnotherProjectsIdentifier().getCurrentValue());
        managedProject.setPpCompetitionBidCollEndPlanDate(getDate(view.getProjectPreparationView().getCompetitionBidCollEndPlanDate().getValue()));
        managedProject.setPpCompetitionBidCollEndFactDate(getDate(view.getProjectPreparationView().getCompetitionBidCollEndFactDate().getValue()));
        managedProject.setPpCompetitionTenderOfferEndPlanDate(getDate(view.getProjectPreparationView().getCompetitionTenderOfferEndPlanDate().getValue()));
        managedProject.setPpCompetitionTenderOfferEndFactDate(getDate(view.getProjectPreparationView().getCompetitionTenderOfferEndFactDate().getValue()));
        managedProject.setPpCompetitionResultsPlanDate(getDate(view.getProjectPreparationView().getCompetitionResultsPlanDate().getValue()));
        managedProject.setPpCompetitionResultsFactDate(getDate(view.getProjectPreparationView().getCompetitionResultsFactDate().getValue()));
        managedProject.setPpCompetitionIsElAuction(view.getProjectPreparationView().getCompetitionIsElAuction().getValue());
        managedProject.setPpCompetitionHasResults(view.getProjectPreparationView().getCompetitionHasResults().getValue());
        managedProject.setPpCompetitionResults(getLongIdOrNull(view.getProjectPreparationView().getCompetitionResults().getCurrentValue()));
        managedProject.setPpCompetitionResultsProtocolNum(view.getProjectPreparationView().getCompetitionResultsProtocolNum().getValue());
        managedProject.setPpCompetitionResultsProtocolDate(getDate(view.getProjectPreparationView().getCompetitionResultsProtocolDate().getValue()));
        managedProject.setPpCompetitionResultsParticipantsNum(view.getProjectPreparationView().getCompetitionResultsParticipantsNum().getValue());
        managedProject.setPpCompetitionResultsSignStatus(getLongIdOrNull(view.getProjectPreparationView().getCompetitionResultsSignStatus().getCurrentValue()));
        managedProject.setPpContractPriceOrder(getLongIdOrNull(view.getProjectPreparationView().getContractPriceOrder().getCurrentValue()));
        managedProject.setPpContractPriceFormula(view.getProjectPreparationView().getContractPriceFormula().getCurrentValue());
        managedProject.setPpContractPricePrice(view.getProjectPreparationView().getContractPricePrice().getValue());

        managedProject.setPpNdsCheck(view.getProjectPreparationView().getNdsCheck().getValue());
        managedProject.setPpDateField(getDate(view.getProjectPreparationView().getDateField().getValue()));
        managedProject.setPpMeasureType(getLongIdOrNull(view.getProjectPreparationView().getMeasureType().getValue()));

        managedProject.setPpContractPriceOffer(getLongIdOrNull(view.getProjectPreparationView().getContractPriceOffer().getCurrentValue()));
        managedProject.setPpContractPriceOfferValue(view.getProjectPreparationView().getContractPriceOfferValue().getValue());
        Long winnerContractPriceOfferId = getLongIdOrNull(view.getProjectPreparationView().getWinnerContractPriceOffer().getValue());
        if (winnerContractPriceOfferId != null) {
            managedProject.setPpWinnerContractPriceOffer(new WinnerContractPriceOfferEntity() {{
                setId(winnerContractPriceOfferId);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setPpWinnerContractPriceOffer(null);
//        }
        managedProject.setPpContractPriceSavingStartDate(getDate(view.getProjectPreparationView().getContractPriceSavingStartDate().getValue()));
        managedProject.setPpContractPriceSavingEndDate(getDate(view.getProjectPreparationView().getContractPriceSavingEndDate().getValue()));
        managedProject.setPpPrivatePartnerCostRecoveryMethod(getLongIdOrNull(view.getProjectPreparationView().getPublicPartnerCostRecoveryMethod().getCurrentValue()));
        managedProject.setPpAdvancePaymentAmount(view.getProjectPreparationView().getAdvancePaymentAmount().getValue());
        managedProject.setPpFirstObjectOperationDate(getDate(view.getProjectPreparationView().getFirstObjectOperationDate().getValue()));
        managedProject.setPpLastObjectCommissioningDate(getDate(view.getProjectPreparationView().getLastObjectCommissioningDate().getValue()));

        PlanInvestments values = getValueFromPlanningInvestments();
        values.setId(managedProject.getId());
        managedProject.setPpInvestmentPlanningAmount(values);

        PlanCreationInvestments planCreationInvestments = getValueFromPlanningCreationInvestments();
        planCreationInvestments.setId(managedProject.getId());
        managedProject.setPpCreationInvestmentPlanningAmount(planCreationInvestments);

        CbcInvestments1 cbcInvestments1 = getValueFromCbcInvestments1();
        cbcInvestments1.setId(managedProject.getId());
        managedProject.setCbcInvestments1(cbcInvestments1);

        CbcInvestments2 cbcInvestments2 = getValueFromCbcInvestments2();
        cbcInvestments2.setId(managedProject.getId());
        managedProject.setCbcInvestments2(cbcInvestments2);

        CbcInvestments3 cbcInvestments3 = getValueFromCbcInvestments3();
        cbcInvestments3.setId(managedProject.getId());
        managedProject.setCbcInvestments3(cbcInvestments3);

        CbcInvestments4 cbcInvestments4 = getValueFromCbcInvestments4();
        cbcInvestments4.setId(managedProject.getId());
        managedProject.setCbcInvestments4(cbcInvestments4);

        RemainingDebt remainingDebt = getValueFromRemainingDebt();
        remainingDebt.setId(managedProject.getId());
        managedProject.setRemainingDebt(remainingDebt);

        managedProject.getPpProjectAgreementFileVersionId().clear();
        managedProject.getPpProjectAgreementFileVersionId().addAll(view.getProjectPreparationView()
                .getProjectAgreementFileUploader().getCurrentFiles().stream()
                .map(i -> new ProjectAgreementFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getPpActTextFileVersionId().clear();
        managedProject.getPpActTextFileVersionId().addAll(view.getProjectPreparationView()
                .getActFileUploader().getCurrentFiles().stream()
                .map(i -> new ActTextFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getPpLeaseAgreementTextFileVersionId().clear();
        managedProject.getPpLeaseAgreementTextFileVersionId().addAll(view.getProjectPreparationView()
                .getLeaseAgreementUploader().getCurrentFiles().stream()
                .map(i -> new LeaseAgreementTextFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.setPpProjectAssignedStatus(view.getProjectPreparationView().getIsProjectAssignedStatus().getValue());

        managedProject.setPpDecisionNumber(view.getProjectPreparationView().getDecisionNumber().getValue());
        managedProject.setPpDecisionDate(getDate(view.getProjectPreparationView().getDecisionDate().getValue()));
        managedProject.getPpDecisionTextFileVersionId().clear();
        managedProject.getPpDecisionTextFileVersionId().addAll(view.getProjectPreparationView()
                .getDecisionFileUploader().getCurrentFiles().stream()
                .map(i -> new DecisionTextFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));


        managedProject.getPpProposalTextFileVersionId().clear();
        managedProject.getPpProposalTextFileVersionId().addAll(view.getProjectPreparationView()
                .getProposalTextFileUploader().getCurrentFiles().stream()
                .map(i -> new ProposalTextFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getPpProtocolFileVersionId().clear();
        managedProject.getPpProtocolFileVersionId().addAll(view.getProjectPreparationView()
                .getProtocolFileUploader().getCurrentFiles().stream()
                .map(i -> new ProtocolFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getPpConcludeAgreementFvId().clear();
        managedProject.getPpConcludeAgreementFvId().addAll(view.getProjectPreparationView()
                .getConclusionFileUploader().getCurrentFiles().stream()
                .map(i -> new ConcludeAgreementFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getPpCompetitionTextFVId().clear();
        managedProject.getPpCompetitionTextFVId().addAll(view.getProjectPreparationView()
                .getCompetitionTextFileUploader().getCurrentFiles().stream()
                .map(i -> new CompetitionText() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getPpCompetitionResultsProtocolTextFvId().clear();
        managedProject.getPpCompetitionResultsProtocolTextFvId().addAll(view.getProjectPreparationView()
                .getCompetitionResultsProtocolFileUploader().getCurrentFiles().stream()
                .map(i -> new CompetitionResultsProtocolFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getPpCompetitionResultsDocFvId().clear();
        managedProject.getPpCompetitionResultsDocFvId().addAll(view.getProjectPreparationView()
                .getCompetitionResultsDocFileUploader().getCurrentFiles().stream()
                .map(i -> new CompetitionResultsFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getPpFinancialModelFVId().clear();
        managedProject.getPpFinancialModelFVId().addAll(view.getProjectPreparationView()
                .getFinancialModelFileUpload().getCurrentFiles().stream()
                .map(i -> new FinancialModelFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getPpSupportingDocumentsFileVersionIds().clear();
        managedProject.getPpSupportingDocumentsFileVersionIds().addAll(view.getProjectPreparationView()
                .getSupportingDocumentsFileUploader().getCurrentFiles().stream()
                .map(i -> new SupportingDocumentsFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

//////////////////////////////////////////////
        managedProject.setCrAgreementComplex(getLongIdOrNull(view.getCreationView().getAgreementComplex().getCurrentValue()));
        managedProject.setPpBudgetExpendituresAgreementOnGchpMchp(view.getProjectPreparationView().getBudgetExpendituresAgreementOnGchpMchp().getValue());
        managedProject.setPpBudgetExpendituresGovContract(view.getProjectPreparationView().getBudgetExpendituresGovContract().getValue());
        managedProject.setPpObligationsInCaseOfRisksAgreementOnGchpMchp(view.getProjectPreparationView().getObligationsInCaseOfRisksAgreementOnGchpMchp().getValue());
        managedProject.setPpObligationsInCaseOfRisksGovContract(view.getProjectPreparationView().getObligationsInCaseOfRisksGovContract().getValue());


        managedProject.setPpIndicatorAssessmentComparativeAdvantage(view.getProjectPreparationView().getIndicatorAssessmentComparativeAdvantage().getValue());

        managedProject.getPpConclusionUOTextFileVersionId().clear();
        managedProject.getPpConclusionUOTextFileVersionId().addAll(view.getProjectPreparationView()
                .getConclusionUOTextFileUploader().getCurrentFiles().stream()
                .map(i -> new ConclusionUOTextFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getPpFinancialModelTextFileVersionId().clear();
        managedProject.getPpFinancialModelTextFileVersionId().addAll(view.getProjectPreparationView()
                .getFinancialModelTextFileUploader().getCurrentFiles().stream()
                .map(i -> new FinancialModelTextFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        Long methodOfExecuteObligationId = getLongIdOrNull(view.getProjectPreparationView().getMethodOfExecuteObligation().getValue());
        if (methodOfExecuteObligationId != null) {
            managedProject.setPpMethodOfExecuteObligation(new MethodOfExecuteObligationEntity() {{
                setId(methodOfExecuteObligationId);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setPpMethodOfExecuteObligation(null);
//        }

        managedProject.setPpLinkToClauseAgreement(view.getProjectPreparationView().getLinkToClauseAgreement().getValue());
        managedProject.setPpIsPrivateLiabilityProvided(view.getProjectPreparationView().getIsPrivateLiabilityProvided().getValue());
        managedProject.setPpLinkToClauseAgreementLiabilityProvided(view.getProjectPreparationView().getLinkToClauseAgreementLiabilityProvided().getValue());
        Long otherGovSupportsId = getLongIdOrNull(view.getProjectPreparationView().getOtherGovSupports().getValue());
        if (otherGovSupportsId != null) {
            managedProject.setPpStateSupportMeasuresSPIC(new OtherGovSupportsEntity() {{
                setId(otherGovSupportsId);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setPpStateSupportMeasuresSPIC(null);
//        }
        managedProject.setPpConcludeAgreementLink(view.getProjectPreparationView().getPpConcludeAgreementLink().getCurrentValue());
        managedProject.setPpImplementProject(view.getProjectPreparationView().getPpImplementProject().getValue());
        managedProject.setPpResultsOfPlacing(getLongIdOrNull(view.getProjectPreparationView().getPpResultsOfPlacing().getCurrentValue()));
        //////////////////////////////////////////////

        managedProject.setCrAgreementStartDate(getDate(view.getCreationView().getAgreementStartDate().getValue()));
        managedProject.setCrAgreementEndDate(getDate(view.getCreationView().getAgreementEndDate().getValue()));
        managedProject.setCrAgreementValidity(view.getCreationView().getAgreementValidity().getValue());
        managedProject.setCrJobDoneTerm(getDate(view.getCreationView().getJobDoneTerm().getValue()));
        managedProject.setCrSavingStartDate(getDate(view.getCreationView().getSavingStartDate().getValue()));
        managedProject.setCrSavingEndDate(getDate(view.getCreationView().getSavingEndDate().getValue()));
        managedProject.setCrInvestmentStageTerm(view.getCreationView().getInvestmentStageTerm().getValue());
        managedProject.setCrIsAutoProlongationProvided(view.getCreationView().getIsAutoProlongationProvided().getValue());
        managedProject.setCrAgreementEndDateAfterProlongation(getDate(view.getCreationView().getAgreementEndDateAfterProlongation().getValue()));
        managedProject.setCrAgreementValidityAfterProlongation(view.getCreationView().getAgreementValidityAfterProlongation().getValue());
        managedProject.setCrOpf(getLongIdOrNull(view.getCreationView().getOpf().getValue()));
        managedProject.setCrIsForeignInvestor(view.getCreationView().getIsForeignInvestor().getValue());
        managedProject.setCrIsMcpSubject(view.getCreationView().getIsMcpSubject().getValue());
        managedProject.setCrConcessionaire(view.getCreationView().getConcessionaire().getValue());
        managedProject.setCrConcessionaireInn(view.getCreationView().getConcessionaireInn().getValue());
        managedProject.setCrFinancialClosingProvided(view.getCreationView().getFinancialClosingProvided().getValue());
        managedProject.setCrFinancialClosingDate(getDate(view.getCreationView().getFinancialClosingDate().getValue()));
        managedProject.setCrFinancialClosingValue(view.getCreationView().getFinancialClosingValue().getValue());
        managedProject.setCrFinancialClosingIsMutualAgreement(view.getCreationView().getFinancialClosingIsMutualAgreement().getValue());
        managedProject.setCrFirstObjectCreationPlanDate(getDate(view.getCreationView().getFirstObjectCreationPlanDate().getValue()));
        managedProject.setCrFirstObjectCreationFactDate(getDate(view.getCreationView().getFirstObjectCreationFactDate().getValue()));


        managedProject.setCrIsRegionPartyAgreement(view.getCreationView().getIsRegionPartyAgreement().getValue());

        managedProject.setCrIsSeveralObjects(view.getCreationView().getIsSeveralObjects().getValue());

        managedProject.setCrFirstSeveralObjectPlanDate(getDate(view.getCreationView().getFirstSeveralObjectPlanDate().getValue()));

        managedProject.setCrIsFirstSeveralObject(view.getCreationView().getIsFirstSeveralObject().getValue());
        managedProject.setCrLastSeveralObjectPlanDate(getDate(view.getCreationView().getLastSeveralObjectPlanDate().getValue()));

        managedProject.setCrIsLastSeveralObject(view.getCreationView().getIsLastSeveralObject().getValue());
        managedProject.setCrSeveralObjectPlanDate(getDate(view.getCreationView().getSeveralObjectPlanDate().getCurrentValue()));

        managedProject.setCrIsSeveralObjectInOperation(view.getCreationView().getIsSeveralObjectInOperation().getValue());


        managedProject.setCrInvestCostsGrantor(view.getCreationView().getInvestCostsGrantor().getCurrentValue());
        managedProject.setCrIsFormulasInvestCosts(view.getCreationView().getIsFormulasInvestCosts().getValue());

        // CR files
        managedProject.getCrCalcInvestCostsActFVId().clear();
        managedProject.getCrCalcInvestCostsActFVId().addAll(view.getCreationView()
                .getCalcInvestCostsFileUpload().getCurrentFiles().stream()
                .map(i -> new CalcInvestCostsActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.setCrActualCostsValue(view.getCreationView().getActualCostsValue().getValue());

        managedProject.setCrAverageInterestRateValue(view.getCreationView().getAverageInterestRateValue().getValue());


        managedProject.setCrLastObjectCreationPlanDate(getDate(view.getCreationView().getLastObjectCreationPlanDate().getValue()));
        managedProject.setCrLastObjectCreationFactDate(getDate(view.getCreationView().getLastObjectCreationFactDate().getValue()));
        // table investments
        managedProject.setCrIsObjectTransferProvided(view.getCreationView().getIsObjectTransferProvided().getValue());
        managedProject.setCrObjectRightsPlanDate(getDate(view.getCreationView().getObjectRightsPlanDate().getValue()));
        managedProject.setCrObjectRightsFactDate(getDate(view.getCreationView().getObjectRightsFactDate().getValue()));
        // files
        managedProject.setCrObjectValue(view.getCreationView().getObjectValue().getValue());
        // files

        setEnsureMethodsFromView();
        managedProject.setCrLandProvided(view.getCreationView().getLandProvided().getValue());
        managedProject.setCrLandIsConcessionaireOwner(view.getCreationView().getLandIsConcessionaireOwner().getValue());
        managedProject.setCrLandActStartPlanDate(getDate(view.getCreationView().getLandActStartPlanDate().getValue()));
        managedProject.setCrLandActStartFactDate(getDate(view.getCreationView().getLandActStartFactDate().getValue()));
        managedProject.setCrLandActEndPlanDate(getDate(view.getCreationView().getLandActEndPlanDate().getValue()));
        managedProject.setCrLandActEndFactDate(getDate(view.getCreationView().getLandActEndFactDate().getValue()));
        managedProject.setCrIsObligationExecuteOnCreationStage(view.getCreationView().getIsObligationExecuteOnCreationStage().getValue());
        managedProject.getCrGovSupport().clear();
        managedProject.getCrGovSupport().addAll(view.getCreationView().getGovSupport().getSelectedItems().stream()
                .map(i -> {
                    GovSupport govSupport = new GovSupport();
                    govSupport.setId(Long.valueOf(i.getId()));
                    return govSupport;
                }).collect(Collectors.toList()));

        CreationInvestments creationInvestments = getValueFromCreationInvestments();
        creationInvestments.setId(managedProject.getId());
        managedProject.setCrInvestmentCreationAmount(creationInvestments);
        managedProject.setCrExpectedRepaymentYear(view.getCreationView().getExpectedRepaymentYear().getCurrentValue());
//        setValueToBalanceOfDebtFromView();
        // CR files
        managedProject.getCrAgreementTextFvId().clear();
        managedProject.getCrAgreementTextFvId().addAll(view.getCreationView()
                .getAgreementFileUpload().getCurrentFiles().stream()
                .map(i -> new AgreementTextFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getCrFinancialClosingActFVId().clear();
        managedProject.getCrFinancialClosingActFVId().addAll(view.getCreationView()
                .getFinancialClosingFileUpload().getCurrentFiles().stream()
                .map(i -> new FinancialClosingActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getCrFirstObjectCompleteActFVId().clear();
        managedProject.getCrFirstObjectCompleteActFVId().addAll(view.getCreationView()
                .getFirstObjectCompleteActFileUpload().getCurrentFiles().stream()
                .map(i -> new FirstObjectCompleteActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getCrLastObjectCompleteActFVId().clear();
        managedProject.getCrLastObjectCompleteActFVId().addAll(view.getCreationView()
                .getLastObjectCompleteActFileUpload().getCurrentFiles().stream()
                .map(i -> new LastObjectCompleteActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getCrInvestmentVolumeStagOfCreationActFVId().clear();
        managedProject.getCrInvestmentVolumeStagOfCreationActFVId().addAll(view.getCreationView()
                .getInvestmentVolumeStagOfCreationFileUpload().getCurrentFiles().stream()
                .map(i -> new InvestmentVolumeStagOfCreationActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));


        managedProject.getCrActFVId().clear();
        managedProject.getCrActFVId().addAll(view.getCreationView()
                .getActFileUpload().getCurrentFiles().stream()
                .map(i -> new InvestmentsActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.setCrIsRenewableBankGuarantee(view.getCreationView().getIsRenewableBankGuarantee().getValue());
        managedProject.setCrIsGuaranteeVariesByYear(view.getCreationView().getIsGuaranteeVariesByYear().getValue());

        setBankGuaranteeFromView();

        managedProject.getCrReferenceFVId().clear();
        managedProject.getCrReferenceFVId().addAll(view.getCreationView()
                .getReferenceFileUpload().getCurrentFiles().stream()
                .map(i -> new InvestmentsLinkFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getCrLandActFVId().clear();
        managedProject.getCrLandActFVId().addAll(view.getCreationView()
                .getLandActTextFileUpload().getCurrentFiles().stream()
                .map(i -> new LandActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getCrConfirmationDocFVId().clear();
        managedProject.getCrConfirmationDocFVId().addAll(view.getCreationView()
                .getConfirmationDocFileUpload().getCurrentFiles().stream()
                .map(i -> new ConfirmationDocFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getCrAgreementTextFiles().clear();
        managedProject.getCrAgreementTextFiles().addAll(view.getCreationView()
                .getAgreementTextFiles().getCurrentFiles().stream()
                .map(i -> new CreationAgreementFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }}).collect(Collectors.toList()));

        ///////////////////////////////////////////////////////////

        // Explotation
        managedProject.setExLastObjectPlanDate(getDate(view.getExploitationView().getLastObjectPlanDate().getValue()));
        managedProject.setExLastObjectFactDate(getDate(view.getExploitationView().getLastObjectFactDate().getValue()));
        //files
        // investments table
        managedProject.setExIsInvestmentsRecoveryProvided(view.getExploitationView().getIsInvestmentRecoveryProvided().getValue());
        // investments recovery table
        managedProject.setExIRSource(getLongIdOrNull(view.getExploitationView().getIrSource().getCurrentValue()));
        managedProject.setExIRLevel(getLongIdOrNull(view.getExploitationView().getIrBudgetLevel().getCurrentValue()));
        managedProject.setExIsObligationExecutingOnOperationPhase(view.getExploitationView().getIsObligationExecutingOnOperationPhase().getValue());
        ////managedProject.setExMethodOfExecOfPublicPartnerObligation(getLongIdOrNull(view.getExploitationView().getExecutionMethod().getCurrentValue()));
        ////managedProject.setExInvestmentRecoveryTerm(getDate(view.getExploitationView().getInvestmentRecoveryTerm().getValue()));
        ////managedProject.setExInvestmentRecoveryValue(view.getExploitationView().getInvestmentRecoveryValue().getValue());
        setExEnsureMethodsFromView();
        setExBankGuaranteeFromView();
        managedProject.setExIsRenewableBankGuarantee(view.getExploitationView().getIsRenewableBankGuarantee().getValue());
        managedProject.setExIsGuaranteeVariesByYear(view.getExploitationView().getIsGuaranteeVariesByYear().getValue());

        ExploitationInvestments exploitationInvestments = getValueFromExploitationInvestments();
        exploitationInvestments.setId(managedProject.getId());
        managedProject.setExInvestmentExploitationAmount(exploitationInvestments);

        managedProject.getExInvestmentVolumeStagOfExploitationActFVId().clear();
        managedProject.getExInvestmentVolumeStagOfExploitationActFVId().addAll(view.getExploitationView()
                .getInvestmentVolumeStagOfExploitationFileUpload().getCurrentFiles().stream()
                .map(i -> new InvestmentVolumeStagOfExploitationActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        ExploitationInvestmentsRecovery investmentsRecovery = getValueFromExploitationInvestmentsRecovery();
        investmentsRecovery.setId(managedProject.getId());
        managedProject.setExInvestmentExploitationRecoveryAmount(investmentsRecovery);

        // table
        managedProject.setExIsConcessionPayProvideded(view.getExploitationView().getIsConcessionPaymentProvided().getValue());
        managedProject.setExPaymentForm(getLongIdOrNull(view.getExploitationView().getPaymentMethod().getCurrentValue()));

        managedProject.getExLastObjectActFVId().clear();
        managedProject.getExLastObjectActFVId().addAll(view.getExploitationView()
                .getLastObjectActFileUpload().getCurrentFiles().stream()
                .map(i -> new LastObjectActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.setExPublicShareExpl(view.getExploitationView().getExPublicShareExpl().getValue());
        managedProject.setExHasPublicShareExpl(view.getExploitationView().getExHasPublicShareExpl().getValue());
        managedProject.getExFinModelFVIds().clear();
        managedProject.getExFinModelFVIds().addAll(view.getExploitationView()
                .getExFinModelFVIds().getCurrentFiles().stream()
                .map(i -> new ExploitationFinModelFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));
        managedProject.getExCostRecoveryMethod().clear();
        managedProject.getExCostRecoveryMethod().addAll(view.getExploitationView().getCostRecoveryMethod().getSelectedItems().stream().map(i -> {
            CostRecoveryMethodEntity costRecoveryMethod = new CostRecoveryMethodEntity();
            costRecoveryMethod.setId(Long.parseLong(i.getId()));
            return costRecoveryMethod;
        }).collect(Collectors.toList()));
        managedProject.setExCostRecoveryMechanism(view.getExploitationView().getExCostRecoveryMechanism().getValue());
        managedProject.getExSupportDocFVIds().clear();
        managedProject.getExSupportDocFVIds().addAll(view.getExploitationView()
                .getExSupportDocFVIds().getCurrentFiles().stream()
                .map(i -> new ExploitationSupportDoclFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getExSupportCompensDocFVIds().clear();
        managedProject.getExSupportCompensDocFVIds().addAll(view.getExploitationView()
                .getExSupportCompensDocFVIds().getCurrentFiles().stream()
                .map(i -> new ExploitationSupportCompensDoclFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.setExOwnPrivatePlanDate(getDate(view.getExploitationView().getExOwnPrivatePlanDate().getValue()));
        managedProject.setExOwnPrivateFactDate(getDate(view.getExploitationView().getExOwnPrivateFactDate().getValue()));
        managedProject.setExOwnPublicPlanDate(getDate(view.getExploitationView().getExOwnPublicPlanDate().getValue()));
        managedProject.setExOwnPublicFactDate(getDate(view.getExploitationView().getExOwnPublicFactDate().getValue()));
        managedProject.getExAgreementFVIds().clear();
        managedProject.getExAgreementFVIds().addAll(view.getExploitationView()
                .getExAgreementFVIds().getCurrentFiles().stream()
                .map(i -> new ExploitationAgreementFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));
        managedProject.getExAcceptActFVIds().clear();
        managedProject.getExAcceptActFVIds().addAll(view.getExploitationView()
                .getExAcceptActFVIds().getCurrentFiles().stream()
                .map(i -> new ExploitationAcceptActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));
        managedProject.setExStartAchEconPlanDate(getDate(view.getExploitationView().getExStartAchEconPlanDate().getValue()));
        managedProject.setExEndAchEconPlanDate(getDate(view.getExploitationView().getExEndAchEconPlanDate().getValue()));
        managedProject.setExStartAchEconFactDate(getDate(view.getExploitationView().getExStartAchEconFactDate().getValue()));
        managedProject.setExEndAchEconFactDate(getDate(view.getExploitationView().getExEndAchEconFactDate().getValue()));
        managedProject.getExAcceptActAAMFVIds().clear();
        managedProject.getExAcceptActAAMFVIds().addAll(view.getExploitationView()
                .getExAcceptActAAMFVIds().getCurrentFiles().stream()
                .map(i -> new ExploitationAcceptActAAMFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));
        managedProject.getExCalculationPlannedAmountFVIds().clear();
        managedProject.getExCalculationPlannedAmountFVIds().addAll(view.getExploitationView()
                .getExCalculationPlannedAmountFVIds().getCurrentFiles().stream()
                .map(i -> new ExCalculationPlannedAmountFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));
        managedProject.setExInvestStagePlanDate(getDate(view.getExploitationView().getExInvestStagePlanDate().getValue()));
        managedProject.setExInvestStageFactDate(getDate(view.getExploitationView().getExInvestStageFactDate().getValue()));
        managedProject.setExGrantorExpenses(view.getExploitationView().getExGrantorExpenses().getCurrentValue());
        managedProject.setExFormulasOrIndexingOrderEstablished(view.getExploitationView().getExFormulasOrIndexingOrderEstablished().getValue());

        setExploitationPaymentsFromView();
        setExploitationCompensationsFromView();

        ///////////////////////////////////////////

        managedProject.setTmCause(getLongIdOrNull(view.getTerminationView().getCause().getCurrentValue()));
        managedProject.setTmActPlanDate(getDate(view.getTerminationView().getActPlanDate().getValue()));
        managedProject.setTmActFactDate(getDate(view.getTerminationView().getActFactDate().getValue()));
        managedProject.setTmActNumber(view.getTerminationView().getActNumber().getValue());
        managedProject.setTmActDate(getDate(view.getTerminationView().getActDate().getValue()));
        managedProject.setTmCauseDescription(view.getTerminationView().getCauseDescription().getValue());
        managedProject.setTmFactDate(getDate(view.getTerminationView().getFactDate().getValue()));
        managedProject.setTmPlanDate(getDate(view.getTerminationView().getPlanDate().getValue()));
        // files

        managedProject.setTmPropertyJointProvided(view.getTerminationView().getPropertyJointProvided().getValue());
        managedProject.setTmPropertyJointPrivatePercent(view.getTerminationView().getTmPropertyJointPrivatePercent().getValue());
        managedProject.setTmPropertyJointPublicPercent(view.getTerminationView().getTmPropertyJointPublicPercent().getValue());

        managedProject.getTmCompositionOfCompensationGrantorFault().clear();
        managedProject.getTmCompositionOfCompensationGrantorFault().addAll(view.getTerminationView().getTmCompositionOfCompensationGrantorFault().getSelectedItems().stream().map(i -> {
            TmCompositionOfCompensationGrantorFaultEntity composition = new TmCompositionOfCompensationGrantorFaultEntity();
            composition.setId(Long.parseLong(i.getId()));
            return composition;
        }).collect(Collectors.toList()));

        managedProject.setTmIsCompensationPayed(view.getTerminationView().getCompensationPayed().getValue());
        managedProject.setTmCompensationValue(view.getTerminationView().getCompensationValue().getValue());
        managedProject.getTmCompensationFVIds().clear();
        managedProject.getTmCompensationFVIds().addAll(view.getTerminationView()
                .getCompensationTextFileUpload().getCurrentFiles().stream()
                .map(i -> new TerminationCompensationFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.setTmAftermath(getLongIdOrNull(view.getTerminationView().getAftermath().getCurrentValue()));

        managedProject.setTmContractDate(getDate(view.getTerminationView().getTerminationActDate().getValue()));
        managedProject.setTmContractNumber(view.getTerminationView().getTerminationActNumber().getValue());
        managedProject.setTmPublicShare(view.getTerminationView().getPublicSideShare().getValue());
        managedProject.setTmIsRfHasShare(view.getTerminationView().getRfShare().getValue());
        managedProject.setTmAnotherDescription(view.getTerminationView().getTmAnotherDescription().getCurrentValue());
        managedProject.setTmClausesOfAgreement(view.getTerminationView().getTmClausesOfAgreement().getCurrentValue());
        managedProject.setTmCompensationLimit(view.getTerminationView().getTmCompensationLimit().getValue());
        managedProject.setTmAgreementTerminated(view.getTerminationView().getTmAgreementTerminated().getValue());
        managedProject.setTmCompensationSum(view.getTerminationView().getTmCompensationSum().getValue());

        managedProject.setTmNdsCheck(view.getTerminationView().getTmNdsCheck().getValue());
        managedProject.setTmDateField(getDate(view.getTerminationView().getTmDateField().getValue()));
        managedProject.setTmMeasureType(getLongIdOrNull(view.getTerminationView().getTmMeasureType().getValue()));

        managedProject.getTmTextFileVersionId().clear();
        managedProject.getTmTextFileVersionId().addAll(view.getTerminationView()
                .getActTextFileUpload().getCurrentFiles().stream()
                .map(i -> new TerminationActFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getTmTaActTextFileVersionId().clear();
        managedProject.getTmTaActTextFileVersionId().addAll(view.getTerminationView()
                .getTaActFileUpload().getCurrentFiles().stream()
                .map(i -> new TerminationActTextFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getTmSupportingDocuments().clear();
        managedProject.getTmSupportingDocuments().addAll(view.getTerminationView()
                .getTmSupportingDocuments().getCurrentFiles().stream()
                .map(i -> new TerminationSupportingDocumentsFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.setCcIsChangesMade(view.getConditionChangeView().getIsChanged().getValue());
        managedProject.setCcReason(getLongIdOrNull(view.getConditionChangeView().getCause().getCurrentValue()));

        managedProject.setCcActNumber(view.getConditionChangeView().getActNumber().getCurrentValue());
        managedProject.setCcActDate(getDate(view.getConditionChangeView().getActDate().getValue()));

        managedProject.getCcTextFileVersionId().clear();
        managedProject.getCcTextFileVersionId().addAll(view.getConditionChangeView()
                .getActFileUpload().getCurrentFiles().stream()
                .map(i -> new ChangeTextFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));
        //files
        setEventsFromView();
        managedProject.setAdsIsThirdPartyOrgsProvided(view.getExtraView().getIsThirdPartyOrgsProvided().getValue());
        setPrivatePartnerThirdPartyOrgFromView();
        setPublicPartnerThirdPartyOrgFromView();

        managedProject.setAdsNpv(view.getExtraView().getFinancialEconomicsIndView().getNPV());
        managedProject.setAdsIrr(view.getExtraView().getFinancialEconomicsIndView().getIRR());
        managedProject.setAdsPb(view.getExtraView().getFinancialEconomicsIndView().getPB());
        managedProject.setAdsPbDiscounted(view.getExtraView().getFinancialEconomicsIndView().getPBDiscounted());
        managedProject.setAdsEbidta(view.getExtraView().getFinancialEconomicsIndView().getEBIDTA());
        managedProject.setAdsIsRegInvestmentProject(view.getExtraView().getIsRegInvestmentProject().getValue());

        managedProject.setAdsHasIncomeTax(view.getExtraView().getHasIncomeTax().getValue());
        managedProject.setAdsIncomeTaxRate(getLongIdOrNull(view.getExtraView().getIncomeTaxRate().getCurrentValue()));
        managedProject.setAdsHasLandTax(view.getExtraView().getHasLandTax().getValue());
        managedProject.setAdsLandTaxRate(getLongIdOrNull(view.getExtraView().getLandTaxRate().getCurrentValue()));
        managedProject.setAdsHasPropertyTax(view.getExtraView().getHasPropertyTax().getValue());
        managedProject.setAdsPropertyTaxRate(getLongIdOrNull(view.getExtraView().getPropertyTaxRate().getValue()));
        managedProject.setAdsHasBenefitClarificationTax(view.getExtraView().getHasBenefitClarificationTax().getValue());
        managedProject.setAdsBenefitClarificationRate(getLongIdOrNull(view.getExtraView().getBenefitClarificationRate().getValue()));
        managedProject.setAdsBenefitDescription(view.getExtraView().getBenefitDescription().getCurrentValue());

        managedProject.setAdsIsTreasurySupport(view.getExtraView().getIsTreasurySupport().getValue());

        managedProject.getAdsDecisionTextFileId().clear();
        managedProject.getAdsDecisionTextFileId().addAll(view.getExtraView()
                .getDecisionTextFileUpload().getCurrentFiles().stream()
                .map(i -> new AdsDecisionTextFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.setAdsWacc(view.getExtraView().getFinancialEconomicsIndView().getWACC());

        managedProject.setAdsConcessionaireOpf(getLongIdOrNull(view.getExtraView().getOpf().getValue()));
        managedProject.setAdsConcessionaireName(view.getExtraView().getConcessionaireName().getValue());
        managedProject.setAdsConcessionaireInn(view.getExtraView().getConcessionaireInn().getValue());
        managedProject.setAdsConcessionaireCreditRating(view.getExtraView().getCreditRating().getCurrentValue());
        managedProject.setAdsConcessionaireCreditRatingStartDate(getDate(view.getExtraView().getCreditRatingSetDate().getValue()));
        managedProject.setAdsConcessionaireCreditRatingEndDate(getDate(view.getExtraView().getCreditRatingReviewDate().getValue()));
        managedProject.setAdsConcessionaireCreditRatingAgency(view.getExtraView().getCreditRatingAgency().getCurrentValue());
        managedProject.setAdsUnforeseenExpencesShareComment(view.getExtraView().getUnforeseenExpencesComment().getCurrentValue());
        managedProject.setAdsWorkPlacesCount(view.getExtraView().getWorkPlacesCount().getValue());

        setValueToInvestmentsInObjectFromView();
        setValueToOperationalCostsFromView();
        setValueToTaxConditionFromView();
        setValueToRevenueIndicatorsFromView();
        managedProject.setFeiTaxIncentivesExist(view.getFinancialAndEconomicIndicatorsView().getFeiTaxIncentivesExist().getValue());
        managedProject.setFeiResidualValue(replace(view.getFinancialAndEconomicIndicatorsView().getFeiResidualValue().getValue()));
        managedProject.setFeiAverageServiceLife(replace(view.getFinancialAndEconomicIndicatorsView().getFeiAverageServiceLife().getValue()));
        managedProject.setFeiForecastValuesDate(getDate(view.getFinancialAndEconomicIndicatorsView().getFeiForecastValuesDate().getValue()));

        managedProject.getAdsCompetitionCriteria().clear();
        managedProject.getAdsCompetitionCriteria().addAll(view.getExtraView().getCompetitionCriteria().getSelectedItems().stream().map(i -> {
            CompetitionCriterion c = new CompetitionCriterion();
            c.setId(Long.parseLong(i.getId()));
            return c;
        }).collect(Collectors.toList()));

        managedProject.getAdsFinancialRequirement().clear();
        managedProject.getAdsFinancialRequirement().addAll(view.getExtraView().getFinancialRequirement().getSelectedItems().stream().map(i -> {
            FinRequirement f = new FinRequirement();
            f.setId(Long.parseLong(i.getId()));
            return f;
        }).collect(Collectors.toList()));

        managedProject.getAdsNonFinancialRequirements().clear();
        managedProject.getAdsNonFinancialRequirements().addAll(view.getExtraView().getNonFinancialRequirement().getSelectedItems().stream().map(i -> {
            NoFinRequirement f = new NoFinRequirement();
            f.setId(Long.parseLong(i.getId()));
            return f;
        }).collect(Collectors.toList()));

        managedProject.setAdsConcessionaireRegime(getStringIdOrNull(view.getExtraView().getRegimeType().getValue()));

        setSanctionsFromView();
        setJudicialActivityFromView();
        setPrivatePartnersOwningPartsFromView();
        setFinancialStructureFromView();
        setInvestmentsCriteriaBooleanFromView();
        setCompositionOfCompensationFromView();

        managedProject.setCmComment(view.getCommentsView().getComment().getCurrentValue());
        managedProject.setCmContacts(view.getCommentsView().getContacts().getCurrentValue());
        managedProject.setGiAlwaysDraftStatus(view.getCommentsView().getIsAlwaysDraftState().getValue());
        Long projectStatusId = getLongIdOrNull(view.getCommentsView().getProjectStatus().getCurrentValue());
        if (projectStatusId != null) {
            managedProject.setGiProjectStatus(new ProjectStatus() {{
                setId(projectStatusId);
            }});
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setGiProjectStatus(null);
//        }

        managedProject.getGiCompletedTemplateTextFVId().clear();
        managedProject.getGiCompletedTemplateTextFVId().addAll(view.getGeneralInformationView()
                .getCompletedTemplateFileUpload().getCurrentFiles().stream()
                .map(i -> new CompletedTemplateFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.setCbcMinimumGuaranteedExist(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedExist().getValue());
        managedProject.setCbcProjectBudgetObligationMissing(view.getContingentBudgetaryCommitmentsView().getProjectBudgetObligationMissing().getValue());
        managedProject.setCbcMinimumGuaranteedClauseAgreement(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedClauseAgreement().getCurrentValue());
        if (view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedIncomeForm().getCurrentValue() != null) {
            managedProject.setCbcMinimumGuaranteedIncomeForm(Long.parseLong(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedIncomeForm().getCurrentValue().getId()));
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
//        else {
//            managedProject.setCbcMinimumGuaranteedIncomeForm(null);
//        }
        managedProject.setCbcCompensationMinimumGuaranteedExist(view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedExist().getValue());

        if (view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedForm().getCurrentValue() != null) {
            managedProject.setCbcNonPaymentConsumersGoodsProvidedForm(Long.parseLong(view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedForm().getCurrentValue().getId()));
        }
        //TODO: GASU21-665 доделать для всех полей логику "если поле пустое, то и сохраняем пустое"
        else {
            managedProject.setCbcNonPaymentConsumersGoodsProvidedForm(null);
        }
        managedProject.setCbcLimitNonPaymentConsumersGoodsProvidedExist(view.getContingentBudgetaryCommitmentsView().getLimitNonPaymentConsumersGoodsProvidedExist().getValue());
        managedProject.setCbcNonPaymentConsumersGoodsProvidedClauseAgreement(view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedClauseAgreement().getCurrentValue());
        managedProject.setCbcLimitNonPaymentConsumersGoodsProvidedClauseAgreement(view.getContingentBudgetaryCommitmentsView().getLimitNonPaymentConsumersGoodsProvidedClauseAgreement().getCurrentValue());
        managedProject.setCbcCompensationLimitNonPaymentConsumersGoodsProvidedExist(view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentConsumersGoodsProvidedExist().getValue());
        managedProject.setCbcNonPaymentConsumersGoodsProvidedExist(view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedExist().getValue());
        managedProject.setCbcArisingProvisionOfBenefitsExist(view.getContingentBudgetaryCommitmentsView().getArisingProvisionOfBenefitsExist().getValue());
        managedProject.setCbcArisingProvisionOfBenefitsClauseAgreement(view.getContingentBudgetaryCommitmentsView().getCbcArisingProvisionOfBenefitsClauseAgreement().getCurrentValue());
        managedProject.setCbcCompensationArisingProvisionOfBenefitsExist(view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsExist().getValue());
        managedProject.setCbcCompensationArisingProvisionOfBenefitsClauseAgreement(view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsClauseAgreement().getCurrentValue());
        managedProject.setCbcDueToOnsetOfCertainCircumstancesExist(view.getContingentBudgetaryCommitmentsView().getDueToOnsetOfCertainCircumstancesExist().getValue());
        managedProject.setCbcDueToOnsetOfCertainCircumstancesClauseAgreement(view.getContingentBudgetaryCommitmentsView().getCbcDueToOnsetOfCertainCircumstancesClauseAgreement().getCurrentValue());
        managedProject.setCbcLimitCompensationAdditionalCostsAgreementExist(view.getContingentBudgetaryCommitmentsView().getLimitCompensationAdditionalCostsAgreementExist().getValue());
        managedProject.setCbcLimitCompensationAdditionalClauseAgreement(view.getContingentBudgetaryCommitmentsView().getLimitCompensationAdditionalClauseAgreement().getCurrentValue());
        managedProject.setCbcSpecifyOtherCircumstancesPrepare(view.getContingentBudgetaryCommitmentsView().getSpecifyOtherCircumstancesPrepare().getCurrentValue());
        managedProject.setCbcSpecifyOtherCircumstancesBuild(view.getContingentBudgetaryCommitmentsView().getSpecifyOtherCircumstancesBuild().getCurrentValue());
        managedProject.setCbcSpecifyOtherCircumstancesExploitation(view.getContingentBudgetaryCommitmentsView().getSpecifyOtherCircumstancesExploitation().getCurrentValue());
        managedProject.setCbcCompensationAdditionalCostsAgreementExist(view.getContingentBudgetaryCommitmentsView().getCompensationAdditionalCostsAgreementExist().getValue());

        managedProject.getCbcArisingProvisionOfBenefitFVId().clear();
        managedProject.getCbcArisingProvisionOfBenefitFVId().addAll(view.getContingentBudgetaryCommitmentsView()
                .getArisingProvisionOfBenefitFileUpload().getCurrentFiles().stream()
                .map(i -> new ArisingProvisionOfBenefitFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));
        managedProject.getCbcCompensationAdditionalCostsAgreementFVId().clear();
        managedProject.getCbcCompensationAdditionalCostsAgreementFVId().addAll(view.getContingentBudgetaryCommitmentsView()
                .getCompensationAdditionalCostsAgreementFileUpload().getCurrentFiles().stream()
                .map(i -> new CompensationAdditionalCostsAgreementFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));
        managedProject.getCbcCompensationArisingProvisionOfBenefitsFVId().clear();
        managedProject.getCbcCompensationArisingProvisionOfBenefitsFVId().addAll(view.getContingentBudgetaryCommitmentsView()
                .getCompensationArisingProvisionOfBenefitsFileUpload().getCurrentFiles().stream()
                .map(i -> new CompensationArisingProvisionOfBenefitsFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));
        managedProject.getCbcMinimumGuaranteedFVId().clear();
        managedProject.getCbcMinimumGuaranteedFVId().addAll(view.getContingentBudgetaryCommitmentsView()
                .getMinimumGuaranteedFileUpload().getCurrentFiles().stream()
                .map(i -> new MinimumGuaranteedFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));
        managedProject.getCbcNonPaymentConsumersFVId().clear();
        managedProject.getCbcNonPaymentConsumersFVId().addAll(view.getContingentBudgetaryCommitmentsView()
                .getNonPaymentConsumersFileUpload().getCurrentFiles().stream()
                .map(i -> new NonPaymentConsumersFile() {{
                    setId(i.getFileVersionId());
                    setFileName(i.getFileName());
                }})
                .collect(Collectors.toList()));

        managedProject.getCbcNameOfCircumstanceAdditionalCostPrepare().clear();
        managedProject.getCbcNameOfCircumstanceAdditionalCostPrepare().addAll(view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostPrepare().getSelectedItems().stream().map(i -> Long.parseLong(i.getId())).collect(Collectors.toList()));
        managedProject.getCbcNameOfCircumstanceAdditionalCostBuild().clear();
        managedProject.getCbcNameOfCircumstanceAdditionalCostBuild().addAll(view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostBuild().getSelectedItems().stream().map(i -> Long.parseLong(i.getId())).collect(Collectors.toList()));
        managedProject.getCbcNameOfCircumstanceAdditionalCostExploitation().clear();
        managedProject.getCbcNameOfCircumstanceAdditionalCostExploitation().addAll(view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostExploitation().getSelectedItems().stream().map(i -> Long.parseLong(i.getId())).collect(Collectors.toList()));
        setValueToMinimumGuaranteedAmountFromView();

        managedProject.setCbcMinimumGuaranteedAmountNdsCheck(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getNdsCheck().getValue());
        managedProject.setCbcMinimumGuaranteedAmountDateField(getDate(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getDateField().getValue()));
        managedProject.setCbcMinimumGuaranteedAmountDateField(getLongIdOrNull(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getMeasureType().getValue()));

        setValueToCompensationMinimumGuaranteedAmountFromView();
        setValueToCompensationLimitNonPaymentAmountFromView();
        setValueToCompensationArisingProvisionOfBenefitsAmountFromView();
        setValueToLimitCompensationAdditionalCostsAmountFromView();

        managedProject.setCbcNdsCheck(view.getContingentBudgetaryCommitmentsView().getCbcNdsCheck().getValue());
        managedProject.setIoNdsCheck(view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getNdsCheck().getValue());
        GWT.log("set IoNdsCheck in managedProject");
        if (view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getNdsCheck().getValue() != null) {
            GWT.log(view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getNdsCheck().getValue().toString());
        }
        managedProject.setCbcDateField(getDate(view.getContingentBudgetaryCommitmentsView().getCbcDateField().getValue()));
        managedProject.setIoDateField(getDate(view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getDateField().getValue()));
        GWT.log("set IoDateField in managedProject");
        if (view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getDateField().getValue() != null) {
            GWT.log(String.valueOf(view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getDateField().getValue()));
        }
        managedProject.setCbcMeasureType(getLongIdOrNull(view.getContingentBudgetaryCommitmentsView().getCbcMeasureType().getValue()));
        managedProject.setIoMeasureType(getLongIdOrNull(view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getMeasureType().getValue()));
        GWT.log("set IoMeasureType in managedProject");
        if (view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getMeasureType().getValue() != null) {
            GWT.log(String.valueOf(view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getMeasureType().getValue()));
        }

        setValueToCircumstancesAdditionalCostsAmountFromView();

    }

    private void setEventsFromView() {
        List<EventModel> viewEvents = view.getExtraView().getEventsView().getGrid().getStore().getAll();
        managedProject.getAdsEvents().clear();
        for (EventModel viewEvent : viewEvents) {
            Event e = new Event();
            e.setId(viewEvent.getId());
            e.setDate(viewEvent.getDate());
            e.setName(viewEvent.getName());
            e.setDescription(viewEvent.getDescription());
            managedProject.getAdsEvents().add(e);
        }
    }

    private void setEventsToView() {
        view.getExtraView().getEventsView().getGrid().getStore().clear();
        for (Event event : managedProject.getAdsEvents()) {
            EventModel model = new EventModel();
            model.setId(event.getId());
            model.setGid(event.getId());
            model.setName(event.getName());
            model.setDescription(event.getDescription());
            model.setDate(event.getDate());
            view.getExtraView().getEventsView().getGrid().getStore().add(model);
        }
    }

    private void setPrivatePartnerThirdPartyOrgFromView() {
        List<ThirdOrgModel> thirdOrgModels = view.getExtraView().getPrivateOrganizationsInvolvementView().getGrid().getStore().getAll();
        managedProject.getAdsPrivatePartnerThirdPartyOrgs().clear();
        for (ThirdOrgModel thirdOrgModel : thirdOrgModels) {
            PrivatePartnerThirdPartyOrg p = new PrivatePartnerThirdPartyOrg();
            p.setId(thirdOrgModel.getId());
            p.setInn(thirdOrgModel.getInn());
            p.setName(thirdOrgModel.getName());
            for (WorkTypeModel workTypeModel : thirdOrgModel.getWorkType()) {
                p.getWorkTypes().add(new WorkType() {{
                    setId(workTypeModel.getId());
                    setName(workTypeModel.getName());
                }});
            }
            managedProject.getAdsPrivatePartnerThirdPartyOrgs().add(p);
        }
    }

    private void setPrivatePartnerThirdPartyOrgToView() {
        view.getExtraView().getPrivateOrganizationsInvolvementView().getGrid().getStore().clear();
        for (PrivatePartnerThirdPartyOrg thirdPartyOrg : managedProject.getAdsPrivatePartnerThirdPartyOrgs()) {
            ThirdOrgModel model = new ThirdOrgModel();
            model.setGid(thirdPartyOrg.getId());
            model.setId(thirdPartyOrg.getId());
            model.setInn(thirdPartyOrg.getInn());
            model.setName(thirdPartyOrg.getName());
            for (WorkType workType : thirdPartyOrg.getWorkTypes()) {
                model.getWorkType().add(new WorkTypeModel() {{
                    setId(workType.getId());
                    setName(workType.getName());
                }});
            }
            view.getExtraView().getPrivateOrganizationsInvolvementView().getGrid().getStore().add(model);
        }
    }

    private void setPublicPartnerThirdPartyOrgFromView() {
        List<ThirdOrgModel> thirdOrgModels = view.getExtraView().getPublicOrganizationsInvolvementView().getGrid().getStore().getAll();
        managedProject.getAdsPublicPartnerThirdPartyOrgs().clear();
        for (ThirdOrgModel thirdOrgModel : thirdOrgModels) {
            PublicPartnerThirdPartyOrg p = new PublicPartnerThirdPartyOrg();
            p.setId(thirdOrgModel.getId());
            p.setInn(thirdOrgModel.getInn());
            p.setName(thirdOrgModel.getName());
            for (WorkTypeModel workTypeModel : thirdOrgModel.getWorkType()) {
                p.getWorkTypes().add(new WorkType() {{
                    setId(workTypeModel.getId());
                    setName(workTypeModel.getName());
                }});
            }
            managedProject.getAdsPublicPartnerThirdPartyOrgs().add(p);
        }
    }

    private void setPublicPartnerThirdPartyOrgToView() {
        view.getExtraView().getPublicOrganizationsInvolvementView().getGrid().getStore().clear();
        for (PublicPartnerThirdPartyOrg thirdPartyOrg : managedProject.getAdsPublicPartnerThirdPartyOrgs()) {
            ThirdOrgModel model = new ThirdOrgModel();
            model.setGid(thirdPartyOrg.getId());
            model.setId(thirdPartyOrg.getId());
            model.setInn(thirdPartyOrg.getInn());
            model.setName(thirdPartyOrg.getName());
            for (WorkType workType : thirdPartyOrg.getWorkTypes()) {
                model.getWorkType().add(new WorkTypeModel() {{
                    setId(workType.getId());
                    setName(workType.getName());
                }});
            }
            view.getExtraView().getPublicOrganizationsInvolvementView().getGrid().getStore().add(model);
        }
    }

    private void setSanctionsFromView() {
        List<SanctionModel> sanctionModels = view.getExtraView().getSanctionsView().getGrid().getStore().getAll();

        managedProject.getAdsSanctions().clear();
        for (SanctionModel sanctionModel : sanctionModels) {
            Sanction s = new Sanction();
            s.setId(sanctionModel.getId());
            s.setCause(sanctionModel.getCause());
            s.setDate(sanctionModel.getDate());
            s.setSum(sanctionModel.getValue());
            for (SanctionTypeModel sanctionTypeModel : sanctionModel.getSanctionType()) {
                s.getType().add(new SanctionType() {{
                    setId(sanctionTypeModel.getId());
                    setName(sanctionTypeModel.getName());
                }});
            }
            managedProject.getAdsSanctions().add(s);
        }
    }

    private void setSanctionsToView() {
        view.getExtraView().getSanctionsView().getGrid().getStore().clear();
        for (Sanction sanction : managedProject.getAdsSanctions()) {
            SanctionModel model = new SanctionModel();
            model.setId(sanction.getId());
            model.setGid(sanction.getId());
            model.setValue(sanction.getSum());
            model.setDate(sanction.getDate());
            model.setCause(sanction.getCause());
            for (SanctionType sanctionType : sanction.getType()) {
                model.getSanctionType().add(new SanctionTypeModel() {{
                    setId(sanctionType.getId());
                    setName(sanctionType.getName());
                }});
            }
            view.getExtraView().getSanctionsView().getGrid().getStore().add(model);
        }
        if (managedProject.getAdsSanctions().size() > 0)
            view.getExtraView().getCp5().expand();
    }

    private void setJudicialActivityFromView() {
        List<CourtActivityModel> courtActivityModels = view.getExtraView().getCourtActivityView().getGrid().getStore().getAll();
        managedProject.getAdsJudicialActivities().clear();
        for (CourtActivityModel courtActivityModel : courtActivityModels) {
            JudicialActivity a = new JudicialActivity();
            a.setId(courtActivityModel.getId());
            a.setComment(courtActivityModel.getComment());
            a.setDate(courtActivityModel.getDate());
            a.setDisputeSubject(courtActivityModel.getDisputeSubject());
            a.setJudicialDecision(courtActivityModel.getJudgment());
            a.setKadArbitrRuUrl(courtActivityModel.getKadArbitrRuUrl());
            managedProject.getAdsJudicialActivities().add(a);
        }
    }

    private void setJudicialActivityToView() {
        view.getExtraView().getCourtActivityView().getGrid().getStore().clear();

        for (JudicialActivity judicialActivity : managedProject.getAdsJudicialActivities()) {
            CourtActivityModel c = new CourtActivityModel();
            c.setId(judicialActivity.getId());
            c.setGid(judicialActivity.getId());
            c.setComment(judicialActivity.getComment());
            c.setKadArbitrRuUrl(judicialActivity.getKadArbitrRuUrl());
            c.setJudgment(judicialActivity.getJudicialDecision());
            c.setDisputeSubject(judicialActivity.getDisputeSubject());
            c.setDate(judicialActivity.getDate());
            view.getExtraView().getCourtActivityView().getGrid().getStore().add(c);
        }
        if (managedProject.getAdsJudicialActivities().size() > 0)
            view.getExtraView().getCp6().expand();
    }

    private void setPrivatePartnersOwningPartsFromView() {
        managedProject.getAdsOwnershipStructures().clear();
        List<PrivatePartnersOwningPartModel> owningPartModels = view.getExtraView().getPrivatePartnersOwningPartsView().getGrid().getStore().getAll();
        for (PrivatePartnersOwningPartModel owningPartModel : owningPartModels) {
            OwnershipStructure o = new OwnershipStructure();
            o.setId(owningPartModel.getId());
            o.setName(owningPartModel.getOrgName());
            o.setCapitalValue(owningPartModel.getOrgCapitalValue());
            o.setPercent(owningPartModel.getPercent());
            managedProject.getAdsOwnershipStructures().add(o);
        }
    }

    private void setPrivatePartnersOwningPartsToView() {
        view.getExtraView().getPrivatePartnersOwningPartsView().getGrid().getStore().clear();
        for (OwnershipStructure ownershipStructure : managedProject.getAdsOwnershipStructures()) {
            PrivatePartnersOwningPartModel model = new PrivatePartnersOwningPartModel();
            model.setId(ownershipStructure.getId());
            model.setGid(ownershipStructure.getId());
            model.setOrgName(ownershipStructure.getName());
            model.setPercent(ownershipStructure.getPercent());
            model.setOrgCapitalValue(ownershipStructure.getCapitalValue());
            view.getExtraView().getPrivatePartnersOwningPartsView().getGrid().getStore().add(model);
        }
    }

    private void setEnsureMethodsToView() {
        view.getCreationView().getLandMethodOfExecuteObligation().getGrid().getStore().clear();
        for (CreationEnsureMethod ensureMethod : managedProject.getCrEnsureMethods()) {
            EnsureMethodModel model = new EnsureMethodModel();
            model.setId(ensureMethod.getId());
            model.setGid(ensureMethod.getId());
            model.setEnsureMethodId(ensureMethod.getEnsureMethodId());
            model.setEnsureMethodName(ensureMethod.getEnsureMethodName());
            model.setTerm(ensureMethod.getTerm());
            model.setValue(ensureMethod.getValue());
            model.setSubmissionDate(ensureMethod.getSubmissionDate());
            model.setRiskType(ensureMethod.getRiskType());
            view.getCreationView().getLandMethodOfExecuteObligation().getGrid().getStore().add(model);
        }
    }

    private void setEnsureMethodsFromView() {
        managedProject.getCrEnsureMethods().clear();
        for (EnsureMethodModel model : view.getCreationView().getLandMethodOfExecuteObligation().getGrid().getStore().getAll()) {
            CreationEnsureMethod e = new CreationEnsureMethod();
            e.setId(model.getId());
            e.setEnsureMethodId(model.getEnsureMethodId());
            e.setEnsureMethodName(model.getEnsureMethodName());
            e.setTerm(model.getTerm());
            e.setValue(model.getValue());
            e.setRiskType(model.getRiskType());
            e.setSubmissionDate(model.getSubmissionDate());
            managedProject.getCrEnsureMethods().add(e);
        }
    }

    private void setFinancialStructureFromView() {
        managedProject.getAdsFinancialStructure().clear();
        for (FinancialIndicatorModel financialIndicator : view.getExtraView().getFinancialStructureView().getGrid().getStore().getAll()) {
            FinancialStructure fs = new FinancialStructure();
            fs.setId(financialIndicator.getId());
            fs.setFinanceIndicator(new FinanceIndicator() {{
                setId(financialIndicator.getGid());
            }});
            fs.setValue(financialIndicator.getValue());
            managedProject.getAdsFinancialStructure().add(fs);
        }
    }

    private void setFinancialStructureToView() {
        List<FinancialIndicatorModel> models = view.getExtraView().getFinancialStructureView().getGrid().getStore().getAll();
        for (FinancialIndicatorModel model : models) {
            for (FinancialStructure financialStructure : managedProject.getAdsFinancialStructure()) {
                if (model.getGid().equals(financialStructure.getFinanceIndicator().getId())) {
                    model.setId(financialStructure.getId());
                    model.setValue(financialStructure.getValue());
                }
            }
        }
        view.getExtraView().getFinancialStructureView().getGrid().getView().refresh(false);
    }

    private void setCompositionOfCompensationFromView() {
        managedProject.getTmCompositionOfCompensationView().clear();
        for (FinancialIndicatorModel financialIndicator : view.getTerminationView().getCompositionOfCompensationView().getGrid().getStore().getAll()) {
            CompositionOfCompensation compositionOfCompensation = new CompositionOfCompensation();
            compositionOfCompensation.setId(financialIndicator.getId());
            compositionOfCompensation.setValue(financialIndicator.getValue());
            compositionOfCompensation.setCompositionIndicatorTypeId(financialIndicator.getGid());
            compositionOfCompensation.setName(financialIndicator.getName());
            managedProject.getTmCompositionOfCompensationView().add(compositionOfCompensation);
        }
    }

    private void setCompositionOfCompensationToView() {
        List<FinancialIndicatorModel> models = view.getTerminationView().getCompositionOfCompensationView().getGrid().getStore().getAll();
        for (FinancialIndicatorModel model : models) {
            for (CompositionOfCompensation compositionOfCompensation : managedProject.getTmCompositionOfCompensationView()) {
                if (model.getGid().equals(compositionOfCompensation.getCompositionIndicatorTypeId())) {
                    model.setId(compositionOfCompensation.getId());
                    model.setValue(compositionOfCompensation.getValue());
                }
            }
        }
        view.getTerminationView().getCompositionOfCompensationView().getGrid().getView().refresh(false);
    }

    private void setInvestmentsCriteriaBooleanFromView() {
        managedProject.getAdsInvestmentBoolCriterias().clear();
        for (InvestmentsCriteriaModel model : view.getExtraView().getInvestmentsCriteriaBooleanWidget().getStore().getAll()) {
            InvestmentsCriteriaBoolean i = new InvestmentsCriteriaBoolean();
            i.setId(model.getId());
            i.setFinanceIndicator(new InvestmentsCriteriaIndBoolean() {{
                setId(model.getGid());
            }});
            i.setComment(model.getComment());
            i.setValue(model.getValue());
            managedProject.getAdsInvestmentBoolCriterias().add(i);
        }
    }

    private void setInvestmentsCriteriaBooleanToView() {
        List<InvestmentsCriteriaModel> models = view.getExtraView().getInvestmentsCriteriaBooleanWidget().getStore().getAll();
        for (InvestmentsCriteriaModel model : models) {
            for (InvestmentsCriteriaBoolean investmentBoolCriteria : managedProject.getAdsInvestmentBoolCriterias()) {
                if (model.getGid().equals(investmentBoolCriteria.getFinanceIndicator().getId())) {
                    model.setId(investmentBoolCriteria.getId());
                    model.setValue(investmentBoolCriteria.isValue());
                    model.setComment(investmentBoolCriteria.getComment());
                }
            }
        }
        view.getExtraView().getInvestmentsCriteriaBooleanWidget().getGrid().getView().refresh(false);
    }

    private void updateViewFromProject() {
        setValuesToPassport();

        view.getGeneralInformationView().getProjectNameField().setValue(managedProject.getGiName());
        selectComboById(view.getGeneralInformationView().getRealizationForm(), getStringValOrEmpty(() -> managedProject.getGiRealizationForm().getId()));

        fetchThenSelectFilteredInitiationMethodDictionary(getStringValOrEmpty(() -> managedProject.getGiRealizationForm().getId()), getStringValOrEmpty(() -> managedProject.getGiInitiationMethod().getId()));
        fetchThenSelectFilteredRealizationLevelDictionary(getStringValOrEmpty(() -> managedProject.getGiRealizationForm().getId()), getStringValOrEmpty(() -> managedProject.getGiRealizationLevel().getId()));
        view.getGeneralInformationView().getIsRFPartOfAgreement().setValue(managedProject.getGiIsRFPartOfAgreement());
        view.getGeneralInformationView().getIsRegionPartOfAgreement().setValue(managedProject.getGiIsRegionPartOfAgreement());
        view.getGeneralInformationView().getIsMunicipalityPartOfAgreement().setValue(managedProject.getGiIsMunicipalityPartOfAgreement());
        selectComboById(view.getGeneralInformationView().getRegion(), getStringValOrEmpty(() -> managedProject.getGiRegion().getId()));
        fetchThenSelectedFilteredMunicipalityDictionary(getStringValOrEmpty(() -> managedProject.getGiRegion().getId()),
                getStringValOrEmpty(() -> managedProject.getGiMunicipality().getId()),
                getStringValOrEmpty(() -> managedProject.getGiPublicPartner().getId()));
        view.getGeneralInformationView().getInn().setValue(managedProject.getGiInn());
        view.getGeneralInformationView().getBalanceHolder().setValue(managedProject.getGiBalanceHolder());
        view.getGeneralInformationView().getImplementer().setValue(managedProject.getGiImplementer());
        view.getGeneralInformationView().getImplementerInn().setValue(managedProject.getGiImplementerInn());
        selectComboById(view.getGeneralInformationView().getOpf(), getStringValOrEmpty(() -> managedProject.getGiOPF().getId()));
        view.getGeneralInformationView().getIsForeignInvestor().setValue(managedProject.getGiIsForeignInvestor());
        view.getGeneralInformationView().getIsMcpSubject().setValue(managedProject.getGiIsMcpSubject());
        view.getGeneralInformationView().getIsSpecialProjectCompany().setValue(managedProject.getGiIsSpecialProjectCompany());
        view.getGeneralInformationView().getHasInvestmentProperty().setValue(managedProject.getGiHasInvestmentProperty());
        view.getGeneralInformationView().getPublicSharePercentage().setValue(managedProject.getGiPublicSharePercentage());
        view.getGeneralInformationView().getIsRFHasShare().setValue(managedProject.getGiIsRFHasShare());
        fetchThenSelectFilteredRealizationSphereDictionary(getStringValOrEmpty(() -> managedProject.getGiRealizationForm().getId()),
                getStringValOrEmpty(() -> managedProject.getGiRealizationSphere().getId()),
                getStringValOrEmpty(() -> managedProject.getGiRealizationSector().getId()),
                managedProject.getGiObjectType().stream().map(ObjectKind::getId).collect(Collectors.toList())
        );
        view.getGeneralInformationView().getObjectLocation().setValue(managedProject.getGiObjectLocation());
        selectMulticomboById(view.getGeneralInformationView().getAgreementSubject(), managedProject.getGiAgreementSubject().stream().map(i -> i.getId()).collect(Collectors.toList()));
        selectComboById(view.getGeneralInformationView().getRealizationStatus(), getStringValOrEmpty(() -> managedProject.getGiRealizationStatus().getId()));

        view.getGeneralInformationView().getCompletedTemplateFileUpload().setFiles(managedProject.getGiCompletedTemplateTextFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        view.getObjectDescriptionView().getObjectNameField().setValue(managedProject.getOdObjectName());
        view.getObjectDescriptionView().getObjectCharacteristic().setValue(managedProject.getOdObjectDescription());
        view.getObjectDescriptionView().getIsPropertyStaysWithPrivateSide().setValue(managedProject.getOdIsPropertyStaysWithPrivateSide());
        view.getObjectDescriptionView().getIsNewPropertyBeGivenToPrivateSide().setValue(managedProject.getOdIsNewPropertyBeGivenToPrivateSide());
        view.getObjectDescriptionView().getIsObjectImprovementsGiveAway().setValue(managedProject.getOdIsObjectImprovementsGiveAway());
        selectComboById(view.getObjectDescriptionView().getRentObject(), getStringValOrEmpty(() -> String.valueOf(managedProject.getOdRentObject())));
        view.getObjectDescriptionView().getFileUploader().setFiles(managedProject.getOdRentPassportFileVersionId().stream().map(
                i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())
        ).collect(Collectors.toList()));

        setValueFromTechEconomicsToView();
        setEnergyEfficiencyPlanToView();

        view.getProjectPreparationView().getIsObjInListWithConcessionalAgreements().setValue(managedProject.getPpIsObjInListWithConcessionalAgreements());
        ValueChangeEvent.fire(view.getProjectPreparationView().getJointCompetition(), managedProject.getPpConcludeAgreementIsJoint());
        ValueChangeEvent.fire(view.getProjectPreparationView().getIsObjInListWithConcessionalAgreements(), managedProject.getPpIsObjInListWithConcessionalAgreements());
        ValueChangeEvent.fire(view.getProjectPreparationView().getCompetitionHasResults(), managedProject.getPpCompetitionHasResults());
        view.getProjectPreparationView().getObjectsListUrl().setValue(managedProject.getPpObjectsListUrl());
        view.getProjectPreparationView().getSupposedPrivatePartnerName().setValue(managedProject.getPpSupposedPrivatePartnerName());
        view.getProjectPreparationView().getSupposedPrivatePartnerInn().setValue(managedProject.getPpSupposedPrivatePartnerInn());
        view.getProjectPreparationView().getIsForeignInvestor().setValue(managedProject.getPpIsForeignInvestor());
        view.getProjectPreparationView().getIsMcpSubject().setValue(managedProject.getPpIsMspSubject());
        view.getProjectPreparationView().getSupposedAgreementStartDate().setValue(getDate(managedProject.getPpSupposedAgreementStartDate()));
        view.getProjectPreparationView().getSupposedAgreementEndDate().setValue(getDate(managedProject.getPpSupposedAgreementEndDate()));
        view.getProjectPreparationView().getSupposedValidityYears().setValue(managedProject.getPpSupposedValidityYears());
        view.getProjectPreparationView().getDeliveryTimeOfGoodsWorkDate().setValue(getDate(managedProject.getPpDeliveryTimeOfGoodsWorkDate()));
        fetchThenSelectFilteredAgreementGroundDictionary(
                getStringValOrEmpty(() -> managedProject.getGiRealizationForm().getId()),
                getStringValOrEmpty(() -> managedProject.getGiInitiationMethod().getId()),
                getStringValOrEmpty(() -> String.valueOf(managedProject.getPpGroundsOfAgreementConclusion())));

        view.getProjectPreparationView().getActNumber().setValue(managedProject.getPpActNumber());
        view.getProjectPreparationView().getActDate().setValue(getDate(managedProject.getPpActDate()));
        view.getProjectPreparationView().getProposalPublishDate().setValue(getDate(managedProject.getPpProposalPublishDate()));
        view.getProjectPreparationView().getTorgiGovRuUrl().setValue(managedProject.getPpTorgiGovRuUrl());
        view.getProjectPreparationView().getIsReadinessRequestReceived().setValue(managedProject.getPpIsReadinessRequestReceived());
        view.getProjectPreparationView().getIsDecisionMadeToConcludeAnAgreement().setValue(managedProject.getPpIsDecisionMadeToConcludeAnAgreement());
        ValueChangeEvent.fire(view.getProjectPreparationView().getIsDecisionMadeToConcludeAnAgreement(), managedProject.getPpIsDecisionMadeToConcludeAnAgreement());
        view.getProjectPreparationView().getConclustionActNumber().setValue(managedProject.getPpConcludeAgreementActNum());
        view.getProjectPreparationView().getConclusionActDate().setValue(getDate(managedProject.getPpConcludeAgreementActDate()));
        view.getProjectPreparationView().getInvestmentStageDurationDate().setValue(getDate(managedProject.getPpInvestmentStageDurationDate()));
        view.getProjectPreparationView().getIsAgreementSigned().setValue(managedProject.getPpConcludeAgreementIsSigned());
        view.getProjectPreparationView().getJointCompetition().setValue(managedProject.getPpConcludeAgreementIsJoint());

    if (managedProject.getPpConcludeAgreementIsJoint() != null && managedProject.getPpConcludeAgreementIsJoint())
            view.getProjectPreparationView().getCp2().expand();

        view.getProjectPreparationView().getAnotherProjectsInfo().setValue(managedProject.getPpConcludeAgreementOtherPjInfo());
        view.getProjectPreparationView().getAnotherProjectsIdentifier().setValue(managedProject.getPpConcludeAgreementOtherPjIdent());
        view.getProjectPreparationView().getCompetitionBidCollEndPlanDate().setValue(getDate(managedProject.getPpCompetitionBidCollEndPlanDate()));
        view.getProjectPreparationView().getCompetitionBidCollEndFactDate().setValue(getDate(managedProject.getPpCompetitionBidCollEndFactDate()));
        view.getProjectPreparationView().getCompetitionTenderOfferEndPlanDate().setValue(getDate(managedProject.getPpCompetitionTenderOfferEndPlanDate()));
        view.getProjectPreparationView().getCompetitionTenderOfferEndFactDate().setValue(getDate(managedProject.getPpCompetitionTenderOfferEndFactDate()));
        view.getProjectPreparationView().getCompetitionResultsPlanDate().setValue(getDate(managedProject.getPpCompetitionResultsPlanDate()));
        view.getProjectPreparationView().getCompetitionResultsFactDate().setValue(getDate(managedProject.getPpCompetitionResultsFactDate()));

        if (managedProject.getPpCompetitionBidCollEndFactDate() != null
                || managedProject.getPpCompetitionTenderOfferEndFactDate() != null
                || managedProject.getPpCompetitionResultsFactDate() != null)
            view.getProjectPreparationView().getCp3().expand();

        view.getProjectPreparationView().getCompetitionIsElAuction().setValue(managedProject.getPpCompetitionIsElAuction());
        view.getProjectPreparationView().getCompetitionHasResults().setValue(managedProject.getPpCompetitionHasResults());
        selectComboById(view.getProjectPreparationView().getCompetitionResults(), managedProject.getPpCompetitionResults());
        view.getProjectPreparationView().getCompetitionResultsProtocolNum().setValue(managedProject.getPpCompetitionResultsProtocolNum());
        view.getProjectPreparationView().getCompetitionResultsProtocolDate().setValue(getDate(managedProject.getPpCompetitionResultsProtocolDate()));

        if (managedProject.getPpCompetitionResults() != null
                || managedProject.getPpCompetitionResultsProtocolDate() != null)
            view.getProjectPreparationView().getCp4().expand();

        view.getProjectPreparationView().getCompetitionResultsParticipantsNum().setValue(managedProject.getPpCompetitionResultsParticipantsNum());
        selectComboById(view.getProjectPreparationView().getCompetitionResultsSignStatus(), managedProject.getPpCompetitionResultsSignStatus());
        selectComboById(view.getProjectPreparationView().getContractPriceOrder(), managedProject.getPpContractPriceOrder());
        view.getProjectPreparationView().getContractPriceFormula().setValue(managedProject.getPpContractPriceFormula());
        view.getProjectPreparationView().getContractPricePrice().setValue(managedProject.getPpContractPricePrice());
        view.getProjectPreparationView().getNdsCheck().setValue(managedProject.getPpNdsCheck());

        view.getProjectPreparationView().getPpConcludeAgreementLink().setValue(managedProject.getPpConcludeAgreementLink());
        view.getProjectPreparationView().getPpImplementProject().setValue(managedProject.getPpImplementProject());
        selectComboById(view.getProjectPreparationView().getPpResultsOfPlacing(), managedProject.getPpResultsOfPlacing());

        if (view.getProjectPreparationView().getMeasureType() != null) {
            selectComboById(view.getProjectPreparationView().getMeasureType(), managedProject.getPpMeasureType());
            if ("1".equals(String.valueOf(view.getProjectPreparationView().getMeasureType().getId())))
                view.getCreationView().getInvestments().getDateField().hide();
        }

        view.getProjectPreparationView().getDateField().setValue(getDate(managedProject.getPpDateField()));
        selectComboById(view.getProjectPreparationView().getContractPriceOffer(), managedProject.getPpContractPriceOffer());
        view.getProjectPreparationView().getContractPriceOfferValue().setValue(managedProject.getPpContractPriceOfferValue());
        view.getProjectPreparationView().getContractPriceSavingStartDate().setValue(getDate(managedProject.getPpContractPriceSavingStartDate()));
        view.getProjectPreparationView().getContractPriceSavingEndDate().setValue(getDate(managedProject.getPpContractPriceSavingEndDate()));
        selectComboById(view.getProjectPreparationView().getPublicPartnerCostRecoveryMethod(), managedProject.getPpPrivatePartnerCostRecoveryMethod());
        view.getProjectPreparationView().getAdvancePaymentAmount().setValue(managedProject.getPpAdvancePaymentAmount());
        view.getProjectPreparationView().getFirstObjectOperationDate().setValue(getDate(managedProject.getPpFirstObjectOperationDate()));
        view.getProjectPreparationView().getLastObjectCommissioningDate().setValue(getDate(managedProject.getPpLastObjectCommissioningDate()));
        view.getProjectPreparationView().getProjectAgreementFileUploader().setFiles(managedProject.getPpProjectAgreementFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getActFileUploader().setFiles(managedProject.getPpActTextFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getLeaseAgreementUploader().setFiles(managedProject.getPpLeaseAgreementTextFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getIsProjectAssignedStatus().setValue(managedProject.getPpProjectAssignedStatus());
        view.getProjectPreparationView().getDecisionNumber().setValue(managedProject.getPpDecisionNumber());
        view.getProjectPreparationView().getDecisionDate().setValue(getDate(managedProject.getPpDecisionDate()));
        view.getProjectPreparationView().getDecisionFileUploader().setFiles(managedProject.getPpDecisionTextFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getProposalTextFileUploader().setFiles(managedProject.getPpProposalTextFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getProtocolFileUploader().setFiles(managedProject.getPpProtocolFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getConclusionFileUploader().setFiles(managedProject.getPpConcludeAgreementFvId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getCompetitionTextFileUploader().setFiles(managedProject.getPpCompetitionTextFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getCompetitionResultsProtocolFileUploader().setFiles(managedProject.getPpCompetitionResultsProtocolTextFvId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getCompetitionResultsDocFileUploader().setFiles(managedProject.getPpCompetitionResultsDocFvId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getFinancialModelFileUpload().setFiles(managedProject.getPpFinancialModelFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getSupportingDocumentsFileUploader().setFiles(managedProject.getPpSupportingDocumentsFileVersionIds().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        view.getProjectPreparationView().getBudgetExpendituresAgreementOnGchpMchp().setValue(managedProject.getPpBudgetExpendituresAgreementOnGchpMchp());
        view.getProjectPreparationView().getBudgetExpendituresGovContract().setValue(managedProject.getPpBudgetExpendituresGovContract());
        view.getProjectPreparationView().getObligationsInCaseOfRisksAgreementOnGchpMchp().setValue(managedProject.getPpObligationsInCaseOfRisksAgreementOnGchpMchp());
        view.getProjectPreparationView().getObligationsInCaseOfRisksGovContract().setValue(managedProject.getPpObligationsInCaseOfRisksGovContract());

        selectComboById(view.getProjectPreparationView().getAgreementsSet(), getStringValOrEmpty(() -> String.valueOf(managedProject.getPpAgreementsSet().getId())));
        selectComboById(view.getProjectPreparationView().getMethodOfExecuteObligation(), getStringValOrEmpty(() -> String.valueOf(managedProject.getPpMethodOfExecuteObligation().getId())));
        selectComboById(view.getProjectPreparationView().getOtherGovSupports(), getStringValOrEmpty(() -> String.valueOf(managedProject.getPpStateSupportMeasuresSPIC().getId())));
        selectComboById(view.getProjectPreparationView().getWinnerContractPriceOffer(), getStringValOrEmpty(() -> String.valueOf(managedProject.getPpWinnerContractPriceOffer().getId())));
        view.getProjectPreparationView().getIndicatorAssessmentComparativeAdvantage().setValue(managedProject.getPpIndicatorAssessmentComparativeAdvantage());
        view.getProjectPreparationView().getConclusionUOTextFileUploader().setFiles(managedProject.getPpConclusionUOTextFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getProjectPreparationView().getFinancialModelTextFileUploader().setFiles(managedProject.getPpFinancialModelTextFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        if (managedProject.getPpConclusionUOTextFileVersionId() != null && managedProject.getPpConclusionUOTextFileVersionId().size() > 0
                || managedProject.getPpFinancialModelTextFileVersionId() != null && managedProject.getPpFinancialModelTextFileVersionId().size() > 0)
            view.getProjectPreparationView().getCp7().expand();

        view.getProjectPreparationView().getLinkToClauseAgreement().setValue(managedProject.getPpLinkToClauseAgreement());
        view.getProjectPreparationView().getIsPrivateLiabilityProvided().setValue(managedProject.getPpIsPrivateLiabilityProvided());
        view.getProjectPreparationView().getLinkToClauseAgreementLiabilityProvided().setValue(managedProject.getPpLinkToClauseAgreementLiabilityProvided());

        setValueFromPlanningInvestments(managedProject.getPpInvestmentPlanningAmount());
        setValueFromPlanningCreationInvestments(managedProject.getPpCreationInvestmentPlanningAmount());
        setValueFromCbcInvestments1(managedProject.getCbcInvestments1());
        setValueFromCbcInvestments2(managedProject.getCbcInvestments2());
        setValueFromCbcInvestments3(managedProject.getCbcInvestments3());
        setValueFromCbcInvestments4(managedProject.getCbcInvestments4());
        setValueFromRemainingDebt(managedProject.getRemainingDebt());
//        setValueFromBalanceOfDebtToView();
        selectComboById(view.getCreationView().getAgreementComplex(), managedProject.getCrAgreementComplex());
        view.getCreationView().getAgreementStartDate().setValue(getDate(managedProject.getCrAgreementStartDate()));
        view.getCreationView().getAgreementEndDate().setValue(getDate(managedProject.getCrAgreementEndDate()));
        view.getCreationView().getAgreementValidity().setValue(managedProject.getCrAgreementValidity());

        view.getCreationView().getJobDoneTerm().setValue(getDate(managedProject.getCrJobDoneTerm()));
        view.getCreationView().getSavingStartDate().setValue(getDate(managedProject.getCrSavingStartDate()));
        view.getCreationView().getSavingEndDate().setValue(getDate(managedProject.getCrSavingEndDate()));
        view.getCreationView().getInvestmentStageTerm().setValue(managedProject.getCrInvestmentStageTerm());
        view.getCreationView().getIsAutoProlongationProvided().setValue(managedProject.getCrIsAutoProlongationProvided());
        view.getCreationView().getAgreementEndDateAfterProlongation().setValue(getDate(managedProject.getCrAgreementEndDateAfterProlongation()));
        view.getCreationView().getAgreementValidityAfterProlongation().setValue(managedProject.getCrAgreementValidityAfterProlongation());
        selectComboById(view.getCreationView().getOpf(), managedProject.getCrOpf());
        view.getCreationView().getIsForeignInvestor().setValue(managedProject.getCrIsForeignInvestor());
        view.getCreationView().getIsMcpSubject().setValue(managedProject.getCrIsMcpSubject());
        view.getCreationView().getAgreementTextFiles().setFiles(managedProject.getCrAgreementTextFiles().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        view.getCreationView().getConcessionaire().setValue(managedProject.getCrConcessionaire());
        view.getCreationView().getConcessionaireInn().setValue(managedProject.getCrConcessionaireInn());
        view.getExtraView().getPrivatePartnersOwningPartsView().setEgrulLabels(managedProject.getCrConcessionaireInn(), true);
        view.getCreationView().getFinancialClosingProvided().setValue(managedProject.getCrFinancialClosingProvided());
        // для отработки логики скрытия атрибутов в зависимости от галочки
        ValueChangeEvent.fire(view.getCreationView().getFinancialClosingProvided(), managedProject.getCrFinancialClosingProvided());

        view.getCreationView().getFinancialClosingDate().setValue(getDate(managedProject.getCrFinancialClosingDate()));
        view.getCreationView().getFinancialClosingValue().setValue(managedProject.getCrFinancialClosingValue());
        view.getCreationView().getFinancialClosingIsMutualAgreement().setValue(managedProject.getCrFinancialClosingIsMutualAgreement());
        view.getCreationView().getFirstObjectCreationPlanDate().setValue(getDate(managedProject.getCrFirstObjectCreationPlanDate()));
        view.getCreationView().getFirstObjectCreationFactDate().setValue(getDate(managedProject.getCrFirstObjectCreationFactDate()));


        view.getCreationView().getIsRegionPartyAgreement().setValue(managedProject.getCrIsRegionPartyAgreement());

        view.getCreationView().getIsSeveralObjects().setValue(managedProject.getCrIsSeveralObjects());


        view.getCreationView().getFirstSeveralObjectPlanDate().setValue(getDate(managedProject.getCrFirstSeveralObjectPlanDate()));

        view.getCreationView().getIsFirstSeveralObject().setValue(managedProject.getCrIsFirstSeveralObject());
        view.getCreationView().getLastSeveralObjectPlanDate().setValue(getDate(managedProject.getCrLastSeveralObjectPlanDate()));

        view.getCreationView().getIsLastSeveralObject().setValue(managedProject.getCrIsLastSeveralObject());
        view.getCreationView().getSeveralObjectPlanDate().setValue(getDate(managedProject.getCrSeveralObjectPlanDate()));

        if (managedProject.getCrSeveralObjectPlanDate() != null
                || managedProject.getCrLastSeveralObjectPlanDate() != null
                || managedProject.getCrFirstSeveralObjectPlanDate() != null)
            view.getCreationView().getCp8().expand();

        view.getCreationView().getIsSeveralObjectInOperation().setValue(managedProject.getCrIsSeveralObjectInOperation());


        view.getCreationView().getInvestCostsGrantor().setValue(managedProject.getCrInvestCostsGrantor());


        view.getCreationView().getIsFormulasInvestCosts().setValue(managedProject.getCrIsFormulasInvestCosts());

        view.getCreationView().getCalcInvestCostsFileUpload().setFiles(managedProject.getCrCalcInvestCostsActFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        view.getCreationView().getActualCostsValue().setValue(managedProject.getCrActualCostsValue());

        view.getCreationView().getAverageInterestRateValue().setValue(managedProject.getCrAverageInterestRateValue());

        view.getCreationView().getLastObjectCreationPlanDate().setValue(getDate(managedProject.getCrLastObjectCreationPlanDate()));
        view.getCreationView().getLastObjectCreationFactDate().setValue(getDate(managedProject.getCrLastObjectCreationFactDate()));
        view.getCreationView().getIsObjectTransferProvided().setValue(managedProject.getCrIsObjectTransferProvided());
        view.getCreationView().getObjectRightsPlanDate().setValue(getDate(managedProject.getCrObjectRightsPlanDate()));
        view.getCreationView().getObjectRightsFactDate().setValue(getDate(managedProject.getCrObjectRightsFactDate()));
        view.getCreationView().getObjectValue().setValue(managedProject.getCrObjectValue());
        view.getCreationView().getIsRenewableBankGuarantee().setValue(managedProject.getCrIsRenewableBankGuarantee());
        view.getCreationView().getIsGuaranteeVariesByYear().setValue(managedProject.getCrIsGuaranteeVariesByYear());
        ValueChangeEvent.fire(view.getCreationView().getIsGuaranteeVariesByYear(), managedProject.getCrIsGuaranteeVariesByYear());

        setBankGuaranteeToView();

        view.getCreationView().getLandProvided().setValue(managedProject.getCrLandProvided());
        ValueChangeEvent.fire(view.getCreationView().getLandProvided(), managedProject.getCrLandProvided());
        view.getCreationView().getLandIsConcessionaireOwner().setValue(managedProject.getCrLandIsConcessionaireOwner());
        view.getCreationView().getLandActStartPlanDate().setValue(getDate(managedProject.getCrLandActEndPlanDate()));
        view.getCreationView().getLandActStartFactDate().setValue(getDate(managedProject.getCrLandActEndFactDate()));
        view.getCreationView().getLandActEndPlanDate().setValue(getDate(managedProject.getCrLandActEndPlanDate()));
        view.getCreationView().getLandActEndFactDate().setValue(getDate(managedProject.getCrLandActEndFactDate()));
        view.getCreationView().getIsObligationExecuteOnCreationStage().setValue(managedProject.getCrIsObligationExecuteOnCreationStage(), true);

        setEnsureMethodsToView();

        selectMulticomboById(view.getCreationView().getGovSupport(), managedProject.getCrGovSupport().stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()));
        view.getCreationView().getAgreementFileUpload().setFiles(managedProject.getCrAgreementTextFvId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getCreationView().getFinancialClosingFileUpload().setFiles(managedProject.getCrFinancialClosingActFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getCreationView().getFirstObjectCompleteActFileUpload().setFiles(managedProject.getCrFirstObjectCompleteActFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getCreationView().getLastObjectCompleteActFileUpload().setFiles(managedProject.getCrLastObjectCompleteActFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        view.getCreationView().getInvestmentVolumeStagOfCreationFileUpload().setFiles(managedProject.getCrInvestmentVolumeStagOfCreationActFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        view.getCreationView().getActFileUpload().setFiles(managedProject.getCrActFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getCreationView().getReferenceFileUpload().setFiles(managedProject.getCrReferenceFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getCreationView().getLandActTextFileUpload().setFiles(managedProject.getCrLandActFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getCreationView().getConfirmationDocFileUpload().setFiles(managedProject.getCrConfirmationDocFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        setValueFromCreationInvestments(managedProject.getCrInvestmentCreationAmount());

        view.getCreationView().getExpectedRepaymentYear().setValue(managedProject.getCrExpectedRepaymentYear());

//        view.getExploitationView().getLastObjectPlanDate().setValue(getDate(managedProject.getExLastObjectPlanDate()));
        view.getExploitationView().getLastObjectFactDate().setValue(getDate(managedProject.getExLastObjectFactDate()));
        view.getExploitationView().getIsInvestmentRecoveryProvided().setValue(managedProject.getExIsInvestmentsRecoveryProvided());
        selectComboById(view.getExploitationView().getIrSource(), managedProject.getExIRSource());

        selectComboById(view.getExploitationView().getIrBudgetLevel(), managedProject.getExIRLevel());
        view.getExploitationView().getIsObligationExecutingOnOperationPhase().setValue(managedProject.getExIsObligationExecutingOnOperationPhase(), true);

        setExEnsureMethodsToView();
        view.getExploitationView().getIsRenewableBankGuarantee().setValue(managedProject.getExIsRenewableBankGuarantee());
        view.getExploitationView().getIsGuaranteeVariesByYear().setValue(managedProject.getExIsGuaranteeVariesByYear());
        ValueChangeEvent.fire(view.getExploitationView().getIsGuaranteeVariesByYear(), managedProject.getExIsGuaranteeVariesByYear());

        setExBankGuaranteeToView();
        view.getExploitationView().getIsConcessionPaymentProvided().setValue(managedProject.getExIsConcessionPayProvideded());
        ValueChangeEvent.fire(view.getExploitationView().getIsConcessionPaymentProvided(), managedProject.getExIsConcessionPayProvideded());

        selectComboById(view.getExploitationView().getPaymentMethod(), managedProject.getExPaymentForm());
        view.getExploitationView().getLastObjectActFileUpload().setFiles(managedProject.getExLastObjectActFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        setValueFromExploitationInvestments(managedProject.getExInvestmentExploitationAmount());

        view.getExploitationView().getInvestmentVolumeStagOfExploitationFileUpload().setFiles(managedProject.getExInvestmentVolumeStagOfExploitationActFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        setValueFromExploitationInvestmentsRecovery(managedProject.getExInvestmentExploitationRecoveryAmount());

        view.getExploitationView().getExPublicShareExpl().setValue(managedProject.getExPublicShareExpl());
        view.getExploitationView().getExHasPublicShareExpl().setValue(managedProject.getExHasPublicShareExpl());
        view.getExploitationView().getExFinModelFVIds().setFiles(managedProject.getExFinModelFVIds().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        selectMulticomboById(view.getExploitationView().getCostRecoveryMethod(), managedProject.getExCostRecoveryMethod().stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()));
        view.getExploitationView().getExCostRecoveryMechanism().setValue(managedProject.getExCostRecoveryMechanism());
        view.getExploitationView().getExSupportDocFVIds().setFiles(managedProject.getExSupportDocFVIds().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getExploitationView().getExSupportCompensDocFVIds().setFiles(managedProject.getExSupportCompensDocFVIds().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        view.getExploitationView().getExOwnPrivatePlanDate().setValue(getDate(managedProject.getExOwnPrivatePlanDate()));
        view.getExploitationView().getExOwnPrivateFactDate().setValue(getDate(managedProject.getExOwnPrivateFactDate()));
        view.getExploitationView().getExOwnPublicPlanDate().setValue(getDate(managedProject.getExOwnPublicPlanDate()));
        view.getExploitationView().getExOwnPublicFactDate().setValue(getDate(managedProject.getExOwnPublicFactDate()));
        view.getExploitationView().getExAgreementFVIds().setFiles(managedProject.getExAgreementFVIds().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getExploitationView().getExAcceptActFVIds().setFiles(managedProject.getExAcceptActFVIds().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getExploitationView().getExStartAchEconPlanDate().setValue(getDate(managedProject.getExStartAchEconPlanDate()));
        view.getExploitationView().getExEndAchEconPlanDate().setValue(getDate(managedProject.getExEndAchEconPlanDate()));
        view.getExploitationView().getExStartAchEconFactDate().setValue(getDate(managedProject.getExStartAchEconFactDate()));
        view.getExploitationView().getExEndAchEconFactDate().setValue(getDate(managedProject.getExEndAchEconFactDate()));
        view.getExploitationView().getExAcceptActAAMFVIds().setFiles(managedProject.getExAcceptActAAMFVIds().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getExploitationView().getExCalculationPlannedAmountFVIds().setFiles(managedProject.getExCalculationPlannedAmountFVIds().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getExploitationView().getExInvestStagePlanDate().setValue(getDate(managedProject.getExInvestStagePlanDate()));
        view.getExploitationView().getExInvestStageFactDate().setValue(getDate(managedProject.getExInvestStageFactDate()));
        view.getExploitationView().getExGrantorExpenses().setValue(managedProject.getExGrantorExpenses());
        view.getExploitationView().getExFormulasOrIndexingOrderEstablished().setValue(managedProject.getExFormulasOrIndexingOrderEstablished());

        setExploitationPaymentsToView();
        setExploitationCompensationsToView();

        selectComboById(view.getTerminationView().getCause(), managedProject.getTmCause());
        view.getTerminationView().getActPlanDate().setValue(getDate(managedProject.getTmActPlanDate()));
        view.getTerminationView().getActFactDate().setValue(getDate(managedProject.getTmActFactDate()));
        view.getTerminationView().getActNumber().setValue(managedProject.getTmActNumber());
        view.getTerminationView().getActDate().setValue(getDate(managedProject.getTmActDate()));
        view.getTerminationView().getCauseDescription().setValue(managedProject.getTmCauseDescription());
        view.getTerminationView().getFactDate().setValue(getDate(managedProject.getTmFactDate()));
        view.getTerminationView().getPlanDate().setValue(getDate(managedProject.getTmPlanDate()));
        view.getTerminationView().getPropertyJointProvided().setValue(managedProject.getTmPropertyJointProvided());
        view.getTerminationView().getTmPropertyJointPrivatePercent().setValue(managedProject.getTmPropertyJointPrivatePercent());
        view.getTerminationView().getTmPropertyJointPublicPercent().setValue(managedProject.getTmPropertyJointPublicPercent());
        view.getTerminationView().getCompensationPayed().setValue(managedProject.getTmIsCompensationPayed());
        ValueChangeEvent.fire(view.getTerminationView().getCompensationPayed(), managedProject.getTmIsCompensationPayed());

        view.getTerminationView().getCompensationValue().setValue(managedProject.getTmCompensationValue());
        view.getTerminationView().getCompensationTextFileUpload().setFiles(managedProject.getTmCompensationFVIds().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        selectComboById(view.getTerminationView().getAftermath(), managedProject.getTmAftermath());
        view.getTerminationView().getTerminationActDate().setValue(getDate(managedProject.getTmContractDate()));
        view.getTerminationView().getTerminationActNumber().setValue(managedProject.getTmContractNumber());
        view.getTerminationView().getPublicSideShare().setValue(managedProject.getTmPublicShare());
        view.getTerminationView().getRfShare().setValue(managedProject.getTmIsRfHasShare());
        view.getTerminationView().getActTextFileUpload().setFiles(managedProject.getTmTextFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getTerminationView().getTaActFileUpload().setFiles(managedProject.getTmTaActTextFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getTerminationView().getTmAnotherDescription().setValue(managedProject.getTmAnotherDescription());
        view.getTerminationView().getTmClausesOfAgreement().setValue(managedProject.getTmClausesOfAgreement());
        view.getTerminationView().getTmCompensationLimit().setValue(managedProject.getTmCompensationLimit());
        view.getTerminationView().getTmAgreementTerminated().setValue(managedProject.getTmAgreementTerminated());
        view.getTerminationView().getTmCompensationSum().setValue(managedProject.getTmCompensationSum());
        view.getTerminationView().getTmNdsCheck().setValue(managedProject.getTmNdsCheck());

        if (view.getTerminationView().getTmMeasureType() != null) {
            selectComboById(view.getTerminationView().getTmMeasureType(), managedProject.getTmMeasureType());
            if ("1".equals(String.valueOf(view.getTerminationView().getTmMeasureType().getId())))
                view.getCreationView().getInvestments().getDateField().hide();
        }

        view.getTerminationView().getTmDateField().setValue(getDate(managedProject.getTmDateField()));
        view.getTerminationView().getTmCompositionOfCompensation().setValue(managedProject.getTmCompositionOfCompensation());
        view.getTerminationView().getTmSupportingDocuments().setFiles(managedProject.getTmSupportingDocuments().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        selectMulticomboById(view.getTerminationView().getTmCompositionOfCompensationGrantorFault(), managedProject.getTmCompositionOfCompensationGrantorFault().stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()));

        view.getConditionChangeView().getIsChanged().setValue(managedProject.getCcIsChangesMade());
        selectComboById(view.getConditionChangeView().getCause(), managedProject.getCcReason());
        view.getConditionChangeView().getActNumber().setValue(managedProject.getCcActNumber());
        view.getConditionChangeView().getActDate().setValue(getDate(managedProject.getCcActDate()));
        view.getConditionChangeView().getActFileUpload().setFiles(managedProject.getCcTextFileVersionId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));

        if (managedProject.getCcActNumber() != null
                || managedProject.getCcActDate() != null
                || managedProject.getCcTextFileVersionId() != null && managedProject.getCcTextFileVersionId().size() > 0
        )
            view.getConditionChangeView().getCp2().expand();

        setEventsToView();
        view.getExtraView().getIsThirdPartyOrgsProvided().setValue(managedProject.getAdsIsThirdPartyOrgsProvided());
        // для отработки логики скрытия атрибутов в зависимости от галочки
        ValueChangeEvent.fire(view.getExtraView().getIsThirdPartyOrgsProvided(), managedProject.getAdsIsThirdPartyOrgsProvided());
        setPrivatePartnerThirdPartyOrgToView();
        setPublicPartnerThirdPartyOrgToView();


        view.getExtraView().getFinancialEconomicsIndView().setNPV(managedProject.getAdsNpv());
        view.getExtraView().getFinancialEconomicsIndView().setIRR(managedProject.getAdsIrr());
        view.getExtraView().getFinancialEconomicsIndView().setPB(managedProject.getAdsPb());
        view.getExtraView().getFinancialEconomicsIndView().setPBDiscounted(managedProject.getAdsPbDiscounted());
        view.getExtraView().getFinancialEconomicsIndView().setEBIDTA(managedProject.getAdsEbidta());
        view.getExtraView().getFinancialEconomicsIndView().setWACC(managedProject.getAdsWacc());

        view.getExtraView().getIsRegInvestmentProject().setValue(managedProject.getAdsIsRegInvestmentProject());

        view.getExtraView().getHasIncomeTax().setValue(managedProject.getAdsHasIncomeTax());
        selectComboById(view.getExtraView().getIncomeTaxRate(), managedProject.getAdsIncomeTaxRate());
        view.getExtraView().getHasLandTax().setValue(managedProject.getAdsHasLandTax());
        selectComboById(view.getExtraView().getLandTaxRate(), managedProject.getAdsLandTaxRate());
        view.getExtraView().getHasPropertyTax().setValue(managedProject.getAdsHasPropertyTax());
        selectComboById(view.getExtraView().getPropertyTaxRate(), managedProject.getAdsPropertyTaxRate());
        view.getExtraView().getHasBenefitClarificationTax().setValue(managedProject.getAdsHasBenefitClarificationTax());
        selectComboById(view.getExtraView().getBenefitClarificationRate(), managedProject.getAdsBenefitClarificationRate());

        if (managedProject.getAdsHasIncomeTax() != null && managedProject.getAdsHasIncomeTax()
                || managedProject.getAdsHasLandTax() != null && managedProject.getAdsHasLandTax()
                || managedProject.getAdsHasPropertyTax() != null && managedProject.getAdsHasPropertyTax()
                || managedProject.getAdsHasBenefitClarificationTax() != null && managedProject.getAdsHasBenefitClarificationTax()
        )
            view.getExtraView().getCp12().expand();

        view.getExtraView().getBenefitDescription().setValue(managedProject.getAdsBenefitDescription());

        view.getExtraView().getIsTreasurySupport().setValue(managedProject.getAdsIsTreasurySupport());
        ValueChangeEvent.fire(view.getExtraView().getIsTreasurySupport(), managedProject.getAdsIsTreasurySupport());

        view.getExtraView().getDecisionTextFileUpload().setFiles(managedProject.getAdsDecisionTextFileId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        selectComboById(view.getExtraView().getOpf(), managedProject.getAdsConcessionaireOpf());

        view.getExtraView().getConcessionaireName().setValue(managedProject.getAdsConcessionaireName());
        view.getExtraView().getConcessionaireInn().setValue(managedProject.getAdsConcessionaireInn());

        // Кредитный рейтинг организации
        view.getExtraView().getCreditRating().setValue(managedProject.getAdsConcessionaireCreditRating());
        view.getExtraView().getCreditRatingSetDate().setValue(getDate(managedProject.getAdsConcessionaireCreditRatingStartDate()));
        view.getExtraView().getCreditRatingReviewDate().setValue(getDate(managedProject.getAdsConcessionaireCreditRatingEndDate()));
        view.getExtraView().getCreditRatingAgency().setValue(managedProject.getAdsConcessionaireCreditRatingAgency());

        view.getExtraView().getUnforeseenExpencesComment().setValue(managedProject.getAdsUnforeseenExpencesShareComment());
        view.getExtraView().getWorkPlacesCount().setValue(managedProject.getAdsWorkPlacesCount());

        selectMulticomboById(view.getExtraView().getCompetitionCriteria(), managedProject.getAdsCompetitionCriteria().stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()));
        selectMulticomboById(view.getExtraView().getFinancialRequirement(), managedProject.getAdsFinancialRequirement().stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()));
        selectMulticomboById(view.getExtraView().getNonFinancialRequirement(), managedProject.getAdsNonFinancialRequirements().stream().map(i -> String.valueOf(i.getId())).collect(Collectors.toList()));

        if (managedProject.getAdsCompetitionCriteria() != null && managedProject.getAdsCompetitionCriteria().size() > 0
                || managedProject.getAdsFinancialRequirement() != null && managedProject.getAdsFinancialRequirement().size() > 0
                || managedProject.getAdsNonFinancialRequirements() != null && managedProject.getAdsNonFinancialRequirements().size() > 0
        )
            view.getExtraView().getCp11().expand();

        // Вид режима
        selectComboById(view.getExtraView().getRegimeType(), managedProject.getAdsConcessionaireRegime());

        if (managedProject.getAdsConcessionaireRegime() != null
                || managedProject.getAdsConcessionaireCreditRating() != null
                || managedProject.getAdsConcessionaireCreditRatingStartDate() != null
                || managedProject.getAdsConcessionaireCreditRatingEndDate() != null
                || managedProject.getAdsConcessionaireCreditRatingAgency() != null
                || managedProject.getCrConcessionaireInn() != null
        )
            view.getExtraView().getCp7().expand();


        setValueFromInvestmentsInObjectToView();
        setValueFromOperationalCostsToView();
        setValueFromTaxConditionToView();
        setValueFromRevenueIndicatorsToView();
        view.getFinancialAndEconomicIndicatorsView().getFeiTaxIncentivesExist().setValue(managedProject.getFeiTaxIncentivesExist());
        if (managedProject.getFeiResidualValue() != null) {
            view.getFinancialAndEconomicIndicatorsView().getFeiResidualValue().setValue(NumberFormat.getFormat("#,##0.0#############").format(managedProject.getFeiResidualValue()));
        }
        if (managedProject.getFeiAverageServiceLife() != null) {
            view.getFinancialAndEconomicIndicatorsView().getFeiAverageServiceLife().setValue(NumberFormat.getFormat("#,##0.0#############").format(managedProject.getFeiAverageServiceLife()));
        }
        view.getFinancialAndEconomicIndicatorsView().getFeiForecastValuesDate().setValue(getDate(managedProject.getFeiForecastValuesDate()));

        if (managedProject.getFeiResidualValue() != null && managedProject.getFeiResidualValue() != 0
                || managedProject.getFeiAverageServiceLife() != null && managedProject.getFeiAverageServiceLife() != 0
                || managedProject.getFeiForecastValuesDate() != null && managedProject.getFeiForecastValuesDate() != 0)
            view.getFinancialAndEconomicIndicatorsView().getCp4().expand();

        setSanctionsToView();
        setJudicialActivityToView();
        setPrivatePartnersOwningPartsToView();
        setFinancialStructureToView();
        setInvestmentsCriteriaBooleanToView();
        setCompositionOfCompensationToView();

        view.getCommentsView().getComment().setValue(managedProject.getCmComment());
        view.getCommentsView().getContacts().setValue(managedProject.getCmContacts());
        view.getCommentsView().getIsAlwaysDraftState().setValue(managedProject.getGiAlwaysDraftStatus());
        // не устанавливаем статус документа в комбобокс при загрузке документа
        // selectComboById(view.getCommentsView().getProjectStatus(), getStringValOrEmpty(() -> String.valueOf(managedProject.getGiProjectStatus().getId())));

        if (managedProject.getGiAlwaysDraftStatus() != null && managedProject.getGiAlwaysDraftStatus()) {
            view.getCommentsView().getProjectStatus().setEnabled(false);
        }

        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedExist().setValue(managedProject.getCbcMinimumGuaranteedExist());
        view.getContingentBudgetaryCommitmentsView().getProjectBudgetObligationMissing().setValue(managedProject.getCbcProjectBudgetObligationMissing());
        selectComboById(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedIncomeForm(), managedProject.getCbcMinimumGuaranteedIncomeForm());
        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedClauseAgreement().setValue(managedProject.getCbcMinimumGuaranteedClauseAgreement());
        view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedExist().setValue(managedProject.getCbcCompensationMinimumGuaranteedExist());
        view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedExist().setValue(managedProject.getCbcNonPaymentConsumersGoodsProvidedExist());
        view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedClauseAgreement().setValue(managedProject.getCbcNonPaymentConsumersGoodsProvidedClauseAgreement());
        selectComboById(view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedForm(), managedProject.getCbcNonPaymentConsumersGoodsProvidedForm());
        view.getContingentBudgetaryCommitmentsView().getLimitNonPaymentConsumersGoodsProvidedExist().setValue(managedProject.getCbcLimitNonPaymentConsumersGoodsProvidedExist());
        view.getContingentBudgetaryCommitmentsView().getLimitNonPaymentConsumersGoodsProvidedClauseAgreement().setValue(managedProject.getCbcLimitNonPaymentConsumersGoodsProvidedClauseAgreement());
        view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentConsumersGoodsProvidedExist().setValue(managedProject.getCbcCompensationLimitNonPaymentConsumersGoodsProvidedExist());
        view.getContingentBudgetaryCommitmentsView().getArisingProvisionOfBenefitsExist().setValue(managedProject.getCbcArisingProvisionOfBenefitsExist());
        view.getContingentBudgetaryCommitmentsView().getCbcArisingProvisionOfBenefitsClauseAgreement().setValue(managedProject.getCbcArisingProvisionOfBenefitsClauseAgreement());
        view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsExist().setValue(managedProject.getCbcCompensationArisingProvisionOfBenefitsExist());
        view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsClauseAgreement().setValue(managedProject.getCbcCompensationArisingProvisionOfBenefitsClauseAgreement());
        view.getContingentBudgetaryCommitmentsView().getDueToOnsetOfCertainCircumstancesExist().setValue(managedProject.getCbcDueToOnsetOfCertainCircumstancesExist());
        view.getContingentBudgetaryCommitmentsView().getCbcDueToOnsetOfCertainCircumstancesClauseAgreement().setValue(managedProject.getCbcDueToOnsetOfCertainCircumstancesClauseAgreement());
        view.getContingentBudgetaryCommitmentsView().getLimitCompensationAdditionalCostsAgreementExist().setValue(managedProject.getCbcLimitCompensationAdditionalCostsAgreementExist());

        view.getContingentBudgetaryCommitmentsView().getLimitCompensationAdditionalClauseAgreement().setValue(managedProject.getCbcLimitCompensationAdditionalClauseAgreement());
        selectMulticomboById(view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostPrepare(), managedProject.getCbcNameOfCircumstanceAdditionalCostPrepare().stream().map(i -> String.valueOf(i)).collect(Collectors.toList()));
        selectMulticomboById(view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostBuild(), managedProject.getCbcNameOfCircumstanceAdditionalCostBuild().stream().map(i -> String.valueOf(i)).collect(Collectors.toList()));
        selectMulticomboById(view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostExploitation(), managedProject.getCbcNameOfCircumstanceAdditionalCostExploitation().stream().map(i -> String.valueOf(i)).collect(Collectors.toList()));
        view.getContingentBudgetaryCommitmentsView().getSpecifyOtherCircumstancesPrepare().setValue(managedProject.getCbcSpecifyOtherCircumstancesPrepare());
        view.getContingentBudgetaryCommitmentsView().getSpecifyOtherCircumstancesBuild().setValue(managedProject.getCbcSpecifyOtherCircumstancesBuild());
        view.getContingentBudgetaryCommitmentsView().getSpecifyOtherCircumstancesExploitation().setValue(managedProject.getCbcSpecifyOtherCircumstancesExploitation());
        view.getContingentBudgetaryCommitmentsView().getCompensationAdditionalCostsAgreementExist().setValue(managedProject.getCbcCompensationAdditionalCostsAgreementExist());

        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedFileUpload().setFiles(managedProject.getCbcMinimumGuaranteedFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersFileUpload().setFiles(managedProject.getCbcNonPaymentConsumersFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getContingentBudgetaryCommitmentsView().getArisingProvisionOfBenefitFileUpload().setFiles(managedProject.getCbcArisingProvisionOfBenefitFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsFileUpload().setFiles(managedProject.getCbcCompensationArisingProvisionOfBenefitsFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        view.getContingentBudgetaryCommitmentsView().getCompensationAdditionalCostsAgreementFileUpload().setFiles(managedProject.getCbcCompensationAdditionalCostsAgreementFVId().stream().map(i -> new FileUploader.GridFileModel(i.getFileName(), i.getId())).collect(Collectors.toList()));
        setValueFromMinimumGuaranteedAmountToView();

        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getNdsCheck().setValue(managedProject.getCbcMinimumGuaranteedAmountNdsCheck());

        if (view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getMeasureType() != null) {
            selectComboById(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getMeasureType(), managedProject.getCbcMinimumGuaranteedAmountMeasureType());
            if ("1".equals(String.valueOf(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getMeasureType().getId())))
                view.getCreationView().getInvestments().getDateField().hide();
        }

        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getDateField().setValue(getDate(managedProject.getCbcMinimumGuaranteedAmountDateField()));

        setValueFromCompensationMinimumGuaranteedAmountToView();
        setValueFromCompensationLimitNonPaymentAmountToView();
        setValueFromCompensationArisingProvisionOfBenefitsAmountToView();
        setValueFromLimitCompensationAdditionalCostsAmountToView();

        view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getNdsCheck().setValue(managedProject.getIoNdsCheck());
        GWT.log("get IoNdsCheck from managedProject");
        if (managedProject.getIoNdsCheck() != null) {
            GWT.log(managedProject.getIoNdsCheck().toString());
        }
        view.getContingentBudgetaryCommitmentsView().getCbcNdsCheck().setValue(managedProject.getCbcNdsCheck());

        if (view.getContingentBudgetaryCommitmentsView().getCbcMeasureType() != null) {
            selectComboById(view.getContingentBudgetaryCommitmentsView().getCbcMeasureType(), managedProject.getCbcMeasureType());
            if ("1".equals(String.valueOf(view.getContingentBudgetaryCommitmentsView().getCbcMeasureType().getId())))
                view.getCreationView().getInvestments().getDateField().hide();
        }

        if (view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getMeasureType() != null) {
            GWT.log("get IoMeasureType from managedProject");
            if (managedProject.getIoMeasureType() != null) {
                GWT.log(String.valueOf(managedProject.getIoMeasureType()));
            }
            selectComboById(view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getMeasureType(), managedProject.getIoMeasureType());
            if ("1".equals(String.valueOf(view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getMeasureType().getId())))
                view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getDateField().hide();
        }

        view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getDateField().setValue(getDate(managedProject.getIoDateField()));
        GWT.log("get IoDateField from managedProject");
        if (managedProject.getIoDateField() != null) {
            GWT.log(managedProject.getIoDateField().toString());
        }
        view.getContingentBudgetaryCommitmentsView().getCbcDateField().setValue(getDate(managedProject.getCbcDateField()));

        setValueFromCircumstancesAdditionalCostsAmountToView();
    }

    private void setValuesToPassport() {

        view.getProjectPassportView().setEmblem(managedProject.getGerbName());
        view.getProjectPassportView().setOpfLabel(getStringValOrDefault(() -> managedProject.getGiOPF().getName(), "-"));
        view.getProjectPassportView().setSubjectLabel(getStringValOrDefault(() -> managedProject.getGiRegion().getName(), "-"));
        view.getProjectPassportView().setPopulationSubjectLabel("-");
        view.getProjectPassportView().setMoLabel(getStringValOrDefault(() -> managedProject.getGiMunicipality().getName(), "-"));
        view.getProjectPassportView().setPopulationMoLabel("-");

        view.getProjectPassportView().setNameLabel(getStringValOrDefault(() -> managedProject.getGiName(), "-"));
        view.getProjectPassportView().setStatusLabel(getStringValOrDefault(() -> managedProject.getGiRealizationStatus().getName(), "-"));
        view.getProjectPassportView().setIdLabel(String.valueOf(managedProject.getId()));
        view.getProjectPassportView().setAgreementMethodLabel(getStringValOrDefault(() -> managedProject.getGiInitiationMethod().getName(), "-"));
        view.getProjectPassportView().setImplFormLabel(getStringValOrDefault(() -> managedProject.getGiRealizationForm().getName(), "-"));
        view.getProjectPassportView().setImplLevelLabel(getStringValOrDefault(() -> managedProject.getGiRealizationLevel().getName(), "-"));
        view.getProjectPassportView().setImplSphereLabel(getStringValOrDefault(() -> managedProject.getGiRealizationSphere().getName(), "-"));
        view.getProjectPassportView().setImplSectorLabel(getStringValOrDefault(() -> managedProject.getGiRealizationSector().getName(), "-"));
        view.getProjectPassportView().setPublicNameLabel(getStringValOrDefault(() -> managedProject.getGiPublicPartner().getName(), "-"));
        view.getProjectPassportView().setPrivateNameLabel(getStringValOrDefault(() -> managedProject.getCrConcessionaire(), "-"));

        Date agreementStartDate = getDate(managedProject.getCrAgreementStartDate());

        view.getProjectPassportView().setAgreementDateLabel(agreementStartDate != null ? formatDate(agreementStartDate) : "-");

        Double agreementValidity = managedProject.getCrAgreementValidity();
        view.getProjectPassportView().setValidityLabel(agreementValidity != null ? agreementValidity.toString() : "-");

        Date exLastObjectPlanDate = getDate(managedProject.getExLastObjectPlanDate());
        view.getProjectPassportView().setCommissioningDateAgreementLabel(exLastObjectPlanDate != null ? formatDate(exLastObjectPlanDate) : "-");

        Date exLastObjectFactDate = getDate(managedProject.getExLastObjectFactDate());
        view.getProjectPassportView().setCommissioningDateFactLabel(exLastObjectFactDate != null ? formatDate(exLastObjectFactDate) : "-");

        Date crLastSeveralObjectPlanDate = getDate(managedProject.getCrLastSeveralObjectPlanDate());
        Boolean crIsSeveralObjects = managedProject.getCrIsSeveralObjects();
        Date crSeveralObjectPlanDate = getDate(managedProject.getCrSeveralObjectPlanDate());
        if (crLastSeveralObjectPlanDate != null && crIsSeveralObjects) {
            view.getExploitationView().getLastObjectPlanDate().setValue(crLastSeveralObjectPlanDate);
        } else view.getExploitationView().getLastObjectPlanDate().setValue(crSeveralObjectPlanDate);

        setValuesToPassportFormInvestmentsAmount();
        setValuesToPassportFormTechEconomicsIndicators();

        view.getProjectPassportView().attachWidget();
    }

    private void setValuesToPassportFormInvestmentsAmount() {
        CreationInvestments creationAmount = managedProject.getCrInvestmentCreationAmount();
        if (creationAmount != null) {
            List<CreationInvestmentIndicator> indicators = creationAmount.getIndicators();
            indicators.sort(Comparator.comparing(CreationInvestmentIndicator::getId));
            if (indicators.get(0).getPlan() != null) {
                view.getProjectPassportView().setTotalInvestmentSizeLabel(indicators.get(0).getPlan().toString());
            }

            if (indicators.get(1).getPlan() != null) {
                view.getProjectPassportView().setPrivateInvestmentSizeLabel(indicators.get(1).getPlan().toString());
            }

            if (indicators.size() > 4 && indicators.get(4).getPlan() != null) {
                view.getProjectPassportView().setBudgetExpendituresLabel(indicators.get(4).getPlan().toString());
            }
        }
    }

    private void setValuesToPassportFormTechEconomicsIndicators() {
        List<TechEconomicsObjectIndicator> indicators = managedProject.getOdTechEconomicsObjectIndicators();
        if (indicators != null) {
            indicators.sort(Comparator.comparing(TechEconomicsObjectIndicator::getId));

            view.getProjectPassportView().setTable(indicators);
        }
    }

    private void setValueFromInvestmentsInObjectToView() {
        view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getYearBox().deselectAll();
        if (managedProject.getFeiInvestmentsInObject() != null && managedProject.getFeiInvestmentsInObject().size() > 0) {
            List<String> savedYears = managedProject.getFeiInvestmentsInObject().get(0).getObjects()
                    .stream().map(i -> i.getName()).collect(Collectors.toList());
            view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getYearBox().select(savedYears, true);


            //в данной конфигурации таблицы корневой элемент всегда будет 1
            InvestmentInObjectMainIndicator mainIndicator = managedProject.getFeiInvestmentsInObject().get(0);
            PlanFactYear pfyMain = view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getTreeStore().getRootItems().get(0);
            pfyMain.setFact(mainIndicator.getFact());
            pfyMain.setPlan(mainIndicator.getPlan());
            view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getTreeStore().update(pfyMain);
            for (InvestmentInObject object : managedProject.getFeiInvestmentsInObject().get(0).getObjects()) {
                PlanFactIndicator pfyObject = new PlanFactIndicator();
                pfyObject.setId(object.getId());
                pfyObject.setGid(object.getId());
                pfyObject.setOrder(object.getId());
                pfyObject.setPlan(object.getPlan());
                pfyObject.setFact(object.getFact());
                pfyObject.setNameOrYear(object.getName());
                view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getTreeStore().add(pfyMain, pfyObject);
                for (InvestmentInObjectIndicator indicator : object.getIndicators()) {
                    PlanFactIndicator pfyIndicator = new PlanFactIndicator();
                    pfyIndicator.setGid(getRandId());
                    pfyIndicator.setId(indicator.getId());
                    pfyIndicator.setOrder(indicator.getId());
                    pfyIndicator.setFact(indicator.getFact());
                    pfyIndicator.setPlan(indicator.getPlan());
                    pfyIndicator.setNameOrYear(String.valueOf(indicator.getName()));
                    view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getTreeStore().add(pfyObject, pfyIndicator);
                    List<PlanFactYear> yearValues = new ArrayList<>();
                    for (InvestmentInObjectIndicatorYearValue yearValue : indicator.getYearValues()) {
                        PlanFactIndicator indicatirYear = new PlanFactIndicator();
                        indicatirYear.setGid(getRandId());
                        indicatirYear.setId(yearValue.getId());
                        indicatirYear.setFact(yearValue.getFact());
                        indicatirYear.setPlan(yearValue.getPlan());
                        indicatirYear.setNameOrYear(String.valueOf(yearValue.getYear()));
                        yearValues.add(indicatirYear);
                    }
                    view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getTreeStore().add(pfyIndicator, yearValues);
                }
            }
        }
    }

    private void setValueFromOperationalCostsToView() {
        view.getFinancialAndEconomicIndicatorsView().getOperationalCostsWidget().getYearBox().deselectAll();
        if (managedProject.getFeiOperationalCosts() != null && managedProject.getFeiOperationalCosts().size() > 0) {
            List<String> savedYears = managedProject.getFeiOperationalCosts().get(0).getYears()
                    .stream().map(i -> i.getYear()).collect(Collectors.toList());
            view.getFinancialAndEconomicIndicatorsView().getOperationalCostsWidget().getYearBox().select(savedYears, true);
        }
        for (PlanFactYear rootItem : view.getFinancialAndEconomicIndicatorsView().getOperationalCostsWidget().getTreeStore().getRootItems()) {
            for (OperationalCostsIndicator indicator : managedProject.getFeiOperationalCosts()) {
                // заносим данные в соответствующие строки
                if (indicator.getType().getId().equals(rootItem.getGid())) {
                    rootItem.setFact(indicator.getFact());
                    rootItem.setPlan(indicator.getPlan());
                    rootItem.setId(indicator.getId());
                    for (OperationalCostsIndicatorYearValue valByYear : indicator.getYears()) {
                        PlanFactYear pfy = new PlanFactYear();
                        pfy.setNameOrYear(String.valueOf(valByYear.getYear()));
                        pfy.setPlan(valByYear.getPlan());
                        pfy.setFact(valByYear.getFact());
                        pfy.setId(valByYear.getId()); // database id
                        pfy.setGid(getRandId());
                        view.getFinancialAndEconomicIndicatorsView().getOperationalCostsWidget().getTreeStore().add(rootItem, pfy);
                    }
                }
            }
        }
    }

    private void setValueFromTaxConditionToView() {
        view.getFinancialAndEconomicIndicatorsView().getTaxConditionWidget().getYearBox().deselectAll();
        if (managedProject.getFeiTaxCondition() != null && managedProject.getFeiTaxCondition().size() > 0) {
            List<String> savedYears = managedProject.getFeiTaxCondition().get(0).getYearValues()
                    .stream().map(i -> i.getYear().toString()).collect(Collectors.toList());
            view.getFinancialAndEconomicIndicatorsView().getTaxConditionWidget().getYearBox().select(savedYears, true);
        }
        view.getFinancialAndEconomicIndicatorsView().getTaxConditionWidget().getTreeStore().clear();
        for (TaxConditionIndicator indicator : managedProject.getFeiTaxCondition()) {
            TaxConditionMain tcm = new TaxConditionMain();
            tcm.setId(indicator.getId());
            tcm.setName(indicator.getName());
            if (indicator.getTaxConditionId() != null) {
                tcm.setTaxCondition(view.getFinancialAndEconomicIndicatorsView().getTaxConditionWidget().getTaxConditionDic().getStore().findModelWithKey(indicator.getTaxConditionId().toString()));
            }
            tcm.setTax(indicator.getTax());
            tcm.setGid(getRandId());
            view.getFinancialAndEconomicIndicatorsView().getTaxConditionWidget().getTreeStore().add(tcm);
            for (TaxConditionIndicatorYearValue yearValue : indicator.getYearValues()) {
                TaxConditionIndicatorByYear tcy = new TaxConditionIndicatorByYear();
                tcy.setId(yearValue.getId());
                tcy.setName(yearValue.getYear().toString());
                tcy.setTax(yearValue.getTax());
                tcy.setGid(getRandId());
                view.getFinancialAndEconomicIndicatorsView().getTaxConditionWidget().getTreeStore().add(tcm, tcy);
            }
        }
    }

    private void setValueFromRevenueIndicatorsToView() {
        view.getFinancialAndEconomicIndicatorsView().getRevenueIndicatorsWidget().getYearBox().deselectAll();
        if (managedProject.getFeiRevenue() != null && managedProject.getFeiRevenue().size() > 0) {
            List<String> savedYears = managedProject.getFeiRevenue().get(0).getIndicators()
                    .stream().map(i -> i.getName()).collect(Collectors.toList());
            view.getFinancialAndEconomicIndicatorsView().getRevenueIndicatorsWidget().getYearBox().select(savedYears, true);
        }
        view.getFinancialAndEconomicIndicatorsView().getRevenueIndicatorsWidget().getTreeStore().clear();
        for (RevenueServiceIndicator service : managedProject.getFeiRevenue()) {
            RevenueMain serviceInView = new RevenueMain();
            serviceInView.setId(service.getId());
            serviceInView.setGid(getRandId());
            serviceInView.setPlan(service.getPlan());
            serviceInView.setFact(service.getFact());
            serviceInView.setName(service.getName());
            view.getFinancialAndEconomicIndicatorsView().getRevenueIndicatorsWidget().getTreeStore().add(serviceInView);
            for (RevenueIndicator indicator : service.getIndicators()) {
                RevenueMain indicatorInView = new RevenueMain();
                indicatorInView.setGid(getRandId());
                indicatorInView.setId(indicator.getId());
                indicatorInView.setFact(indicator.getFact());
                indicatorInView.setPlan(indicator.getPlan());
                indicatorInView.setName(indicator.getName());
                indicatorInView.setUm(indicator.getUm());
                view.getFinancialAndEconomicIndicatorsView().getRevenueIndicatorsWidget().getTreeStore().add(serviceInView, indicatorInView);
                for (RevenueIndicatorYearValue yearValue : indicator.getYearValues()) {
                    RevenueByYear year = new RevenueByYear();
                    year.setGid(getRandId());
                    year.setId(yearValue.getId());
                    year.setFact(yearValue.getFact());
                    year.setPlan(yearValue.getPlan());
                    year.setName(String.valueOf(yearValue.getName()));
                    year.setUm(yearValue.getUm());
                    view.getFinancialAndEconomicIndicatorsView().getRevenueIndicatorsWidget().getTreeStore().add(indicatorInView, year);
                }
            }
        }
    }

    private void setValueToInvestmentsInObjectFromView() {
        if (managedProject != null && managedProject.getFeiInvestmentsInObject() != null) {
            managedProject.getFeiInvestmentsInObject().clear();

            for (PlanFactYear mainIndicatorFromView : view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getTreeStore().getRootItems()) {
                InvestmentInObjectMainIndicator mainIndicator = new InvestmentInObjectMainIndicator();
                mainIndicator.setId(mainIndicatorFromView.getId());
                mainIndicator.setFact(mainIndicatorFromView.getFact());
                mainIndicator.setPlan(mainIndicatorFromView.getPlan());
                mainIndicator.setName(mainIndicatorFromView.getNameOrYear());
                for (PlanFactYear objectFromView : view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getTreeStore().getChildren(mainIndicatorFromView)) {
                    InvestmentInObject object = new InvestmentInObject();
                    object.setId(objectFromView.getId());
                    object.setFact(objectFromView.getFact());
                    object.setPlan(objectFromView.getPlan());
                    object.setName(objectFromView.getNameOrYear());
                    mainIndicator.getObjects().add(object);
                    for (PlanFactYear indicatorFromView : view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getTreeStore().getChildren(objectFromView)) {
                        InvestmentInObjectIndicator objectIndicator = new InvestmentInObjectIndicator();
                        objectIndicator.setId(indicatorFromView.getId());
                        objectIndicator.setFact(indicatorFromView.getFact());
                        objectIndicator.setPlan(indicatorFromView.getPlan());
                        objectIndicator.setName(indicatorFromView.getNameOrYear());
                        object.getIndicators().add(objectIndicator);
                        for (PlanFactYear year : view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getTreeStore().getChildren(indicatorFromView)) {
                            InvestmentInObjectIndicatorYearValue yearValue = new InvestmentInObjectIndicatorYearValue();
                            yearValue.setId(year.getId());
                            yearValue.setFact(year.getFact());
                            yearValue.setPlan(year.getPlan());
                            yearValue.setYear(Long.parseLong(year.getNameOrYear()));
                            objectIndicator.getYearValues().add(yearValue);
                        }
                    }
                }
                managedProject.getFeiInvestmentsInObject().add(mainIndicator);
            }
        }
    }

    private void setValueToOperationalCostsFromView() {
        if (managedProject != null && managedProject.getFeiOperationalCosts() != null) {
            managedProject.getFeiOperationalCosts().clear();
            for (PlanFactYear indicator : view.getFinancialAndEconomicIndicatorsView().getOperationalCostsWidget().getTreeStore().getRootItems()) {
                OperationalCostsIndicator ind = new OperationalCostsIndicator();
                ind.setId(indicator.getId());
                ind.setFact(indicator.getFact());
                ind.setPlan(indicator.getPlan());
                ind.setName(indicator.getNameOrYear());
                InvestmentIndicatorType type = new InvestmentIndicatorType();
                type.setId(indicator.getGid());
                ind.setType(type);
                for (PlanFactYear byYear : view.getFinancialAndEconomicIndicatorsView().getOperationalCostsWidget().getTreeStore().getChildren(indicator)) {
                    OperationalCostsIndicatorYearValue y = new OperationalCostsIndicatorYearValue();
                    y.setId(byYear.getId());
                    y.setFact(byYear.getFact());
                    y.setPlan(byYear.getPlan());
                    y.setYear(byYear.getNameOrYear());
                    ind.getYears().add(y);
                }
                managedProject.getFeiOperationalCosts().add(ind);
            }
        }
    }

    private void setValueToTaxConditionFromView() {
        if (managedProject != null && managedProject.getFeiTaxCondition() != null) {
            managedProject.getFeiTaxCondition().clear();
            for (TaxConditionIndicatorByYear indicator : view.getFinancialAndEconomicIndicatorsView().getTaxConditionWidget().getTreeStore().getRootItems()) {
                TaxConditionIndicator ind = new TaxConditionIndicator();
                ind.setId(indicator.getId());
                if (indicator.getTaxCondition() != null) {
                    ind.setTaxConditionId(indicator.getTaxCondition().getId());
                }
                ind.setTax(indicator.getTax());
                ind.setName(indicator.getName());
                for (TaxConditionIndicatorByYear byYear : view.getFinancialAndEconomicIndicatorsView().getTaxConditionWidget().getTreeStore().getChildren(indicator)) {
                    TaxConditionIndicatorYearValue y = new TaxConditionIndicatorYearValue();
                    y.setId(byYear.getId());
                    y.setTax(byYear.getTax());
                    y.setYear(Integer.parseInt(byYear.getName()));
                    ind.getYearValues().add(y);
                }
                managedProject.getFeiTaxCondition().add(ind);
            }
        }
    }

    private void setValueToRevenueIndicatorsFromView() {
        if (managedProject != null && managedProject.getFeiRevenue() != null) {
            managedProject.getFeiRevenue().clear();
            for (RevenueByYear serviceFromView : view.getFinancialAndEconomicIndicatorsView().getRevenueIndicatorsWidget().getTreeStore().getRootItems()) {
                RevenueServiceIndicator service = new RevenueServiceIndicator();
                service.setId(serviceFromView.getId());

                service.setFact(serviceFromView.getFact());
                service.setPlan(serviceFromView.getPlan());
                service.setName(serviceFromView.getName());
                for (RevenueByYear indicatorFromView : view.getFinancialAndEconomicIndicatorsView().getRevenueIndicatorsWidget().getTreeStore().getChildren(serviceFromView)) {
                    RevenueIndicator indicator = new RevenueIndicator();
                    indicator.setId(indicatorFromView.getId());
                    indicator.setFact(indicatorFromView.getFact());
                    indicator.setPlan(indicatorFromView.getPlan());
                    indicator.setName(indicatorFromView.getName());
                    indicator.setUm(indicatorFromView.getUm());
                    service.getIndicators().add(indicator);
                    for (RevenueByYear year : view.getFinancialAndEconomicIndicatorsView().getRevenueIndicatorsWidget().getTreeStore().getChildren(indicatorFromView)) {
                        RevenueIndicatorYearValue yearValue = new RevenueIndicatorYearValue();
                        yearValue.setId(year.getId());
                        yearValue.setFact(year.getFact());
                        yearValue.setPlan(year.getPlan());
                        yearValue.setName(year.getName());
                        yearValue.setUm(year.getUm());
                        indicator.getYearValues().add(yearValue);
                    }
                }
                managedProject.getFeiRevenue().add(service);
            }
        }
    }

    private void setValueToMinimumGuaranteedAmountFromView() {
        if (managedProject != null && managedProject.getCbcMinimumGuaranteedAmount() != null) {
            managedProject.getCbcMinimumGuaranteedAmount().clear();
            for (SimpleYearIndicatorModel indicatorFromView : view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getTreeStore().getRootItems()) {
                SimpleYearIndicator indicator = new SimpleYearIndicator();
                indicator.setId(indicatorFromView.getId());
                indicator.setPlan(indicatorFromView.getPlan());
                indicator.setName(indicatorFromView.getName());
                managedProject.getCbcMinimumGuaranteedAmount().add(indicator);
            }
        }
    }

    private void setValueToCompensationMinimumGuaranteedAmountFromView() {
        if (managedProject != null && managedProject.getCbcCompensationMinimumGuaranteedAmount() != null) {
            managedProject.getCbcCompensationMinimumGuaranteedAmount().clear();
            for (SimpleYearIndicatorModel indicatorFromView : view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmount().getTreeStore().getRootItems()) {
                SimpleYearIndicator indicator = new SimpleYearIndicator();
                indicator.setId(indicatorFromView.getId());
                indicator.setPlan(indicatorFromView.getPlan());
                indicator.setName(indicatorFromView.getName());
                managedProject.getCbcCompensationMinimumGuaranteedAmount().add(indicator);
            }
        }
    }

    private void setValueToCompensationLimitNonPaymentAmountFromView() {
        if (managedProject != null && managedProject.getCbcCompensationLimitNonPaymentAmount() != null) {
            managedProject.getCbcCompensationLimitNonPaymentAmount().clear();
            for (SimpleYearIndicatorModel indicatorFromView : view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmount().getTreeStore().getRootItems()) {
                SimpleYearIndicator indicator = new SimpleYearIndicator();
                indicator.setId(indicatorFromView.getId());
                indicator.setPlan(indicatorFromView.getPlan());
                indicator.setName(indicatorFromView.getName());
                managedProject.getCbcCompensationLimitNonPaymentAmount().add(indicator);
            }
        }
    }

    private void setValueToCompensationArisingProvisionOfBenefitsAmountFromView() {
        if (managedProject != null && managedProject.getCbcCompensationArisingProvisionOfBenefitsAmount() != null) {
            managedProject.getCbcCompensationArisingProvisionOfBenefitsAmount().clear();
            for (SimpleYearIndicatorModel indicatorFromView : view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmount().getTreeStore().getRootItems()) {
                SimpleYearIndicator indicator = new SimpleYearIndicator();
                indicator.setId(indicatorFromView.getId());
                indicator.setPlan(indicatorFromView.getPlan());
                indicator.setName(indicatorFromView.getName());
                managedProject.getCbcCompensationArisingProvisionOfBenefitsAmount().add(indicator);
            }
        }
    }

    private void setValueToLimitCompensationAdditionalCostsAmountFromView() {
        if (managedProject != null && managedProject.getCbcLimitCompensationAdditionalCostsAmount() != null) {
            managedProject.getCbcLimitCompensationAdditionalCostsAmount().clear();
            for (Circumstance indicatorFromView : view.getContingentBudgetaryCommitmentsView().getLimitCompensationAdditionalCostsAmount().getTreeStore().getRootItems()) {
                CircumstanceStageIndicator indicator = new CircumstanceStageIndicator();
                indicator.setId(indicatorFromView.getId());
                indicator.setName(indicatorFromView.getName());
                indicator.setSum(indicatorFromView.getSum());
                CircumstanceStageType stageType = new CircumstanceStageType();
                stageType.setId(indicatorFromView.getGid());
                indicator.setCircumstanceType(stageType);
                managedProject.getCbcLimitCompensationAdditionalCostsAmount().add(indicator);
            }
        }
    }

    private void setValueToCircumstancesAdditionalCostsAmountFromView() {
        if (managedProject != null && managedProject.getCbcCircumstancesAdditionalCostsAmount() != null) {
            managedProject.getCbcCircumstancesAdditionalCostsAmount().clear();
            for (Circumstance mainIndicatorFromView : view.getContingentBudgetaryCommitmentsView().getCircumstancesAdditionalCostsAmount().getTreeStore().getRootItems()) {
                CircumstanceStageIndicator indicator = new CircumstanceStageIndicator();
                indicator.setId(mainIndicatorFromView.getId());
                indicator.setName(mainIndicatorFromView.getName());
                indicator.setSum(mainIndicatorFromView.getSum());
                indicator.setYears(mainIndicatorFromView.getYears());
                CircumstanceStageType stageType = new CircumstanceStageType();
                stageType.setId(mainIndicatorFromView.getGid());
                indicator.setCircumstanceType(stageType);
                for (Circumstance circumstanceFromView : view.getContingentBudgetaryCommitmentsView().getCircumstancesAdditionalCostsAmount().getTreeStore().getChildren(mainIndicatorFromView)) {
                    CircumstanceIndicator circumstanceIndicator = new CircumstanceIndicator();
                    circumstanceIndicator.setId(circumstanceFromView.getId());
                    circumstanceIndicator.setName(circumstanceFromView.getName());
                    circumstanceIndicator.setSum(circumstanceFromView.getSum());
                    circumstanceIndicator.setYears(circumstanceFromView.getYears());
                    indicator.getCircumstances().add(circumstanceIndicator);
                }
                managedProject.getCbcCircumstancesAdditionalCostsAmount().add(indicator);
            }
        }
    }

    private void setValueFromMinimumGuaranteedAmountToView() {
        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getYearBox().deselectAll();
        if (managedProject.getCbcMinimumGuaranteedAmount() != null && managedProject.getCbcMinimumGuaranteedAmount().size() > 0) {
            List<String> savedYears = managedProject.getCbcMinimumGuaranteedAmount()
                    .stream().map(SimpleYearIndicator::getName).collect(Collectors.toList());
            view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getYearBox().select(savedYears, true);
        }
        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getTreeStore().clear();
        for (SimpleYearIndicator indicator : managedProject.getCbcMinimumGuaranteedAmount()) {
            SimpleYearIndicatorModel indicatorToView = new SimpleYearIndicatorModel();
            indicatorToView.setId(indicator.getId());
            indicatorToView.setName(indicator.getName());
            indicatorToView.setPlan(indicator.getPlan());
            indicatorToView.setGid(indicator.getId());
            view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedAmount().getTreeStore().add(indicatorToView);
        }
    }

    private void setValueFromCompensationMinimumGuaranteedAmountToView() {
        view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmount().getYearBox().deselectAll();
        if (managedProject.getCbcCompensationMinimumGuaranteedAmount() != null && managedProject.getCbcCompensationMinimumGuaranteedAmount().size() > 0) {
            List<String> savedYears = managedProject.getCbcCompensationMinimumGuaranteedAmount()
                    .stream().map(SimpleYearIndicator::getName).collect(Collectors.toList());
            view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmount().getYearBox().select(savedYears, true);
        }
        view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmount().getTreeStore().clear();
        for (SimpleYearIndicator indicator : managedProject.getCbcCompensationMinimumGuaranteedAmount()) {
            SimpleYearIndicatorModel indicatorToView = new SimpleYearIndicatorModel();
            indicatorToView.setId(indicator.getId());
            indicatorToView.setName(indicator.getName());
            indicatorToView.setPlan(indicator.getPlan());
            indicatorToView.setGid(indicator.getId());
            view.getContingentBudgetaryCommitmentsView().getCompensationMinimumGuaranteedAmount().getTreeStore().add(indicatorToView);
        }
    }

    private void setValueFromCompensationLimitNonPaymentAmountToView() {
        view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmount().getYearBox().deselectAll();
        if (managedProject.getCbcCompensationLimitNonPaymentAmount() != null && managedProject.getCbcCompensationLimitNonPaymentAmount().size() > 0) {
            List<String> savedYears = managedProject.getCbcCompensationLimitNonPaymentAmount()
                    .stream().map(SimpleYearIndicator::getName).collect(Collectors.toList());
            view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmount().getYearBox().select(savedYears, true);
        }
        view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmount().getTreeStore().clear();
        for (SimpleYearIndicator indicator : managedProject.getCbcCompensationLimitNonPaymentAmount()) {
            SimpleYearIndicatorModel indicatorToView = new SimpleYearIndicatorModel();
            indicatorToView.setId(indicator.getId());
            indicatorToView.setName(indicator.getName());
            indicatorToView.setPlan(indicator.getPlan());
            indicatorToView.setGid(indicator.getId());
            view.getContingentBudgetaryCommitmentsView().getCompensationLimitNonPaymentAmount().getTreeStore().add(indicatorToView);
        }
    }

    private void setValueFromCompensationArisingProvisionOfBenefitsAmountToView() {
        view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmount().getYearBox().deselectAll();
        if (managedProject.getCbcCompensationArisingProvisionOfBenefitsAmount() != null && managedProject.getCbcCompensationArisingProvisionOfBenefitsAmount().size() > 0) {
            List<String> savedYears = managedProject.getCbcCompensationArisingProvisionOfBenefitsAmount()
                    .stream().map(SimpleYearIndicator::getName).collect(Collectors.toList());
            view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmount().getYearBox().select(savedYears, true);
        }
        view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmount().getTreeStore().clear();
        for (SimpleYearIndicator indicator : managedProject.getCbcCompensationArisingProvisionOfBenefitsAmount()) {
            SimpleYearIndicatorModel indicatorToView = new SimpleYearIndicatorModel();
            indicatorToView.setId(indicator.getId());
            indicatorToView.setName(indicator.getName());
            indicatorToView.setPlan(indicator.getPlan());
            indicatorToView.setGid(indicator.getId());
            view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsAmount().getTreeStore().add(indicatorToView);
        }
    }

    private void setValueFromLimitCompensationAdditionalCostsAmountToView() {
        if (managedProject.getCbcLimitCompensationAdditionalCostsAmount() != null && managedProject.getCbcLimitCompensationAdditionalCostsAmount().size() > 0) {
            view.getContingentBudgetaryCommitmentsView().getLimitCompensationAdditionalCostsAmount().getTreeStore().clear();
        }
        for (CircumstanceStageIndicator indicator : managedProject.getCbcLimitCompensationAdditionalCostsAmount()) {
            Circumstance indicatorToView = new Circumstance();
            indicatorToView.setId(indicator.getId());
            indicatorToView.setName(indicator.getName());
            indicatorToView.setSum(indicator.getSum());
            indicatorToView.setGid(indicator.getCircumstanceType().getId());
            view.getContingentBudgetaryCommitmentsView().getLimitCompensationAdditionalCostsAmount().getTreeStore().add(indicatorToView);
        }
    }

    private void setValueFromCircumstancesAdditionalCostsAmountToView() {
        if (managedProject.getCbcCircumstancesAdditionalCostsAmount() != null && managedProject.getCbcCircumstancesAdditionalCostsAmount().size() > 0) {
            view.getContingentBudgetaryCommitmentsView().getCircumstancesAdditionalCostsAmount().getTreeStore().clear();
        }
        Long gid = getRandId();
        for (CircumstanceStageIndicator stageIndicator : managedProject.getCbcCircumstancesAdditionalCostsAmount()) {
            Circumstance circumstanceStage = new Circumstance();
            circumstanceStage.setId(stageIndicator.getId());
            circumstanceStage.setName(stageIndicator.getName());
            circumstanceStage.setSum(stageIndicator.getSum());
            circumstanceStage.setGid(stageIndicator.getCircumstanceType().getId());
            circumstanceStage.setYears(stageIndicator.getYears());
            view.getContingentBudgetaryCommitmentsView().getCircumstancesAdditionalCostsAmount().getTreeStore().add(circumstanceStage);
            for (CircumstanceIndicator circumstanceIndicator : stageIndicator.getCircumstances()) {
                Circumstance circumstance = new Circumstance();
                circumstance.setId(circumstanceIndicator.getId());
                circumstance.setName(circumstanceIndicator.getName());
                circumstance.setSum(circumstanceIndicator.getSum());
                circumstance.setGid(gid);
                circumstance.setYears(circumstanceIndicator.getYears());
                view.getContingentBudgetaryCommitmentsView().getCircumstancesAdditionalCostsAmount().getTreeStore().add(circumstanceStage, circumstance);
                gid++;
            }
        }
    }

    /**
     * Полчить из модели выпадающего списка Long id или null, если ничего не выбрано
     *
     * @param model
     * @return
     */
    private Long getLongIdOrNull(SimpleIdNameModel model) {
        try {
            return Long.valueOf(model.getId());
        } catch (Exception ex) {
            return null;
        }
    }

    private String getStringIdOrNull(SimpleIdNameModel model) {
        if (model == null) {
            return null;
        }
        return model.getId();
    }

    private void fetchRealizationFormDictionary() {
        view.getGeneralInformationView().getRealizationForm().mask();
        RealisationFormFilters filters = new RealisationFormFilters();
        rpcService.getFilteredRealizationForms(filters, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник форм реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getRealizationForm().getStore().clear();
                view.getGeneralInformationView().getRealizationForm().getStore().addAll(response);
                view.getGeneralInformationView().getRealizationForm().unmask();
            }
        });
    }

    private void fetchFilteredInitiationMethodDictionary(Long formId) {
        view.getGeneralInformationView().getInitMethod().mask();
        Filter filter = new Filter();
        filter.setParentId(formId);
        rpcService.getFilteredInitiationMethods(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник способ инициации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getInitMethod().clear();
                view.getGeneralInformationView().getInitMethod().getStore().clear();
                view.getGeneralInformationView().getInitMethod().getStore().addAll(response);
                view.getGeneralInformationView().getInitMethod().unmask();
            }
        });
    }

    private void fetchThenSelectFilteredInitiationMethodDictionary(String formId, String selectId) {
        if (formId.isEmpty()) return;
        view.getGeneralInformationView().getInitMethod().mask();
        Filter filter = new Filter();
        filter.setParentId(Long.parseLong(formId));
        rpcService.getFilteredInitiationMethods(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник способ инициации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getInitMethod().clear();
                view.getGeneralInformationView().getInitMethod().getStore().clear();
                view.getGeneralInformationView().getInitMethod().getStore().addAll(response);
                selectComboById(view.getGeneralInformationView().getInitMethod(), selectId);
                view.getGeneralInformationView().getInitMethod().unmask();
            }
        });
    }

    private void fetchThenSelectFilteredRealizationLevelDictionary(String formId, String selectId) {
        if (formId.isEmpty()) return;
        view.getGeneralInformationView().getImplementationLvl().mask();
        Filter filter = new Filter();
        filter.setParentId(Long.parseLong(formId));
        rpcService.getFilteredRealizationLevels(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник уровень реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getImplementationLvl().clear();
                view.getGeneralInformationView().getImplementationLvl().getStore().clear();
                view.getGeneralInformationView().getImplementationLvl().getStore().addAll(response);
                selectComboById(view.getGeneralInformationView().getImplementationLvl(), selectId);
                view.getGeneralInformationView().getImplementationLvl().unmask();
            }
        });
    }

    private void fetchThenSelectFilteredRealizationSphereDictionary(String formId, String selectId, String sectorId, List<String> objectTypes) {
        if (formId.isEmpty()) return;
        view.getGeneralInformationView().getImplementationSphere().mask();
        Filter filter = new Filter();
        filter.setParentId(Long.parseLong(formId));
        rpcService.getFilteredRealizationSpheres(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить сфер реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getImplementationSphere().clear();
                view.getGeneralInformationView().getImplementationSphere().getStore().clear();
                view.getGeneralInformationView().getImplementationSphere().getStore().addAll(response);
                selectComboById(view.getGeneralInformationView().getImplementationSphere(), selectId);
                view.getGeneralInformationView().getImplementationSphere().unmask();

                fetchThenSelectedFilteredRealizationSectorDictionary(formId, selectId, sectorId, objectTypes);
            }
        });
    }

    private void fetchThenSelectFilteredAgreementGroundDictionary(String formId, String initMethodId, String selectId) {
        if (formId == null || formId.isEmpty()) return;
        if (initMethodId == null || initMethodId.isEmpty()) return;
        view.getProjectPreparationView().getGroundsOfAgreementConclusion().mask();
        FilterByInitFormAndMethod filter = new FilterByInitFormAndMethod();
        filter.setFormId(Long.parseLong(formId));
        filter.setInitMethodId(Long.parseLong(initMethodId));

        rpcService.getFilteredAgreementGrounds(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник уровень реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().clear();
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().getStore().clear();
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().getStore().addAll(response);
                selectComboById(view.getProjectPreparationView().getGroundsOfAgreementConclusion(), selectId);
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().unmask();
            }
        });
    }

    private void fetchRealizationLevelDictionary() {
        view.getGeneralInformationView().getImplementationLvl().mask();
        Filter filters = new Filter();
        if (preferences.getRealizationForm() != null) {
            view.getGeneralInformationView().getImplementationLvl().setEnabled(false);
            filters.setId(preferences.getRealizationForm());
        }

        rpcService.getFilteredRealizationLevels(filters, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник уровень реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getImplementationLvl().getStore().addAll(response);
                view.getGeneralInformationView().getImplementationLvl().unmask();
            }
        });
    }

    private void fetchFilteredRealizationLevelDictionary(Long formId) {
        view.getGeneralInformationView().getImplementationLvl().mask();
        Filter filter = new Filter();
        filter.setParentId(formId);
        rpcService.getFilteredRealizationLevels(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник уровень реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getImplementationLvl().clear();
                view.getGeneralInformationView().getImplementationLvl().getStore().clear();
                view.getGeneralInformationView().getImplementationLvl().getStore().addAll(response);
                view.getGeneralInformationView().getImplementationLvl().unmask();
            }
        });
    }

    private void fetchProjectStatusDictionary() {
        view.getCommentsView().getProjectStatus().mask();
        rpcService.getAllProjectStatuses(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник Статус проекта");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getCommentsView().getProjectStatus().getStore()
                        .addAll(response.stream().filter(status -> status.getId().equals("3") || status.getId().equals("1")).collect(Collectors.toList()));
                view.getCommentsView().getProjectStatus().unmask();
            }
        });
    }

    private void fetchRegionDictionary() {
        view.getGeneralInformationView().getRegion().mask();

        RfRegionFilters filters = new RfRegionFilters();
        rpcService.getAllRFRegions(filters, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник регионов РФ");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getRegion().getStore().addAll(response);
                view.getGeneralInformationView().getRegion().unmask();
            }
        });
    }

    private void fetchFilteredMunicipalityDictionary(String regionId) {
        view.getGeneralInformationView().getMunicipality().mask();
        RfMunicipalityFilters filters = new RfMunicipalityFilters();
        filters.setRegionId(regionId);
        rpcService.getFilteredMunicipalities(filters, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить перечень муниципальных образований");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getMunicipality().clear();
                view.getGeneralInformationView().getMunicipality().getStore().clear();
                view.getGeneralInformationView().getMunicipality().getStore().addAll(response);
                view.getGeneralInformationView().getMunicipality().unmask();
            }
        });
    }

    private void fetchThenSelectedFilteredMunicipalityDictionary(String regionId, String selectId, String ppkId) {
        view.getGeneralInformationView().getMunicipality().mask();
        RfMunicipalityFilters filters = new RfMunicipalityFilters();
        filters.setRegionId(regionId);
        rpcService.getFilteredMunicipalities(filters, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить перечень муниципальных образований");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getMunicipality().clear();
                view.getGeneralInformationView().getMunicipality().getStore().clear();
                view.getGeneralInformationView().getMunicipality().getStore().addAll(response);
                selectComboById(view.getGeneralInformationView().getMunicipality(), selectId);
                view.getGeneralInformationView().getMunicipality().unmask();

                fetchThenSelectedPpkFilterData(regionId, selectId, ppkId);
            }
        });
    }

    private void fetchPpkFilterData(String regionId, String oktmoCode) {
        view.getGeneralInformationView().getPpk().mask();
        FilterSvrOrgs filters = new FilterSvrOrgs();
        filters.setRegion(regionId);

        if (oktmoCode != null) {
            filters.setOktmo(oktmoCode);
        }

        if (preferences.isFilterByOrg()) {
            filters.getIds().addAll(preferences.getOrgIds());
        }

        rpcService.getSvrOrgs(filters, new MethodCallback<List<SvrOrg>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить перечень концендентов");
            }

            @Override
            public void onSuccess(Method method, List<SvrOrg> response) {
                view.getGeneralInformationView().getPpk().clear();
                view.getGeneralInformationView().getPpk().getStore().clear();
                view.getGeneralInformationView().getPpk().getStore().addAll(response);
                view.getGeneralInformationView().getPpk().unmask();
            }
        });
    }

    private void fetchThenSelectedPpkFilterData(String regionId, String oktmoCode, String selectId) {
        view.getGeneralInformationView().getPpk().mask();
        FilterSvrOrgs filters = new FilterSvrOrgs();
        filters.setRegion(regionId);

        if (oktmoCode != null) {
            filters.setOktmo(oktmoCode);
        }

        if (preferences.isFilterByOrg()) {
            filters.getIds().addAll(preferences.getOrgIds());
        }
        rpcService.getSvrOrgs(filters, new MethodCallback<List<SvrOrg>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить перечень концендентов");
            }

            @Override
            public void onSuccess(Method method, List<SvrOrg> response) {
                view.getGeneralInformationView().getPpk().clear();
                view.getGeneralInformationView().getPpk().getStore().clear();
                view.getGeneralInformationView().getPpk().getStore().addAll(response);

                response.stream().filter(ppk -> ppk.getId().equals(selectId)).findFirst()
                        .ifPresent(ppk -> view.getGeneralInformationView().getPpk().setValue(ppk));
                view.getGeneralInformationView().getPpk().unmask();
            }
        });
    }

    private void fetchFilteredRealizationSphereDictionary(Long formId) {
        view.getGeneralInformationView().getImplementationSphere().mask();
        Filter filter = new Filter();
        filter.setParentId(formId);
        rpcService.getFilteredRealizationSpheres(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник уровень реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getImplementationSphere().clear();
                view.getGeneralInformationView().getImplementationSphere().getStore().clear();
                view.getGeneralInformationView().getImplementationSphere().getStore().addAll(response);
                view.getGeneralInformationView().getImplementationSphere().unmask();
            }
        });
    }

    void fetchFilteredRealizationSectorDictionary(Long formId, Long sphereId) {
        FilterByInitFormAndSphere filter = new FilterByInitFormAndSphere();
        filter.setFormId(formId);
        filter.setSphereId(sphereId);

        view.getGeneralInformationView().getImplementationSector().mask();
        rpcService.getFilteredRealizationSectors(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник отрасль реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getImplementationSector().clear();
                view.getGeneralInformationView().getImplementationSector().getStore().clear();
                view.getGeneralInformationView().getImplementationSector().getStore().addAll(response);
                view.getGeneralInformationView().getImplementationSector().unmask();
            }
        });
    }

    void fetchThenSelectedFilteredRealizationSectorDictionary(String formId, String sphereId, String selectId, List<String> objectTypes) {
        FilterByInitFormAndSphere filter = new FilterByInitFormAndSphere();
        filter.setFormId(Long.valueOf(formId));
        filter.setSphereId(Long.valueOf(sphereId));

        rpcService.getFilteredRealizationSectors(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник отрасль реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getImplementationSector().getStore().clear();
                view.getGeneralInformationView().getImplementationSector().getStore().addAll(response);
                selectComboById(view.getGeneralInformationView().getImplementationSector(), selectId);
                view.getGeneralInformationView().getImplementationSector().unmask();

                fetchThenSelectedObjectKindFilteredDictionary(formId, sphereId, selectId, objectTypes);
            }
        });
    }

    void fetchFilteredAgreementGroundsDictionary(Long formId, Long initMethodId) {
        FilterByInitFormAndMethod filter = new FilterByInitFormAndMethod();
        filter.setFormId(formId);
        filter.setInitMethodId(initMethodId);

        view.getProjectPreparationView().getGroundsOfAgreementConclusion().mask();
        rpcService.getFilteredAgreementGrounds(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник отрасль реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().clear();
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().getStore().clear();
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().getStore().addAll(response);
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().unmask();
            }
        });
    }

    private void fetchObjectKindFilteredDictionary(Long formId, Long sphereId, Long sectorId) {
        view.getGeneralInformationView().getObjectType().mask();
        ObjectKindFilter filter = new ObjectKindFilter();
        filter.setFormId(formId);
        filter.setSphereId(sphereId);
        filter.setSectorId(sectorId);
        rpcService.getFilteredObjectKinds(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник типов объектов");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getObjectType().getStore().clear();
                view.getGeneralInformationView().getObjectType().getStore().addAll(response);
                view.getGeneralInformationView().getObjectType().refreshGridHeight();
                view.getGeneralInformationView().getObjectType().unmask();
            }
        });
    }

    private void fetchThenSelectedObjectKindFilteredDictionary(String formId, String sphereId, String sectorId, List<String> selectId) {
        view.getGeneralInformationView().getObjectType().mask();
        ObjectKindFilter filter = new ObjectKindFilter();
        filter.setFormId(Long.valueOf(formId));
        filter.setSphereId(Long.valueOf(sphereId));
        filter.setSectorId(Long.valueOf(sectorId));
        rpcService.getFilteredObjectKinds(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник типов объектов");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getObjectType().getStore().clear();
                view.getGeneralInformationView().getObjectType().getStore().addAll(response);
                view.getGeneralInformationView().getObjectType().refreshGridHeight();
                selectMulticomboById(view.getGeneralInformationView().getObjectType(), selectId);
                view.getGeneralInformationView().getObjectType().unmask();
            }
        });
    }

    private void fetchAgreementSubjectDictionary() {
        view.getGeneralInformationView().getAgreementSubject().mask();
        rpcService.getAllAgreementSubjects(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник предмет соглашения");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getAgreementSubject().getStore().addAll(response);
                view.getGeneralInformationView().getAgreementSubject().unmask();
            }
        });
    }

    private void fetchRealizationStatusDictionary() {
        view.getGeneralInformationView().getRealizationStatus().mask();
        rpcService.getAllRealizationStatuses(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник статусы реализации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getGeneralInformationView().getRealizationStatus().getStore().addAll(response);
                view.getGeneralInformationView().getRealizationStatus().unmask();
            }
        });
    }

    private void fetchAgreementGroundsDictionary() {
        view.getProjectPreparationView().getGroundsOfAgreementConclusion().mask();
        rpcService.getAllAgreementGrounds(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник основания заключения соглашения");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().getStore().addAll(response);
                view.getProjectPreparationView().getGroundsOfAgreementConclusion().unmask();
            }
        });
    }

    private void fetchCompetitionResultsDictionary() {
        view.getProjectPreparationView().getCompetitionResults().mask();
        rpcService.getAllCompetitionResults(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник результатов проедения конкурса");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getCompetitionResults().getStore().addAll(response);
                view.getProjectPreparationView().getCompetitionResults().unmask();
            }
        });
    }

    private void fetchCompetitionResultSignsDictionary() {
        view.getProjectPreparationView().getCompetitionResultsSignStatus().mask();
        rpcService.getAllCompetitionResultsSign(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник статусов подписания соглашения");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getCompetitionResultsSignStatus().getStore().addAll(response);
                view.getProjectPreparationView().getCompetitionResultsSignStatus().unmask();
            }
        });
    }

    private void fetchPriceOrderDictionary() {
        view.getProjectPreparationView().getContractPriceOrder().mask();
        rpcService.getAllPriceOrders(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник порядка расчета цены контракта");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getContractPriceOrder().getStore().addAll(response);
                view.getProjectPreparationView().getContractPriceOrder().unmask();
            }
        });
    }

    private void fetchContractPriceOffersDictionary() {
        view.getProjectPreparationView().getContractPriceOffer().mask();
        rpcService.getAllContractPriceOffers(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник порядка расчета цены контракта");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getContractPriceOffer().getStore().addAll(response);
                view.getProjectPreparationView().getContractPriceOffer().unmask();
            }
        });
    }

    private void fetchPrivatePartnerCostRecoveryMethodsDictionary() {
        view.getProjectPreparationView().getPublicPartnerCostRecoveryMethod().mask();
        rpcService.getAllPrivatePartnerCostRecoveryMethods(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник порядка расчета цены контракта");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getPublicPartnerCostRecoveryMethod().getStore().addAll(response);
                view.getProjectPreparationView().getPublicPartnerCostRecoveryMethod().unmask();
            }
        });
    }

    private void fetchGovSupportsDictionary() {
        view.getCreationView().getGovSupport().mask();
        rpcService.getAllGovSupports(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник государственная поддержка");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getCreationView().getGovSupport().getStore().addAll(response);
                view.getCreationView().getGovSupport().unmask();
            }
        });
    }

    private void fetchIRSourcesDictionary() {
        view.getExploitationView().getIrSource().mask();
        rpcService.getAllIRSources(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник источник возмещения");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getExploitationView().getIrSource().getStore().addAll(response);
                view.getExploitationView().getIrSource().unmask();
            }
        });
    }

    private void fetchPpResultsOfPlacingDictionary() {
        view.getProjectPreparationView().getPpResultsOfPlacing().mask();
        rpcService.getAllPpResultsOfPlacing(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник результаты размещения предложения на torgi.gov.ru");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getPpResultsOfPlacing().getStore().addAll(response);
                view.getProjectPreparationView().getPpResultsOfPlacing().unmask();
            }
        });
    }

    private void fetchIRLevelsDictionary() {
        view.getExploitationView().getIrBudgetLevel().mask();
        rpcService.getAllIRLevels(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник уровень бюджета для возмещения");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getExploitationView().getIrBudgetLevel().getStore().addAll(response);
                view.getExploitationView().getIrBudgetLevel().unmask();
            }
        });
    }

    /*////
        private void fetchExEnsureMethodsDictionary() {
            view.getExploitationView().getExecutionMethod().mask();
            rpcService.getAllEnsureMethods(new MethodCallback<List<SimpleIdNameModel>>() {
                @Override
                public void onFailure(Method method, Throwable exception) {
                    Info.display("Ошибка", "Не удалось загрузить справочник способы обеспечения");
                }

                @Override
                public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                    view.getExploitationView().getExecutionMethod().getStore().addAll(response);
                    view.getExploitationView().getExecutionMethod().unmask();
                }
            });
        }
    */
    private void fetchAgreementsSetsDictionary() {
        view.getCreationView().getAgreementComplex().mask();
        rpcService.getAllAgreementsSets(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник комплексов соглашений");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getCreationView().getAgreementComplex().getStore().addAll(response);
                view.getCreationView().getAgreementComplex().unmask();
            }
        });
    }

    private void fetchExPaymentMethodsDictionary() {
        view.getExploitationView().getPaymentMethod().mask();
        rpcService.getAllPaymentMethods(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник форма взимания платы");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getExploitationView().getPaymentMethod().getStore().addAll(response);
                view.getExploitationView().getPaymentMethod().unmask();
            }
        });
    }

    private void fetchExCostRecoveryMethodsDictionary() {
        view.getExploitationView().getCostRecoveryMethod().mask();
        rpcService.getAllCostRecoveryMethods(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник Способ возмещения расходов арендатора ");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getExploitationView().getCostRecoveryMethod().getStore().addAll(response);
                view.getExploitationView().getCostRecoveryMethod().unmask();
            }
        });
    }

    private void fetchTerminationCausesDictionary() {
        view.getTerminationView().getCause().mask();
        rpcService.getAllTerminationCauses(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник причина прекращения");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getTerminationView().getCause().getStore().addAll(response);
                view.getTerminationView().getCause().unmask();
            }
        });
    }

    private void fetchTerminationAftermathsDictionary() {
        view.getTerminationView().getAftermath().mask();
        rpcService.getAllTerminationAftermaths(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник последствия прекращения");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getTerminationView().getAftermath().getStore().addAll(response);
                view.getTerminationView().getAftermath().unmask();
            }
        });
    }

    private void fetchConditionChangeDictionary() {
        view.getConditionChangeView().getCause().mask();
        rpcService.getAllChangesReasons(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник последствия прекращения");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getConditionChangeView().getCause().getStore().addAll(response);
                view.getConditionChangeView().getCause().unmask();
            }
        });
    }

    private void fetchOpfDictionary() {
        view.getExtraView().getOpf().mask();
        view.getGeneralInformationView().getOpf().mask();
        view.getCreationView().getOpf().mask();
        rpcService.getAllOpf(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник ОПФ");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getExtraView().getOpf().getStore().addAll(response);
                view.getExtraView().getOpf().unmask();
                view.getCreationView().getOpf().getStore().addAll(response);
                view.getCreationView().getOpf().unmask();
                view.getGeneralInformationView().getOpf().getStore().addAll(response);
                view.getGeneralInformationView().getOpf().unmask();
            }
        });
    }

    private void fetchAgreementsSetDictionary() {
        view.getProjectPreparationView().getAgreementsSet().mask();
        rpcService.getAllAgreementsSet(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник Комплекс соглашений, заключаемых в рамках проекта");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getAgreementsSet().getStore().addAll(response);
                view.getProjectPreparationView().getAgreementsSet().unmask();
            }
        });
    }

    private void fetchMethodOfExecuteObligationDictionary() {
        view.getProjectPreparationView().getMethodOfExecuteObligation().mask();
        rpcService.getAllMethodOfExecuteObligation(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник Обязательства частной стороны, предусмотренные соглашением");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getMethodOfExecuteObligation().getStore().addAll(response);
                view.getProjectPreparationView().getMethodOfExecuteObligation().unmask();
            }
        });
    }

    private void fetchOtherGovSupportsDictionary() {
        view.getProjectPreparationView().getOtherGovSupports().mask();
        rpcService.getAllOtherGovSupports(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник Иные меры господдержки, предоставляемые в рамках СПИК");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getOtherGovSupports().getStore().addAll(response);
                view.getProjectPreparationView().getOtherGovSupports().unmask();
            }
        });
    }

    private void fetchWinnerContractPriceOfferDictionary() {
        view.getProjectPreparationView().getWinnerContractPriceOffer().mask();
        rpcService.getAllWinnerContractPriceOffers(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник Предложение победителя конкурса");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getProjectPreparationView().getWinnerContractPriceOffer().getStore().addAll(response);
                view.getProjectPreparationView().getWinnerContractPriceOffer().unmask();
            }
        });
    }

    private void fetchRentObjectDictionary() {
        view.getObjectDescriptionView().getRentObject().mask();
        rpcService.getAllRentObject(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник Объектом договора аренды является");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getObjectDescriptionView().getRentObject().getStore().addAll(response);
                view.getObjectDescriptionView().getRentObject().unmask();
            }
        });
    }

    private void fetchСompetitionСriterionDictionary() {
        view.getExtraView().getCompetitionCriteria().mask();
        rpcService.getAllCompetitionCriterion(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник Критерии конкурса");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getExtraView().getCompetitionCriteria().getStore().addAll(response);
                view.getExtraView().getCompetitionCriteria().unmask();
            }
        });
    }

    private void fetchBusinessModeTypeDictionary() {
        view.getExtraView().getRegimeType().mask();
        rpcService.getAllBusinessModeTypes(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник виды режимов");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getExtraView().getRegimeType().getStore().addAll(response);
                view.getExtraView().getRegimeType().unmask();
            }
        });
    }

    private void fetchFinRequirmentsDictionary() {
        view.getExtraView().getFinancialRequirement().mask();
        rpcService.getAllFinRequirments(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник финансовые требования к участнику конкурса");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getExtraView().getFinancialRequirement().getStore().addAll(response);
                view.getExtraView().getFinancialRequirement().unmask();
            }
        });
    }

    private void fetchNoFinRequirmentsDictionary() {
        view.getExtraView().getNonFinancialRequirement().mask();
        rpcService.getAllNoFinRequirments(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник нефинансовые требования к участнику конкурса");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getExtraView().getNonFinancialRequirement().getStore().addAll(response);
                view.getExtraView().getNonFinancialRequirement().unmask();
            }
        });
    }

    private void fetchTaxConditionDictionary() {
        view.getFinancialAndEconomicIndicatorsView().getTaxConditionWidget().getTaxConditionDic().mask();
        rpcService.getTaxCondition(new MethodCallback<List<TaxConditionModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник налоговых условий");
            }

            @Override
            public void onSuccess(Method method, List<TaxConditionModel> response) {
                for (TaxConditionModel condition : response) {
                    view.getFinancialAndEconomicIndicatorsView().getTaxConditionWidget().getTaxConditionDic().getStore().add(condition);
                }
                view.getExtraView().getRegimeType().unmask();
            }
        });
    }

    private void fetchMinimumGuarantIncomeTypesDictionary() {
        view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedIncomeForm().mask();
        rpcService.getMinimumGuarantIncomeTypes(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник вид минимального гарантированного дохода");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedIncomeForm().getStore().addAll(response);
                view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedIncomeForm().unmask();
            }
        });
    }

    private void fetchEnablingPayoutsDictionary() {
        view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedForm().mask();
        rpcService.getEnablingPayouts(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник включение выплаты");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedForm().getStore().addAll(response);
                view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedForm().unmask();
            }
        });
    }

    private void fetchCircumstancesDictionary(Long circumstancesStageId, MultiSelectComboBox<SimpleIdNameModel> combobox) {
        combobox.mask();
        Filter filter = new Filter();
        filter.setParentId(circumstancesStageId);
        rpcService.getFilteredCircumstances(filter, new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник обстоятельств возникновения компенсации дополнительных расходов");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                combobox.getStore().addAll(response);
                view.getContingentBudgetaryCommitmentsView().getCircumstancesAdditionalCostsAmount().setDictionaries(circumstancesStageId, response);
                combobox.unmask();
            }
        });
    }

    private void fetchCircumstancesDictionaries() {
        fetchCircumstancesDictionary(1L, view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostPrepare());
        fetchCircumstancesDictionary(2L, view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostBuild());
        fetchCircumstancesDictionary(3L, view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCostExploitation());
    }

    private void fetchRevisions() {
        if (managedProject.getId() != null) {
            view.getEditionsView().getRevisions().mask();
            rpcService.getAllRevisions(managedProject.getId(), new MethodCallback<List<ProjectDetailsRevision>>() {
                @Override
                public void onFailure(Method method, Throwable exception) {
                    Info.display("Ошибка", "Не удалось загрузить список редакций");
                }

                @Override
                public void onSuccess(Method method, List<ProjectDetailsRevision> response) {
                    view.getEditionsView().getRevisions().getStore().clear();
                    view.getEditionsView().getRevisions().getStore().addAll(response);
                    view.getEditionsView().getRevisions().unmask();
                }
            });
        }
    }

    private void fetchTmCompositionOfCompensationGrantorFaultDictionary() {
        view.getTerminationView().getTmCompositionOfCompensationGrantorFault().mask();
        rpcService.getAllTmCompositionOfCompensationGrantorFault(new MethodCallback<List<SimpleIdNameModel>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Info.display("Ошибка", "Не удалось загрузить справочник состав компенсации");
            }

            @Override
            public void onSuccess(Method method, List<SimpleIdNameModel> response) {
                view.getTerminationView().getTmCompositionOfCompensationGrantorFault().getStore().addAll(response);
                view.getTerminationView().getTmCompositionOfCompensationGrantorFault().unmask();
            }
        });
    }

    /**
     * Выбран ли в поле "Форма реализации" какой-либо id из списка аргументов
     *
     * @param list
     * @return
     */
    private boolean formIn(Integer... list) {
        if (view.getGeneralInformationView().getRealizationForm().getCurrentValue() == null) {
            return false;
        }
        for (Integer integer : list) {
            if (Integer.parseInt(view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId()) == integer) {
                return true;
            }
        }
        return false;
    }

    /**
     * Выбран ли в поле "Способ инициации проекта" какой-либо id из списка аргументов
     *
     * @param list
     * @return
     */
    private boolean initIn(Integer... list) {
        if (view.getGeneralInformationView().getInitMethod().getCurrentValue() == null) {
            return false;
        }
        for (Integer integer : list) {
            if (Integer.parseInt(view.getGeneralInformationView().getInitMethod().getCurrentValue().getId()) == integer) {
                return true;
            }
        }
        return false;
    }

    /**
     * Выбран ли в поле "Уровень реализации" какой-либо id из списка аргументов
     *
     * @param list
     * @return
     */
    private boolean realizationLevelIn(Integer... list) {
        if (view.getGeneralInformationView().getImplementationLvl().getCurrentValue() == null) {
            return false;
        }
        for (Integer integer : list) {
            if (Integer.parseInt(view.getGeneralInformationView().getImplementationLvl().getCurrentValue().getId()) == integer) {
                return true;
            }
        }
        return false;
    }

    private String createText(String viewName, String elementName) {
        return "Вкладка \"" + viewName + "\", поле \"" + elementName + "\";";
    }

    private String createFlkText(String elementName) {
        return elementName + ";";
    }

    /**
     * Проверка обязательных для заполнения полей.
     */
    private List<String> checkRequiredFields() {
        List<String> res = new ArrayList<>();

        Map<Supplier<Boolean>, String> giChecks = new HashMap<>(); // Supplier true is error

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                GWT.log("check name field: " + view.getGeneralInformationView().getProjectNameField().getValue());
                return view.getGeneralInformationView().getProjectNameField().getValue() == null;
            }
        }, createText("Общая информация", "Наименование проекта"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getGeneralInformationView().getRealizationForm().getValue() == null;
            }
        }, createText("Общая информация", "Форма реализации"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && view.getGeneralInformationView().getInitMethod().getValue() == null;
            }
        }, createText("Общая информация", "Способ инициации"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getGeneralInformationView().getImplementationLvl().getValue() == null;
            }
        }, createText("Общая информация", "Уровень реализации"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getGeneralInformationView().getRegion().getValue() == null;
            }
        }, createText("Общая информация", "Субъект РФ"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return realizationLevelIn(9) && view.getGeneralInformationView().getMunicipality().getValue() == null;
            }
        }, createText("Общая информация", "Муниципальное образование"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getGeneralInformationView().getPpk().getValue() == null;
            }
        }, createText("Общая информация", "Концедент/Публичная сторона"));

//        giChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(251, 301, 451, 501) && view.getGeneralInformationView().getInn().getValue() == null;
//            }
//        }, createText("Общая информация", "ИНН заказчика"));

//        giChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(401) && view.getGeneralInformationView().getBalanceHolder().getValue() == null;
//            }
//        }, createText("Общая информация", "Балансодержатель"));

//        giChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(601) && view.getGeneralInformationView().getPublicSharePercentage().getValue() == null;
//            }
//        }, createText("Общая информация", "Доля публичной стороны в капитале совместного юридического лица (совместного предприятия), реализующего проект, до реализации проекта, %"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getGeneralInformationView().getImplementationSphere().getValue() == null;
            }
        }, createText("Общая информация", "Сфера реализации проекта"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getGeneralInformationView().getImplementationSector().getValue() == null;
            }
        }, createText("Общая информация", "Отрасль реализации проекта"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getGeneralInformationView().getObjectType().getSelectedItems().size() == 0;
            }
        }, createText("Общая информация", "Вид объекта"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getGeneralInformationView().getObjectLocation().getValue() == null;
            }
        }, createText("Общая информация", "Место нахождения объекта"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getGeneralInformationView().getAgreementSubject().getSelectedItems().size() == 0;
            }
        }, createText("Общая информация", "Предмет соглашения"));

        giChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getGeneralInformationView().getRealizationStatus().getValue() == null;
            }
        }, createText("Общая информация", "Статус реализации проекта"));

//        giChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//
//                boolean formRFCheck = view.getGeneralInformationView().getRegion().getValue().getName().equals("Российская Федерация");
//                return formIn(101, 151) && formRFCheck && view.getGeneralInformationView().getCompletedTemplateFileUpload().getCurrentFiles().size() == 0;
//            }
//        }, createText("Общая информация", "Сведения по условным и безусловным обязательствам"));

        Map<Supplier<Boolean>, String> odChecks = new HashMap<>(); // Supplier true is error

        odChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && (view.getObjectDescriptionView().getObjectNameField().getValue() == null
                        || view.getObjectDescriptionView().getObjectNameField().getValue() == "");
            }
        }, createText("Описание объекта", "Наименование объекта/товара"));

        odChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && view.getObjectDescriptionView().getTechEcononomyIndicatorsWidget().getTreeStore().getAll().size() == 0;
            }
        }, createText("Описание объекта", "Технико-экономические показатели"));

//        odChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(401) && view.getObjectDescriptionView().getRentObject().getValue() == null;
//            }
//        }, createText("Описание объекта", "Объектом договора аренды является"));


//        odChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(251, 451, 501) && view.getObjectDescriptionView().getFileUploader().getCurrentFiles().size() == 0;
//            }
//        }, createText("Описание объекта", "Энергетический паспорт объекта"));


        Map<Supplier<Boolean>, String> ppChecks = new HashMap<>(); // Supplier true is error

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && !initIn(201) && view.getProjectPreparationView().getSupposedPrivatePartnerName().getValue() == null;
            }
        }, createText("Подготовка проекта", "Наименование юридического лица, выступившего с предложением о заключении соглашения"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                boolean formInitCheck = formIn(101, 151) && !initIn(201);
                return formInitCheck && view.getProjectPreparationView().getSupposedPrivatePartnerInn().getValue() == null;
            }
        }, createText("Подготовка проекта", "ИНН предполагаемой частной стороны / юридического лица, выступившего с частной инициативой (инвестора, отобранного по результатам конкурса)"));

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(101, 151, 201, 551, 601) && view.getProjectPreparationView().getSupposedAgreementStartDate().getValue() == null;
//            }
//        }, createText("Подготовка проекта", "Предполагаемая дата заключения соглашения (контракта)"));

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(101, 151, 201, 551, 601) && view.getProjectPreparationView().getSupposedAgreementEndDate().getValue() == null;
//            }
//        }, createText("Подготовка проекта", "Предполагаемая дата окончания соглашения (контракта)"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && view.getProjectPreparationView().getSupposedValidityYears().getValue() == null;
            }
        }, createText("Подготовка проекта", "Предполагаемый срок действия соглашения (контракта), лет"));

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(251) && view.getProjectPreparationView().getDeliveryTimeOfGoodsWorkDate().getValue() == null;
//            }
//        }, createText("Подготовка проекта", "Срок поставки товара/выполнения работ"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && !initIn(151, 201) &&
                        view.getProjectPreparationView().getGroundsOfAgreementConclusion().getValue() == null;
            }
        }, createText("Подготовка проекта", "Способ реализации проекта"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return ("".equals(view.getProjectPreparationView().getActNumber().getValue())
                        || view.getProjectPreparationView().getActNumber().getValue() == null)
                        && (view.getProjectPreparationView().getPpImplementProject().getValue()
                        && !"3".equals(getStringIdOrNull(view.getProjectPreparationView().getPpResultsOfPlacing().getValue())));
            }
        }, createText("Подготовка проекта", "Номер решения"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {

                return view.getProjectPreparationView().getActDate().getValue() == null
                        && (view.getProjectPreparationView().getPpImplementProject().getValue()
                        && !"3".equals(getStringIdOrNull(view.getProjectPreparationView().getPpResultsOfPlacing().getValue())));
            }
        }, createText("Подготовка проекта", "Дата решения"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getProjectPreparationView().getActFileUploader().getCurrentFiles().size() == 0
                        && (view.getProjectPreparationView().getPpImplementProject().getValue()
                        && !"3".equals(getStringIdOrNull(view.getProjectPreparationView().getPpResultsOfPlacing().getValue())));
            }
        }, createText("Подготовка проекта", "Текст решения"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && view.getProjectPreparationView().getCreationInvestments().isNotFilled();
            }
        }, createText("Подготовка проекта", "Планируемый объем инвестиций на стадии создания/реконструкции") + " План должн быть заполнен.");

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return view.getProjectPreparationView().getInvestments().isNotFilled();
//            }
//        }, createText("Подготовка проекта", "Планируемый объем инвестиций на стадии эксплуатации") + " План должн быть заполнен.");

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return !formIn(451, 501) && view.getProjectPreparationView().getFinancialModelFileUpload().getCurrentFiles().size() == 0;
//            }
//        }, createText("Подготовка проекта", "Планируемый объем инвестиций - Обосновывающие документы (финансовая модель)"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(251, 301) && view.getProjectPreparationView().getPublicPartnerCostRecoveryMethod().getValue() == null;
            }
        }, createText("Подготовка проекта", "Способ возмещения обязательств частного партнера"));

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(251, 301) && view.getProjectPreparationView().getAdvancePaymentAmount().getValue() == null;
//            }
//        }, createText("Подготовка проекта", "Размер авансового платежа"));

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(251) && view.getProjectPreparationView().getFirstObjectOperationDate().getValue() == null;
//            }
//        }, createText("Подготовка проекта", "Дата начала создания / реконструкции первого объекта соглашения"));

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return view.getProjectPreparationView().getLastObjectCommissioningDate().getValue() == null;
//            }
//        }, createText("Подготовка проекта", "Дата ввода последнего объекта в эксплуатацию"));

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return !formIn(251, 451, 501) && view.getProjectPreparationView().getSupportingDocumentsFileUploader().getCurrentFiles().size() == 0;
//            }
//        }, createText("Подготовка проекта", "Основание (обосновывающие документы)"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && initIn(151) && view.getProjectPreparationView().getProposalPublishDate().getValue() == null;
            }
        }, createText("Подготовка проекта", "Дата размещения предложения на torgi.gov.ru"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && initIn(151) && view.getProjectPreparationView().getProposalTextFileUploader().getCurrentFiles().size() == 0;
            }
        }, createText("Подготовка проекта", "Текст предложения"));

        ppChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return (formIn(101, 151) && initIn(201) && view.getProjectPreparationView().getTorgiGovRuUrl().getCurrentValue() == null)
                        || (formIn(101, 151) && initIn(151) && view.getProjectPreparationView().getTorgiGovRuUrl().getCurrentValue() == null
                        && "2".equals(getStringIdOrNull(view.getProjectPreparationView().getPpResultsOfPlacing().getValue())));
            }
        }, createText("Подготовка проекта", "Ссылка на torgi.gov.ru, где размещено сообщение о конкурсе"));

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(101, 151, 201) && initIn(151) && view.getProjectPreparationView().getProtocolFileUploader().getCurrentFiles().size() == 0;
//            }
//        }, createText("Подготовка проекта", "Протокол рассмотрения заявок от иных лиц"));

//        ppChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(501) && view.getProjectPreparationView().getInvestmentStageDurationDate().getValue() == null;
//            }
//        }, createText("Подготовка проекта", "Срок инвестиционной стадии"));

        if (!"3".equals(getStringIdOrNull(view.getGeneralInformationView().getRealizationStatus().getValue()))) {
            ppChecks.clear();
        }

        Map<Supplier<Boolean>, String> crChecks = new HashMap<>(); // Supplier true is error

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getCreationView().getAgreementStartDate().getValue() == null;
            }
        }, createText("Создание", "Дата заключения соглашения"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getCreationView().getAgreementEndDate().getValue() == null;
            }
        }, createText("Создание", "Дата окончания действия соглашения"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getCreationView().getAgreementValidity().getValue() == null;
            }
        }, createText("Создание", "Срок действия соглашения, лет"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getCreationView().getSeveralObjectPlanDate().getValue() == null
                        && !view.getCreationView().getIsSeveralObjects().getValue();
            }
        }, createText("Создание", "Плановая дата завершения создания/реконструкции объекта соглашения"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getCreationView().getLastSeveralObjectPlanDate().getValue() == null
                        && view.getCreationView().getIsSeveralObjects().getValue();
            }
        }, createText("Создание", "Плановая дата завершения создания/реконструкции последнего объекта соглашения"));

//        crChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(251) && view.getCreationView().getJobDoneTerm().getValue() == null;
//            }
//        }, createText("Создание", "Срок поставки товара/выполнения работ"));
//
//        crChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(451) && view.getCreationView().getSavingStartDate().getValue() == null;
//            }
//        }, createText("Создание", "Начальный срок достижения экономии"));
//
//        crChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(451) && view.getCreationView().getSavingEndDate().getValue() == null;
//            }
//        }, createText("Создание", "Конечный срок достижения экономии"));
//
//        crChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(501) && view.getCreationView().getInvestmentStageTerm().getValue() == null;
//            }
//        }, createText("Создание", "Срок инвестиционной стадии"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getCreationView().getAgreementFileUpload().getCurrentFiles().size() == 0;
            }
        }, createText("Создание", "Основание (текст соглашения)"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return "4".equals(getStringIdOrNull(view.getGeneralInformationView().getRealizationStatus().getValue())) && view.getCreationView().getConcessionaire().getValue() == null;
            }
        }, createText("Создание", "Концессионер/Частная сторона - наименование"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && view.getCreationView().getIsFormulasInvestCosts().getValue() && view.getCreationView().getCalcInvestCostsFileUpload().getCurrentFiles().size() == 0;
            }
        }, createText("Создание", "Расчет планового размера инвестиционных расходов концедента (публичного партнера)"));

//        crChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(101, 151, 201, 301, 351, 401, 4, 601) && view.getCreationView().getOpf().getValue() == null;
//            }
//        }, createText("Создание", "ОПФ"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return "4".equals(getStringIdOrNull(view.getGeneralInformationView().getRealizationStatus().getValue())) && view.getCreationView().getConcessionaireInn().getValue() == null;
            }
        }, createText("Создание", "Концессионер/Частная сторона - ИНН"));

//        crChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(101, 151, 201, 301, 351, 401, 4, 551, 601) && view.getCreationView().getFirstObjectCreationPlanDate().getValue() == null;
//            }
//        }, createText("Создание", "Дата создания первого объекта соглашения"));
//
//        crChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(101, 151, 201, 301, 351, 401, 4, 551, 601) && view.getCreationView().getLastObjectCreationPlanDate().getValue() == null;
//            }
//        }, createText("Создание", "Дата создания последнего объекта соглашения"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getCreationView().getInvestments().isNotFilled();
            }
        }, createText("Создание", "Объем инвестиций на стадии создания"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(201) && view.getCreationView().getGovSupport().getSelectedItems().size() == 0;
            }
        }, createText("Создание", "Государственная поддержка, оказываемая проекту"));

        crChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(201) && view.getCreationView().getConfirmationDocFileUpload().getCurrentFiles().size() == 0;
            }
        }, createText("Создание", "Тексты подтверждающих документов"));

        if (!"4".equals(getStringIdOrNull(view.getGeneralInformationView().getRealizationStatus().getValue()))) {
            crChecks.clear();
        }

        Map<Supplier<Boolean>, String> exChecks = new HashMap<>(); // Supplier true is error

        exChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getExploitationView().getLastObjectPlanDate().getValue() == null;
            }
        }, createText("Эксплуатация", "Дата ввода последнего объекта в эксплуатацию - план"));

//        exChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(601) && view.getExploitationView().getExPublicShareExpl().getValue() == null;
//            }
//        }, createText("Эксплуатация", "Доля публичной стороны в капитале совместного юридического лица (совместного предприятия), реализующего проект, после ввода объекта в эксплуатацию"));

        exChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && view.getExploitationView() != null
                        && view.getExploitationView().getInvestments() != null && view.getExploitationView().getInvestments().isNotFilled();
            }
        }, createText("Эксплуатация", "Объем инвестиций на этапе эксплуатации") + " Должен быть заполнен план.");

        exChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && view.getExploitationView().getExFormulasOrIndexingOrderEstablished().getValue() && view.getExploitationView().getExCalculationPlannedAmountFVIds().getCurrentFiles().size() == 0;
            }
        }, createText("Эксплуатация", "Расчет планового размера расходов концедента (публичного партнера) на стадии эксплуатации"));

        exChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return formIn(101, 151) && view.getExploitationView().getPaymentMethod().getValue() == null && view.getExploitationView().getIsConcessionPaymentProvided().getValue();
            }
        }, createText("Эксплуатация", "Форма взимания платы"));

//        exChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(401) && view.getExploitationView().getCostRecoveryMethod().getSelectedItems().size() == 0;
//            }
//        }, createText("Эксплуатация", "Способ возмещения расходов арендатора"));
//
//        exChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(401) && view.getExploitationView().getExCostRecoveryMechanism().getValue() == null;
//            }
//        }, createText("Эксплуатация", "Описание механизма возмещения расходов"));

//        exChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(151, 201) && view.getExploitationView().getExOwnPrivatePlanDate().getValue() == null;
//            }
//        }, createText("Эксплуатация", "Дата возникновения права собственности у частной стороны - план"));
//
//        exChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(201) && view.getExploitationView().getExOwnPublicPlanDate().getValue() == null;
//            }
//        }, createText("Эксплуатация", "Дата возникновения права собственности у публичной стороны - план"));

//        exChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(451) && view.getExploitationView().getExStartAchEconPlanDate().getValue() == null;
//            }
//        }, createText("Эксплуатация", "Начальный срок достижения экономии, план"));
//
//        exChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(451) && view.getExploitationView().getExEndAchEconPlanDate().getValue() == null;
//            }
//        }, createText("Эксплуатация", "Конечный срок достижения экономии, план"));

//        exChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(501) && view.getExploitationView().getExInvestStagePlanDate().getValue() == null;
//            }
//        }, createText("Эксплуатация", "Срок инвестиционной стадии"));

        if (!"14".equals(getStringIdOrNull(view.getGeneralInformationView().getRealizationStatus().getValue()))) {
            exChecks.clear();
        }

        Map<Supplier<Boolean>, String> tmChecks = new HashMap<>(); // Supplier true is error

        tmChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getTerminationView().getCause().getValue() == null;
            }
        }, createText("Прекращение", "Причина прекращения"));

        tmChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getTerminationView().getActFactDate().getValue() == null;
            }
        }, createText("Прекращение", "Дата прекращения соглашения"));

        tmChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                return view.getTerminationView().getActTextFileUpload().getCurrentFiles().size() == 0;
            }
        }, createText("Прекращение", "Текст соглашения (решения)"));

//        tmChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(101, 151, 201, 301) && view.getTerminationView().getFactDate().getValue() == null;
//            }
//        }, createText("Прекращение", "Дата прекращения права владения - факт"));
//
//        tmChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(101, 151, 201, 301) && view.getTerminationView().getTaActFileUpload().getCurrentFiles().size() == 0;
//            }
//        }, createText("Прекращение", "Основание (акт приема-передачи)"));

//        tmChecks.put(new Supplier<Boolean>() {
//            @Override
//            public Boolean get() {
//                return formIn(601) && view.getTerminationView().getPublicSideShare().getValue() == null;
//            }
//        }, createText("Прекращение", "Доля публичной стороны в капитале совместного юридического лица (совместного предприятия), реализующего проект, после реализации проекта, %"));

        if (!"2".equals(getStringIdOrNull(view.getGeneralInformationView().getRealizationStatus().getValue()))) {
            tmChecks.clear();
        }

        for (Map.Entry<Supplier<Boolean>, String> entry : giChecks.entrySet()) {
            if (entry.getKey().get()) {
                res.add(entry.getValue());
            }
        }

        for (Map.Entry<Supplier<Boolean>, String> entry : odChecks.entrySet()) {
            if (entry.getKey().get()) {
                res.add(entry.getValue());
            }
        }

        for (Map.Entry<Supplier<Boolean>, String> entry : ppChecks.entrySet()) {
            if (entry.getKey().get()) {
                res.add(entry.getValue());
            }
        }

        for (Map.Entry<Supplier<Boolean>, String> entry : exChecks.entrySet()) {
            if (entry.getKey().get()) {
                res.add(entry.getValue());
            }
        }

        for (Map.Entry<Supplier<Boolean>, String> entry : crChecks.entrySet()) {
            if (entry.getKey().get()) {
                res.add(entry.getValue());
            }
        }

        for (Map.Entry<Supplier<Boolean>, String> entry : tmChecks.entrySet()) {
            if (entry.getKey().get()) {
                res.add(entry.getValue());
            }
        }

        return res;
    }

    private List<String> checkFlk() {

        List<String> res = new ArrayList<>();

        Map<Supplier<Boolean>, String> flkChecks = new HashMap<>();

        flkChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                if (view.getGeneralInformationView().getImplementationLvl().getCurrentValue().getId().equals("1")) {
                    PlanFactYear creation = view.getCreationView().getInvestments().getInd(1L);
                    PlanFactYear financing = view.getFinancialAndEconomicIndicatorsView().getInvestmentsInObjectWidget().getInd(1L);
                    GWT.log("creation: " + creation.getPlan() + "/" + creation.getFact());
                    GWT.log("financing: " + financing.getPlan() + "/" + financing.getFact());
                    return !Objects.equals(creation.getPlan(), financing.getPlan())
                            || !Objects.equals(creation.getFact(), financing.getFact());
                } else
                    return false;
            }
        }, createFlkText("Значения «Общий объем инвестиций» раздела «Создание» и «Всего инвестиции» раздела «Финансово-экономические показатели» должны быть равны"));


        flkChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                if (!view.getCreationView().getIsSeveralObjects().getValue()) {
                    Date agreement = view.getCreationView().getAgreementStartDate().getValue(); // Дата заключения соглашения
                    Date several = view.getCreationView().getSeveralObjectPlanDate().getValue(); // Плановая дата завершения создания/реконструкции объекта соглашения
                    GWT.log("agreement: " + agreement);
                    GWT.log("several: " + several);
                    if (several == null)
                        return false;
                    if (agreement == null)
                        return true;

                    return several.before(agreement);
                }
                return false;
            }
        }, createFlkText("Не допускается превышение значения атрибута «Дата заключения соглашения» по отношению к атрибуту «Плановая дата завершения создания/реконструкции объекта соглашения» раздела «Создание»"));

        flkChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                if (view.getCreationView().getIsSeveralObjects().getValue()) {
                    Date agreement = view.getCreationView().getAgreementStartDate().getValue(); // Дата заключения соглашения
                    Date several = view.getCreationView().getFirstSeveralObjectPlanDate().getValue(); // Плановая дата завершения создания/реконструкции первого объекта соглашения
                    GWT.log("first agreement: " + agreement);
                    GWT.log("first several: " + several);
                    if (several == null)
                        return false;
                    if (agreement == null)
                        return true;

                    return several.before(agreement);
                }
                return false;
            }
        }, createFlkText("Не допускается превышение значения атрибута «Дата заключения соглашения» по отношению к атрибуту «Плановая дата завершения создания/реконструкции первого объекта соглашения» раздела «Создание»"));

        flkChecks.put(new Supplier<Boolean>() {
            @Override
            public Boolean get() {
                if (view.getCreationView().getIsSeveralObjects().getValue()) {
                    Date agreement = view.getCreationView().getAgreementStartDate().getValue(); // Дата заключения соглашения
                    Date several = view.getCreationView().getLastSeveralObjectPlanDate().getValue(); // Плановая дата завершения создания/реконструкции последнего объекта соглашения
                    GWT.log("last agreement: " + agreement);
                    GWT.log("last several: " + several);
                    if (several == null)
                        return false;
                    if (agreement == null)
                        return true;

                    return several.before(agreement);
                }
                return false;
            }
        }, createFlkText("Не допускается превышение значения атрибута «Дата заключения соглашения» по отношению к атрибуту «Плановая дата завершения создания/реконструкции последнего объекта соглашения» раздела «Создание»"));

        for (Map.Entry<Supplier<Boolean>, String> entry : flkChecks.entrySet()) {
            if (entry.getKey().get()) {
                res.add(entry.getValue());
            }
        }
        return res;
    }

    private void hideAttributesImplementationByLvlFederal() {
        if (view.getGeneralInformationView().getImplementationLvl().getCurrentValue() != null) {
            view.getCreationView().setDisplayedByLvlFederal(view.getGeneralInformationView().getImplementationLvl().getCurrentValue().getId().equals("1"));
        } else {
            if (managedProject != null && managedProject.getGiRealizationLevel() != null && managedProject.getGiRealizationLevel().getId() != null) {
                view.getCreationView().setDisplayedByLvlFederal(managedProject.getGiRealizationLevel().getId().equals("1"));
            } else {
                view.getCreationView().setDisplayedByLvlFederal(false);
            }
        }

    }

    private void setConcessionaireHeaderByRealizationForm() {
        if (view.getGeneralInformationView().getRealizationForm().getCurrentValue() != null) {
            if (view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId().equals("101") ||
                    view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId().equals("151")) {
                view.getCreationView().setConcessionaireHeader("Концессионер / Частный партнер");
            } else {
                view.getCreationView().setConcessionaireHeader("Частная сторона");
            }
        } else {
            if (managedProject != null && managedProject.getGiRealizationForm() != null) {
                if (managedProject.getGiRealizationForm().getId().equals("101") ||
                        managedProject.getGiRealizationForm().getId().equals("151")
                ) {
                    view.getCreationView().setConcessionaireHeader("Концессионер / Частный партнер");
                } else {
                    view.getCreationView().setConcessionaireHeader("Частная сторона");
                }
            } else {
                view.getCreationView().setConcessionaireHeader("Концессионер / Частный партнер");
            }
        }
    }

    private void setLastObjectDateContainerHeaderByExploitationView() {
        if (view.getGeneralInformationView().getRealizationForm().getCurrentValue() != null) {
            view.getExploitationView().setLastObjectPlanDateHeader(!view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId().equals("101") &&
                    !view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId().equals("151"));
        } else {
            if (managedProject != null && managedProject.getGiRealizationForm() != null) {
                view.getExploitationView().setLastObjectPlanDateHeader(!managedProject.getGiRealizationForm().getId().equals("101") &&
                        !managedProject.getGiRealizationForm().getId().equals("151"));
            } else {
                view.getExploitationView().setLastObjectPlanDateHeader(false);
            }
        }
    }

    private void setLastObjectActFileUploadHeaderByExploitationView() {
        if (view.getGeneralInformationView().getRealizationForm().getCurrentValue() != null) {
            view.getExploitationView().setLastObjectActFileUploadHeader(!view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId().equals("101") &&
                    !view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId().equals("151"));
        } else {
            if (managedProject != null && managedProject.getGiRealizationForm() != null) {
                view.getExploitationView().setLastObjectActFileUploadHeader(!managedProject.getGiRealizationForm().getId().equals("101") &&
                        !managedProject.getGiRealizationForm().getId().equals("151"));
            } else {
                view.getExploitationView().setLastObjectActFileUploadHeader(false);
            }
        }
    }

    private void setImplementProjectHeaderByPpView() {
        if (view.getGeneralInformationView().getRealizationForm().getCurrentValue() != null) {
            view.getProjectPreparationView().setImplementProjectLabel(!view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId().equals("101") &&
                    !view.getGeneralInformationView().getRealizationForm().getCurrentValue().getId().equals("151"));
        } else {
            if (managedProject != null && managedProject.getGiRealizationForm() != null) {
                view.getProjectPreparationView().setImplementProjectLabel(!managedProject.getGiRealizationForm().getId().equals("101") &&
                        !managedProject.getGiRealizationForm().getId().equals("151"));
            } else {
                view.getProjectPreparationView().setImplementProjectLabel(false);
            }
        }
    }

    private void addHandlerToDueToOnsetOfCertainCircumstancesExistInContingentBudgetaryCommitmentsView() {
        if (view.getGeneralInformationView().getImplementationLvl().getCurrentValue() != null) {
            if (view.getGeneralInformationView().getImplementationLvl().getCurrentValue().getId().equals("1")) {
                view.getContingentBudgetaryCommitmentsView().addNameOfCircumstanceAdditionalCosContainerHandler(view.getGeneralInformationView().getImplementationLvl().getCurrentValue().getId());
                if (view.getContingentBudgetaryCommitmentsView().getDueToOnsetOfCertainCircumstancesExist().getValue()) {
                    view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCosContainer().show();
                }
            } else {
                view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCosContainer().hide();
            }
        } else {
            if (managedProject.getGiRealizationLevel() != null && managedProject.getGiRealizationLevel().getId().equals("1")) {
                view.getContingentBudgetaryCommitmentsView().addNameOfCircumstanceAdditionalCosContainerHandler(managedProject.getGiRealizationLevel().getId());
                if (view.getContingentBudgetaryCommitmentsView().getDueToOnsetOfCertainCircumstancesExist().getValue()) {
                    view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCosContainer().show();
                }
            } else {
                view.getContingentBudgetaryCommitmentsView().getNameOfCircumstanceAdditionalCosContainer().hide();
            }
        }
    }

    public GchpNewDocumentPanelPresenter(RestService rpcService, HandlerManager eventBus, Display view, RolePreferences preferences) {
        this.rpcService = (GchpApi) rpcService;
        this.eventBus = eventBus;
        this.view = view;
        this.preferences = preferences;
    }

    public Double replace(String str) {
        if (str != null) {
            GWT.log(String.valueOf(str.length()));
            if (str.length() > 0) {
                if (str.replaceAll("[^\\d.]", "").equals("")) {
                    return 0D;
                }
                return Double.parseDouble(str.replaceAll("[^\\d]", "").substring(0, str.length() - 2));
            }
        }
        return 0D;
    }

    // тут собирается строка из полей, в которых превышено максимальное допустимое количество символов
    // сами валидаторы установлены во view
    public String validateForm() {
        String message = validation(view.getGeneralInformationView().getProjectNameField(), 5000) +
                validation(view.getGeneralInformationView().getObjectLocation(), 5000) +
                validation(view.getObjectDescriptionView().getObjectNameField(), 5000) +
                validation(view.getObjectDescriptionView().getObjectCharacteristic(), 5000) +
                validation(view.getProjectPreparationView().getSupposedPrivatePartnerName(), 255) +
                validation(view.getProjectPreparationView().getSupposedPrivatePartnerInn(), 255) +
                validation(view.getProjectPreparationView().getActNumber(), 255) +
                validation(view.getProjectPreparationView().getTorgiGovRuUrl(), 1000) +
                validation(view.getProjectPreparationView().getAnotherProjectsIdentifier(), 255) +
                validation(view.getProjectPreparationView().getContractPriceFormula(), 255) +
                validation(view.getProjectPreparationView().getPpConcludeAgreementLink(), 255) +
                validation(view.getCreationView().getInvestCostsGrantor(), 255) +
                validation(view.getCreationView().getExpectedRepaymentYear(), 255) +
                validation(view.getExploitationView().getExGrantorExpenses(), 255) +
                validation(view.getTerminationView().getTmAnotherDescription(), 255) +
                validation(view.getTerminationView().getTmClausesOfAgreement(), 255) +
                validation(view.getContingentBudgetaryCommitmentsView().getMinimumGuaranteedClauseAgreement(), 255) +
                validation(view.getContingentBudgetaryCommitmentsView().getLimitNonPaymentConsumersGoodsProvidedClauseAgreement(), 255) +
                validation(view.getContingentBudgetaryCommitmentsView().getNonPaymentConsumersGoodsProvidedClauseAgreement(), 255) +
                validation(view.getContingentBudgetaryCommitmentsView().getCompensationArisingProvisionOfBenefitsClauseAgreement(), 255) +
                validation(view.getContingentBudgetaryCommitmentsView().getLimitCompensationAdditionalClauseAgreement(), 255) +
                validation(view.getContingentBudgetaryCommitmentsView().getCbcArisingProvisionOfBenefitsClauseAgreement(), 255) +
                validation(view.getContingentBudgetaryCommitmentsView().getSpecifyOtherCircumstancesPrepare(), 4000) +
                validation(view.getContingentBudgetaryCommitmentsView().getSpecifyOtherCircumstancesBuild(), 4000) +
                validation(view.getContingentBudgetaryCommitmentsView().getSpecifyOtherCircumstancesExploitation(), 4000) +
                validation(view.getConditionChangeView().getActNumber(), 255) +
                validation(view.getExtraView().getBenefitDescription(), 255) +
                validation(view.getExtraView().getCreditRating(), 255) +
                validation(view.getExtraView().getCreditRatingAgency(), 255) +
                validation(view.getExtraView().getUnforeseenExpencesComment(), 255) +
                validation(view.getCommentsView().getComment(), 5000) +
                validation(view.getCommentsView().getContacts(), 5000);
        return message.substring(0, message.length() - 2);
    }
}
