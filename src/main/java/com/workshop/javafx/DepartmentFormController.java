package com.workshop.javafx;

import com.workshop.javafx.model.entities.Department;
import com.workshop.javafx.util.Constraints;
import com.workshop.javafx.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department entity;

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


    public void setDepartment(Department entity) {
        this.entity = entity;
    }

    @FXML
    public void onBtSaveAction() {
        entity.setName(String.valueOf(txtName));
    }

    public void onBtCancelAction(ActionEvent event) {
        Stage currentStage = Utils.currentStage(event);
        currentStage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName() == null ? "" : String.valueOf(entity.getName()));
    }
}
