package com.gustavo.controller;

import com.gustavo.App;
import com.gustavo.infra.db.DbIntegrityException;
import com.gustavo.infra.listeners.DataChangeListener;
import com.gustavo.model.entities.Seller;
import com.gustavo.model.services.SellerService;
import com.gustavo.utils.Alerts;
import com.gustavo.utils.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListController implements Initializable, DataChangeListener {

    private SellerService service;

    @FXML
    private TableView<Seller> tableViewSeller;

    @FXML
    private TableColumn<Seller, Integer> tableColumnId;

    @FXML
    private TableColumn<Seller, String> tableColumnName;

    @FXML
    private TableColumn<Seller, String> tableColumnEmail;

    @FXML
    private TableColumn<Seller, Date> tableColumnBirthDate;

    @FXML
    private TableColumn<Seller, Double> tableColumnBaseSalary;

    @FXML
    private TableColumn<Seller, Seller> tableColumnEdit;

    @FXML
    private TableColumn<Seller, Seller> tableColumnRemove;

    @FXML
    private Button btnNew;

    private ObservableList<Seller> obsList;

    @FXML
    public void onBtnNewAction(ActionEvent event) {
        Stage parent = Utils.currentStage(event);
        Seller obj = new Seller();
        createDialogForm(obj,"SellerForm", parent);
    }

    public void setSellerService(SellerService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);

        Stage stage = (Stage) App.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (service == null) {
            throw new IllegalStateException("Service não está injetado");
        }

        List<Seller> SellerList = service.findAll();
        obsList = FXCollections.observableArrayList(SellerList);
        tableViewSeller.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Seller obj, String fileName, Stage parentStage) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fileName + ".fxml"));
            Pane pane = loader.load();

            SellerFormController controller = loader.getController();
            controller.setSeller(obj);
            controller.setSellerService(new SellerService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialog = new Stage();
            dialog.setTitle("Registro de Vendedores");
            dialog.setScene(new Scene(pane));
            dialog.setResizable(false);
            dialog.initOwner(parentStage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("Erro", null, "Erro ao carregar o formulário", Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("Editar");

            @Override
            protected void updateItem(Seller obj, boolean empty) {
                super.updateItem(obj,empty);

                if(obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> createDialogForm(obj,
                        "SellerForm",
                        Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param -> new TableCell<Seller, Seller>() {
            private final Button button = new Button("Excluir");

            @Override
            protected void updateItem(Seller item, boolean empty) {
                super.updateItem(item, empty);

                if(item == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(item));
            }

            private void removeEntity(Seller item) {
                Optional<ButtonType> result =  Alerts.showConfirmation("Confirmação", "Tem certeza da deleção?");

                if(result.get() == ButtonType.OK) {
                    if(service == null) throw new IllegalStateException("service was null");

                    try {
                        service.remove(item);
                        onDataChanged();
                    } catch (DbIntegrityException e) {
                        Alerts.showAlert("Erro ao remover",
                                null,
                                "Não foi possível remover o departamento",
                                Alert.AlertType.ERROR);
                    }
                }
            }
        });
    }
}
