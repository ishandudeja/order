package com.ecomerce.process.order.web;

import com.ecomerce.process.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RepositoryRestController
public class OrderRepositoryRestController {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderRepositoryRestController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // Handle DELETE requests for Spring Data REST repository endpoint (e.g. /api/orders/{id})
    @DeleteMapping({"/orders/{id}", "/api/orders/{id}"})
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
