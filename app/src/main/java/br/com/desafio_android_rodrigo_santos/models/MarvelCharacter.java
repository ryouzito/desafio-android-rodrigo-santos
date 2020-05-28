package br.com.desafio_android_rodrigo_santos.models;

public class MarvelCharacter {

    private int id;
    private String sNome;
    private String sDescricao;
    private String sImagePath;

    public MarvelCharacter(int id, String sNome, String sDescricao, String sImagePath) {
        super();
        this.id = id;
        this.sNome = sNome;
        this.sDescricao = sDescricao;
        this.sImagePath = sImagePath;
    }

    public int getId() {
        return id;
    }

    public String getsNome() {
        return sNome;
    }

    public String getsDescricao() {
        return sDescricao;
    }

    public String getsImagePath() {
        return sImagePath;
    }
}
