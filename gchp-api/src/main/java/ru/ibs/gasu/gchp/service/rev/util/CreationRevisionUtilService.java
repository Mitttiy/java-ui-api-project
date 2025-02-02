package ru.ibs.gasu.gchp.service.rev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.dictionaries.domain.AgreementsSetEntity;
import ru.ibs.gasu.dictionaries.domain.EnsureMethod;
import ru.ibs.gasu.dictionaries.domain.GovSupport;
import ru.ibs.gasu.dictionaries.domain.OpfEntity;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.domain.ProjectDetailsRevision;
import ru.ibs.gasu.gchp.entities.*;
import ru.ibs.gasu.gchp.entities.CreationInvestmentIndicator;
import ru.ibs.gasu.gchp.entities.CreationInvestments;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.ibs.gasu.gchp.domain.ProjectDetailsRevision.SIMPLE_DATE_FORMAT;
import static ru.ibs.gasu.gchp.domain.ProjectField.*;
import static ru.ibs.gasu.gchp.domain.ProjectField.REMAINING_DEBT;

@Service
public class CreationRevisionUtilService {
    @Autowired
    private DictionaryDataService dictionaryDataService;

    public String getGovSupport(Project project) {
        if (project.getCrGovSupport() != null) {
            return dictionaryDataService.getAllGovSupports().stream().filter(kind -> {
                Predicate<GovSupport> predicate = p -> false;
                for (Long type : project.getCrGovSupport()) {
                    predicate = predicate.or(p -> p.getId().equals(type));
                }
                return predicate.test(kind);
            }).map(GovSupport::getName).collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getAgreementsSet(Project project) {
        if (project.getCrAgreementComplex() != null) {
            return dictionaryDataService.getAllAgreementsSets()
                    .stream()
                    .filter(form -> form.getId().equals(project.getCrAgreementComplex()))
                    .map(AgreementsSetEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getOpf(Project project) {
        if (project.getCrOpf() != null) {
            return dictionaryDataService.getAllOpfs()
                    .stream()
                    .filter(form -> form.getId().equals(String.valueOf(project.getCrOpf())))
                    .map(OpfEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    private String getEnsureMethod(Long id) {
        if (id == null) {
            return "";
        }
        return dictionaryDataService.getAllEnsureMethods()
                .stream()
                .filter(form -> form.getId().equals(id.toString()))
                .map(EnsureMethod::getName)
                .collect(Collectors.joining(", "));
    }

    public Optional<ProjectDetailsRevision> createInvestmentAmountRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;

        checkExistValuesInvestmentAmount(prevProject);
        checkExistValuesInvestmentAmount(currProject);
        if (!Objects.equals(prevProject.getCrInvestmentCreationAmount(), currProject.getCrInvestmentCreationAmount())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(CR_INVESTMENT_CREATION_AMOUNT);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getInvestmentAmount(prevProject));
            revision.setCurrentValue(getInvestmentAmount(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private void checkExistValuesInvestmentAmount(Project project) {
        boolean hasValue = false;

        CreationInvestments amount = project.getCrInvestmentCreationAmount();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()) {
            for (CreationInvestmentIndicator indicator : amount.getIndicators()) {
                if (indicator.getPlan() != null || indicator.getFact() != null || !indicator.getValuesByYears().isEmpty()) {
                    hasValue = true;
                    break;
                }
            }

            if (!hasValue) {
                project.setCrInvestmentCreationAmount(null);
            }
        }
    }

    private String getInvestmentAmount(Project project) {
        CreationInvestments amount = project.getCrInvestmentCreationAmount();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()){
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

    public Optional<ProjectDetailsRevision> createEnsureMethodsRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        if (!Objects.equals(prevProject.getCrEnsureMethods(), currProject.getCrEnsureMethods())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(CR_ENSURE_METHODS);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getEnsureMethods(prevProject));
            revision.setCurrentValue(getEnsureMethods(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getEnsureMethods(Project project) {
        if (project.getCrEnsureMethods() != null && !project.getCrEnsureMethods().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Способ обеспечения обязательств частного партнера</th>" +
                    "                   <th>Тип риска / форма обеспечения</th>" +
                    "                   <th>Срок обеспечения</th>" +
                    "                   <th>Размер обеспечения</th>" +
                    "                   <th>Планируемая дата представления</th>" +
                    "               </tr>";

            String body = project.getCrEnsureMethods().stream().map(method -> {
                        String indicatorRow = "<tr>" +
                                "            <td>" +
                                getEnsureMethod(method.getEnsureMethodId()) +
                                "            </td>" +
                                "            <td>" +
                                (method.getRiskType() != null ? method.getRiskType() : "") +
                                "            </td>" +
                                "            <td>" +
                                (method.getTerm() != null ? method.getTerm() : "") +
                                "            </td>" +
                                "            <td>" +
                                (method.getValue() != null ? method.getValue() : "") +
                                "            </td>" +
                                "            <td>" +
                                (method.getSubmissionDate() != null ? method.getSubmissionDate() : "") +
                                "            </td>" +
                                "        </tr>";

                        return indicatorRow;
                    }
            ).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createBankGuaranteeByYearsRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        checkExistBankGuarantee(prevProject);
        checkExistBankGuarantee(currProject);
        if (!Objects.equals(prevProject.getCrBankGuaranteeByYears(), currProject.getCrBankGuaranteeByYears())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(CR_BANK_GUARANTEE_BY_YEARS);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getBankGuaranteeByYears(prevProject));
            revision.setCurrentValue(getBankGuaranteeByYears(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private void checkExistBankGuarantee(Project project) {
        boolean hasValue = false;

        BankGuarantee bankGuarantee = project.getCrBankGuaranteeByYears();
        if (bankGuarantee != null) {
            if (/*bankGuarantee.getPlan() != null ||*/bankGuarantee.getFact() != null || !bankGuarantee.getValuesByYears().isEmpty()) {
                hasValue = true;
            }

            if (!hasValue) {
                project.setCrBankGuaranteeByYears(null);
            }
        }
    }

    private String getBankGuaranteeByYears(Project project) {
        BankGuarantee bankGuarantee = project.getCrBankGuaranteeByYears();

        if (bankGuarantee != null) {
            String indicatorsTable = "";
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Размер банковской гарантии по годам, тыс.руб.</th>" +
                    "                   <th>Значение</th>" +
                    "               </tr>";

            String indicatorRow = "<tr>" +
                    "            <td>" +
                    "   Всего" +
                    "            </td>" +
                    "            <td>" +
                    (bankGuarantee.getFact() != null ? bankGuarantee.getFact() : "") +
                    "            </td>" +
                    "        </tr>";

            if (bankGuarantee.getValuesByYears() != null && !bankGuarantee.getValuesByYears().isEmpty()) {
                StringBuilder stringBuilder = new StringBuilder();
                bankGuarantee.getValuesByYears().forEach(yearValue -> {
                    stringBuilder.append("<tr>" +
                            "            <td>" +
                            yearValue.getYear() +
                            "            </td>" +
                            "            <td>" +
                            (yearValue.getFact() != null ? yearValue.getFact() : "") +
                            "            </td>" +
                            "        </tr>");
                });
                indicatorRow = indicatorRow + stringBuilder;
            }
            indicatorsTable = header + indicatorRow + "</table>";
            return indicatorsTable;
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createRemainingDebtAmountRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;

        checkExistValuesRemainingDebtAmount(prevProject);
        checkExistValuesRemainingDebtAmount(currProject);
        if (!Objects.equals(prevProject.getRemainingDebt(), currProject.getRemainingDebt())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(REMAINING_DEBT);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getRemainingDebtPlanningAmount(prevProject));
            revision.setCurrentValue(getRemainingDebtPlanningAmount(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private void checkExistValuesRemainingDebtAmount(Project project) {
        boolean hasValue = false;

        RemainingDebt amount = project.getRemainingDebt();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()) {
            for (RemainingDebtIndicator indicator : amount.getIndicators()) {
                if (indicator.getPlan() != null || indicator.getFact() != null || !indicator.getValuesByYears().isEmpty()) {
                    hasValue = true;
                    break;
                }
            }

            if (!hasValue) {
                project.setRemainingDebt(null);
            }
        }
    }

    private String getRemainingDebtPlanningAmount(Project project) {
        RemainingDebt amount = project.getRemainingDebt();
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
}
