package com.ecomerce.process.order.repository;

import com.ecomerce.process.order.domain.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(path="orders")
public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.customerName = :customerName")
    List<Order> findByCustomerName(@Param("customerName") String customerName);

    List<Order> findByStatus(String status);

    List<Order> findByAmountGreaterThan(int amount);

    List<Order> findByOrderDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate);

    List<Order> findByItemCountLessThan(int itemCount);

//    List<Order> findByItems_ProductName(String productName);

    List<Order> findByItems_Products_PriceGreaterThan(int price);

}
