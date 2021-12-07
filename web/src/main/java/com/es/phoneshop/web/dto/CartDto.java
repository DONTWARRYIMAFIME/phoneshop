package com.es.phoneshop.web.dto;

import java.math.BigDecimal;

public class CartDto {
    private long totalQuantity;
    private BigDecimal totalPrice;

    public CartDto() {
    }

    public CartDto(long totalQuantity, BigDecimal totalPrice) {
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
