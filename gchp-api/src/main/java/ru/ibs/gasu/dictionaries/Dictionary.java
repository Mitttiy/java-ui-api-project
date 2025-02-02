package ru.ibs.gasu.dictionaries;

enum EvictDuration {
    ShortEvict,
    LongEvict
}

public enum Dictionary {
    DIC_GASU_GCHP_FORM("DIC_GASU_GCHP_FORM", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_IMPL_METHOD("DIC_GASU_GCHP_IMPL_METHOD", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_LVL("DIC_GASU_GCHP_LVL", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_FORM_LEVEL("DIC_GASU_GCHP_FORM_LEVEL", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_FORM_SPHERE("DIC_GASU_GCHP_FORM_SPHERE", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_FORM_IMPL_METHOD("DIC_GASU_GCHP_FORM_IMPL_METHOD", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_FORM_IMPL_METHOD_AGR_GROUNDS("DIC_GASU_GCHP_FORM_IMPL_METHOD_AGR_GROUNDS", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_FORM_SPHERE_SECTOR("dicGasuGchpFormSphereSector", false, EvictDuration.ShortEvict),
    DIC_GASU_SP1("DIC_GASU_SP1", true, EvictDuration.ShortEvict),
    DIC_GASU_OKTMO("DIC_GASU_OKTMO", true, EvictDuration.LongEvict),
    DIC_GASU_GCHP_ORG("DIC_GASU_GCHP_ORG", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_SPHERE("DIC_GASU_GCHP_SPHERE", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_SECTOR("DIC_GASU_GCHP_SECTOR", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_IMPL_STATUS("DIC_GASU_GCHP_IMPL_STATUS", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_PROJECT_STATUS("DIC_GASU_GCHP_PROJECT_STATUS", true, EvictDuration.ShortEvict),

    DIC_GASU_GCHP_OBJ_KIND("DIC_GASU_GCHP_OBJ_KIND", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_FORM_SPHERE_SECTOR_OBJECT_TYPE("DIC_GASU_GCHP_FORM_SPHERE_SECTOR_OBJECT_TYPE", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_AGRMT_SUBJ("DIC_GASU_GCHP_AGRMT_SUBJ", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_RENT_OBJECT("DIC_GASU_GCHP_RENT_OBJECT", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_AGREEMENT_GROUNDS("DIC_GASU_GCHP_AGREEMENT_GROUNDS", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_COMPETITION_RESULTS("DIC_GASU_GCHP_COMPETITION_RESULTS", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_COMPETITION_RESULT_SIGN("DIC_GASU_GCHP_COMPETITION_RESULT_SIGN", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_PR_P_COST_RECOVERY_METHODS("DIC_GASU_GCHP_PR_P_COST_RECOVERY_METHODS", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_ENSURE_METHD("DIC_GASU_GCHP_ENSURE_METHD", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_OTHER_GOV_SUPPORTS("DIC_GASU_GCHP_OTHER_GOV_SUPPORTS", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_AGREEMENT_SETS("DIC_GASU_GCHP_AGREEMENT_SETS", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_CONTRACT_PRICE_OFFER("DIC_GASU_GCHP_CONTRACT_PRICE_OFFER", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_WINNER_CONTRACT_PRICE_OFFER("DIC_GASU_GCHP_WINNER_CONTRACT_PRICE_OFFER", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_OPF("DIC_GASU_GCHP_OPF", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_GOV_SUPPORT("DIC_GASU_GCHP_GOV_SUPPORT", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_IR_SOURCE("DIC_GASU_GCHP_IR_SOURCE", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_COMP_OF_COMPENSATION("DIC_GASU_GCHP_COMP_OF_COMPENSATION", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_IR_LEVEL("DIC_GASU_GCHP_IR_LEVEL", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_PAYMENT_METHOD("DIC_GASU_GCHP_PAYMENT_METHOD", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_TERMINATION_CAUSE("DIC_GASU_GCHP_TERMINATION_CAUSE", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_TERMINATION_AFTERMATH("DIC_GASU_GCHP_TERMINATION_AFTERMATH", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_CHANGES_REASON("DIC_GASU_GCHP_CHANGES_REASON", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_FORM_CHANGE_REASON("DIC_GASU_GCHP_FORM_CHANGE_REASON", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_COST_RECOVERY_METHOD("DIC_GASU_GCHP_COST_RECOVERY_METHOD", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_METHOD_OF_EXECUTE_OBLIGATION("DIC_GASU_GCHP_METHOD_OF_EXECUTE_OBLIGATION", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_PRICE_ORDER("DIC_GASU_GCHP_PRICE_ORDER", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_FIN_IND_DIM_TYPE("DIC_GASU_GCHP_FIN_IND_DIM_TYPE", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_BUSINESS_MODE_TYPE("DIC_GASU_GCHP_BUSINESS_MODE_TYPE", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_COMPETITION_CRITERION("DIC_GASU_GCHP_COMPETITION_CRITERION", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_FIN_REQUIREMENT("DIC_GASU_GCHP_FIN_REQUIREMENT", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_NO_FIN_REQUIREMENT("DIC_GASU_GCHP_NO_FIN_REQUIREMENT", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_PRIVATE_SERVICE_TYPE("DIC_GASU_GCHP_PRIVATE_SERVICE_TYPE", true, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_PUBLIC_SERVICE_TYPE("DIC_GASU_GCHP_PUBLIC_SERVICE_TYPE", true, EvictDuration.ShortEvict),
    DIC_GASU_OKEI("DIC_GASU_OKEI", true, EvictDuration.ShortEvict),
    DIC_GASU_EGRUL("DIC_GASU_EGRUL", false, EvictDuration.ShortEvict),
    DIC_GASU_EGRIP("DIC_GASU_EGRIP", false, EvictDuration.ShortEvict),
    DIC_GASU_EGRUL_FL("DIC_GASU_EGRUL_FL", false, EvictDuration.ShortEvict),
    DIC_GASU_EGRUL_FOUNDER_FOR("DIC_GASU_EGRUL_FOUNDER_FOR", false, EvictDuration.ShortEvict),
    DIC_GASU_EGRUL_FOUNDER_IND("DIC_GASU_EGRUL_FOUNDER_IND", false, EvictDuration.ShortEvict),
    DIC_GASU_EGRUL_FOUNDER_PIF("DIC_GASU_EGRUL_FOUNDER_PIF", false, EvictDuration.ShortEvict),
    DIC_GASU_EGRUL_FOUNDER_RF_MO("DIC_GASU_EGRUL_FOUNDER_RF_MO", false, EvictDuration.ShortEvict),
    DIC_GASU_EGRUL_FOUNDER_RUS("DIC_GASU_EGRUL_FOUNDER_RUS", false, EvictDuration.ShortEvict),
    DIC_GASU_EGRUL_AUTH_CAPITAL("DIC_GASU_EGRUL_AUTH_CAPITAL", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_TAX_CONDITIONS_PROJECT("DIC_GASU_GCHP_TAX_CONDITIONS_PROJECT", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_MINIMUM_GUARANT_INCOME_TYPE("DIC_GASU_GCHP_MINIMUM_GUARANT_INCOME_TYPE", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_ENABLING_PAYOUTS("DIC_GASU_GCHP_ENABLING_PAYOUTS", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_CIRCUMSTANCES_STAGES("DIC_GASU_GCHP_CIRCUMSTANCES_STAGES", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_CIRCUMSTANCES("DIC_GASU_GCHP_CIRCUMSTANCES", false, EvictDuration.ShortEvict),
    DIC_GASU_GCHP_RESULTS_PLACING_AN_OFFER("DIC_GASU_GCHP_RESULTS_PLACING_AN_OFFER", true, EvictDuration.ShortEvict);

    private final String name;
    private final boolean loadOnStart;
    private final EvictDuration evictDuration;

    Dictionary(String name, boolean loadOnStart, EvictDuration evictDuration) {
        this.name = name;
        this.loadOnStart = loadOnStart;
        this.evictDuration = evictDuration;
    }

    public String getName() {
        return name;
    }

    public boolean isLoadOnStart() {
        return loadOnStart;
    }

    public EvictDuration getEvictDuration() {
        return evictDuration;
    }

}
