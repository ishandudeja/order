package com.ecomerce.process.order.handler;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.repository.OrderRepository;
import com.ecomerce.process.order.service.EventPublisherService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RepositoryEventHandler(Order.class)
public class OrderEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventHandler.class.getName());
    private final OrderRepository orderRepository;
    private final EventPublisherService eventPublisherService;

    @PersistenceContext
    private EntityManager entityManager;
    public OrderEventHandler() {
        this.orderRepository = null;
        this.eventPublisherService = null;
    }
    @Autowired
    public OrderEventHandler(OrderRepository orderRepository, EventPublisherService eventPublisherService) {
        this.eventPublisherService = eventPublisherService;
        this.orderRepository = orderRepository;
    }

    public void handleBeforeCreate(Order entity) {
        // Logic to execute before creating an order
    }
    @HandleAfterCreate
    public void handleAfterCreate(Order entity) {
        // Logic to execute after creating an order
        logger.info("Order created with ID: " + entity.getId());
        eventPublisherService.publishEvent(entity);

    }

    @HandleBeforeSave
    public void handleBeforeUpdate(Order entity) {
        // Logic to execute before updating an order
        logger.info("About to update Order with ID: " + entity.getId());
        // For example, you could validate the order status here
        entityManager.detach(entity); // Detach to get the current state from DB
        Optional<Order> existingOrder = orderRepository.findById(entity.getId());
        if (existingOrder.isPresent()) {
            Order order = existingOrder.get();
            // Perform validation or logging based on existing order data
            logger.info("Existing Order Status: " + order.getStatus());
            if ("COMPLETED".equals(order.getStatus())) {
                logger.error("Cannot update an order that has already been shipped.");
                // You could throw an exception here to prevent the update
                throw new IllegalStateException("Cannot update a completed order.");
            }
            if (entity.getStatus().equals("CANCELLED")) {
                logger.info("Order is being cancelled. Performing necessary actions.");
                // Additional logic for cancellation can be added here
                eventPublisherService.publishEvent(entity);
            }
            if (entity.getStatus().equals("COMPLETED")) {
                logger.info("Order is being marked as shipped. Notifying customer.");
                // Additional logic for shipping can be added here
                eventPublisherService.publishEvent(entity);
            }
        }
        entityManager.clear(); // Clear the persistence context
    }
    @HandleAfterSave
    public void handleAfterUpdate(Order entity) {
        // Logic to execute after updating an order
        logger.info("Order updated with ID: " + entity.getId());
    }
    public void handleBeforeDelete(Order entity) {
        // Logic to execute before deleting an order
    }
    public void handleAfterDelete(Order entity) {
        // Logic to execute after deleting an order
    }

}
