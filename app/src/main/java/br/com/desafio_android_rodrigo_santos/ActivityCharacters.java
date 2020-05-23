package br.com.desafio_android_rodrigo_santos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import br.com.webservice.WebService;
import okhttp3.Response;

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

        new exibeListaPersonagens().execute();
    }

    class exibeListaPersonagens extends  AsyncTask<Void, Void, Response> {
        Response response;
        int idDoPersonagem;
        String nomeDoPersonagem = "";
        String descricaoDoPersonagem = "";
        String imagePath = "";

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                response = WebService.getListaCharacters();
                String dados = response.body().string();
                JSONArray jsonArray = new JSONObject(dados).getJSONObject("data").getJSONArray("results");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    idDoPersonagem = jsonObject.getInt("id");
                    nomeDoPersonagem = jsonObject.getString("name");
                    descricaoDoPersonagem = jsonObject.getString("description");
                    JSONObject jsonObjectImagem = jsonObject.getJSONObject("thumbnail");
                    imagePath = jsonObjectImagem.getString("path") + "." + jsonObjectImagem.getString("extension");

                    alMarvelCharacters.add(new MarvelCharacter(idDoPersonagem, nomeDoPersonagem, descricaoDoPersonagem, imagePath));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);

            gvListaPersonagens.setAdapter(new CharactersAdapter(context, alMarvelCharacters));
        }
    }

    class exibePersonagem extends AsyncTask<Void, Void, Response> {
        Response response;
        int idDoPersonagem;
        String nomeDoPersonagem = "";
        String descricaoDoPersonagem = "";

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                response = WebService.getCharacter();
                String dados = response.body().string();
                JSONArray jsonArray = new JSONObject(dados).getJSONObject("data").getJSONArray("results");
                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                idDoPersonagem = jsonObject.getInt("id");
                nomeDoPersonagem = jsonObject.getString("name");
                descricaoDoPersonagem = jsonObject.getString("description");

                alMarvelCharacters.add(new MarvelCharacter(idDoPersonagem, nomeDoPersonagem, descricaoDoPersonagem, ""));

                return response;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);

            gvListaPersonagens.setAdapter(new CharactersAdapter(context, alMarvelCharacters));
        }
    }
}
