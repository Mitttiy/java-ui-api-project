package ru.ibs.gasu.gchp.ws;

import ru.ibs.gasu.dictionaries.domain.*;
import ru.ibs.gasu.dictionaries.entities.MeasureType;
import ru.ibs.gasu.gchp.entities.*;
import ru.ibs.gasu.gchp.domain.UmSearchCriteria;

import javax.jws.WebService;
import java.util.List;

@WebService(name = "GchpDictionariesWebService")
public interface GchpDictionariesWebService {
    List<RealizationForm> getAllRealizationForms();

    List<RealizationForm> getAllFilteredRealizationForms(RealisationFormFilters formFilters);

    List<InitiationMethod> getAllInitiationMethods();

    List<InitiationMethod> getFilteredInitiationMethods(Filter filter);

    List<RealizationLevel> getAllRealizationLevels();

    List<RealizationLevel> getFilteredRealizationLevels(Filter filter);

    List<DicGasuSp1> getAllRFRegions();

    List<DicGasuSp1> getAllFilteredRFRegions(RFRegionFilters filters);

    List<Municipality> getFilteredMunicipality(RFMunicipalityFilters filter);

    List<RealizationSphereEntity> getAllRealizationSpheres();

    List<RealizationSphereEntity> getFilteredRealizationSpheres(Filter filter);

    List<RealizationSectorEntity> getAllRealizationSectors();

    List<RealizationSectorEntity> getFilteredRealizationSectors(FilterByInitFormAndSphere filter);

    List<ObjectKind> getAllObjectKinds();

    List<ObjectKind> getFilteredObjectKinds(ObjectKindFilter filter);

    List<AgreementSubject> getAllAgreementSubjects();

    List<ProjectStatus> getAllProjectStatuses();

    List<RealizationStatus> getAllRealizationStatuses();

    List<RentObject> getAllRentObjects();

    List<AgreementGrounds> getAllAgreementGrounds();

    List<AgreementGrounds> getFilteredAgreementGrounds(FilterByInitFormAndMethod filter);

    List<CompetitionResults> getAllCompetitionResults();

    List<CompetitionResultsSign> getAllCompetitionResultSigns();

    List<PrivatePartnerCostRecoveryMethodEntity> getAllPrivatePartnerCostRecoveryMethods();

    List<CostRecoveryMethodEntity> getAllCostRecoveryMethods();

    List<MethodOfExecuteObligationEntity> getAllMethodOfExecuteObligations();

    List<OtherGovSupportsEntity> getAllOtherGovSupports();

    List<AgreementsSetEntity> getAllAgreementsSets();

    List<ContractPriceOffer> getAllContractPriceOffers();

    List<WinnerContractPriceOfferEntity> getAllWinnerContractPriceOffers();

    List<OpfEntity> getAllOpfs();

    List<EnsureMethod> getAllEnsureMethods();

    List<GovSupport> getAllGovSupports();

    List<TmCompositionOfCompensationGrantorFaultEntity> getAllTmCompositionOfCompensationGrantorFault();

    List<IRSourceEntity> getAllIRSources();

    List<PpResultOfPlacingEntity> getAllPpResultsOfPlacing();

    List<IRLevelEntity> getAllIRLevels();

    List<PaymentMethodEntity> getAllPaymentMethods();

    List<TerminationCause> getAllTerminationCauses();

    List<TerminationAftermathEntity> getAllTerminationAftermaths();

    List<ChangesReasonEntity> getAllChangesReasons();

    List<ChangesReasonEntity> getFilteredChangesReasons(Filter filter);

    List<PriceOrderEntity> getAllPriceOrders();

    List<FinIndDimType> getAllFinIndDimTypes();

    List<BusinessModeType> getAllBusinessModeTypes();

    List<CompetitionCriterion> getAllCompetitionCriterions();

    List<FinRequirement> getAllFinRequirements();

    List<NoFinRequirement> getAllNonFinRequirements();

    List<PrivateServiceType> getAllPrivateServiceTypes();

    List<PublicServiceType> getAllPublicServiceTypes();

    List<MeasureType> getAllMeasureTypes();

    UmResult getFilteredOkei(UmSearchCriteria umSearchCriteria);

    EgrulDomainResult getSimpleEgrulDomains(EgrulCriteria criteria);

    EgrulDomain getEgrulDomain(String inn);

    List<TaxCondition> getAllTaxConditions();

    List<MinimumGuarantIncomeType> getAllMinimumGuarantIncomeType();

    List<EnablingPayout> getAllEnablingPayouts();

    List<Circumstance> getAllCircumstances(Filter filter);

}
