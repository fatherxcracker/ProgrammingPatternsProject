package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.models.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.sql.*;

public class MenuController {
    private final HashMap<Integer, MenuItem> menuMap;
    private final ObservableList<MenuItem> menuItems;

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
    }

    // Edit CRUD Operation
    public void editMenuItem(MenuItem item) {
        MenuItem.updateMenuItem(item); // Updates the Database
        menuMap.put(item.getId(), item); // Updates HashMap
        menuItems.setAll(menuMap.values());
    }

    // Delete CRUD operation
    public void deleteMenuItem(MenuItem item) {
        if (item == null) return;

        MenuItem.deleteMenuItem(item);
        menuMap.remove(item.getId());
        menuItems.remove(item);
    }

    // Business Logic: Calculating frequency of menu items by category
    public HashMap<String, Integer> getCategoryFrequency() {
        HashMap<String, Integer> freq = new HashMap<>();

        for (MenuItem item : menuMap.values()) {
            freq.merge(item.getCategory(), 1, Integer::sum);
        }

        return freq;
    }
}
