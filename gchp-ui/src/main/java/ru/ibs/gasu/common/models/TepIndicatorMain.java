package ru.ibs.gasu.common.models;

import java.util.ArrayList;
import java.util.List;

public class TepIndicatorMain extends TepIndicatorByYear {
    private List<TepIndicatorByYear> valuesByYears = new ArrayList<>();

    @Override
    public List<TepIndicatorByYear> getChildren() {
        return valuesByYears;
    }

    public List<TepIndicatorByYear> getValuesByYears() {
        return valuesByYears;
    }

    public void setValuesByYears(List<TepIndicatorByYear> valuesByYears) {
        this.valuesByYears = valuesByYears;
    }
}
