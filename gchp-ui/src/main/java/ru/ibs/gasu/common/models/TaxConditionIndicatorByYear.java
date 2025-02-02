package ru.ibs.gasu.common.models;

import com.sencha.gxt.data.shared.TreeStore;

import java.util.Comparator;
import java.util.List;

public class TaxConditionIndicatorByYear implements TreeStore.TreeNode<TaxConditionIndicatorByYear> {
    private Long id;
    private Long gid;
    private String indicatorName;
    private TaxConditionModel taxCondition;
    private String name;
    private Double tax;

    public static Comparator<TaxConditionIndicatorByYear> getComparator() {
        return Comparator.comparing(TaxConditionIndicatorByYear::getName);
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

    public String getIndicatorName() {
        return indicatorName;
    }

    public void setIndicatorName(String indicatorName) {
        this.indicatorName = indicatorName;
    }

    public TaxConditionModel getTaxCondition() {
        return taxCondition;
    }

    public void setTaxCondition(TaxConditionModel taxCondition) {
        this.taxCondition = taxCondition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    @Override
    public List<? extends TreeStore.TreeNode<TaxConditionIndicatorByYear>> getChildren() {
        return null;
    }

    @Override
    public TaxConditionIndicatorByYear getData() {
        return null;
    }
}

