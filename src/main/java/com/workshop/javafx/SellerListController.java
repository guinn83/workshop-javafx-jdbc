package com.workshop.javafx;

import com.workshop.javafx.model.entities.Seller;
import com.workshop.javafx.model.dao.DaoFactory;
import com.workshop.javafx.model.dao.SellerDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SellerListController implements Initializable {

    private SellerDao sellerDao = DaoFactory.createSellerDao();

    @FXML
    private TableView<Seller> tableViewDepartment;
    @FXML
    private TableColumn<Seller, Integer> tableColumnId;
    @FXML
    private TableColumn<Seller, String> tableColumnName;
    @FXML
    private Button btNew;

    public void setSellerDao(SellerDao sellerDao) {
        this.sellerDao = sellerDao;
    }

    private ObservableList<Seller> obsList;

    @FXML
    public void onBtNewAction() {
        System.out.println("onBtNewAction");
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
        updateTableView();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        Stage stage = (Stage) Application.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (sellerDao == null) {
            throw new IllegalStateException("Service was null");
        }
        List<Seller> list = sellerDao.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewDepartment.setItems(obsList);
    }
}
