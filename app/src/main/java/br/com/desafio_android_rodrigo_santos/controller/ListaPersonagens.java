package br.com.desafio_android_rodrigo_santos.controller;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import br.com.desafio_android_rodrigo_santos.R;
import br.com.desafio_android_rodrigo_santos.model.MarvelCharacter;
import br.com.desafio_android_rodrigo_santos.model.ParametrosUrl;
import br.com.desafio_android_rodrigo_santos.view.ActivityCharacters;
import br.com.webservice.WebService;
import okhttp3.Response;

public class ListaPersonagens {

    private Context context;
    private ArrayList<MarvelCharacter> alMarvelCharacters = new ArrayList<>();
    private WebService parametros = new WebService();
    private ProgressDialog dialog;
    private GridView gvListaPersonagens;
    private int offset = 0;
    private int total;
    private int contagem = 0;

    public void exibeListaPersonagens(Context context, GridView gvListaPersonagens, int offset) {
        this.context = context;
        this.gvListaPersonagens = gvListaPersonagens;
        this.offset = offset;
        this.dialog = new ProgressDialog(context);
        alMarvelCharacters.clear();

        if (!parametros.isNetworkConnected(context))
            semConexaoDialog();
        else
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

        ParametrosUrl parametrosUrl = parametros.geraParametrosUrl();

        @Override
        protected void onPreExecute() {
            dialog.setTitle(context.getString(R.string.buscando_personagens));
            dialog.setMessage(context.getString(R.string.aguarde));
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                //chama a api e varre o json ate encontrar o campo results
                String path = "/characters";

                int limit = 20;
                response = WebService.getDados(path, parametrosUrl.getTimestamp(),
                        parametrosUrl.getChavePublica(), parametrosUrl.getHash(), limit, offset);
                String dados = Objects.requireNonNull(response.body()).string();
                JSONObject jsonObjectContagem = new JSONObject(dados).getJSONObject("data");
                total = jsonObjectContagem.getInt("total");
                contagem += jsonObjectContagem.getInt("count");
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
            if (dialog.isShowing()) {
                dialog.dismiss();
                if (offset > 0)
                    ActivityCharacters.btnAnterior.setVisibility(View.VISIBLE);
                else
                    ActivityCharacters.btnAnterior.setVisibility(View.INVISIBLE);

                if (contagem >= total)
                    ActivityCharacters.btnProximo.setVisibility(View.INVISIBLE);
                else
                    ActivityCharacters.btnProximo.setVisibility(View.VISIBLE);

                gvListaPersonagens.setAdapter(new CharactersAdapter(context, alMarvelCharacters));
            }
        }
    }

    private void semConexaoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.sem_conexao));
        builder.setMessage(context.getString(R.string.verifique_internet))
                .setPositiveButton(context.getString(R.string.tentar_novamente), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        exibeListaPersonagens(context, gvListaPersonagens, offset);
                    }
                });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }
}
