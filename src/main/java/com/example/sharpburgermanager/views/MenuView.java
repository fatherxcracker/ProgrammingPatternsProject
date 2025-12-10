package com.example.sharpburgermanager.views;

import com.example.sharpburgermanager.controllers.MenuController;
import com.example.sharpburgermanager.models.MenuItem;
import com.example.sharpburgermanager.factories.MenuItemFactory;
import com.example.sharpburgermanager.threads.CategoryCountThread;
import javafx.collections.FXCollections;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.List;

public class MenuView extends VBox {

    private final TableView<MenuItem> tableView;
    private final MenuController controller;

    private final RadioButton categoryRB = new RadioButton("Category");
    private final RadioButton nameRB = new RadioButton("Name");
    private PieChart categoryChart;

    public MenuView(MenuController controller) {
        this.controller = controller;
        this.tableView = new TableView<>();

        controller.setCategoryUpdateCallback(freqMap -> {
            PieChart.Data[] data = freqMap.entrySet()
                    .stream()
                    .map(e -> new PieChart.Data(e.getKey(), e.getValue()))
                    .toArray(PieChart.Data[]::new);
            categoryChart.setData(FXCollections.observableArrayList(data));
        });

        createLayout();
    }

    // Building user interface layout
    private void createLayout() {
        Label titleLabel = new Label("SharpBurger Menu Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: darkslateblue; -fx-font-family: 'Showcard Gothic';");
        HBox titleHBox = new HBox(titleLabel);
        titleHBox.setStyle("-fx-alignment: center; -fx-padding: 0 0 10 0");

        createTable();
        bindTableData();

        categoryChart = new PieChart();
        categoryChart.setTitle("Menu Items by Category");
        categoryChart.setPrefSize(350, 250); // It is width x height
        categoryChart.setMinSize(350, 250);
        categoryChart.setMaxSize(350, 250);
        categoryChart.lookup(".chart-title").setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        updateCategoryChart();

        VBox searchSection = buildSearchSection();
        VBox crudSection = buildCRUDSection();
        HBox backSection = buildBackSection();

        this.getChildren().addAll(
                titleHBox,
                searchSection,
                tableView,
                crudSection,
                categoryChart,
                backSection
        );

        this.setSpacing(15);
        this.setStyle("-fx-padding: 20; -fx-background-color: GREY;");
    }

    // Handling searching code
    private VBox buildSearchSection() {
        Label searchLabel = new Label("Search by Category or Name:");
        Label filterLabel = new Label("Search Filter:");

        ToggleGroup toggleGroup = new ToggleGroup();
        categoryRB.setToggleGroup(toggleGroup);
        nameRB.setToggleGroup(toggleGroup);

        TextField searchTF = new TextField();
        Button searchBtn = new Button("Search");
        searchBtn.setOnAction(e -> handleSearch(searchTF.getText()));

        Button showAllBtn = new Button("Show All");
        showAllBtn.setOnAction(e -> {
            searchTF.clear();
            bindTableData(); // reloads everything
        });

        HBox radioBox = new HBox(10, filterLabel, categoryRB, nameRB);
        HBox searchBox = new HBox(10, searchTF, searchBtn, showAllBtn);

        return new VBox(5, searchLabel, radioBox, searchBox);
    }

    private void handleSearch(String searchInput) {
        if (searchInput == null || searchInput.isEmpty()) {
            bindTableData();
            return;
        }

        // So case in-sensitive
        String lowerSearch = searchInput.toLowerCase();

        List<MenuItem> list = controller.getMenuItems();
        if (nameRB.isSelected()) {
            tableView.setItems(FXCollections.observableArrayList(
                    list.stream().filter(e -> e.getName().toLowerCase().contains(lowerSearch)).toList()));
        } else if (categoryRB.isSelected()) {
            tableView.setItems(FXCollections.observableArrayList(
                    list.stream().filter(e -> e.getCategory().toLowerCase().contains(lowerSearch)).toList()));
        }
    }

    private VBox buildCRUDSection() {
        Label crudLabel = new Label("Manage Menu Items");

        TextField nameTF = new TextField();
        nameTF.setPromptText("Name");
        nameTF.setPrefWidth(150);

        TextField categoryTF = new TextField();
        categoryTF.setPromptText("Category");
        categoryTF.setPrefWidth(150);

        TextField priceTF = new TextField();
        priceTF.setPromptText("Price");
        priceTF.setPrefWidth(100);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                nameTF.setText(newV.getName());
                categoryTF.setText(newV.getCategory());
                priceTF.setText(String.valueOf(newV.getPrice()));
            }
        });

        // Add button
        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            try {
                double price = Double.parseDouble(priceTF.getText());
                if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");

                MenuItem newItem = MenuItemFactory.createMenuItem(
                        nameTF.getText(), categoryTF.getText(), price);

                controller.addMenuItem(newItem);
                bindTableData();
                updateCategoryChart();

                nameTF.clear();
                categoryTF.clear();
                priceTF.clear();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        // Edit button
        Button editBtn = new Button("Edit");
        editBtn.setOnAction(e -> {
            MenuItem selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select an item to edit.");
                return;
            }

            try {
                double price = Double.parseDouble(priceTF.getText());
                if (price < 0) throw new IllegalArgumentException("Price cannot be negative.");

                MenuItem validated = MenuItemFactory.createMenuItem(
                        nameTF.getText(), categoryTF.getText(), price);

                selected.nameProperty().set(validated.getName());
                selected.categoryProperty().set(validated.getCategory());
                selected.priceProperty().set(validated.getPrice());

                controller.editMenuItem(selected);
                bindTableData();
                updateCategoryChart();
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        // Delete button
        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> {
            MenuItem selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showError("Select an item to delete.");
                return;
            }

            controller.deleteMenuItem(selected);
            bindTableData();
            updateCategoryChart();

            nameTF.clear();
            categoryTF.clear();
            priceTF.clear();
        });

        HBox crudLine = new HBox(10,
                nameTF,
                categoryTF,
                priceTF,
                addBtn,
                editBtn,
                deleteBtn
        );
        crudLine.setStyle("-fx-padding: 5 0 0 0;");

        return new VBox(5, crudLabel, crudLine);
    }

    // Back button code
    private HBox buildBackSection() {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            this.getScene().getWindow().hide();
            new com.example.sharpburgermanager.SharpBurgerManager().start(new javafx.stage.Stage());
        });
        HBox box = new HBox(backBtn);
        box.setStyle("-fx-alignment: bottom-right;");
        return box;
    }

    // Pie Chart (now recalculates in the background)
    private void updateCategoryChart() {
        CategoryCountThread thread = new CategoryCountThread(
                controller.getMenuItems(),
                resultMap -> {
                    PieChart.Data[] data = resultMap.entrySet()
                            .stream()
                            .map(e -> new PieChart.Data(e.getKey(), e.getValue()))
                            .toArray(PieChart.Data[]::new);

                    categoryChart.setData(FXCollections.observableArrayList(data));
                }
        );
        thread.start();
    }

    // Making Table
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
