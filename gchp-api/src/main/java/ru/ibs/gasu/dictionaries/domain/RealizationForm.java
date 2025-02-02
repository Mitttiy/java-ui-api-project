package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * DIC_GASU_GCHP_FORM
 * справочник "Форма реализации" для фильтра
 */
@Getter
@Setter
public class RealizationForm {
    private String id;
    private String idParent;
    private String name;
    private Date periodStart;
    private Date periodEnd;
    private Boolean isLeaf;
    private Long idEdoc;
    private Boolean obsolete;
    private Date dCreated;
    private String uCreated;
    private Date dModified;
    private Integer sortOrder;
    private String uModified;
    private String dicGasuGchpProjectType;
}
