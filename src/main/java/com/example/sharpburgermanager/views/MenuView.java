package com.example.sharpburgermanager.views;

import com.example.sharpburgermanager.controllers.MenuController;
import com.example.sharpburgermanager.models.MenuItem;
import factories.MenuItemFactory;
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
        Label titleLabel = new Label("SharpBurger Menu Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox titleHBox = new HBox(titleLabel);
        titleHBox.setStyle("-fx-alignment: center; -fx-padding: 0 0 10 0");

        this.controller = controller;
        this.tableView = new TableView<>();

        createTable();
        bindTableData();

        // Search Box Code

        HBox labelSearchHBox = new HBox();
        HBox searchHBox = new HBox(10);
        HBox radioButtonHBox = new HBox(10);
        Button searchButton = new Button("Search");
        TextField searchTF = new TextField();
        Label searchLabel = new Label("Search by Category or Name of the Menu Item: ");
        Label radioButtonLabel = new Label("Search Filter Option: ");

        ToggleGroup categoryOrNameTG = new ToggleGroup();
        categoryRB.setToggleGroup(categoryOrNameTG);
        nameRB.setToggleGroup(categoryOrNameTG);

        setupSearchButton(searchButton, searchTF);

        labelSearchHBox.getChildren().add(searchLabel);
        radioButtonHBox.getChildren().addAll(radioButtonLabel, categoryRB, nameRB);
        searchHBox.getChildren().addAll(searchTF, searchButton);

        // Add New Menu Item CRUD operation Code

        Label addLabel = new Label("Add New Menu Item");

        TextField nameTF = new TextField();
        nameTF.setPromptText("Name");

        TextField categoryTF = new TextField();
        categoryTF.setPromptText("Category");

        TextField priceTF = new TextField();
        priceTF.setPromptText("Price");

        Button addButton = new Button("Add");

        addButton.setOnAction(e -> {
            try {
                String name = nameTF.getText();
                String category = categoryTF.getText();
                double price = Double.parseDouble(priceTF.getText());

                MenuItem newItem = MenuItemFactory.createMenuItem(name, category, price);
                controller.addMenuItem(newItem);

                bindTableData(); // refreshes the table

                nameTF.clear();
                categoryTF.clear();
                priceTF.clear();

            } catch (IllegalArgumentException iae) {
                showError(iae.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showError("Invalid input. Please enter valid values.");
            }
        });

        HBox addHBox = new HBox(10, nameTF, categoryTF, priceTF, addButton);

        // Edit CRUD Operation code

        Label editLabel = new Label("Edit Selected Menu Item");

        TextField editNameTF = new TextField();
        editNameTF.setPromptText("Name");

        TextField editCategoryTF = new TextField();
        editCategoryTF.setPromptText("Category");

        TextField editPriceTF = new TextField();
        editPriceTF.setPromptText("Price");

        Button editButton = new Button("Edit");

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                editNameTF.setText(newSelection.getName());
                editCategoryTF.setText(newSelection.getCategory());
                editPriceTF.setText(String.valueOf(newSelection.getPrice()));
            }
        });

        editButton.setOnAction(e -> {
            MenuItem selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    String newName = editNameTF.getText();
                    String newCategory = editCategoryTF.getText();
                    double newPrice = Double.parseDouble(editPriceTF.getText());

                    MenuItem validateItem = MenuItemFactory.createMenuItem(newName, newCategory, newPrice);

                    selected.nameProperty().set(validateItem.getName());
                    selected.categoryProperty().set(validateItem.getCategory());
                    selected.priceProperty().set(validateItem.getPrice());

                    controller.editMenuItem(selected); // Saves changes to our database
                    bindTableData(); // refreshes the table

                } catch (IllegalArgumentException iae) {
                    showError(iae.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Invalid input. Please enter valid values.");
                }
            } else {
                showError("Please select a menu item to edit.");
            }
        });

        HBox editHBox = new HBox(10, editNameTF, editCategoryTF, editPriceTF, editButton);


        // Delete CRUD operation

        Label deleteLabel = new Label("Delete A Menu Item (Select It From The Table)");
        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(e -> {
            MenuItem selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.deleteMenuItem(selected);
            } else {
                showError("Please select a menu item to delete.");
            }
        });

        HBox deleteHBox = new HBox(10, deleteButton);

        // Back Button Code


        Button backBtn = new Button("Back");
        HBox backHBox = new HBox(backBtn);
        backHBox.setStyle("-fx-alignment: bottom-right;");

        backBtn.setOnAction(e -> {
            // Closes current window
            this.getScene().getWindow().hide();

            // Reopens main window
            new com.example.sharpburgermanager.SharpBurgerManager().start(new javafx.stage.Stage());
        });

        this.getChildren().addAll(
                titleHBox,
                labelSearchHBox,
                radioButtonHBox,
                searchHBox,
                tableView,
                addLabel,
                addHBox,
                editLabel,
                editHBox,
                deleteLabel,
                deleteHBox,
                backHBox
        );

        this.setSpacing(15);
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

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
