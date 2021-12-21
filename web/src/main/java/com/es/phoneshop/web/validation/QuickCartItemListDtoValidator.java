package com.es.phoneshop.web.validation;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.exception.StockNotFoundException;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import com.es.core.service.PhoneService;
import com.es.core.service.StockService;
import com.es.phoneshop.web.dto.QuickCartItemDto;
import com.es.phoneshop.web.dto.QuickCartItemListDto;
import org.h2.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class QuickCartItemListDtoValidator implements Validator {
    @Resource
    private StockService stockService;
    @Resource
    private PhoneService phoneService;
    @Resource(name = "httpSessionCartService")
    private CartService cartService;

    @Override
    public boolean supports(Class<?> aClass) {
        return QuickCartItemListDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        QuickCartItemListDto items = (QuickCartItemListDto)o;
        List<QuickCartItemDto> cartItems = items.getCartItems();
        for (int i = 0; i < cartItems.size(); i++) {
            QuickCartItemDto cartItemDto = cartItems.get(i);
            if (StringUtils.isNullOrEmpty(cartItemDto.getModel()) && cartItemDto.getQuantity() == null) {
                continue;
            }

            long quantity = Optional.ofNullable(cartItemDto.getQuantity()).orElse(0L);
            if (quantity < 1) {
                errors.rejectValue("cartItems[" + i + "].quantity", "validation.quantity.lessThanOne");
            }

            try {
                Phone phone = phoneService.getPhoneByModel(cartItemDto.getModel());
                Stock stock = stockService.getStock(phone.getId());

                long inCartQuantity = cartService.findCartItem(phone.getId())
                        .map(CartItem::getQuantity)
                        .orElse(0L);

                if (quantity > stock.getStock() - inCartQuantity) {
                    errors.rejectValue("cartItems[" + i + "].quantity", "validation.quantity.outOfStock");
                }
            } catch (PhoneNotFoundException | StockNotFoundException e) {
                errors.rejectValue("cartItems[" + i + "].model", "validation.model.phoneNotFound");
            }

        }
    }

}