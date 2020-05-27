package br.com.desafio_android_rodrigo_santos.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import br.com.desafio_android_rodrigo_santos.model.MarvelCharacter;
import br.com.desafio_android_rodrigo_santos.model.ParametrosUrl;
import br.com.webservice.WebService;
import okhttp3.Response;

public class ListaPersonagens {

    private Context context;
    private ArrayList<MarvelCharacter> alMarvelCharacters = new ArrayList<>();
    private GridView gvListaPersonagens;
    private int limit = 20;
    private int offset = 0;
    private int total;
    private int count;

    public void exibeListaPersonagens(Context context, GridView gvListaPersonagens) {
        this.context = context;
        this.gvListaPersonagens = gvListaPersonagens;
        new ListaPersonagensAsyncTask().execute();
    }

    //asynctask para listagem geral de personagens
    @SuppressLint("StaticFieldLeak")
    class ListaPersonagensAsyncTask extends AsyncTask<Void, Void, Response> {
        Response response;
        int idDoPersonagem;
        String nomeDoPersonagem = "";
        String descricaoDoPersonagem = "";
        String imagePath = "";

        WebService parametros = new WebService();
        ParametrosUrl parametrosUrl = parametros.geraParametrosUrl();

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                //chama a api e varre o json ate encontrar o campo results
                String path = "/characters";

                response = WebService.getDados(path, parametrosUrl.getTimestamp(),
                        parametrosUrl.getChavePublica(), parametrosUrl.getHash(), limit, offset);
                String dados = Objects.requireNonNull(response.body()).string();
                JSONArray jsonArray = new JSONObject(dados).getJSONObject("data").getJSONArray("results");

                //para cada posicao no campo results, cria um objeto MarvelCharacter e o adiciona a uma lista
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

            /* chama o adapter que trata o gridview, passando o conteudo da lista */
            gvListaPersonagens.setAdapter(new CharactersAdapter(context, alMarvelCharacters));
        }
    }
}
