package com.es.phoneshop.web.controller.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PhoneDto {
    @NotNull
    private Long id;
    @Min(1)
    @NotNull
    private Long quantity;

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
