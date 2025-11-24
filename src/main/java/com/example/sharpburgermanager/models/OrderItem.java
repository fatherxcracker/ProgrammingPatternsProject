package com.example.sharpburgermanager.models;

import com.example.sharpburgermanager.database.ConnectionManager;
import javafx.beans.property.*;

import java.sql.*;
import java.util.HashMap;

public class OrderItem {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty type;
    private final BooleanProperty status;
    private final DoubleProperty price;

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

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orderMap;
    }

    public static void addOrderItem(OrderItem item) {
        String sql = "INSERT INTO orders (name, type, status, price) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS))  {
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
            e.printStackTrace();
        }
    }

    public static void updateOrderItem(OrderItem item) {
        String sql = "UPDATE orders SET name = ?, type = ?, status = ?, price = ? WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getType());
            stmt.setBoolean(3, item.isStatus());
            stmt.setDouble(4, item.getPrice());
            stmt.setInt(5, item.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteOrderItem(OrderItem item) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, item.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

