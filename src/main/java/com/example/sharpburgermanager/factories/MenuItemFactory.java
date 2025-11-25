package com.example.sharpburgermanager.factories;

import com.example.sharpburgermanager.models.MenuItem;

public class MenuItemFactory {
    private static final String[] validCategories = {"Burgers", "Beverages", "Sides", "Desserts"};

    public static MenuItem createMenuItem(String name, String category, double price){
        String normalizedCategory = normalizeCategory(category);
        return new MenuItem(name, normalizedCategory, price);
    }

    private static String normalizeCategory(String category){
        for (String c : validCategories){
            if (c.equalsIgnoreCase(category))
                return c;
        }
        throw new IllegalArgumentException("Invalid Category: " + category);
    }

}
