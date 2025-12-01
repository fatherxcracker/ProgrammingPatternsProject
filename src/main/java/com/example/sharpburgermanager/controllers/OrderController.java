package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.models.OrderItem;
import com.example.sharpburgermanager.threads.TypeCountThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.function.Consumer;

public class OrderController {
    private final HashMap<Integer, OrderItem> orderMap;
    private final ObservableList<OrderItem> orderItems;
    private Consumer<HashMap<String, Integer>> onTypeUpdateCallback;

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
        recalcAsync();
    }

    public void editOrderItem(OrderItem item) {
        OrderItem.updateOrderItem(item); // Updates the Database
        orderMap.put(item.getId(), item); // Updates HashMap
        orderItems.setAll(orderMap.values());
        recalcAsync();
    }

    public void deleteOrderItem(OrderItem item) {
        if (item == null) return;

        OrderItem.deleteOrderItem(item);
        orderMap.remove(item.getId());
        orderItems.remove(item);
        recalcAsync();
    }

    // Business Logic: Calculating frequency of menu items by type
    public HashMap<String, Integer> getTypeFrequency() {
        HashMap<String, Integer> freq = new HashMap<>();
        for (OrderItem item : orderMap.values()) {
            freq.merge(item.getType(), 1, Integer::sum);
        }
        return freq;
    }

    public HashMap<String, Integer> getStatusFrequency() {
        HashMap<String, Integer> freq = new HashMap<>();
        for (OrderItem item : orderMap.values()) {
            String type = item.getType();
            String status = item.isStatus() ? "Completed" : "Incompleted";

            String key = type + "-" + status;

            freq.merge(key, 1, Integer::sum);
        }
        return freq;
    }

    // Parallel Processing Task (MultiThreading: this recalculates the total of items per category
    public void setTypeUpdateCallback(Consumer<HashMap<String, Integer>> callback) {
        this.onTypeUpdateCallback = callback;
    }

    public void recalcAsync() {
        new TypeCountThread(orderItems, onTypeUpdateCallback).start();
    }
}
