package ru.ibs.gasu.common.models;

public class InvestmentIndicator {
    private Long id;
    private String name;
    private Double creationValue;
    private Double exploitationValue;

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

    public Double getCreationValue() {
        return creationValue;
    }

    public void setCreationValue(Double creationValue) {
        this.creationValue = creationValue;
    }

    public Double getExploitationValue() {
        return exploitationValue;
    }

    public void setExploitationValue(Double exploitationValue) {
        this.exploitationValue = exploitationValue;
    }
}
