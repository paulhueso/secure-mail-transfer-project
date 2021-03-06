package client.controller;

import client.model.encryption.AESCrypto;
import client.model.encryption.CryptedCredentials;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

        if (mail == null || mail.isEmpty() || password == null || password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Missings arguments");
            alert.setContentText("Fill out blanks");
            alert.showAndWait();
        } else {
            User user = new User(mail, password);
            this.clientApp.setUser(user);
            getPublicParams();
            boolean gotSecretKey = getSecretKey(user);
            if (!gotSecretKey) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Authentication error");
                alert.setContentText("Invalid username or password");
                alert.showAndWait();
            }
            else {
                this.clientApp.showMailsOverview();

            }
        }
    }

    private boolean getSecretKey(User user) {
        Pairing pairing = PairingFactory.getPairing("src\\utilities\\curves\\a.properties"); // chargement des param??tres de la courbe elliptique
        Element secretKey = pairing.getZr().newRandomElement();
        try {
            System.out.println("--------------------------");
            System.out.println("Fetching secret key...");
            URL url = new URL(ConfigManager.getConfigItem("PROTOCOL") + "://" + ConfigManager.getConfigItem("IP") + ":" +
                    ConfigManager.getConfigItem("PORT") + "/key");

            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            ObjectOutputStream out = new ObjectOutputStream(urlConn.getOutputStream());
            System.out.println("Sending crypted credentials...");
            CryptedCredentials cryptedCredentials = new CryptedCredentials(user.getCredentials());
            byte[] AESKey = cryptedCredentials.encrypt(this.clientApp.getPp(), pairing, secretKey);
            out.writeObject(cryptedCredentials);
            if (urlConn.getHeaderField("Content-length") != null) {
                int contentLength = Integer.parseInt(urlConn.getHeaderField("Content-length"));
                InputStream is = urlConn.getInputStream();
                byte[] cryptedSKBytes = new byte[contentLength];
                is.read(cryptedSKBytes);
                Element sk = pairing.getG1().newElementFromBytes(AESCrypto.decrypt(cryptedSKBytes, AESKey));
                this.clientApp.getUser().setsK(sk);
                return true;
            }
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getPublicParams() {
        try {
            //System.out.println("--------------------------");
            System.out.println("Fetching public params...");
            URL url = new URL(ConfigManager.getConfigItem("PROTOCOL") + "://" + ConfigManager.getConfigItem("IP") + ":" +
                    ConfigManager.getConfigItem("PORT") + "/publicparams");

            URLConnection urlConn = url.openConnection();
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);

            Pairing pairing = PairingFactory.getPairing("src\\utilities\\curves\\a.properties"); // chargement des param??tres de la courbe elliptique

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
