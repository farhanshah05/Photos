module Photos02 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
    exports model;
    opens model to javafx.fxml;
    exports controller;
    opens controller to javafx.fxml;

//    opens application to javafx.fxml;
//    exports application;
}