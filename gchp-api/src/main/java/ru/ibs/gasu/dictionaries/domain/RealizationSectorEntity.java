package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

/**
 * DIC_GASU_GCHP_SECTOR
 * Отрасль реализации проекта
 */
@Getter
@Setter
public class RealizationSectorEntity {
    private String id;
    private String name;

    @XmlTransient
    private String dicGasuGchpFormSphereSector;

    private String idParent;
    private Date periodStart;
    private Date periodEnd;
    private Boolean isLeaf;
    private Long idEdoc;
    private String dicGasuGchpSphere;
    private Boolean obsolete;
    private Date dCreated;
    private String uCreated;
    private Date dModified;
    private String uModified;
    private Long sortOrder;
    private Long formId;
}
