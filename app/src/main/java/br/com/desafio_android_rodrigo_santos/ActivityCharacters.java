package br.com.desafio_android_rodrigo_santos;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

import br.com.webservice.WebService;
import okhttp3.Response;

public class ActivityCharacters extends AppCompatActivity {

    Context context;
    String timestamp = "";
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

    //gera os parametros passados na url
    public String geraParametrosUrl() {
        String parametrosUrl = "";

        //gera timestamp
        String dataBrasil = "dd-MM-yyyy-hh-mm-ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dataBrasil, new Locale("pt", "BR"));
        timestamp = simpleDateFormat.format(new Date());

        byte[] data = Base64.decode("MGEyNTg2MDE1MzdkYTlkNDAzMmMxMGMyOGUwMjIyMmQzYjhjMmY2YQ==", Base64.DEFAULT);
        String text = new String(data, StandardCharsets.UTF_8);

        String hash = timestamp + text + "306eb231e4603390b106800cff2e5b54";

        //cria o hash que a api espera receber
        try {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
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
    class exibeListaPersonagens extends AsyncTask<Void, Void, Response> {
        Response response;
        int idDoPersonagem;
        String nomeDoPersonagem = "";
        String descricaoDoPersonagem = "";
        String imagePath = "";

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                String hash = geraParametrosUrl();
                response = WebService.getListaCharacters(timestamp, hash);
                String dados = Objects.requireNonNull(response.body()).string();
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

    @SuppressLint("StaticFieldLeak")
    class exibePersonagem extends AsyncTask<Void, Void, Response> {
        Response response;
        int idDoPersonagem;
        String nomeDoPersonagem = "";
        String descricaoDoPersonagem = "";

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                response = WebService.getCharacter(timestamp, geraParametrosUrl());
                String dados = Objects.requireNonNull(response.body()).string();
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
