package br.com.opet.tds.harmobeerAndroid.model;



import java.io.Serializable;


/**
 * Created by José Carlos on 18/11/2018.
 */

public class Usuario implements Serializable {


    private String id;
    private String username;
    private String email;
    private String senha;


    public Usuario() {
    }
    public Usuario(String id, String username, String email, String senha) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.senha = senha;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
}
