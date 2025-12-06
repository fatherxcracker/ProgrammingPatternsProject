package com.example.sharpburgermanager.models;

import com.example.sharpburgermanager.database.ConnectionManager;
import com.example.sharpburgermanager.logging.SharpBurgerLogger;
import javafx.beans.property.*;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuItem {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty category;
    private final DoubleProperty price;
    private static final Logger logger = SharpBurgerLogger.getLogger();

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
            // Example of using our SharpBurger logger to log changes in our database such as loading
            logger.info("Loading menu items from database...");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double price = rs.getDouble("price");

                MenuItem item = new MenuItem(name, category, price);
                item.idProperty().set(id);
                menuMap.put(id, item);
            }

            logger.info("Successfully loaded " + menuMap.size() + " menu items.");

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to load menu items.", e);
        }
        return menuMap;
    }

    public static void addMenuItem(MenuItem item) {
        String sql = "INSERT INTO menu (name, category, price) VALUES (?, ?, ?)";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            logger.log(Level.INFO, "Adding new MenuItem: {0}", item.getName());

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setDouble(3, item.getPrice());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    item.idProperty().set(id);
                    logger.log(Level.INFO, "MenuItem added with generated ID = {0}", id);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to add MenuItem: " + item.getName(), e);
        }
    }

    public static void updateMenuItem(MenuItem item) {
        String sql = "UPDATE menu SET name = ?, category = ?, price = ? WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            logger.log(Level.INFO, "Updating MenuItem ID = {0}", item.getId());

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getCategory());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getId());
            stmt.executeUpdate();

            logger.log(Level.INFO, "Successfully updated MenuItem ID = {0}", item.getId());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Update failed for MenuItem ID = " + item.getId(), e);
        }
    }

    public static void deleteMenuItem(MenuItem item) {
        String sql = "DELETE FROM menu WHERE id = ?";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            logger.log(Level.INFO, "Deleting MenuItem ID = {0}", item.getId());

            stmt.setInt(1, item.getId());
            stmt.executeUpdate();

            logger.log(Level.INFO, "Successfully deleted MenuItem ID = {0}", item.getId());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to delete MenuItem ID = " + item.getId(), e);
        }
    }

}

