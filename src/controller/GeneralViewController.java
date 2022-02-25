package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import main.Mailsendreceivetest;
import main.ClientApp;
import model.Mail;

import java.security.GeneralSecurityException;
import java.util.ArrayList;


public class GeneralViewController {
    @FXML
    private GridPane mails;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button writeBtn;

    private ClientApp clientApp;

    public GeneralViewController() {

    }

    public ClientApp getClientApp() {
        return clientApp;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    @FXML
    private void initialize() throws GeneralSecurityException {
        loadMails(this.clientApp.getUser().getUsername(), this.clientApp.getUser().getPassword());
        //loadMails("test.tpcrypto@outlook.fr", "Azerty123");
    }

    @FXML
    private void writeEmail(MouseEvent event) {
        //TODO: write function
    }

    @FXML
    private void refresh(ActionEvent event) throws GeneralSecurityException {
        loadMails(this.clientApp.getUser().getUsername(), this.clientApp.getUser().getPassword());
    }

    private void loadMails(String username, String password) throws GeneralSecurityException {
        System.out.println("Fetching emails...");
        ArrayList<Mail> mailList = Mailsendreceivetest.downloadEmails(username, password);
        mails.getChildren().clear();
        mailList.forEach(mail -> {
            addPane(0, mail.getId(), mail.getFrom());
            addPane(1, mail.getId(), mail.getSubject());
            addPane(2, mail.getId(), mail.getSentDate());
        });
        System.out.println("Done !");
        //TODO: Insert loading logo
        //TODO: Broken function
    }

    private void addPane(int column, int row, String label){
        Pane pane = new Pane(new Label(label));
        pane.setMinHeight(20);
        pane.setOnMouseClicked(e -> {
            System.out.println(row); //TODO: change screen to mail screen
        });
        mails.add(pane, column, row);
    }

}
