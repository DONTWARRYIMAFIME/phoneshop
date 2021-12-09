package com.es.core.service.impl;

import com.es.core.dao.OrderDao;
import com.es.core.dao.OrderItemDao;
import com.es.core.dao.StockDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceImplTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private StockDao stockDao;
    @Mock
    private OrderItemDao orderItemDao;
    @Mock
    private CartService cartService;
    @Mock
    private BigDecimal deliveryPrice;
    @Mock
    private Cart cart;
    @Mock
    private Order order;
    @Mock
    private OrderItem orderItem1;
    @Mock
    private OrderItem orderItem2;
    @Mock
    private CartItem cartItem1;
    @Mock
    private CartItem cartItem2;
    @Mock
    private Phone phone1;
    @Mock
    private Phone phone2;
    @Spy
    private ArrayList<OrderItem> orderItems;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Before
    public void init() {
        when(phone1.getId()).thenReturn(101L);
        when(phone2.getId()).thenReturn(102L);

        when(orderItem1.getPhone()).thenReturn(phone1);
        when(orderItem2.getPhone()).thenReturn(phone2);

        when(orderItem1.getQuantity()).thenReturn(11L);
        when(orderItem2.getQuantity()).thenReturn(12L);

        orderItems.addAll(List.of(orderItem1, orderItem2));

        when(order.getOrderItems()).thenReturn(orderItems);

        when(cart.getItems()).thenReturn(List.of(cartItem1, cartItem2));
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(999));
    }

    @Test
    public void testCreateOrder() {
        Order order = orderService.createOrder();

        assertEquals(2, order.getOrderItems().size());
        assertEquals(new BigDecimal(999), order.getTotalPrice());
    }

    @Test
    public void testPlaceOrder() {
        Stock stock1 = new Stock(phone1, 14, 0);
        Stock stock2 = new Stock(phone2, 25, 0);

        when(stockDao.get(101L)).thenReturn(Optional.of(stock1));
        when(stockDao.get(102L)).thenReturn(Optional.of(stock2));

        orderService.placeOrder(order);

        assertEquals(3, stock1.getStock().intValue());
        assertEquals(13, stock2.getStock().intValue());

        verify(stockDao, times(1)).save(stock1);
        verify(stockDao, times(1)).save(stock2);
        verify(orderDao, times(1)).save(order);
        verify(cartService, times(1)).clear();
    }

    @Test(expected = OutOfStockException.class)
    public void testPlaceOrderOutOfStock() {
        orderService.placeOrder(order);

        verify(stockDao, times(1)).get(phone1.getId());
        verify(stockDao, times(2)).get(phone2.getId());
        verify(cartService, times(1)).remove(phone1.getId());
        verify(cartService, times(1)).remove(phone2.getId());
    }

    @Test
    public void testGetOrderByCorrectSecureId() {
        when(orderDao.getBySecureId("secureId")).thenReturn(Optional.of(order));
        assertEquals(order, orderService.getBySecureId("secureId"));
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderByIncorrectSecureId() {
        orderService.getBySecureId("incorrectSecureId");
        verify(orderDao, times(1)).getBySecureId("incorrectSecureId");
    }

}
