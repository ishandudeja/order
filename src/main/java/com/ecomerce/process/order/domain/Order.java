package com.ecomerce.process.order.domain;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String customerName, status, email, phoneNumber;
    private int itemCount, amount;
    private LocalDate orderDate;

    @JsonIgnore
    // new: one Order has many OrderItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
    }

    // Ensure items are attached to this order when created
    public Order(String customerName, String email, String phoneNumber,List<OrderItem> items) {
        this.customerName = customerName;
        this.items = new ArrayList<>();
        if (items != null) {
            for (OrderItem item : items) {
                addItem(item); // keep both sides in sync and compute counts
            }
        }
        this.amount = this.items.stream().mapToInt(OrderItem::getPrice).sum();
        this.orderDate = LocalDate.now();
        this.status = "CREATED";
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public Long getId() {
        return id;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public int getItemCount() {
        return itemCount;
    }
    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public LocalDate getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    // getters/setters for items
    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        this.itemCount = (items != null) ? items.size() : 0;
    }

    // helper methods to keep both sides in sync
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        this.itemCount = items.size();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
        this.itemCount = items.size();
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}