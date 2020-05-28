package br.com.desafio_android_rodrigo_santos.controller;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

import br.com.desafio_android_rodrigo_santos.R;
import br.com.desafio_android_rodrigo_santos.model.CharacterComic;
import br.com.desafio_android_rodrigo_santos.model.ParametrosUrl;
import br.com.desafio_android_rodrigo_santos.view.CharacterDetails;
import br.com.webservice.WebService;
import okhttp3.Response;

public class ExibeHQ {

    private Context context;
    private int id;
    private String nomePersonagem;
    private CharacterComic characterComic;
    private ArrayList<CharacterComic> listaDeHqs = new ArrayList<>();
    private WebService parametros = new WebService();

    public void exibeHqDoPersonagem(Context context, int id, String nomePersonagem) {
        this.context = context;
        this.id = id;
        this.nomePersonagem = nomePersonagem;

        if (!parametros.isNetworkConnected(context))
            Dialog(context.getString(R.string.sem_conexao), context.getString(R.string.verifique_internet));
        else
            new BuscaHQAsyncTask().execute();
    }

    //asynctask para listagem da HQ do personagem selecionado
    @SuppressLint("StaticFieldLeak")
    class BuscaHQAsyncTask extends AsyncTask<Void, Void, Response> {

        private ProgressDialog dialog = new ProgressDialog(context);

        Response response;
        int idHQ;
        String nomeHQ = "";
        String descricaoHQ = "";
        String imagePath = "";
        Double precoHQ;
        JSONArray jsonArray;
        ParametrosUrl parametrosUrl = parametros.geraParametrosUrl();

        @Override
        protected void onPreExecute() {
            this.dialog.setTitle(context.getString(R.string.buscando_hq));
            this.dialog.setMessage(context.getString(R.string.aguarde));
            this.dialog.setCancelable(false);
            this.dialog.show();
        }

        @Override
        protected Response doInBackground(Void... voids) {
            try {
                String path = "/characters/" + id + "/comics";
                int limit = 100;
                int offset = 0;
                int total;

                //repete a busca ate encontrar todos os resultados, respeitando o limite maximo por busca
                do {
                    //chama a api e varre o json ate encontrar o campo results
                    response = WebService.getDados(path, parametrosUrl.getTimestamp(),
                            parametrosUrl.getChavePublica(), parametrosUrl.getHash(), limit, offset);

                    String dados = Objects.requireNonNull(response.body()).string();
                    JSONObject jsonObjectContagem = new JSONObject(dados).getJSONObject("data");
                    total = jsonObjectContagem.getInt("total");
                    jsonArray = new JSONObject(dados).getJSONObject("data").getJSONArray("results");

                    if (jsonArray.length() <= 0)
                        return response;

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

                        listaDeHqs.add(new CharacterComic(idHQ, nomePersonagem, nomeHQ, descricaoHQ, precoHQ, imagePath));
                    }
                    //incrementa o offset em 100 para realizar uma nova busca
                    offset += 100;
                }
                while (listaDeHqs.size() < total);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);

            //interrompe o fluxo caso o personagem nao possua hq na lista
            if (jsonArray.length() <= 0) {
                dialog.dismiss();
                Dialog(context.getString(R.string.personagem_sem_hq), "");
            }

            Collections.sort(listaDeHqs, new Comparator<CharacterComic>() {
                @Override
                public int compare(CharacterComic a, CharacterComic b) {
                    return a.getPrecoHQ().compareTo(b.getPrecoHQ());
                }
            });

            if (dialog.isShowing()) {
                dialog.dismiss();

                //cria um objeto contendo a hq mais cara e envia de volta para o metodo que chama a intent
                characterComic = listaDeHqs.get(listaDeHqs.size()-1);
                CharacterDetails characterDetails = new CharacterDetails();
                characterDetails.exibeHQ(context, characterComic);
            }
        }
    }

    //exibe um dialog informando que o personagem nao possui hq
    private void Dialog(String titulo, String mensagem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setMessage(mensagem);

        //altera o botao dependendo do contexto
        if (parametros.isNetworkConnected(context)) {
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                }
            });
        }
        else {
            builder.setPositiveButton(context.getString(R.string.tentar_novamente), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    exibeHqDoPersonagem(context, id, nomePersonagem);
                }
            });
        }

        builder.setCancelable(false);
        builder.create();
        builder.show();
    }
}
