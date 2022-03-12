package client.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author imino
 */
import com.sun.mail.util.MailSSLSocketFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import java.io.File;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Mailsendreceivetest{

    public static void sendmessage(User sender, String destination, String content, String subject) throws GeneralSecurityException {
        System.out.println("Sending email...");
        Properties properties = new Properties();

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.outlook.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender.getUsername(), sender.getPassword());
            }
        });
        System.out.println("session.getProviders():"+session.getProviders()[0].getType());
        try{
            MimeMessage message=new MimeMessage(session);
            message.setFrom(sender.getUsername());
            message.setText(content);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
            message.setSubject(subject);
            Transport.send(message);
            System.out.println("Mail sent !");

        } catch (NoSuchProviderException e) {e.printStackTrace();}
        catch (MessagingException e) {e.printStackTrace();}

    }

    public static void sendmessagewithattachement(User sender, String destination, String content, String subject, ArrayList<File> attachments) throws GeneralSecurityException {
        Properties properties = new Properties();

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.smtp.ssl.trust", "*");
        properties.put("mail.smtp.ssl.socketFactory", sf);

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.outlook.com");
        properties.put("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender.getUsername(),sender.getPassword());
            }
        });
        System.out.println("session.getProviders():"+session.getProviders()[0].getType());
        try{
            MimeMessage message=new MimeMessage(session);
            message.setFrom(sender.getUsername());
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
            message.setSubject(subject);

            Multipart myemailcontent=new MimeMultipart();
            MimeBodyPart bodypart=new MimeBodyPart();
            bodypart.setText(content);



            MimeBodyPart attachementfiles=new MimeBodyPart();
            attachments.forEach(file -> {
                try {
                    attachementfiles.attachFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });

            myemailcontent.addBodyPart(bodypart);
            myemailcontent.addBodyPart(attachementfiles);
            message.setContent(myemailcontent);
            Transport.send(message);

            System.out.println("Mail sent!");

        } catch (NoSuchProviderException e) {e.printStackTrace();}
        catch (MessagingException e) {e.printStackTrace();}

    }



    public static ObservableList<Mail> downloadEmails(String userName, String password) throws GeneralSecurityException {
        ObservableList<Mail> mails = FXCollections.observableArrayList();
        Properties properties = new Properties();

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        properties.put("mail.imap.ssl.trust", "*");
        properties.put("mail.imap.ssl.socketFactory", sf);

        // server setting (it can be pop3 too
        properties.put("mail.imap.host", "outlook.office365.com");
        properties.put("mail.imap.port", "993");
        properties.setProperty("mail.imap.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.imap.socketFactory.fallback","false");
        properties.setProperty("mail.imap.socketFactory.port", "993");

        Session session = Session.getDefaultInstance(properties);

        try {

            // connects to the message store imap or pop3
            //     Store store = session.getStore("pop3");
            Store store = session.getStore("imap");

            store.connect(userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
            // fetches new messages from server
            Message[] arrayMessages = folderInbox.getMessages();

            for (int i = 0; i < arrayMessages.length; i++) {
                Message message = arrayMessages[i];
                Address[] fromAddress = message.getFrom();
                String from = fromAddress[0].toString();
                String subject = message.getSubject();
                String sentDate = message.getSentDate().toString();
                String contentType = message.getContentType();
                String messageContent = "";
                boolean message_seen = message.getFlags().contains(Flags.Flag.SEEN);
                // store attachment file name, separated by comma
                String attachFiles = "";

                if (contentType.contains("multipart")) {
                    // content may contain attachments
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";
                            part.saveFile(fileName); // le dossier Myfiles à créer dans votre projet

                        } else {
                            // this part may be the message content
                            messageContent = part.getContent().toString();
                        }
                    }

                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                    }
                } else if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    Object content = message.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }

                Mail mail = new Mail(i+1, message_seen, from, subject, sentDate, messageContent);
                mails.add(mail);

            }

            // disconnect
            folderInbox.close(false);
            store.close();

        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for imap.");
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return mails;
    }

}