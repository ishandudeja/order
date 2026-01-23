package com.ecomerce.process.order;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class TestAuthUtil {

    /**
     * Performs a login request against /login and returns the raw Authorization header value ("Bearer <token>").
     */
    public static String obtainAuthHeader(MockMvc mockMvc, String username, String password) throws Exception {
        String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn();
        String auth = result.getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        return auth; // may be null if authentication failed
    }
}
