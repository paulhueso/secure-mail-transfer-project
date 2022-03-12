package client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import client.main.ClientApp;
import client.model.User;

public class LoginController {

    private ClientApp clientApp;

    @FXML
    private Button loginBtn;
    @FXML
    private TextField emailInput;
    @FXML
    private PasswordField passwordInput;

    public ClientApp getClientApp() {
        return clientApp;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleLoginMouse(ActionEvent event) {
        login();
    }

    @FXML
    private void handleLoginKeyEvent(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            login();
        }
    }

    private void login() {
        String mail = emailInput.getText();
        String password = passwordInput.getText();

        if (mail.isEmpty() || mail == null || password.isEmpty() || password == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Missings arguments");
            alert.setContentText("Fill out blanks");
            alert.showAndWait();
        } else {
            this.clientApp.setUser(new User(mail, password));
            this.clientApp.showMailsOverview();
        }
    }
}
