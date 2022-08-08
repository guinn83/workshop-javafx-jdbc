package com.workshop.javafx;

import com.workshop.javafx.db.DbException;
import com.workshop.javafx.model.entities.Department;
import com.workshop.javafx.model.services.DepartmentService;
import com.workshop.javafx.util.Alerts;
import com.workshop.javafx.util.Constraints;
import com.workshop.javafx.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private Department entity;
    private DepartmentService service;

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

    public void setService(DepartmentService service) {
        this.service = service;
    }

    public void setDepartment(Department entity) {
        this.entity = entity;
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (service == null) {
            throw new IllegalStateException("Service was null");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            Utils.currentStage(event).close();
        } catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    private Department getFormData() {
        Department obj = new Department();
        obj.setId(Utils.tryPargeToInt(txtId.getText()));
        obj.setName(txtName.getText());
        return obj;
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
