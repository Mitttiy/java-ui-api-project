package ru.ibs.gasu.common.models;

import java.util.ArrayList;
import java.util.List;

public class TaxConditionMain extends TaxConditionIndicatorByYear {
    private List<TaxConditionIndicatorByYear> valuesByYears = new ArrayList<>();

    @Override
    public List<TaxConditionIndicatorByYear> getChildren() {
        return valuesByYears;
    }

    public List<TaxConditionIndicatorByYear> getValuesByYears() {
        return valuesByYears;
    }

    public void setValuesByYears(List<TaxConditionIndicatorByYear> valuesByYears) {
        this.valuesByYears = valuesByYears;
    }
}
