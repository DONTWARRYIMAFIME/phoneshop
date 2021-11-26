package com.es.core.model.search;

public class SearchStructure {
    private String query;
    private SortField sortField;
    private SortOrder order;

    public SearchStructure(String search, SortField sortField, SortOrder order) {
        this.query = search;
        this.sortField = sortField;
        this.order = order;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public SortField getSortField() {
        return sortField;
    }

    public void setSortField(SortField sortField) {
        this.sortField = sortField;
    }

    public SortOrder getOrder() {
        return order;
    }

    public void setOrder(SortOrder order) {
        this.order = order;
    }
}
