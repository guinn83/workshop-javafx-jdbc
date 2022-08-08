package com.workshop.javafx;

import com.workshop.javafx.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private Label lbErrorName;

    @FXML
    public void onBtSaveAction() {
        System.out.println("onBtSaveAction");
    }

    public void onBtCancelAction() {
        System.out.println("onBtCancelAction");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }
}
