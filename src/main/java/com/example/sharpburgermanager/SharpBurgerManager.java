package com.example.sharpburgermanager;

import com.example.sharpburgermanager.controllers.MenuController;
import com.example.sharpburgermanager.views.MenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SharpBurgerManager extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button menuBtn = new Button("Menu Item Management");
        Button orderBtn = new Button("Order Management");

        menuBtn.setOnAction(e -> openMenuWindow());

        VBox root = new VBox(10, menuBtn, orderBtn);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Main Window");
        primaryStage.show();
    }

    private void openMenuWindow() {
        MenuController controller = new MenuController();
        MenuView view = new MenuView(controller);

        Stage stage = new Stage();
        stage.setScene(new Scene(view, 400, 300));
        stage.setTitle("Menu Item Management");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}