package ru.ibs.gasu.gchp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.ibs.gasu.dictionaries.Converter;
import ru.ibs.gasu.dictionaries.DictCache;
import ru.ibs.gasu.dictionaries.Dictionary;
import ru.ibs.gasu.dictionaries.domain.*;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.domain.ProjectDetailDTO;
import ru.ibs.gasu.gchp.entities.*;
import ru.ibs.gasu.gchp.entities.files.*;
import ru.ibs.gasu.gchp.service.role.SvrOrgService;
import ru.ibs.gasu.gchp.ws.GerbWebService;
import ru.ibs.gasu.soap.generated.dictionary.DictionaryDataRecordDescriptor;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.ibs.gasu.dictionaries.Converter.*;
import static ru.ibs.gasu.dictionaries.Dictionary.DIC_GASU_OKEI;
import static ru.ibs.gasu.gchp.util.Utils.opt;

@Component
public class ProjectMapper {

    @Resource
    private DictCache dictCache;

    @Autowired
    private SvrOrgService svrOrgService;

    @Autowired
    private DictionaryDataService dictionaryDataService;

    @Autowired
    private GerbWebService gerbWebService;

    public ProjectDetailDTO transform(Project source) {
        if (source == null) {
            return null;
        }

        ProjectDetailDTO projectDetailDTO = new ProjectDetailDTO();

        projectDetailDTO.setGerbName(gerbWebService.findGerbById(source.getGiRegion()).getRegionIcon());
        projectDetailDTO.setId(source.getId());
        projectDetailDTO.setGiName(source.getGiName());
        projectDetailDTO.setGiRealizationForm(dictToRealizationForm(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_FORM, source.getGiRealizationForm())));
        projectDetailDTO.setGiInitiationMethod(dictToInitiationMethod(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_IMPL_METHOD, source.getGiInitiationMethod())));
        projectDetailDTO.setGiRealizationLevel(dictToRealizationLevel(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_LVL, source.getGiRealizationLevel())));
        projectDetailDTO.setGiIsRFPartOfAgreement(source.getGiIsRFPartOfAgreement());
        projectDetailDTO.setGiIsRegionPartOfAgreement(source.getGiIsRegionPartOfAgreement());
        projectDetailDTO.setGiIsMunicipalityPartOfAgreement(source.getGiIsMunicipalityPartOfAgreement());
        projectDetailDTO.setGiRegion(dictToDicGasuSp1(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_SP1, source.getGiRegion())));

        if (source.getGiMunicipality() != null) {
            RFMunicipalityFilters rfMunicipalityFilters = new RFMunicipalityFilters();
            rfMunicipalityFilters.setRegionId(source.getGiRegion());
            rfMunicipalityFilters.setOktmo(source.getGiMunicipality());
            List<Municipality> municipality = dictionaryDataService.getFilteredMunicipality(rfMunicipalityFilters);
            projectDetailDTO.setGiMunicipality(municipality.isEmpty() ? null : municipality.get(0));
        }

        projectDetailDTO.setGiPublicPartner(svrOrgService.getSvrOrgById(source.getGiPublicPartner()));

        projectDetailDTO.setGiPublicPartnerOgrn(source.getGiPublicPartnerOgrn());
        projectDetailDTO.setGiInn(source.getGiInn());
        projectDetailDTO.setGiBalanceHolder(source.getGiBalanceHolder());
        projectDetailDTO.setGiImplementer(source.getGiImplementer());
        projectDetailDTO.setGiImplementerInn(source.getGiImplementerInn());
        projectDetailDTO.setGiOPF(dictToOpfEntity(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_OPF, source.getGiOPF())));
        projectDetailDTO.setGiIsForeignInvestor(source.getGiIsForeignInvestor());
        projectDetailDTO.setGiIsMcpSubject(source.getGiIsMcpSubject());
        projectDetailDTO.setGiIsSpecialProjectCompany(source.getGiIsSpecialProjectCompany());
        projectDetailDTO.setGiHasInvestmentProperty(source.getGiHasInvestmentProperty());
        projectDetailDTO.setGiPublicSharePercentage(source.getGiPublicSharePercentage());
        projectDetailDTO.setGiIsRFHasShare(source.getGiIsRFHasShare());
        projectDetailDTO.setGiRealizationSphere(dictToRealizationSphereEntity(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_SPHERE, source.getGiRealizationSphere())));
        projectDetailDTO.setGiRealizationSector(dictToRealizationSectorEntity(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_SECTOR, source.getGiRealizationSector())));
        List<Long> list = source.getGiObjectType();
        if (list != null) {
            projectDetailDTO.setGiObjectType(dictListToObjectKinds(dictCache.getDictDataRecordsFromCache(Dictionary.DIC_GASU_GCHP_OBJ_KIND, list)));
        }
        projectDetailDTO.setGiObjectLocation(source.getGiObjectLocation());
        List<Long> list1 = source.getGiAgreementSubject();
        if (list1 != null) {
            projectDetailDTO.setGiAgreementSubject(dictListToAgreementSubjects(dictCache.getDictDataRecordsFromCache(Dictionary.DIC_GASU_GCHP_AGRMT_SUBJ, list1)));
        }
        projectDetailDTO.setGiRealizationStatus(dictToRealizationStatus(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_IMPL_STATUS, source.getGiRealizationStatus())));


        List<CompletedTemplateFile> list228 = source.getGiCompletedTemplateTextFVId();
        if (list228 != null) {
            projectDetailDTO.setGiCompletedTemplateTextFVId(new ArrayList<CompletedTemplateFile>(list228));
        }


        projectDetailDTO.setGiProjectStatus(dictToProjectStatus(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_PROJECT_STATUS, source.getGiProjectStatus())));
        projectDetailDTO.setGiAlwaysDraftStatus(source.getGiAlwaysDraftStatus());
        projectDetailDTO.setPpInvestmentPlanningAmount(source.getPpInvestmentPlanningAmount());
        projectDetailDTO.setPpCreationInvestmentPlanningAmount(source.getPpCreationInvestmentPlanningAmount());
        projectDetailDTO.setCbcInvestments1(source.getCbcInvestments1());
        projectDetailDTO.setCbcInvestments2(source.getCbcInvestments2());
        projectDetailDTO.setCbcInvestments3(source.getCbcInvestments3());
        projectDetailDTO.setCbcInvestments4(source.getCbcInvestments4());
        projectDetailDTO.setRemainingDebt(source.getRemainingDebt());
        projectDetailDTO.setOdObjectName(source.getOdObjectName());
        projectDetailDTO.setOdObjectDescription(source.getOdObjectDescription());
        projectDetailDTO.setOdIsPropertyStaysWithPrivateSide(source.getOdIsPropertyStaysWithPrivateSide());
        projectDetailDTO.setOdIsNewPropertyBeGivenToPrivateSide(source.getOdIsNewPropertyBeGivenToPrivateSide());
        projectDetailDTO.setOdIsObjectImprovementsGiveAway(source.getOdIsObjectImprovementsGiveAway());
        projectDetailDTO.setOdRentObject(source.getOdRentObject());
        List<RentPassportFilesEntity> list2 = source.getOdRentPassportFileVersionId();
        if (list2 != null) {
            projectDetailDTO.setOdRentPassportFileVersionId(new ArrayList<RentPassportFilesEntity>(list2));
        }
        List<TechEconomicsObjectIndicator> list3 = source.getOdTechEconomicsObjectIndicators();
        if (list3 != null) {
            projectDetailDTO.setOdTechEconomicsObjectIndicators(new ArrayList<TechEconomicsObjectIndicator>(list3));
        }

        Collection<DictionaryDataRecordDescriptor> descriptorsOkei = dictCache.getDictFromCache(DIC_GASU_OKEI);
        Map<Long, UnitOfMeasure> umMap = descriptorsOkei.stream().map(Converter::dictToUnitOfMeasureType).collect(Collectors.toMap(UnitOfMeasure::getId, Function.identity()));
        for(TechEconomicsObjectIndicator object : projectDetailDTO.getOdTechEconomicsObjectIndicators()) {
            for (TechEconomicsIndicator indicator : object.getTechEconomicsIndicators()) {
                Long umId = indicator.getUmId();
                if (umId != null) {
                    UnitOfMeasure um = new UnitOfMeasure();
                    um.setId(umId);
                    um.setName(umMap.get(umId).getName());
                    um.setOkei(umMap.get(umId).getOkei());
                    indicator.setUm(um);
                }
            }
        }

        List<EnergyEfficiencyPlan> list4 = source.getOdEnergyEfficiencyPlans();
        if (list4 != null) {
            projectDetailDTO.setOdEnergyEfficiencyPlans(new ArrayList<EnergyEfficiencyPlan>(list4));
        }
        projectDetailDTO.setPpIsObjInListWithConcessionalAgreements(source.getPpIsObjInListWithConcessionalAgreements());
        projectDetailDTO.setPpObjectsListUrl(source.getPpObjectsListUrl());
        projectDetailDTO.setPpSupposedPrivatePartnerName(source.getPpSupposedPrivatePartnerName());
        projectDetailDTO.setPpSupposedPrivatePartnerInn(source.getPpSupposedPrivatePartnerInn());
        projectDetailDTO.setPpIsForeignInvestor(source.getPpIsForeignInvestor());
        projectDetailDTO.setPpIsMspSubject(source.getPpIsMspSubject());
        projectDetailDTO.setPpAgreementsSet(dictToAgreementsSetEntity(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_AGREEMENT_SETS, source.getPpAgreementsSet())));
        projectDetailDTO.setPpSupposedAgreementStartDate(source.getPpSupposedAgreementStartDate());
        projectDetailDTO.setPpSupposedAgreementEndDate(source.getPpSupposedAgreementEndDate());
        projectDetailDTO.setPpSupposedValidityYears(source.getPpSupposedValidityYears());
        projectDetailDTO.setPpDeliveryTimeOfGoodsWorkDate(source.getPpDeliveryTimeOfGoodsWorkDate());
        List<ProjectAgreementFile> list5 = source.getPpProjectAgreementFileVersionId();
        if (list5 != null) {
            projectDetailDTO.setPpProjectAgreementFileVersionId(new ArrayList<ProjectAgreementFile>(list5));
        }
        projectDetailDTO.setPpGroundsOfAgreementConclusion(source.getPpGroundsOfAgreementConclusion());
        projectDetailDTO.setPpActNumber(source.getPpActNumber());
        projectDetailDTO.setPpActDate(source.getPpActDate());
        List<ActTextFile> list6 = source.getPpActTextFileVersionId();
        if (list6 != null) {
            projectDetailDTO.setPpActTextFileVersionId(new ArrayList<ActTextFile>(list6));
        }

        List<LeaseAgreementTextFile> list226 = source.getPpLeaseAgreementTextFileVersionId();
        if (list226 != null) {
            projectDetailDTO.setPpLeaseAgreementTextFileVersionId(new ArrayList<LeaseAgreementTextFile>(list226));
        }

        projectDetailDTO.setPpProjectAssignedStatus(source.getPpProjectAssignedStatus());
        projectDetailDTO.setPpDecisionNumber(source.getPpDecisionNumber());
        projectDetailDTO.setPpDecisionDate(source.getPpDecisionDate());
        List<DecisionTextFile> list116 = source.getPpDecisionTextFileVersionId();
        if (list116 != null) {
            projectDetailDTO.setPpDecisionTextFileVersionId(new ArrayList<DecisionTextFile>(list116));
        }
        projectDetailDTO.setPpProposalPublishDate(source.getPpProposalPublishDate());
        List<ProposalTextFile> list7 = source.getPpProposalTextFileVersionId();
        if (list7 != null) {
            projectDetailDTO.setPpProposalTextFileVersionId(new ArrayList<ProposalTextFile>(list7));
        }
        projectDetailDTO.setPpTorgiGovRuUrl(source.getPpTorgiGovRuUrl());
        List<ProtocolFile> list8 = source.getPpProtocolFileVersionId();
        if (list8 != null) {
            projectDetailDTO.setPpProtocolFileVersionId(new ArrayList<ProtocolFile>(list8));
        }
        projectDetailDTO.setPpIsReadinessRequestReceived(source.getPpIsReadinessRequestReceived());
        projectDetailDTO.setPpIsDecisionMadeToConcludeAnAgreement(source.getPpIsDecisionMadeToConcludeAnAgreement());
        projectDetailDTO.setPpConcludeAgreementActNum(source.getPpConcludeAgreementActNum());
        projectDetailDTO.setPpConcludeAgreementActDate(source.getPpConcludeAgreementActDate());
        List<ConcludeAgreementFile> list9 = source.getPpConcludeAgreementFvId();
        if (list9 != null) {
            projectDetailDTO.setPpConcludeAgreementFvId(new ArrayList<ConcludeAgreementFile>(list9));
        }
        projectDetailDTO.setPpInvestmentStageDurationDate(source.getPpInvestmentStageDurationDate());
        projectDetailDTO.setPpConcludeAgreementIsSigned(source.getPpConcludeAgreementIsSigned());
        projectDetailDTO.setPpConcludeAgreementIsJoint(source.getPpConcludeAgreementIsJoint());
        projectDetailDTO.setPpConcludeAgreementOtherPjInfo(source.getPpConcludeAgreementOtherPjInfo());
        projectDetailDTO.setPpConcludeAgreementOtherPjIdent(source.getPpConcludeAgreementOtherPjIdent());
        projectDetailDTO.setPpCompetitionBidCollEndPlanDate(source.getPpCompetitionBidCollEndPlanDate());
        projectDetailDTO.setPpCompetitionBidCollEndFactDate(source.getPpCompetitionBidCollEndFactDate());
        projectDetailDTO.setPpCompetitionTenderOfferEndPlanDate(source.getPpCompetitionTenderOfferEndPlanDate());
        projectDetailDTO.setPpCompetitionTenderOfferEndFactDate(source.getPpCompetitionTenderOfferEndFactDate());
        projectDetailDTO.setPpCompetitionResultsPlanDate(source.getPpCompetitionResultsPlanDate());
        projectDetailDTO.setPpCompetitionResultsFactDate(source.getPpCompetitionResultsFactDate());
        List<CompetitionText> list10 = source.getPpCompetitionTextFVId();
        if (list10 != null) {
            projectDetailDTO.setPpCompetitionTextFVId(new ArrayList<CompetitionText>(list10));
        }
        projectDetailDTO.setPpCompetitionIsElAuction(source.getPpCompetitionIsElAuction());
        projectDetailDTO.setPpCompetitionHasResults(source.getPpCompetitionHasResults());
        projectDetailDTO.setPpCompetitionResults(source.getPpCompetitionResults());
        projectDetailDTO.setPpCompetitionResultsProtocolNum(source.getPpCompetitionResultsProtocolNum());
        projectDetailDTO.setPpCompetitionResultsProtocolDate(source.getPpCompetitionResultsProtocolDate());
        projectDetailDTO.setPpCompetitionResultsParticipantsNum(source.getPpCompetitionResultsParticipantsNum());
        List<CompetitionResultsProtocolFile> list11 = source.getPpCompetitionResultsProtocolTextFvId();
        if (list11 != null) {
            projectDetailDTO.setPpCompetitionResultsProtocolTextFvId(new ArrayList<CompetitionResultsProtocolFile>(list11));
        }
        List<CompetitionResultsFile> list12 = source.getPpCompetitionResultsDocFvId();
        if (list12 != null) {
            projectDetailDTO.setPpCompetitionResultsDocFvId(new ArrayList<CompetitionResultsFile>(list12));
        }
        projectDetailDTO.setPpCompetitionResultsSignStatus(source.getPpCompetitionResultsSignStatus());
        projectDetailDTO.setPpContractPriceOrder(source.getPpContractPriceOrder());
        projectDetailDTO.setPpContractPriceFormula(source.getPpContractPriceFormula());
        projectDetailDTO.setPpContractPricePrice(source.getPpContractPricePrice());
        projectDetailDTO.setPpNdsCheck(source.getPpNdsCheck());
        projectDetailDTO.setPpMeasureType(source.getPpMeasureType());
        projectDetailDTO.setPpDateField(source.getPpDateField());
