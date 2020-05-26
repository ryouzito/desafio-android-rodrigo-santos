package br.com.desafio_android_rodrigo_santos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;
import br.com.desafio_android_rodrigo_santos.R;
import br.com.desafio_android_rodrigo_santos.controller.ListaPersonagens;

public class ActivityCharacters extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        //instancia o GridView e a classe que gera a lista de personagens
        GridView gvListaPersonagens = findViewById(R.id.gridViewListaPersonagens);
        ListaPersonagens listaPersonagens = new ListaPersonagens();

        //chama o metodo que inicia a AsyncTask de listagem
        listaPersonagens.exibeListaPersonagens(context, gvListaPersonagens);
    }
}
