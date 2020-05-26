package br.com.desafio_android_rodrigo_santos.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import br.com.desafio_android_rodrigo_santos.BuildConfig;
import br.com.desafio_android_rodrigo_santos.model.MarvelCharacter;
import br.com.webservice.WebService;
import okhttp3.Response;

public class ListaPersonagens {

    private Context context;
    private String timestamp = "";
    private ArrayList<MarvelCharacter> alMarvelCharacters = new ArrayList<>();
    private GridView gvListaPersonagens;

    public void exibeListaPersonagens(Context context, GridView gvListaPersonagens) {
        this.context = context;
        this.gvListaPersonagens = gvListaPersonagens;
        new ListaPersonagensAsyncTask().execute();
    }

    //gera os parametros passados na url
    private String geraParametrosUrl() {

        //gera timestamp
        String dataBrasil = "dd-MM-yyyy-hh-mm-ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dataBrasil, new Locale("pt", "BR"));
        timestamp = simpleDateFormat.format(new Date());

        byte[] data = Base64.decode("MGEyNTg2MDE1MzdkYTlkNDAzMmMxMGMyOGUwMjIyMmQzYjhjMmY2YQ==", Base64.DEFAULT);
        String text = new String(data, StandardCharsets.UTF_8);

        String hash = timestamp + text + "306eb231e4603390b106800cff2e5b54";

        //cria o hash que a api espera receber
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(hash.getBytes());
            byte[] messageDigest = digest.digest();

            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xff & aMessageDigest);
                while (h.length() < 2)
                    h = "0".concat(h);
                hexString.append(h);
            }
            return  hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    //asynctask para listagem geral de personagens
    @SuppressLint("StaticFieldLeak")
    class ListaPersonagensAsyncTask extends AsyncTask<Void, Void, Response> {
        Response response;
        int idDoPersonagem;
        String nomeDoPersonagem = "";
        String descricaoDoPersonagem = "";
        String imagePath = "";

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                //chama a api e varre o json ate encontrar o campo results
                String hash = geraParametrosUrl();
                response = WebService.getListaCharacters(timestamp, hash);
                String dados = Objects.requireNonNull(response.body()).string();
                JSONArray jsonArray = new JSONObject(dados).getJSONObject("data").getJSONArray("results");

                //para cada posicao no campo results, cria um objeto MarvelCharater e o adiciona a uma lista
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
