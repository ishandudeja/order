package com.ecomerce.process.order.service;

import org.springframework.stereotype.Component;

@Component
public class PhoneNotificationService {

    public boolean sendPhoneNotification(String phoneNumber,String message) {
        // Simulate sending a phone notification
        System.out.println("Sending phone notification to " + phoneNumber + ": " + message);

        return phoneNumber != null && phoneNumber.matches("\\d{10}");
    }

}
