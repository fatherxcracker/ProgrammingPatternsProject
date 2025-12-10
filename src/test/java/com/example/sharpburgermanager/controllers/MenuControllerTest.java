package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.factories.MenuItemFactory;
import com.example.sharpburgermanager.models.MenuItem;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuControllerTest {
    private MenuController controller;
    @BeforeEach
    void setup() {
        // Instead of loading from database we make an empty controller
        controller = new MenuController() {
            @Override
            public void addMenuItem(MenuItem item) {

                getMenuItems().add(item);
            }
        };
    }

    @Test
    void testAddMenuItemInMemory() {
        ObservableList<MenuItem> items = controller.getMenuItems();
        int before = items.size();

        MenuItem item = new MenuItem("Cheeseburger", "Burgers", 9.99);
        controller.addMenuItem(item);

        assertEquals(before + 1, items.size());
        assertTrue(items.contains(item));
    }
}