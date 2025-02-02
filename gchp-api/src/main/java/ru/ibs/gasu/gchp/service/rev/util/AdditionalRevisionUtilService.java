package ru.ibs.gasu.gchp.service.rev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.dictionaries.domain.*;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.domain.ProjectDetailsRevision;
import ru.ibs.gasu.gchp.entities.FinancialStructure;
import ru.ibs.gasu.gchp.entities.Project;
import ru.ibs.gasu.gchp.entities.SanctionType;
import ru.ibs.gasu.gchp.entities.WorkType;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.ibs.gasu.gchp.domain.ProjectField.*;
import static ru.ibs.gasu.gchp.domain.ProjectDetailsRevision.SIMPLE_DATE_FORMAT;


@Service
public class AdditionalRevisionUtilService {
    @Autowired
    private DictionaryDataService dictionaryDataService;

    public String getConcessionaireOpf(Project project) {
        if (project.getAdsConcessionaireOpf() != null) {
            return dictionaryDataService.getAllOpfs()
                    .stream()
                    .filter(form -> form.getId().equals(String.valueOf(project.getAdsConcessionaireOpf())))
                    .map(OpfEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getConcessionaireRegime(Project project) {
        if (project.getAdsConcessionaireRegime() != null) {
            return dictionaryDataService.getAllBusinessModeTypeMethods()
                    .stream()
                    .filter(form -> form.getId().equals(Long.valueOf(project.getAdsConcessionaireRegime())))
                    .map(BusinessModeType::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getFinancialRequirements(Project project) {
        if (project.getAdsFinancialRequirement() != null) {
            return dictionaryDataService.getAllFinRequirementMethods().stream().filter(kind -> {
                Predicate<FinRequirement> predicate = p -> false;
                for (Long requirement : project.getAdsFinancialRequirement()) {
                    predicate = predicate.or(p -> p.getId().equals(requirement));
                }
                return predicate.test(kind);
            }).map(FinRequirement::getName).collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getNonFinancialRequirements(Project project) {
        if (project.getAdsNonFinancialRequirements() != null) {
            return dictionaryDataService.getAllNonFinRequirementMethods().stream().filter(kind -> {
                Predicate<NoFinRequirement> predicate = p -> false;
                for (Long requirement : project.getAdsNonFinancialRequirements()) {
                    predicate = predicate.or(p -> p.getId().equals(requirement));
                }
                return predicate.test(kind);
            }).map(NoFinRequirement::getName).collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getCompetitionCriteria(Project project) {
        if (project.getAdsCompetitionCriteria() != null) {
            return dictionaryDataService.getAllCompetitionCriterionMethods().stream().filter(kind -> {
                Predicate<CompetitionCriterion> predicate = p -> false;
                for (Long requirement : project.getAdsCompetitionCriteria()) {
                    predicate = predicate.or(p -> p.getId().equals(requirement));
                }
                return predicate.test(kind);
            }).map(CompetitionCriterion::getName).collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getRate(Long id) {
        if (id != null) {
            if (id == 1L) {
                return "Нулевая";
            } else if (id == 2L) {
                return "Пониженная";
            }
            return "?";
        }
        return "";
    }


    public Optional<ProjectDetailsRevision> createEvents(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        if (!Objects.equals(prevProject.getAdsEvents(), currProject.getAdsEvents())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(ADS_EVENTS);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getEvents(prevProject));
            revision.setCurrentValue(getEvents(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getEvents(Project project) {
        if (project.getAdsEvents() != null && !project.getAdsEvents().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Дата</th>" +
                    "                   <th>Наименование события</th>" +
                    "                   <th>Описание события</th>" +
                    "               </tr>";

            String body = project.getAdsEvents().stream().map(indicator -> "<tr>" +
                    "            <td>" +
                    (indicator.getDate() != null ? SIMPLE_DATE_FORMAT.format(indicator.getDate()) : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getName() != null ? indicator.getName() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getDescription() != null ? indicator.getDescription() : "") +
                    "            </td>" +
                    "        </tr>"
            ).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createPrivatePartnerThirdPartyOrgs(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        if (!Objects.equals(prevProject.getAdsPrivatePartnerThirdPartyOrgs(), currProject.getAdsPrivatePartnerThirdPartyOrgs())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(ADS_PRIVATE_PARTNER_THIRD_PARTY_ORGS);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getPrivatePartnerThirdPartyOrgs(prevProject));
            revision.setCurrentValue(getPrivatePartnerThirdPartyOrgs(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getPrivatePartnerThirdPartyOrgs(Project project) {
        if (project.getAdsPrivatePartnerThirdPartyOrgs() != null && !project.getAdsPrivatePartnerThirdPartyOrgs().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Наименование события</th>" +
                    "                   <th>ИНН</th>" +
                    "                   <th>Виды работ</th>" +
                    "               </tr>";

            String body = project.getAdsPrivatePartnerThirdPartyOrgs().stream().map(indicator -> "<tr>" +
                    "            <td>" +
                    (indicator.getName() != null ? indicator.getName() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getInn() != null ? indicator.getInn() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getWorkTypes() != null ? workTypes(indicator.getWorkTypes()) : "") +
                    "            </td>" +
                    "        </tr>"
            ).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createPublicPartnerThirdPartyOrgs(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        if (!Objects.equals(prevProject.getAdsPublicPartnerThirdPartyOrgs(), currProject.getAdsPublicPartnerThirdPartyOrgs())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(ADS_PUBLIC_PARTNER_THIRD_PARTY_ORGS);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getPublicPartnerThirdPartyOrgs(prevProject));
            revision.setCurrentValue(getPublicPartnerThirdPartyOrgs(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getPublicPartnerThirdPartyOrgs(Project project) {
        if (project.getAdsPublicPartnerThirdPartyOrgs() != null && !project.getAdsPublicPartnerThirdPartyOrgs().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Наименование события</th>" +
                    "                   <th>ИНН</th>" +
                    "                   <th>Виды работ</th>" +
                    "               </tr>";

            String body = project.getAdsPublicPartnerThirdPartyOrgs().stream().map(indicator -> "<tr>" +
                    "            <td>" +
                    (indicator.getName() != null ? indicator.getName() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getInn() != null ? indicator.getInn() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getWorkTypes() != null ? workTypes(indicator.getWorkTypes()) : "") +
                    "            </td>" +
                    "        </tr>"
            ).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createSanctions(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        if (!Objects.equals(prevProject.getAdsSanctions(), currProject.getAdsSanctions())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(ADS_SANCTIONS);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getSanctions(prevProject));
            revision.setCurrentValue(getSanctions(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getSanctions(Project project) {
        if (project.getAdsSanctions() != null && !project.getAdsSanctions().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Виды санкций</th>" +
                    "                   <th>Дата</th>" +
                    "                   <th>Сумма тыс. руб.</th>" +
                    "                   <th>Причина</th>" +
                    "               </tr>";

            String body = project.getAdsSanctions().stream().map(indicator -> "<tr>" +
                    "            <td>" +
                    (indicator.getType() != null ? sanctionTypes(indicator.getType()) : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getDate() != null ? SIMPLE_DATE_FORMAT.format(indicator.getDate()) : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getSum() != null ? indicator.getSum() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getCause() != null ? indicator.getCause() : "") +
                    "            </td>" +
                    "        </tr>"
            ).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createJudicialActivities(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        if (!Objects.equals(prevProject.getAdsJudicialActivities(), currProject.getAdsJudicialActivities())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(ADS_JUDICIAL_ACTIVITIES);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getJudicialActivities(prevProject));
            revision.setCurrentValue(getJudicialActivities(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getJudicialActivities(Project project) {
        if (project.getAdsJudicialActivities() != null && !project.getAdsJudicialActivities().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Дата</th>" +
                    "                   <th>Предмет спора</th>" +
                    "                   <th>Судебное решение</th>" +
                    "                   <th>Ссылка на kad.arbitr.ru</th>" +
                    "                   <th>Комментарий</th>" +
                    "               </tr>";

            String body = project.getAdsJudicialActivities().stream().map(indicator -> "<tr>" +
                    "            <td>" +
                    (indicator.getDate() != null ? SIMPLE_DATE_FORMAT.format(indicator.getDate()) : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getDisputeSubject() != null ? indicator.getDisputeSubject() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getJudicialDecision() != null ? indicator.getJudicialDecision() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getKadArbitrRuUrl() != null ? indicator.getKadArbitrRuUrl() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getComment() != null ? indicator.getComment() : "") +
                    "            </td>" +
                    "        </tr>"
            ).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createOwnershipStructures(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        if (!Objects.equals(prevProject.getAdsOwnershipStructures(), currProject.getAdsOwnershipStructures())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(ADS_OWNERSHIP_STRUCTURES);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getOwnershipStructures(prevProject));
            revision.setCurrentValue(getOwnershipStructures(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getOwnershipStructures(Project project) {
        if (project.getAdsOwnershipStructures() != null && !project.getAdsOwnershipStructures().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Наименование организации</th>" +
                    "                   <th>%</th>" +
                    "               </tr>";

            String body = project.getAdsOwnershipStructures().stream().map(indicator -> "<tr>" +
                    "            <td>" +
                    (indicator.getName() != null ? indicator.getName() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getPercent() != null ? indicator.getPercent() : "") +
                    "            </td>" +
                    "        </tr>"
            ).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createFinancialStructure(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;

        checkExistValuesFinancialStructure(prevProject);
        checkExistValuesFinancialStructure(currProject);
        if (!Objects.equals(prevProject.getAdsFinancialStructure(), currProject.getAdsFinancialStructure())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(ADS_FINANCIAL_STRUCTURE);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getFinancialStructure(prevProject));
            revision.setCurrentValue(getFinancialStructure(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getFinancialStructure(Project project) {
        if (project.getAdsFinancialStructure() != null && !project.getAdsFinancialStructure().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Номер по порядку</th>" +
                    "                   <th>Наименование показателя</th>" +
                    "                   <th>Факт, тыс. рублей</th>" +
                    "               </tr>";

            String body = project.getAdsFinancialStructure().stream().map(indicator -> "<tr>" +
                    "            <td>" +
                    (indicator.getFinanceIndicator() != null ? indicator.getFinanceIndicator().getSerialNum() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getFinanceIndicator() != null ? indicator.getFinanceIndicator().getName() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getValue() != null ? indicator.getValue() : "") +
                    "            </td>" +
                    "        </tr>"
            ).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

    private void checkExistValuesFinancialStructure(Project project) {
        boolean hasValue = false;

        List<FinancialStructure> financialStructures = project.getAdsFinancialStructure();
        if (financialStructures != null) {
            for (FinancialStructure financialStructure : financialStructures) {
                if (financialStructure.getValue() != null) {
                    hasValue = true;
                }
            }

            if (!hasValue) {
                project.getAdsFinancialStructure().clear();
            }
        }
    }

    public Optional<ProjectDetailsRevision> createInvestmentBoolCriterias(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        if (!Objects.equals(prevProject.getAdsInvestmentBoolCriterias(), currProject.getAdsInvestmentBoolCriterias())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(ADS_INVESTMENT_BOOL_CRITERIAS);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getInvestmentBoolCriterias(prevProject));
            revision.setCurrentValue(getInvestmentBoolCriterias(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getInvestmentBoolCriterias(Project project) {
        if (project.getAdsInvestmentBoolCriterias() != null && !project.getAdsInvestmentBoolCriterias().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Критерий</th>" +
                    "                   <th>Значение</th>" +
                    "                   <th>Комментарий</th>" +
                    "               </tr>";

            String body = project.getAdsInvestmentBoolCriterias().stream().map(indicator -> "<tr>" +
                    "            <td>" +
                    (indicator.getFinanceIndicator() != null ? indicator.getFinanceIndicator().getName() : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getValue() != null ? indicator.getValue() ? "Да" : "Нет" : "") +
                    "            </td>" +
                    "            <td>" +
                    (indicator.getComment() != null ? indicator.getComment() : "") +
                    "            </td>" +
                    "        </tr>"
            ).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

    private String workTypes(List<WorkType> workTypes) {
        if (workTypes.isEmpty()) {
            return "";
        }
        return workTypes.stream().map(WorkType::getName).collect(Collectors.joining(", "));
    }

    private String sanctionTypes(List<SanctionType> type) {
        if (type.isEmpty()) {
            return "";
        }
        return type.stream().map(SanctionType::getName).collect(Collectors.joining(", "));
    }
}
