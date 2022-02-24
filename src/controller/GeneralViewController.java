package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import main.MainApp;

import java.awt.event.MouseEvent;

public class GeneralViewController {
    @FXML
    private VBox mailsLayout;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button writeBtn;

    private MainApp mainApp;

    public GeneralViewController() {

    }

    public MainApp getMainApp() {
        return mainApp;
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void initialize() {
        //Initiatilize mailsLayout with all the currents mails
    }

    @FXML
    private void writeEmail(MouseEvent event) {

    }

    @FXML
    private void refresh(MouseEvent event) {

    }


}
