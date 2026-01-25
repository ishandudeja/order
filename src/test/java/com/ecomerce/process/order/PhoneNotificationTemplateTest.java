package com.ecomerce.process.order;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.OrderItem;
import com.ecomerce.process.order.domain.Product;
import com.ecomerce.process.order.event.template.PhoneNotificationTemplate;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class PhoneNotificationTemplateTest {

    @Test
    public void generatePhoneContent_includesOrderSummary() {
        Product p = new Product("Tablet", 300, 4);
        OrderItem item = new OrderItem(p, 1);
        Order order = new Order("Carol", "carol@example.com", "999888777", Arrays.asList(item));
        order.setStatus("COMPLETED");

        PhoneNotificationTemplate tpl = new PhoneNotificationTemplate(this, order);

        assertThat(tpl.getPhoneNumber()).isEqualTo("999888777");
        assertThat(tpl.getMessage()).contains("Order ID:");
    }
}
