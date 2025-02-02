package ru.ibs.gasu.gchp.ws;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.ibs.gasu.dictionaries.DictCache;
import ru.ibs.gasu.dictionaries.Dictionary;
import ru.ibs.gasu.gchp.user.UserContext;
import ru.ibs.gasu.gchp.domain.*;
import ru.ibs.gasu.gchp.entities.*;
import ru.ibs.gasu.gchp.repositories.ProjectRepository;
import ru.ibs.gasu.gchp.repositories.ProjectExcelProjectionRepository;
import ru.ibs.gasu.gchp.service.excel.ExcelService;
import ru.ibs.gasu.gchp.service.ProjectMapper;
import ru.ibs.gasu.gchp.service.rev.ProjectRevisionService;
import ru.ibs.gasu.gchp.service.role.RolePreferencesService;
import ru.ibs.gasu.gchp.service.role.SvrOrg;
import ru.ibs.gasu.gchp.service.role.SvrOrgService;
import ru.ibs.gasu.gchp.util.Utils;
import ru.ibs.gasu.soap.generated.dictionary.DictionaryDataRecordDescriptor;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.springframework.util.ObjectUtils.isEmpty;
import static ru.ibs.gasu.gchp.specifications.ProjectSpecification.filterByCriteria;
import static ru.ibs.gasu.gchp.util.Utils.opt;

