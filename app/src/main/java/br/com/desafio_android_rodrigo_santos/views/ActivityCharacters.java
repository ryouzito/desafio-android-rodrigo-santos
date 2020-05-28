package br.com.desafio_android_rodrigo_santos.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import br.com.desafio_android_rodrigo_santos.R;
import br.com.desafio_android_rodrigo_santos.controllers.ListaPersonagens;

public class ActivityCharacters extends AppCompatActivity {

    private Context context = this;
    private int offset = 0;
    public static Button btnAnterior;
    public static Button btnProximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_characters);

        //instancia o GridView e a classe que gera a lista de personagens
        final GridView gvListaPersonagens = findViewById(R.id.gridViewListaPersonagens);
        btnAnterior = findViewById(R.id.buttonAnterior);
        btnProximo = findViewById(R.id.buttonProximo);

        //mantem os botoes invisiveis ate que a api retorne algo
        btnAnterior.setVisibility(View.INVISIBLE);
        btnProximo.setVisibility(View.INVISIBLE);

        final ListaPersonagens listaPersonagens = new ListaPersonagens();

        btnAnterior.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //impede que seja feita uma busca com offset menor que 0
                if (offset > 0)
                    offset -= 20;
                else
                    offset = 0;

                listaPersonagens.exibeListaPersonagens(context, gvListaPersonagens, offset);
            }
        });

        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offset += 20;
                listaPersonagens.exibeListaPersonagens(context, gvListaPersonagens, offset);
            }
        });

        listaPersonagens.exibeListaPersonagens(context, gvListaPersonagens, offset);
    }
}
