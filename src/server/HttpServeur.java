/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import model.encryption.IBEBasicIdent;
import model.encryption.KeyPair;
import model.encryption.SettingParameters;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author imino
 */
public class HttpServeur {


    
    
    
    public static void main(String[] args) {
     
        try {
             // InetSocketAddress s = new InetSocketAddress("localhost", 8080);
            InetSocketAddress s = new InetSocketAddress(InetAddress.getLocalHost(), 8080);
            //  InetSocketAddress s = new InetSocketAddress("localhost", 8080);
              
              
            HttpServer server = HttpServer.create(s, 1000);
            System.out.println(server.getAddress());
            server.createContext("/key", new HttpHandler()
            {
                public void handle(HttpExchange he) throws IOException {
                    //Setup
                    System.out.println("Setup ....");
                    Pairing pairing = PairingFactory.getPairing("src\\curves\\a.properties"); // chargement des paramètres de la courbe elliptique
                    SettingParameters sp = IBEBasicIdent.setup(pairing); // génération des paramètres du système (ie: generateur, clef publique du système et clef du maitre)
                    System.out.println("Paremètre du système :");
                    System.out.println("generator:" + sp.getP());
                    System.out.println("P_pub:" + sp.getP_pub());
                    System.out.println("MSK:" + sp.getMsk());

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

                    byte[] keysBytes = keys.getSk().toBytes();

                    he.sendResponseHeaders(200, keysBytes.length);
                    OutputStream os = he.getResponseBody();

                    os.write(keysBytes);
                    System.out.println("sending response done....");
                    os.close();
                }
            });

            server.start();
        } catch (IOException ex) {
            Logger.getLogger(HttpServeur.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    
}
