package ru.ibs.gasu.common.models;

import java.util.ArrayList;
import java.util.List;

public class SanctionModel {
    private Long id;
    private Long gid;
    private List<SanctionTypeModel> sanctionType = new ArrayList<>();
    private Long date;
    private Double value;
    private String cause;

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

    public List<SanctionTypeModel> getSanctionType() {
        return sanctionType;
    }

    public void setSanctionType(List<SanctionTypeModel> sanctionType) {
        this.sanctionType = sanctionType;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
