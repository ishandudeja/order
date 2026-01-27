package com.ecomerce.process.order.web;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.OrderItem;
import com.ecomerce.process.order.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest {

    @Test
    public void getAllOrders_returnsJsonWithoutLazyInit() throws Exception {

        OrderItem item = new OrderItem("Cam", 150, 1);
        Order o = new Order("Alice", "alice@example.com", "123", Collections.singletonList(item));

        OrderRepository repo = mock(OrderRepository.class);
        when(repo.findAllWithItems()).thenReturn(Collections.singletonList(o));

        OrderController controller = new OrderController(repo);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

        mvc.perform(get("/orders").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Alice")));
    }

    @Test
    public void deleteOrder_existing_returnsNoContent() throws Exception {
        OrderRepository repo = mock(OrderRepository.class);
        when(repo.existsById(1L)).thenReturn(true);
        doNothing().when(repo).deleteById(1L);

        OrderController controller = new OrderController(repo);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

        mvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());

        verify(repo).deleteById(1L);
    }

    @Test
    public void deleteOrder_missing_returnsNotFound() throws Exception {
        OrderRepository repo = mock(OrderRepository.class);
        when(repo.existsById(2L)).thenReturn(false);

        OrderController controller = new OrderController(repo);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();

        mvc.perform(delete("/orders/2"))
                .andExpect(status().isNotFound());
    }
}
