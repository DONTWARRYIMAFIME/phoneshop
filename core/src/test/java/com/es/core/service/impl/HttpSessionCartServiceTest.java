package com.es.core.service.impl;

import com.es.core.dao.PhoneDao;
import com.es.core.dao.StockDao;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionCartServiceTest {
    @Mock
    private PhoneDao phoneDao;
    @Mock
    private StockDao stockDao;
    @Mock
    private Phone phone1;
    @Mock
    private Phone phone2;
    @Mock
    private Stock stock1;
    @Mock
    private Stock stock2;

    @Spy
    private Cart cart;

    @InjectMocks
    private HttpSessionCartService cartService;

    private void setupPhone(Phone phone, Long id, BigDecimal price) {
        when(phone.getId()).thenReturn(id);
        when(phone.getPrice()).thenReturn(price);
    }

    private void setupStock(Stock stock, Integer quantity) {
        when(stock.getStock()).thenReturn(quantity);
    }

    @Before
    public void setup() {
        setupPhone(phone1, 101L, BigDecimal.valueOf(101));
        setupPhone(phone2, 102L, BigDecimal.valueOf(102));
        setupStock(stock1, 101);
        setupStock(stock2, 102);

        when(phoneDao.get(phone1.getId())).thenReturn(Optional.of(phone1));
        when(stockDao.get(phone1.getId())).thenReturn(Optional.of(stock1));
    }

    @Test
    public void testAddProductToEmptyCart() {
        cartService.addPhone(phone1.getId(), 5L);

        verify(phoneDao).get(phone1.getId());
        assertEquals(List.of(new CartItem(phone1, 5L)), cartService.getCart().getItems());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartTooMuchExistingProduct() {
        cartService.addPhone(phone1.getId(), 404L);
    }

    @Test(expected = OutOfStockException.class)
    public void testAddToCartTooMuchProducts() {
        cartService.addPhone(phone1.getId(), 102L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddToCartProductWithInvalidQuantity() {
        cartService.addPhone(phone1.getId(), 0L);
    }

    @Test
    public void testUpdateCartCorrectly() {
        cartService.update(Map.of(phone1.getId(), 8L));
        verify(cart).addItem(new CartItem(phone1, 8L));
    }

    @Test(expected = OutOfStockException.class)
    public void testUpdateCartWithTooMuchProducts() {
        cartService.update(Map.of(phone1.getId(), 102L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateCartWithInvalidQuantity() {
        cartService.update(Map.of(phone1.getId(), 0L));
    }

    @Test
    public void testRemoveFromCart() {
        cartService.getCart().addItem(new CartItem(phone1, 5L));
        cartService.remove(phone1.getId());

        assertEquals(List.of(), cartService.getCart().getItems());
    }
}
