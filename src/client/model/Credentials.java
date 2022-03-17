package client.model;

import it.unisa.dia.gas.jpbc.Element;

import java.io.Serializable;

public class Credentials implements Serializable {
    private String username;
    private String password;

    public Credentials(String username, String password) {
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

    public String toString() {
        return "login: " + this.username + "\tpassword: " + this.password;
    }
}
