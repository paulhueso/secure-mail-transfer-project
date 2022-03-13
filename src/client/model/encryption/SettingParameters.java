/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.model.encryption;

import it.unisa.dia.gas.jpbc.Element;

/**
 *
 * @author imino
 */
public class SettingParameters {

    private PublicParameters publicParams;

    private Element msk; // clef du maitre

    public SettingParameters(PublicParameters pp, Element msk) {
        this.publicParams = pp;
        this.msk=msk;
    }

    public PublicParameters getPublicParams() {
        return publicParams;
    }

    public Element getMsk() {
        return msk;
    }


}
