package com.es.phoneshop.web.controller.validation;

import com.es.core.dao.StockDao;
import com.es.core.model.phone.Stock;
import com.es.phoneshop.web.controller.dto.CartItemDto;
import com.es.phoneshop.web.controller.dto.CartItemListDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class CartItemListDtoValidator implements Validator {
    @Value("${validation.id.empty}")
    private String idEmpty;
    @Value("${validation.quantity.empty}")
    private String quantityEmpty;
    @Value("${validation.outOfStock}")
    private String outOfStock;
    @Value("${validation.lessThenOne}")
    private String lessThenOne;

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
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cartItems[" + i + "].id", "cartItem.id.empty", idEmpty);
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cartItems[" + i + "].quantity", "cartItem.quantity.empty", quantityEmpty);

            Long quantity = cartItemDto.getQuantity();
            if (quantity != null && quantity < 1) {
                errors.rejectValue("cartItems[" + i + "].quantity", "validation.lessThenOne", lessThenOne);
            }

            Optional<Stock> stock = stockDao.get(cartItemDto.getId());
            if (cartItemDto.getQuantity() > stock.map(Stock::getStock).orElse(0)) {
                errors.rejectValue("cartItems[" + i + "].quantity", "validation.outOfStock", outOfStock);
            }
        }
    }
}
