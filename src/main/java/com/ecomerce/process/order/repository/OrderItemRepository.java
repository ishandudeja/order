package com.ecomerce.process.order.repository;

import com.ecomerce.process.order.domain.OrderItem;
import com.ecomerce.process.order.projections.OrderItemsProjection;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(excerptProjection = OrderItemsProjection.class, path="items")
public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
}
