package com.example.sharpburgermanager.views;

import com.example.sharpburgermanager.controllers.MenuController;
import com.example.sharpburgermanager.models.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class MenuView extends VBox {

    private final TableView<MenuItem> tableView;
    private final MenuController controller;

    public MenuView(MenuController controller) {
        this.controller = controller;
        this.tableView = new TableView<>();

        createTable();
        bindTableData();

        this.getChildren().add(tableView);
        this.setSpacing(10);
        this.setStyle("-fx-padding: 20;");
    }

    private void createTable() {
        TableColumn<MenuItem, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<MenuItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<MenuItem, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        TableColumn<MenuItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.getColumns().addAll(idCol, nameCol, categoryCol, priceCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void bindTableData() {
        tableView.setItems(controller.getMenuItems());
    }
}
