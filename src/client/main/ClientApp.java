package client.main;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import client.controller.GeneralViewController;
import client.controller.LoginController;
import client.controller.SendMailController;
import client.model.encryption.PublicParameters;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import client.model.User;

public class ClientApp extends Application {
    private static final String ENCRYPT_FILE_NAME = "encrypt";
    private static final String DECRYPT_FILE_NAME = "decrypt";

    private User user;
    private PublicParameters pp;

    private Stage primaryStage;
    private BorderPane rootLayout;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Secure mail transfer");

        initRootLayout();

        showConnexionLayout();
    }

    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("../../resources/fxml/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showConnexionLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("../../resources/fxml/LoginView.fxml"));

            LoginController controller = new LoginController();
            loader.setController(controller);
            controller.setClientApp(this);

            AnchorPane connexion = loader.load();

            rootLayout.setCenter(connexion);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMailsOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("../../resources/fxml/GeneralMailView.fxml"));

            GeneralViewController controller = new GeneralViewController();
            loader.setController(controller);
            controller.setClientApp(this);

            AnchorPane mailOverview = loader.load();

            rootLayout.setCenter(mailOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSendMailOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ClientApp.class.getResource("../../resources/fxml/NewMailView.fxml"));

            SendMailController controller = new SendMailController();
            loader.setController(controller);
            controller.setClientApp(this);

            AnchorPane mailOverview = loader.load();

            rootLayout.setCenter(mailOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public PublicParameters getPp() {
        return pp;
    }

    public void setPp(PublicParameters pp) {
        this.pp = pp;
    }

    /**
     * Returns the client.main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setupCache() {
        File encryptDir = new File(ENCRYPT_FILE_NAME);
        File decryptDir = new File(DECRYPT_FILE_NAME);
        if (!encryptDir.exists()) {
            encryptDir.mkdirs();
        }
        if (!decryptDir.exists()) {
            decryptDir.mkdirs();
        }
    }

    public static void cleanCache() {
        File encryptDir = new File(ENCRYPT_FILE_NAME);
        File decryptDir = new File(DECRYPT_FILE_NAME);
        if (ENCRYPT_FILE_NAME.length() < 4 || DECRYPT_FILE_NAME.length() < 4) { // Protection
            return;
        }
        for(File file: Objects.requireNonNull(encryptDir.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
        for(File file: Objects.requireNonNull(decryptDir.listFiles())) {
            if (!file.isDirectory()) {
                file.delete();
            }
        }
    }

    public static void main(String[] args) {
        setupCache();
        launch(args);
        cleanCache();
    }
}