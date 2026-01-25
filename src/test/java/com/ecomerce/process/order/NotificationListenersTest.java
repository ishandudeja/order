package com.ecomerce.process.order;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.OrderItem;
import com.ecomerce.process.order.domain.Product;
import com.ecomerce.process.order.event.EmailNotificatonListener;
import com.ecomerce.process.order.event.PhoneNotificationListener;
import com.ecomerce.process.order.event.template.EmailNotificationTemplate;
import com.ecomerce.process.order.event.template.PhoneNotificationTemplate;
import com.ecomerce.process.order.service.EmailNotificationService;
import com.ecomerce.process.order.service.PhoneNotificationService;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotificationListenersTest {

    @Test
    public void emailListener_receivesEventAndLogs() {
        EmailNotificationService emailNotificationService = mock(EmailNotificationService.class);
        EmailNotificatonListener listener = new EmailNotificatonListener(emailNotificationService);
        Product p = new Product("Cam", 150, 4);
        OrderItem item = new OrderItem(p, 1);
        Order order = new Order("Dan", "dan@example.com", "333222111", Collections.singletonList(item));
        order.setStatus("COMPLETED");

        EmailNotificationTemplate tpl = new EmailNotificationTemplate(this, order);

        // listeners log to SLF4J; we can't assert logs easily without appender, but ensure onApplicationEvent doesn't throw
        listener.onApplicationEvent(tpl);
        verify(emailNotificationService).sendEmailNotification(tpl.getEmail(), tpl.getMessage());
    }

    @Test
    public void phoneListener_receivesEventAndLogs() {
        PhoneNotificationService phoneNotificationService = mock(PhoneNotificationService.class);
        PhoneNotificationListener listener = new PhoneNotificationListener(phoneNotificationService);
        Product p = new Product("Cam", 150, 4);
        OrderItem item = new OrderItem(p, 1);
        Order order = new Order("Eve", "eve@example.com", "444555666", Collections.singletonList(item));
        order.setStatus("SHIPPED");

        PhoneNotificationTemplate tpl = new PhoneNotificationTemplate(this, order);
        listener.onApplicationEvent(tpl);
        verify(phoneNotificationService).sendPhoneNotification(tpl.getPhoneNumber(), tpl.getMessage());
    }
}
