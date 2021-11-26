package com.es.phoneshop.web.controller.pages;

import com.es.core.exception.OutOfStockException;
import com.es.core.model.cart.Cart;
import com.es.core.model.cart.CartItem;
import com.es.core.service.CartService;
import com.es.phoneshop.web.controller.exception.RestResponseEntityExceptionHandler;
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

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AjaxCartControllerTest {

    private static final String URL = "/ajaxCart";

    @Mock
    private CartService cartService;
    @Mock
    private Cart cart;
    @Mock
    private CartItem cartItem;
    @Spy
    private Map<Long, CartItem> cartItems;

    @InjectMocks
    private AjaxCartController ajaxCartController;

    private MockMvc mockMvc;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(ajaxCartController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();

        when(cartService.getCart()).thenReturn(cart);
    }

    @Test
    public void testGetCart() throws Exception {
        when(cart.getTotalQuantity()).thenReturn(2L);
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(999));

        mockMvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.totalQuantity").value(2))
                .andExpect(jsonPath("$.totalPrice").value(999));
    }

    @Test
    public void testAddPhone() throws Exception {
        doAnswer(invocation -> cartItems.put(101L, cartItem)).when(cartService).addPhone(101L, 101L);
        when(cart.getTotalQuantity()).thenReturn(1L);
        when(cart.getTotalPrice()).thenReturn(new BigDecimal(999));

        String cartItemJson =
                "{" +
                        "\"id\": \"101\", " +
                        "\"quantity\": \"101\"" +
                "}";

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cartItemJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.totalQuantity").value(1))
                .andExpect(jsonPath("$.totalPrice").value(999));
    }

    @Test
    public void testAddPhoneWithIncorrectId() throws Exception {
        String json = 
                "{" +
                        "\"id\": \"something\", " +
                        "\"quantity\": \"101\"" +
                "}";

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid format"));
    }

    @Test
    public void testAddPhoneOutOfStock() throws Exception {
        doThrow(OutOfStockException.class).when(cartService).addPhone(105L, 105L);
        String json =
                "{" +
                        "\"id\": \"105\", " +
                        "\"quantity\": \"105\"" +
                "}";

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertEquals(result.getResolvedException().getClass(), OutOfStockException.class))
                .andExpect(content().string("Out of stock"));
    }
}