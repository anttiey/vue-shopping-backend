package com.example.shoppingbackend.controller;

import com.example.shoppingbackend.entity.Item;
import com.example.shoppingbackend.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ShoppingItemsController {
    @Autowired
    ItemRepository itemRepository;
    @GetMapping("/")
    public String hello() {
        return "Hello";
    }
    @GetMapping("/api/items")
    public List<Item> getItems() {
        List<Item> items = itemRepository.findAll();
        System.out.println(items);
        return items;
    }
}
