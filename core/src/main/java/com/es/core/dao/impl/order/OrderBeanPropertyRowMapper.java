package com.es.core.dao.impl.order;

import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderBeanPropertyRowMapper extends BeanPropertyRowMapper<Order> {
    private static final String GET_ORDER_ITEMS_BY_ORDER_ID = "SELECT * FROM orderItems WHERE orderId = :orderId";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private OrderItemBeanPropertyRowMapper orderItemBeanPropertyRowMapper;

    public OrderBeanPropertyRowMapper(NamedParameterJdbcTemplate namedParameterJdbcTemplate, OrderItemBeanPropertyRowMapper orderItemBeanPropertyRowMapper) {
        super(Order.class);
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.orderItemBeanPropertyRowMapper = orderItemBeanPropertyRowMapper;
    }

    @Override
    public Order mapRow(ResultSet rs, int rowNumber) throws SQLException {
        Order order = new Order();

        order.setId(rs.getLong("id"));
        order.setSecureId(rs.getString("secureId"));
        order.setSubtotal(rs.getBigDecimal("subtotal"));
        order.setDeliveryPrice(rs.getBigDecimal("deliveryPrice"));
        order.setTotalPrice(rs.getBigDecimal("totalPrice"));
        order.setFirstName(rs.getString("firstName"));
        order.setLastName(rs.getString("lastName"));
        order.setDeliveryAddress(rs.getString("deliveryAddress"));
        order.setContactPhoneNo(rs.getString("contactPhoneNo"));
        order.setStatus(OrderStatus.valueOf(rs.getString("status")));
        order.setDateTime(rs.getTimestamp("dateTime").toLocalDateTime());
        order.setAdditionalInfo(rs.getString("additionalInfo"));

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("orderId", order.getId());
        List<OrderItem> orderItems = new ArrayList<>(namedParameterJdbcTemplate.query(GET_ORDER_ITEMS_BY_ORDER_ID, sqlParameterSource, orderItemBeanPropertyRowMapper));
        orderItems.forEach(item -> item.setOrder(order));
        order.setOrderItems(orderItems);

        return order;
    }
}
