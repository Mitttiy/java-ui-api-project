package ru.ibs.gasu.dictionaries.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AgreementSubject {
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
    private String uModified;
}
