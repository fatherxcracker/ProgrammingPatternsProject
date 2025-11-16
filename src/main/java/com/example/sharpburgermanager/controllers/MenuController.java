package com.example.sharpburgermanager.controllers;

import com.example.sharpburgermanager.models.MenuItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class MenuController {

    private final HashMap<Integer, MenuItem> menuMap;

    public MenuController() {
        menuMap = new HashMap<>();
        // This is sample data which will be replaced by a database later on
        menuMap.put(Integer.valueOf(1), new MenuItem(1, "Classic Burger", "Burger", 5.99));
        menuMap.put(Integer.valueOf(2), new MenuItem(2, "Cheese Burger", "Burger", 6.99));
        menuMap.put(Integer.valueOf(3), new MenuItem(3, "French Fries", "Side", 2.99));
        menuMap.put(Integer.valueOf(4), new MenuItem(4, "Coke", "Drink", 1.50));
    }

    // Returns data for TableView as an ObservableList
    public ObservableList<MenuItem> getMenuItems() {
        return FXCollections.observableArrayList(menuMap.values());
    }

}
