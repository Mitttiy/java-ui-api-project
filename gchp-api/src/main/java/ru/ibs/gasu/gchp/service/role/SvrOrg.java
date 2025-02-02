package ru.ibs.gasu.gchp.service.role;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "svr_orgs")
public class SvrOrg  {
    @Id
    private String id;
    private String name;
    private String shortName;
    private String sp2;
    private String sp1;
    private String isFoiv;
    private String isUfk;
    private String isGrbs;
    private String isFrguRepr;
    private String ogrn;
    private String levelType;
    private String isMup;
    private String oktmoCode;
    private String orgtype;
    private Integer rn;
    private Date periodEnd;
}
