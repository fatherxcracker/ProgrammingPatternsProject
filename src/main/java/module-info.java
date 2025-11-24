module com.example.sharpburgermanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires java.sql;


    opens com.example.sharpburgermanager.models to javafx.fxml, javafx.base;
    exports com.example.sharpburgermanager;
}