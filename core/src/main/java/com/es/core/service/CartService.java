package com.es.core.service;

import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;

import java.util.Map;
import java.util.Optional;

public interface CartService {
    Cart getCart();
    Optional<CartItem> findCartItem(Long phoneId);
    void addPhone(Long phoneId, Long quantity);
    void addPhone(String phoneModel, Long quantity);
    void update(Map<Long, Long> items);
    void remove(Long phoneId);
    void clear();
}
