module com.example.sharpburgermanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sharpburgermanager.models to javafx.fxml, javafx.base;
    exports com.example.sharpburgermanager;
}