package ru.ibs.gasu.gchp.service.rev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.dictionaries.domain.RentObject;
import ru.ibs.gasu.dictionaries.domain.UmResult;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.domain.ProjectDetailsRevision;
import ru.ibs.gasu.gchp.domain.UmSearchCriteria;
import ru.ibs.gasu.gchp.entities.Project;
import ru.ibs.gasu.gchp.entities.TechEconomicsIndicator;
import ru.ibs.gasu.gchp.entities.UnitOfMeasure;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.ibs.gasu.gchp.domain.ProjectField.OD_ENERGY_EFFICIENCY_PLANS;
import static ru.ibs.gasu.gchp.domain.ProjectField.OD_TECH_ECONOMICS_INDICATORS;

@Service
public class DescriptionRevisionUtilService {
    @Autowired
    private DictionaryDataService dictionaryDataService;

    public String getRentObject(Project project) {
        if (project.getOdRentObject() != null) {
            return dictionaryDataService.getAllRentObjects()
                    .stream()
                    .filter(form -> form.getId().equals(project.getOdRentObject()))
                    .map(RentObject::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createEnergyEfficiencyPlansRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        if (!Objects.equals(prevProject.getOdEnergyEfficiencyPlans(), currProject.getOdEnergyEfficiencyPlans())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(OD_ENERGY_EFFICIENCY_PLANS);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getEnergyEfficiencyPlans(prevProject));
            revision.setCurrentValue(getEnergyEfficiencyPlans(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getEnergyEfficiencyPlans(Project project) {
        if (project.getOdEnergyEfficiencyPlans() != null && !project.getOdEnergyEfficiencyPlans().isEmpty()) {
            String header = "<table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Год</th>" +
                    "                   <th>План</th>" +
                    "                   <th>Факт</th>" +
                    "                   <th>Акт о приемке</th>" +
                    "               </tr>";

            String body = project.getOdEnergyEfficiencyPlans().stream().map(efficiencyPlan ->
                    "        <tr>" +
                            "            <td>" +
                            (efficiencyPlan.getYear() != null ? efficiencyPlan.getYear() : "") +
                            "            </td>" +
                            "            <td>" +
                            (efficiencyPlan.getPlan() != null ? efficiencyPlan.getPlan() : "") +
                            "            </td>" +
                            "            <td>" +
                            (efficiencyPlan.getFact() != null ? efficiencyPlan.getFact() : "") +
                            "            </td>" +
                            "            <td>" +
                            (efficiencyPlan.getFileName() != null ? efficiencyPlan.getFileName() : "") +
                            "            </td>" +
                            "</tr>"
            ).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createTechEconomicIndicatorRevision(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;
        if (!Objects.equals(prevProject.getOdTechEconomicsObjectIndicators(), currProject.getOdTechEconomicsObjectIndicators())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(OD_TECH_ECONOMICS_INDICATORS);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getTechEconomicsIndicators(prevProject));
            revision.setCurrentValue(getTechEconomicsIndicators(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getTechEconomicsIndicators(Project project) {
        if (project.getOdTechEconomicsObjectIndicators() != null && !project.getOdTechEconomicsObjectIndicators().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Показатель/Год</th>" +
                    "                   <th>Ед.изм.</th>" +
                    "                   <th>План</th>" +
                    "                   <th>Факт</th>" +
                    "               </tr>";

            String body = project.getOdTechEconomicsObjectIndicators().stream().map(object -> {
                String objectRow = "<tr>" +
                        "            <td>" +
                        (object.getName() != null ? object.getName() : "") +
                        "            </td>" +
                        "            <tr>";
                if (object.getTechEconomicsIndicators() == null) {
                    return objectRow;
                }
                StringBuilder stringBuilder = new StringBuilder(objectRow);
                for (TechEconomicsIndicator indicator : object.getTechEconomicsIndicators()) {
                    UmSearchCriteria searchCriteria = new UmSearchCriteria();
                    searchCriteria.setIdOrName(String.valueOf(indicator.getUmId()));
                    UmResult umResult = dictionaryDataService.getFilteredOkei(searchCriteria);
                    String umName = "";
                    if (umResult != null) {
                        umName = umResult.getData().stream().findFirst().map(UnitOfMeasure::getName).orElse("");
                    }

                    stringBuilder.append("<tr>" +
                            "            <td>" +
                            (indicator.getName() != null ? "&nbsp;&nbsp;" + indicator.getName() : "") +
                            "            </td>" +
                            "            <td>" +
                            umName +
                            "            </td>" +
                            "            <td>" +
                            (indicator.getPlan() != null ? indicator.getPlan() : "") +
                            "            </td>" +
                            "            <td>" +
                            (indicator.getFact() != null ? indicator.getFact() : "") +
                            "            </td>" +
                            "        </tr>");
                        if (indicator.getYearValues() == null) {
                            return stringBuilder.toString();
                        }
                    indicator.getYearValues().forEach(yearValue -> stringBuilder.append("<tr>" +
                            "            <td>" +
                            (yearValue.getYear() != null ? "&nbsp;&nbsp;&nbsp;&nbsp;" + yearValue.getYear() : "") +
                            "            </td>" +
                            "            <td>" +
                            "            </td>" +
                            "            <td>" +
                            (yearValue.getPlan() != null ? yearValue.getPlan() : "") +
                            "            </td>" +
                            "            <td>" +
                            (yearValue.getFact() != null ? yearValue.getFact() : "") +
                            "            </td>" +
                            "        </tr>"));
                    return stringBuilder    +
                            "        </tr>" +
                            "        </tr>";
                }
                return stringBuilder.toString();
            }).collect(Collectors.joining(""));
            return header + body + "</table>";
        }
        return "";
    }

}
