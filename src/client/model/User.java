package client.model;

import it.unisa.dia.gas.jpbc.Element;

public class User {
    private final Credentials credentials;
    private Element sK;

    public User(String username, String password) {
        this.credentials = new Credentials(username, password);
    }

    public Credentials getCredentials() {
        return this.credentials;
    }

    public Element getsK() {
        return sK;
    }

    public void setsK(Element sK) {
        this.sK = sK;
    }
}
