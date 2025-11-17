package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.models.MenuItem;
import com.example.sharpburgermanager.models.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class OrderController {
    private final HashMap<Integer, OrderItem> orderMap;

    public OrderController() {
        orderMap = new HashMap<>();
        // This is sample data which will be replaced by a database later on
        orderMap.put(Integer.valueOf(1), new OrderItem(1, "Classic Burger", "Burger", true,5.99));
        orderMap.put(Integer.valueOf(2), new OrderItem(2, "Cheese Burger", "Burger", true,6.99));
        orderMap.put(Integer.valueOf(3), new OrderItem(3, "French Fries", "Side", false,2.99));
        orderMap.put(Integer.valueOf(4), new OrderItem(4, "Coke", "Drink", false,1.50));
    }

    // Returns data for TableView as an ObservableList
    public ObservableList<OrderItem> getOrderItems() {
        return FXCollections.observableArrayList(orderMap.values());
    }
}
