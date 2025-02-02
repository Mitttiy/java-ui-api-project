package ru.ibs.gasu.common.models;

public class EemModel {
    private Long id;
    private Integer period;
    private Double planValue;
    private Double factValue;
    private FileModel fileModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public FileModel getFileModel() {
        return fileModel;
    }

    public void setFileModel(FileModel fileModel) {
        this.fileModel = fileModel;
    }
}
