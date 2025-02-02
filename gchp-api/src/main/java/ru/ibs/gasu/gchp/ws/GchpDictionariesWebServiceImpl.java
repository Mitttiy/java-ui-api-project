package ru.ibs.gasu.gchp.ws;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ibs.gasu.dictionaries.domain.*;
import ru.ibs.gasu.dictionaries.entities.MeasureType;
import ru.ibs.gasu.dictionaries.repository.MeasureTypeRepo;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.entities.*;
import ru.ibs.gasu.gchp.domain.UmSearchCriteria;

import javax.jws.WebService;
import java.util.List;

@WebService
public class GchpDictionariesWebServiceImpl implements GchpDictionariesWebService {

    @Autowired
    private MeasureTypeRepo measureTypeRepo;

    @Autowired
    private DictionaryDataService dictionaryDataService;

    @Override
    public List<RealizationForm> getAllRealizationForms() {
        return dictionaryDataService.getAllRealizationForms();
    }

    @Override
    public List<RealizationForm> getAllFilteredRealizationForms(RealisationFormFilters formFilters) {
        return dictionaryDataService.getAllFilteredRealizationForms(formFilters);
    }

    @Override
    public List<InitiationMethod> getAllInitiationMethods() {
        return dictionaryDataService.getAllInitiationMethods();
    }

    @Override
    public List<InitiationMethod> getFilteredInitiationMethods(Filter filter) {
        return dictionaryDataService.getFilteredInitiationMethods(filter);
    }

    @Override
    public List<RealizationLevel> getAllRealizationLevels() {
        return dictionaryDataService.getAllRealizationLevels();
    }

    @Override
    public List<RealizationLevel> getFilteredRealizationLevels(Filter filter) {
        return dictionaryDataService.getFilteredRealizationLevels(filter);
    }

    @Override
    public List<DicGasuSp1> getAllRFRegions() {
        return dictionaryDataService.getAllRFRegions();
    }

    @Override
    public List<DicGasuSp1> getAllFilteredRFRegions(RFRegionFilters filters) {
        return dictionaryDataService.getAllFilteredRFRegions(filters);
    }

    @Override
    public List<RealizationSphereEntity> getAllRealizationSpheres() {
        return dictionaryDataService.getAllRealizationSpheres();
    }

    @Override
    public List<RealizationSphereEntity> getFilteredRealizationSpheres(Filter filter) {
        return dictionaryDataService.getFilteredRealizationSpheres(filter);
    }

    @Override
    public List<RealizationSectorEntity> getAllRealizationSectors() {
        return dictionaryDataService.getAllRealizationSectors();
    }

    @Override
    public List<RealizationSectorEntity> getFilteredRealizationSectors(FilterByInitFormAndSphere filter) {
        return dictionaryDataService.getFilteredRealizationSectors(filter);
    }

    @Override
    public List<ObjectKind> getAllObjectKinds() {
        return dictionaryDataService.getAllObjectKinds();
    }


    @Override
    public List<ObjectKind> getFilteredObjectKinds(ObjectKindFilter filter) {
        return dictionaryDataService.getFilteredObjectKinds(filter);
    }

    @Override
    public List<AgreementSubject> getAllAgreementSubjects() {
        return dictionaryDataService.getAllAgreementSubjects();
    }

    @Override
    public List<ProjectStatus> getAllProjectStatuses() {
        return dictionaryDataService.getAllProjectStatuses();
    }

    @Override
    public List<RealizationStatus> getAllRealizationStatuses() {
        return dictionaryDataService.getAllRealizationStatuses();
    }

    @Override
    public List<RentObject> getAllRentObjects() {
        return dictionaryDataService.getAllRentObjects();
    }

    @Override
    public List<AgreementGrounds> getAllAgreementGrounds() {
        return dictionaryDataService.getAllAgreementGrounds();
    }

    public List<AgreementGrounds> getFilteredAgreementGrounds(FilterByInitFormAndMethod filter) {
        return dictionaryDataService.getFilteredAgreementGrounds(filter);
    }

    @Override
    public List<CompetitionResults> getAllCompetitionResults() {
        return dictionaryDataService.getAllCompetitionResults();
    }

    @Override
    public List<CompetitionResultsSign> getAllCompetitionResultSigns() {
        return dictionaryDataService.getAllCompetitionResultSigns();
    }

    @Override
    public List<PrivatePartnerCostRecoveryMethodEntity> getAllPrivatePartnerCostRecoveryMethods() {
        return dictionaryDataService.getAllPrivatePartnerCostRecoveryMethods();
    }

    @Override
    public List<CostRecoveryMethodEntity> getAllCostRecoveryMethods() {
        return dictionaryDataService.getAllCostRecoveryMethods();
    }

    @Override
    public List<MethodOfExecuteObligationEntity> getAllMethodOfExecuteObligations() {
        return dictionaryDataService.getAllMethodOfExecuteObligations();
    }

    @Override
    public List<OtherGovSupportsEntity> getAllOtherGovSupports() {
        return dictionaryDataService.getAllOtherGovSupports();
    }

