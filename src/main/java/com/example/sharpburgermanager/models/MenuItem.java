package com.example.sharpburgermanager.models;

import com.example.sharpburgermanager.database.ConnectionManager;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;

public class MenuItem {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty category;
    private final DoubleProperty price;

    public MenuItem(String name, String category, double price) {
        this.id = new SimpleIntegerProperty(-1);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.price = new SimpleDoubleProperty(price);
    }

    // Properties for TableView binding
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty categoryProperty() { return category; }
    public DoubleProperty priceProperty() { return price; }

    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getCategory() { return category.get(); }
    public double getPrice() { return price.get(); }

    public static HashMap<Integer, MenuItem> loadFromDatabase() {
        HashMap<Integer, MenuItem> menuMap = new HashMap<>();
        String query = "SELECT * FROM menu";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");

                MenuItem item = new MenuItem(name, category, price);
                item.idProperty().set(id);
                menuMap.put(id, item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return menuMap;
    }

    public static void addMenuItem(MenuItem item) {
        String sql = "INSERT INTO menu (name, category, price) VALUES (?, ?, ?)";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))  {
            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setDouble(3, item.getPrice());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    item.idProperty().set(id); // Updates the id Property so doesn't show -1
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateMenuItem(MenuItem item) {
        String sql = "UPDATE menu SET name = ?, category = ?, price = ? WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteMenuItem(MenuItem item) {
        String sql = "DELETE FROM menu WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, item.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

