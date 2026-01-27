package com.ecomerce.process.order;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.OrderItem;
//import com.ecomerce.process.order.domain.Product;
import com.ecomerce.process.order.event.template.EmailNotificationTemplate;
import com.ecomerce.process.order.event.template.PhoneNotificationTemplate;
import com.ecomerce.process.order.service.EventPublisherService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class EventPublisherServiceTest {

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private EventPublisherService eventPublisherService;

    @Test
    public void publishEvent_shouldPublishEmailAndPhoneTemplates() {

        OrderItem item = new OrderItem("TestProduct", 100, 2);
        Order order = new Order("Alice", "alice@example.com", "1234567890", Arrays.asList(item));

        eventPublisherService.publishEvent(order);

        // verify both kinds of templates were published
        verify(publisher).publishEvent(isA(EmailNotificationTemplate.class));
        verify(publisher).publishEvent(isA(PhoneNotificationTemplate.class));
    }
}
