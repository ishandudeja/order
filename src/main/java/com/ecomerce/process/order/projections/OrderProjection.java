package com.ecomerce.process.order.projections;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.OrderItem;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;
import java.util.List;

@Projection(name = "order", types = { Order.class })
public interface OrderProjection {
    Long getId();
    String getCustomerName();
    String getStatus();
    int getAmount();
    LocalDate getOrderDate();
    int getItemCount();
    List<OrderItem> getItems();
}
