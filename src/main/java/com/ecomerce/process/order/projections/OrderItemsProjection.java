package com.ecomerce.process.order.projections;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.domain.OrderItem;
import com.ecomerce.process.order.domain.Product;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

@Projection(name = "", types = { OrderItem.class })
public interface OrderItemsProjection {

    Long getId();
    int getQuantity();
    int getPrice();
    List<Product> getProducts();
}
