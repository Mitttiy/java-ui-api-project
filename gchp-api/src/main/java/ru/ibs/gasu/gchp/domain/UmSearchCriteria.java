package ru.ibs.gasu.gchp.domain;

public class UmSearchCriteria {

    /**
     * ОКЕИ или Наименование - поиск на вхождение
     */
    private String idOrName;
    private int limit;
    private int offset;

    public String getIdOrName() {
        return idOrName;
    }

    public void setIdOrName(String idOrName) {
        this.idOrName = idOrName;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
