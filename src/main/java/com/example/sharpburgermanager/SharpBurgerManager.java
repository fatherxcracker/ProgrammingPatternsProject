package com.example.sharpburgermanager;

import com.example.sharpburgermanager.controllers.MenuController;
import com.example.sharpburgermanager.controllers.OrderController;
import com.example.sharpburgermanager.views.MenuView;
import com.example.sharpburgermanager.views.OrderView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SharpBurgerManager extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Welcome to the SharpBurger Manager");
        Button menuBtn = new Button("Menu Item Management");
        Button orderBtn = new Button("Order Item Management");
        Button exitButton = new Button("Exit");

        try {
            // for image
            FileInputStream fis = new FileInputStream("src\\images\\Logo.png");
            Image imageLogo = new Image(fis, 300, 300, false, false);
            ImageView imageView = new ImageView(imageLogo);
            imageView.setX(25);
            imageView.setY(25);

            menuBtn.setOnAction(e -> openMenuWindow(primaryStage));
            orderBtn.setOnAction(e -> openOrderWindow(primaryStage));
            exitButton.setOnAction(e -> System.exit(0));

            VBox root = new VBox(30, titleLabel, imageView, menuBtn, orderBtn, exitButton);

            // setting the style
            root.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: grey;");
            titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 30px; -fx-font-family: 'Showcard Gothic'; -fx-text-fill: darkslateblue;");
            menuBtn.setStyle("-fx-background-color: black; -fx-text-fill: darkslateblue; -fx-font-size: 15px;");
            orderBtn.setStyle("-fx-background-color: black; -fx-text-fill: darkslateblue; -fx-font-size: 15px;");
            exitButton.setStyle("-fx-background-color: black; -fx-text-fill: darkslateblue; -fx-font-size: 15px;");

            Scene scene = new Scene(root, 700, 700);
            primaryStage.setScene(scene);
            primaryStage.setTitle("SharpBurger Main Menu");
            primaryStage.show();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        stage.setScene(new Scene(view, 950, 900));
        stage.setTitle("Order Item Management");
        stage.show();
        primaryStage.hide(); // Closes main window
    }

    public static void main(String[] args) {
        launch();
    }
}