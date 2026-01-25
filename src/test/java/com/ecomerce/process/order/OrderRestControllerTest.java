package com.ecomerce.process.order;

import com.ecomerce.process.order.domain.AppUser;
import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.OrderItem;
import com.ecomerce.process.order.domain.Product;
import com.ecomerce.process.order.repository.AppUserRepository;
import com.ecomerce.process.order.repository.OrderRepository;
import com.ecomerce.process.order.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Product product;

    @BeforeEach
    public void setup() {
        // ensure clean DB: delete orders first then products
        orderRepository.deleteAll();
        productRepository.deleteAll();

        // create a product to use in order items
        product = productRepository.save(new Product("TestPhone-Order", 200, 5));

        // create admin user
        userRepository.findByUsername("admin").ifPresent(userRepository::delete);
        AppUser u = new AppUser();
        u.setUsername("admin");
        u.setPassword(passwordEncoder.encode("admin"));
        u.setRole("ADMIN");
        userRepository.save(u);
    }

    @AfterEach
    public void cleanup() {
        userRepository.findByUsername("admin").ifPresent(userRepository::delete);
        orderRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void testCreateRetrieveUpdateAndSearchOrder_viaRest() throws Exception {
        // create Order via repository (complex nested items aren't created via Data REST easily)
        Order order = new Order("CustomerRest","Justin@example.com", "+64 234567890", Arrays.asList(new OrderItem(product, 2)));
        order = orderRepository.save(order);

        String auth = TestAuthUtil.obtainAuthHeader(mockMvc, "admin", "admin");
        assertThat(auth).isNotNull();

        // Retrieve via Data REST
        String itemPath = "/api/orders/" + order.getId();
        String body = mockMvc.perform(get(itemPath)
                        .header(HttpHeaders.AUTHORIZATION, auth))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(body).contains("CustomerRest");

        // Update status via PATCH
        String patchJson = "{\"status\":\"COMPLETED\"}";
        mockMvc.perform(patch(itemPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .content(patchJson))
                .andExpect(status().is2xxSuccessful());

        // Verify update persisted
        Order updated = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo("COMPLETED");

        // Search via Data REST search endpoint: findByCustomerName
        String searchPath = "/api/orders/search/findByCustomerName?customerName=CustomerRest";
        String searchBody = mockMvc.perform(get(searchPath)
                        .header(HttpHeaders.AUTHORIZATION, auth))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(searchBody).contains("CustomerRest");
    }
}
