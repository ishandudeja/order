package com.ecomerce.process.order;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.OrderItem;
import com.ecomerce.process.order.event.template.EmailNotificationTemplate;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailNotificationTemplateTest {

    @Test
    public void generateEmailContent_includesOrderDetailsAndSubject() {
        OrderItem item = new OrderItem("Phone", 200, 1);
        Order order = new Order("Bob", "bob@example.com", "000111222", Arrays.asList(item));
        order.setStatus("SHIPPED");

        EmailNotificationTemplate tpl = new EmailNotificationTemplate(this, order);

        assertThat(tpl.getEmail()).isEqualTo("bob@example.com");
        assertThat(tpl.getMessage()).contains("Order ID:");
        assertThat(tpl.getSubject()).isEqualTo("Order Shipped");
    }
}
