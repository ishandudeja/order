package com.ecomerce.process.order;

import com.ecomerce.process.order.domain.*;
import com.ecomerce.process.order.repository.AppUserRepository;
import com.ecomerce.process.order.repository.OrderRepository;
import com.ecomerce.process.order.repository.ProductRepository;
import com.ecomerce.process.order.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.ObjectProvider;

import java.util.Arrays;

@EnableAsync
@SpringBootApplication
@EnableMethodSecurity
public class OrderApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(
            OrderApplication.class
    );

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AppUserRepository appUserRepository;
    private final SecurityUtils securityUtils; // may be null when running in sliced tests


    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
        logger.info("Order Service is running...");
    }

    public OrderApplication(OrderRepository orderRepository,
                            ProductRepository productRepository,
                            AppUserRepository appUserRepository, ObjectProvider<SecurityUtils> securityUtilsProvider) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.appUserRepository = appUserRepository;
        this.securityUtils = securityUtilsProvider.getIfAvailable();
    }

    @Override
    public void run(String... args) throws Exception {

        Product product1 = new Product("Apple iPhone 13", 999, 5);
        Product product2 = new Product("Samsung Galaxy S21", 799, 4);
        Product product3 = new Product("Google Pixel 6", 599, 4);

        productRepository.saveAll(Arrays.asList(product1, product2, product3));
        logger.info("Number of products: {}", productRepository.count());

        // Do NOT call orderItemRepository.saveAll(...) here. Instead rely on cascade from Order.

        Order order1 = new Order("Justin", "Justin@example.com", "+64 234567890", Arrays.asList(new OrderItem("Apple iPhone 13", 999, 2), new OrderItem("Apple iPhone 14", 999, 1)));
        Order order2 = new Order("Alice", "Alice@example.com", "+64 234567890", Arrays.asList(new OrderItem("Samsung Galaxy S21", 799, 1)));
        Order order3 = new Order("Bob", "Bob@example.com", "+64 234567890", Arrays.asList(new OrderItem("Apple iPhone 13", 999, 1)));
        Order order4 = new Order("Eve", "Eve@example.com", "+64 234567890", Arrays.asList(new OrderItem("Apple iPhone 13", 999, 1), new OrderItem("Google Pixel 6", 599, 3)));
        Order order5 = new Order("Charlie", "Charlie@example.com", "+64 234567890", Arrays.asList(new OrderItem("Google Pixel 6", 599, 3)));


        // Username: user, password: user
        appUserRepository.save(new AppUser("user",
                "$2a$10$NVM0n8ElaRgg7zWO1CxUdei7vWoPg91Lz2aYavh9.f9q0e4bRadue", "USER"));
        // Username: admin, password: admin
        appUserRepository.save(new AppUser("admin",
                "$2a$10$8cjz47bjbR4Mn8GMg9IZx.vyjhLXR/SKKMSZ9. mP9vpMu0ssKi8GW", "ADMIN"));



        /*
         * Due to method-level protections on {@link example.company.ItemRepository}, the security context must be loaded
         * with an authentication token containing the necessary privileges.
         */
        if (this.securityUtils != null) {
            securityUtils.runAs("user", "user", "ROLE_USER");
        }
        orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order5));
        logger.info("Number of orders: {}", orderRepository.count());
        SecurityContextHolder.clearContext();

    }
}
