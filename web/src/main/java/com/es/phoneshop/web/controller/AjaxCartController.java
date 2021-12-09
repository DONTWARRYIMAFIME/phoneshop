package com.es.phoneshop.web.controller;

import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import com.es.phoneshop.web.dto.CartDto;
import com.es.phoneshop.web.dto.CartItemDto;
import com.es.phoneshop.web.exception.InvalidFormatException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/ajaxCart")
public class AjaxCartController {
    @Resource
    private CartService cartService;

    @GetMapping
    public CartDto getCart() {
        Cart cart = cartService.getCart();
        return new CartDto(cart.getTotalQuantity(), cart.getTotalPrice());
    }

    @PostMapping
    public CartDto addPhone(@RequestBody @Valid CartItemDto phoneDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidFormatException();
        }

        cartService.addPhone(phoneDto.getId(), phoneDto.getQuantity());
        Cart cart = cartService.getCart();

        return new CartDto(cart.getTotalQuantity(), cart.getTotalPrice());
    }
}
