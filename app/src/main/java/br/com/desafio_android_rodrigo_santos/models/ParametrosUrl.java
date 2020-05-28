package br.com.desafio_android_rodrigo_santos.models;

public class ParametrosUrl {

    private String timestamp;
    private String chavePublica;
    private String hash;

    public ParametrosUrl(String timestamp, String chavePublica, String hash) {
        this.timestamp = timestamp;
        this.chavePublica = chavePublica;
        this.hash = hash;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getChavePublica() {
        return chavePublica;
    }

    public String getHash() {
        return hash;
    }
}
