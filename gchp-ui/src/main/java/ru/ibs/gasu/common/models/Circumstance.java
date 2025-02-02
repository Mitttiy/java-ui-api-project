package ru.ibs.gasu.common.models;

import com.sencha.gxt.data.shared.TreeStore;

import java.util.List;
import java.util.Objects;

public class Circumstance implements SortableTreeNode {

    private Long id;
    private Long gid;
    private String name;
    private Double sum;
    private String years;
    private CircumstanceStage parent;

    public Circumstance() {

    }

    public Circumstance(Long gid, String name) {
        this.gid = gid;
        this.name = name;
    }

    @Override
    public String getChildSortField() {
        return name;
    }

    @Override
    public Long getRootSortField() {
        return gid;
    }

    @Override
    public List<? extends TreeStore.TreeNode<SortableTreeNode>> getChildren() {
        return null;
    }

    @Override
    public SortableTreeNode getData() {
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

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public CircumstanceStage getParent() {
        return parent;
    }

    public void setParent(CircumstanceStage parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circumstance that = (Circumstance) o;
        return Objects.equals(gid, that.gid) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gid, name);
    }
}
