package br.com.desafio_android_rodrigo_santos.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.desafio_android_rodrigo_santos.R;
import br.com.desafio_android_rodrigo_santos.model.MarvelCharacter;
import br.com.desafio_android_rodrigo_santos.view.CharacterDetails;

public class CharactersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MarvelCharacter> alMarvelCharacters;

    CharactersAdapter(Context context, ArrayList<MarvelCharacter> alMarvelCharacters) {
        this.context = context;
        this.alMarvelCharacters = alMarvelCharacters;
    }

    @Override
    public int getCount() {
        return alMarvelCharacters.size();
    }

    @Override
    public Object getItem(int position) {
        return alMarvelCharacters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alMarvelCharacters.indexOf(getItem(position));
    }

    private static class ViewHolder {
        TextView tvCharacterName;
        ImageView ivCharacterImage;
    }

    //gera e popula o gridview com cada item da lista de personagens
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            assert inflater != null;
            convertView = inflater.inflate(R.layout.character, null);
            holder = new ViewHolder();
            holder.tvCharacterName = convertView.findViewById(R.id.textViewPersonagem);
            holder.ivCharacterImage = convertView.findViewById(R.id.imageViewPersonagem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //adiciona as informações de cada personagem na intent e chama a activity de detalhes
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CharacterDetails.class);
                intent.putExtra("id", alMarvelCharacters.get(position).getId());
                intent.putExtra("nome", alMarvelCharacters.get(position).getsNome());
                intent.putExtra("descricao", alMarvelCharacters.get(position).getsDescricao());
                intent.putExtra("imagem", alMarvelCharacters.get(position).getsImagePath());
                context.startActivity(intent);
            }
        });

        //utiliza Picasso para adicionar as imagens no gridview atraves da url
        MarvelCharacter currentRowItem = (MarvelCharacter) getItem(position);
        holder.tvCharacterName.setText(currentRowItem.getsNome());
        Picasso.get()
                .load(alMarvelCharacters.get(position).getsImagePath())
                .error(R.drawable.marvel_logo)
                .into(holder.ivCharacterImage);
        String nome = alMarvelCharacters.get(position).getsNome();
        holder.tvCharacterName.setText(nome);

        return convertView;
    }
}
