package com.es.core.service;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderStatus;

import java.util.List;

public interface OrderService {
    Order createOrder();
    void placeOrder(Order order) throws OutOfStockException;
    Order getById(Long id) throws OrderNotFoundException;
    Order getBySecureId(String secureId);
    List<Order> getAllOrders();
    void updateOrderStatus(Long id, OrderStatus orderStatus);
}
