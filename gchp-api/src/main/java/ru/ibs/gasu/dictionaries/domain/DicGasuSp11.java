package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Справочник "Муниципальные образования РФ".
 */
@Entity
@Table(name = "dic_gasu_sp11")
@Getter
@Setter
@Deprecated
public class DicGasuSp11 {
    @Id
    private String id;
    private String idParent;
    private String name;
    private Date periodStart;
    private Date periodEnd;
    private Boolean isLeaf;
    private String dicGasuSp1;
    private String idGmu;
    private String ogrn;
}
