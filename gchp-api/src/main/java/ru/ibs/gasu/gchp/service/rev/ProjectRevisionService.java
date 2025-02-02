package ru.ibs.gasu.gchp.service.rev;

import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ibs.gasu.gchp.domain.FieldsMap;
import ru.ibs.gasu.gchp.domain.ProjectDetailsRevision;
import ru.ibs.gasu.gchp.domain.ProjectField;
import ru.ibs.gasu.gchp.entities.Project;
import ru.ibs.gasu.gchp.entities.Project_;
import ru.ibs.gasu.gchp.entities.files.ProjectFile;
import ru.ibs.gasu.gchp.repositories.ProjectDetailsRevisionRepository;
import ru.ibs.gasu.gchp.service.rev.util.*;
import ru.ibs.gasu.soap.generated.user.PublicUserWebService;
import ru.ibs.gasu.soap.generated.user.User;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Boolean.TRUE;

@Slf4j
@Transactional
@Service
public class ProjectRevisionService {
    private static final Map<String, ProjectField> FIELDS_MAP = new FieldsMap();

    @Autowired
    private GeneralInfoRevisionUtilService generalInfoRevisionUtilService;

    @Autowired
    private DescriptionRevisionUtilService descriptionRevisionUtilService;

    @Autowired
    private PreparationRevisionUtilService preparationRevisionUtilService;

    @Autowired
    private CreationRevisionUtilService creationRevisionUtilService;

    @Autowired
    private ExploitationRevisionUtilService exploitationRevisionUtilService;

    @Autowired
    private TerminationRevisionUtilService terminationRevisionUtilService;

    @Autowired
    private ChangeConditionRevisionUtilService changeConditionRevisionUtilService;

    @Autowired
    private AdditionalRevisionUtilService additionalRevisionUtilService;

    @Autowired
    private ProjectDetailsRevisionRepository revisionRepository;

    @Autowired
    private PublicUserWebService userWebService;

    @Autowired
    private CbcRevisionUtilService cbcRevisionUtilService;

    public List<ProjectDetailsRevision> getRevisions(Long id) throws NoSuchFieldException, IllegalAccessException, NotFoundException {
        log.info("Inside a method getRevisions(id = {})", id);
        List<Object[]> revisions = revisionRepository.getRevisionsWithModifications(id);
        log.info("Number of published revisions: revisions.size() = {}", revisions.size());
        if (revisions.isEmpty()) {
            return new ArrayList<>();
        }

        List<ProjectDetailsRevision> result = new ArrayList<>();
        Map<Long, User> userMap = new HashMap<>();

        Object[] current = revisions.get(0);

        //если не найден опубликованный проект среди ревизий, возвращаем пустой список.
        if (!TRUE.equals(((Project) current[0]).getPublished())) {
            return new ArrayList<>();
        }

        Project currProject = (Project) current[0];
        Project prevProject;
        if (revisions.size() > 1) {
            // Предыдущий подписанный документ.
            prevProject = (Project) revisions.get(1)[0];
        } else {
            // Если только документ был подписан один раз, то предыдущим будет пустой документ.
            prevProject = new Project();
        }
        log.info("Before method getUserNameById({}, userMap)", currProject.getUpdateUserId());
        String userName = getUserNameById(currProject.getUpdateUserId(), userMap);
        log.info("Revision was updated by user {}", userName);

        Field[] declaredFields = Project.class.getDeclaredFields();
        Set<String> properties = Arrays.stream(declaredFields).map(Field::getName).collect(Collectors.toSet());
        result.addAll(createRevisions(properties, prevProject, currProject, userName));
        result.addAll(createRevisions(prevProject, currProject, userName));

        // Если ревизий больше двух, то проходим по всем и сравниваем с предыдущей.
        for (int i = 2; i <= revisions.size(); i++) {
            currProject = prevProject;
            // Если ревизия была последняя, то сравниваем ее с пустым, если нет, то берем следующую ревизию.
            if (i == revisions.size()) {
                prevProject = new Project();
            } else {
                prevProject = (Project) revisions.get(i)[0];
            }

            userName = getUserNameById(currProject.getUpdateUserId(), userMap);
            result.addAll(createRevisions(properties, prevProject, currProject, userName));
            result.addAll(createRevisions(prevProject, currProject, userName));
        }

        //  Под ревизией понимаю модификацию документа с обязательной публикацией (поле published = true, см. revisionRepository.getRevisionsWithModifications())
        //  Если таких ревизий несколько, то мы сравниваем соседние ревизии. Сравнение идет по полям. Если какое-либо поле изменилось, то
        //  добавляем такую 'локальную' ревизию (т.е. ревизию внутри опубликованной ревизии) в result.
        //  Т.о., например, общее кол-во ревизий с публикацией revisions.size() = 2, но при этом локальных ревизий, (то, что будет храниться в result) м.б. гораздо больше.
      log.info("Number of local revisions inside of all published revisions: revisions.size() = {}", result.size());
        Collections.sort(result);
        Collections.reverse(result);
        return setKey(result);
    }

