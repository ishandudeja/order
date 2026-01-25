package com.ecomerce.process.order.service;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.event.template.EmailNotificationTemplate;
import com.ecomerce.process.order.event.template.PhoneNotificationTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisherService {

    @Autowired
    private final ApplicationEventPublisher publisher;
    public EventPublisherService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishEvent(Order orderEvent) {
        publisher.publishEvent(new EmailNotificationTemplate(this, orderEvent));
        publisher.publishEvent(new PhoneNotificationTemplate(this, orderEvent));
    }

    public void publishOnPhone(Order orderEvent) {
        publisher.publishEvent(new PhoneNotificationTemplate(this, orderEvent));
    }

    public void publishOnEmail(Order orderEvent) {
        publisher.publishEvent(new EmailNotificationTemplate(this, orderEvent));
    }

}
