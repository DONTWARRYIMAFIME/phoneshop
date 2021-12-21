package com.es.core.service.impl;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import com.es.core.service.StockService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpSessionCartService implements CartService {
    @Resource
    private Cart cart;
    @Resource
    private PhoneService phoneService;
    @Resource
    private StockService stockService;

    @Override
    public Cart getCart() {
        return cart;
    }

    @Override
    public void addPhone(Long phoneId, Long quantity) {
        Phone phone = phoneService.getPhoneById(phoneId);
        addPhone(phone, quantity);
    }

    @Override
    public void addPhone(String phoneModel, Long quantity) {
        Phone phone = phoneService.getPhoneByModel(phoneModel);
        addPhone(phone, quantity);
    }

    private void addPhone(Phone phone, Long quantity) {
        Long inCartQuantity = findCartItem(phone.getId())
                .map(CartItem::getQuantity)
                .orElse(0L);

        addItemToCart(phone, quantity, inCartQuantity);
    }

    @Override
    public void update(Map<Long, Long> items) {
        for (Map.Entry<Long, Long> entry : items.entrySet()) {
            Long phoneId = entry.getKey();
            Long quantity = entry.getValue();

            Phone phone = phoneService.getPhoneById(phoneId);
            addItemToCart(phone, quantity, 0L);
        }
    }

    @Override
    public void remove(Long phoneId) {
        findCartItem(phoneId).ifPresent(item -> {
            cart.removeItem(item);
            recalculateTotalQuantityAndPrice();
        });
    }

    @Override
    public void clear() {
        cart.clear();
        recalculateTotalQuantityAndPrice();
    }

    public Optional<CartItem> findCartItem(Long phoneId) {
        return cart.getItems().stream()
                .filter(item -> item.getPhone().getId().equals(phoneId))
                .findAny();
    }

    private void addItemToCart(Phone phone, Long quantity, Long inCartQuantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be more than 0");
        }

        Stock stock = stockService.getStock(phone.getId());
        Long quantityInStock = (long)stock.getStock();

        Long requestQuantity = inCartQuantity + quantity;
        if (requestQuantity > quantityInStock) {
            throw new OutOfStockException(quantity, quantityInStock - inCartQuantity);
        }

        findCartItem(phone.getId()).ifPresentOrElse(
                item -> item.setQuantity(requestQuantity),
                () -> cart.addItem(new CartItem(phone, requestQuantity))
        );

        recalculateTotalQuantityAndPrice();
    }

    private void recalculateTotalQuantityAndPrice() {
        long totalQuantity = cart
                .getItems()
                .stream()
                .mapToLong(CartItem::getQuantity)
                .sum();

        BigDecimal totalPrice = cart
                .getItems()
                .stream()
                .map(item -> item.getPhone().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalQuantity(totalQuantity);
        cart.setTotalPrice(totalPrice);
    }
}
