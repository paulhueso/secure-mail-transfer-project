/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.model.Credentials;
import client.model.encryption.*;
import com.sun.mail.util.MailSSLSocketFactory;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import utilities.config.ConfigManager;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.*;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author imino
 */
class ServerHttp {
    
    
    
    public static void main(String[] args) {
     
        try {
            InetSocketAddress s = new InetSocketAddress(InetAddress.getLocalHost(), 8080);

            //Setup
            System.out.println("Setup ....");
            Pairing pairing = PairingFactory.getPairing("src\\utilities\\curves\\a.properties"); // chargement des paramètres de la courbe elliptique
            SettingParameters sp = IBEBasicIdent.setup(pairing); // génération des paramètres du système (ie: generateur, clef publique du système et clef du maitre)
            System.out.println("Paremètre du système :");
            System.out.println("generator:" + pairing.getG1().newElementFromBytes(sp.getPublicParams().getP()));
            System.out.println("P_pub:" + pairing.getG1().newElementFromBytes(sp.getPublicParams().getP_pub()));
            System.out.println("MSK:" + sp.getMsk());
            HttpServer server = HttpServer.create(s, 1000);
            System.out.println(server.getAddress());
            String ip = server.getAddress().toString();
            ip = ip.substring(1, ip.length() - 5); // This trims first "/" and port
            if (!ip.equals(ConfigManager.getConfigItem("IP"))) { // Set the server IP in the config
                ConfigManager.putConfigItem("IP", ip);
            }
            server.createContext("/key", new HttpHandler()
            {
                public void handle(HttpExchange he) throws IOException {
                    System.out.println("--------------------------");

                    ObjectInputStream ois = new ObjectInputStream(he.getRequestBody());
                    try {
                        CryptedCredentials cryptedCredentials = (CryptedCredentials) ois.readObject();
                        byte[] AESKey = cryptedCredentials.decrypt(pairing, sp.getMsk());
                        Credentials userCredentials = cryptedCredentials.getCredentials();
                        System.out.println("credentials: " + cryptedCredentials.getCredentials());
                        boolean authentificationOK = checkLoginPassword(userCredentials);
                        if (authentificationOK) {
                            System.out.println("Access authorized");
                            //Generate key
                            System.out.println("Key generation .....");
                            KeyPair keys = null; // genération d'une paire de clefs correspondante à id
                            keys = IBEBasicIdent.keygen(pairing, sp.getMsk(), userCredentials.getUsername());

                            System.out.println("PK:" + keys.getPk());
                            System.out.println("SK:" + keys.getSk());

                            byte[] cryptedKeyBytes = AESCrypto.encrypt(keys.getSk().toBytes(), AESKey);

                            he.sendResponseHeaders(200, cryptedKeyBytes.length);
                            OutputStream os = he.getResponseBody();

                            os.write(cryptedKeyBytes);
                            System.out.println("sending response done....");
                            os.close();
                        }
                        else {
                            System.out.println("Wrong username or password, access forbidden");
                            he.sendResponseHeaders(403, 0);
                        }
                    }
                    catch (ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
                        e.printStackTrace();
                    }
                }
            });

            server.createContext("/publicparams", new HttpHandler()
            {
                public void handle(HttpExchange he) throws IOException {
                    System.out.println("............................");

                    File serialized = new File("serialized.txt");

                    //Serialization
                    FileOutputStream fos = new FileOutputStream(serialized);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(sp.getPublicParams());
                    oos.close();
                    fos.close();

                    //Binary read to send through response
                    FileInputStream fis = new FileInputStream(serialized);
                    byte[] filebytes = new byte[fis.available()];
                    fis.read(filebytes);
                    fis.close();


                    //Response
                    he.sendResponseHeaders(200, filebytes.length);
                    OutputStream os = he.getResponseBody();
                    os.write(filebytes);
                    System.out.println("sending response done....");
                    os.close();

                    serialized.delete();

                }
            });

            server.start();
        } catch (IOException ex) {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    private static boolean checkLoginPassword(Credentials credentials) {
        try {
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

            Store store = session.getStore("imap");
            store.connect(credentials.getUsername(), credentials.getPassword());
            }
        catch (AuthenticationFailedException e) {
            return false;
        }
        catch (GeneralSecurityException | MessagingException e) {
            e.printStackTrace();
        }
        return true;
    }
}
