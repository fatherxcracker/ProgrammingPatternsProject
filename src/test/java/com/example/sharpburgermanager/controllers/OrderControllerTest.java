package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.factories.OrderItemFactory;
import com.example.sharpburgermanager.models.OrderItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderControllerTest {
    @Test
    void testAddOrderItem() {
        OrderController controller = new OrderController();
        int before = controller.getOrderItems().size();

        // Creates new item
        OrderItem item = OrderItemFactory.createOrderItem("Cheeseburger", "Drive Thru", false, 12.50);
        controller.addOrderItem(item);

        assertEquals(before + 1, controller.getOrderItems().size());
        assertTrue(controller.getOrderItems().contains(item));
    }
}