package com.example.sharpburgermanager.models;

import com.example.sharpburgermanager.controllers.MenuController;
import com.example.sharpburgermanager.controllers.OrderController;
import javafx.beans.property.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class OrderItem {
    private final IntegerProperty id;
    private final StringProperty name;
    private final StringProperty type;
    private final BooleanProperty status;
    private final DoubleProperty price;

    public OrderItem(int id, String name, String type, boolean status, double price) {
        this.id = new SimpleIntegerProperty(id);
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
}

