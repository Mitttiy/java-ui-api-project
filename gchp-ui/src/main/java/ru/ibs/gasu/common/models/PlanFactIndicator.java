package ru.ibs.gasu.common.models;

import java.util.ArrayList;
import java.util.List;

public class PlanFactIndicator extends PlanFactYear {

    private List<PlanFactYear> valuesByYears = new ArrayList<>();
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
    public List<PlanFactYear> getChildren() {
        return valuesByYears;
    }

    public List<PlanFactYear> getValuesByYears() {
        return valuesByYears;
    }

    public void setValuesByYears(List<PlanFactYear> valuesByYears) {
        this.valuesByYears = valuesByYears;
    }

}
