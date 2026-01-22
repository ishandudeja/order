package com.ecomerce.process.order.service;

import com.ecomerce.process.order.domain.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @PreAuthorize("hasRole('USER')")
    public void updateOrder(Order order) {
        // This method can be invoked by user with USER role.

    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOrder(Long orderId) {
        // This method can be invoked by user with ADMIN role.
    }

}
