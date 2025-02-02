package ru.ibs.gasu.common.models;

import com.sencha.gxt.data.shared.TreeStore;

import java.util.Comparator;
import java.util.List;

public class SimpleYearIndicatorModel implements TreeStore.TreeNode<SimpleYearIndicatorModel>  {

    private Long id;
    private Long gid;
    private String name;
    private Double plan;

    public static Comparator<SimpleYearIndicatorModel> getComparator() {
        return Comparator.comparing(SimpleYearIndicatorModel::getName);
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

    @Override
    public List<? extends TreeStore.TreeNode<SimpleYearIndicatorModel>> getChildren() {
        return null;
    }

    @Override
    public SimpleYearIndicatorModel getData() {
        return null;
    }
}
