package com.es.phoneshop.web.controller.pages;

import com.es.core.service.PhoneService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageControllerTest {
    private static final String URL = "/products";

    @Mock
    private PhoneService phoneService;
    @InjectMocks
    private ProductListPageController plpController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(plpController)
                .setSingleView(new InternalResourceView("/WEB-INF/pages/productList.jsp"))
                .build();
    }

    @Test
    public void testShowProductList() throws Exception {
        mockMvc.perform(get(URL)
                        .param("page", "4")
                        .param("sortBy", "price")
                        .param("orderBy", "desc"))
                .andExpect(model().attributeExists("pages"))
                .andExpect(model().attributeExists("phones"))
                .andExpect(model().attributeExists("phoneQuantity"))
                .andExpect(view().name("productList"));
    }
}
