package com.ecomerce.process.order.domain;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int quantity;
    private int price;
    private String productName;

    // many OrderItem belong to one Order
    @ManyToOne
    @JsonIgnore
    private Order order;

    public OrderItem() {}

    public OrderItem(String productName, int price,int quantity) {
        //this.products.add(product);
        this.productName = productName;
        this.quantity = quantity;
        this.price = price * quantity;
    }

    // getters/setters
    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getProductName() {
        return productName;
    }

    public void setProducts(String productName) {
        this.productName = productName;
    }

    public String toString() {
        return String.format("Product: %s, @%s",productName, price);
    }
}
