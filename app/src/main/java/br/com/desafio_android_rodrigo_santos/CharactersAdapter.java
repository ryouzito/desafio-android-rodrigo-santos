package br.com.desafio_android_rodrigo_santos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

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

    private class ViewHolder {
        TextView tvCharacterName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.character, null);
            holder = new ViewHolder();
            holder.tvCharacterName = convertView.findViewById(R.id.textViewPersonagem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MarvelCharacter currentRowItem = (MarvelCharacter) getItem(position);
        holder.tvCharacterName.setText(currentRowItem.getsNome());
        String nome = alMarvelCharacters.get(position).getsNome();
        holder.tvCharacterName.setText(nome);

        return convertView;
    }
}