    @Override
    public List<AgreementsSetEntity> getAllAgreementsSets() {
        return dictionaryDataService.getAllAgreementsSets();
    }

    @Override
    public List<ContractPriceOffer> getAllContractPriceOffers() {
        return dictionaryDataService.getAllContractPriceOffers();
    }

    @Override
    public List<WinnerContractPriceOfferEntity> getAllWinnerContractPriceOffers() {
        return dictionaryDataService.getAllWinnerContractPriceOffers();
    }

    @Override
    public List<OpfEntity> getAllOpfs() {
        return dictionaryDataService.getAllOpfs();
    }

    @Override
    public List<EnsureMethod> getAllEnsureMethods() {
        return dictionaryDataService.getAllEnsureMethods();
    }

    @Override
    public List<GovSupport> getAllGovSupports() {
        return dictionaryDataService.getAllGovSupports();
    }

    @Override
    public List<IRSourceEntity> getAllIRSources() {
        return dictionaryDataService.getAllIRSources();
    }

    @Override
    public List<PpResultOfPlacingEntity> getAllPpResultsOfPlacing() {
        return dictionaryDataService.getAllPpResultsOfPlacing();
    }

    @Override
    public List<TmCompositionOfCompensationGrantorFaultEntity> getAllTmCompositionOfCompensationGrantorFault() {
        return dictionaryDataService.getAllTmCompositionOfCompensationGrantorFault();
    }

    @Override
    public List<IRLevelEntity> getAllIRLevels() {
        return dictionaryDataService.getAllIRLevels();
    }

    @Override
    public List<PaymentMethodEntity> getAllPaymentMethods() {
        return dictionaryDataService.getAllPaymentMethods();
    }

    @Override
    public List<TerminationCause> getAllTerminationCauses() {
        return dictionaryDataService.getAllTerminationCauses();
    }

    @Override
    public List<TerminationAftermathEntity> getAllTerminationAftermaths() {
        return dictionaryDataService.getAllTerminationAftermaths();
    }

    @Override
    public List<ChangesReasonEntity> getAllChangesReasons() {
        return dictionaryDataService.getAllChangesReasons();
    }

    @Override
    public List<ChangesReasonEntity> getFilteredChangesReasons(Filter filter) {
        return dictionaryDataService.getFilteredChangesReasons(filter);
    }

    @Override
    public List<Municipality> getFilteredMunicipality(RFMunicipalityFilters filters) {
        return dictionaryDataService.getFilteredMunicipality(filters);
    }

    @Override
    public List<PriceOrderEntity> getAllPriceOrders() {
        return dictionaryDataService.getAllPriceOrderMethods();
    }

    @Override
    public List<FinIndDimType> getAllFinIndDimTypes() {
        return dictionaryDataService.getAllFinIndDimTypeMethods();
    }

    @Override
    public List<BusinessModeType> getAllBusinessModeTypes() {
        return dictionaryDataService.getAllBusinessModeTypeMethods();
    }

    @Override
    public List<CompetitionCriterion> getAllCompetitionCriterions() {
        return dictionaryDataService.getAllCompetitionCriterionMethods();
    }

    @Override
    public List<FinRequirement> getAllFinRequirements() {
        return dictionaryDataService.getAllFinRequirementMethods();
    }

    @Override
    public List<NoFinRequirement> getAllNonFinRequirements() {
        return dictionaryDataService.getAllNonFinRequirementMethods();
    }

    @Override
    public List<PrivateServiceType> getAllPrivateServiceTypes() {
        return dictionaryDataService.getAllPrivateServiceTypes();
    }

    @Override
    public List<PublicServiceType> getAllPublicServiceTypes() {
        return dictionaryDataService.getAllPublicServiceTypes();
    }

    public List<MeasureType> getAllMeasureTypes() {
        return measureTypeRepo.findAll();
    }

    public UmResult getFilteredOkei(UmSearchCriteria umSearchCriteria) {
        return dictionaryDataService.getFilteredOkei(umSearchCriteria);
    }

    @Override
    public EgrulDomainResult getSimpleEgrulDomains(EgrulCriteria criteria) {
        return dictionaryDataService.getSimpleEgrulDomains(criteria);
    }

    @Override
    public EgrulDomain getEgrulDomain(String inn) {
        return dictionaryDataService.getEgrulDomain(inn);
    }

    @Override
    public List<TaxCondition> getAllTaxConditions() {
        return dictionaryDataService.getAllTaxConditions();
    }

    @Override
    public List<MinimumGuarantIncomeType> getAllMinimumGuarantIncomeType() {
        return dictionaryDataService.getAllMinimumGuarantIncomeType();
    }

    @Override
    public List<EnablingPayout> getAllEnablingPayouts() {
        return dictionaryDataService.getAllEnablingPayouts();
    }

    @Override
    public List<Circumstance> getAllCircumstances(Filter filter) {
        return dictionaryDataService.getFilteredCircumstances(filter);
    }
}
