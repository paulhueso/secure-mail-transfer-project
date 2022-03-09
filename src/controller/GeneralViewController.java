package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import model.Mailsendreceivetest;
import main.ClientApp;
import model.Mail;

import java.io.File;
import java.security.GeneralSecurityException;
import java.util.ArrayList;


public class GeneralViewController {

    @FXML
    private Button refreshBtn;
    @FXML
    private Button writeBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Label dateLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label fromLabel;
    @FXML
    private VBox vBoxDetailledMail;
    @FXML
    private Button downloadBtn;
    @FXML
    private Label nbAttachments;

    @FXML
    private TableView<Mail> mailTable;
    @FXML
    private TableColumn<Mail, String> from;
    @FXML
    private TableColumn<Mail, String> subject;
    @FXML
    private TableColumn<Mail, String> receivedDate;
    @FXML
    private WebView mailContent;

    private ClientApp clientApp;

    public GeneralViewController() {

    }

    @FXML
    private void initialize() throws GeneralSecurityException {
        from.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFrom()));
        subject.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject()));
        receivedDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSentDate()));
        mailTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> displayEmail(newValue));
        loadMails(this.clientApp.getUser().getUsername(), this.clientApp.getUser().getPassword());

        //loadMails("test.tpcrypto@outlook.fr", "Azerty123");
    }

    @FXML
    private void writeEmail(ActionEvent event) {
        clientApp.showSendMailOverview();
    }

    @FXML
    private void refresh(ActionEvent event) throws GeneralSecurityException {
        loadMails(this.clientApp.getUser().getUsername(), this.clientApp.getUser().getPassword());
        //loadMails("test.tpcrypto@outlook.fr", "Azerty123");
    }

    private void loadMails(String username, String password) throws GeneralSecurityException {
        System.out.println("Fetching emails...");
        ObservableList<Mail> mailList = Mailsendreceivetest.downloadEmails(username, password);
        mailTable.setItems(mailList);
        System.out.println("Done !");
    }

    private void displayEmail(Mail selectedMail) {
        mailTable.setVisible(false);
        refreshBtn.setVisible(false);
        backBtn.setVisible(true);
        vBoxDetailledMail.setVisible(true);

        dateLabel.setText(selectedMail.getSentDate());
        fromLabel.setText(selectedMail.getFrom());
        subjectLabel.setText(selectedMail.getSubject());

        WebEngine engine = mailContent.getEngine();
        engine.loadContent(selectedMail.getMessage());

        ArrayList<File> attachments = selectedMail.getAttachments();

        if(attachments == null || attachments.isEmpty()){
            nbAttachments.setText("No");
        } else {
            downloadBtn.setVisible(true);
            nbAttachments.setText(String.valueOf(attachments.size()));
        }
    }

    @FXML
    private void displayEmailOverview(){
        mailTable.setVisible(true);
        refreshBtn.setVisible(true);
        backBtn.setVisible(false);
        vBoxDetailledMail.setVisible(false);
    }

    public ClientApp getClientApp() {
        return clientApp;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

}
