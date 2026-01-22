package com.ecomerce.process.order.web;

import com.ecomerce.process.order.repository.AccountCredentials;
import com.ecomerce.process.order.service.JwtService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AccountCredentials credentials) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        credentials.username(),
                        credentials.password()
                )
        );

        // Generate token
        String jwts = jwtService.getToken(authentication.getName());

        // Build response with the generated token
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION,
                "Bearer " + jwts).header(HttpHeaders.
                        ACCESS_CONTROL_EXPOSE_HEADERS,
                "Authorization").build();
    }


}
