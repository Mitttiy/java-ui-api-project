package ru.ibs.gasu.common.models;

import java.util.ArrayList;
import java.util.List;

public class ThirdOrgModel {
    private Long id;
    private Long gid;
    private String name;
    private String inn;
    private List<WorkTypeModel> workType = new ArrayList<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public List<WorkTypeModel> getWorkType() {
        return workType;
    }

    public void setWorkType(List<WorkTypeModel> workType) {
        this.workType = workType;
    }
}
