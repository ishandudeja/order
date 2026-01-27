package com.ecomerce.process.order.repository;

import com.ecomerce.process.order.domain.Order;
import com.ecomerce.process.order.projections.OrderProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(path="orders")
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.customerName = :customerName")
    List<Order> findByCustomerName(@Param("customerName") String customerName, Pageable pageable);

    List<Order> findByStatus(String status, Pageable pageable);

    List<Order> findByAmountGreaterThan(int amount, Pageable pageable);

    List<Order> findByOrderDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate, Pageable pageable);

    List<Order> findByItemCountLessThan(int itemCount, Pageable pageable);

    // Fetch orders with items to avoid lazy init exceptions when serializing
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items")
    List<Order> findAllWithItems();

//    @PreAuthorize("hasRole('ADMIN')")
//    void deleteById(Long id);

}
