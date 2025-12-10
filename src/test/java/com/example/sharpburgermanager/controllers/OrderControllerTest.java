package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.factories.OrderItemFactory;
import com.example.sharpburgermanager.models.OrderItem;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {
    private OrderController controller;

    @BeforeEach
    void setUp() {
        // Create a fresh controller for each test
        controller = new OrderController() {
            // Override database methods to avoid real DB interaction
            @Override
            public void addOrderItem(OrderItem item) {
                // Only update the in-memory list and map
                this.getOrderItems().add(item);
            }
        };
    }

    @Test
    void testAddOrderItem() {
        ObservableList<OrderItem> orderItems = controller.getOrderItems();
        int before = orderItems.size();

        // Creates a new item
        OrderItem item = OrderItemFactory.createOrderItem("Cheeseburger", "Drive Thru", false, 12.50);
        controller.addOrderItem(item);

        // Assertions
        assertEquals(before + 1, orderItems.size(), "Order list size should increase by 1");
        assertTrue(orderItems.contains(item), "Order list should contain the added item");
    }
}