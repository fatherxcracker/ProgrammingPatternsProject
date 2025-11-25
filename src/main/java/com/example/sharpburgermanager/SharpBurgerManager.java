package com.example.sharpburgermanager;

import com.example.sharpburgermanager.controllers.MenuController;
import com.example.sharpburgermanager.controllers.OrderController;
import com.example.sharpburgermanager.views.MenuView;
import com.example.sharpburgermanager.views.OrderView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SharpBurgerManager extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Welcome to the SharpBurger Manager");
        Button menuBtn = new Button("Menu Item Management");
        Button orderBtn = new Button("Order Management");
        Button exitButton = new Button("Exit");

        menuBtn.setOnAction(e -> openMenuWindow(primaryStage));
        orderBtn.setOnAction(e -> openOrderWindow(primaryStage));
        exitButton.setOnAction(e -> System.exit(0));

        VBox root = new VBox(10, titleLabel, menuBtn, orderBtn, exitButton);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        titleLabel.setStyle("-fx-font-weight: bold;");

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Window");
        primaryStage.show();
    }

    private void openMenuWindow(Stage primaryStage) {
        MenuController controller = new MenuController();
        MenuView view = new MenuView(controller);

        Stage stage = new Stage();
        stage.setScene(new Scene(view, 750, 900));
        stage.setTitle("Menu Item Management");
        stage.show();
        primaryStage.hide(); // Closes main window
    }

    private void openOrderWindow(Stage primaryStage) {
        OrderController controller = new OrderController();
        OrderView view = new OrderView(controller);

        Stage stage = new Stage();
        stage.setScene(new Scene(view, 800, 600));
        stage.setTitle("Order Item Management");
        stage.show();
        primaryStage.hide(); // Closes main window
    }

    public static void main(String[] args) {
        launch();
    }
}