package client.model.encryption;

import client.model.Credentials;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class CryptedCredentials implements Serializable {
    private final Credentials credentials;
    private byte[] publicKey;

    public CryptedCredentials(Credentials credentials) {
        this.credentials = new Credentials(credentials.getUsername(), credentials.getPassword());
    }

    public Credentials getCredentials() {
        return this.credentials;
    }

    public byte[] encrypt(PublicParameters publicParameters, Pairing pairing, Element secretKey) {
        this.publicKey = pairing.getG1().newElementFromBytes(publicParameters.getP()).duplicate().mulZn(secretKey).toBytes();
        byte[] AESKey = pairing.getG1().newElementFromBytes(publicParameters.getP_pub()).mulZn(secretKey).toBytes();
        try {
            this.credentials.setUsername(new String(AESCrypto.encrypt(credentials.getUsername().getBytes(), AESKey)));
            this.credentials.setPassword(new String(AESCrypto.encrypt(credentials.getPassword().getBytes(), AESKey)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return AESKey;
    }

    public byte[] decrypt(Pairing pairing, Element msk) {
       byte[] AESKey = pairing.getG1().newElementFromBytes(this.publicKey).duplicate().mulZn(msk).toBytes();
        try {
            this.credentials.setUsername(new String(AESCrypto.decrypt(credentials.getUsername().getBytes(), AESKey)));
            this.credentials.setPassword(new String(AESCrypto.decrypt(credentials.getPassword().getBytes(), AESKey)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return AESKey;
    }
}
