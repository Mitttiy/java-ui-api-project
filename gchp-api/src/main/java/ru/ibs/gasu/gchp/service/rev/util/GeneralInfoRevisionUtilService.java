package ru.ibs.gasu.gchp.service.rev.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ibs.gasu.dictionaries.domain.*;
import ru.ibs.gasu.dictionaries.service.DictionaryDataService;
import ru.ibs.gasu.gchp.entities.Project;
import ru.ibs.gasu.gchp.service.role.SvrOrg;
import ru.ibs.gasu.gchp.service.role.SvrOrgService;

import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class GeneralInfoRevisionUtilService {
    @Autowired
    private DictionaryDataService dictionaryDataService;

    @Autowired
    private SvrOrgService svrOrgService;

    public String getRealisationForm(Project project) {
        if (project.getGiRealizationForm() != null) {
            return dictionaryDataService.getAllRealizationForms()
                    .stream()
                    .filter(form -> form.getId().equals(project.getGiRealizationForm()))
                    .map(RealizationForm::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getInitiationMethod(Project project) {
        if (project.getGiInitiationMethod() != null) {
            return dictionaryDataService.getAllInitiationMethods()
                    .stream()
                    .filter(form -> form.getId().equals(project.getGiInitiationMethod()))
                    .map(InitiationMethod::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getRealizationLevel(Project project) {
        if (project.getGiRealizationLevel() != null) {
            return dictionaryDataService.getAllRealizationLevels()
                    .stream()
                    .filter(form -> form.getId().equals(project.getGiRealizationLevel()))
                    .map(RealizationLevel::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getRegion(Project project) {
        if (project.getGiRegion() != null) {
            return dictionaryDataService.getAllRFRegions()
                    .stream()
                    .filter(form -> form.getId().equals(project.getGiRegion()))
                    .map(DicGasuSp1::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getOpf(Project project) {
        if (project.getGiOPF() != null) {
            return dictionaryDataService.getAllOpfs()
                    .stream()
                    .filter(form -> form.getId().equals(project.getGiOPF()))
                    .map(OpfEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getMunicipality(Project project) {
        if (project.getGiRegion() != null && project.getGiMunicipality() != null) {
            RFMunicipalityFilters filters = new RFMunicipalityFilters();
            filters.setOktmo(project.getGiMunicipality());
            filters.setRegionId(project.getGiRegion());
            return dictionaryDataService.getFilteredMunicipality(filters)
                    .stream()
                    .filter(form -> form.getId().equals(project.getGiMunicipality()))
                    .map(Municipality::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getRealizationSphere(Project project) {
        if (project.getGiRealizationSphere() != null) {
            return dictionaryDataService.getAllRealizationSpheres()
                    .stream()
                    .filter(form -> form.getId().equals(project.getGiRealizationSphere()))
                    .map(RealizationSphereEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getRealizationSector(Project project) {
        if (project.getGiRealizationSector() != null) {
            return dictionaryDataService.getAllRealizationSectors()
                    .stream()
                    .filter(form -> form.getId().equals(project.getGiRealizationSector()))
                    .map(RealizationSectorEntity::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String getRealizationStatus(Project project) {
        if (project.getGiRealizationStatus() != null) {
            return dictionaryDataService.getAllRealizationStatuses()
                    .stream()
                    .filter(form -> form.getId().equals(project.getGiRealizationStatus()))
                    .map(RealizationStatus::getName)
                    .collect(Collectors.joining(", "));
        }
        return "";
    }

    public String objectTypes(Project project) {
        if (project.getGiObjectType() != null) {
            return dictionaryDataService.getAllObjectKinds().stream().filter(kind -> {
                Predicate<ObjectKind> predicate = p -> false;
                for (Long type : project.getGiObjectType()) {
                    predicate = predicate.or(p -> p.getId().equals(String.valueOf(type)));
                }
                return predicate.test(kind);
            }).map(ObjectKind::getName).collect(Collectors.joining(", "));
        }
        return "";
    }

    public String agreementSubject(Project project) {
        if (project.getGiAgreementSubject() != null) {
            return dictionaryDataService.getAllAgreementSubjects().stream().filter(subject -> {
                Predicate<AgreementSubject> predicate = p -> false;
                for (Long type : project.getGiObjectType()) {
                    predicate = predicate.or(p -> p.getId().equals(String.valueOf(type)));
                }
                return predicate.test(subject);
            }).map(AgreementSubject::getName).collect(Collectors.joining(", "));
        }
        return "";
    }

    public String publicPartner(Project project) {
        if (project.getGiPublicPartner() != null) {
            SvrOrg svrOrg = svrOrgService.getSvrOrgById(project.getGiPublicPartner());
            return svrOrg == null ? "" : svrOrg.getName();
        }
        return "";
    }

    public String getProjectStatus(Project project) {
        if (project.getGiProjectStatus() != null) {
            return dictionaryDataService.getAllProjectStatuses()
                    .stream()
                    .filter(partner -> partner.getId().equals(project.getGiProjectStatus())).map(ProjectStatus::getName)
                    .findFirst()
                    .orElse("");
        }
        return "";
    }
}
