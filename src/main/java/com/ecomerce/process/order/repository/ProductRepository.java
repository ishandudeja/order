package com.ecomerce.process.order.repository;
import com.ecomerce.process.order.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long>{
}
