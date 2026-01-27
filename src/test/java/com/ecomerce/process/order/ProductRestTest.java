package com.ecomerce.process.order;

import com.ecomerce.process.order.domain.AppUser;
//import com.ecomerce.process.order.repository.ProductRepository;
import com.ecomerce.process.order.repository.OrderRepository;
import com.ecomerce.process.order.repository.AppUserRepository;
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


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductRestTest {

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String BASE = "/api/products";

    @BeforeEach
    public void setupUser() {
        userRepository.findByUsername("admin").ifPresent(userRepository::delete);
        AppUser u = new AppUser();
        u.setUsername("admin");
        u.setPassword(passwordEncoder.encode("admin"));
        u.setRole("USER");
        userRepository.save(u);

        // Remove orders first (they reference products) then products to avoid FK constraint
        orderRepository.deleteAll();
//        productRepository.deleteAll();
    }

    @AfterEach
    public void cleanup() {
        userRepository.findByUsername("admin").ifPresent(userRepository::delete);
        orderRepository.deleteAll();
//        productRepository.deleteAll();
    }

    @Test
    public void testGetProducts_Unauthenticated_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get(BASE)).andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateAndGetProduct_WithAuth() throws Exception {
        String auth = TestAuthUtil.obtainAuthHeader(mockMvc, "admin", "admin");
        // create a new product using Spring Data REST endpoint
        String json = "{\"name\":\"Test Phone\",\"price\":123,\"rating\":4}";

        mockMvc.perform(post(BASE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, auth)
                        .content(json))
                .andExpect(status().isCreated());

        // GET list
        String list = mockMvc.perform(get(BASE)
                        .header(HttpHeaders.AUTHORIZATION, auth))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(list).contains("Test Phone");
    }
}
