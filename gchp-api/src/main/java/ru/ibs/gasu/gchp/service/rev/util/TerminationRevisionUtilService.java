package ru.ibs.gasu.gchp.service.rev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.dictionaries.domain.TerminationAftermathEntity;
import ru.ibs.gasu.dictionaries.domain.TerminationCause;
import ru.ibs.gasu.dictionaries.domain.TmCompositionOfCompensationGrantorFaultEntity;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.domain.ProjectDetailsRevision;
import ru.ibs.gasu.gchp.entities.CompositionOfCompensation;
import ru.ibs.gasu.gchp.entities.Project;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import static ru.ibs.gasu.gchp.domain.ProjectField.TM_COMPOSITION_OF_COMPENSATION;

@Service
public class TerminationRevisionUtilService {
    @Autowired
    private DictionaryDataService dictionaryDataService;

    public String getCause(Project project) {
        if (project.getTmCause() != null) {
            return dictionaryDataService.getAllTerminationCauses()
                    .stream()
                    .filter(form -> form.getId().equals(project.getTmCause()))
                    .map(TerminationCause::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getAftermath(Project project) {
        if (project.getTmAftermath() != null) {
            return dictionaryDataService.getAllTerminationAftermaths()
                    .stream()
                    .filter(form -> form.getId().equals(project.getTmAftermath()))
                    .map(TerminationAftermathEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getTmCompositionOfCompensationGrantorFault(Project project) {
        if (project.getTmCompositionOfCompensationGrantorFault() != null) {
            return dictionaryDataService.getAllTmCompositionOfCompensationGrantorFault()
                    .stream()
                    .filter(form -> form.getId().equals(project.getTmCompositionOfCompensationGrantorFault()))
                    .map(TmCompositionOfCompensationGrantorFaultEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public Optional<ProjectDetailsRevision> createCompositionOfCompensation(Project prevProject, Project currProject, String userName) {
        ProjectDetailsRevision revision = null;

        checkExistValuesCompositionOfCompensation(prevProject);
        checkExistValuesCompositionOfCompensation(currProject);
        if (!Objects.equals(prevProject.getTmCompositionOfCompensationView(), currProject.getTmCompositionOfCompensationView())) {
            revision = new ProjectDetailsRevision();
            revision.setEditedField(TM_COMPOSITION_OF_COMPENSATION);

            revision.setEditor(userName);
            revision.setDate(currProject.getUpdateDate());

            revision.setPrevValue(getCompositionOfCompensation(prevProject));
            revision.setCurrentValue(getCompositionOfCompensation(currProject));
        }
        return Optional.ofNullable(revision);
    }

    private String getCompositionOfCompensation(Project project) {
        if (project.getTmCompositionOfCompensationView() != null && !project.getTmCompositionOfCompensationView().isEmpty()) {
            String header = "    <table style='float:left; border-style:solid; border-width: 1'>" +
                    "               <tr>" +
                    "                   <th>Состав компенсации при прекращении соглашения</th>" +
                    "                   <th>Сумма выплаченной компенсации при прекращении (накопленным итогом), тыс. руб.</th>" +
                    "               </tr>";

            String body = project.getTmCompositionOfCompensationView().stream().map(indicator -> "<tr>" +
                    "            <td>" +
                    (indicator.getName() != null ? indicator.getName() : "") +
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

    private void checkExistValuesCompositionOfCompensation(Project project) {
        boolean hasValue = false;

        List<CompositionOfCompensation> compositionOfCompensations = project.getTmCompositionOfCompensationView();
        if (compositionOfCompensations != null) {
            for (CompositionOfCompensation compositionOfCompensation : compositionOfCompensations) {
                if (compositionOfCompensation.getValue() != null) {
                    hasValue = true;
                }
            }

            if (!hasValue) {
                project.getTmCompositionOfCompensationView().clear();
            }
        }
    }
}
