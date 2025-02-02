package ru.ibs.gasu.dictionaries.service;

import ru.ibs.gasu.dictionaries.domain.*;
import ru.ibs.gasu.gchp.entities.*;
import ru.ibs.gasu.gchp.domain.UmSearchCriteria;

import java.util.List;

public interface DictionaryDataService {
    String RF_ID = "8";

    List<RealizationForm> getAllRealizationForms();

    List<RealizationForm> getAllFilteredRealizationForms(RealisationFormFilters formFilters);

    List<InitiationMethod> getAllInitiationMethods();

    List<InitiationMethod> getFilteredInitiationMethods(Filter filter);

    List<RealizationLevel> getAllRealizationLevels();

    List<RealizationLevel> getFilteredRealizationLevels(Filter filter);

    List<DicGasuSp1> getAllRFRegions();

    List<DicGasuSp1> getAllFilteredRFRegions(RFRegionFilters filters);

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

    Municipality findMunicipalityById(Object id);

    List<CostRecoveryMethodEntity> getAllCostRecoveryMethods();

    List<MethodOfExecuteObligationEntity> getAllMethodOfExecuteObligations();

    List<OtherGovSupportsEntity> getAllOtherGovSupports();

    List<AgreementsSetEntity> getAllAgreementsSets();

    List<ContractPriceOffer> getAllContractPriceOffers();

    List<WinnerContractPriceOfferEntity> getAllWinnerContractPriceOffers();

    List<OpfEntity> getAllOpfs();

    List<EnsureMethod> getAllEnsureMethods();

    List<GovSupport> getAllGovSupports();

    List<IRSourceEntity> getAllIRSources();

    List<PpResultOfPlacingEntity> getAllPpResultsOfPlacing();

    List<TmCompositionOfCompensationGrantorFaultEntity> getAllTmCompositionOfCompensationGrantorFault();

    List<IRLevelEntity> getAllIRLevels();

    List<PaymentMethodEntity> getAllPaymentMethods();

    List<TerminationCause> getAllTerminationCauses();

    List<TerminationAftermathEntity> getAllTerminationAftermaths();

    List<ChangesReasonEntity> getAllChangesReasons();

    List<ChangesReasonEntity> getFilteredChangesReasons(Filter filter);

    List<Municipality> getFilteredMunicipality(RFMunicipalityFilters filters);

//    List<PublicPartnerEntity> getFilteredPublicPartners();

    List<PriceOrderEntity> getAllPriceOrderMethods();

    List<FinIndDimType> getAllFinIndDimTypeMethods();

    List<BusinessModeType> getAllBusinessModeTypeMethods();

    List<CompetitionCriterion> getAllCompetitionCriterionMethods();

    List<FinRequirement> getAllFinRequirementMethods();

    List<NoFinRequirement> getAllNonFinRequirementMethods();

    List<PrivateServiceType> getAllPrivateServiceTypes();

    List<PublicServiceType> getAllPublicServiceTypes();

    UmResult getFilteredOkei(UmSearchCriteria umSearchCriteria);

    List<PublicServiceType> getAllOkei();

    EgrulDomainResult getSimpleEgrulDomains(EgrulCriteria criteria);

    EgrulDomain getEgrulDomain(String inn);

    List<TaxCondition> getAllTaxConditions();

    List<MinimumGuarantIncomeType> getAllMinimumGuarantIncomeType();

    List<EnablingPayout> getAllEnablingPayouts();

    List<Circumstance> getFilteredCircumstances(Filter filter);

}
