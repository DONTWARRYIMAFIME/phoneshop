package com.es.phoneshop.web.converter;

import com.es.core.model.order.Order;
import com.es.core.service.OrderService;
import com.es.phoneshop.web.dto.OrderDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OrderDtoToOrderConverter implements Converter<OrderDto, Order> {
    @Resource
    private OrderService orderService;

    @Override
    public Order convert(OrderDto orderDto) {
        Order order = orderService.createOrder();

        order.setFirstName(orderDto.getFirstName());
        order.setLastName(orderDto.getLastName());
        order.setDeliveryAddress(orderDto.getAddress());
        order.setContactPhoneNo(orderDto.getPhone());
        order.setAdditionalInfo(orderDto.getAdditionalInfo());

        return order;
    }
}