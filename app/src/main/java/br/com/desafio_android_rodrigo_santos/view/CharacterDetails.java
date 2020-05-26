package br.com.desafio_android_rodrigo_santos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import br.com.desafio_android_rodrigo_santos.R;

public class CharacterDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);

        //obtem os dados do personagem enviados pelo adapter
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String nome = intent.getStringExtra("nome");
        String descricao = intent.getStringExtra("descricao");
        String imagem = intent.getStringExtra("imagem");

        //define o titulo da ActionBar como o nome do personagem
        Objects.requireNonNull(getSupportActionBar()).setTitle(nome);

        //instancia um ImageView e adiciona a imagem do personagem utilizando Picasso
        ImageView imagemPersonagem = findViewById(R.id.imageViewPersonagem);
        Picasso.get()
                .load(imagem)
                .error(R.drawable.marvel_logo)
                .into(imagemPersonagem);

        //instancia um TextView e adiciona a descricao do personagem a ele
        TextView tvDescricaoPersonagem = findViewById(R.id.textViewDescricaoPersonagem);
        assert descricao != null;
        if (descricao.equals(""))
            //muitos personagens vem sem descricao, entao essa condicao foi adicionada
            tvDescricaoPersonagem.setText(R.string.sem_descricao);
        else
            tvDescricaoPersonagem.setText(descricao);
    }
}
