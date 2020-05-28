package br.com.desafio_android_rodrigo_santos.controllers;

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
import br.com.desafio_android_rodrigo_santos.models.MarvelCharacter;
import br.com.desafio_android_rodrigo_santos.models.ParametrosUrl;
import br.com.desafio_android_rodrigo_santos.views.ActivityCharacters;
import br.com.desafio_android_rodrigo_santos.webservice.WebService;
import okhttp3.Response;

public class ListaPersonagens {

    private Context context;
    private ArrayList<MarvelCharacter> alMarvelCharacters = new ArrayList<>();
    private WebService parametros;
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
        this.parametros = new WebService(context);
        alMarvelCharacters.clear();

        if (!parametros.isNetworkConnected())
            Dialog(context.getString(R.string.sem_conexao), context.getString(R.string.tentar_novamente));
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

                //se houver um campo "data", a busca ocorreu corretamente
                if (dados.contains("data")) {
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
                }
                //caso nao haja um campo data, houve um erro na chamada da api
                else return null;
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);

            //response é nulo se nao houver campo data, significando erro na chamada da api
            if (response == null)
                Dialog(context.getString(R.string.erro_api_titulo), context.getString(R.string.erro_api_mensagem));

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

    //dialog usado em contextos de erro
    private void Dialog(String titulo, String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);

        //altera o botao dependendo do contexto
        if (parametros.isNetworkConnected()) {
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
        }
        else {
            builder.setPositiveButton(context.getString(R.string.tentar_novamente), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    exibeListaPersonagens(context, gvListaPersonagens, offset);
                }
            });
        }

        builder.setCancelable(false);
        builder.create();
        builder.show();
    }
}
