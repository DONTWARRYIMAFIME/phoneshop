package com.es.phoneshop.web.controller.validation;

import com.es.core.dao.StockDao;
import com.es.core.model.phone.Stock;
import com.es.phoneshop.web.controller.dto.CartItemDto;
import com.es.phoneshop.web.controller.dto.CartItemListDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class CartItemListDtoValidator implements Validator {
    @Resource
    private StockDao stockDao;

    @Override
    public boolean supports(Class<?> aClass) {
        return CartItemListDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CartItemListDto cartItemListDto = (CartItemListDto)o;
        List<CartItemDto> cartItems = cartItemListDto.getCartItems();
        for (int i = 0; i < cartItems.size(); i++) {
            CartItemDto cartItemDto = cartItems.get(i);
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cartItems[" + i + "].id", "cartItem.id.empty", "Product id is empty");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cartItems[" + i + "].quantity", "cartItem.quantity.empty", "Product quantity is empty");

            Long quantity = cartItemDto.getQuantity();
            if (quantity != null && quantity < 1) {
                errors.rejectValue("cartItems[" + i + "].quantity", "cartItem.quantity.lessThenOne", "Quantity cannot be less then 1");
            }

            Optional<Stock> stock = stockDao.get(cartItemDto.getId());
            if (cartItemDto.getQuantity() > stock.map(Stock::getStock).orElse(0)) {
                errors.rejectValue("cartItems[" + i + "].quantity", "cartItem.quantity.outOfStock", "Out of stock");
            }
        }
    }
}
