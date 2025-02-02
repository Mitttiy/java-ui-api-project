package ru.ibs.gasu.gchp.service.rev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.dictionaries.domain.*;
import ru.ibs.gasu.dictionaries.repository.MeasureTypeRepo;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.domain.ProjectDetailsRevision;
import ru.ibs.gasu.gchp.entities.*;
import ru.ibs.gasu.gchp.entities.PlanInvestments;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.ibs.gasu.gchp.domain.ProjectField.PP_CREATION_INVESTMENT_PLANNING_AMOUNT;
import static ru.ibs.gasu.gchp.domain.ProjectField.PP_INVESTMENT_PLANNING_AMOUNT;
import static ru.ibs.gasu.gchp.domain.ProjectDetailsRevision.SIMPLE_DATE_FORMAT;

@Service
public class PreparationRevisionUtilService {
    @Autowired
    private DictionaryDataService dictionaryDataService;

    @Autowired
    private MeasureTypeRepo measureTypeRepo;

    public Optional<ProjectDetailsRevision> createInvestmentAmountRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;

        checkExistValuesInvestmentAmount(prevProject);
        checkExistValuesInvestmentAmount(currProject);
        if (!Objects.equals(prevProject.getPpInvestmentPlanningAmount(), currProject.getPpInvestmentPlanningAmount())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(PP_INVESTMENT_PLANNING_AMOUNT);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getInvestmentPlanningAmount(prevProject));
            revision.setCurrentValue(getInvestmentPlanningAmount(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private void checkExistValuesInvestmentAmount(Project project) {
        boolean hasValue = false;

        PlanInvestments amount = project.getPpInvestmentPlanningAmount();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()) {
            for (PlanInvestmentIndicator indicator : amount.getIndicators()) {
                if (indicator.getPlan() != null || indicator.getFact() != null || !indicator.getValuesByYears().isEmpty()) {
                    hasValue = true;
                    break;
                }
            }

            if (!hasValue) {
                project.setPpInvestmentPlanningAmount(null);
            }
        }
    }

    private String getInvestmentPlanningAmount(Project project) {
        PlanInvestments amount = project.getPpInvestmentPlanningAmount();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()) {
            String commonInfo = "<div>" +
                    "<span> Включая НДС: " + (amount.getIncludeNds() != null ? amount.getIncludeNds() ? "Да" : "Нет" : "") + "<span><br>" +
                    "<span> Способ измерения измерения: " + (amount.getMeasureType() != null && amount.getMeasureType().getName() != null ? amount.getMeasureType().getName() : "") + "<span>" +
                    (amount.getOnDate() == null
                            ? ""
                            : "<br><span> На дату: " + SIMPLE_DATE_FORMAT.format(amount.getOnDate()) + "<span>") +
                    "</div>";
            String indicatorsTable = "";
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Показатель/Год</th>" +
                    "                   <th>План</th>" +
                    "                   <th>Факт</th>" +
                    "               </tr>";

            String body = amount.getIndicators().stream().map(indicator -> {
                        String indicatorRow = "<tr>" +
                                "            <td>" +
                                (indicator.getType() != null && indicator.getType().getName() != null ? indicator.getType().getName() : "") +
                                "            </td>" +
                                "            <td>" +
                                (indicator.getPlan() != null ? indicator.getPlan() : "") +
                                "            </td>" +
                                "            <td>" +
                                (indicator.getFact() != null ? indicator.getFact() : "") +
                                "            </td>" +
                                "        </tr>";

                        if (indicator.getValuesByYears() == null) {
                            return indicatorRow;
                        }
                        StringBuilder stringBuilder = new StringBuilder(indicatorRow);
                        indicator.getValuesByYears().forEach(yearValue -> {
                            stringBuilder.append("<tr>" +
                                    "            <td>" +
                                    yearValue.getYear() +
                                    "            </td>" +
                                    "            <td>" +
                                    (yearValue.getPlan() != null ? yearValue.getPlan() : "") +
                                    "            </td>" +
                                    "            <td>" +
                                    (yearValue.getFact() != null ? yearValue.getFact() : "") +
                                    "            </td>" +
                                    "        </tr>");
                        });
                        return stringBuilder.toString();
                    }
            ).collect(Collectors.joining(""));
            indicatorsTable = header + body + "</table>";
            return commonInfo + indicatorsTable;
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createCreationInvestmentAmountRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;

        checkExistValuesCreationInvestmentAmount(prevProject);
        checkExistValuesCreationInvestmentAmount(currProject);
        if (!Objects.equals(prevProject.getPpCreationInvestmentPlanningAmount(), currProject.getPpCreationInvestmentPlanningAmount())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(PP_CREATION_INVESTMENT_PLANNING_AMOUNT);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getCreationInvestmentPlanningAmount(prevProject));
            revision.setCurrentValue(getCreationInvestmentPlanningAmount(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private void checkExistValuesCreationInvestmentAmount(Project project) {
        boolean hasValue = false;

        PlanCreationInvestments amount = project.getPpCreationInvestmentPlanningAmount();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()) {
            for (PlanCreationInvestmentIndicator indicator : amount.getIndicators()) {
                if (indicator.getPlan() != null || indicator.getFact() != null || !indicator.getValuesByYears().isEmpty()) {
                    hasValue = true;
                    break;
                }
            }

            if (!hasValue) {
                project.setPpCreationInvestmentPlanningAmount(null);
            }
        }
    }

    private String getCreationInvestmentPlanningAmount(Project project) {
        PlanCreationInvestments amount = project.getPpCreationInvestmentPlanningAmount();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()) {
            String commonInfo = "<div>" +
                    "<span> Включая НДС: " + (amount.getIncludeNds() != null ? amount.getIncludeNds() ? "Да" : "Нет" : "") + "<span><br>" +
                    "<span> Способ измерения измерения: " + (amount.getMeasureType() != null && amount.getMeasureType().getName() != null ? amount.getMeasureType().getName() : "") + "<span>" +
                    (amount.getOnDate() == null
                            ? ""
                            : "<br><span> На дату: " + SIMPLE_DATE_FORMAT.format(amount.getOnDate()) + "<span>") +
                    "</div>";
            String indicatorsTable = "";
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Показатель/Год</th>" +
                    "                   <th>План</th>" +
                    "                   <th>Факт</th>" +
                    "               </tr>";

            String body = amount.getIndicators().stream().map(indicator -> {
                        String indicatorRow = "<tr>" +
                                "            <td>" +
                                (indicator.getType() != null && indicator.getType().getName() != null ? indicator.getType().getName() : "") +
                                "            </td>" +
                                "            <td>" +
                                (indicator.getPlan() != null ? indicator.getPlan() : "") +
                                "            </td>" +
                                "            <td>" +
                                (indicator.getFact() != null ? indicator.getFact() : "") +
                                "            </td>" +
                                "        </tr>";

                        if (indicator.getValuesByYears() == null) {
                            return indicatorRow;
                        }
                        StringBuilder stringBuilder = new StringBuilder(indicatorRow);
                        indicator.getValuesByYears().forEach(yearValue -> {
                            stringBuilder.append("<tr>" +
                                    "            <td>" +
                                    yearValue.getYear() +
                                    "            </td>" +
                                    "            <td>" +
                                    (yearValue.getPlan() != null ? yearValue.getPlan() : "") +
                                    "            </td>" +
                                    "            <td>" +
                                    (yearValue.getFact() != null ? yearValue.getFact() : "") +
                                    "            </td>" +
                                    "        </tr>");
                        });
                        return stringBuilder.toString();
                    }
            ).collect(Collectors.joining(""));
            indicatorsTable = header + body + "</table>";
            return commonInfo + indicatorsTable;
        }
        return "";
    }

    public String getGroundsOfAgreementConclusion(Project project) {
        if (project.getPpGroundsOfAgreementConclusion() != null) {
            return dictionaryDataService.getAllAgreementGrounds()
                    .stream()
                    .filter(form -> form.getId().equals(project.getPpGroundsOfAgreementConclusion()))
                    .map(AgreementGrounds::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getCompetitionResultsSignStatus(Project project) {
        if (project.getPpCompetitionResultsSignStatus() != null) {
            return dictionaryDataService.getAllCompetitionResultSigns()
                    .stream()
                    .filter(form -> form.getId().equals(project.getPpCompetitionResultsSignStatus()))
                    .map(CompetitionResultsSign::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getContractPriceOrder(Project project) {
        if (project.getPpContractPriceOrder() != null) {
            return dictionaryDataService.getAllPriceOrderMethods()
                    .stream()
                    .filter(form -> form.getId().equals(project.getPpContractPriceOrder()))
                    .map(PriceOrderEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

//    public String getContractPriceMethod(ProjectDetailsSimple project) {
//        if (project.getPpContractPriceMethod() != null) {
//            return dictionaryDataService.getAllPaymentMethods()
//                    .stream()
//                    .filter(form -> form.getId().equals(project.getPpContractPriceMethod()))
//                    .map(PaymentMethodEntity::getName)
//                    .collect(Collectors.joining(", "));
//        }
//        return "";
//    }

    public String getContractPriceOffer(Project project) {
        if (project.getPpContractPriceOffer() != null) {
            return dictionaryDataService.getAllContractPriceOffers()
                    .stream()
                    .filter(form -> form.getId().equals(project.getPpContractPriceOffer()))
                    .map(ContractPriceOffer::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getPrivatePartnerCostRecoveryMethod(Project project) {
        if (project.getPpPrivatePartnerCostRecoveryMethod() != null) {
            return dictionaryDataService.getAllCostRecoveryMethods()
                    .stream()
                    .filter(form -> form.getId().equals(project.getPpPrivatePartnerCostRecoveryMethod()))
                    .map(CostRecoveryMethodEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getCompetitionResult(Project project) {
        if (project.getPpCompetitionResults() != null) {
            return dictionaryDataService.getAllCompetitionResults()
                    .stream()
                    .filter(form -> form.getId().equals(project.getPpCompetitionResults()))
                    .map(CompetitionResults::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getMethodOfExecuteObligation(Project project) {
        if (project.getPpMethodOfExecuteObligation() != null) {
            return dictionaryDataService.getAllMethodOfExecuteObligations()
                    .stream()
                    .filter(form -> form.getId().equals(project.getPpMethodOfExecuteObligation()))
                    .map(MethodOfExecuteObligationEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getStateSupportMeasuresSPIC(Project project) {
        if (project.getPpStateSupportMeasuresSPIC() != null) {
            return dictionaryDataService.getAllOtherGovSupports()
                    .stream()
                    .filter(form -> form.getId().equals(project.getPpStateSupportMeasuresSPIC()))
                    .map(OtherGovSupportsEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getAgreementsSet(Project project) {
        if (project.getPpAgreementsSet() != null) {
            return dictionaryDataService.getAllAgreementsSets()
                    .stream()
                    .filter(form -> form.getId().equals(project.getPpAgreementsSet()))
                    .map(AgreementsSetEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getMeasureType(Project project) {
        if (project.getPpMeasureType() != null) {
            return measureTypeRepo.findById(project.getPpMeasureType()).get().getName();
        }
        return "";
    }

    public String getPpResultsOfPlacing(Project project) {
        if (project.getPpResultsOfPlacing() != null) {
            return dictionaryDataService.getAllPpResultsOfPlacing()
                    .stream()
                    .filter(form -> form.getId().equals(project.getPpResultsOfPlacing()))
                    .map(PpResultOfPlacingEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

}
