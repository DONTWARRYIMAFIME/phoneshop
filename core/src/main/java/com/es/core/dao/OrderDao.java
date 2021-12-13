package com.es.core.dao;

import com.es.core.model.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    List<Order> findAll();
    Optional<Order> getById(Long id);
    Optional<Order> getBySecureId(String secureId);
    void save(Order order);
}
