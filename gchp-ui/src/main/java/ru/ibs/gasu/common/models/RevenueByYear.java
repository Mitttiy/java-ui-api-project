package ru.ibs.gasu.common.models;

import java.util.List;

public class RevenueByYear implements SortableTreeNode {
    private Long id;
    private Long gid;
    private String name;
    private String um;
    private Double plan;
    private Double fact;
    private RevenueMain parent;
    private String info;

    @Override
    public String getChildSortField() {
        return name;
    }

    @Override
    public Long getRootSortField() {
        return gid;
    }

    @Override
    public List<? extends SortableTreeNode> getChildren() {
        return null;
    }

    @Override
    public RevenueByYear getData() {
        return null;
    }

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

    public Double getPlan() {
        return plan;
    }

    public void setPlan(Double plan) {
        this.plan = plan;
    }

    public Double getFact() {
        return fact;
    }

    public void setFact(Double fact) {
        this.fact = fact;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public RevenueMain getParent() {
        return parent;
    }

    public void setParent(RevenueMain parent) {
        this.parent = parent;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
