package ru.ibs.gasu.common.models;

public class EnsureMethodModel {
    private Long id;
    private Long gid;
    private Long ensureMethodId;
    private String ensureMethodName;

    private String riskType;
    private Long submissionDate;
    private String term;
    private Double value;
    private String info;

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


    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getEnsureMethodId() {
        return ensureMethodId;
    }

    public void setEnsureMethodId(Long ensureMethodId) {
        this.ensureMethodId = ensureMethodId;
    }

    public String getEnsureMethodName() {
        return ensureMethodName;
    }

    public void setEnsureMethodName(String ensureMethodName) {
        this.ensureMethodName = ensureMethodName;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public Long getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Long submissionDate) {
        this.submissionDate = submissionDate;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
