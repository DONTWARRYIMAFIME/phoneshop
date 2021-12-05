package com.es.phoneshop.web.controller.dto;

import java.util.List;

public class CartItemListDto {
    private List<CartItemDto> cartItems;

    public CartItemListDto(List<CartItemDto> cartItems) {
        this.cartItems = cartItems;
    }

    public List<CartItemDto> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemDto> cartItems) {
        this.cartItems = cartItems;
    }
}
