package com.ecomerce.process.order.event;

import com.ecomerce.process.order.event.template.PhoneNotificationTemplate;
import com.ecomerce.process.order.service.PhoneNotificationService;
import jakarta.validation.constraints.AssertTrue;
import org.slf4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@Component
public class PhoneNotificationListener implements ApplicationListener<PhoneNotificationTemplate> {

        Logger logger = org.slf4j.LoggerFactory.getLogger(PhoneNotificationListener.class);
        private final PhoneNotificationService phoneNotificationService;
        public PhoneNotificationListener(PhoneNotificationService phoneNotificationService) {
            this.phoneNotificationService = phoneNotificationService;
        }
    @Override
    public void onApplicationEvent(PhoneNotificationTemplate event) {
        // Handle the event (e.g., send notification)
        logger.info("Received order event - Message: " + event.getMessage() +
                ", Phone Number: " + event.getPhoneNumber());
       phoneNotificationService.sendPhoneNotification(event.getPhoneNumber(), event.getMessage());

    }
}
