package com.es.phoneshop.web.controller.pages;

import com.es.core.service.CartService;
import com.es.phoneshop.web.dto.QuickCartItemDto;
import com.es.phoneshop.web.dto.QuickCartItemListDto;
import com.es.phoneshop.web.validation.QuickCartItemListDtoValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/quickOrder")
public class QuickOrderPageController {
    @Resource
    private CartService cartService;
    @Resource(name = "quickCartItemListDtoValidator")
    private QuickCartItemListDtoValidator validator;
    @Value("${quickOrder.rows.count}")
    private int rowsCount;

    @InitBinder("quickCartItemListDto")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @ModelAttribute
    public QuickCartItemListDto addCartItems() {
        ArrayList<QuickCartItemDto> cartItems = new ArrayList<>();

        for (int i = 0; i < rowsCount; i++) {
            cartItems.add(new QuickCartItemDto());
        }

        return new QuickCartItemListDto(cartItems);
    }

    @GetMapping
    public String showQuickOrder() {
        return "quickOrder";
    }

    @PostMapping
    public String addToCart(@ModelAttribute("quickCartItemListDto") @Valid QuickCartItemListDto cartItemList,
                            BindingResult bindingResult, Model model) {
        List<String> successMessages = new ArrayList<>();

        for (int i = 0; i < cartItemList.getCartItems().size(); i++) {
            if (!hasValidationErrors(bindingResult, i)) {
                QuickCartItemDto cartItem = cartItemList.getCartItems().get(i);

                if (cartItem.getModel() != null && cartItem.getQuantity() != null) {
                    cartService.addPhone(cartItem.getModel(), cartItem.getQuantity());
                    successMessages.add("Phone with MODEL: " + cartItem.getModel() + " added successfully");

                    resetQuickCartItemDtoFields(cartItem);
                }

            }
        }

        model.addAttribute("success", successMessages);
        return "quickOrder";
    }

    private boolean hasValidationErrors(BindingResult bindingResult, int index) {
        return bindingResult.hasFieldErrors("cartItems[" + index + "].model") ||
               bindingResult.hasFieldErrors("cartItems[" + index + "].quantity");
    }

    private void resetQuickCartItemDtoFields(QuickCartItemDto cartItem) {
        cartItem.setModel(null);
        cartItem.setQuantity(null);
    }

}
