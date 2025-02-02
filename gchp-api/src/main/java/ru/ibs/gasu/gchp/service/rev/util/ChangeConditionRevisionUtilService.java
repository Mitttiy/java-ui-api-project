package ru.ibs.gasu.gchp.service.rev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.dictionaries.domain.ChangesReasonEntity;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.entities.Project;

import java.util.stream.Collectors;

@Service
public class ChangeConditionRevisionUtilService {
    @Autowired
    private DictionaryDataService dictionaryDataService;

    public String getReason(Project project) {
        if (project.getCcReason() != null) {
            return dictionaryDataService.getAllChangesReasons()
                    .stream()
                    .filter(form -> form.getId().equals(project.getCcReason()))
                    .map(ChangesReasonEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }
}
