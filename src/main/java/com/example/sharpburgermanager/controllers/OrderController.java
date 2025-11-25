package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.models.OrderItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class OrderController {
    private final HashMap<Integer, OrderItem> orderMap;
    private final ObservableList<OrderItem> orderItems;

    public OrderController() {
        orderMap = OrderItem.loadFromDatabase();
        orderItems = FXCollections.observableArrayList(orderMap.values());

    }

    // Returns data for TableView as an ObservableList
    public ObservableList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void addOrderItem(OrderItem item) {
        OrderItem.addOrderItem(item); // saves to sharpburger database
        orderMap.put(item.getId(), item); // updates HashMap
        orderItems.add(item); // updates ObservableList for TableView
    }

    public void editOrderItem(OrderItem item) {
        OrderItem.updateOrderItem(item); // Updates the Database
        orderMap.put(item.getId(), item); // Updates HashMap
        orderItems.setAll(orderMap.values());
    }

    public void deleteOrderItem(OrderItem item) {
        if (item == null) return;

        OrderItem.deleteOrderItem(item);
        orderMap.remove(item.getId());
        orderItems.remove(item);
    }
}
