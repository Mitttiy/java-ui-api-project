package ru.ibs.gasu.gchp.domain;

/**
 * Criteria пагинации
 */
public class PagingSearchCriteria {

    private int limit;

    private int offset;

    private SortInfo sortInfo;

    public PagingSearchCriteria() {
    }

    public PagingSearchCriteria(int limit, int offset, SortInfo sortInfo) {
        this.limit = limit;
        this.offset = offset;
        this.sortInfo = sortInfo;
    }

    public int getPage() {
        return limit == 0 ? 0 : offset / limit;
    }

    public int getLimit() {
        return limit == 0 ? 1000000 : limit;
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

    public SortInfo getSortInfo() {
        return sortInfo;
    }

    public void setSortInfo(SortInfo sortInfo) {
        this.sortInfo = sortInfo;
    }
}
