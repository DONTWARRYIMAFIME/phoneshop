package com.es.phoneshop.web.controller.pages;

import com.es.core.model.cart.Cart;
import com.es.core.service.CartService;
import com.es.phoneshop.web.controller.dto.CartItemDto;
import com.es.phoneshop.web.controller.dto.CartItemListDto;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/cart")
public class CartPageController {
    @Resource
    private CartService cartService;
    @Resource
    private Validator cartItemListDtoValidator;

    @InitBinder("CartItemListDto")
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(cartItemListDtoValidator);
    }

    @ModelAttribute("cart")
    public Cart insertCart() {
        return cartService.getCart();
    }

    @GetMapping
    public String getCart() {
        return "cart";
    }

    @ModelAttribute("CartItemListDto")
    public CartItemListDto addCartItems() {
        List<CartItemDto> cartItems = cartService.getCart().getItems().values().stream()
                .map(item -> new CartItemDto(item.getPhone().getId(), item.getQuantity()))
                .collect(Collectors.toList());

        return new CartItemListDto(cartItems);
    }

    @PutMapping
    public String updateCart(@ModelAttribute("CartItemListDto") @Valid CartItemListDto cartItems,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "cart";
        }

        Map<Long, Long> newCartItems = cartItems
                .getCartItems()
                .stream()
                .collect(Collectors.toMap(CartItemDto::getId, CartItemDto::getQuantity));

        cartService.update(newCartItems);

        return "redirect:/cart";
    }

    @DeleteMapping
    public String deletePhone(Long id) {
        cartService.remove(id);
        return "redirect:/cart";
    }
}
