package ru.ibs.gasu.gchp.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RolePreferences {
    private boolean admin = false;

    //только GCHPUser
    private boolean user = false;

    private boolean canRegistry;
    private boolean canEdit;
    private boolean canDelete;
    private boolean access = true;
    private String accessErr;

    private Set<String> orgIds = new HashSet<>();
    private Set<String> activeOrgIds = new HashSet<>();

    private String ogrn;
    private String oktmoCode;
    private String regionId;
    private Long realizationForm;
    private Long realizationLevel;
    private Long realizationSector;
    private Long realizationSphere;

    private boolean filterByOrg;
    private boolean filterByOrgDocumentForm;

    private boolean filterByOktmo;
    private boolean filterByOktmoDocumentForm;

    private boolean filterByRegion;
    private boolean filterByRegionDocumentForm;

    private boolean filterByRealizationLevel;
    private boolean filterByRealizationForm;
    private boolean filterByRealizationSector;
    private boolean filterByRealizationSphere;

    private boolean canChangeStatus;
    private boolean canDoStatusDraft;

    private Set<UserRole> userRoles = new HashSet<>();
    private OrgLevel orgLevel;

    public static boolean isCurator(String role) {
        return "admin_gchp".equals(role);
    }

    public static boolean isController(String role) {
        return "gchp_analysts".equals(role);
    }

    public static boolean isControllerRoiv(String role) {
        return "gchp_curator".equals(role);
    }

    public static boolean isConcendent(String role) {
        return "gchp_user".equals(role);
    }

    public static boolean isMinistryCurator(String role) {
        return "minstroy_gchp".equals(role);
    }

    public static boolean isTSCurator(String role) {
        return "mintrans_gchp".equals(role);
    }

    public static boolean isOZCurator(String role) {
        return "curator_gchp_public_healthcare".equals(role);
    }

    public static boolean isFinCurator(String role) {
        return "minfin_gchp".equals(role);
    }
}
