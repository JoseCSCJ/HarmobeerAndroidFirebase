package br.com.opet.tds.harmobeerAndroid.model;



/**
 * Created by José Carlos on 18/11/2018.
 */

public class Prato {


    private String id;
    private String nome;
    private String usuarioId;

    public Prato() {
    }
    public Prato(String id, String nome ) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }
    public void setId(String Id) {
        this.id = Id;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
