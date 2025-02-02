package ru.ibs.gasu.common.models;

public class PrivatePartnersOwningPartModel {
    private Long id;
    private Long gid;
    private String orgName;
    private Double percent;
    private String orgCapitalValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public String getOrgCapitalValue() {
        return orgCapitalValue;
    }

    public void setOrgCapitalValue(String orgCapitalValue) {
        this.orgCapitalValue = orgCapitalValue;
    }
}
