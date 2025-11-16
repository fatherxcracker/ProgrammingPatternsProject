package com.example.sharpburgermanager.models;

import javafx.beans.property.*;

public class MenuItem {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty category;
    private final DoubleProperty price;

    public MenuItem(int id, String name, String category, double price) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.price = new SimpleDoubleProperty(price);
    }

    // Properties for TableView binding
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty categoryProperty() { return category; }
    public DoubleProperty priceProperty() { return price; }

    // Getters and setters
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getCategory() { return category.get(); }
    public double getPrice() { return price.get(); }

    public void setId(int id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setCategory(String category) { this.category.set(category); }
    public void setPrice(double price) { this.price.set(price); }
}

