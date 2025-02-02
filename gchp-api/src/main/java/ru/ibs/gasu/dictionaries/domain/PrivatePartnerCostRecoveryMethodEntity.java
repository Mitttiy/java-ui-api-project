package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DIC_GASU_GCHP_PR_P_COST_RECOVERY_METHODS
 */
@Getter
@Setter
public class PrivatePartnerCostRecoveryMethodEntity {
    private Long id;
    private String name;
}
