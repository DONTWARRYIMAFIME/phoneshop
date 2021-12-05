package com.es.core.service.impl;

import com.es.core.dao.OrderDao;
import com.es.core.dao.OrderItemDao;
import com.es.core.dao.StockDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.exception.StockNotFoundException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import com.es.core.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private Cart cart;
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderItemDao orderItemDao;
    @Resource
    private StockDao stockDao;
    @Resource
    private CartService cartService;

    private BigDecimal deliveryPrice;

    public void setDeliveryPrice(BigDecimal deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    @Override
    public Order createOrder() {
        Order order = new Order();
        order.setSubtotal(cart.getTotalPrice());
        order.setDeliveryPrice(deliveryPrice);
        order.setTotalPrice(cart.getTotalPrice().add(deliveryPrice));

        cart.getItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setPhone(cartItem.getPhone());
            orderItem.setQuantity(cartItem.getQuantity());

            order.getOrderItems().add(orderItem);
        });

        return order;
    }

    @Override
    @Transactional
    public void placeOrder(Order order) throws OutOfStockException {
        List<OrderItem> outOfStockOrderItems = findOutOfStockOrderItemsInOrder(order);

        if (!outOfStockOrderItems.isEmpty()) {
            //Removing outOfStockItems from order and cart
            order.getOrderItems().removeAll(outOfStockOrderItems);
            removeOutOfStockItemsFromCart(outOfStockOrderItems);

            order.setSubtotal(cart.getTotalPrice());
            order.setTotalPrice(cart.getTotalPrice().add(deliveryPrice));

            String outOfStockPhoneIds = outOfStockOrderItems.stream()
                    .map(orderItem -> orderItem.getPhone().getModel())
                    .collect(Collectors.joining(", "));

            throw new OutOfStockException("Selected products out of stock: " + outOfStockPhoneIds);
        }

        order.getOrderItems().forEach(orderItem -> {
            Long phoneId = orderItem.getPhone().getId();

            Optional<Stock> stockOptional = stockDao.get(phoneId);
            Stock stock = stockOptional.orElseThrow(() -> new StockNotFoundException(phoneId));

            stock.setStock((int) (stock.getStock() - orderItem.getQuantity()));
            stock.setReserved((int) (stock.getReserved() + orderItem.getQuantity()));

            stockDao.save(stock);
        });

        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
        orderItemDao.insertOrderItems(order);
        cartService.clear();
    }

    @Override
    public Order getBySecureId(String secureId) {
        return orderDao.getBySecureId(secureId).orElseThrow(() -> new OrderNotFoundException(secureId));
    }

    private List<OrderItem> findOutOfStockOrderItemsInOrder(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItem> outOfStockItems = new ArrayList<>();

        orderItems.forEach(orderItem -> {
            Optional<Stock> stockOptional = stockDao.get(orderItem.getPhone().getId());
            Integer stockQuantity = stockOptional.map(Stock::getStock).orElse(0);
            if (stockQuantity < orderItem.getQuantity()) {
                outOfStockItems.add(orderItem);
            }
        });

        return outOfStockItems;
    }

    private void removeOutOfStockItemsFromCart(List<OrderItem> outOfStockOrderItems) {
        outOfStockOrderItems.forEach(item -> cartService.remove(item.getPhone().getId()));
    }

}
