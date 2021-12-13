package com.es.core.service.impl;

import com.es.core.dao.OrderDao;
import com.es.core.dao.OrderItemDao;
import com.es.core.exception.OrderNotFoundException;
import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.model.order.OrderStatus;
import com.es.core.model.phone.Phone;
import com.es.core.model.phone.Stock;
import com.es.core.service.CartService;
import com.es.core.service.StockService;
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
    private StockService stockService;
    @Mock
    private OrderItemDao orderItemDao;
    @Mock
    private CartService cartService;
    @Mock
    private BigDecimal deliveryPrice;
    @Mock
    private Cart cart;
    @Mock
    private Order order1;
    @Mock
    private Order order2;
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
        when(order1.getOrderItems()).thenReturn(orderItems);

        when(cart.getItems()).thenReturn(List.of(cartItem1, cartItem2));
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(999));
        
        when(orderDao.findAll()).thenReturn(List.of(order1, order2));
    }
    
    @Test
    public void testGetAllOrders() {
        assertEquals(List.of(order1, order2), orderService.getAllOrders());
        verify(orderDao, times(1)).findAll();
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

        when(stockService.getStock(101L)).thenReturn(stock1);
        when(stockService.getStock(102L)).thenReturn(stock2);

        orderService.placeOrder(order1);

        verify(stockService, times(2)).changeStockToReserved(anyLong(), anyInt());
        verify(orderItemDao, times(1)).insertOrderItems(order1);
        verify(orderDao, times(1)).save(order1);
        verify(cartService, times(1)).clear();
    }

    @Test(expected = OutOfStockException.class)
    public void testPlaceOrderOutOfStock() {
        Stock stock1 = new Stock(phone1, 0, 0);
        Stock stock2 = new Stock(phone2, 0, 0);

        when(stockService.getStock(101L)).thenReturn(stock1);
        when(stockService.getStock(102L)).thenReturn(stock2);

        orderService.placeOrder(order1);

        verify(stockService, times(1)).getStock(phone1.getId());
        verify(stockService, times(2)).getStock(phone2.getId());
        verify(cartService, times(1)).remove(phone1.getId());
        verify(cartService, times(1)).remove(phone2.getId());
    }

    @Test
    public void testUpdateOrderStatusToDelivered() {
        when(orderDao.getById(1L)).thenReturn(Optional.of(order1));
        orderService.updateOrderStatus(1L, OrderStatus.DELIVERED);

        verify(order1).setStatus(OrderStatus.DELIVERED);
        verify(stockService).removeReserved(phone1.getId(), 11);
        verify(stockService).removeReserved(phone2.getId(), 12);
        verify(orderDao).save(order1);
    }

    @Test
    public void testUpdateOrderStatusToRejected() {
        when(orderDao.getById(1L)).thenReturn(Optional.of(order1));
        orderService.updateOrderStatus(1L, OrderStatus.REJECTED);

        verify(order1).setStatus(OrderStatus.REJECTED);
        verify(stockService).changeReservedToStock(phone1.getId(), 11);
        verify(stockService).changeReservedToStock(phone2.getId(), 12);
        verify(orderDao).save(order1);
    }

    @Test
    public void testGetOrderByCorrectId() {
        when(orderDao.getById(101L)).thenReturn(Optional.of(order1));
        assertEquals(order1, orderService.getById(101L));
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderByIncorrectId() {
        orderService.getById(-5L);
        verify(orderDao, times(1)).getById(-5L);
    }

    @Test
    public void testGetOrderByCorrectSecureId() {
        when(orderDao.getBySecureId("secureId")).thenReturn(Optional.of(order1));
        assertEquals(order1, orderService.getBySecureId("secureId"));
    }

    @Test(expected = OrderNotFoundException.class)
    public void testGetOrderByIncorrectSecureId() {
        orderService.getBySecureId("incorrectSecureId");
        verify(orderDao, times(1)).getBySecureId("incorrectSecureId");
    }

}
