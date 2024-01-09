package com.gustavo.controller;


import com.gustavo.App;
import com.gustavo.utils.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
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
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction() {
        System.out.println("onMenuItemSellerAction");
    }

    @FXML
    public void onMenuItemDepartmentAction() {
        loadView("DepartmentList");
    }

    @FXML
    public void onMenuItemAboutAction() {
        loadView("About");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    private synchronized void loadView(String fileName) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fileName + ".fxml"));
            VBox newVBox = loader.load();

            Scene mainScene = App.getMainScene();
            VBox mainVBox = (VBox) mainScene.getRoot();

            Node mainMenu = mainVBox.getChildren().get(0);

            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

        } catch (IOException e) {
            Alerts.showAlert("Erro", null, "Não foi possível carregar a página", Alert.AlertType.ERROR);
        }
    }

}
