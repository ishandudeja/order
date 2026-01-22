package com.ecomerce.process.order.web;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.repository.OrderRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
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
        return orderRepository.findAll();
    }

}
