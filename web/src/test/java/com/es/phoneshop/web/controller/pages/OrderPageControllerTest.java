package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.order.Order;
import com.es.core.model.order.OrderItem;
import com.es.core.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class OrderPageControllerTest {

    private static final String URL = "/orders";
    private static final String JSON =
            "{" +
            "   \"firstName\": \"name\", " +
            "   \"lastName\": \"lastname\", " +
            "   \"address\": \"address\", " +
            "   \"phone\": \"12345\"" +
            "}";

    @Mock
    private OrderService orderService;
    @Mock
    private Order order;
    @Mock
    private ConversionService conversionService;
    @InjectMocks
    private OrderPageController orderPageController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderPageController)
                .setSingleView(new InternalResourceView("/WEB-INF/pages/order.jsp"))
                .build();

        when(orderService.createOrder()).thenReturn(order);
        when(conversionService.convert(any(), any())).thenReturn(order);
    }

    @Test
    public void testGetOrder() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attributeExists("OrderDto"))
                .andExpect(view().name("order"));
    }

    @Test
    public void testPlaceOrder() throws Exception {
        OrderItem orderItem = mock(OrderItem.class);
        when(order.getOrderItems()).thenReturn(Collections.singletonList(orderItem));
        when(order.getSecureId()).thenReturn("secureId");

        mockMvc.perform(post(URL + "/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attributeExists("OrderDto"))
                .andExpect(view().name("redirect:/orderOverview/secureId"));
    }

    @Test
    public void testPlaceEmptyOrder() throws Exception {
        mockMvc.perform(post(URL + "/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attributeExists("OrderDto"))
                .andExpect(view().name("order"));
    }

    @Test
    public void testPlaceOrderOutOfStock() throws Exception {
        OrderItem orderItem = mock(OrderItem.class);
        when(order.getOrderItems()).thenReturn(Collections.singletonList(orderItem));

        String errorMessage = "out of stock";
        doThrow(new OutOfStockException(errorMessage)).when(orderService).placeOrder(order);

        mockMvc.perform(post(URL + "/place")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON))
                .andExpect(model().attribute("order", order))
                .andExpect(model().attributeExists("OrderDto"))
                .andExpect(model().attribute("outOfStockMessage", errorMessage))
                .andExpect(view().name("order"));
    }
}