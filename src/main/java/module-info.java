module com.example.pingpongproyect2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.pingpongproyect2 to javafx.fxml;
    exports com.example.pingpongproyect2;
}