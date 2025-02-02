package ru.ibs.gasu.common.models;

import java.util.List;

public class PlanFactYear implements SortableTreeNode {
    private Long id;
    private Long gid;
    private String nameOrYear;
    private Double plan;
    private Double fact;
    private PlanFactIndicator parent;
    private String info;

    @Override
    public String getChildSortField() {
        return nameOrYear;
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
    public PlanFactYear getData() {
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

    public String getNameOrYear() {
        return nameOrYear;
    }

    public void setNameOrYear(String nameOrYear) {
        this.nameOrYear = nameOrYear;
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

    public PlanFactIndicator getParent() {
        return parent;
    }

    public void setParent(PlanFactIndicator parent) {
        this.parent = parent;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDeleteButton() {
        return "<i title='Удалить год' class='fas fa-times' style='cursor: pointer; opacity: 0.5;' onmouseover=\"this.style.opacity = '1';\" onmouseout=\"this.style.opacity = '0.5';\"></i>";
    }
}
