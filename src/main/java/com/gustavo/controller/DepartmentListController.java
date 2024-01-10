package com.gustavo.controller;

import com.gustavo.App;
import com.gustavo.model.entities.Department;
import com.gustavo.model.services.DepartmentService;
import com.gustavo.utils.Alerts;
import com.gustavo.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

    private DepartmentService service;

    @FXML
    private TableView<Department> tableViewDepartment;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private TableColumn<Department, String> tableColumnName;

    @FXML
    private Button btnNew;

    private ObservableList<Department> obsList;

    @FXML
    public void onBtnNewAction(ActionEvent event) {
        Stage parent = Utils.currentStage(event);
        createDialogForm("DepartmentForm", parent);
    }

    public void setDepartmentService(DepartmentService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        Stage stage = (Stage) App.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service não está injetado");
        }

        List<Department> departmentList = service.findAll();
        obsList = FXCollections.observableArrayList(departmentList);
        tableViewDepartment.setItems(obsList);
    }

    private void createDialogForm(String fileName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fileName + ".fxml"));
            Pane pane = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle("Registro de departamento");
            dialog.setScene(new Scene(pane));
            dialog.setResizable(false);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("Erro", null, "Erro ao carregar o formulário", Alert.AlertType.ERROR);
        }
    }
}
