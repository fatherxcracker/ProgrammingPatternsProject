package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.logging.SharpBurgerLogger;
import com.example.sharpburgermanager.models.MenuItem;
import com.example.sharpburgermanager.threads.CategoryCountThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.sql.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuController {
    private final HashMap<Integer, MenuItem> menuMap;
    private final ObservableList<MenuItem> menuItems;
    private Consumer<HashMap<String, Integer>> onCategoryUpdateCallback;
    private static final Logger logger = SharpBurgerLogger.getLogger();

    public MenuController() {
        menuMap = MenuItem.loadFromDatabase();
        menuItems = FXCollections.observableArrayList(menuMap.values());
    }

    public ObservableList<MenuItem> getMenuItems() {
        return menuItems; // return the same ObservableList
    }

    // Add CRUD operation
    public void addMenuItem(MenuItem item) {
        logger.log(Level.INFO, "Adding MenuItem: {0}", item.getName());

        MenuItem.addMenuItem(item); // saves to sharpburger database
        menuMap.put(item.getId(), item); // updates HashMap
        menuItems.add(item); // updates ObservableList for TableView
        recalcAsync();
    }

    // Edit CRUD Operation
    public void editMenuItem(MenuItem item) {
        logger.log(Level.INFO, "Editing MenuItem ID={0}", item.getId());

        MenuItem.updateMenuItem(item); // Updates the Database
        menuMap.put(item.getId(), item); // Updates HashMap
        menuItems.setAll(menuMap.values());
        recalcAsync();
    }

    // Delete CRUD operation
    public void deleteMenuItem(MenuItem item) {
        if (item == null) return;
        logger.log(Level.INFO, "Deleting MenuItem ID={0}", item.getId());

        MenuItem.deleteMenuItem(item);
        menuMap.remove(item.getId());
        menuItems.remove(item);
        recalcAsync();
    }

    // Business Logic: Calculating frequency of menu items by category
    public HashMap<String, Integer> getCategoryFrequency() {
        HashMap<String, Integer> freq = new HashMap<>();

        for (MenuItem item : menuMap.values()) {
            freq.merge(item.getCategory(), 1, Integer::sum);
        }

        return freq;
    }

    // Parallel Processing Task (MultiThreading: this recalculates the total of items per category
    public void setCategoryUpdateCallback(Consumer<HashMap<String, Integer>> callback) {
        this.onCategoryUpdateCallback = callback;
    }

    public void recalcAsync() {
        new CategoryCountThread(menuItems, onCategoryUpdateCallback).start();
    }
}
