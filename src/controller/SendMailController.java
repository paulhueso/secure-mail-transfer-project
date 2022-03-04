package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import main.ClientApp;
import model.Mail;

import java.io.File;

public class SendMailController {
    private ClientApp clientApp;

    private Mail mailToSend;

    @FXML
    private Button attachmentBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button sendBtn;

    public SendMailController() {}

    @FXML
    private void initialize() {}

    @FXML
    private void insertAttachment(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to upload");
        File file = fileChooser.showOpenDialog(clientApp.getPrimaryStage());
    }

    @FXML
    private void showMails(ActionEvent event) {
        this.clientApp.showMailsOverview();
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

}
