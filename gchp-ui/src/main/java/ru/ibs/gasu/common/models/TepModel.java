package ru.ibs.gasu.common.models;

public class TepModel {
    private String id;
    private String name;
    private Double planValue;
    private Double factValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPlanValue() {
        return planValue;
    }

    public void setPlanValue(Double planValue) {
        this.planValue = planValue;
    }

    public Double getFactValue() {
        return factValue;
    }

    public void setFactValue(Double factValue) {
        this.factValue = factValue;
    }
}
