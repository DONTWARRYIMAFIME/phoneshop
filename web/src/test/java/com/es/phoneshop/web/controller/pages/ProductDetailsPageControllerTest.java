package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.PhoneNotFoundException;
import com.es.core.model.phone.Phone;
import com.es.core.service.PhoneService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageControllerTest {
    private static final String URL = "/products/{id}";

    @Mock
    private PhoneService phoneService;
    @Mock
    private Phone phone;
    @InjectMocks
    private ProductDetailsPageController pdpController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(pdpController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    public void testShowProductDetails() throws Exception {
        when(phoneService.getPhoneById(1L)).thenReturn(phone);

        mockMvc.perform(get(URL, "1"))
                .andExpect(model().attribute("phone", phone))
                .andExpect(view().name("product"));
    }

    @Test
    public void testShowProductNotFound() throws Exception {
        when(phoneService.getPhoneById(1L)).thenThrow(PhoneNotFoundException.class);

        mockMvc.perform(get(URL, "1"))
                .andExpect(view().name("error/404"));
    }
}