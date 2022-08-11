package com.workshop.javafx.controllers;

import com.workshop.javafx.db.DbException;
import com.workshop.javafx.model.entities.Department;
import com.workshop.javafx.model.entities.Seller;
import com.workshop.javafx.model.exceptions.ValidationException;
import com.workshop.javafx.model.services.DepartmentService;
import com.workshop.javafx.model.services.SellerService;
import com.workshop.javafx.util.Alerts;
import com.workshop.javafx.util.Constraints;
import com.workshop.javafx.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SellerFormController extends Observable implements Initializable {
    private Seller entity;
    private SellerService service;
    private DepartmentService departmentService;

    @FXML
    private Button btSave;
    @FXML
    private Button btCancel;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtEmail;
    @FXML
    private DatePicker dateBirthDate;
    @FXML
    private TextField txtBaseSalary;
    @FXML
    private ComboBox<Department> cmbDepartment;
    @FXML
    private Label lbErrorName;
    @FXML
    private Label lbErrorEmail;
    @FXML
    private Label lbErrorBirthDate;
    @FXML
    private Label lbErrorBaseSalary;
    @FXML
    private Label lbErrorDepartment;

    public void setService(SellerService service) {
        this.service = service;
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
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
        if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")) {
            exception.addError("email", "Field can't be empty");
        }
        if (dateBirthDate.getValue() == null) {
            exception.addError("birthDate", "Field can't be empty");
        }
        if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")) {
            exception.addError("baseSalary", "Field can't be empty");
        }
        if (cmbDepartment.getValue() == null) {
            exception.addError("department", "Field can't be empty");
        }
        if (exception.getErrors().size() == 0) {
            obj.setName(txtName.getText());
            obj.setEmail(txtEmail.getText());
            obj.setBirthDate(Date.from(dateBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            obj.setBaseSalary(Utils.tryPargeToDouble(txtBaseSalary.getText()));
            obj.setDepartment(cmbDepartment.getValue());
        }

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
        Utils.formatDatePicker(dateBirthDate, "dd/MM/yyyy");
    }

    public void updateFormData() {
        if (entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if (entity.getId() != null) {
            txtId.setText(String.valueOf(entity.getId()));
            txtName.setText(entity.getName() == null ? "" : String.valueOf(entity.getName()));
            txtEmail.setText(entity.getEmail() == null ? "" : String.valueOf(entity.getEmail()));
            dateBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
            txtBaseSalary.setText(entity.getBaseSalary() == null ? "" : String.format("%.2f", entity.getBaseSalary()));
        }

        List<Department> list = departmentService.findAll();
        ObservableList<Department> obsList = FXCollections.observableArrayList(list);
        cmbDepartment.setItems(obsList);

        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };

        cmbDepartment.setCellFactory(factory);
        cmbDepartment.setButtonCell(factory.call(null));
        if (entity.getId() != null) cmbDepartment.getSelectionModel().select(entity.getDepartment().getId());
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> field = errors.keySet();

        lbErrorName.setText(field.contains("name") ? errors.get("name") : "");
        lbErrorEmail.setText(field.contains("email") ? errors.get("email") : "");
        lbErrorBirthDate.setText(field.contains("birthDate") ? errors.get("birthDate") : "");
        lbErrorBaseSalary.setText(field.contains("baseSalary") ? errors.get("baseSalary") : "");
        lbErrorDepartment.setText(field.contains("department") ? errors.get("department") : "");
    }
}
