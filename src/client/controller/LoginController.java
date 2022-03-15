package client.controller;

import client.model.encryption.PublicParameters;
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
import client.main.ClientApp;
import client.model.User;

import it.unisa.dia.gas.jpbc.Element;
import utilities.config.ConfigManager;

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
            getSecretKey(user);
            getPublicParams();
            this.clientApp.showMailsOverview();

        }
    }

    private void getSecretKey(User user) {
        try {
            System.out.println("--------------------------");
            System.out.println("Fetching secret key...");
            URL url = new URL("http://" + ConfigManager.getConfigItem("IP") + ":" +
                    ConfigManager.getConfigItem("PORT") + "/key");

            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            OutputStream out = urlConn.getOutputStream();
            System.out.println("Sending username");
            out.write(user.getUsername().getBytes());

            Pairing pairing = PairingFactory.getPairing("src\\utilities\\curves\\a.properties"); // chargement des paramètres de la courbe elliptique

            InputStream is = urlConn.getInputStream();
            byte[] sKBytes = new byte[Integer.parseInt(urlConn.getHeaderField("Content-length"))];
            is.read(sKBytes);
            Element sk = pairing.getG1().newElementFromBytes(sKBytes);

            this.clientApp.getUser().setsK(sk);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPublicParams() {
        try {
            //System.out.println("--------------------------");
            System.out.println("Fetching public params...");
            URL url = new URL("http://" + ConfigManager.getConfigItem("IP") + ":" +
                    ConfigManager.getConfigItem("PORT") + "/publicparams");

            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);

            Pairing pairing = PairingFactory.getPairing("src\\utilities\\curves\\a.properties"); // chargement des paramètres de la courbe elliptique

            //Get file bytes
            InputStream is = urlConn.getInputStream();
            byte[] filebytes = new byte[Integer.parseInt(urlConn.getHeaderField("Content-length"))];
            is.read(filebytes);
            is.close();

            File deserialized = new File("deserialized.txt");

            //Write file bytes in file
            FileOutputStream fos = new FileOutputStream(deserialized);
            fos.write(filebytes);
            fos.close();

            //Deserialize file in object
            FileInputStream fis = new FileInputStream(deserialized);
            ObjectInputStream ois = new ObjectInputStream(fis);
            PublicParameters publicParams = (PublicParameters) ois.readObject();
            ois.close();
            fis.close();

            deserialized.delete();

            this.clientApp.setPp(publicParams);

            System.out.println("System params fetched !");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
