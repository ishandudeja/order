package com.ecomerce.process.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

import com.ecomerce.process.order.repository.AppUserRepository;
import com.ecomerce.process.order.domain.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void setupUser() {
        // Ensure a user with username "admin" and password "admin" exists
        // Delete any pre-existing 'admin' (the application may have inserted one with an invalid hash)
        userRepository.findByUsername("admin").ifPresent(userRepository::delete);

        AppUser u = new AppUser();
        u.setUsername("admin");
        u.setPassword(passwordEncoder.encode("admin"));
        u.setRole("ADMIN");
        userRepository.save(u);
    }

    @AfterEach
    public void cleanup() {
        // Remove the test user to keep DB clean
        userRepository.findByUsername("admin").ifPresent(userRepository::delete);
    }

    @Test
    public void testAuthentication() throws Exception {
        // Testing authentication with correct credentials
        this.mockMvc
                .perform(post("/login")
                        .content("{\"username\":\"admin\",\"password\":\"admin\"}")
                        .header(HttpHeaders.CONTENT_TYPE,"application/json"))
                .andDo(print()).andExpect(status().isOk());
    }
}
