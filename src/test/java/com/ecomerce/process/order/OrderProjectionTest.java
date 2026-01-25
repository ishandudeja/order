package com.ecomerce.process.order;
import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.OrderItem;
import com.ecomerce.process.order.domain.Product;
import com.ecomerce.process.order.projections.OrderItemsProjection;
import com.ecomerce.process.order.projections.OrderProjection;
import org.junit.jupiter.api.Test;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrderProjectionTest {

    @Test
    public void testOrderProjectionCreation() {
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Product product1 = new Product("Apple iPhone 13", 999, 5);
        // Assuming Order is a valid class in your domain
        Order order = new Order("Justin","Justin@example.com", "+64 234567890", Arrays.asList( new OrderItem(product1, 2)));
        OrderProjection projection = factory.createProjection(OrderProjection.class, order);
        // Add assertions here to validate the projection
        assertThat(projection.getCustomerName()).isEqualTo("Justin");
        assertThat(projection.getItemCount()).isEqualTo(1);
        assertThat(projection.getAmount()).isEqualTo(1998); // 2 * 999
        assertThat(projection.getStatus()).isEqualTo("CREATED"); // Assuming default status
        assertThat(projection.getItems().size()).isEqualTo(1);
        assertThat(projection.getItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(projection.getItems().get(0).getPrice()).isEqualTo(1998); // 2 * 999
        assertThat(projection.getItems().get(0).getProducts().get(0).getName()).isEqualTo("Apple iPhone 13");
        assertThat(projection.getItems().get(0).getProducts().get(0).getPrice()).isEqualTo(999);
        assertThat(projection.getItems().get(0).getProducts().get(0).getRating()).isEqualTo(5);

    }
    @Test
    public void testOrderItemProjectionCreation() {
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
        Product product1 = new Product("Apple iPhone 13", 999, 5);
        OrderItem orderItem = new OrderItem(product1, 2);
        var projection = factory.createProjection(
                OrderItemsProjection.class,
                orderItem
        );
        // Add assertions here to validate the projection
        assertThat(projection.getQuantity()).isEqualTo(2);
        assertThat(projection.getPrice()).isEqualTo(1998); // 2 * 999
        assertThat(projection.getProducts().get(0).getName()).isEqualTo("Apple iPhone 13");
        assertThat(projection.getProducts().get(0).getPrice()).isEqualTo(999);
        assertThat(projection.getProducts().get(0).getRating()).isEqualTo(5);
    }

}
