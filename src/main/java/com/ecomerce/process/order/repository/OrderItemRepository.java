package com.ecomerce.process.order.repository;

import com.ecomerce.process.order.domain.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="orderItems")
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
}
