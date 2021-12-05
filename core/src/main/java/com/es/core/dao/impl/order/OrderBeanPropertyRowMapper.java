package com.es.core.dao.impl.order;

import com.es.core.dao.PhoneDao;
import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderBeanPropertyRowMapper extends BeanPropertyRowMapper<Order> {
    private final PhoneDao phoneDao;

    public OrderBeanPropertyRowMapper(PhoneDao phoneDao) {
        super(Order.class);
        this.phoneDao = phoneDao;
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
        order.setAdditionalInfo(rs.getString("additionalInfo"));

        do {
            Long phoneId = rs.getLong("phoneId");
            Phone phone = phoneDao.get(phoneId).orElseThrow(() -> new PhoneNotFoundException(phoneId));
            Long quantity = rs.getLong("quantity");

            OrderItem orderItem = new OrderItem(phone, order, quantity);
            order.getOrderItems().add(orderItem);
        } while (rs.next());

        return order;
    }
}
