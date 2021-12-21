package com.es.phoneshop.web.dto;

import java.util.List;

public class QuickCartItemListDto {
    private List<QuickCartItemDto> cartItems;

    public QuickCartItemListDto(List<QuickCartItemDto> cartItems) {
        this.cartItems = cartItems;
    }

    public List<QuickCartItemDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<QuickCartItemDto> cartItems) {
        this.cartItems = cartItems;
    }
}
