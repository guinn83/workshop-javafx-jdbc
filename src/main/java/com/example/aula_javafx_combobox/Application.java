package com.example.aula_javafx_combobox;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("mainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Sample JavaFX application");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
