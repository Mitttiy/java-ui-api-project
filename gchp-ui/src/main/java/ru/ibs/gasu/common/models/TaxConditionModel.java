package ru.ibs.gasu.common.models;

public class TaxConditionModel {

    private Long id;
    private String name;

    public TaxConditionModel() {
    }

    public TaxConditionModel(String name) {
        this.name = name;
    }

    public TaxConditionModel(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
