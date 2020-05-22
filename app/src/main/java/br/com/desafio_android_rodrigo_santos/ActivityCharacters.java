package br.com.desafio_android_rodrigo_santos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;

import java.util.ArrayList;

public class ActivityCharacters extends AppCompatActivity {

    Context context;
    GridView gvListaPersonagens;
    ArrayList<MarvelCharacter> alMarvelCharacters = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        context = this;
        exibePersonagens();
    }

    public void exibePersonagens() {
        gvListaPersonagens = findViewById(R.id.gridViewListaPersonagens);

        alMarvelCharacters.add(new MarvelCharacter(001, "Deadpool", "", ""));
        alMarvelCharacters.add(new MarvelCharacter(002, "Iron Man", "", ""));
        alMarvelCharacters.add(new MarvelCharacter(003, "Thor", "", ""));

        gvListaPersonagens.setAdapter(new CharactersAdapter(context, alMarvelCharacters));
    }
}
