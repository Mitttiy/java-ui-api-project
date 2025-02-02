package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * dic_gasu_sp1
 * Справочник "Регион РФ" для фильтра.
 */
@Getter
@Setter
public class DicGasuSp1 {
    private String id;
    private String idParent;
    private String name;
    private Date periodStart;
    private Date periodEnd;
    private Boolean isLeaf;
    private String attrbudget;
    private String attrokato;
    private String attrufk;
    private Long idEdoc;
    private String attrcode;
    private String oktmo;
    private String admCntr;
    private Double x;
    private Double y;
    private String actualCode;
    private Boolean bActual;
}
