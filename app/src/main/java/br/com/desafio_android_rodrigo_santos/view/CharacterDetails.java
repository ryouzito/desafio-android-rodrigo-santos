package br.com.desafio_android_rodrigo_santos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import br.com.desafio_android_rodrigo_santos.R;
import br.com.desafio_android_rodrigo_santos.controller.ExibeHQ;
import br.com.desafio_android_rodrigo_santos.model.CharacterComic;

public class CharacterDetails extends AppCompatActivity {

    private Context context = this;
    private int id;
    private String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_details);

        //obtem os dados do personagem enviados pelo adapter
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        nome = intent.getStringExtra("nome");
        String descricao = intent.getStringExtra("descricao");
        String imagem = intent.getStringExtra("imagem");
        Button btnHQ = findViewById(R.id.buttonHQ);

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

        btnHQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExibeHQ exibeHQ = new ExibeHQ();
                exibeHQ.exibeHqDoPersonagem(context, id, nome);
            }
        });
    }

    public void exibeHQ(Context context, CharacterComic characterComic) {
        Intent intent = new Intent(context, ComicDetails.class);
        intent.putExtra("id", characterComic.getId());
        intent.putExtra("nomePersonagem", characterComic.getNomePersonagem());
        intent.putExtra("nomeHQ", characterComic.getNomeHQ());
        intent.putExtra("descricaoHQ", characterComic.getDescricaoHQ());
        intent.putExtra("imagemHQ", characterComic.getPathImageHQ());
        intent.putExtra("precoHQ", characterComic.getPrecoHQ());
        context.startActivity(intent);
    }
}
