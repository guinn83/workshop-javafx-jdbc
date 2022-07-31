module com.example.java_javafx1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    opens com.example.aula_javafx_combobox to javafx.fxml;
    exports com.example.aula_javafx_combobox;
}
