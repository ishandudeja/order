package com.ecomerce.process.order.event.template;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.Product;
import org.springframework.context.ApplicationEvent;

public class EmailNotificationTemplate extends ApplicationEvent {
    private  String email;
    private  String message, subject;
    private final Order order;
    public EmailNotificationTemplate(Object source, Order order) {
        super(source);
        this.email = order.getEmail();
        this.order = order;
        this.generateEmailContent();
    }

    public String getEmail() {
        return email;
    }

    public String getMessage() {
        return message;
    }

    public String getSubject() {
        return subject;
    }

    public void generateEmailContent() {

        StringBuilder sb = new StringBuilder();

        sb.append("Dear ").append(order.getCustomerName()).append(",\n\n");

        switch (order.getStatus()) {
            case "CREATED": sb.append("We are pleased to inform you that your order has been successfully placed.\n\n");
                    subject = "Order Confirmation";
            break;
            case "CANCELLED": sb.append("We regret to inform you that your order has been cancelled.\n\n");
                            subject = "Order Cancellation";
            break;
            case "SHIPPED": sb.append("Good news! Your order has been shipped and is on its way to you.\n\n");
                            subject = "Order Shipped";
            break;
            case "COMPLETED": sb.append("Your order has been delivered. We hope you enjoy your purchase!\n\n");
                            subject = "Order Delivered";
            break;
            default : sb.append("Your order status is: ").append(order.getStatus()).append("\n\n");
        }
        sb.append("Order Details:\n");
        sb.append("Order ID: ").append(order.getId()).append("\n");
        sb.append("Amount: $").append(order.getAmount()).append("\n\n");
        sb.append("Items:\n");
        order.getItems().forEach(item -> {
            sb.append("- ");
            item.getProducts().forEach(product -> {
                sb.append(product.toString()).append(" ");
            });
            sb.append("(Quantity: ").append(item.getQuantity())
              .append(", Price: $").append(item.getPrice()).append(")\n");
        });
        sb.append(message).append("\n\n");
        sb.append("Thank you for shopping with us!\n");
        sb.append("Best regards,\n");
        sb.append("E-Commerce Team");

        message = sb.toString();

    }
}