    private boolean isRevisionPrevValAndCurrentValEquals(String prevVal, String currentVal) {
        if (prevVal == null && currentVal == null)
            return true;
        if (prevVal == null || currentVal == null)
            return false;
        return prevVal.equals(currentVal);
    }

    private List<ProjectDetailsRevision> createRevisions(Set<String> properties, Project prevProject, Project currProject, String userName) throws IllegalAccessException, NoSuchFieldException {
        List<ProjectDetailsRevision> result = new ArrayList<>();
        for (String property : properties) {
            if (FIELDS_MAP.containsKey(property)) {
                ProjectDetailsRevision revision = new ProjectDetailsRevision();

                //для получения значения поля по его наименованию используется рефлексия.
                Field field = Project.class.getDeclaredField(property);
                field.setAccessible(true);

                boolean setCurrentValue = setCurrentValue(revision, field, currProject, property);
                boolean setPrevValue = setPrevValue(revision, field, prevProject, property);

                if (!isRevisionPrevValAndCurrentValEquals(revision.getPrevValue(), revision.getCurrentValue()) &&
                        setCurrentValue &&
                        setPrevValue &&
                        (!revision.getCurrentValue().isEmpty() || !revision.getPrevValue().isEmpty())) {
                    revision.setEditedField(FIELDS_MAP.get(property));

                    revision.setEditor(userName);
                    revision.setDate(currProject.getUpdateDate());

                    result.add(revision);
                }
            }
        }
        return result;
    }

