package com.workshop.javafx;

import com.workshop.javafx.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;
    @FXML
    private MenuItem menuItemDepartment;
    @FXML
    private MenuItem menuItemSmenuItemAbout;

    @FXML
    public void onMenuItemSellerAction() {
        System.out.println("onMenuItemSellerAction");
    }

    @FXML
    public void onMenuItemDepartmentAction() {
        loadView("DepartmentList.fxml");
    }

    @FXML
    public void onMenuItemAboutAction() {
        loadView("AboutView.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private synchronized void loadView(String absoluteName) {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource(absoluteName));
            VBox newVbox = loader.load();

            Scene mainScene = Application.getMainScene();
            VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVbox.getChildren().get(0);
            mainVbox.getChildren().clear();
            mainVbox.getChildren().add(mainMenu);
            mainVbox.getChildren().addAll(newVbox.getChildren());

//            DepartmentListController controller = loader.getController();
//            controller.setDepartmentDao(DaoFactory.createDepartmentDao());
//            controller.updateTableView();

        } catch (IOException e) {
            Alerts.showAlert("IO Exception", null, e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }

    }
}
