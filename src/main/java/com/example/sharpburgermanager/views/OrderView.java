package com.example.sharpburgermanager.views;

import com.example.sharpburgermanager.controllers.OrderController;
import com.example.sharpburgermanager.models.MenuItem;
import com.example.sharpburgermanager.models.OrderItem;
import factories.MenuItemFactory;
import factories.OrderItemFactory;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class OrderView extends VBox {

    private final TableView<OrderItem> tableView;
    private final OrderController controller;

    public OrderView(OrderController controller) {
        Label titleLabel = new Label("SharpBurger Order Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox titleHBox = new HBox(titleLabel);
        titleHBox.setStyle("-fx-alignment: center; -fx-padding: 0 0 10 0");

        this.controller = controller;
        this.tableView = new TableView<>();

        createTable();
        bindTableData();

        HBox radioButtonRBHBox = new HBox();
        Button searchButton = new Button("Search");
        TextField searchTF = new TextField();
        Label searchLabel = new Label("Search by Type of the Order: ");
        Label statusTGLabel = new Label("Option filter for Status of the Order: ");
        Button backButton = new Button("Back");

        HBox labelHBox = new HBox(searchLabel);
        HBox searchHBox = new HBox(searchTF, searchButton);
        HBox backHBox = new HBox(backButton);
        backHBox.setStyle("-fx-alignment: bottom-right;");

        setupSearchButton(searchButton, searchTF);

        // Radio Button Group
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
        backButton.setOnAction(actionEvent -> handleBack());

        noneRB.setSelected(true);
        completedRB.setSelected(false);
        uncompletedRB.setSelected(false);
        radioButtonRBHBox.getChildren().addAll(noneRB, completedRB, uncompletedRB);
        // End of RBG

        // Add Operation Code
        Label addLabel = new Label("Add New Order Item");
        TextField addNameTF = new TextField();
        addNameTF.setPromptText("Name");
        TextField addTypeTF = new TextField();
        addTypeTF.setPromptText("Type");
        TextField addPriceTF = new TextField();
        addPriceTF.setPromptText("Price");

        Button addButton = new Button("Add");
        addButton.setOnAction(actionEvent -> {
            try {
                String name = addNameTF.getText();
                String type = addTypeTF.getText();
                double price = Double.parseDouble(addPriceTF.getText());

                if (price < 0) {
                    throw new IndexOutOfBoundsException();
                }

                boolean status;

                Alert statusAlert = new Alert(Alert.AlertType.CONFIRMATION);
                statusAlert.setTitle("Order Status");
                statusAlert.setHeaderText("Order Completed or Incomplete?");
                statusAlert.setContentText("Is the Order completed or Incompleted? Press OK to say it's Completed or otherwise, Incompleted ");
                Optional<ButtonType> result = statusAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    status = true;
                } else {
                    status = false;
                }

                OrderItem orderItem = OrderItemFactory.createOrderItem(name, type, status, price);
                controller.addOrderItem(orderItem);

                addNameTF.clear();
                addTypeTF.clear();
                addPriceTF.clear();

                showAlert(Alert.AlertType.INFORMATION, "Order Data added", "Order Has been added into the list.");

            } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
                showAlert(Alert.AlertType.ERROR, "Price is Negative", "The Price number must be positive or 0.");
                addPriceTF.clear();
            }
        });

        HBox addLabelHBox = new HBox(addLabel);
        HBox addHBox = new HBox(10, addNameTF, addTypeTF, addPriceTF, addButton);

        // Edit
        Label editLabel = new Label("Edit Order Item");
        Button editButton = new Button("Edit");
        TextField editNameTF = new TextField();
        editNameTF.setPromptText("Name");
        TextField editTypeTF = new TextField();
        editTypeTF.setPromptText("Type");
        TextField editPriceTF = new TextField();
        editPriceTF.setPromptText("Price");

        editButton.setOnAction(e -> {
            com.example.sharpburgermanager.models.OrderItem selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    String newName = editNameTF.getText();
                    String newType = editTypeTF.getText();
                    double newPrice = Double.parseDouble(editPriceTF.getText());
                    boolean newStatus;

                    Alert statusAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    statusAlert.setTitle("Order Status");
                    statusAlert.setHeaderText("Order Completed or Incomplete?");
                    statusAlert.setContentText("Is the Order completed or Incompleted? Press OK to say it's Completed or otherwise, Incompleted.");
                    Optional<ButtonType> result = statusAlert.showAndWait();

                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        newStatus = true;
                    } else {
                        newStatus = false;
                    }

                    OrderItem validateItem = OrderItemFactory.createOrderItem(newName, newType, newStatus, newPrice);

                    selected.nameProperty().set(validateItem.getName());
                    selected.typeProperty().set(validateItem.getType());
                    selected.priceProperty().set(validateItem.getPrice());
                    selected.statusProperty().set(validateItem.statusProperty().get());

                    controller.editOrderItem(selected); // Saves changes to our database
                    bindTableData(); // refreshes the table

                } catch (IllegalArgumentException iae) {
                    showAlert(Alert.AlertType.ERROR, "IllegalArgumentException Occurred", iae.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert(Alert.AlertType.ERROR,"Exception Occurred", "Invalid input. Please enter valid values.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Editing Error Occurred","Please select a menu item to edit.");
            }
        });

        HBox editLabelHBox = new HBox(editLabel);
        HBox editHBox = new HBox(10, editNameTF, editTypeTF, editPriceTF, editButton);

        // Delete
        Label deleteLabel = new Label("Delete A Menu Item (Select It From The Table)");
        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(e -> {
            OrderItem selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                controller.deleteOrderItem(selected);
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Exception Occurred", "Please select a menu item to delete.");
            }
        });

        HBox deleteLabelHBox = new HBox(deleteLabel);
        HBox deleteHBox = new HBox(10, deleteButton);

        // Finalizing
        this.getChildren().addAll(
                titleHBox,
                statusTGLabel,
                radioButtonRBHBox,
                labelHBox,
                searchHBox,
                tableView,
                addLabelHBox,
                addHBox,
                editLabelHBox,
                editHBox,
                deleteLabelHBox,
                deleteHBox,
                backHBox
        );
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

    private void handleBack() {
        // Closes current window
        this.getScene().getWindow().hide();

        // Reopens main window
        new com.example.sharpburgermanager.SharpBurgerManager().start(new javafx.stage.Stage());
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

    // Utility method to display an alert dialog
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
