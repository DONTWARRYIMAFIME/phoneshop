package com.es.core.dao;

import com.es.core.model.order.Order;

public interface OrderItemDao {
    void insertOrderItems(Order order);
}