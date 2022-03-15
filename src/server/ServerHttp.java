/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import client.model.encryption.IBEBasicIdent;
import client.model.encryption.KeyPair;
import client.model.encryption.SettingParameters;
import utilities.config.ConfigManager;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
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

                    //Receive id
                    byte[] bytes1 = new byte[Integer.parseInt(he.getRequestHeaders().getFirst("Content-length"))];
                    he.getRequestBody().read(bytes1);
                    String id = new String(bytes1);
                    System.out.println("user : " + id);

                    //Generate key
                    System.out.println("Key generation .....");
                    KeyPair keys = null; // genération d'une paire de clefs correspondante à id
                    try {
                        keys = IBEBasicIdent.keygen(pairing, sp.getMsk(), id);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    System.out.println("PK:" + keys.getPk());
                    System.out.println("SK:" + keys.getSk());

                    byte[] keyBytes = keys.getSk().toBytes();

                    he.sendResponseHeaders(200, keyBytes.length);
                    OutputStream os = he.getResponseBody();

                    os.write(keyBytes);
                    System.out.println("sending response done....");
                    os.close();
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
    
}
