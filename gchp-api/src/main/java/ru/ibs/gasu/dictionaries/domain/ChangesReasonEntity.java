package ru.ibs.gasu.dictionaries.domain;


import lombok.Getter;
import lombok.Setter;

/**
 * DIC_GASU_GCHP_CHANGES_REASON
 */
@Getter
@Setter
public class ChangesReasonEntity {
    private Long id;
    private String name;
    private Long formId;
}
