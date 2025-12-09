package com.example.sharpburgermanager.models;

import com.example.sharpburgermanager.database.ConnectionManager;
import com.example.sharpburgermanager.logging.SharpBurgerLogger;
import javafx.beans.property.*;

import java.sql.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderItem {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty type;
    private final BooleanProperty status;
    private final DoubleProperty price;
    private static final Logger logger = SharpBurgerLogger.getLogger();

    public OrderItem(String name, String type, boolean status, double price) {
        this.id = new SimpleIntegerProperty(-1); //placeholder
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.status = new SimpleBooleanProperty(status);
        this.price = new SimpleDoubleProperty(price);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public boolean isStatus() {
        return status.get();
    }

    public BooleanProperty statusProperty() {
        return status;
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }



    public static HashMap<Integer, OrderItem> loadFromDatabase() {
        HashMap<Integer, OrderItem> orderMap = new HashMap<>();
        String query = "SELECT * FROM orders";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            // Example of using our SharpBurger logger to log changes in our database such as loading
            logger.info("Loading order items from database...");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                boolean status = rs.getBoolean("status");
                double price = rs.getDouble("price");

                OrderItem item = new OrderItem(name, type, status, price);
                item.idProperty().set(id);
                orderMap.put(id, item);
            }

            logger.info("Successfully loaded " + orderMap.size() + " menu items.");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to load order items.", e);
        }

        return orderMap;
    }

    public static void addOrderItem(OrderItem item) {
        String sql = "INSERT INTO orders (name, type, status, price) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))  {
            logger.log(Level.INFO, "Adding new OrderItem: {0}", item.getName());

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getType());
            stmt.setBoolean(3, item.isStatus());
            stmt.setDouble(4, item.getPrice());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    item.idProperty().set(id); // Updates the id Property so doesn't show -1
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to add OrderItem: " + item.getName(), e);
        }
    }

    public static void updateOrderItem(OrderItem item) {
        String sql = "UPDATE orders SET name = ?, type = ?, status = ?, price = ? WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            logger.log(Level.INFO, "Updating OrderItem ID = {0}", item.getId());

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getType());
            stmt.setBoolean(3, item.isStatus());
            stmt.setDouble(4, item.getPrice());
            stmt.setInt(5, item.getId());
            stmt.executeUpdate();

            logger.log(Level.INFO, "Successfully updated OrderItem ID = {0}", item.getId());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Update failed for OrderItem ID = " + item.getId(), e);
        }
    }

    public static void deleteOrderItem(OrderItem item) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            logger.log(Level.INFO, "Deleting OrderItem ID = {0}", item.getId());

            stmt.setInt(1, item.getId());
            stmt.executeUpdate();

            logger.log(Level.INFO, "Successfully deleted OrderItem ID = {0}", item.getId());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to delete OrderItem ID = " + item.getId(), e);
        }
    }
}

