package client.model;

import it.unisa.dia.gas.jpbc.Element;

public class User {
    private String username;
    private String password;
    private Element sK;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Element getsK() {
        return sK;
    }

    public void setsK(Element sK) {
        this.sK = sK;
    }
}
