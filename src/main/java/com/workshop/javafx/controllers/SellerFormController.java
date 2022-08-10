package com.workshop.javafx.controllers;

import com.workshop.javafx.db.DbException;
import com.workshop.javafx.model.entities.Seller;
import com.workshop.javafx.model.exceptions.ValidationException;
import com.workshop.javafx.model.services.SellerService;
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

import java.net.URL;
import java.util.Map;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Set;

public class SellerFormController extends Observable implements Initializable {

    private Seller entity;
    private SellerService service;


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

    public void setService(SellerService service) {
        this.service = service;
    }

    public void setSeller(Seller entity) {
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
            setChanged();
            notifyObservers();
            Utils.currentStage(event).close();
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        } catch (DbException e) {
            Alerts.showAlert("Error saving object", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    private Seller getFormData() {
        Seller obj = new Seller();
        ValidationException exception = new ValidationException("Validation error");

        obj.setId(Utils.tryPargeToInt(txtId.getText()));

        if (txtName.getText() == null || txtName.getText().trim().equals("")) {
            exception.addError("name", "Field can't be empty");
        }
        obj.setName(txtName.getText());

        if (exception.getErrors().size() > 0) {
            throw exception;
        }

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

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> field = errors.keySet();

        if (field.contains("name")) {
            lbErrorName.setText(errors.get("name"));
        }
    }
}
