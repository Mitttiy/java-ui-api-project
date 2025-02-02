package ru.ibs.gasu.common.models;

public class BalanceOfDebt {
    private Long id;
    private String period;
    private Double factValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getFactValue() {
        return factValue;
    }

    public void setFactValue(Double factValue) {
        this.factValue = factValue;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

}
