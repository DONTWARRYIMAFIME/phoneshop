package com.es.phoneshop.web.controller.pages.admin;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class OrdersPageControllerTest {
    private static final String URL = "/admin/orders";

    @Mock
    private OrderService orderService;
    @Mock
    private Order order1;
    @Mock
    private Order order2;
    @Spy
    private ArrayList<Order> orders;

    @InjectMocks
    private OrdersPageController ordersPageController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(ordersPageController)
                .setSingleView(new InternalResourceView("/WEB-INF/pages/admin/orders.jsp"))
                .build();

        orders.addAll(Arrays.asList(order1, order2));
    }

    @Test
    public void testGetOrders() throws Exception {
        when(orderService.getAllOrders()).thenReturn(orders);

        mockMvc.perform(get(URL))
                .andExpect(model().attribute("orders", orders))
                .andExpect(view().name("admin/orders"));
    }

    @Test
    public void testGetOrder() throws Exception {
        when(orderService.getById(1L)).thenReturn(order1);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(model().attribute("order", order1))
                .andExpect(view().name("admin/orderDetails"));
    }

    @Test
    public void testGetOrderNotFound() throws Exception {
        when(orderService.getById(1L)).thenThrow(OrderNotFoundException.class);

        mockMvc.perform(get(URL + "/1"))
                .andExpect(view().name("error/404"));
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        mockMvc.perform(put(URL + "/1")
                        .param("status", "REJECTED"))
                .andExpect(view().name("redirect:/admin/orders/1"));
    }
}