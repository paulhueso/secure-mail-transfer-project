package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Mailsendreceivetest;
import main.MainApp;
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
    private void initialize() throws GeneralSecurityException {
        ArrayList<Mail> mailList = Mailsendreceivetest.downloadEmails("test.tpcrypto@outlook.fr", "Azerty123");
        mails.setHgap(10);
        for(int i = 0; i < mailList.size(); i++){
            mails.add(new Label(mailList.get(i).getFrom()), 0, i);
            mails.add(new Label(mailList.get(i).getSubject() + " - " + mailList.get(i).getMessage()), 1, i);
            mails.add(new Label(mailList.get(i).getSentDate()), 2, i);
        };
        //TODO: Insert loading logo

    }

    @FXML
    private void writeEmail(MouseEvent event) {

    }

    @FXML
    private void refresh(ActionEvent event) throws GeneralSecurityException {
        ArrayList<Mail> mailList = Mailsendreceivetest.downloadEmails("test.tpcrypto@outlook.fr", "Azerty123");
        mails.getChildren().clear();
        for(int i = 0; i < mailList.size(); i++){
            mails.add(new Label(mailList.get(i).getFrom()), 0, i);
            mails.add(new Label(mailList.get(i).getSubject() + " - " + mailList.get(i).getMessage()), 1, i);
            mails.add(new Label(mailList.get(i).getSentDate()), 2, i);
        };
        //TODO: Insert loading logo
    }




}
