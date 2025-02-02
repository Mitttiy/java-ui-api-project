package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

/**
 * DIC_GASU_GCHP_AGREEMENT_GROUNDS
 */
@Getter
@Setter
public class AgreementGrounds {
    private Long id;
    private String name;
    private Long formId;
    private Long initMethodId;
}
