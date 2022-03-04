package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import main.ClientApp;
import main.Mailsendreceivetest;
import model.Mail;

import java.io.File;
import java.security.GeneralSecurityException;

public class SendMailController {
    private ClientApp clientApp;

    private Mail mailToSend;

    @FXML
    private Button attachmentBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button sendBtn;
    @FXML
    private TextField toInput;
    @FXML
    private TextField subjectInput;
    @FXML
    private HTMLEditor mailContent;

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

    @FXML
    private void sendMail(ActionEvent event) {
        try {
            Mailsendreceivetest.sendmessage(this.clientApp.getUser(), this.toInput.getText(), this.mailContent.getHtmlText(), this.subjectInput.getText());
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

}
