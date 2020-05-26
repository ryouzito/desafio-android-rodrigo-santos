package br.com.desafio_android_rodrigo_santos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import br.com.desafio_android_rodrigo_santos.R;
import br.com.desafio_android_rodrigo_santos.controller.ExibeHQ;
import br.com.desafio_android_rodrigo_santos.model.CharacterComic;

public class ComicDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_details);

        Intent intent = getIntent();
        String nomeDoPersonagem = "Captain America";
        String nomeHQ = intent.getStringExtra("nomeHQ");
        String descricaoHQ = intent.getStringExtra("descricaoHQ");
        String pathImagemHQ = intent.getStringExtra("imagemHQ");
        String precoHQ = "$ " + intent.getDoubleExtra("precoHQ", 0.00);

        //define o titulo da ActionBar como o nome do personagem
        Objects.requireNonNull(getSupportActionBar()).setTitle(nomeDoPersonagem);

        TextView tvNomeHQ = findViewById(R.id.textViewTituloHQ);
        tvNomeHQ.setText(nomeHQ);

        //instancia um ImageView e adiciona a imagem do personagem utilizando Picasso
        ImageView imagemHQ = findViewById(R.id.imageViewComic);
        Picasso.get()
                .load(pathImagemHQ)
                .error(R.drawable.marvel_logo)
                .into(imagemHQ);

        //instancia um TextView e adiciona a descricao do personagem a ele
        TextView tvDescricaoHQ = findViewById(R.id.textViewDescricaoHQ);
        assert descricaoHQ != null;
        if (descricaoHQ.equals("null"))
            //muitos personagens vem sem descricao, entao essa condicao foi adicionada
            tvDescricaoHQ.setText(R.string.sem_descricao);
        else
            tvDescricaoHQ.setText(descricaoHQ);

        TextView tvPreco = findViewById(R.id.textViewPrecoHQ);
        tvPreco.setText(precoHQ);
    }
}
