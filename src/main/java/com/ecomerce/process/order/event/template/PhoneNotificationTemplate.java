package com.ecomerce.process.order.event.template;

import com.ecomerce.process.order.domain.Order;
import org.springframework.context.ApplicationEvent;

public class PhoneNotificationTemplate extends ApplicationEvent {
    private final String phoneNumber;
    private String message;
    public final Order order;
    public PhoneNotificationTemplate(Object source, Order order) {
        super(source);
        this.phoneNumber = order.getPhoneNumber();
        this.order = order;
        this.generatePhoneContent();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getMessage() {
        return message;
    }

    public void generatePhoneContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear ").append(order.getCustomerName()).append(",\n\n");
        switch (order.getStatus()) {
            case "CREATED": sb.append("Your order has been successfully placed.\n\n");
                break;
            case "CANCELLED": sb.append("Your order has been cancelled.\n\n");
                break;
            case "SHIPPED": sb.append("Your order has been shipped and is on its way to you.\n\n");
                break;
            case "COMPLETED": sb.append("Your order has been delivered. Enjoy your purchase!\n\n");
                break;
            default : sb.append("Your order status is: ").append(order.getStatus()).append("\n\n");
        }
        sb.append("Order ID: ").append(order.getId()).append(", ");
        sb.append("Amount: $").append(order.getAmount()).append(". ");
        sb.append("Thank you for shopping with us!");
        this.message = sb.toString();
    }

}