    private boolean setCurrentValue(ProjectDetailsRevision revision, Field field, Project project, String property) throws IllegalAccessException {
        String value = getValue(project, property);
        if (value == null) {
            Object o = field.get(project);
            if (o instanceof Date) {
                revision.setCurrentValue((Date) o);
            } else if (o instanceof Boolean) {
                revision.setCurrentValue((Boolean) o);
            } else if (o instanceof Number) {
                revision.setCurrentValue((Number) o);
            } else if (o instanceof String) {
                revision.setCurrentValue((String) o);
            } else {
                try {
                    revision.setCurrentValue((List<? extends ProjectFile>) o);
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            revision.setCurrentValue(value);
        }
        return true;
    }

    private boolean setPrevValue(ProjectDetailsRevision revision, Field field, Project project, String property) throws IllegalAccessException {
        String value = getValue(project, property);
        if (value == null) {
            Object o = field.get(project);
            if (o instanceof Date) {
                revision.setPrevValue((Date) o);
            } else if (o instanceof Boolean) {
                revision.setPrevValue((Boolean) o);
            } else if (o instanceof Number) {
                revision.setPrevValue((Number) o);
            } else if (o instanceof String) {
                revision.setPrevValue((String) o);
            } else {
                try {
                    revision.setPrevValue((List<? extends ProjectFile>) o);
                } catch (Exception ignore) {
                    return false;
                }
            }
        } else {
            revision.setPrevValue(value);
        }
        return true;
    }

    private String getValue(Project project, String property) {
        switch (property) {
            case Project_.GI_REALIZATION_FORM: {
                return generalInfoRevisionUtilService.getRealisationForm(project);
            }
            case Project_.GI_INITIATION_METHOD: {
                return generalInfoRevisionUtilService.getInitiationMethod(project);
            }
            case Project_.GI_REALIZATION_LEVEL: {
                return generalInfoRevisionUtilService.getRealizationLevel(project);
            }
            case Project_.GI_REGION: {
                return generalInfoRevisionUtilService.getRegion(project);
            }
            case Project_.GI_MUNICIPALITY: {
                return generalInfoRevisionUtilService.getMunicipality(project);
            }
            case Project_.GI_REALIZATION_SPHERE: {
                return generalInfoRevisionUtilService.getRealizationSphere(project);
            }
            case Project_.GI_REALIZATION_SECTOR: {
                return generalInfoRevisionUtilService.getRealizationSector(project);
            }
            case Project_.GI_REALIZATION_STATUS: {
                return generalInfoRevisionUtilService.getRealizationStatus(project);
            }
            case Project_.GI_OBJECT_TYPE: {
                return generalInfoRevisionUtilService.objectTypes(project);
            }
            case Project_.GI_AGREEMENT_SUBJECT: {
                return generalInfoRevisionUtilService.agreementSubject(project);
            }
            case Project_.GI_PUBLIC_PARTNER: {
                return generalInfoRevisionUtilService.publicPartner(project);
            }
            case Project_.GI_OP_F: {
                return generalInfoRevisionUtilService.getOpf(project);
            }
            case Project_.OD_RENT_OBJECT: {
                return descriptionRevisionUtilService.getRentObject(project);
            }
            case Project_.CR_GOV_SUPPORT: {
                return creationRevisionUtilService.getGovSupport(project);
            }
            case Project_.CR_AGREEMENT_COMPLEX: {
                return creationRevisionUtilService.getAgreementsSet(project);
            }
            case Project_.CR_OPF: {
                return creationRevisionUtilService.getOpf(project);
            }
            case Project_.PP_GROUNDS_OF_AGREEMENT_CONCLUSION: {
                return preparationRevisionUtilService.getGroundsOfAgreementConclusion(project);
            }
            case Project_.PP_COMPETITION_RESULTS_SIGN_STATUS: {
                return preparationRevisionUtilService.getCompetitionResultsSignStatus(project);
            }
            case Project_.PP_CONTRACT_PRICE_ORDER: {
                return preparationRevisionUtilService.getContractPriceOrder(project);
            }
//            case Project_.PP_CONTRACT_PRICE_METHOD: {
//                return preparationRevisionUtilService.getContractPriceMethod(project);
//            }
            case Project_.PP_CONTRACT_PRICE_OFFER: {
                return preparationRevisionUtilService.getContractPriceOffer(project);
            }
            case Project_.PP_PRIVATE_PARTNER_COST_RECOVERY_METHOD: {
                return preparationRevisionUtilService.getPrivatePartnerCostRecoveryMethod(project);
            }
            case Project_.PP_COMPETITION_RESULTS: {
                return preparationRevisionUtilService.getCompetitionResult(project);
            }
            case Project_.PP_METHOD_OF_EXECUTE_OBLIGATION: {
                return preparationRevisionUtilService.getMethodOfExecuteObligation(project);
            }
            case Project_.PP_STATE_SUPPORT_MEASURES_SP_IC: {
                return preparationRevisionUtilService.getStateSupportMeasuresSPIC(project);
            }
            case Project_.PP_AGREEMENTS_SET: {
                return preparationRevisionUtilService.getAgreementsSet(project);
            }
            case Project_.PP_MEASURE_TYPE: {
                return preparationRevisionUtilService.getMeasureType(project);
            }
            case Project_.EX_IR_SOURCE: {
                return exploitationRevisionUtilService.getIRSource(project);
            }
            case Project_.PP_RESULTS_OF_PLACING: {
                return preparationRevisionUtilService.getPpResultsOfPlacing(project);
            }
            case Project_.EX_IR_LEVEL: {
                return exploitationRevisionUtilService.getIRLevel(project);
            }
            ////case Project_.EX_METHOD_OF_EXEC_OF_PUBLIC_PARTNER_OBLIGATION: {
            ////return exploitationRevisionUtilService.getMethodOfExecOfPublicPartnerObligation(project);
            ////}
            case Project_.EX_PAYMENT_FORM: {
                return exploitationRevisionUtilService.getPaymentForm(project);
            }
            case Project_.TM_CAUSE: {
                return terminationRevisionUtilService.getCause(project);
            }
            case Project_.TM_AFTERMATH: {
                return terminationRevisionUtilService.getAftermath(project);
            }
            case Project_.TM_COMPOSITION_OF_COMPENSATION_GRANTOR_FAULT: {
                return terminationRevisionUtilService.getTmCompositionOfCompensationGrantorFault(project);
            }
            case Project_.CC_REASON: {
                return changeConditionRevisionUtilService.getReason(project);
            }
            case Project_.ADS_CONCESSIONAIRE_OPF: {
                return additionalRevisionUtilService.getConcessionaireOpf(project);
            }
            case Project_.ADS_CONCESSIONAIRE_REGIME: {
                return additionalRevisionUtilService.getConcessionaireRegime(project);
            }
            case Project_.ADS_FINANCIAL_REQUIREMENT: {
                return additionalRevisionUtilService.getFinancialRequirements(project);
            }
            case Project_.ADS_NON_FINANCIAL_REQUIREMENTS: {
                return additionalRevisionUtilService.getNonFinancialRequirements(project);
            }
            case Project_.ADS_COMPETITION_CRITERIA: {
                return additionalRevisionUtilService.getCompetitionCriteria(project);
            }
            case Project_.ADS_INCOME_TAX_RATE: {
                return additionalRevisionUtilService.getRate(project.getAdsIncomeTaxRate());
            }
            case Project_.ADS_LAND_TAX_RATE: {
                return additionalRevisionUtilService.getRate(project.getAdsLandTaxRate());
            }
            case Project_.ADS_PROPERTY_TAX_RATE: {
                return additionalRevisionUtilService.getRate(project.getAdsPropertyTaxRate());
            }
            case Project_.ADS_BENEFIT_CLARIFICATION_RATE: {
                return additionalRevisionUtilService.getRate(project.getAdsBenefitClarificationRate());
            }
            case Project_.GI_PROJECT_STATUS: {
                return generalInfoRevisionUtilService.getProjectStatus(project);
            }
        }
        return null;
    }

    private List<ProjectDetailsRevision> createRevisions(Project prevProject, Project currProject, String userName) {
        List<ProjectDetailsRevision> result = new ArrayList<>();

        descriptionRevisionUtilService.createTechEconomicIndicatorRevision(prevProject, currProject, userName).ifPresent(result::add);
        descriptionRevisionUtilService.createEnergyEfficiencyPlansRevision(prevProject, currProject, userName).ifPresent(result::add);
        additionalRevisionUtilService.createEvents(prevProject, currProject, userName).ifPresent(result::add);
        additionalRevisionUtilService.createFinancialStructure(prevProject, currProject, userName).ifPresent(result::add);
        additionalRevisionUtilService.createInvestmentBoolCriterias(prevProject, currProject, userName).ifPresent(result::add);
        additionalRevisionUtilService.createJudicialActivities(prevProject, currProject, userName).ifPresent(result::add);
        additionalRevisionUtilService.createOwnershipStructures(prevProject, currProject, userName).ifPresent(result::add);
        additionalRevisionUtilService.createPrivatePartnerThirdPartyOrgs(prevProject, currProject, userName).ifPresent(result::add);
        additionalRevisionUtilService.createPublicPartnerThirdPartyOrgs(prevProject, currProject, userName).ifPresent(result::add);
        additionalRevisionUtilService.createSanctions(prevProject, currProject, userName).ifPresent(result::add);
        creationRevisionUtilService.createEnsureMethodsRevision(prevProject, currProject, userName).ifPresent(result::add);
        creationRevisionUtilService.createBankGuaranteeByYearsRevision(prevProject, currProject, userName).ifPresent(result::add);
        exploitationRevisionUtilService.createEnsureMethodsRevision(prevProject, currProject, userName).ifPresent(result::add);
        exploitationRevisionUtilService.createBankGuaranteeByYearsRevision(prevProject, currProject, userName).ifPresent(result::add);

        cbcRevisionUtilService.createCbcInvestment1AmountRevision(prevProject, currProject, userName).ifPresent(result::add);
        cbcRevisionUtilService.createCbcInvestment2AmountRevision(prevProject, currProject, userName).ifPresent(result::add);
        cbcRevisionUtilService.createCbcInvestment3AmountRevision(prevProject, currProject, userName).ifPresent(result::add);
        cbcRevisionUtilService.createCbcInvestment4AmountRevision(prevProject, currProject, userName).ifPresent(result::add);
        creationRevisionUtilService.createRemainingDebtAmountRevision(prevProject, currProject, userName).ifPresent(result::add);
        terminationRevisionUtilService.createCompositionOfCompensation(prevProject, currProject, userName).ifPresent(result::add);
        preparationRevisionUtilService.createInvestmentAmountRevision(prevProject, currProject, userName).ifPresent(result::add);
        preparationRevisionUtilService.createCreationInvestmentAmountRevision(prevProject, currProject, userName).ifPresent(result::add);
        creationRevisionUtilService.createInvestmentAmountRevision(prevProject, currProject, userName).ifPresent(result::add);
        exploitationRevisionUtilService.createInvestmentAmountRevision(prevProject, currProject, userName).ifPresent(result::add);
        exploitationRevisionUtilService.createInvestmentRecoveryAmountRevision(prevProject, currProject, userName).ifPresent(result::add);
        exploitationRevisionUtilService.createPaymentRevision(prevProject, currProject, userName).ifPresent(result::add);
        exploitationRevisionUtilService.createCompensationRevision(prevProject, currProject, userName).ifPresent(result::add);

        return result;
    }

    private List<ProjectDetailsRevision> setKey(List<ProjectDetailsRevision> result) {
        int i = 0;
        for (ProjectDetailsRevision revision : result) {
            revision.setKey(++i);
        }
        return result;
    }

    private String getUserNameById(Long id, Map<Long, User> userMap) throws NotFoundException {
        if (id == null) {
            return "";
        }
        User user = userMap.get(id);
        if (user == null) {
            user = userWebService.getUserById(id);
            if (user == null) {
                throw new NotFoundException("Author of revision has id = " + id + ", but this user not exist");
            }
            userMap.put(id, user);
        }
        return user.getFirstName() + " " + user.getMiddleName() + " " + user.getLastName();
    }

}
