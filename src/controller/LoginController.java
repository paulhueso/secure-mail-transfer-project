package controller;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.ClientApp;
import model.User;

import it.unisa.dia.gas.jpbc.Element;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
        if (event.getCode() == KeyCode.ENTER) {
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
            User user = new User(mail, password);
            this.clientApp.setUser(user);
            this.clientApp.showMailsOverview();
            getSecretKey(user);
        }
    }

    private void getSecretKey(User user) {
        try {
            System.out.println("Fetching secret key...");
            URL url = new URL("http://192.168.1.12:8080/key");

            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            OutputStream out = urlConn.getOutputStream();
            System.out.println("Sending username");
            out.write(user.getUsername().getBytes());

            InputStream is = urlConn.getInputStream();
            byte[] fileBytes = new byte[Integer.parseInt(urlConn.getHeaderField("Content-length"))];
            is.read(fileBytes);
            Pairing pairing = PairingFactory.getPairing("src\\curves\\a.properties"); // chargement des paramètres de la courbe elliptique

            Element k = pairing.getG1().newElementFromBytes(fileBytes);

            System.out.println("sk : "+k);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
