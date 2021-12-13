package com.es.core.service.impl;

import com.es.core.dao.OrderDao;
import com.es.core.dao.OrderItemDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import com.es.core.service.OrderService;
import com.es.core.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    private StockService stockService;
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

        order.getOrderItems().forEach(item ->
                stockService.changeStockToReserved(item.getPhone().getId(),
                item.getQuantity().intValue()));

        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
        orderItemDao.insertOrderItems(order);
        cartService.clear();
    }

    @Override
    public Order getById(Long id) throws OrderNotFoundException {
        return orderDao.getById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    public Order getBySecureId(String secureId) {
        return orderDao.getBySecureId(secureId).orElseThrow(() -> new OrderNotFoundException(secureId));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDao.findAll();
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long id, OrderStatus orderStatus) {
        Order order = getById(id);
        order.setStatus(orderStatus);

        if (orderStatus == OrderStatus.DELIVERED) {
            order.getOrderItems().forEach(item ->
                    stockService.removeReserved(item.getPhone().getId(),
                    item.getQuantity().intValue()));
        } else if (orderStatus == OrderStatus.REJECTED) {
            order.getOrderItems().forEach(item ->
                    stockService.changeReservedToStock(item.getPhone().getId(),
                    item.getQuantity().intValue()));
        }

        orderDao.save(order);
    }

    private List<OrderItem> findOutOfStockOrderItemsInOrder(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();
        List<OrderItem> outOfStockItems = new ArrayList<>();

        orderItems.forEach(orderItem -> {
            Stock stock = stockService.getStock(orderItem.getPhone().getId());
            Integer stockQuantity = stock.getStock();
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
