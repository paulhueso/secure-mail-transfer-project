package client.model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author imino
 */
import client.model.encryption.IBEBasicIdent;
import client.model.encryption.IBECipher;
import client.model.encryption.PublicParameters;
import com.sun.mail.util.MailSSLSocketFactory;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Properties;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
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

    public static void sendmessagewithattachement(User sender, String destination, String content, String subject,
                                                  ArrayList<File> attachments, PublicParameters publicParam) throws GeneralSecurityException {
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
            MimeMessage message = new MimeMessage(session);
            message.setFrom(sender.getUsername());
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destination));
            message.setSubject(subject);

            Multipart myemailcontent = new MimeMultipart();
            MimeBodyPart bodypart = new MimeBodyPart();
            bodypart.setText(content);

            MimeBodyPart attachmentfiles = new MimeBodyPart();
            attachments.forEach(file -> {
                File encryptedFile = encryptFile(file, publicParam, sender);
                try {
                    attachmentfiles.attachFile(encryptedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

            });

            myemailcontent.addBodyPart(bodypart);
            myemailcontent.addBodyPart(attachmentfiles);
            message.setContent(myemailcontent);
            Transport.send(message);

            System.out.println("Mail sent!");

        } catch (NoSuchProviderException e) {e.printStackTrace();}
        catch (MessagingException e) {e.printStackTrace();}

    }



    public static ObservableList<Mail> downloadEmails(User user, PublicParameters publicParameters) throws GeneralSecurityException {
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

            store.connect(user.getUsername(), user.getPassword());

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

                            decryptFile(part, publicParameters, user);


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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return mails;
    }

    private static File encryptFile(File file, PublicParameters publicParam, User sender) {
        File encryptedFile = new File("encryptedFile.txt");

        try {
            //file to byte[]
            FileInputStream fis = new FileInputStream(file);
            byte[] filebytes = new byte[fis.available()];
            fis.read(filebytes);
            fis.close();

            System.out.println(String.valueOf(filebytes));
            Pairing pairing = PairingFactory.getPairing("src\\utilities\\curves\\a.properties"); // chargement des paramètres de la courbe elliptique
            IBECipher ibecipher = IBEBasicIdent.IBEencryption(pairing, pairing.getG1().newElementFromBytes(publicParam.getP()), pairing.getG1().newElementFromBytes(publicParam.getP_pub()), filebytes, sender.getUsername()); // chiffrement BasicID-IBE/AES
            //Serialized
            FileOutputStream fos = new FileOutputStream(encryptedFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ibecipher);
            oos.close();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return encryptedFile;
    }

    private static void decryptFile(MimeBodyPart part, PublicParameters publicParam, User user) throws MessagingException, IOException, ClassNotFoundException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        //Save EncryptedFile
        String filename = part.getFileName();
        part.saveFile(filename+"_serialized");

        //Deserialize IBECipher
        FileInputStream fis = new FileInputStream(filename+"_serialized");
        ObjectInputStream ois = new ObjectInputStream(fis);
        IBECipher cipher = (IBECipher) ois.readObject();
        ois.close();
        fis.close();

        Pairing pairing = PairingFactory.getPairing("src\\utilities\\curves\\a.properties"); // chargement des paramètres de la courbe elliptique

        //Decrypt
        byte[] resulting_bytes = IBEBasicIdent.IBEdecryption(pairing, pairing.getG1().newElementFromBytes(publicParam.getP()), pairing.getG1().newElementFromBytes(publicParam.getP_pub()), user.getsK(), cipher); //déchiffrment Basic-ID IBE/AES
        /*for (byte b : resulting_bytes) {
            System.out.print(b+",");
        }
        System.out.println(); */
        File f = new File("decryptionresult.txt"); // création d'un fichier pour l'enregistrement du résultat du déchiffrement
        f.createNewFile();
        FileOutputStream fout = new FileOutputStream(f);
        fout.write(resulting_bytes); // ecriture du résultat de déchiffrement dans le fichier
        fout.close();
    }
}