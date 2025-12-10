package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.factories.MenuItemFactory;
import com.example.sharpburgermanager.models.MenuItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuControllerTest {
    @Test
    void testAddMenuItem() {
        MenuController controller = new MenuController();
        int before = controller.getMenuItems().size();

        // Creates new item
        MenuItem item = MenuItemFactory.createMenuItem("Cheeseburger", "Burgers", 8.99);
        controller.addMenuItem(item);

        // Makes sure it is equal to the expected result
        assertEquals(before + 1, controller.getMenuItems().size());
        assertTrue(controller.getMenuItems().contains(item));
    }
}