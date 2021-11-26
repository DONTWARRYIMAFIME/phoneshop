package com.es.core.model.search;

import java.util.stream.Stream;

public enum SortField {
    BRAND, MODEL, DISPLAYSIZEINCHES, PRICE;

    public static SortField safeValueOf(String field) {
        return Stream
                .of(values())
                .filter(value -> value.toString().equalsIgnoreCase(field))
                .findAny()
                .orElse(null);
    }
}
