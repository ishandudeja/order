package com.ecomerce.process.order.web;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.repository.OrderRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

@RestController
public class OrderController {

    private final OrderRepository orderRepository;
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    @GetMapping("/orders/count")
    public Long countOrders() {
        return orderRepository.count();
    }
    @GetMapping("/orders")
    public Iterable<Order> getAllOrders() {
        // Use a fetch-join query to initialize items to prevent lazy init exceptions
        return orderRepository.findAllWithItems();
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (!orderRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        orderRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
