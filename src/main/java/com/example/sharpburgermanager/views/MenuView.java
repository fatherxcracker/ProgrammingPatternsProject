package com.example.sharpburgermanager.views;

import com.example.sharpburgermanager.controllers.MenuController;
import com.example.sharpburgermanager.models.MenuItem;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class MenuView extends VBox {

    private final TableView<MenuItem> tableView;
    private final MenuController controller;

    private final RadioButton categoryRB = new RadioButton("Category");
    private final RadioButton nameRB = new RadioButton("Name");

    public MenuView(MenuController controller) {
        this.controller = controller;
        this.tableView = new TableView<>();

        createTable();
        bindTableData();

        HBox labelSearchHBox = new HBox();
        HBox searchHBox = new HBox();
        HBox radioButtonHBox = new HBox();
        Button searchButton = new Button("Search");
        TextField searchTF = new TextField();
        Label searchLabel = new Label("Search by Category or Name of the Menu: ");
        Label radioButtonLabel = new Label("Search Filter Option: ");

        ToggleGroup categoryOrNameTG = new ToggleGroup();
        categoryRB.setToggleGroup(categoryOrNameTG);
        nameRB.setToggleGroup(categoryOrNameTG);

        setupSearchButton(searchButton, searchTF);

        labelSearchHBox.getChildren().add(searchLabel);
        radioButtonHBox.getChildren().addAll(radioButtonLabel, categoryRB, nameRB);
        searchHBox.getChildren().addAll(searchTF, searchButton);

        this.getChildren().add(labelSearchHBox);
        this.getChildren().add(radioButtonHBox);
        this.getChildren().add(searchHBox);
        this.getChildren().addAll(tableView);
        this.setSpacing(10);
        this.setStyle("-fx-padding: 20;");
    }

    private void setupSearchButton(Button searchButton, TextField searchTF) {
        searchButton.setOnAction(event -> handleSearch(searchTF.getText()));
    }

    private void handleSearch(String searchInput) {
        if (searchInput == null || searchInput.isEmpty()) {
            bindTableData();
            return;
        }

        List<MenuItem> menuItemList = controller.getMenuItems();
        if (nameRB.isSelected()) {
            List<MenuItem> filteredList = menuItemList.stream()
                    .filter(e -> e.nameProperty().getValue().contains(searchInput))
                    .toList();
            tableView.setItems(FXCollections.observableArrayList(filteredList));
        } else if (categoryRB.isSelected()) {
            List<MenuItem> filteredList = menuItemList.stream()
                    .filter(e -> e.categoryProperty().getValue().contains(searchInput))
                    .toList();
            tableView.setItems(FXCollections.observableArrayList(filteredList));
        }
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
