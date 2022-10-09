module com.example.socketfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.socketfx to javafx.fxml;
    exports com.example.socketfx;
}