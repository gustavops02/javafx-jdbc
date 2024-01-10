package com.gustavo.controller;

import com.gustavo.db.DbException;
import com.gustavo.infra.listeners.DataChangeListener;
import com.gustavo.model.entities.Department;
import com.gustavo.model.services.DepartmentService;
import com.gustavo.utils.Alerts;
import com.gustavo.utils.Constraints;
import com.gustavo.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private DepartmentService service;
    private Department entity;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();


    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    public void setDepartment(Department entity) { this.entity = entity; }
    public void setDepartmentService (DepartmentService service) {this.service = service;}

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtnSaveAction(ActionEvent event) {
        if(entity == null) {
            throw new IllegalStateException("Entity was null");
        }
        if(service == null) {
            throw new IllegalStateException("Service was null");
        }
        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch(DbException e) {
            Alerts.showAlert("Erro ao cadastrar", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for(DataChangeListener listener : dataChangeListeners) {
            listener.onDataChanged();
        }
    }

    private Department getFormData() {
        Department obj = new Department();
        obj.setId(Utils.tryParseToInt(txtId.getText()));
        obj.setName(txtName.getText());

        return obj;
    }

    @FXML
    public void onBtnCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);

    }

    public void updateFormData () {
        if(entity == null) {
            throw new IllegalStateException("Entity was null");
        }

        txtId.setText(String.valueOf(entity.getId()));
        txtName.setText(entity.getName());
    }

}
