package main;

import java.io.IOException;

import controller.GeneralViewController;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

public class ClientApp extends Application {
    private User user;

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
            loader.setLocation(ClientApp.class.getResource("../view/RootLayout.fxml"));
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
            loader.setLocation(ClientApp.class.getResource("../view/LoginView.fxml"));

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
            loader.setLocation(ClientApp.class.getResource("../view/GeneralMailView.fxml"));

            GeneralViewController controller = new GeneralViewController();
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

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}