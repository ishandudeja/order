package com.ecomerce.process.order;

import com.ecomerce.process.order.domain.*;
import com.ecomerce.process.order.repository.OrderItemRepository;
import com.ecomerce.process.order.repository.OrderRepository;
import com.ecomerce.process.order.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class OrderApplication implements CommandLineRunner{
	private static final Logger logger = LoggerFactory.getLogger(
			OrderApplication.class
	);

	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final OrderItemRepository orderItemRepository;

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
		logger.info("Order Service is running...");
	}

	public OrderApplication(OrderRepository orderRepository, ProductRepository productRepository, OrderItemRepository orderItemRepository) {
		this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

	@Override
	public void run(String... args) throws Exception {

		Product product1 = new Product("Apple iPhone 13", 999, 5);
		Product product2 = new Product("Samsung Galaxy S21", 799, 4);
		Product product3 = new Product("Google Pixel 6", 599, 4);

		productRepository.saveAll(Arrays.asList(product1, product2, product3));
		logger.info("Number of products: {}", productRepository.count());

		// Do NOT call orderItemRepository.saveAll(...) here. Instead rely on cascade from Order.

		Order order1 = new Order("Justin", Arrays.asList( new OrderItem(product1, 2), new OrderItem(product2, 1)));
		Order order2 = new Order("Alice", Arrays.asList(new OrderItem(product2,1)));
		Order order3 = new Order("Bob", Arrays.asList(new OrderItem(product1,1)));
		Order order4 = new Order("Eve", Arrays.asList(new OrderItem(product1,1), new OrderItem(product3,3)));
		Order order5 = new Order("Charlie", Arrays.asList(new OrderItem(product3,3)));

		orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order5));
		logger.info("Number of orders: {}", orderRepository.count());

	}
}
