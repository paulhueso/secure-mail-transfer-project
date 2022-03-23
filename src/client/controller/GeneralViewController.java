package client.controller;

import client.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import client.model.MailSendReceive;
import client.main.ClientApp;
import client.model.Mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;


public class GeneralViewController {

    @FXML
    private Button refreshBtn;
    @FXML
    private Button writeBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Label idLabel;
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
    @FXML
    private GridPane attachmentsGrid;

    private ClientApp clientApp;
    
    private SendMailController sendmailcontroller;

    public GeneralViewController() {

    }

    @FXML
    private void initialize() throws GeneralSecurityException {
        from.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFrom()));
        subject.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject()));
        receivedDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSentDate()));
        mailTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> displayEmail(newValue));
        loadMails(this.clientApp.getUser());
    }

    @FXML
    private void writeEmail(ActionEvent event) {
        clientApp.showSendMailOverview();
    }
    
    @FXML
    private void DownloadEmailInChosenFolder(ActionEvent event) throws IOException, GeneralSecurityException{
    
    	
    	DirectoryChooser directoryChooser = new DirectoryChooser();
    	directoryChooser.setTitle("Select a directory to save the file(s)");
        File selectedDirectory = directoryChooser.showDialog(clientApp.getPrimaryStage());
        if (selectedDirectory != null) {
            System.out.println(selectedDirectory);

            int id = Integer.parseInt(idLabel.getText());

            ObservableList<Mail> mailList = MailSendReceive.downloadEmails(this.clientApp.getUser(), this.clientApp.getPp());
            for (Mail mail: mailList) {
                if (id == mail.getId()) {
                    String attachments = mail.getAttachments();

                    String[] tabattach = attachments.split(", ");
                    for (String attachment: tabattach) {
                        String filename = attachment;

                        File file = new File(selectedDirectory.toString()+"/"+filename);
                        File file0 = new File("decrypt/"+filename);
                        FileInputStream inputStream = new FileInputStream(file0);
                        FileOutputStream out = new FileOutputStream(file);
                        byte[] fileBytes = new byte[inputStream.available()];
                        try {
                            inputStream.read(fileBytes);
                            out.write(fileBytes);
//                            int i;
//
//                            while ((i = inputStream.read()) != -1) {
//                                out.write(i);
//                            }
                        }catch(Exception e) {
                            System.out.println("Error Found: "+e.getMessage());
                        }
                        finally {
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (out != null) {
                                out.close();
                            }
                        }
                        System.out.println("File Copied");
                        out.close();
                    }
                }
            }
        }
    }

    @FXML
    private void refresh(ActionEvent event) throws GeneralSecurityException {
        loadMails(this.clientApp.getUser());
    }

    private void loadMails(User user) throws GeneralSecurityException {
        System.out.println("Fetching emails...");
        ObservableList<Mail> mailList = MailSendReceive.downloadEmails(user, this.clientApp.getPp());
        
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
        idLabel.setText(String.valueOf(selectedMail.getId()));
        System.out.println(selectedMail.getAttachments());
        String attachmentsStr = selectedMail.getAttachments();
        int nbAttachmentsMail = 0;
        if (attachmentsStr.length() > 0) {
            nbAttachmentsMail = 1;
            for (int i = 0; i < attachmentsStr.length(); i++) {
                if (attachmentsStr.charAt(i) == ',') {
                    nbAttachmentsMail++;
                }
            }
        }
        nbAttachments.setText(String.valueOf(nbAttachmentsMail));
        downloadBtn.setVisible(nbAttachmentsMail > 0);
        WebEngine engine = mailContent.getEngine();
        engine.loadContent(selectedMail.getMessage());

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