@WebService
@Slf4j
public class GchpDocumentsWebServiceImpl implements GchpDocumentsWebService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectRevisionService revisionService;

    @Autowired
    private RolePreferencesService rolePreferencesService;

    @Autowired
    private SvrOrgService svrOrgService;

    @Resource
    private DictCache dictCache;

    @Resource
    private ProjectMapper mapper;

    @Autowired
    private ProjectExcelProjectionRepository projectRepository2;

    @Autowired
    private ExcelService excelService;

    @Override
    @Transactional
    public ProjectDetailDTO saveGchpDocument(ProjectDetailDTO project) {

        log.info("FIELD adsWorkPlacesCount:" + project.getAdsWorkPlacesCount());
        Project map = mapper.transform(project);

        log.info("FIELD ENTITY adsWorkPlacesCount:" + map.getAdsWorkPlacesCount());

        if (map.getGiAlwaysDraftStatus()) {
            map.setPublished(false);
        }

        map.getOdRentPassportFileVersionId().forEach(i -> i.setProject(map));
        map.getPpProjectAgreementFileVersionId().forEach(i -> i.setProject(map));
        map.getPpActTextFileVersionId().forEach(i -> i.setProject(map));
        map.getPpLeaseAgreementTextFileVersionId().forEach(i -> i.setProject(map));
        map.getPpDecisionTextFileVersionId().forEach(i -> i.setProject(map));
        map.getPpConclusionUOTextFileVersionId().forEach(i -> i.setProject(map));
        map.getPpFinancialModelTextFileVersionId().forEach(i -> i.setProject(map));
        map.getPpProposalTextFileVersionId().forEach(i -> i.setProject(map));
        map.getPpProtocolFileVersionId().forEach(i -> i.setProject(map));
        map.getPpConcludeAgreementFvId().forEach(i -> i.setProject(map));
        map.getPpCompetitionResultsProtocolTextFvId().forEach(i -> i.setProject(map));
        map.getPpCompetitionResultsDocFvId().forEach(i -> i.setProject(map));
        map.getPpFinancialModelFVId().forEach(i -> i.setProject(map));
        map.getPpSupportingDocumentsFileVersionIds().forEach(i -> i.setProject(map));
        map.getTmSupportingDocuments().forEach(i -> i.setProject(map));
        map.getCrAgreementTextFvId().forEach(i -> i.setProject(map));
        map.getCrFinancialClosingActFVId().forEach(i -> i.setProject(map));
        map.getCrFirstObjectCompleteActFVId().forEach(i -> i.setProject(map));
        map.getCrLastObjectCompleteActFVId().forEach(i -> i.setProject(map));
        map.getCrInvestmentVolumeStagOfCreationActFVId().forEach(i -> i.setProject(map));
        map.getCrActFVId().forEach(i -> i.setProject(map));
        map.getCrReferenceFVId().forEach(i -> i.setProject(map));
        map.getCrLandActFVId().forEach(i -> i.setProject(map));
        map.getCrConfirmationDocFVId().forEach(i -> i.setProject(map));
        map.getCrCalcInvestCostsActFVId().forEach(i -> i.setProject(map));
        map.getExLastObjectActFVId().forEach(i -> i.setProject(map));
        map.getExInvestmentVolumeStagOfExploitationActFVId().forEach(i -> i.setProject(map));
        map.getExInvestmentRecoveryFinancialModelFileVersionId().forEach(i -> i.setProject(map));
        map.getExFinModelFVIds().forEach(i -> i.setProject(map));
        map.getExSupportDocFVIds().forEach(i -> i.setProject(map));
        map.getExSupportCompensDocFVIds().forEach(i -> i.setProject(map));
        map.getExAgreementFVIds().forEach(i -> i.setProject(map));
        map.getExAcceptActFVIds().forEach(i -> i.setProject(map));
        map.getExAcceptActAAMFVIds().forEach(i -> i.setProject(map));
        map.getExCalculationPlannedAmountFVIds().forEach(i -> i.setProject(map));
        map.getTmTextFileVersionId().forEach(i -> i.setProject(map));
        map.getTmTaActTextFileVersionId().forEach(i -> i.setProject(map));
        map.getTmCompensationFVIds().forEach(i -> i.setProject(map));
        map.getCcTextFileVersionId().forEach(i -> i.setProject(map));
        map.getPpCompetitionTextFVId().forEach(i -> i.setProject(map));
        map.getCrAgreementTextFiles().forEach(i -> i.setProject(map));
        map.getAdsDecisionTextFileId().forEach(i -> i.setProject(map));
        map.getGiCompletedTemplateTextFVId().forEach(i -> i.setProject(map));

        if (map.getPpInvestmentPlanningAmount() != null) {
            map.getPpInvestmentPlanningAmount().setProject(map);
            for (PlanInvestmentIndicator indicator : map.getPpInvestmentPlanningAmount().getIndicators()) {
                indicator.setInvestments(map.getPpInvestmentPlanningAmount());
                for (PlanInvestmentIndicatorYearValue valuesByYear : indicator.getValuesByYears()) {
                    valuesByYear.setInvestmentIndicator(indicator);
                }
            }
        }

        if (map.getPpCreationInvestmentPlanningAmount() != null) {
            map.getPpCreationInvestmentPlanningAmount().setProject(map);
            for (PlanCreationInvestmentIndicator indicator : map.getPpCreationInvestmentPlanningAmount().getIndicators()) {
                indicator.setInvestments(map.getPpCreationInvestmentPlanningAmount());
                for (PlanCreationInvestmentIndicatorYearValue valuesByYear : indicator.getValuesByYears()) {
                    valuesByYear.setInvestmentIndicator(indicator);
                }
            }
        }

        if (map.getCbcInvestments1() != null) {
            map.getCbcInvestments1().setProject(map);
            for (CbcInvestments1Indicator indicator : map.getCbcInvestments1().getIndicators()) {
                indicator.setInvestments(map.getCbcInvestments1());
                for (CbcInvestments1IndicatorYearValue valuesByYear : indicator.getValuesByYears()) {
                    valuesByYear.setInvestmentIndicator(indicator);
                }
            }
        }

        if (map.getCbcInvestments2() != null) {
            map.getCbcInvestments2().setProject(map);
            for (CbcInvestments2Indicator indicator : map.getCbcInvestments2().getIndicators()) {
                indicator.setInvestments(map.getCbcInvestments2());
                for (CbcInvestments2IndicatorYearValue valuesByYear : indicator.getValuesByYears()) {
                    valuesByYear.setInvestmentIndicator(indicator);
                }
            }
        }

        if (map.getCbcInvestments3() != null) {
            map.getCbcInvestments3().setProject(map);
            for (CbcInvestments3Indicator indicator : map.getCbcInvestments3().getIndicators()) {
                indicator.setInvestments(map.getCbcInvestments3());
                for (CbcInvestments3IndicatorYearValue valuesByYear : indicator.getValuesByYears()) {
                    valuesByYear.setInvestmentIndicator(indicator);
                }
            }
        }

        if (map.getCbcInvestments4() != null) {
            map.getCbcInvestments4().setProject(map);
            for (CbcInvestments4Indicator indicator : map.getCbcInvestments4().getIndicators()) {
                indicator.setInvestments(map.getCbcInvestments4());
                for (CbcInvestments4IndicatorYearValue valuesByYear : indicator.getValuesByYears()) {
                    valuesByYear.setInvestmentIndicator(indicator);
                }
            }
        }

        if (map.getRemainingDebt() != null) {
            map.getRemainingDebt().setProject(map);
            for (RemainingDebtIndicator indicator : map.getRemainingDebt().getIndicators()) {
                indicator.setInvestments(map.getRemainingDebt());
                for (RemainingDebtIndicatorYearValue valuesByYear : indicator.getValuesByYears()) {
                    valuesByYear.setInvestmentIndicator(indicator);
                }
            }
        }

        if (map.getCrInvestmentCreationAmount() != null) {
            map.getCrInvestmentCreationAmount().setProject(map);
            for (CreationInvestmentIndicator indicator : map.getCrInvestmentCreationAmount().getIndicators()) {
                indicator.setInvestments(map.getCrInvestmentCreationAmount());
                for (CreationInvestmentIndicatorYearValue valuesByYear : indicator.getValuesByYears()) {
                    valuesByYear.setInvestmentIndicator(indicator);
                }
            }
        }

        if (map.getExInvestmentExploitationAmount() != null) {
            map.getExInvestmentExploitationAmount().setProject(map);
            for (ExploitationInvestmentIndicator indicator : map.getExInvestmentExploitationAmount().getIndicators()) {
                indicator.setInvestments(map.getExInvestmentExploitationAmount());
                for (ExploitationInvestmentIndicatorYearValue valuesByYear : indicator.getValuesByYears()) {
                    valuesByYear.setInvestmentIndicator(indicator);
                }
            }
        }

        if (map.getExInvestmentExploitationRecoveryAmount() != null) {
            map.getExInvestmentExploitationRecoveryAmount().setProject(map);
            for (ExploitationInvestmentRecoveryIndicator indicator : map.getExInvestmentExploitationRecoveryAmount().getIndicators()) {
                indicator.setInvestments(map.getExInvestmentExploitationRecoveryAmount());
                for (ExploitationInvestmentRecoveryIndicatorYearValue valuesByYear : indicator.getValuesByYears()) {
                    valuesByYear.setInvestmentIndicator(indicator);
                }
            }
        }

        map.getOdEnergyEfficiencyPlans().forEach(i -> i.setProject(map));
        map.getCrBalanceOfDebt().forEach(i -> i.setProject(map));

        for(TechEconomicsObjectIndicator object : map.getOdTechEconomicsObjectIndicators()) {
            object.setProject(map);
            for (TechEconomicsIndicator techEconomicsIndicator : object.getTechEconomicsIndicators()) {
                techEconomicsIndicator.setObject(object);
                for (TechEconomicsIndicatorYearValue value : techEconomicsIndicator.getYearValues()) {
                    value.setEconomicsIndicator(techEconomicsIndicator);
                }
            }
        }

        if (map.getExPayment() != null) {
            map.getExPayment().setProject(map);
            for (ExploitationPaymentByYears year : map.getExPayment().getValuesByYears()) {
                year.setPayment(map.getExPayment());
            }
        }

        if (map.getCrBankGuaranteeByYears() != null) {
            map.getCrBankGuaranteeByYears().setProject(map);
            for (BankGuaranteeByYears year : map.getCrBankGuaranteeByYears().getValuesByYears()) {
                year.setPayment(map.getCrBankGuaranteeByYears());
            }
        }

        if (map.getExBankGuaranteeByYears() != null) {
            map.getExBankGuaranteeByYears().setProject(map);
            for (ExBankGuaranteeByYears year : map.getExBankGuaranteeByYears().getValuesByYears()) {
                year.setPayment(map.getExBankGuaranteeByYears());
            }
        }

        if (map.getExCompensation() != null) {
            map.getExCompensation().setProject(map);
            for (ExploitationCompensationByYears year : map.getExCompensation().getValuesByYears()) {
                year.setCompensation(map.getExCompensation());
            }
        }

        for (InvestmentInObjectMainIndicator investmentInObjectMainIndicator : map.getFeiInvestmentsInObject()) {
            for(InvestmentInObject object : investmentInObjectMainIndicator.getObjects()) {
                for (InvestmentInObjectIndicator indicator : object.getIndicators()) {
                    for(InvestmentInObjectIndicatorYearValue year : indicator.getYearValues()) {
                        year.setObjectIndicator(indicator);
                    }
                    indicator.setObject(object);
                }
                object.setMainObjectIndicator(investmentInObjectMainIndicator);
            }
            investmentInObjectMainIndicator.setProject(map);
        }

        for (OperationalCostsIndicator operationalCostsIndicator : map.getFeiOperationalCosts()) {
            for(OperationalCostsIndicatorYearValue year : operationalCostsIndicator.getYears()) {
                year.setOperationalCostsIndicator(operationalCostsIndicator);
            }
            operationalCostsIndicator.setProject(map);
        }

        for (TaxConditionIndicator taxConditionIndicator : map.getFeiTaxCondition()) {
            for(TaxConditionIndicatorYearValue year : taxConditionIndicator.getYearValues()) {
                year.setTaxConditionIndicator(taxConditionIndicator);
            }
            taxConditionIndicator.setProject(map);
        }

        for (RevenueServiceIndicator revenueServiceIndicator : map.getFeiRevenueService()) {
            for(RevenueIndicator revenueIndicator : revenueServiceIndicator.getIndicators()) {
                for(RevenueIndicatorYearValue year : revenueIndicator.getYearValues()) {
                    year.setRevenueIndicator(revenueIndicator);
                }
                revenueIndicator.setRevenueService(revenueServiceIndicator);
            }
            revenueServiceIndicator.setProject(map);
        }

        map.getAdsEvents().forEach(i -> i.setProject(map));
        map.getAdsPrivatePartnerThirdPartyOrgs().forEach(i -> i.setProject(map));
        map.getAdsPublicPartnerThirdPartyOrgs().forEach(i -> i.setProject(map));
        map.getAdsSanctions().forEach(i -> i.setProject(map));

        map.getAdsJudicialActivities().forEach(i -> i.setProject(map));
        map.getAdsOwnershipStructures().forEach(i -> i.setProject(map));
        map.getAdsFinancialStructure().forEach(i -> i.setProject(map));
        map.getTmCompositionOfCompensationView().forEach(i -> i.setProject(map));
        map.getAdsInvestmentBoolCriterias().forEach(i -> i.setProject(map));
        map.getCrEnsureMethods().forEach(i -> i.setProject(map));
        map.getExEnsureMethods().forEach(i -> i.setProject(map));

        map.getCbcArisingProvisionOfBenefitFVId().forEach(i -> i.setProject(map));
        map.getCbcCompensationAdditionalCostsAgreementFVId().forEach(i -> i.setProject(map));
        map.getCbcCompensationArisingProvisionOfBenefitsFVId().forEach(i -> i.setProject(map));
        map.getCbcMinimumGuaranteedFVId().forEach(i -> i.setProject(map));
        map.getCbcNonPaymentConsumersFVId().forEach(i -> i.setProject(map));

        map.getCbcMinimumGuaranteedAmount().forEach(i -> {
            i.setProject(map);
            i.setIndicator("cbcMinimumGuaranteedAmount");
        });
        map.getCbcCompensationMinimumGuaranteedAmount().forEach(i -> {
            i.setProject(map);
            i.setIndicator("cbcCompensationMinimumGuaranteedAmount");
        });
        map.getCbcCompensationLimitNonPaymentAmount().forEach(i -> {
            i.setProject(map);
            i.setIndicator("cbcCompensationLimitNonPaymentAmount");
        });
        map.getCbcCompensationArisingProvisionOfBenefitsAmount().forEach(i -> {
            i.setProject(map);
            i.setIndicator("cbcCompensationArisingProvisionOfBenefitsAmount");
        });
        map.getCbcLimitCompensationAdditionalCostsAmount().forEach(i -> {
            i.setProject(map);
            i.setParentField("cbcLimitCompensationAdditionalCostsAmount");
        });

        for (CircumstanceStageIndicator circumstanceStageIndicator : map.getCbcCircumstancesAdditionalCostsAmount()) {
            for(CircumstanceIndicator circumstanceIndicator : circumstanceStageIndicator.getCircumstances()) {
                circumstanceIndicator.setStage(circumstanceStageIndicator);
            }
            circumstanceStageIndicator.setProject(map);
            circumstanceStageIndicator.setParentField("cbcCircumstancesAdditionalCostsAmount");
        }

        Project savedProject = projectRepository.save(map);

        return mapper.transform(savedProject);
    }

    @Transactional
    public void softDeleteGchpDocumentById(Long id) {
        Project p = projectRepository.getOne(id);
        p.setObsolete(true);
        projectRepository.save(p);
    }

    @Override
    public List<ExportTask> runExcelFileGeneration(DocumentsFilterPaginateCriteria criteria) {
        // TODO check max tasks per user
        Long userId = getNotNullCurrentUser();
        UUID uuid = UUID.randomUUID();

        try {
            excelService.getTasks().putIfAbsent(userId, new ArrayList<>());
            ExportTask currentTask = new ExportTask(uuid);
            Future<String> export = excelService.export(criteria, currentTask);
            currentTask.setTask(export);
            excelService.getTasks().get(userId).add(currentTask);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        return excelService.getTasks().get(userId);
    }

    @Override
    public List<ExportTask> clearUserDoneTasks() {
        Long currentUser = getNotNullCurrentUser();
        excelService.getTasks().get(currentUser).removeIf(ExportTask::isDone);
        return excelService.getTasks().get(currentUser);
    }

    @Override
    public String getFileByUuid(String uuid) throws InterruptedException, ExecutionException {
        Long currentUser = getNotNullCurrentUser();
        for (ExportTask task : excelService.getTasks().get(currentUser)) {
            if (task.getUuid().compareTo(UUID.fromString(uuid)) == 0 && task.getStatus().equals("Завершено")) {
                return (String)task.getTask().get();
            }
        }
        return "";
    }

    @Override
    public String getOdsFileByUuid(String uuid) {
        Long currentUser = getNotNullCurrentUser();
        for (ExportTask task : excelService.getTasks().get(currentUser)) {
            if (task.getUuid().compareTo(UUID.fromString(uuid)) == 0 && task.getStatus().equals("Завершено")) {
                return Base64.getEncoder().encodeToString(task.getOds());
            }
        }
        return "";
    }

    /**
     * @return userId or -1 if user is not defined
     */
    private Long getNotNullCurrentUser() {
        if (UserContext.get().getUserId() == null)
            return -1L;
        return UserContext.get().getUserId();
    }

    @Override
    public List<ExportTask> getAllTasksStatus() {
        Long currentUser = getNotNullCurrentUser();
        return excelService.getTasks().get(currentUser);
    }

    @Transactional
    public ProjectsResult paginateAndFilterDocuments(DocumentsFilterPaginateCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getLimit(), Utils.createSort(criteria.getSortInfo()));
        Page<Project> res = projectRepository.findAll(filterByCriteria(criteria), pageable);
        List<ProjectDetailDTO> collect = res.stream().map(mapper::transform).collect(Collectors.toList());

        for (Project re : res) {
            if (re.getPpCreationInvestmentPlanningAmount() != null) {
                Hibernate.initialize(re.getPpCreationInvestmentPlanningAmount().getIndicators());
                for (PlanCreationInvestmentIndicator indicator : re.getPpCreationInvestmentPlanningAmount().getIndicators()) {
                    if (indicator.getValuesByYears() != null)
                        Hibernate.initialize(indicator.getValuesByYears());
                }
            }

            if (re.getCbcInvestments1() != null) {
                Hibernate.initialize(re.getCbcInvestments1().getIndicators());
                for (CbcInvestments1Indicator indicator : re.getCbcInvestments1().getIndicators()) {
                    if (indicator.getValuesByYears() != null)
                        Hibernate.initialize(indicator.getValuesByYears());
                }
            }

            if (re.getCbcInvestments2() != null) {
                Hibernate.initialize(re.getCbcInvestments2().getIndicators());
                for (CbcInvestments2Indicator indicator : re.getCbcInvestments2().getIndicators()) {
                    if (indicator.getValuesByYears() != null)
                        Hibernate.initialize(indicator.getValuesByYears());
                }
            }

            if (re.getCbcInvestments3() != null) {
                Hibernate.initialize(re.getCbcInvestments3().getIndicators());
                for (CbcInvestments3Indicator indicator : re.getCbcInvestments3().getIndicators()) {
                    if (indicator.getValuesByYears() != null)
                        Hibernate.initialize(indicator.getValuesByYears());
                }
            }

            if (re.getCbcInvestments4() != null) {
                Hibernate.initialize(re.getCbcInvestments4().getIndicators());
                for (CbcInvestments4Indicator indicator : re.getCbcInvestments4().getIndicators()) {
                    if (indicator.getValuesByYears() != null)
                        Hibernate.initialize(indicator.getValuesByYears());
                }
            }

            if (re.getRemainingDebt() != null) {
                Hibernate.initialize(re.getRemainingDebt().getIndicators());
                for (RemainingDebtIndicator indicator : re.getRemainingDebt().getIndicators()) {
                    if (indicator.getValuesByYears() != null)
                        Hibernate.initialize(indicator.getValuesByYears());
                }
            }

            if (re.getPpInvestmentPlanningAmount() != null) {
                Hibernate.initialize(re.getPpInvestmentPlanningAmount().getIndicators());
                for (PlanInvestmentIndicator indicator : re.getPpInvestmentPlanningAmount().getIndicators()) {
                    if (indicator.getValuesByYears() != null)
                        Hibernate.initialize(indicator.getValuesByYears());
                }
            }

            if (re.getCrInvestmentCreationAmount() != null) {
                Hibernate.initialize(re.getCrInvestmentCreationAmount().getIndicators());
                for (CreationInvestmentIndicator indicator : re.getCrInvestmentCreationAmount().getIndicators()) {
                    if (indicator.getValuesByYears() != null)
                        Hibernate.initialize(indicator.getValuesByYears());
                }
            }

            if (re.getExInvestmentExploitationAmount() != null) {
                Hibernate.initialize(re.getExInvestmentExploitationAmount().getIndicators());
                for (ExploitationInvestmentIndicator indicator : re.getExInvestmentExploitationAmount().getIndicators()) {
                    if (indicator.getValuesByYears() != null)
                        Hibernate.initialize(indicator.getValuesByYears());
                }
            }

            if (re.getExInvestmentExploitationRecoveryAmount() != null) {
                Hibernate.initialize(re.getExInvestmentExploitationRecoveryAmount().getIndicators());
                for (ExploitationInvestmentRecoveryIndicator indicator : re.getExInvestmentExploitationRecoveryAmount().getIndicators()) {
                    if (indicator.getValuesByYears() != null)
                        Hibernate.initialize(indicator.getValuesByYears());
                }
            }

            for(TechEconomicsObjectIndicator object : re.getOdTechEconomicsObjectIndicators()) {
                for (TechEconomicsIndicator indicator : object.getTechEconomicsIndicators()) {
                    Hibernate.initialize(indicator.getYearValues());
                }
            }

            if (re.getExPayment() != null) {
                Hibernate.initialize(re.getExPayment().getValuesByYears());
            }

            if (re.getExCompensation() != null) {
                Hibernate.initialize(re.getExCompensation().getValuesByYears());
            }

            if (re.getCrBankGuaranteeByYears() != null) {
                Hibernate.initialize(re.getCrBankGuaranteeByYears().getValuesByYears());
            }

            if (re.getExBankGuaranteeByYears() != null) {
                Hibernate.initialize(re.getExBankGuaranteeByYears().getValuesByYears());
            }

            for (PrivatePartnerThirdPartyOrg thirdPartyOrg : re.getAdsPrivatePartnerThirdPartyOrgs()) {
                Hibernate.initialize(thirdPartyOrg.getWorkTypes());
            }

            for (PublicPartnerThirdPartyOrg thirdPartyOrg : re.getAdsPublicPartnerThirdPartyOrgs()) {
                Hibernate.initialize(thirdPartyOrg.getWorkTypes());
            }

            for (Sanction sanction : re.getAdsSanctions()) {
                Hibernate.initialize(sanction.getType());
            }
        }

        ProjectsResult rest = new ProjectsResult();
        rest.getList().addAll(collect);
        rest.setOffset(criteria.getOffset());
        rest.setTotalLength((int) res.getTotalElements());
        return rest;
    }

    @Override
    public List<ProjectDetailsRevision> getRevisions(Long id) throws Exception {
        return revisionService.getRevisions(id);
    }

    @Transactional
    public Integer updateDocumentStatus() {
        DocumentsFilterPaginateCriteria criteria = new DocumentsFilterPaginateCriteria();
        criteria.setProjectStatusIds(getProjStatusIds("GREEN").stream().map(String::valueOf).collect(Collectors.toList()));
        criteria.setRealizationStatusIds(getImplStatusIdsForUpdate());

        List<Project> docList = projectRepository.findAll(filterByCriteria(criteria));
        List<Long> yellowStatusIds = getProjStatusIds("YELLOW");

        if (!isEmpty(docList) && !isEmpty(yellowStatusIds)) {
            Long yellowStatusId = yellowStatusIds.get(0);
            docList.forEach(o -> o.setGiProjectStatus(yellowStatusId));
            projectRepository.saveAll(docList);
        }

        return docList.size();
    }

    @Override
    public RolePreferences getRolePreferences() {
        return rolePreferencesService.getRolePreferences();
    }

    @Override
    public List<SvrOrg> getSvrOrgs(FilterSvrOrgs filterSvrOrgs) {
        return svrOrgService.getSvrOrgs(filterSvrOrgs);
    }

    private List<Long> getProjStatusIds(String colorCode) {
        Collection<DictionaryDataRecordDescriptor> projStatusList = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_PROJECT_STATUS);
        List<Long> resultList = new ArrayList<>();
        projStatusList.forEach(o -> o.getFields().forEach(v -> {
            if ("COLOR_CODE".equals(v.getFieldDescriptor().getName())) {
                String statusColorCode = opt(() -> v.getExValues().get(0).getValue());
                if (colorCode.equals(statusColorCode)) {
                    resultList.add(o.getId());
                }
            }
        }));
        return resultList;
    }

    private List<String> getImplStatusIdsForUpdate() {
        Collection<DictionaryDataRecordDescriptor> implStatusList = dictCache.getDictFromCache(Dictionary.DIC_GASU_GCHP_IMPL_STATUS);
        List<String> resultList = new ArrayList<>();

        implStatusList.forEach(o -> o.getFields().forEach(v -> {
            if ("UPDATE_PROJECT_STATUS_EXCLUSION".equals(v.getFieldDescriptor().getName())) {
                Boolean exclusion = opt(() -> Boolean.parseBoolean(v.getExValues().get(0).getValue()));
                if (!(exclusion != null && exclusion)) {
                    resultList.add(String.valueOf(o.getId()));
                }
            }
        }));

        return resultList;
    }
}
