package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.models.MenuItem;
import com.example.sharpburgermanager.threads.CategoryCountThread;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.sql.*;
import java.util.function.Consumer;

public class MenuController {
    private final HashMap<Integer, MenuItem> menuMap;
    private final ObservableList<MenuItem> menuItems;
    private Consumer<HashMap<String, Integer>> onCategoryUpdateCallback;

    public MenuController() {
        menuMap = MenuItem.loadFromDatabase();
        menuItems = FXCollections.observableArrayList(menuMap.values());
    }

    public ObservableList<MenuItem> getMenuItems() {
        return menuItems; // return the same ObservableList
    }

    // Add CRUD operation
    public void addMenuItem(MenuItem item) {
        MenuItem.addMenuItem(item); // saves to sharpburger database
        menuMap.put(item.getId(), item); // updates HashMap
        menuItems.add(item); // updates ObservableList for TableView
        recalcAsync();
    }

    // Edit CRUD Operation
    public void editMenuItem(MenuItem item) {
        MenuItem.updateMenuItem(item); // Updates the Database
        menuMap.put(item.getId(), item); // Updates HashMap
        menuItems.setAll(menuMap.values());
        recalcAsync();
    }

    // Delete CRUD operation
    public void deleteMenuItem(MenuItem item) {
        if (item == null) return;

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
