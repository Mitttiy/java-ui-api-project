package ru.ibs.gasu.common.models;

import java.util.List;

public class TepIndicatorByYear implements SortableTreeNode {
    private Long id;
    private Long gid;
    private String indicatorName;
    private UmModel um;
    private String name;
    private Double plan;
    private Double fact;

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
    public TepIndicatorByYear getData() {
        return null;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public UmModel getUm() {
        return um;
    }

    public void setUm(UmModel um) {
        this.um = um;
    }

    public String getDeleteButton() {
        return "<i title='Удалить год' class='fas fa-times' style='cursor: pointer; opacity: 0.5;' onmouseover=\"this.style.opacity = '1';\" onmouseout=\"this.style.opacity = '0.5';\"></i>";
    }
}
