package ru.ibs.gasu.gchp.service.rev.util;

import org.springframework.stereotype.Service;
import ru.ibs.gasu.gchp.domain.ProjectDetailsRevision;
import ru.ibs.gasu.gchp.entities.*;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.ibs.gasu.gchp.domain.ProjectDetailsRevision.SIMPLE_DATE_FORMAT;
import static ru.ibs.gasu.gchp.domain.ProjectField.CBC_INVESTMENTS1;
import static ru.ibs.gasu.gchp.domain.ProjectField.CBC_INVESTMENTS2;
import static ru.ibs.gasu.gchp.domain.ProjectField.CBC_INVESTMENTS3;
import static ru.ibs.gasu.gchp.domain.ProjectField.CBC_INVESTMENTS4;

@Service
public class CbcRevisionUtilService {

    public Optional<ProjectDetailsRevision> createCbcInvestment1AmountRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;

        checkExistValuesCbcInvestment1Amount(prevProject);
        checkExistValuesCbcInvestment1Amount(currProject);
        if (!Objects.equals(prevProject.getCbcInvestments1(), currProject.getCbcInvestments1())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(CBC_INVESTMENTS1);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getCbcInvestment1PlanningAmount(prevProject));
            revision.setCurrentValue(getCbcInvestment1PlanningAmount(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private void checkExistValuesCbcInvestment1Amount(Project project) {
        boolean hasValue = false;

        CbcInvestments1 amount = project.getCbcInvestments1();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()) {
            for (CbcInvestments1Indicator indicator : amount.getIndicators()) {
                if (indicator.getPlan() != null || indicator.getFact() != null || !indicator.getValuesByYears().isEmpty()) {
                    hasValue = true;
                    break;
                }
            }

            if (!hasValue) {
                project.setCbcInvestments1(null);
            }
        }
    }

    private String getCbcInvestment1PlanningAmount(Project project) {
        CbcInvestments1 amount = project.getCbcInvestments1();
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

    public Optional<ProjectDetailsRevision> createCbcInvestment2AmountRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;

        checkExistValuesCbcInvestment2Amount(prevProject);
        checkExistValuesCbcInvestment2Amount(currProject);
        if (!Objects.equals(prevProject.getCbcInvestments2(), currProject.getCbcInvestments2())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(CBC_INVESTMENTS2);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getCbcInvestment2PlanningAmount(prevProject));
            revision.setCurrentValue(getCbcInvestment2PlanningAmount(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private void checkExistValuesCbcInvestment2Amount(Project project) {
        boolean hasValue = false;

        CbcInvestments2 amount = project.getCbcInvestments2();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()) {
            for (CbcInvestments2Indicator indicator : amount.getIndicators()) {
                if (indicator.getPlan() != null || indicator.getFact() != null || !indicator.getValuesByYears().isEmpty()) {
                    hasValue = true;
                    break;
                }
            }

            if (!hasValue) {
                project.setCbcInvestments2(null);
            }
        }
    }

    private String getCbcInvestment2PlanningAmount(Project project) {
        CbcInvestments2 amount = project.getCbcInvestments2();
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

    public Optional<ProjectDetailsRevision> createCbcInvestment3AmountRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;

        checkExistValuesCbcInvestment3Amount(prevProject);
        checkExistValuesCbcInvestment3Amount(currProject);
        if (!Objects.equals(prevProject.getCbcInvestments3(), currProject.getCbcInvestments3())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(CBC_INVESTMENTS3);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getCbcInvestment3PlanningAmount(prevProject));
            revision.setCurrentValue(getCbcInvestment3PlanningAmount(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private void checkExistValuesCbcInvestment3Amount(Project project) {
        boolean hasValue = false;

        CbcInvestments3 amount = project.getCbcInvestments3();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()) {
            for (CbcInvestments3Indicator indicator : amount.getIndicators()) {
                if (indicator.getPlan() != null || indicator.getFact() != null || !indicator.getValuesByYears().isEmpty()) {
                    hasValue = true;
                    break;
                }
            }

            if (!hasValue) {
                project.setCbcInvestments3(null);
            }
        }
    }

    private String getCbcInvestment3PlanningAmount(Project project) {
        CbcInvestments3 amount = project.getCbcInvestments3();
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

    public Optional<ProjectDetailsRevision> createCbcInvestment4AmountRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;

        checkExistValuesCbcInvestment4Amount(prevProject);
        checkExistValuesCbcInvestment4Amount(currProject);
        if (!Objects.equals(prevProject.getCbcInvestments4(), currProject.getCbcInvestments4())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(CBC_INVESTMENTS4);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getCbcInvestment4PlanningAmount(prevProject));
            revision.setCurrentValue(getCbcInvestment4PlanningAmount(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private void checkExistValuesCbcInvestment4Amount(Project project) {
        boolean hasValue = false;

        CbcInvestments4 amount = project.getCbcInvestments4();
        if (amount != null && amount.getIndicators() != null && !amount.getIndicators().isEmpty()) {
            for (CbcInvestments4Indicator indicator : amount.getIndicators()) {
                if (indicator.getPlan() != null || indicator.getFact() != null || !indicator.getValuesByYears().isEmpty()) {
                    hasValue = true;
                    break;
                }
            }

            if (!hasValue) {
                project.setCbcInvestments4(null);
            }
        }
    }

    private String getCbcInvestment4PlanningAmount(Project project) {
        CbcInvestments4 amount = project.getCbcInvestments4();
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
