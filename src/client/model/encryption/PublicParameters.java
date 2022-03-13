package client.model.encryption;

import java.io.Serializable;

public class PublicParameters implements Serializable {

    private byte[] p; //generateur

    private byte[] p_pub; // clef publique du système

    public PublicParameters(byte[] p, byte[] p_pub) {
        this.p = p;
        this.p_pub = p_pub;
    }

    public byte[] getP() {
        return p;
    }

    public byte[] getP_pub() {
        return p_pub;
    }

}
