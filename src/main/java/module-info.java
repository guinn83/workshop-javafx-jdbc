module com.example.java_javafx1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires java.sql;

    opens com.workshop.javafx to javafx.fxml;
    exports com.workshop.javafx;
    exports com.workshop.javafx.model.entities;
    opens com.workshop.javafx.model.entities to javafx.fxml;
}
