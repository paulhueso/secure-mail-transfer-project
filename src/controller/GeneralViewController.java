package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
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
    private TableView<Mail> mailTable;
    @FXML
    private TableColumn<Mail, String> from;
    @FXML
    private TableColumn<Mail, String> subject;
    @FXML
    private TableColumn<Mail, String> receivedDate;

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

    }

    public ClientApp getClientApp() {
        return clientApp;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

}
