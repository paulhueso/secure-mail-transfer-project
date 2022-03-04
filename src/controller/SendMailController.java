package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import main.ClientApp;
import main.Mailsendreceivetest;
import model.Mail;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class SendMailController {
    private ClientApp clientApp;

    private ArrayList<File> attachments;

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
    @FXML
    private GridPane attachmentsGrid;

    public SendMailController() {}

    @FXML
    private void initialize() {
        this.attachments = new ArrayList<>();
    }

    @FXML
    private void insertAttachment(ActionEvent event){
        int row = attachments.size();
        ImageView closeImg = new ImageView("File:src/images/closeBtn.png");
        closeImg.setFitWidth(25.0);
        closeImg.setFitHeight(22.0);
        closeImg.setCursor(Cursor.HAND);
        closeImg.setOnMouseClicked(e -> deleteFile(row));
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to upload");
        File file = fileChooser.showOpenDialog(clientApp.getPrimaryStage());
        attachmentsGrid.add(new Text(file.getName()), 0, row);
        attachmentsGrid.add(closeImg, 1, row);
        attachments.add(file);
    }

    @FXML
    private void showMails(ActionEvent event) {
        this.clientApp.showMailsOverview();
    }

    @FXML
    private void sendMail(ActionEvent event) {
        try {
            if(attachments.isEmpty()) Mailsendreceivetest.sendmessage(this.clientApp.getUser(), this.toInput.getText(),
                    this.mailContent.getHtmlText(), this.subjectInput.getText());
            else Mailsendreceivetest.sendmessagewithattachement(this.clientApp.getUser(), this.toInput.getText(),
                    this.mailContent.getHtmlText(), this.subjectInput.getText(), this.attachments);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private void deleteFile(int id) {
        ImageView closeImg = new ImageView("File:src/images/closeBtn.png");
        closeImg.setFitWidth(25.0);
        closeImg.setFitHeight(22.0);
        closeImg.setCursor(Cursor.HAND);

        attachments.remove(id);
        attachmentsGrid.getChildren().clear();

        for(int i = 0; i < attachments.size(); i++){
            attachmentsGrid.add(new Text(attachments.get(i).getName()), 0, i);
            int finalI = i;
            closeImg.setOnMouseClicked(e -> deleteFile(finalI));
            attachmentsGrid.add(closeImg, 1, i);
        }
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

}
