package br.com.desafio_android_rodrigo_santos.model;

public class CharacterComic {

    private int id;
    private String nomeHQ;
    private String descricaoHQ;
    private Double precoHQ;
    private String pathImageHQ;

    public CharacterComic(int id, String nomeHQ, String descricaoHQ, Double precoHQ, String pathImageHQ) {
        super();
        this.id = id;
        this.nomeHQ = nomeHQ;
        this.descricaoHQ = descricaoHQ;
        this.precoHQ = precoHQ;
        this.pathImageHQ = pathImageHQ;
    }

    public int getId() {
        return id;
    }

    public String getNomeHQ() {
        return nomeHQ;
    }

    public String getDescricaoHQ() {
        return descricaoHQ;
    }

    public Double getPrecoHQ() {
        return precoHQ;
    }

    public String getPathImageHQ() {
        return pathImageHQ;
    }
}
