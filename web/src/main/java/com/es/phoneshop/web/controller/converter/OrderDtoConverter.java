package com.es.phoneshop.web.controller.converter;

import com.es.core.model.order.Order;
import com.es.phoneshop.web.controller.dto.OrderDto;
import org.springframework.core.convert.converter.Converter;

public class OrderDtoConverter implements Converter<OrderDto, Order> {
    private Order order;

    public OrderDtoConverter() {
        this.order = new Order();
    }

    public OrderDtoConverter(Order order) {
        this.order = order;
    }

    @Override
    public Order convert(OrderDto userDataForm) {
        order.setFirstName(userDataForm.getFirstName());
        order.setLastName(userDataForm.getLastName());
        order.setDeliveryAddress(userDataForm.getAddress());
        order.setContactPhoneNo(userDataForm.getPhone());
        order.setAdditionalInfo(userDataForm.getAdditionalInfo());

        return order;
    }
}