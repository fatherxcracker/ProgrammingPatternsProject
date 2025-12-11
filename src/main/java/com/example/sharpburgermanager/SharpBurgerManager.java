package com.example.sharpburgermanager;

import com.example.sharpburgermanager.controllers.MenuController;
import com.example.sharpburgermanager.controllers.OrderController;
import com.example.sharpburgermanager.views.MenuView;
import com.example.sharpburgermanager.views.OrderView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SharpBurgerManager extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label titleLabel = new Label("Welcome to the SharpBurger Manager");
        titleLabel.setFont(Font.font("Showcard Gothic", 36));
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: darkslateblue;");

        Label sloganLabel = new Label("Fast, Fresh, Delicious");
        sloganLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 20));
        sloganLabel.setStyle("-fx-text-fill: darkslateblue;");

        Button menuBtn = new Button("Menu Item Management");
        Button orderBtn = new Button("Order Item Management");
        Button exitButton = new Button("Exit");

        menuBtn.setFont(Font.font("Arial", 20));
        orderBtn.setFont(Font.font("Arial", 20));
        exitButton.setFont(Font.font("Arial", 20));

        try {
            // for image
            FileInputStream fis = new FileInputStream("src\\images\\Logo.png");
            Image imageLogo = new Image(fis, 300, 300, false, false);
            ImageView imageView = new ImageView(imageLogo);

            menuBtn.setOnAction(e -> openMenuWindow(primaryStage));
            orderBtn.setOnAction(e -> openOrderWindow(primaryStage));
            exitButton.setOnAction(e -> System.exit(0));


            Label ownersLabel = new Label("Owners: James Luciano and Jodel Santos");
            ownersLabel.setFont(Font.font("Arial", 14));
            ownersLabel.setStyle("-fx-text-fill: darkslateblue;");

            VBox root = new VBox(20, titleLabel, sloganLabel, imageView, menuBtn, orderBtn, exitButton, ownersLabel);
            root.setAlignment(Pos.CENTER);
            root.setStyle("-fx-padding: 30; -fx-background-color: grey");

            menuBtn.setStyle("-fx-background-color: black; -fx-text-fill: darkSlateBlue; -fx-padding: 15 40 15 40; -fx-background-radius: 10;");
            orderBtn.setStyle("-fx-background-color: black; -fx-text-fill: darkSlateBlue; -fx-padding: 15 40 15 40; -fx-background-radius: 10;");
            exitButton.setStyle("-fx-background-color: black; -fx-text-fill: darkSlateBlue; -fx-padding: 15 40 15 40; -fx-background-radius: 10;");

            Scene scene = new Scene(root, 900, 850);
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
