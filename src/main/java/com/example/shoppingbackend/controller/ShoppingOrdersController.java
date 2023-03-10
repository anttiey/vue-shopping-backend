package com.example.shoppingbackend.controller;

import com.example.shoppingbackend.dto.OrderDto;
import com.example.shoppingbackend.entity.Order;
import com.example.shoppingbackend.repository.CartRepository;
import com.example.shoppingbackend.repository.OrderRepository;
import com.example.shoppingbackend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ShoppingOrdersController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    JwtService jwtService;

    @GetMapping("/api/orders") /* 주문 가져오기 */
    public ResponseEntity getOrder(@CookieValue(value = "token", required = false) String token) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        List<Order> orders = orderRepository.findAll();

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PostMapping("/api/orders") /* 주문 넣기 */
    public ResponseEntity pushOrder(@RequestBody OrderDto orderDto, @CookieValue(value = "token", required = false) String token) {

        if (!jwtService.isValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        int memberId = jwtService.getId(token);
        Order order = new Order();
        order.setMemberId(memberId);
        order.setName(orderDto.getName());
        order.setAddress(orderDto.getAddress());
        order.setPayment(orderDto.getPayment());
        order.setCardNumber(orderDto.getCardNumber());
        order.setItems(orderDto.getItems());

        orderRepository.save(order);
        cartRepository.deleteByMemberId(memberId); /* 주문이 완료되면 Cart 데이터베이스의 목록 삭제 */

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
