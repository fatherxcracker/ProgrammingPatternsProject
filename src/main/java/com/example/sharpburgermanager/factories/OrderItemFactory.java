package com.example.sharpburgermanager.factories;

import com.example.sharpburgermanager.models.OrderItem;

public class OrderItemFactory {
    private static final String[] validTypes = {"Drive Thru", "Pick Up", "Delivery", "Uber Eats", "Dine In"};

    public static OrderItem createOrderItem(String name, String type, boolean status, double price){
        String normalizeType = normalizeType(type);
        return new OrderItem(name, normalizeType, status, price);
    }

    private static String normalizeType(String type){
        for (String t : validTypes){
            if (t.equalsIgnoreCase(type))
                return t;
        }
        throw new IllegalArgumentException("Invalid Category: " + type + "\nAcceptable Order Types: Drive Thru, Pick up, Delivery, Uber Eats and Dine In");
    }
}
