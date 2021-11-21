package com.es.core.model.cart;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Component
@SessionScope
public class Cart implements Serializable {
    private Map<Long, CartItem> items = new LinkedHashMap<>();

    private long totalQuantity;
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public Cart() {
    }

    public Cart(Map<Long, CartItem> items) {
        this.items.putAll(items);
    }

    public Map<Long, CartItem> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public void setItems(Map<Long, CartItem> items) {
        this.items = items;
    }

    public void addItem(Long id, CartItem item) {
        items.put(id, item);
    }

    public void removeItem(Long id) {
        items.remove(id);
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

    public void clear() {
        items.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return totalQuantity == cart.totalQuantity && Objects.equals(items, cart.items) && Objects.equals(totalPrice, cart.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, totalQuantity, totalPrice);
    }
}
