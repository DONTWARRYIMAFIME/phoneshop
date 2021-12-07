package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OrderNotFoundException;
import com.es.core.model.order.Order;
import com.es.core.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageControllerTest {

    private static final String URL = "/orderOverview/{secureId}";

    @Mock
    private OrderService orderService;
    @Mock
    private Order order;

    @InjectMocks
    private OrderOverviewPageController orderOverviewPageController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(orderOverviewPageController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testShowOrderOverview() throws Exception {
        when(orderService.getBySecureId("secureId")).thenReturn(order);

        mockMvc.perform(get(URL, "secureId"))
                .andExpect(model().attribute("order", order))
                .andExpect(view().name("orderOverview"));
    }

    @Test
    public void testShowOrderNotFound() throws Exception {
        doThrow(OrderNotFoundException.class).when(orderService).getBySecureId("secureId");

        mockMvc.perform(get(URL, "secureId"))
                .andExpect(model().attributeDoesNotExist("order"))
                .andExpect(view().name("error/404"));
    }
}