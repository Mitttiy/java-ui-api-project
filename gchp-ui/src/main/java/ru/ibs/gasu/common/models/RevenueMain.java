package ru.ibs.gasu.common.models;

import java.util.ArrayList;
import java.util.List;

public class RevenueMain extends RevenueByYear {

    private List<RevenueByYear> valuesByYears = new ArrayList<>();
    private Long order = 0L;

    @Override
    public Long getRootSortField() {
        return order;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    @Override
    public List<RevenueByYear> getChildren() {
        return valuesByYears;
    }

    public List<RevenueByYear> getValuesByYears() {
        return valuesByYears;
    }

    public void setValuesByYears(List<RevenueByYear> valuesByYears) {
        this.valuesByYears = valuesByYears;
    }
}
