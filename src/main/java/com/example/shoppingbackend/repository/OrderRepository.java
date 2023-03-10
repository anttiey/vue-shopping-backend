package com.example.shoppingbackend.repository;

import com.example.shoppingbackend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}