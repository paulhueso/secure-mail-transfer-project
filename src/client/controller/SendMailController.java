package client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import client.main.ClientApp;
import client.model.MailSendReceive;

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
    private TextArea mailContent;
    @FXML
    private GridPane attachmentsGrid;

    public SendMailController() {}

    @FXML
    private void initialize() {
        this.attachments = new ArrayList<>();
    }

    @FXML
    private void insertAttachment(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to upload");
        File file = fileChooser.showOpenDialog(clientApp.getPrimaryStage());
        attachments.add(file);

        int row = attachments.size();

        ImageView closeImg = new ImageView("File:src/images/closeBtn.png");
        closeImg.setFitWidth(25.0);
        closeImg.setFitHeight(22.0);
        closeImg.setCursor(Cursor.HAND);
        closeImg.setOnMouseClicked(e -> deleteFile(row-1));

        ListView<GridPane> attachmentsList = new ListView<>();
        GridPane grid = new GridPane();
        grid.add(new Text(file.getName()), 0, 0);
        grid.add(closeImg, 1, 0);
        attachmentsGrid.add(grid, row, 0);

    }

    @FXML
    private void showMails(ActionEvent event) {
        this.clientApp.showMailsOverview();
    }

    @FXML
    private void sendMail(ActionEvent event) {
        try {
            if(attachments.isEmpty()) MailSendReceive.sendmessage(this.clientApp.getUser(), this.toInput.getText(),
                    this.mailContent.getText(), this.subjectInput.getText());
            else MailSendReceive.sendmessagewithattachement(this.clientApp.getUser(), this.toInput.getText(),
                    this.mailContent.getText(), this.subjectInput.getText(), this.attachments, this.clientApp.getPp());
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
        attachmentsGrid.add(new Text("Attachments:"), 0, 0);

        for(int i = 0; i < attachments.size(); i++){
            GridPane grid = new GridPane();
            grid.add(new Text(attachments.get(i).getName()), 0, 0);
            grid.add(closeImg, 1, 0);
            attachmentsGrid.add(grid, i+1, 0);
            int finalI = i;
            closeImg.setOnMouseClicked(e -> deleteFile(finalI));
        }
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

}
