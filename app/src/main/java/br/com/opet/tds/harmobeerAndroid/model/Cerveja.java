package br.com.opet.tds.harmobeerAndroid.model;



/**
 * Created by José Carlos on 18/11/2018.
 */


public class Cerveja {


    private String ID;
    private String nome;
    private String estilo;
    private double teor_alc;
    private String usuarioId;

    public Cerveja(){}
    public Cerveja(String ID, String nome, String estilo, double teor_alc) {
        this.ID = ID;
        this.nome = nome;
        this.estilo = estilo;
        this.teor_alc = teor_alc;
    }

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEstilo() {
        return estilo;
    }
    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public double getTeor_alc() {
        return teor_alc;
    }
    public void setTeor_alc(double teor_alc) {
        this.teor_alc = teor_alc;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }
}
