package com.es.core.dao.impl.order;

import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.PhoneService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OrderItemBeanPropertyRowMapper extends BeanPropertyRowMapper<OrderItem> {
    private PhoneService phoneService;

    public OrderItemBeanPropertyRowMapper(PhoneService phoneService) {
        super(OrderItem.class);
        this.phoneService = phoneService;
    }

    @Override
    public OrderItem mapRow(ResultSet rs, int rowNumber) throws SQLException {
        OrderItem orderItem = super.mapRow(rs, rowNumber);

        Long phoneId = rs.getLong("phoneId");
        Phone phone = phoneService.getPhone(phoneId);

        orderItem.setPhone(phone);

        return orderItem;
    }
}
