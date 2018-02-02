package com.bassoon.stockanalyzer.model;

import java.io.Serializable;
import java.util.Collection;

public class PageResult<T> implements Serializable {
    private long total;
    private int page;
    private int limit;
    private Collection<T> result;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Collection<T> getResult() {
        return result;
    }

    public void setResult(Collection<T> result) {
        this.result = result;
    }
}
