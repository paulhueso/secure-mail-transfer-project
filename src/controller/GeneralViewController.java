package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import main.Mailsendreceivetest;
import main.ClientApp;
import model.Mail;

import java.security.GeneralSecurityException;


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
        //loadMails(this.clientApp.getUser().getUsername(), this.clientApp.getUser().getPassword());
        from.setCellValueFactory(cellData -> cellData.getValue().getFrom());
        subject.setCellValueFactory(cellData -> cellData.getValue().getSubject());
        receivedDate.setCellValueFactory(cellData -> cellData.getValue().getSentDate());
        mailTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> displayEmail(newValue));
        loadMails("test.tpcrypto@outlook.fr", "Azerty123");
    }

    @FXML
    private void writeEmail(MouseEvent event) {
        //TODO: write function
    }

    @FXML
    private void refresh(ActionEvent event) throws GeneralSecurityException {
        //loadMails(this.clientApp.getUser().getUsername(), this.clientApp.getUser().getPassword());
        loadMails("test.tpcrypto@outlook.fr", "Azerty123");
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

        dateLabel.setText(selectedMail.getSentDate().get());
        fromLabel.setText(selectedMail.getFrom().get());
        subjectLabel.setText(selectedMail.getSubject().get());

        WebEngine engine = mailContent.getEngine();
        engine.loadContent(selectedMail.getMessage().get());
        System.out.println(selectedMail.getMessage().get());
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
