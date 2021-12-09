package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.service.OrderService;

import com.es.phoneshop.web.dto.OrderDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/orders")
public class OrderPageController {
    @Resource
    private OrderService orderService;
    @Resource
    private ConversionService conversionService;

    @ModelAttribute
    public Order addOrder() {
        return orderService.createOrder();
    }

    @ModelAttribute("OrderDto")
    public OrderDto addOrderDto() {
        return new OrderDto();
    }

    @GetMapping
    public String getOrder() {
        return "order";
    }

    @PostMapping("/place")
    public String placeOrder(@ModelAttribute("OrderDto") @Valid OrderDto orderDto,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "order";
        }

        Order order = conversionService.convert(orderDto, Order.class);

        if (order.getOrderItems().isEmpty()) {
            return "order";
        }

        try {
            orderService.placeOrder(order);
        } catch (OutOfStockException e) {
            model.addAttribute("order", order);
            model.addAttribute("outOfStockMessage", e.getMessage());
            return "order";
        }

        return "redirect:/orderOverview/" + order.getSecureId();
    }
}
