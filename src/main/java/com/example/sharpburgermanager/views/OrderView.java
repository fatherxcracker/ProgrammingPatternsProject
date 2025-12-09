package com.example.sharpburgermanager.views;

import com.example.sharpburgermanager.SharpBurgerManager;
import com.example.sharpburgermanager.controllers.OrderController;
import com.example.sharpburgermanager.models.OrderItem;
import com.example.sharpburgermanager.factories.OrderItemFactory;
import javafx.collections.FXCollections;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class OrderView extends VBox {

    private final TableView<OrderItem> tableView;
    private final OrderController controller;

    private final XYChart.Data<String, Number> driveThruBarCompleted;
    private final XYChart.Data<String, Number> driveThruBarIncompleted;
    private final XYChart.Data<String, Number> pickUpBarCompleted;
    private final XYChart.Data<String, Number> pickUpBarIncompleted;
    private final XYChart.Data<String, Number> deliveryBarCompleted;
    private final XYChart.Data<String, Number> deliveryBarIncompleted;
    private final XYChart.Data<String, Number> uberEatsBarCompleted;
    private final XYChart.Data<String, Number> uberEatsBarIncompleted;
    private final XYChart.Data<String, Number> dineInBarCompleted;
    private final XYChart.Data<String, Number> dineInBarIncompleted;

    public OrderView(OrderController controller) {
        Label titleLabel = new Label("SharpBurger Order Management");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: darkslateblue; -fx-font-family: 'Showcard Gothic';");
        HBox titleHBox = new HBox(titleLabel);
        titleHBox.setStyle("-fx-alignment: center; -fx-padding: 0 0 10 0;");

        this.controller = controller;
        this.tableView = new TableView<>();

        createTable();
        bindTableData();

        controller.setTypeUpdateCallback(freqMap -> updateBarGraph());

        Button searchButton = new Button("Search");
        TextField searchTF = new TextField();
        Label searchLabel = new Label("Search by Type of the Order: ");
        Label statusTGLabel = new Label("Option filter for Status of the Order: ");
        Button backButton = new Button("Back");

        HBox labelHBox = new HBox(searchLabel);
        HBox searchHBox = new HBox(10, searchTF, searchButton);
        HBox backHBox = new HBox(backButton);
        backHBox.setStyle("-fx-alignment: bottom-right; -fx-background-color: grey");

        setupSearchButton(searchButton, searchTF);

        // Radio Button Group
        ToggleGroup statusCompletedOrIncompletedTG = new ToggleGroup();
        RadioButton incompletedRB = new RadioButton("Incompleted");
        incompletedRB.setToggleGroup(statusCompletedOrIncompletedTG);
        RadioButton completedRB = new RadioButton("Completed");
        completedRB.setToggleGroup(statusCompletedOrIncompletedTG);
        RadioButton noneRB = new RadioButton("None");
        noneRB.setToggleGroup(statusCompletedOrIncompletedTG);

        incompletedRB.setOnAction(actionEvent -> handleIncompletedRB());
        completedRB.setOnAction(actionEvent -> handleCompletedRB());
        backButton.setOnAction(actionEvent -> handleBack());

        noneRB.setSelected(true);
        completedRB.setSelected(false);
        incompletedRB.setSelected(false);
        HBox radioButtonRBHBox = new HBox(10, noneRB, completedRB, incompletedRB);
        // End of RBG

        // Add and Edit Operation Code
        Label orderLabel = new Label("Add a new Order Item, select a row to update its data or delete A Order Item (Select It From The Table)");
        TextField nameTF = new TextField();
        nameTF.setPromptText("Name");
        TextField typeTF = new TextField();
        typeTF.setPromptText("Type");
        TextField priceTF = new TextField();
        priceTF.setPromptText("Price");
        Button editButton = new Button("Update");

        ToggleGroup editStatusTG = new ToggleGroup();
        RadioButton editStatTrueRB = new RadioButton("Completed");
        RadioButton editStatFalseRB = new RadioButton("Incompleted");

        editStatFalseRB.setToggleGroup(editStatusTG);
        editStatTrueRB.setToggleGroup(editStatusTG);

        Button addButton = new Button("Add");
        addButton.setOnAction(actionEvent -> handleAdd(nameTF, typeTF, priceTF, editStatTrueRB));

        // Delete
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(actionEvent -> handleDelete());

        HBox addLabelHBox = new HBox(orderLabel);
        HBox addHBox = new HBox(10, nameTF, typeTF, priceTF, editStatTrueRB, editStatFalseRB, addButton, editButton, deleteButton);

        addButton.setStyle("-fx-text-fill: white; -fx-background-color: black;");
        editButton.setStyle("-fx-text-fill: white; -fx-background-color: black;");
        deleteButton.setStyle("-fx-text-fill: white; -fx-background-color: black;");
        backButton.setStyle("-fx-text-fill: white; -fx-background-color: black;");

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameTF.setText(newSelection.getName());
                typeTF.setText(newSelection.getType());
                priceTF.setText(String.valueOf(newSelection.getPrice()));
                if (newSelection.isStatus()) {
                    editStatTrueRB.setSelected(true);
                } else {
                    editStatFalseRB.setSelected(true);
                }
            }
        });

        editButton.setOnAction(e -> handleEdit(nameTF, typeTF, priceTF, editStatTrueRB));

        //Bar Graph (Based on Status)
        CategoryAxis categoryAxis = new CategoryAxis(); // x-axis
        NumberAxis numberAxis = new NumberAxis(); // y-axis
        BarChart<String, Number> barChart = new BarChart<>(categoryAxis, numberAxis);

        XYChart.Series<String, Number> completedSeries = new XYChart.Series<>();
        completedSeries.setName("Completed");

        XYChart.Series<String, Number> incompletedSeries = new XYChart.Series<>();
        incompletedSeries.setName("Incompleted");

        driveThruBarCompleted = new XYChart.Data<>("Drive Thru", 0);
        driveThruBarIncompleted = new XYChart.Data<>("Drive Thru", 0);
        pickUpBarCompleted = new XYChart.Data<>("Pick Up", 0);
        pickUpBarIncompleted = new XYChart.Data<>("Pick Up", 0);
        deliveryBarCompleted = new XYChart.Data<>("Delivery", 0);
        deliveryBarIncompleted = new XYChart.Data<>("Delivery", 0);
        uberEatsBarCompleted = new XYChart.Data<>("Uber Eats", 0);
        uberEatsBarIncompleted = new XYChart.Data<>("Uber Eats", 0);
        dineInBarCompleted = new XYChart.Data<>("Dine In", 0);
        dineInBarIncompleted = new XYChart.Data<>("Dine In", 0);

        completedSeries.getData().addAll(
                Arrays.asList(driveThruBarCompleted, pickUpBarCompleted, deliveryBarCompleted, uberEatsBarCompleted, dineInBarCompleted)
        );

        incompletedSeries.getData().addAll(
                Arrays.asList(driveThruBarIncompleted, pickUpBarIncompleted, deliveryBarIncompleted, uberEatsBarIncompleted, dineInBarIncompleted)
        );

        barChart.getData().addAll(Arrays.asList(completedSeries, incompletedSeries));
        barChart.setTitle("Completed vs Incompleted Orders by Order Type");
        barChart.setStyle("-fx-font-weight: bold; -fx-background-color: white");



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
                barChart,
                backHBox
        );
        this.setSpacing(10);
        this.setStyle("-fx-padding: 20; -fx-background-color: grey; -fx-font-weight: bold;");

        updateBarGraph();
    }

    private void updateBarGraph() {
        HashMap<String, Integer> freq = controller.getStatusFrequency();

        driveThruBarCompleted.setYValue(freq.getOrDefault("Drive Thru-Completed", 0));
        driveThruBarIncompleted.setYValue(freq.getOrDefault("Drive Thru-Incompleted", 0));

        pickUpBarCompleted.setYValue(freq.getOrDefault("Pick Up-Completed", 0));
        pickUpBarIncompleted.setYValue(freq.getOrDefault("Pick Up-Incompleted", 0));

        deliveryBarCompleted.setYValue(freq.getOrDefault("Delivery-Completed", 0));
        deliveryBarIncompleted.setYValue(freq.getOrDefault("Delivery-Incompleted", 0));

        uberEatsBarCompleted.setYValue(freq.getOrDefault("Uber Eats-Completed", 0));
        uberEatsBarIncompleted.setYValue(freq.getOrDefault("Uber Eats-Incompleted", 0));

        dineInBarCompleted.setYValue(freq.getOrDefault("Dine In-Completed", 0));
        dineInBarIncompleted.setYValue(freq.getOrDefault("Dine In-Incompleted", 0));
    }

    // Setups the search button function down below
    private void setupSearchButton(Button searchButton, TextField searchTF) {
        searchButton.setOnAction(event -> handleSearch(searchTF.getText()));
    }

    // handles the search function
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

    // handles the radio button function for the search filter when selecting the incompleted button
    private void handleIncompletedRB() {
        List<OrderItem> orderItemList = controller.getOrderItems();
        List<OrderItem> filteredList = orderItemList.stream()
                .filter(e -> !e.statusProperty().getValue())
                .toList();
        tableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    // handles the radio button function for the search filter when selecting the completed button
    private void handleCompletedRB() {
        List<OrderItem> orderItemList = controller.getOrderItems();
        List<OrderItem> filteredList = orderItemList.stream()
                .filter(e -> e.statusProperty().getValue().equals(Optional.of(true)))
                .toList();
        tableView.setItems(FXCollections.observableArrayList(filteredList));
    }

    // handles back function to the main menu window
    private void handleBack() {
        // Closes current window
        this.getScene().getWindow().hide();

        // Reopens main window
        new SharpBurgerManager().start(new Stage());
    }

    // handles the add button function
    private void handleAdd(TextField nameTF, TextField typeTF, TextField priceTF, RadioButton radioButtonTrue) {
        try {
            String name = nameTF.getText();
            String type = typeTF.getText();
            double price = Double.parseDouble(priceTF.getText());

            if (price < 0) {
                throw new IndexOutOfBoundsException();
            }

            boolean status = radioButtonTrue.isSelected();

            OrderItem orderItem = OrderItemFactory.createOrderItem(name, type, status, price);
            controller.addOrderItem(orderItem);
            updateBarGraph();

            nameTF.clear();
            typeTF.clear();
            priceTF.clear();

            showAlert(Alert.AlertType.INFORMATION, "Order Data added", "Order, " +  orderItem.getName() + " Has been added into the list.");
        } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            showAlert(Alert.AlertType.ERROR, "Price is Negative", "The Price number must be positive or 0.");
            priceTF.clear();
        }
    }

    // handles the edit button function
    private void handleEdit(TextField editNameTF, TextField editTypeTF, TextField editPriceTF, RadioButton editStatTrueRB) {
        OrderItem selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String newName = editNameTF.getText();
                String newType = editTypeTF.getText();
                double newPrice = Double.parseDouble(editPriceTF.getText());
                boolean newStatus = editStatTrueRB.isSelected();

                OrderItem validateItem = OrderItemFactory.createOrderItem(newName, newType, newStatus, newPrice);

                selected.nameProperty().set(validateItem.getName());
                selected.typeProperty().set(validateItem.getType());
                selected.priceProperty().set(validateItem.getPrice());
                selected.statusProperty().set(validateItem.statusProperty().get());

                controller.editOrderItem(selected); // Saves changes to our database
                bindTableData(); // refreshes the table
                updateBarGraph();

            } catch (IllegalArgumentException iae) {
                showAlert(Alert.AlertType.ERROR, "IllegalArgumentException Occurred", iae.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR,"Exception Occurred", "Invalid input. Please enter valid values.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Editing Error Occurred","Please select a menu item to edit.");
        }
    }

    // handles the delete button function
    private void handleDelete() {
        OrderItem selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            controller.deleteOrderItem(selected);
            updateBarGraph();
        } else {
            showAlert(Alert.AlertType.ERROR, "Delete Exception Occurred", "Please select a menu item to delete.");
        }
    }

    // creates the table for OrderView
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
                    setText(orderCompleted ? "Completed" : "Incompleted");
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