//        projectDetailDTO.setPpContractPriceMethod(source.getPpContractPriceMethod());
        projectDetailDTO.setPpContractPriceOffer(source.getPpContractPriceOffer());
        projectDetailDTO.setPpContractPriceOfferValue(source.getPpContractPriceOfferValue());
        projectDetailDTO.setPpWinnerContractPriceOffer(dictToWinnerContractPriceOfferEntity(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_WINNER_CONTRACT_PRICE_OFFER, source.getPpWinnerContractPriceOffer())));
        projectDetailDTO.setPpContractPriceSavingStartDate(source.getPpContractPriceSavingStartDate());
        projectDetailDTO.setPpContractPriceSavingEndDate(source.getPpContractPriceSavingEndDate());
        List<FinancialModelFile> list13 = source.getPpFinancialModelFVId();
        if (list13 != null) {
            projectDetailDTO.setPpFinancialModelFVId(new ArrayList<FinancialModelFile>(list13));
        }
        projectDetailDTO.setPpPrivatePartnerCostRecoveryMethod(source.getPpPrivatePartnerCostRecoveryMethod());
        projectDetailDTO.setPpAdvancePaymentAmount(source.getPpAdvancePaymentAmount());
        projectDetailDTO.setPpFirstObjectOperationDate(source.getPpFirstObjectOperationDate());
        projectDetailDTO.setPpLastObjectCommissioningDate(source.getPpLastObjectCommissioningDate());
        List<SupportingDocumentsFile> list14 = source.getPpSupportingDocumentsFileVersionIds();
        if (list14 != null) {
            projectDetailDTO.setPpSupportingDocumentsFileVersionIds(new ArrayList<SupportingDocumentsFile>(list14));
        }
        projectDetailDTO.setPpBudgetExpendituresAgreementOnGchpMchp(source.getPpBudgetExpendituresAgreementOnGchpMchp());
        projectDetailDTO.setPpBudgetExpendituresGovContract(source.getPpBudgetExpendituresGovContract());
        projectDetailDTO.setPpObligationsInCaseOfRisksAgreementOnGchpMchp(source.getPpObligationsInCaseOfRisksAgreementOnGchpMchp());
        projectDetailDTO.setPpObligationsInCaseOfRisksGovContract(source.getPpObligationsInCaseOfRisksGovContract());

        projectDetailDTO.setPpIndicatorAssessmentComparativeAdvantage(source.getPpIndicatorAssessmentComparativeAdvantage());
        List<ConclusionUOTextFile> list114 = source.getPpConclusionUOTextFileVersionId();
        if (list114 != null) {
            projectDetailDTO.setPpConclusionUOTextFileVersionId(new ArrayList<ConclusionUOTextFile>(list114));
        }
        List<FinancialModelTextFile> list115 = source.getPpFinancialModelTextFileVersionId();
        if (list114 != null) {
            projectDetailDTO.setPpFinancialModelTextFileVersionId(new ArrayList<FinancialModelTextFile>(list115));
        }
        projectDetailDTO.setPpMethodOfExecuteObligation(dictToMethodOfExecuteObligationEntity(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_METHOD_OF_EXECUTE_OBLIGATION, source.getPpMethodOfExecuteObligation())));
        projectDetailDTO.setPpLinkToClauseAgreement(source.getPpLinkToClauseAgreement());
        projectDetailDTO.setPpIsPrivateLiabilityProvided(source.getPpIsPrivateLiabilityProvided());
        projectDetailDTO.setPpLinkToClauseAgreementLiabilityProvided(source.getPpLinkToClauseAgreementLiabilityProvided());
        projectDetailDTO.setPpStateSupportMeasuresSPIC(dictToOtherGovSupportsEntity(dictCache.getDictDataRecordFromCache(Dictionary.DIC_GASU_GCHP_OTHER_GOV_SUPPORTS, source.getPpStateSupportMeasuresSPIC())));
        projectDetailDTO.setPpConcludeAgreementLink(source.getPpConcludeAgreementLink());
        projectDetailDTO.setPpImplementProject(source.getPpImplementProject());
        projectDetailDTO.setPpResultsOfPlacing(source.getPpResultsOfPlacing());

        projectDetailDTO.setCrAgreementStartDate(source.getCrAgreementStartDate());
        projectDetailDTO.setCrAgreementEndDate(source.getCrAgreementEndDate());
        projectDetailDTO.setCrAgreementValidity(source.getCrAgreementValidity());
        List<AgreementTextFile> list15 = source.getCrAgreementTextFvId();
        if (list15 != null) {
            projectDetailDTO.setCrAgreementTextFvId(new ArrayList<AgreementTextFile>(list15));
        }
        projectDetailDTO.setCrConcessionaire(source.getCrConcessionaire());
        projectDetailDTO.setCrConcessionaireInn(source.getCrConcessionaireInn());
        projectDetailDTO.setCrFinancialClosingProvided(source.getCrFinancialClosingProvided());
        projectDetailDTO.setCrFinancialClosingDate(source.getCrFinancialClosingDate());
        projectDetailDTO.setCrFinancialClosingValue(source.getCrFinancialClosingValue());
        List<FinancialClosingActFile> list16 = source.getCrFinancialClosingActFVId();
        if (list16 != null) {
            projectDetailDTO.setCrFinancialClosingActFVId(new ArrayList<FinancialClosingActFile>(list16));
        }
        projectDetailDTO.setCrFinancialClosingIsMutualAgreement(source.getCrFinancialClosingIsMutualAgreement());
        projectDetailDTO.setCrFirstObjectCreationPlanDate(source.getCrFirstObjectCreationPlanDate());
        projectDetailDTO.setCrFirstObjectCreationFactDate(source.getCrFirstObjectCreationFactDate());
        List<FirstObjectCompleteActFile> list17 = source.getCrFirstObjectCompleteActFVId();
        if (list17 != null) {
            projectDetailDTO.setCrFirstObjectCompleteActFVId(new ArrayList<FirstObjectCompleteActFile>(list17));
        }

        projectDetailDTO.setCrIsRegionPartyAgreement(source.getCrIsRegionPartyAgreement());
        projectDetailDTO.setCrIsSeveralObjects(source.getCrIsSeveralObjects());
        projectDetailDTO.setCrFirstSeveralObjectPlanDate(source.getCrFirstSeveralObjectPlanDate());

        projectDetailDTO.setCrIsFirstSeveralObject(source.getCrIsFirstSeveralObject());
        projectDetailDTO.setCrLastSeveralObjectPlanDate(source.getCrLastSeveralObjectPlanDate());

        projectDetailDTO.setCrIsLastSeveralObject(source.getCrIsLastSeveralObject());
        projectDetailDTO.setCrSeveralObjectPlanDate(source.getCrSeveralObjectPlanDate());

        projectDetailDTO.setCrIsSeveralObjectInOperation(source.getCrIsSeveralObjectInOperation());

        projectDetailDTO.setCrInvestCostsGrantor(source.getCrInvestCostsGrantor());

        projectDetailDTO.setCrIsFormulasInvestCosts(source.getCrIsFormulasInvestCosts());

        List<CalcInvestCostsActFile> list2222 = source.getCrCalcInvestCostsActFVId();
        if (list2222 != null) {
            projectDetailDTO.setCrCalcInvestCostsActFVId(new ArrayList<CalcInvestCostsActFile>(list2222));
        }

        projectDetailDTO.setCrActualCostsValue(source.getCrActualCostsValue());

        projectDetailDTO.setCrAverageInterestRateValue(source.getCrAverageInterestRateValue());

        projectDetailDTO.setCrLastObjectCreationPlanDate(source.getCrLastObjectCreationPlanDate());
        projectDetailDTO.setCrLastObjectCreationFactDate(source.getCrLastObjectCreationFactDate());
        List<LastObjectCompleteActFile> list18 = source.getCrLastObjectCompleteActFVId();
        if (list18 != null) {
            projectDetailDTO.setCrLastObjectCompleteActFVId(new ArrayList<LastObjectCompleteActFile>(list18));
        }
        projectDetailDTO.setCrInvestmentCreationAmount(source.getCrInvestmentCreationAmount());

        List<InvestmentVolumeStagOfCreationActFile> list128 = source.getCrInvestmentVolumeStagOfCreationActFVId();
        if (list128 != null) {
            projectDetailDTO.setCrInvestmentVolumeStagOfCreationActFVId(new ArrayList<InvestmentVolumeStagOfCreationActFile>(list128));
        }

        projectDetailDTO.setCrExpectedRepaymentYear(source.getCrExpectedRepaymentYear());
        List<BalanceOfDebt> list5367 = source.getCrBalanceOfDebt();
        if (list5367 != null) {
            projectDetailDTO.setCrBalanceOfDebt(new ArrayList<BalanceOfDebt>(list5367));
        }
        projectDetailDTO.setCrIsObjectTransferProvided(source.getCrIsObjectTransferProvided());
        projectDetailDTO.setCrObjectRightsPlanDate(source.getCrObjectRightsPlanDate());
        projectDetailDTO.setCrObjectRightsFactDate(source.getCrObjectRightsFactDate());
        List<InvestmentsActFile> list19 = source.getCrActFVId();
        if (list19 != null) {
            projectDetailDTO.setCrActFVId(new ArrayList<InvestmentsActFile>(list19));
        }
        projectDetailDTO.setCrObjectValue(source.getCrObjectValue());
        List<InvestmentsLinkFile> list20 = source.getCrReferenceFVId();
        if (list20 != null) {
            projectDetailDTO.setCrReferenceFVId(new ArrayList<InvestmentsLinkFile>(list20));
        }
        projectDetailDTO.setCrIsRenewableBankGuarantee(source.getCrIsRenewableBankGuarantee());
        projectDetailDTO.setCrIsGuaranteeVariesByYear(source.getCrIsGuaranteeVariesByYear());
        projectDetailDTO.setCrBankGuaranteeByYears(source.getCrBankGuaranteeByYears());

        projectDetailDTO.setCrLandProvided(source.getCrLandProvided());
        projectDetailDTO.setCrLandIsConcessionaireOwner(source.getCrLandIsConcessionaireOwner());
        projectDetailDTO.setCrLandActStartPlanDate(source.getCrLandActStartPlanDate());
        projectDetailDTO.setCrLandActStartFactDate(source.getCrLandActStartFactDate());
        projectDetailDTO.setCrLandActEndPlanDate(source.getCrLandActEndPlanDate());
        projectDetailDTO.setCrLandActEndFactDate(source.getCrLandActEndFactDate());
        List<LandActFile> list21 = source.getCrLandActFVId();
        if (list21 != null) {
            projectDetailDTO.setCrLandActFVId(new ArrayList<LandActFile>(list21));
        }
        projectDetailDTO.setCrIsObligationExecuteOnCreationStage(source.getCrIsObligationExecuteOnCreationStage());

        List<CreationEnsureMethod> list41 = source.getCrEnsureMethods();
        if (list41 != null) {
            projectDetailDTO.setCrEnsureMethods(new ArrayList<>(list41));
        }

        List<MethodOfExecuteObligationEntity> collect = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_ENSURE_METHD)
                .stream().map(Converter::dictToMethodOfExecuteObligationEntity).collect(Collectors.toList());
        for (CreationEnsureMethod method : projectDetailDTO.getCrEnsureMethods()) {
            Long id = method.getEnsureMethodId();
            MethodOfExecuteObligationEntity e = collect.stream().filter(i -> i.getId().equals(id)).findFirst().orElse(null);
            if (e != null)
                method.setEnsureMethodName(e.getName());
        }


        List<Long> list22 = source.getCrGovSupport();
        if (list22 != null) {
            projectDetailDTO.setCrGovSupport(dictListToGovSupports(dictCache.getDictDataRecordsFromCache(Dictionary.DIC_GASU_GCHP_GOV_SUPPORT, list22)));
        }
        List<ConfirmationDocFile> list23 = source.getCrConfirmationDocFVId();
        if (list23 != null) {
            projectDetailDTO.setCrConfirmationDocFVId(new ArrayList<ConfirmationDocFile>(list23));
        }
        projectDetailDTO.setExLastObjectPlanDate(source.getExLastObjectPlanDate());
        projectDetailDTO.setExLastObjectFactDate(source.getExLastObjectFactDate());
        List<LastObjectActFile> list24 = source.getExLastObjectActFVId();
        if (list24 != null) {
            projectDetailDTO.setExLastObjectActFVId(new ArrayList<LastObjectActFile>(list24));
        }
        projectDetailDTO.setExInvestmentExploitationAmount(source.getExInvestmentExploitationAmount());

        List<InvestmentVolumeStagOfExploitationActFile> list241 = source.getExInvestmentVolumeStagOfExploitationActFVId();
        if (list241 != null) {
            projectDetailDTO.setExInvestmentVolumeStagOfExploitationActFVId(new ArrayList<InvestmentVolumeStagOfExploitationActFile>(list241));
        }

        projectDetailDTO.setExIsInvestmentsRecoveryProvided(source.getExIsInvestmentsRecoveryProvided());
        projectDetailDTO.setExInvestmentExploitationRecoveryAmount(source.getExInvestmentExploitationRecoveryAmount());
        List<InvestmentRecoveryFinancialModelFile> list25 = source.getExInvestmentRecoveryFinancialModelFileVersionId();
        if (list25 != null) {
            projectDetailDTO.setExInvestmentRecoveryFinancialModelFileVersionId(new ArrayList<InvestmentRecoveryFinancialModelFile>(list25));
        }
        projectDetailDTO.setExIRSource(source.getExIRSource());
        projectDetailDTO.setExIRLevel(source.getExIRLevel());
        projectDetailDTO.setExIsObligationExecutingOnOperationPhase(source.getExIsObligationExecutingOnOperationPhase());
        ////projectDetailDTO.setExMethodOfExecOfPublicPartnerObligation(source.getExMethodOfExecOfPublicPartnerObligation());
        ////projectDetailDTO.setExInvestmentRecoveryTerm(source.getExInvestmentRecoveryTerm());
        ////projectDetailDTO.setExInvestmentRecoveryValue(source.getExInvestmentRecoveryValue());
        List<ExploitationEnsureMethod> exEnsureMethods = source.getExEnsureMethods();
        if (exEnsureMethods != null) {
            projectDetailDTO.setExEnsureMethods(new ArrayList<>(exEnsureMethods));
        }

        projectDetailDTO.setExIsRenewableBankGuarantee(source.getExIsRenewableBankGuarantee());
        projectDetailDTO.setExIsGuaranteeVariesByYear(source.getExIsGuaranteeVariesByYear());


        projectDetailDTO.setExBankGuaranteeByYears(source.getExBankGuaranteeByYears());

        List<MethodOfExecuteObligationEntity> methodOfExecuteObligations = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_ENSURE_METHD)
                .stream().map(Converter::dictToMethodOfExecuteObligationEntity).collect(Collectors.toList());
        for (ExploitationEnsureMethod method : projectDetailDTO.getExEnsureMethods()) {
            Long id = method.getEnsureMethodId();
            methodOfExecuteObligations.stream().filter(i -> i.getId().equals(id)).findFirst().ifPresent(e -> method.setEnsureMethodName(e.getName()));
        }

        projectDetailDTO.setExIsConcessionPayProvideded(source.getExIsConcessionPayProvideded());
        projectDetailDTO.setExPaymentForm(source.getExPaymentForm());

        projectDetailDTO.setExPublicShareExpl(source.getExPublicShareExpl());
        projectDetailDTO.setExHasPublicShareExpl(source.getExHasPublicShareExpl());
        List<ExploitationFinModelFile> list60 = source.getExFinModelFVIds();
        if (list60 != null) {
            projectDetailDTO.setExFinModelFVIds(new ArrayList<ExploitationFinModelFile>(list60));
        }
        List<Long> costRecoveryMethodList = source.getExCostRecoveryMethod();
        if (costRecoveryMethodList != null) {
            projectDetailDTO.setExCostRecoveryMethod(dictListToCostRecoveryMethods(dictCache.getDictDataRecordsFromCache(Dictionary.DIC_GASU_GCHP_AGRMT_SUBJ, costRecoveryMethodList)));
        }
        projectDetailDTO.setExCostRecoveryMechanism(source.getExCostRecoveryMechanism());
        List<ExploitationSupportDoclFile> list61 = source.getExSupportDocFVIds();
        if (list61 != null) {
            projectDetailDTO.setExSupportDocFVIds(new ArrayList<ExploitationSupportDoclFile>(list61));
        }

        List<ExploitationSupportCompensDoclFile> list62 = source.getExSupportCompensDocFVIds();
        if (list62 != null) {
            projectDetailDTO.setExSupportCompensDocFVIds(new ArrayList<>(list62));
        }

        projectDetailDTO.setExOwnPrivatePlanDate(source.getExOwnPrivatePlanDate());
        projectDetailDTO.setExOwnPrivateFactDate(source.getExOwnPrivateFactDate());
        projectDetailDTO.setExOwnPublicPlanDate(source.getExOwnPublicPlanDate());
        projectDetailDTO.setExOwnPublicFactDate(source.getExOwnPublicFactDate());
        List<ExploitationAgreementFile> list50 = source.getExAgreementFVIds();
        if (list50 != null) {
            projectDetailDTO.setExAgreementFVIds(new ArrayList<ExploitationAgreementFile>(list50));
        }
        List<ExploitationAcceptActFile> list51 = source.getExAcceptActFVIds();
        if (list51 != null) {
            projectDetailDTO.setExAcceptActFVIds(new ArrayList<ExploitationAcceptActFile>(list51));
        }
        projectDetailDTO.setExStartAchEconPlanDate(source.getExStartAchEconPlanDate());
        projectDetailDTO.setExEndAchEconPlanDate(source.getExEndAchEconPlanDate());
        projectDetailDTO.setExStartAchEconFactDate(source.getExStartAchEconFactDate());
        projectDetailDTO.setExEndAchEconFactDate(source.getExEndAchEconFactDate());
        List<ExploitationAcceptActAAMFile> list52 = source.getExAcceptActAAMFVIds();
        if (list52 != null) {
            projectDetailDTO.setExAcceptActAAMFVIds(new ArrayList<ExploitationAcceptActAAMFile>(list52));
        }
        List<ExCalculationPlannedAmountFile> list5211 = source.getExCalculationPlannedAmountFVIds();
        if (list52 != null) {
            projectDetailDTO.setExCalculationPlannedAmountFVIds(new ArrayList<>(list5211));
        }
        projectDetailDTO.setExInvestStagePlanDate(source.getExInvestStagePlanDate());
        projectDetailDTO.setExInvestStageFactDate(source.getExInvestStageFactDate());
        projectDetailDTO.setExGrantorExpenses(source.getExGrantorExpenses());
        projectDetailDTO.setExFormulasOrIndexingOrderEstablished(source.getExFormulasOrIndexingOrderEstablished());

        projectDetailDTO.setTmCause(source.getTmCause());
        projectDetailDTO.setTmActPlanDate(source.getTmActPlanDate());
        projectDetailDTO.setTmActFactDate(source.getTmActFactDate());
        projectDetailDTO.setTmActNumber(source.getTmActNumber());
        projectDetailDTO.setTmActDate(source.getTmActDate());
        List<TerminationActFile> list26 = source.getTmTextFileVersionId();
        if (list26 != null) {
            projectDetailDTO.setTmTextFileVersionId(new ArrayList<TerminationActFile>(list26));
        }
        projectDetailDTO.setTmCauseDescription(source.getTmCauseDescription());
        projectDetailDTO.setTmPlanDate(source.getTmPlanDate());
        projectDetailDTO.setTmFactDate(source.getTmFactDate());
        List<TerminationActTextFile> list27 = source.getTmTaActTextFileVersionId();
        if (list27 != null) {
            projectDetailDTO.setTmTaActTextFileVersionId(new ArrayList<TerminationActTextFile>(list27));
        }
        projectDetailDTO.setTmPropertyJointProvided(source.getTmPropertyJointProvided());
        projectDetailDTO.setTmPropertyJointPrivatePercent(source.getTmPropertyJointPrivatePercent());
        projectDetailDTO.setTmPropertyJointPublicPercent(source.getTmPropertyJointPublicPercent());

        projectDetailDTO.setTmIsCompensationPayed(source.getTmIsCompensationPayed());
        projectDetailDTO.setTmCompensationValue(source.getTmCompensationValue());
        List<TerminationCompensationFile> list40 = source.getTmCompensationFVIds();
        if (list40 != null) {
            projectDetailDTO.setTmCompensationFVIds(new ArrayList<TerminationCompensationFile>(list40));
        }

        projectDetailDTO.setTmAftermath(source.getTmAftermath());
        projectDetailDTO.setTmContractNumber(source.getTmContractNumber());
        projectDetailDTO.setTmContractDate(source.getTmContractDate());
        projectDetailDTO.setTmProjectId(source.getTmProjectId());
        projectDetailDTO.setTmPublicShare(source.getTmPublicShare());
        projectDetailDTO.setTmIsRfHasShare(source.getTmIsRfHasShare());
        projectDetailDTO.setTmAnotherDescription(source.getTmAnotherDescription());
        projectDetailDTO.setTmClausesOfAgreement(source.getTmClausesOfAgreement());
        projectDetailDTO.setTmCompensationLimit(source.getTmCompensationLimit());
        projectDetailDTO.setTmAgreementTerminated(source.getTmAgreementTerminated());
        projectDetailDTO.setTmCompensationSum(source.getTmCompensationSum());
        projectDetailDTO.setTmNdsCheck(source.getTmNdsCheck());
        projectDetailDTO.setTmMeasureType(source.getTmMeasureType());
        projectDetailDTO.setTmDateField(source.getTmDateField());
        projectDetailDTO.setTmCompositionOfCompensation(source.getTmCompositionOfCompensation());

        List<CompositionOfCompensation> compositonOfCompensationList = source.getTmCompositionOfCompensationView();
        if (compositonOfCompensationList != null) {
            projectDetailDTO.setTmCompositionOfCompensationView(new ArrayList<CompositionOfCompensation>(compositonOfCompensationList));
        }

        List<TerminationSupportingDocumentsFile> list56 = source.getTmSupportingDocuments();
        if (list56 != null) {
            projectDetailDTO.setTmSupportingDocuments(new ArrayList<TerminationSupportingDocumentsFile>(list56));
        }

        List<Long> list95 = source.getTmCompositionOfCompensationGrantorFault();
        List<DictionaryDataRecordDescriptor> dictDataRecordsFromCache5 = dictCache.getDictDataRecordsFromCache(Dictionary.DIC_GASU_GCHP_NO_FIN_REQUIREMENT, list95);
        if (list95 != null) {
            projectDetailDTO.setTmCompositionOfCompensationGrantorFault(dictDataRecordsFromCache5.stream().map(Converter::dictToTmCompositionOfCompensationGrantorFaultEntity).collect(Collectors.toList()));
        }

        projectDetailDTO.setCcIsChangesMade(source.getCcIsChangesMade());
        projectDetailDTO.setCcReason(source.getCcReason());
        projectDetailDTO.setCcActNumber(source.getCcActNumber());
        projectDetailDTO.setCcActDate(source.getCcActDate());
        List<ChangeTextFile> list28 = source.getCcTextFileVersionId();
        if (list28 != null) {
            projectDetailDTO.setCcTextFileVersionId(new ArrayList<ChangeTextFile>(list28));
        }
        projectDetailDTO.setExPayment(source.getExPayment());
        projectDetailDTO.setExCompensation(source.getExCompensation());
        // На клиенте подписи быть не должно. только с клиента
        // projectDetailDTO.setCert(source.getCert());
        // projectDetailDTO.setSignature(source.getSignature());
        projectDetailDTO.setSnils(source.getSnils());
        List<Event> list29 = source.getAdsEvents();
        if (list29 != null) {
            projectDetailDTO.setAdsEvents(new ArrayList<Event>(list29));
        }
        projectDetailDTO.setAdsIsThirdPartyOrgsProvided(source.getAdsIsThirdPartyOrgsProvided());
        projectDetailDTO.setAdsIsRegInvestmentProject(source.getAdsIsRegInvestmentProject());
        projectDetailDTO.setAdsIsTreasurySupport(source.getAdsIsTreasurySupport());

        projectDetailDTO.setAdsHasIncomeTax(source.getAdsHasIncomeTax());
        List<InvestmentInObjectMainIndicator> investmentInObjectMainIndicatorList = source.getFeiInvestmentsInObject();
        if (investmentInObjectMainIndicatorList != null) {
            projectDetailDTO.setFeiInvestmentsInObject(new ArrayList<InvestmentInObjectMainIndicator>(investmentInObjectMainIndicatorList));
        }
        List<OperationalCostsIndicator> operationalCostsIndicatorList = source.getFeiOperationalCosts();
        if (operationalCostsIndicatorList != null) {
            projectDetailDTO.setFeiOperationalCosts(new ArrayList<OperationalCostsIndicator>(operationalCostsIndicatorList));
        }

        List<TaxConditionIndicator> taxConditionIndicatorList = source.getFeiTaxCondition();
        if (taxConditionIndicatorList != null) {
            projectDetailDTO.setFeiTaxCondition(new ArrayList<TaxConditionIndicator>(taxConditionIndicatorList));
        }
        List<RevenueServiceIndicator> revenueServiceIndicatorList = source.getFeiRevenueService();
        if (revenueServiceIndicatorList != null) {
            projectDetailDTO.setFeiRevenue(new ArrayList<RevenueServiceIndicator>(revenueServiceIndicatorList));
        }
        projectDetailDTO.setFeiTaxIncentivesExist(source.getFeiTaxIncentivesExist());
        projectDetailDTO.setFeiResidualValue(source.getFeiResidualValue());
        projectDetailDTO.setFeiAverageServiceLife(source.getFeiAverageServiceLife());
        projectDetailDTO.setFeiForecastValuesDate(source.getFeiForecastValuesDate());
        projectDetailDTO.setAdsIncomeTaxRate(source.getAdsIncomeTaxRate());
        projectDetailDTO.setAdsHasLandTax(source.getAdsHasLandTax());
        projectDetailDTO.setAdsLandTaxRate(source.getAdsLandTaxRate());
        projectDetailDTO.setAdsHasPropertyTax(source.getAdsHasPropertyTax());
        projectDetailDTO.setAdsPropertyTaxRate(source.getAdsPropertyTaxRate());
        projectDetailDTO.setAdsHasBenefitClarificationTax(source.getAdsHasBenefitClarificationTax());
        projectDetailDTO.setAdsBenefitClarificationRate(source.getAdsBenefitClarificationRate());
        projectDetailDTO.setAdsBenefitDescription(source.getAdsBenefitDescription());

        List<AdsDecisionTextFile> list221 = source.getAdsDecisionTextFileId();
        if (list221 != null) {
            projectDetailDTO.setAdsDecisionTextFileId(new ArrayList<AdsDecisionTextFile>(list221));
        }

        List<PrivatePartnerThirdPartyOrg> list30 = source.getAdsPrivatePartnerThirdPartyOrgs();
        if (list30 != null) {
            projectDetailDTO.setAdsPrivatePartnerThirdPartyOrgs(new ArrayList<PrivatePartnerThirdPartyOrg>(list30));
        }
        Collection<DictionaryDataRecordDescriptor> descriptors = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_PRIVATE_SERVICE_TYPE);
        Map<Long, PublicServiceType> publicServiceTypeMap = descriptors.stream().map(Converter::dictToPublicServiceType).collect(Collectors.toMap(PublicServiceType::getId, Function.identity()));
        for (PrivatePartnerThirdPartyOrg partyOrg : projectDetailDTO.getAdsPrivatePartnerThirdPartyOrgs()) {
            for (Long typesId : partyOrg.getWorkTypesIds()) {
                partyOrg.getWorkTypes().add(new WorkType() {{
                    setId(typesId);
                    setName(publicServiceTypeMap.get(typesId).getName());
                }});
            }
        }


        List<PublicPartnerThirdPartyOrg> list31 = source.getAdsPublicPartnerThirdPartyOrgs();
        if (list31 != null) {
            projectDetailDTO.setAdsPublicPartnerThirdPartyOrgs(new ArrayList<PublicPartnerThirdPartyOrg>(list31));
        }
        Collection<DictionaryDataRecordDescriptor> descriptors1 = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_PUBLIC_SERVICE_TYPE);
        Map<Long, PublicServiceType> publicServiceTypeMap2 = descriptors1.stream().map(Converter::dictToPublicServiceType).collect(Collectors.toMap(PublicServiceType::getId, Function.identity()));
        for (PublicPartnerThirdPartyOrg partyOrg : projectDetailDTO.getAdsPublicPartnerThirdPartyOrgs()) {
            for (Long typesId : partyOrg.getWorkTypesIds()) {
                partyOrg.getWorkTypes().add(new WorkType() {{
                    setId(typesId);
                    setName(publicServiceTypeMap2.get(typesId).getName());
                }});
            }
        }

        projectDetailDTO.setAdsNpv(source.getAdsNpv());
        projectDetailDTO.setAdsIrr(source.getAdsIrr());
        projectDetailDTO.setAdsPb(source.getAdsPb());
        projectDetailDTO.setAdsPbDiscounted(source.getAdsPbDiscounted());
        projectDetailDTO.setAdsEbidta(source.getAdsEbidta());
        projectDetailDTO.setAdsWacc(source.getAdsWacc());
        projectDetailDTO.setAdsTaxRelief(source.getAdsTaxRelief());
        List<Sanction> list32 = source.getAdsSanctions();
        if (list32 != null) {
            projectDetailDTO.setAdsSanctions(new ArrayList<Sanction>(list32));
        }
        List<JudicialActivity> list33 = source.getAdsJudicialActivities();
        if (list33 != null) {
            projectDetailDTO.setAdsJudicialActivities(new ArrayList<JudicialActivity>(list33));
        }
        projectDetailDTO.setAdsConcessionaireOpf(source.getAdsConcessionaireOpf());
        projectDetailDTO.setAdsConcessionaireName(source.getAdsConcessionaireName());
        projectDetailDTO.setAdsConcessionaireInn(source.getAdsConcessionaireInn());
        projectDetailDTO.setAdsConcessionaireRegime(source.getAdsConcessionaireRegime());
        projectDetailDTO.setAdsConcessionaireCreditRating(source.getAdsConcessionaireCreditRating());
        projectDetailDTO.setAdsConcessionaireCreditRatingStartDate(source.getAdsConcessionaireCreditRatingStartDate());
        projectDetailDTO.setAdsConcessionaireCreditRatingEndDate(source.getAdsConcessionaireCreditRatingEndDate());
        projectDetailDTO.setAdsConcessionaireCreditRatingAgency(source.getAdsConcessionaireCreditRatingAgency());
        List<OwnershipStructure> list34 = source.getAdsOwnershipStructures();
        if (list34 != null) {
            projectDetailDTO.setAdsOwnershipStructures(new ArrayList<OwnershipStructure>(list34));
        }
        List<FinancialStructure> list35 = source.getAdsFinancialStructure();
        if (list35 != null) {
            projectDetailDTO.setAdsFinancialStructure(new ArrayList<FinancialStructure>(list35));
        }
        List<InvestmentsCriteriaBoolean> list36 = source.getAdsInvestmentBoolCriterias();
        if (list36 != null) {
            projectDetailDTO.setAdsInvestmentBoolCriterias(new ArrayList<InvestmentsCriteriaBoolean>(list36));
        }
        projectDetailDTO.setAdsUnforeseenExpencesShare(source.getAdsUnforeseenExpencesShare());
        projectDetailDTO.setAdsUnforeseenExpencesShareComment(source.getAdsUnforeseenExpencesShareComment());
        projectDetailDTO.setAdsWorkPlacesCount(source.getAdsWorkPlacesCount());

        List<Long> list37 = source.getAdsCompetitionCriteria();
        List<DictionaryDataRecordDescriptor> dictDataRecordsFromCache = dictCache.getDictDataRecordsFromCache(Dictionary.DIC_GASU_GCHP_COMPETITION_CRITERION, list37);
        if (list37 != null) {
            projectDetailDTO.setAdsCompetitionCriteria(dictDataRecordsFromCache.stream().map(Converter::dictToCompetitionCriterion).collect(Collectors.toList()));
        }

        List<Long> list38 = source.getAdsFinancialRequirement();
        List<DictionaryDataRecordDescriptor> dictDataRecordsFromCache1 = dictCache.getDictDataRecordsFromCache(Dictionary.DIC_GASU_GCHP_FIN_REQUIREMENT, list38);
        if (list38 != null) {
            projectDetailDTO.setAdsFinancialRequirement(dictDataRecordsFromCache1.stream().map(Converter::dictToFinRequirement).collect(Collectors.toList()));
        }

        List<Long> list39 = source.getAdsNonFinancialRequirements();
        List<DictionaryDataRecordDescriptor> dictDataRecordsFromCache2 = dictCache.getDictDataRecordsFromCache(Dictionary.DIC_GASU_GCHP_NO_FIN_REQUIREMENT, list39);
        if (list39 != null) {
            projectDetailDTO.setAdsNonFinancialRequirements(dictDataRecordsFromCache2.stream().map(Converter::dictToNonFinRequirement).collect(Collectors.toList()));
        }

        projectDetailDTO.setCrAgreementComplex(source.getCrAgreementComplex());
        projectDetailDTO.setCrJobDoneTerm(source.getCrJobDoneTerm());
        projectDetailDTO.setCrSavingStartDate(source.getCrSavingStartDate());
        projectDetailDTO.setCrSavingEndDate(source.getCrSavingEndDate());
        projectDetailDTO.setCrInvestmentStageTerm(source.getCrInvestmentStageTerm());
        projectDetailDTO.setCrIsAutoProlongationProvided(source.getCrIsAutoProlongationProvided());
        projectDetailDTO.setCrAgreementEndDateAfterProlongation(source.getCrAgreementEndDateAfterProlongation());
        projectDetailDTO.setCrAgreementValidityAfterProlongation(source.getCrAgreementValidityAfterProlongation());
        List<CreationAgreementFile> list42 = source.getCrAgreementTextFiles();
        if (list42 != null) {
            projectDetailDTO.setCrAgreementTextFiles(new ArrayList<CreationAgreementFile>(list42));
        }

        projectDetailDTO.setCrOpf(source.getCrOpf());
        projectDetailDTO.setCrIsForeignInvestor(source.getCrIsForeignInvestor());
        projectDetailDTO.setCrIsMcpSubject(source.getCrIsMcpSubject());

        projectDetailDTO.setCmComment(source.getCmComment());
        projectDetailDTO.setCmContacts(source.getCmContacts());
        projectDetailDTO.setPublished(source.getPublished());
        projectDetailDTO.setUpdateDate(source.getUpdateDate());
        projectDetailDTO.setUpdateUserId(source.getUpdateUserId());
        projectDetailDTO.setCreateDate(source.getCreateDate());
        projectDetailDTO.setCreateUserId(source.getCreateUserId());
        projectDetailDTO.setVersion(source.getVersion());
        projectDetailDTO.setSaveIndex(source.getSaveIndex());

        projectDetailDTO.setCbcMinimumGuaranteedExist(source.getCbcMinimumGuaranteedExist());
        projectDetailDTO.setCbcProjectBudgetObligationMissing(source.getCbcProjectBudgetObligationMissing());
        projectDetailDTO.setCbcMinimumGuaranteedClauseAgreement(source.getCbcMinimumGuaranteedClauseAgreement());
        projectDetailDTO.setCbcCompensationMinimumGuaranteedExist(source.getCbcCompensationMinimumGuaranteedExist());
        projectDetailDTO.setCbcNonPaymentConsumersGoodsProvidedExist(source.getCbcNonPaymentConsumersGoodsProvidedExist());
        projectDetailDTO.setCbcLimitNonPaymentConsumersGoodsProvidedExist(source.getCbcLimitNonPaymentConsumersGoodsProvidedExist());
        projectDetailDTO.setCbcNonPaymentConsumersGoodsProvidedClauseAgreement(source.getCbcNonPaymentConsumersGoodsProvidedClauseAgreement());
        projectDetailDTO.setCbcLimitNonPaymentConsumersGoodsProvidedClauseAgreement(source.getCbcLimitNonPayConsGoodsProvidedClauseAgree());
        projectDetailDTO.setCbcCompensationLimitNonPaymentConsumersGoodsProvidedExist(source.getCbcCompLimitNonPaymentConsGoodsProvidedExist());
        projectDetailDTO.setCbcArisingProvisionOfBenefitsExist(source.getCbcArisingProvisionOfBenefitsExist());
        projectDetailDTO.setCbcArisingProvisionOfBenefitsClauseAgreement(source.getCbcArisingProvisionOfBenefitsClauseAgreement());
        projectDetailDTO.setCbcCompensationArisingProvisionOfBenefitsExist(source.getCbcCompensationArisingProvisionOfBenefitsExist());
        projectDetailDTO.setCbcCompensationArisingProvisionOfBenefitsClauseAgreement(source.getCbcCompArisingProvisionOfBenefitsClauseAgree());
        projectDetailDTO.setCbcDueToOnsetOfCertainCircumstancesExist(source.getCbcDueToOnsetOfCertainCircumstancesExist());
        projectDetailDTO.setCbcDueToOnsetOfCertainCircumstancesClauseAgreement(source.getCbcDueToOnsetOfCertainCircumstancesClauseAgreement());
        projectDetailDTO.setCbcLimitCompensationAdditionalCostsAgreementExist(source.getCbcLimitCompAdditionalCostsAgreeExist());
        projectDetailDTO.setCbcLimitCompensationAdditionalClauseAgreement(source.getCbcLimitCompensationAdditionalClauseAgreement());
        projectDetailDTO.setCbcSpecifyOtherCircumstancesPrepare(source.getCbcSpecifyOtherCircumstancesPrepare());
        projectDetailDTO.setCbcSpecifyOtherCircumstancesBuild(source.getCbcSpecifyOtherCircumstancesBuild());
        projectDetailDTO.setCbcSpecifyOtherCircumstancesExploitation(source.getCbcSpecifyOtherCircumstancesExploitation());
        projectDetailDTO.setCbcCompensationAdditionalCostsAgreementExist(source.getCbcCompensationAdditionalCostsAgreementExist());

        List<ArisingProvisionOfBenefitFile> list1111 = source.getCbcArisingProvisionOfBenefitFVId();
        if (list1111 != null) {
            projectDetailDTO.setCbcArisingProvisionOfBenefitFVId(new ArrayList<>(list1111));
        }
        List<CompensationAdditionalCostsAgreementFile> list1112 = source.getCbcCompensationAdditionalCostsAgreementFVId();
        if (list1112 != null) {
            projectDetailDTO.setCbcCompensationAdditionalCostsAgreementFVId(new ArrayList<>(list1112));
        }
        List<CompensationArisingProvisionOfBenefitsFile> list1113 = source.getCbcCompensationArisingProvisionOfBenefitsFVId();
        if (list1113 != null) {
            projectDetailDTO.setCbcCompensationArisingProvisionOfBenefitsFVId(new ArrayList<>(list1113));
        }
        List<MinimumGuaranteedFile> list1114 = source.getCbcMinimumGuaranteedFVId();
        if (list1114 != null) {
            projectDetailDTO.setCbcMinimumGuaranteedFVId(new ArrayList<>(list1114));
        }
        List<NonPaymentConsumersFile> list1115 = source.getCbcNonPaymentConsumersFVId();
        if (list1115 != null) {
            projectDetailDTO.setCbcNonPaymentConsumersFVId(new ArrayList<>(list1115));
        }

        List<Long> list1116 = source.getCbcNameOfCircumstanceAdditionalCostPrepare();
        if (list1116 != null) {
            projectDetailDTO.setCbcNameOfCircumstanceAdditionalCostPrepare(new ArrayList<>(list1116));
        }
        List<Long> list1117 = source.getCbcNameOfCircumstanceAdditionalCostBuild();
        if (list1117 != null) {
            projectDetailDTO.setCbcNameOfCircumstanceAdditionalCostBuild(new ArrayList<>(list1117));
        }
        List<Long> list1118 = source.getCbcNameOfCircumstanceAdditionalCostExploitation();
        if (list1118 != null) {
            projectDetailDTO.setCbcNameOfCircumstanceAdditionalCostExploitation(new ArrayList<>(list1118));
        }

        projectDetailDTO.setCbcMinimumGuaranteedIncomeForm(source.getCbcMinimumGuaranteedIncomeForm());
        projectDetailDTO.setCbcNonPaymentConsumersGoodsProvidedForm(source.getCbcNonPaymentConsumersGoodsProvidedForm());

        List<SimpleYearIndicator> listCbc3 = source.getCbcMinimumGuaranteedAmount();
        if (listCbc3 != null) {
            projectDetailDTO.setCbcMinimumGuaranteedAmount(new ArrayList<>(listCbc3));
        }

        projectDetailDTO.setCbcMinimumGuaranteedAmountNdsCheck(source.getCbcMinimumGuaranteedAmountNdsCheck());
        projectDetailDTO.setCbcMinimumGuaranteedAmountDateField(source.getCbcMinimumGuaranteedAmountDateField());
        projectDetailDTO.setCbcMinimumGuaranteedAmountMeasureType(source.getCbcMinimumGuaranteedAmountMeasureType());

        List<SimpleYearIndicator> listCbc7 = source.getCbcCompensationMinimumGuaranteedAmount();
        if (listCbc7 != null) {
            projectDetailDTO.setCbcCompensationMinimumGuaranteedAmount(new ArrayList<>(listCbc7));
        }
        List<SimpleYearIndicator> listCbc14 = source.getCbcCompensationLimitNonPaymentAmount();
        if (listCbc14 != null) {
            projectDetailDTO.setCbcCompensationLimitNonPaymentAmount(new ArrayList<>(listCbc14));
        }

        List<SimpleYearIndicator> listCbc18 = source.getCbcCompensationArisingProvisionOfBenefitsAmount();
        if (listCbc18 != null) {
            projectDetailDTO.setCbcCompensationArisingProvisionOfBenefitsAmount(new ArrayList<>(listCbc18));
        }
        List<CircumstanceStageIndicator> listCbc23 = source.getCbcLimitCompensationAdditionalCostsAmount();
        if (listCbc23 != null) {
            projectDetailDTO.setCbcLimitCompensationAdditionalCostsAmount(new ArrayList<>(listCbc23));
        }

        projectDetailDTO.setCbcNdsCheck(source.getCbcNdsCheck());
        projectDetailDTO.setIoNdsCheck(source.getIoNdsCheck());
        projectDetailDTO.setCbcDateField(source.getCbcDateField());
        projectDetailDTO.setIoDateField(source.getIoDateField());
        projectDetailDTO.setCbcMeasureType(source.getCbcMeasureType());
        projectDetailDTO.setIoMeasureType(source.getIoMeasureType());

        List<CircumstanceStageIndicator> listCbc27 = source.getCbcCircumstancesAdditionalCostsAmount();
        if (listCbc27 != null) {
            projectDetailDTO.setCbcCircumstancesAdditionalCostsAmount(new ArrayList<>(listCbc27));
        }

        return projectDetailDTO;
    }

    public Project transform(ProjectDetailDTO source) {
        if (source == null) {
            return null;
        }

        Project projectDetailsSimple = new Project();

        projectDetailsSimple.setId(source.getId());
        projectDetailsSimple.setGiName(source.getGiName());
        projectDetailsSimple.setGiRealizationForm(opt(() -> source.getGiRealizationForm().getId()));
        projectDetailsSimple.setGiInitiationMethod(opt(() -> source.getGiInitiationMethod().getId()));
        projectDetailsSimple.setGiRealizationLevel(opt(() -> source.getGiRealizationLevel().getId()));
        projectDetailsSimple.setGiIsRFPartOfAgreement(source.getGiIsRFPartOfAgreement());
        projectDetailsSimple.setGiIsRegionPartOfAgreement(source.getGiIsRegionPartOfAgreement());
        projectDetailsSimple.setGiIsMunicipalityPartOfAgreement(source.getGiIsMunicipalityPartOfAgreement());
        projectDetailsSimple.setGiRegion(opt(() -> source.getGiRegion().getId()));
        projectDetailsSimple.setGiMunicipality(opt(() -> source.getGiMunicipality().getId()));
        projectDetailsSimple.setGiPublicPartner(opt(() -> source.getGiPublicPartner().getId()));
        projectDetailsSimple.setGiInn(source.getGiInn());
        projectDetailsSimple.setGiBalanceHolder(source.getGiBalanceHolder());
        projectDetailsSimple.setGiImplementer(source.getGiImplementer());
        projectDetailsSimple.setGiImplementerInn(source.getGiImplementerInn());
        projectDetailsSimple.setGiOPF(opt(() -> source.getGiOPF().getId()));
        projectDetailsSimple.setGiIsForeignInvestor(source.getGiIsForeignInvestor());
        projectDetailsSimple.setGiIsMcpSubject(source.getGiIsMcpSubject());
        projectDetailsSimple.setGiIsSpecialProjectCompany(source.getGiIsSpecialProjectCompany());
        projectDetailsSimple.setGiHasInvestmentProperty(source.getGiHasInvestmentProperty());
        projectDetailsSimple.setGiPublicSharePercentage(source.getGiPublicSharePercentage());
        projectDetailsSimple.setGiIsRFHasShare(source.getGiIsRFHasShare());
        projectDetailsSimple.setGiRealizationSphere(opt(() -> source.getGiRealizationSphere().getId()));
        projectDetailsSimple.setGiRealizationSector(opt(() -> source.getGiRealizationSector().getId()));

        List<ObjectKind> list = source.getGiObjectType();
        if (list != null) {
            List<Long> idList = list.stream().mapToLong(o -> Long.parseLong(o.getId())).boxed().collect(Collectors.toList());
            projectDetailsSimple.setGiObjectType(idList);
        }
        projectDetailsSimple.setGiObjectLocation(source.getGiObjectLocation());
        List<AgreementSubject> list1 = source.getGiAgreementSubject();
        if (list1 != null) {
            List<Long> idList = list1.stream().mapToLong(o -> Long.parseLong(o.getId())).boxed().collect(Collectors.toList());
            projectDetailsSimple.setGiAgreementSubject(idList);
        }
        projectDetailsSimple.setGiRealizationStatus(opt(() -> source.getGiRealizationStatus().getId()));

        List<CompletedTemplateFile> list228 = source.getGiCompletedTemplateTextFVId();
        if (list228 != null) {
            projectDetailsSimple.setGiCompletedTemplateTextFVId(new ArrayList<CompletedTemplateFile>(list228));
        }

        projectDetailsSimple.setGiProjectStatus(opt(() -> source.getGiProjectStatus().getId()));
        projectDetailsSimple.setGiAlwaysDraftStatus(source.getGiAlwaysDraftStatus());
        projectDetailsSimple.setPpInvestmentPlanningAmount(source.getPpInvestmentPlanningAmount());
        projectDetailsSimple.setPpCreationInvestmentPlanningAmount(source.getPpCreationInvestmentPlanningAmount());
        projectDetailsSimple.setCbcInvestments1(source.getCbcInvestments1());
        projectDetailsSimple.setCbcInvestments2(source.getCbcInvestments2());
        projectDetailsSimple.setCbcInvestments3(source.getCbcInvestments3());
        projectDetailsSimple.setCbcInvestments4(source.getCbcInvestments4());
        projectDetailsSimple.setRemainingDebt(source.getRemainingDebt());
        projectDetailsSimple.setOdObjectName(source.getOdObjectName());
        projectDetailsSimple.setOdObjectDescription(source.getOdObjectDescription());
        projectDetailsSimple.setOdIsPropertyStaysWithPrivateSide(source.getOdIsPropertyStaysWithPrivateSide());
        projectDetailsSimple.setOdIsNewPropertyBeGivenToPrivateSide(source.getOdIsNewPropertyBeGivenToPrivateSide());
        projectDetailsSimple.setOdIsObjectImprovementsGiveAway(source.getOdIsObjectImprovementsGiveAway());
        projectDetailsSimple.setOdRentObject(source.getOdRentObject());
        List<RentPassportFilesEntity> list2 = source.getOdRentPassportFileVersionId();
        if (list2 != null) {
            projectDetailsSimple.setOdRentPassportFileVersionId(new ArrayList<RentPassportFilesEntity>(list2));
        }
        List<TechEconomicsObjectIndicator> list3 = source.getOdTechEconomicsObjectIndicators();
        if (list3 != null) {
            for(TechEconomicsObjectIndicator object : list3) {
                for (TechEconomicsIndicator indicator : object.getTechEconomicsIndicators()) {
                    indicator.setUmId(opt(() -> indicator.getUm().getId()));
                }
            }
            projectDetailsSimple.setOdTechEconomicsObjectIndicators(new ArrayList<TechEconomicsObjectIndicator>(list3));
        }

        List<EnergyEfficiencyPlan> list4 = source.getOdEnergyEfficiencyPlans();
        if (list4 != null) {
            projectDetailsSimple.setOdEnergyEfficiencyPlans(new ArrayList<EnergyEfficiencyPlan>(list4));
        }
        projectDetailsSimple.setPpIsObjInListWithConcessionalAgreements(source.getPpIsObjInListWithConcessionalAgreements());
        projectDetailsSimple.setPpObjectsListUrl(source.getPpObjectsListUrl());
        projectDetailsSimple.setPpSupposedPrivatePartnerName(source.getPpSupposedPrivatePartnerName());
        projectDetailsSimple.setPpSupposedPrivatePartnerInn(source.getPpSupposedPrivatePartnerInn());
        projectDetailsSimple.setPpIsForeignInvestor(source.getPpIsForeignInvestor());
        projectDetailsSimple.setPpIsMspSubject(source.getPpIsMspSubject());
        projectDetailsSimple.setPpAgreementsSet(opt(() -> source.getPpAgreementsSet().getId()));
        projectDetailsSimple.setPpSupposedAgreementStartDate(source.getPpSupposedAgreementStartDate());
        projectDetailsSimple.setPpSupposedAgreementEndDate(source.getPpSupposedAgreementEndDate());
        projectDetailsSimple.setPpSupposedValidityYears(source.getPpSupposedValidityYears());
        projectDetailsSimple.setPpDeliveryTimeOfGoodsWorkDate(source.getPpDeliveryTimeOfGoodsWorkDate());
        List<ProjectAgreementFile> list5 = source.getPpProjectAgreementFileVersionId();
        if (list5 != null) {
            projectDetailsSimple.setPpProjectAgreementFileVersionId(new ArrayList<ProjectAgreementFile>(list5));
        }
        projectDetailsSimple.setPpGroundsOfAgreementConclusion(source.getPpGroundsOfAgreementConclusion());
        projectDetailsSimple.setPpActNumber(source.getPpActNumber());
        projectDetailsSimple.setPpActDate(source.getPpActDate());
        List<ActTextFile> list6 = source.getPpActTextFileVersionId();
        if (list6 != null) {
            projectDetailsSimple.setPpActTextFileVersionId(new ArrayList<ActTextFile>(list6));
        }
        List<LeaseAgreementTextFile> list226 = source.getPpLeaseAgreementTextFileVersionId();
        if (list226 != null) {
            projectDetailsSimple.setPpLeaseAgreementTextFileVersionId(new ArrayList<LeaseAgreementTextFile>(list226));
        }
        projectDetailsSimple.setPpProjectAssignedStatus(source.getPpProjectAssignedStatus());
        projectDetailsSimple.setPpDecisionNumber(source.getPpDecisionNumber());
        projectDetailsSimple.setPpDecisionDate(source.getPpDecisionDate());
        List<DecisionTextFile> list116 = source.getPpDecisionTextFileVersionId();
        if (list116 != null) {
            projectDetailsSimple.setPpDecisionTextFileVersionId(new ArrayList<DecisionTextFile>(list116));
        }
        projectDetailsSimple.setPpProposalPublishDate(source.getPpProposalPublishDate());
        List<ProposalTextFile> list7 = source.getPpProposalTextFileVersionId();
        if (list7 != null) {
            projectDetailsSimple.setPpProposalTextFileVersionId(new ArrayList<ProposalTextFile>(list7));
        }
        projectDetailsSimple.setPpTorgiGovRuUrl(source.getPpTorgiGovRuUrl());
        List<ProtocolFile> list8 = source.getPpProtocolFileVersionId();
        if (list8 != null) {
            projectDetailsSimple.setPpProtocolFileVersionId(new ArrayList<ProtocolFile>(list8));
        }
        projectDetailsSimple.setPpIsReadinessRequestReceived(source.getPpIsReadinessRequestReceived());
        projectDetailsSimple.setPpIsDecisionMadeToConcludeAnAgreement(source.getPpIsDecisionMadeToConcludeAnAgreement());
        projectDetailsSimple.setPpConcludeAgreementActNum(source.getPpConcludeAgreementActNum());
        projectDetailsSimple.setPpConcludeAgreementActDate(source.getPpConcludeAgreementActDate());
        List<ConcludeAgreementFile> list9 = source.getPpConcludeAgreementFvId();
        if (list9 != null) {
            projectDetailsSimple.setPpConcludeAgreementFvId(new ArrayList<ConcludeAgreementFile>(list9));
        }
        projectDetailsSimple.setPpInvestmentStageDurationDate(source.getPpInvestmentStageDurationDate());
        projectDetailsSimple.setPpConcludeAgreementIsSigned(source.getPpConcludeAgreementIsSigned());
        projectDetailsSimple.setPpConcludeAgreementIsJoint(source.getPpConcludeAgreementIsJoint());
        projectDetailsSimple.setPpConcludeAgreementOtherPjInfo(source.getPpConcludeAgreementOtherPjInfo());
        projectDetailsSimple.setPpConcludeAgreementOtherPjIdent(source.getPpConcludeAgreementOtherPjIdent());
        projectDetailsSimple.setPpCompetitionBidCollEndPlanDate(source.getPpCompetitionBidCollEndPlanDate());
        projectDetailsSimple.setPpCompetitionBidCollEndFactDate(source.getPpCompetitionBidCollEndFactDate());
        projectDetailsSimple.setPpCompetitionTenderOfferEndPlanDate(source.getPpCompetitionTenderOfferEndPlanDate());
        projectDetailsSimple.setPpCompetitionTenderOfferEndFactDate(source.getPpCompetitionTenderOfferEndFactDate());
        projectDetailsSimple.setPpCompetitionResultsPlanDate(source.getPpCompetitionResultsPlanDate());
        projectDetailsSimple.setPpCompetitionResultsFactDate(source.getPpCompetitionResultsFactDate());
        List<CompetitionText> list10 = source.getPpCompetitionTextFVId();
        if (list10 != null) {
            projectDetailsSimple.setPpCompetitionTextFVId(new ArrayList<CompetitionText>(list10));
        }
        projectDetailsSimple.setPpCompetitionIsElAuction(source.getPpCompetitionIsElAuction());
        projectDetailsSimple.setPpCompetitionHasResults(source.getPpCompetitionHasResults());
        projectDetailsSimple.setPpCompetitionResults(source.getPpCompetitionResults());
        projectDetailsSimple.setPpCompetitionResultsProtocolNum(source.getPpCompetitionResultsProtocolNum());
        projectDetailsSimple.setPpCompetitionResultsProtocolDate(source.getPpCompetitionResultsProtocolDate());
        projectDetailsSimple.setPpCompetitionResultsParticipantsNum(source.getPpCompetitionResultsParticipantsNum());
        List<CompetitionResultsProtocolFile> list11 = source.getPpCompetitionResultsProtocolTextFvId();
        if (list11 != null) {
            projectDetailsSimple.setPpCompetitionResultsProtocolTextFvId(new ArrayList<CompetitionResultsProtocolFile>(list11));
        }
        List<CompetitionResultsFile> list12 = source.getPpCompetitionResultsDocFvId();
        if (list12 != null) {
            projectDetailsSimple.setPpCompetitionResultsDocFvId(new ArrayList<CompetitionResultsFile>(list12));
        }
        projectDetailsSimple.setPpCompetitionResultsSignStatus(source.getPpCompetitionResultsSignStatus());
        projectDetailsSimple.setPpContractPriceOrder(source.getPpContractPriceOrder());
        projectDetailsSimple.setPpContractPriceFormula(source.getPpContractPriceFormula());
        projectDetailsSimple.setPpContractPricePrice(source.getPpContractPricePrice());
        projectDetailsSimple.setPpNdsCheck(source.getPpNdsCheck());
        projectDetailsSimple.setPpMeasureType(source.getPpMeasureType());
        projectDetailsSimple.setPpDateField(source.getPpDateField());
//        projectDetailsSimple.setPpContractPriceMethod(source.getPpContractPriceMethod());
        projectDetailsSimple.setPpContractPriceOffer(source.getPpContractPriceOffer());
        projectDetailsSimple.setPpContractPriceOfferValue(source.getPpContractPriceOfferValue());
        projectDetailsSimple.setPpWinnerContractPriceOffer(opt(() -> source.getPpWinnerContractPriceOffer().getId()));
        projectDetailsSimple.setPpContractPriceSavingStartDate(source.getPpContractPriceSavingStartDate());
        projectDetailsSimple.setPpContractPriceSavingEndDate(source.getPpContractPriceSavingEndDate());
        List<FinancialModelFile> list13 = source.getPpFinancialModelFVId();
        if (list13 != null) {
            projectDetailsSimple.setPpFinancialModelFVId(new ArrayList<FinancialModelFile>(list13));
        }
        projectDetailsSimple.setPpPrivatePartnerCostRecoveryMethod(source.getPpPrivatePartnerCostRecoveryMethod());
        projectDetailsSimple.setPpAdvancePaymentAmount(source.getPpAdvancePaymentAmount());
        projectDetailsSimple.setPpFirstObjectOperationDate(source.getPpFirstObjectOperationDate());
        projectDetailsSimple.setPpLastObjectCommissioningDate(source.getPpLastObjectCommissioningDate());
        List<SupportingDocumentsFile> list14 = source.getPpSupportingDocumentsFileVersionIds();
        if (list14 != null) {
            projectDetailsSimple.setPpSupportingDocumentsFileVersionIds(new ArrayList<SupportingDocumentsFile>(list14));
        }
        projectDetailsSimple.setPpBudgetExpendituresAgreementOnGchpMchp(source.getPpBudgetExpendituresAgreementOnGchpMchp());
        projectDetailsSimple.setPpBudgetExpendituresGovContract(source.getPpBudgetExpendituresGovContract());
        projectDetailsSimple.setPpObligationsInCaseOfRisksAgreementOnGchpMchp(source.getPpObligationsInCaseOfRisksAgreementOnGchpMchp());
        projectDetailsSimple.setPpObligationsInCaseOfRisksGovContract(source.getPpObligationsInCaseOfRisksGovContract());

        projectDetailsSimple.setPpIndicatorAssessmentComparativeAdvantage(source.getPpIndicatorAssessmentComparativeAdvantage());
        List<ConclusionUOTextFile> list114 = source.getPpConclusionUOTextFileVersionId();
        if (list114 != null) {
            projectDetailsSimple.setPpConclusionUOTextFileVersionId(new ArrayList<ConclusionUOTextFile>(list114));
        }
        List<FinancialModelTextFile> list115 = source.getPpFinancialModelTextFileVersionId();
        if (list114 != null) {
            projectDetailsSimple.setPpFinancialModelTextFileVersionId(new ArrayList<FinancialModelTextFile>(list115));
        }
        projectDetailsSimple.setPpMethodOfExecuteObligation(opt(() -> source.getPpMethodOfExecuteObligation().getId()));
        projectDetailsSimple.setPpLinkToClauseAgreement(source.getPpLinkToClauseAgreement());
        projectDetailsSimple.setPpIsPrivateLiabilityProvided(source.getPpIsPrivateLiabilityProvided());
        projectDetailsSimple.setPpLinkToClauseAgreementLiabilityProvided(source.getPpLinkToClauseAgreementLiabilityProvided());
        projectDetailsSimple.setPpStateSupportMeasuresSPIC(opt(() -> source.getPpStateSupportMeasuresSPIC().getId()));
        projectDetailsSimple.setPpImplementProject(source.getPpImplementProject());
        projectDetailsSimple.setPpConcludeAgreementLink(source.getPpConcludeAgreementLink());
        projectDetailsSimple.setPpResultsOfPlacing(source.getPpResultsOfPlacing());
        projectDetailsSimple.setCrAgreementStartDate(source.getCrAgreementStartDate());
        projectDetailsSimple.setCrAgreementEndDate(source.getCrAgreementEndDate());
        projectDetailsSimple.setCrAgreementValidity(source.getCrAgreementValidity());
        List<AgreementTextFile> list15 = source.getCrAgreementTextFvId();
        if (list15 != null) {
            projectDetailsSimple.setCrAgreementTextFvId(new ArrayList<AgreementTextFile>(list15));
        }
        projectDetailsSimple.setCrConcessionaire(source.getCrConcessionaire());
        projectDetailsSimple.setCrConcessionaireInn(source.getCrConcessionaireInn());
        projectDetailsSimple.setCrFinancialClosingProvided(source.getCrFinancialClosingProvided());
        projectDetailsSimple.setCrFinancialClosingDate(source.getCrFinancialClosingDate());
        projectDetailsSimple.setCrFinancialClosingValue(source.getCrFinancialClosingValue());
        List<FinancialClosingActFile> list16 = source.getCrFinancialClosingActFVId();
        if (list16 != null) {
            projectDetailsSimple.setCrFinancialClosingActFVId(new ArrayList<FinancialClosingActFile>(list16));
        }
        projectDetailsSimple.setCrFinancialClosingIsMutualAgreement(source.getCrFinancialClosingIsMutualAgreement());
        projectDetailsSimple.setCrFirstObjectCreationPlanDate(source.getCrFirstObjectCreationPlanDate());
        projectDetailsSimple.setCrFirstObjectCreationFactDate(source.getCrFirstObjectCreationFactDate());
        List<FirstObjectCompleteActFile> list17 = source.getCrFirstObjectCompleteActFVId();
        if (list17 != null) {
            projectDetailsSimple.setCrFirstObjectCompleteActFVId(new ArrayList<FirstObjectCompleteActFile>(list17));
        }
        projectDetailsSimple.setCrIsRegionPartyAgreement(source.getCrIsRegionPartyAgreement());
        projectDetailsSimple.setCrIsSeveralObjects(source.getCrIsSeveralObjects());
        projectDetailsSimple.setCrFirstSeveralObjectPlanDate(source.getCrFirstSeveralObjectPlanDate());

        projectDetailsSimple.setCrIsFirstSeveralObject(source.getCrIsFirstSeveralObject());
        projectDetailsSimple.setCrLastSeveralObjectPlanDate(source.getCrLastSeveralObjectPlanDate());

        projectDetailsSimple.setCrIsLastSeveralObject(source.getCrIsLastSeveralObject());
        projectDetailsSimple.setCrSeveralObjectPlanDate(source.getCrSeveralObjectPlanDate());

        projectDetailsSimple.setCrIsSeveralObjectInOperation(source.getCrIsSeveralObjectInOperation());

        projectDetailsSimple.setCrInvestCostsGrantor(source.getCrInvestCostsGrantor());

        projectDetailsSimple.setCrIsFormulasInvestCosts(source.getCrIsFormulasInvestCosts());

        List<CalcInvestCostsActFile> list1888 = source.getCrCalcInvestCostsActFVId();
        if (list1888 != null) {
            projectDetailsSimple.setCrCalcInvestCostsActFVId(new ArrayList<CalcInvestCostsActFile>(list1888));
        }
        projectDetailsSimple.setCrActualCostsValue(source.getCrActualCostsValue());

        projectDetailsSimple.setCrAverageInterestRateValue(source.getCrAverageInterestRateValue());

        projectDetailsSimple.setCrLastObjectCreationPlanDate(source.getCrLastObjectCreationPlanDate());
        projectDetailsSimple.setCrLastObjectCreationFactDate(source.getCrLastObjectCreationFactDate());
        List<LastObjectCompleteActFile> list18 = source.getCrLastObjectCompleteActFVId();
        if (list18 != null) {
            projectDetailsSimple.setCrLastObjectCompleteActFVId(new ArrayList<LastObjectCompleteActFile>(list18));
        }
        projectDetailsSimple.setCrIsObjectTransferProvided(source.getCrIsObjectTransferProvided());
        projectDetailsSimple.setCrObjectRightsPlanDate(source.getCrObjectRightsPlanDate());
        projectDetailsSimple.setCrObjectRightsFactDate(source.getCrObjectRightsFactDate());
        List<InvestmentsActFile> list19 = source.getCrActFVId();
        if (list19 != null) {
            projectDetailsSimple.setCrActFVId(new ArrayList<InvestmentsActFile>(list19));
        }
        projectDetailsSimple.setCrObjectValue(source.getCrObjectValue());
        projectDetailsSimple.setCrIsRenewableBankGuarantee(source.getCrIsRenewableBankGuarantee());
        projectDetailsSimple.setCrIsGuaranteeVariesByYear(source.getCrIsGuaranteeVariesByYear());
        projectDetailsSimple.setCrBankGuaranteeByYears(source.getCrBankGuaranteeByYears());
        List<InvestmentsLinkFile> list20 = source.getCrReferenceFVId();
        if (list20 != null) {
            projectDetailsSimple.setCrReferenceFVId(new ArrayList<InvestmentsLinkFile>(list20));
        }
        projectDetailsSimple.setCrLandProvided(source.getCrLandProvided());
        projectDetailsSimple.setCrLandIsConcessionaireOwner(source.getCrLandIsConcessionaireOwner());
        projectDetailsSimple.setCrLandActStartPlanDate(source.getCrLandActStartPlanDate());
        projectDetailsSimple.setCrLandActStartFactDate(source.getCrLandActStartFactDate());
        projectDetailsSimple.setCrLandActEndPlanDate(source.getCrLandActEndPlanDate());
        projectDetailsSimple.setCrLandActEndFactDate(source.getCrLandActEndFactDate());
        List<LandActFile> list21 = source.getCrLandActFVId();
        if (list21 != null) {
            projectDetailsSimple.setCrLandActFVId(new ArrayList<LandActFile>(list21));
        }
        projectDetailsSimple.setCrIsObligationExecuteOnCreationStage(source.getCrIsObligationExecuteOnCreationStage());

        List<CreationEnsureMethod> list41 = source.getCrEnsureMethods();
        if (list41 != null) {
            projectDetailsSimple.setCrEnsureMethods(new ArrayList<>(list41));
        }

        List<GovSupport> list22 = source.getCrGovSupport();
        if (list22 != null) {
            List<Long> idList = list22.stream().mapToLong(o -> o.getId()).boxed().collect(Collectors.toList());
            projectDetailsSimple.setCrGovSupport(idList);
        }
        List<ConfirmationDocFile> list23 = source.getCrConfirmationDocFVId();
        if (list23 != null) {
            projectDetailsSimple.setCrConfirmationDocFVId(new ArrayList<ConfirmationDocFile>(list23));
        }
        projectDetailsSimple.setExLastObjectPlanDate(source.getExLastObjectPlanDate());
        projectDetailsSimple.setExLastObjectFactDate(source.getExLastObjectFactDate());
        List<LastObjectActFile> list24 = source.getExLastObjectActFVId();
        if (list24 != null) {
            projectDetailsSimple.setExLastObjectActFVId(new ArrayList<LastObjectActFile>(list24));
        }
        projectDetailsSimple.setExIsInvestmentsRecoveryProvided(source.getExIsInvestmentsRecoveryProvided());
        projectDetailsSimple.setExInvestmentExploitationRecoveryAmount(source.getExInvestmentExploitationRecoveryAmount());
        List<InvestmentRecoveryFinancialModelFile> list25 = source.getExInvestmentRecoveryFinancialModelFileVersionId();
        if (list25 != null) {
            projectDetailsSimple.setExInvestmentRecoveryFinancialModelFileVersionId(new ArrayList<InvestmentRecoveryFinancialModelFile>(list25));
        }
        projectDetailsSimple.setExIRSource(source.getExIRSource());
        projectDetailsSimple.setExIRLevel(source.getExIRLevel());
        projectDetailsSimple.setExIsObligationExecutingOnOperationPhase(source.getExIsObligationExecutingOnOperationPhase());
        ////projectDetailsSimple.setExMethodOfExecOfPublicPartnerObligation(source.getExMethodOfExecOfPublicPartnerObligation());
        ////projectDetailsSimple.setExInvestmentRecoveryTerm(source.getExInvestmentRecoveryTerm());
        ////projectDetailsSimple.setExInvestmentRecoveryValue(source.getExInvestmentRecoveryValue());
        List<ExploitationEnsureMethod> exEnsureMethods = source.getExEnsureMethods();
        if (exEnsureMethods != null) {
            projectDetailsSimple.setExEnsureMethods(new ArrayList<>(exEnsureMethods));
        }

        projectDetailsSimple.setExIsRenewableBankGuarantee(source.getExIsRenewableBankGuarantee());
        projectDetailsSimple.setExIsGuaranteeVariesByYear(source.getExIsGuaranteeVariesByYear());


        projectDetailsSimple.setExBankGuaranteeByYears(source.getExBankGuaranteeByYears());

        projectDetailsSimple.setExIsConcessionPayProvideded(source.getExIsConcessionPayProvideded());
        projectDetailsSimple.setExPaymentForm(source.getExPaymentForm());
        projectDetailsSimple.setTmCause(source.getTmCause());
        projectDetailsSimple.setTmActPlanDate(source.getTmActPlanDate());
        projectDetailsSimple.setTmActFactDate(source.getTmActFactDate());
        projectDetailsSimple.setTmActNumber(source.getTmActNumber());
        projectDetailsSimple.setTmActDate(source.getTmActDate());
        List<TerminationActFile> list26 = source.getTmTextFileVersionId();
        if (list26 != null) {
            projectDetailsSimple.setTmTextFileVersionId(new ArrayList<TerminationActFile>(list26));
        }
        projectDetailsSimple.setTmCauseDescription(source.getTmCauseDescription());
        projectDetailsSimple.setTmPlanDate(source.getTmPlanDate());
        projectDetailsSimple.setTmFactDate(source.getTmFactDate());
        List<TerminationActTextFile> list27 = source.getTmTaActTextFileVersionId();
        if (list27 != null) {
            projectDetailsSimple.setTmTaActTextFileVersionId(new ArrayList<TerminationActTextFile>(list27));
        }
        projectDetailsSimple.setTmPropertyJointProvided(source.getTmPropertyJointProvided());
        projectDetailsSimple.setTmPropertyJointPrivatePercent(source.getTmPropertyJointPrivatePercent());
        projectDetailsSimple.setTmPropertyJointPublicPercent(source.getTmPropertyJointPublicPercent());

        projectDetailsSimple.setTmIsCompensationPayed(source.getTmIsCompensationPayed());
        projectDetailsSimple.setTmCompensationValue(source.getTmCompensationValue());
        List<TerminationCompensationFile> list40 = source.getTmCompensationFVIds();
        if (list40 != null) {
            projectDetailsSimple.setTmCompensationFVIds(new ArrayList<TerminationCompensationFile>(list40));
        }

        projectDetailsSimple.setTmAftermath(source.getTmAftermath());
        projectDetailsSimple.setTmContractNumber(source.getTmContractNumber());
        projectDetailsSimple.setTmContractDate(source.getTmContractDate());
        projectDetailsSimple.setTmProjectId(source.getTmProjectId());
        projectDetailsSimple.setTmPublicShare(source.getTmPublicShare());
        projectDetailsSimple.setTmIsRfHasShare(source.getTmIsRfHasShare());
        projectDetailsSimple.setTmAnotherDescription(source.getTmAnotherDescription());
        projectDetailsSimple.setTmClausesOfAgreement(source.getTmClausesOfAgreement());
        projectDetailsSimple.setTmCompensationLimit(source.getTmCompensationLimit());
        projectDetailsSimple.setTmAgreementTerminated(source.getTmAgreementTerminated());
        projectDetailsSimple.setTmCompensationSum(source.getTmCompensationSum());
        projectDetailsSimple.setTmNdsCheck(source.getTmNdsCheck());
        projectDetailsSimple.setTmMeasureType(source.getTmMeasureType());
        projectDetailsSimple.setTmDateField(source.getTmDateField());
        projectDetailsSimple.setTmCompositionOfCompensation(source.getTmCompositionOfCompensation());

        List<CompositionOfCompensation> compositonOfCompensationList = source.getTmCompositionOfCompensationView();
        if (compositonOfCompensationList != null) {
            projectDetailsSimple.setTmCompositionOfCompensationView(new ArrayList<CompositionOfCompensation>(compositonOfCompensationList));
        }

        List<TerminationSupportingDocumentsFile> list56 = source.getTmSupportingDocuments();
        if (list56 != null) {
            projectDetailsSimple.setTmSupportingDocuments(new ArrayList<TerminationSupportingDocumentsFile>(list56));
        }

        List<TmCompositionOfCompensationGrantorFaultEntity> list95 = source.getTmCompositionOfCompensationGrantorFault();
        if (list95 != null) {
            projectDetailsSimple.setTmCompositionOfCompensationGrantorFault(list95.stream().mapToLong(i -> i.getId()).boxed().collect(Collectors.toList()));
        }

        projectDetailsSimple.setCcIsChangesMade(source.getCcIsChangesMade());
        projectDetailsSimple.setCcReason(source.getCcReason());
        projectDetailsSimple.setCcActNumber(source.getCcActNumber());
        projectDetailsSimple.setCcActDate(source.getCcActDate());
        List<ChangeTextFile> list28 = source.getCcTextFileVersionId();
        if (list28 != null) {
            projectDetailsSimple.setCcTextFileVersionId(new ArrayList<ChangeTextFile>(list28));
        }
        projectDetailsSimple.setCrInvestmentCreationAmount(source.getCrInvestmentCreationAmount());

        projectDetailsSimple.setCrExpectedRepaymentYear(source.getCrExpectedRepaymentYear());

        List<BalanceOfDebt> list5367 = source.getCrBalanceOfDebt();
        if (list5367 != null) {
            projectDetailsSimple.setCrBalanceOfDebt(new ArrayList<BalanceOfDebt>(list5367));
        }

        List<InvestmentVolumeStagOfCreationActFile> list128 = source.getCrInvestmentVolumeStagOfCreationActFVId();
        if (list128 != null) {
            projectDetailsSimple.setCrInvestmentVolumeStagOfCreationActFVId(new ArrayList<InvestmentVolumeStagOfCreationActFile>(list128));
        }

        projectDetailsSimple.setExInvestmentExploitationAmount(source.getExInvestmentExploitationAmount());

        List<InvestmentVolumeStagOfExploitationActFile> list241 = source.getExInvestmentVolumeStagOfExploitationActFVId();
        if (list241 != null) {
            projectDetailsSimple.setExInvestmentVolumeStagOfExploitationActFVId(new ArrayList<InvestmentVolumeStagOfExploitationActFile>(list241));
        }
        projectDetailsSimple.setExPayment(source.getExPayment());
        projectDetailsSimple.setExCompensation(source.getExCompensation());

        projectDetailsSimple.setExPublicShareExpl(source.getExPublicShareExpl());
        projectDetailsSimple.setExHasPublicShareExpl(source.getExHasPublicShareExpl());
        List<ExploitationFinModelFile> list60 = source.getExFinModelFVIds();
        if (list60 != null) {
            projectDetailsSimple.setExFinModelFVIds(new ArrayList<ExploitationFinModelFile>(list60));
        }
        List<CostRecoveryMethodEntity> costRecoveryMethods = source.getExCostRecoveryMethod();
        if (costRecoveryMethods != null) {
            List<Long> idList = costRecoveryMethods.stream().mapToLong(CostRecoveryMethodEntity::getId).boxed().collect(Collectors.toList());
            projectDetailsSimple.setExCostRecoveryMethod(idList);
        }
        projectDetailsSimple.setExCostRecoveryMechanism(source.getExCostRecoveryMechanism());
        List<ExploitationSupportDoclFile> list61 = source.getExSupportDocFVIds();
        if (list61 != null) {
            projectDetailsSimple.setExSupportDocFVIds(new ArrayList<ExploitationSupportDoclFile>(list61));
        }

        List<ExploitationSupportCompensDoclFile> list62 = source.getExSupportCompensDocFVIds();
        if (list62 != null) {
            projectDetailsSimple.setExSupportCompensDocFVIds(new ArrayList<>(list62));
        }

        projectDetailsSimple.setExOwnPrivatePlanDate(source.getExOwnPrivatePlanDate());
        projectDetailsSimple.setExOwnPrivateFactDate(source.getExOwnPrivateFactDate());
        projectDetailsSimple.setExOwnPublicPlanDate(source.getExOwnPublicPlanDate());
        projectDetailsSimple.setExOwnPublicFactDate(source.getExOwnPublicFactDate());
        List<ExploitationAgreementFile> list50 = source.getExAgreementFVIds();
        if (list50 != null) {
            projectDetailsSimple.setExAgreementFVIds(new ArrayList<ExploitationAgreementFile>(list50));
        }
        List<ExploitationAcceptActFile> list51 = source.getExAcceptActFVIds();
        if (list51 != null) {
            projectDetailsSimple.setExAcceptActFVIds(new ArrayList<ExploitationAcceptActFile>(list51));
        }
        projectDetailsSimple.setExStartAchEconPlanDate(source.getExStartAchEconPlanDate());
        projectDetailsSimple.setExEndAchEconPlanDate(source.getExEndAchEconPlanDate());
        projectDetailsSimple.setExStartAchEconFactDate(source.getExStartAchEconFactDate());
        projectDetailsSimple.setExEndAchEconFactDate(source.getExEndAchEconFactDate());
        List<ExploitationAcceptActAAMFile> list52 = source.getExAcceptActAAMFVIds();
        if (list52 != null) {
            projectDetailsSimple.setExAcceptActAAMFVIds(new ArrayList<ExploitationAcceptActAAMFile>(list52));
        }
        List<ExCalculationPlannedAmountFile> list5211 = source.getExCalculationPlannedAmountFVIds();
        if (list52 != null) {
            projectDetailsSimple.setExCalculationPlannedAmountFVIds(new ArrayList<>(list5211));
        }
        projectDetailsSimple.setExInvestStagePlanDate(source.getExInvestStagePlanDate());
        projectDetailsSimple.setExInvestStageFactDate(source.getExInvestStageFactDate());
        projectDetailsSimple.setExGrantorExpenses(source.getExGrantorExpenses());
        projectDetailsSimple.setExFormulasOrIndexingOrderEstablished(source.getExFormulasOrIndexingOrderEstablished());

        projectDetailsSimple.setCert(source.getCert());
        projectDetailsSimple.setSignature(source.getSignature());
        projectDetailsSimple.setSnils(source.getSnils());
        List<Event> list29 = source.getAdsEvents();
        if (list29 != null) {
            projectDetailsSimple.setAdsEvents(new ArrayList<Event>(list29));
        }
        projectDetailsSimple.setAdsIsThirdPartyOrgsProvided(source.getAdsIsThirdPartyOrgsProvided());

        projectDetailsSimple.setAdsIsRegInvestmentProject(source.getAdsIsRegInvestmentProject());
        projectDetailsSimple.setAdsIsTreasurySupport(source.getAdsIsTreasurySupport());

        projectDetailsSimple.setAdsHasIncomeTax(source.getAdsHasIncomeTax());
        List<InvestmentInObjectMainIndicator> investmentInObjectMainIndicatorList = source.getFeiInvestmentsInObject();
        if (investmentInObjectMainIndicatorList != null) {
            projectDetailsSimple.setFeiInvestmentsInObject(new ArrayList<InvestmentInObjectMainIndicator>(investmentInObjectMainIndicatorList));
        }

        List<OperationalCostsIndicator> operationalCostsIndicatorList = source.getFeiOperationalCosts();
        if (operationalCostsIndicatorList != null) {
            projectDetailsSimple.setFeiOperationalCosts(new ArrayList<OperationalCostsIndicator>(operationalCostsIndicatorList));
        }
        List<TaxConditionIndicator> taxConditionIndicatorList = source.getFeiTaxCondition();
        if (taxConditionIndicatorList != null) {
            projectDetailsSimple.setFeiTaxCondition(new ArrayList<TaxConditionIndicator>(taxConditionIndicatorList));
        }
        List<RevenueServiceIndicator> revenueServiceIndicatorList = source.getFeiRevenue();
        if (revenueServiceIndicatorList != null) {
            projectDetailsSimple.setFeiRevenueService(new ArrayList<RevenueServiceIndicator>(revenueServiceIndicatorList));
        }
        projectDetailsSimple.setFeiTaxIncentivesExist(source.getFeiTaxIncentivesExist());
        projectDetailsSimple.setFeiResidualValue(source.getFeiResidualValue());
        projectDetailsSimple.setFeiAverageServiceLife(source.getFeiAverageServiceLife());
        projectDetailsSimple.setFeiForecastValuesDate(source.getFeiForecastValuesDate());
        projectDetailsSimple.setAdsIncomeTaxRate(source.getAdsIncomeTaxRate());
        projectDetailsSimple.setAdsHasLandTax(source.getAdsHasLandTax());
        projectDetailsSimple.setAdsLandTaxRate(source.getAdsLandTaxRate());
        projectDetailsSimple.setAdsHasPropertyTax(source.getAdsHasPropertyTax());
        projectDetailsSimple.setAdsPropertyTaxRate(source.getAdsPropertyTaxRate());
        projectDetailsSimple.setAdsHasBenefitClarificationTax(source.getAdsHasBenefitClarificationTax());
        projectDetailsSimple.setAdsBenefitClarificationRate(source.getAdsBenefitClarificationRate());
        projectDetailsSimple.setAdsBenefitDescription(source.getAdsBenefitDescription());

        List<AdsDecisionTextFile> list151 = source.getAdsDecisionTextFileId();
        if (list151 != null) {
            projectDetailsSimple.setAdsDecisionTextFileId(new ArrayList<AdsDecisionTextFile>(list151));
        }

        List<PrivatePartnerThirdPartyOrg> list30 = source.getAdsPrivatePartnerThirdPartyOrgs();
        if (list30 != null) {
            projectDetailsSimple.setAdsPrivatePartnerThirdPartyOrgs(new ArrayList<PrivatePartnerThirdPartyOrg>(list30));
        }
        for (PrivatePartnerThirdPartyOrg partyOrg : projectDetailsSimple.getAdsPrivatePartnerThirdPartyOrgs()) {
            partyOrg.setWorkTypesIds(partyOrg.getWorkTypes().stream().map(i -> i.getId()).collect(Collectors.toList()));
        }


        List<PublicPartnerThirdPartyOrg> list31 = source.getAdsPublicPartnerThirdPartyOrgs();
        if (list31 != null) {
            projectDetailsSimple.setAdsPublicPartnerThirdPartyOrgs(new ArrayList<PublicPartnerThirdPartyOrg>(list31));
        }
        for (PublicPartnerThirdPartyOrg partyOrg : projectDetailsSimple.getAdsPublicPartnerThirdPartyOrgs()) {
            partyOrg.setWorkTypesIds(partyOrg.getWorkTypes().stream().map(i -> i.getId()).collect(Collectors.toList()));
        }


        projectDetailsSimple.setAdsNpv(source.getAdsNpv());
        projectDetailsSimple.setAdsIrr(source.getAdsIrr());
        projectDetailsSimple.setAdsPb(source.getAdsPb());
        projectDetailsSimple.setAdsPbDiscounted(source.getAdsPbDiscounted());
        projectDetailsSimple.setAdsEbidta(source.getAdsEbidta());
        projectDetailsSimple.setAdsWacc(source.getAdsWacc());
        projectDetailsSimple.setAdsTaxRelief(source.getAdsTaxRelief());
        List<Sanction> list32 = source.getAdsSanctions();
        if (list32 != null) {
            projectDetailsSimple.setAdsSanctions(new ArrayList<Sanction>(list32));
        }
        List<JudicialActivity> list33 = source.getAdsJudicialActivities();
        if (list33 != null) {
            projectDetailsSimple.setAdsJudicialActivities(new ArrayList<JudicialActivity>(list33));
        }
        projectDetailsSimple.setAdsConcessionaireOpf(source.getAdsConcessionaireOpf());
        projectDetailsSimple.setAdsConcessionaireName(source.getAdsConcessionaireName());
        projectDetailsSimple.setAdsConcessionaireInn(source.getAdsConcessionaireInn());
        projectDetailsSimple.setAdsConcessionaireRegime(source.getAdsConcessionaireRegime());
        projectDetailsSimple.setAdsConcessionaireCreditRating(source.getAdsConcessionaireCreditRating());
        projectDetailsSimple.setAdsConcessionaireCreditRatingStartDate(source.getAdsConcessionaireCreditRatingStartDate());
        projectDetailsSimple.setAdsConcessionaireCreditRatingEndDate(source.getAdsConcessionaireCreditRatingEndDate());
        projectDetailsSimple.setAdsConcessionaireCreditRatingAgency(source.getAdsConcessionaireCreditRatingAgency());
        List<OwnershipStructure> list34 = source.getAdsOwnershipStructures();
        if (list34 != null) {
            projectDetailsSimple.setAdsOwnershipStructures(new ArrayList<OwnershipStructure>(list34));
        }
        List<FinancialStructure> list35 = source.getAdsFinancialStructure();
        if (list35 != null) {
            projectDetailsSimple.setAdsFinancialStructure(new ArrayList<FinancialStructure>(list35));
        }
        List<InvestmentsCriteriaBoolean> list36 = source.getAdsInvestmentBoolCriterias();
        if (list36 != null) {
            projectDetailsSimple.setAdsInvestmentBoolCriterias(new ArrayList<InvestmentsCriteriaBoolean>(list36));
        }
        projectDetailsSimple.setAdsUnforeseenExpencesShare(source.getAdsUnforeseenExpencesShare());
        projectDetailsSimple.setAdsUnforeseenExpencesShareComment(source.getAdsUnforeseenExpencesShareComment());
        projectDetailsSimple.setAdsWorkPlacesCount(source.getAdsWorkPlacesCount());

        List<CompetitionCriterion> list37 = source.getAdsCompetitionCriteria();
        if (source.getAdsCompetitionCriteria() != null) {
            projectDetailsSimple.setAdsCompetitionCriteria(list37.stream().mapToLong(i -> i.getId()).boxed().collect(Collectors.toList()));
        }

        List<FinRequirement> list38 = source.getAdsFinancialRequirement();
        if (list38 != null) {
            projectDetailsSimple.setAdsFinancialRequirement(list38.stream().mapToLong(i -> i.getId()).boxed().collect(Collectors.toList()));
        }

        List<NoFinRequirement> list39 = source.getAdsNonFinancialRequirements();
        if (list39 != null) {
            projectDetailsSimple.setAdsNonFinancialRequirements(list39.stream().mapToLong(i -> i.getId()).boxed().collect(Collectors.toList()));
        }

        projectDetailsSimple.setCmComment(source.getCmComment());
        projectDetailsSimple.setPublished(source.getPublished());
        projectDetailsSimple.setCmContacts(source.getCmContacts());
        projectDetailsSimple.setUpdateDate(source.getUpdateDate());
        projectDetailsSimple.setUpdateUserId(source.getUpdateUserId());
        projectDetailsSimple.setCreateDate(source.getCreateDate());
        projectDetailsSimple.setCreateUserId(source.getCreateUserId());
        projectDetailsSimple.setVersion(source.getVersion());
        projectDetailsSimple.setSaveIndex(source.getSaveIndex() + 1);

        projectDetailsSimple.setCrAgreementComplex(source.getCrAgreementComplex());
        projectDetailsSimple.setCrJobDoneTerm(source.getCrJobDoneTerm());
        projectDetailsSimple.setCrSavingStartDate(source.getCrSavingStartDate());
        projectDetailsSimple.setCrSavingEndDate(source.getCrSavingEndDate());
        projectDetailsSimple.setCrInvestmentStageTerm(source.getCrInvestmentStageTerm());
        projectDetailsSimple.setCrIsAutoProlongationProvided(source.getCrIsAutoProlongationProvided());
        projectDetailsSimple.setCrAgreementEndDateAfterProlongation(source.getCrAgreementEndDateAfterProlongation());
        projectDetailsSimple.setCrAgreementValidityAfterProlongation(source.getCrAgreementValidityAfterProlongation());
        List<CreationAgreementFile> list42 = source.getCrAgreementTextFiles();
        if (list42 != null) {
            projectDetailsSimple.setCrAgreementTextFiles(new ArrayList<CreationAgreementFile>(list42));
        }
        projectDetailsSimple.setCrOpf(source.getCrOpf());
        projectDetailsSimple.setCrIsForeignInvestor(source.getCrIsForeignInvestor());
        projectDetailsSimple.setCrIsMcpSubject(source.getCrIsMcpSubject());
        projectDetailsSimple.setGiPublicPartnerOgrn(source.getGiPublicPartnerOgrn());

        projectDetailsSimple.setCbcMinimumGuaranteedExist(source.getCbcMinimumGuaranteedExist());
        projectDetailsSimple.setCbcProjectBudgetObligationMissing(source.getCbcProjectBudgetObligationMissing());
        projectDetailsSimple.setCbcMinimumGuaranteedClauseAgreement(source.getCbcMinimumGuaranteedClauseAgreement());
        projectDetailsSimple.setCbcCompensationMinimumGuaranteedExist(source.getCbcCompensationMinimumGuaranteedExist());
        projectDetailsSimple.setCbcNonPaymentConsumersGoodsProvidedExist(source.getCbcNonPaymentConsumersGoodsProvidedExist());
        projectDetailsSimple.setCbcLimitNonPaymentConsumersGoodsProvidedExist(source.getCbcLimitNonPaymentConsumersGoodsProvidedExist());
        projectDetailsSimple.setCbcNonPaymentConsumersGoodsProvidedClauseAgreement(source.getCbcNonPaymentConsumersGoodsProvidedClauseAgreement());
        projectDetailsSimple.setCbcLimitNonPayConsGoodsProvidedClauseAgree(source.getCbcLimitNonPaymentConsumersGoodsProvidedClauseAgreement());
        projectDetailsSimple.setCbcCompLimitNonPaymentConsGoodsProvidedExist(source.getCbcCompensationLimitNonPaymentConsumersGoodsProvidedExist());
        projectDetailsSimple.setCbcArisingProvisionOfBenefitsExist(source.getCbcArisingProvisionOfBenefitsExist());
        projectDetailsSimple.setCbcArisingProvisionOfBenefitsClauseAgreement(source.getCbcArisingProvisionOfBenefitsClauseAgreement());
        projectDetailsSimple.setCbcCompensationArisingProvisionOfBenefitsExist(source.getCbcCompensationArisingProvisionOfBenefitsExist());
        projectDetailsSimple.setCbcCompArisingProvisionOfBenefitsClauseAgree(source.getCbcCompensationArisingProvisionOfBenefitsClauseAgreement());
        projectDetailsSimple.setCbcDueToOnsetOfCertainCircumstancesExist(source.getCbcDueToOnsetOfCertainCircumstancesExist());
        projectDetailsSimple.setCbcDueToOnsetOfCertainCircumstancesClauseAgreement(source.getCbcDueToOnsetOfCertainCircumstancesClauseAgreement());
        projectDetailsSimple.setCbcLimitCompAdditionalCostsAgreeExist(source.getCbcLimitCompensationAdditionalCostsAgreementExist());
        projectDetailsSimple.setCbcLimitCompensationAdditionalClauseAgreement(source.getCbcLimitCompensationAdditionalClauseAgreement());
        projectDetailsSimple.setCbcSpecifyOtherCircumstancesPrepare(source.getCbcSpecifyOtherCircumstancesPrepare());
        projectDetailsSimple.setCbcSpecifyOtherCircumstancesBuild(source.getCbcSpecifyOtherCircumstancesBuild());
        projectDetailsSimple.setCbcSpecifyOtherCircumstancesExploitation(source.getCbcSpecifyOtherCircumstancesExploitation());
        projectDetailsSimple.setCbcCompensationAdditionalCostsAgreementExist(source.getCbcCompensationAdditionalCostsAgreementExist());

        List<ArisingProvisionOfBenefitFile> list1111 = source.getCbcArisingProvisionOfBenefitFVId();
        if (list1111 != null) {
            projectDetailsSimple.setCbcArisingProvisionOfBenefitFVId(new ArrayList<ArisingProvisionOfBenefitFile>(list1111));
        }
        List<CompensationAdditionalCostsAgreementFile> list1112 = source.getCbcCompensationAdditionalCostsAgreementFVId();
        if (list1112 != null) {
            projectDetailsSimple.setCbcCompensationAdditionalCostsAgreementFVId(new ArrayList<CompensationAdditionalCostsAgreementFile>(list1112));
        }
        List<CompensationArisingProvisionOfBenefitsFile> list1113 = source.getCbcCompensationArisingProvisionOfBenefitsFVId();
        if (list1113 != null) {
            projectDetailsSimple.setCbcCompensationArisingProvisionOfBenefitsFVId(new ArrayList<CompensationArisingProvisionOfBenefitsFile>(list1113));
        }
        List<MinimumGuaranteedFile> list1114 = source.getCbcMinimumGuaranteedFVId();
        if (list1114 != null) {
            projectDetailsSimple.setCbcMinimumGuaranteedFVId(new ArrayList<MinimumGuaranteedFile>(list1114));
        }
        List<NonPaymentConsumersFile> list1115 = source.getCbcNonPaymentConsumersFVId();
        if (list1115 != null) {
            projectDetailsSimple.setCbcNonPaymentConsumersFVId(new ArrayList<NonPaymentConsumersFile>(list1115));
        }

        List<Long> list1116 = source.getCbcNameOfCircumstanceAdditionalCostPrepare();
        if (list1116 != null) {
            projectDetailsSimple.setCbcNameOfCircumstanceAdditionalCostPrepare(new ArrayList<>(list1116));
        }
        List<Long> list1117 = source.getCbcNameOfCircumstanceAdditionalCostBuild();
        if (list1117 != null) {
            projectDetailsSimple.setCbcNameOfCircumstanceAdditionalCostBuild(new ArrayList<>(list1117));
        }
        List<Long> list1118 = source.getCbcNameOfCircumstanceAdditionalCostExploitation();
        if (list1118 != null) {
            projectDetailsSimple.setCbcNameOfCircumstanceAdditionalCostExploitation(new ArrayList<>(list1118));
        }
        projectDetailsSimple.setCbcMinimumGuaranteedIncomeForm(source.getCbcMinimumGuaranteedIncomeForm());
        projectDetailsSimple.setCbcNonPaymentConsumersGoodsProvidedForm(source.getCbcNonPaymentConsumersGoodsProvidedForm());

        List<SimpleYearIndicator> listCbc3 = source.getCbcMinimumGuaranteedAmount();
        if (listCbc3 != null) {
            projectDetailsSimple.setCbcMinimumGuaranteedAmount(new ArrayList<>(listCbc3));
        }

        projectDetailsSimple.setCbcMinimumGuaranteedAmountNdsCheck(source.getCbcMinimumGuaranteedAmountNdsCheck());
        projectDetailsSimple.setCbcMinimumGuaranteedAmountDateField(source.getCbcMinimumGuaranteedAmountDateField());
        projectDetailsSimple.setCbcMinimumGuaranteedAmountMeasureType(source.getCbcMinimumGuaranteedAmountMeasureType());

        List<SimpleYearIndicator> listCbc7 = source.getCbcCompensationMinimumGuaranteedAmount();
        if (listCbc7 != null) {
            projectDetailsSimple.setCbcCompensationMinimumGuaranteedAmount(new ArrayList<>(listCbc7));
        }
        List<SimpleYearIndicator> listCbc14 = source.getCbcCompensationLimitNonPaymentAmount();
        if (listCbc14 != null) {
            projectDetailsSimple.setCbcCompensationLimitNonPaymentAmount(new ArrayList<>(listCbc14));
        }

        List<SimpleYearIndicator> listCbc18 = source.getCbcCompensationArisingProvisionOfBenefitsAmount();
        if (listCbc18 != null) {
            projectDetailsSimple.setCbcCompensationArisingProvisionOfBenefitsAmount(new ArrayList<>(listCbc18));
        }
        List<CircumstanceStageIndicator> listCbc23 = source.getCbcLimitCompensationAdditionalCostsAmount();
        if (listCbc23 != null) {
            projectDetailsSimple.setCbcLimitCompensationAdditionalCostsAmount(new ArrayList<>(listCbc23));
        }

        projectDetailsSimple.setCbcNdsCheck(source.getCbcNdsCheck());
        projectDetailsSimple.setIoNdsCheck(source.getIoNdsCheck());
        projectDetailsSimple.setCbcDateField(source.getCbcDateField());
        projectDetailsSimple.setIoDateField(source.getIoDateField());
        projectDetailsSimple.setCbcMeasureType(source.getCbcMeasureType());
        projectDetailsSimple.setIoMeasureType(source.getIoMeasureType());

        List<CircumstanceStageIndicator> listCbc27 = source.getCbcCircumstancesAdditionalCostsAmount();
        if (listCbc27 != null) {
            projectDetailsSimple.setCbcCircumstancesAdditionalCostsAmount(new ArrayList<>(listCbc27));
        }
        return projectDetailsSimple;
    }
}
