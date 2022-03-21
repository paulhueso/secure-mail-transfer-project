/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model.encryption;

import java.io.Serializable;

/**
 *
 * @author imino
 */
public class IBECipher implements Serializable {

    private static final long SerialVersionUID = 104564987;

    private byte[] U; // rP (vu dans le cours)

    byte[] V; // K xor e(Q_id,P_pub) avec K la clef symmetrique AES

    byte[] Aescipher; // résultat du chiffrement avec AES

    public IBECipher(byte[] U, byte[] V, byte[] Aescipher) {
        this.U = U;
        this.V = V;
        this.Aescipher = Aescipher;
    }

    public byte[] getAescipher() {
        return Aescipher;
    }

    public byte[] getU() {
        return U;
    }

    public byte[] getV() {
        return V;
    }


}
