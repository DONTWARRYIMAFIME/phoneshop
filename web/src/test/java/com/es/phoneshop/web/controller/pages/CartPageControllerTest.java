package com.es.phoneshop.web.controller.pages;


import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.model.phone.Phone;
import com.es.core.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class CartPageControllerTest {
    private static final String URL = "/cart";
    private static final String JSON =
            "{" +
            "   \"cartItems[0].id\": \"101\", " +
            "   \"cartItems[0].quantity\": \"101\"" +
            "}";

    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Mock
    private CartItem cartItem1;
    @Mock
    private CartItem cartItem2;
    @Mock
    private Phone phone1;
    @Mock
    private Phone phone2;
    @Spy
    private Map<Long, CartItem> cartItems;

    @InjectMocks
    private CartPageController cartPageController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartPageController)
                .setSingleView(new InternalResourceView("/WEB-INF/pages/cart.jsp"))
                .build();

        when(cartService.getCart()).thenReturn(cart);
        when(cart.getItems()).thenReturn(cartItems);

        when(phone1.getId()).thenReturn(101L);
        when(phone2.getId()).thenReturn(102L);

        cartItems.put(phone1.getId(), cartItem1);
        cartItems.put(phone2.getId(), cartItem2);
    }

    @Test
    public void testGetCart() throws Exception {
        mockMvc.perform(get(URL))
                .andExpect(model().attributeExists("CartItemListDto"))
                .andExpect(model().attribute("cart", cart))
                .andExpect(view().name("cart"));
    }

    @Test
    public void testUpdateCart() throws Exception {
        mockMvc.perform(put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON))
                .andExpect(model().attributeExists("CartItemListDto"))
                .andExpect(model().attribute("cart", cart))
                .andExpect(view().name("redirect:/cart"));
    }

    @Test
    public void testDeletePhone() throws Exception {
        String request = "\"id\": \"101\"";

        mockMvc.perform(delete(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(model().attributeExists("CartItemListDto"))
                .andExpect(model().attribute("cart", cart))
                .andExpect(view().name("redirect:/cart"));
    }
}