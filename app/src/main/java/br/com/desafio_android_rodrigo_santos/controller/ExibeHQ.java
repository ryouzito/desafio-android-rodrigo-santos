package br.com.desafio_android_rodrigo_santos.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import br.com.desafio_android_rodrigo_santos.model.CharacterComic;
import br.com.desafio_android_rodrigo_santos.model.ParametrosUrl;
import br.com.desafio_android_rodrigo_santos.view.CharacterDetails;
import br.com.webservice.WebService;
import okhttp3.Response;

public class ExibeHQ {

    private Context context;
    private CharacterComic characterComic;
    private ArrayList<CharacterComic> listaDeHqs = new ArrayList<>();

    public void exibeHqDoPersonagem(Context context) {
        this.context = context;
        new BuscaHQAsyncTask().execute();
    }

    public CharacterComic getCharacterComic() {
        return characterComic;
    }

    //asynctask para listagem da HQ do personagem selecionado
    @SuppressLint("StaticFieldLeak")
    class BuscaHQAsyncTask extends AsyncTask<Void, Void, Response> {
        Response response;
        int idHQ;
        String nomeHQ = "";
        String descricaoHQ = "";
        String imagePath = "";
        Double precoHQ;

        WebService parametros = new WebService();
        ParametrosUrl parametrosUrl = parametros.geraParametrosUrl();

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                //chama a api e varre o json ate encontrar o campo results
                String path = "/characters/1009220/comics";
                String limit = "100";
                String offset = "0";

                response = WebService.getDados(path, parametrosUrl.getTimestamp(),
                        parametrosUrl.getChavePublica(), parametrosUrl.getHash(), limit, offset);
                String dados = Objects.requireNonNull(response.body()).string();
                JSONArray jsonArray = new JSONObject(dados).getJSONObject("data").getJSONArray("results");

                //para cada posicao no campo results, cria um objeto MarvelCharacter e o adiciona a uma lista
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                    idHQ = jsonObject.getInt("id");
                    nomeHQ = jsonObject.getString("title");
                    descricaoHQ = jsonObject.getString("description");
                    JSONArray jsonArrayPreco = jsonObject.getJSONArray("prices");
                    JSONObject jsonObjectPreco = jsonArrayPreco.getJSONObject(0);
                    precoHQ = Double.parseDouble(jsonObjectPreco.getString("price"));
                    JSONObject jsonObjectImagem = jsonObject.getJSONObject("thumbnail");
                    imagePath = jsonObjectImagem.getString("path") + "." + jsonObjectImagem.getString("extension");

                    listaDeHqs.add(new CharacterComic(idHQ, nomeHQ, descricaoHQ, precoHQ, imagePath));
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);

            Collections.sort(listaDeHqs, new Comparator<CharacterComic>() {
                @Override
                public int compare(CharacterComic a, CharacterComic b) {
                    return a.getPrecoHQ().compareTo(b.getPrecoHQ());
                }
            });

            listaDeHqs.size();

            //cria um objeto contendo a hq mais cara e envia de volta para o metodo que chama a intent
//            characterComic = new CharacterComic(idHQ, nomeHQ, descricaoHQ, precoHQ, imagePath);
            characterComic = listaDeHqs.get(listaDeHqs.size()-1);
            CharacterDetails characterDetails = new CharacterDetails();
            characterDetails.exibeHQ(context, characterComic);
        }
    }
}
