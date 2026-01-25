package com.ecomerce.process.order.service;

import org.springframework.stereotype.Component;

@Component
public class EmailNotificationService {

    public boolean sendEmailNotification(String email, String message) {
        // Simulate sending an email notification
        System.out.println("Sending email to " + email + ": " + message);

        return email != null && email.contains("@");
    }
}
