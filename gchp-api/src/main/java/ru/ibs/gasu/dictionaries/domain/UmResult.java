package ru.ibs.gasu.dictionaries.domain;

import ru.ibs.gasu.gchp.entities.UnitOfMeasure;

import java.util.List;

public class UmResult {
    private List<UnitOfMeasure> data;
    private Integer totalLength;
    private Integer offset;

    public List<UnitOfMeasure> getData() {
        return data;
    }

    public void setData(List<UnitOfMeasure> data) {
        this.data = data;
    }

    public Integer getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Integer totalLength) {
        this.totalLength = totalLength;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}
