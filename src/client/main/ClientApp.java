package client.main;

import java.io.IOException;

import client.controller.GeneralViewController;
import client.controller.LoginController;
import client.controller.SendMailController;
import it.unisa.dia.gas.jpbc.Element;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import client.model.User;

public class ClientApp extends Application {
    private User user;
    private Element p_pub;
    private Element p;

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

    public Element getP_pub() {
        return p_pub;
    }

    public void setP_pub(Element p_pub) {
        this.p_pub = p_pub;
    }

    public Element getP() {
        return p;
    }

    public void setP(Element p) {
        this.p = p;
    }

    /**
     * Returns the client.main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }



    public static void main(String[] args) {
        launch(args);
    }
}