package com.ecomerce.process.order;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.OrderItem;
import com.ecomerce.process.order.domain.Product;
import com.ecomerce.process.order.repository.OrderRepository;
import com.ecomerce.process.order.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
        productRepository.deleteAll();

        product1 = new Product("Apple iPhone 13", 999, 5);
        product2 = new Product("Samsung Galaxy S21", 799, 4);
        product3 = new Product("Google Pixel 6", 599, 4);

        product1 = productRepository.save(product1);
        product2 = productRepository.save(product2);
        product3 = productRepository.save(product3);

        // Create a variety of orders
        Order order1 = new Order("Justin", Arrays.asList(new OrderItem(product1, 2), new OrderItem(product2, 1)));
        Order order2 = new Order("Alice", Arrays.asList(new OrderItem(product2, 1)));
        Order order3 = new Order("Bob", Arrays.asList(new OrderItem(product1, 1)));
        Order order4 = new Order("Eve", Arrays.asList(new OrderItem(product1, 1), new OrderItem(product3, 3)));
        Order order5 = new Order("Charlie", Arrays.asList(new OrderItem(product3, 3)));

        // Give one order a different status to test status queries
        order2.setStatus("COMPLETED");

        orderRepository.saveAll(Arrays.asList(order1, order2, order3, order4, order5));
    }

    @Test
    public void testFindByCustomerName() {
        List<Order> result = orderRepository.findByCustomerName("Justin");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Justin", result.get(0).getCustomerName());
    }

    @Test
    public void testFindByStatus() {
        List<Order> shipped = orderRepository.findByStatus("COMPLETED");
        assertNotNull(shipped);
        assertEquals(1, shipped.size());
        assertEquals("Alice", shipped.get(0).getCustomerName());

        List<Order> created = orderRepository.findByStatus("CREATED");
        assertTrue(created.size() >= 4);
    }

    @Test
    public void testFindByAmountGreaterThan() {
        // product1 price 999 * 2 in Justin => amount >= 1998
        List<Order> expensive = orderRepository.findByAmountGreaterThan(1000);
        assertNotNull(expensive);
        assertTrue(expensive.stream().anyMatch(o -> o.getCustomerName().equals("Justin")));
    }

    @Test
    public void testFindByOrderDateBetween() {
        LocalDate today = LocalDate.now();
        List<Order> between = orderRepository.findByOrderDateBetween(today.minusDays(1), today.plusDays(1));
        assertNotNull(between);
        // all created orders should be in this range
        assertTrue(between.size() >= 5);
    }

    @Test
    public void testFindByItemCountLessThan() {
        // Several orders have 1 item
        List<Order> singleItem = orderRepository.findByItemCountLessThan(2);
        assertNotNull(singleItem);
        assertTrue(singleItem.stream().anyMatch(o -> o.getCustomerName().equals("Alice")));
    }

    @Test
    public void testFindByItemsProductsPriceGreaterThan() {
        // price > 800 should pick up orders containing product1 (999)
        List<Order> highPrice = orderRepository.findByItems_Products_PriceGreaterThan(800);
        assertNotNull(highPrice);
        assertTrue(highPrice.stream().anyMatch(o -> Arrays.asList("Justin","Bob","Eve").contains(o.getCustomerName())));
    }

}
