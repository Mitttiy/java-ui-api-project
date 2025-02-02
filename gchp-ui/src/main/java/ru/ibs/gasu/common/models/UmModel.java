package ru.ibs.gasu.common.models;

public class UmModel {

    private Long id;
    private String name;
    private String okeiCode;

    public UmModel() {
    }

    public UmModel(Long id, String name, String okeiCode) {
        this.id = id;
        this.name = name;
        this.okeiCode = okeiCode;
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

    public String getOkeiCode() {
        return okeiCode;
    }

    public void setOkeiCode(String okeiCode) {
        this.okeiCode = okeiCode;
    }
}
