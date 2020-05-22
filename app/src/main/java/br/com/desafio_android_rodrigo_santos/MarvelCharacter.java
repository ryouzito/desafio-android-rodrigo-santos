package br.com.desafio_android_rodrigo_santos;

class MarvelCharacter {

    private int id;
    private String sNome;
    private String sDescricao;
    private String sImagePath;

    MarvelCharacter (int id, String sNome, String sDescricao, String sImagePath) {
        super();
        this.id = id;
        this.sNome = sNome;
        this.sDescricao = sDescricao;
        this.sImagePath = sImagePath;
    }

    public int getId() {
        return id;
    }

    String getsNome() {
        return sNome;
    }

    public void setsNome(String sNome) {
        this.sNome = sNome;
    }

    public String getsDescricao() {
        return sDescricao;
    }

    public String getsImagePath() {
        return sImagePath;
    }
}
