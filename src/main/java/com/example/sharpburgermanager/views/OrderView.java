package com.example.sharpburgermanager.views;

import com.example.sharpburgermanager.controllers.OrderController;
import com.example.sharpburgermanager.models.OrderItem;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class OrderView extends VBox {

    private final TableView<OrderItem> tableView;
    private final OrderController controller;

    public OrderView(OrderController controller) {
        this.controller = controller;
        this.tableView = new TableView<>();

        createTable();
        bindTableData();

        HBox labelHBox = new HBox();
        HBox searchHBox = new HBox();
        HBox radioButtonRBHBox = new HBox();
        Button searchButton = new Button("Search");
        TextField searchTF = new TextField();
        Label searchLabel = new Label("Search by Type of the Order: ");
        Label statusTGLabel = new Label("Option filter for Status of the Order: ");

        setupSearchButton(searchButton, searchTF);
        labelHBox.getChildren().add(searchLabel);
        searchHBox.getChildren().addAll(searchTF, searchButton);

        ToggleGroup statusCompletedOrUncompletedTG = new ToggleGroup();
        RadioButton uncompletedRB = new RadioButton("Uncompleted");
        uncompletedRB.setToggleGroup(statusCompletedOrUncompletedTG);
        RadioButton completedRB = new RadioButton("Completed");
        completedRB.setToggleGroup(statusCompletedOrUncompletedTG);
        RadioButton noneRB = new RadioButton("None");
        noneRB.setToggleGroup(statusCompletedOrUncompletedTG);

        uncompletedRB.setOnAction(actionEvent -> handleUncompletedRB());
        completedRB.setOnAction(actionEvent -> handleCompletedRB());
        noneRB.setOnAction(actionEvent -> bindTableData());

        noneRB.setSelected(true);
        completedRB.setSelected(false);
        uncompletedRB.setSelected(false);

        radioButtonRBHBox.getChildren().addAll(noneRB, completedRB, uncompletedRB);
        this.getChildren().addAll(statusTGLabel, radioButtonRBHBox);
        this.getChildren().add(labelHBox);
        this.getChildren().add(searchHBox);
        this.getChildren().add(tableView);
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

        List<OrderItem> orderItemList = controller.getOrderItems();
        List<OrderItem> filteredList = orderItemList.stream()
                .filter(e -> e.typeProperty().getValue().contains(searchInput))
                .toList();
        tableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    private void handleUncompletedRB() {
        List<OrderItem> orderItemList = controller.getOrderItems();
        List<OrderItem> filteredList = orderItemList.stream()
                .filter(e -> !e.statusProperty().getValue())
                .toList();
        tableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    private void handleCompletedRB() {
        List<OrderItem> orderItemList = controller.getOrderItems();
        List<OrderItem> filteredList = orderItemList.stream()
                .filter(e -> e.statusProperty().getValue().equals(true))
                .toList();
        tableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    private void createTable() {
        TableColumn<OrderItem, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<OrderItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<OrderItem, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<OrderItem, Boolean> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean orderCompleted, boolean isRowEmpty) {
                super.updateItem(orderCompleted, isRowEmpty); //isRowEmpty is set to always 'true'
                if (isRowEmpty || orderCompleted == null) {
                    setText(null);
                } else {
                    setText(orderCompleted ? "Completed" : "Uncompleted");
                }
            }
        });

        TableColumn<OrderItem, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.getColumns().addAll(idCol, nameCol, typeCol, statusCol, priceCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void bindTableData() {
        tableView.setItems(controller.getOrderItems());
    }
}
