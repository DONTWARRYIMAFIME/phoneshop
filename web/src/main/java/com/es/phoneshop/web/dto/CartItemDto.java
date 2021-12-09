package com.es.phoneshop.web.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartItemDto {
    @NotNull
    private Long id;
    @NotNull
    @Min(1)
    private Long quantity;

    public CartItemDto() {
    }

    public CartItemDto(Long id, Long quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
