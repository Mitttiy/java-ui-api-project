package ru.ibs.gasu.common.models;

import java.util.ArrayList;
import java.util.List;

public class CircumstanceStage extends Circumstance {

    private List<Circumstance> circumstances = new ArrayList<>();
    private Long order = 0L;

    public List<Circumstance> getCircumstances() {
        return circumstances;
    }

    @Override
    public Long getRootSortField() {
        return order;
    }

    public void setCircumstances(List<Circumstance> circumstances) {
        this.circumstances = circumstances;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }
}
