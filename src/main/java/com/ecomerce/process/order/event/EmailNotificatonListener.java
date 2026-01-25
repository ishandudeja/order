package com.ecomerce.process.order.event;

import com.ecomerce.process.order.event.template.EmailNotificationTemplate;
import com.ecomerce.process.order.service.EmailNotificationService;
import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EmailNotificatonListener implements ApplicationListener<EmailNotificationTemplate> {

    Logger logger = org.slf4j.LoggerFactory.getLogger(EmailNotificatonListener.class);
    private final EmailNotificationService emailNotificationService;

    public EmailNotificatonListener(EmailNotificationService emailNotificationService) {
        this.emailNotificationService = emailNotificationService;
    }
    @Override
    public void onApplicationEvent(EmailNotificationTemplate event) {
        // Handle the event (e.g., send notification)
        logger.info("Received order event - Message: " + event.getMessage() +
                ", Email: " + event.getEmail());
        emailNotificationService.sendEmailNotification(event.getEmail(), event.getMessage());

    }
}
