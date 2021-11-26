package com.es.core.model.cart;

import com.es.core.model.phone.Phone;

import java.io.Serializable;
import java.util.Objects;

public class CartItem implements Serializable {
    private Phone phone;
    private Long quantity;

    public CartItem() {
    }

    public CartItem(Phone phone, Long quantity) {
        this.phone = phone;
        this.quantity = quantity;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return Objects.equals(phone, cartItem.phone) && Objects.equals(quantity, cartItem.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone, quantity);
    }
}
