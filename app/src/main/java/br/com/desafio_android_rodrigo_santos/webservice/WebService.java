package br.com.desafio_android_rodrigo_santos.webservice;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.desafio_android_rodrigo_santos.BuildConfig;
import br.com.desafio_android_rodrigo_santos.R;
import br.com.desafio_android_rodrigo_santos.models.ParametrosUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebService {

    private Context context;

    public WebService(Context context) {
        this.context = context;
    }

    public static Response getDados(String path, String timestamp, String chavePublica, String hash, int limit, int offset) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url = "https://gateway.marvel.com:443/v1/public";
        Request request = new Request.Builder()
                .url(url + path + "?ts=" + timestamp + "&apikey=" + chavePublica + "&hash=" + hash + "&limit=" + limit + "&offset=" + offset)
                .method("GET", null)
                .build();

        return client.newCall(request).execute();
    }

    public ParametrosUrl geraParametrosUrl() {

        //gera timestamp
        String dataBrasil = "dd-MM-yyyy-hh-mm-ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dataBrasil, new Locale("pt", "BR"));
        String timestamp = simpleDateFormat.format(new Date());

        String hash = timestamp + BuildConfig.PRIVATE_KEY + BuildConfig.PUBLIC_KEY;

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
            hash = hexString.toString();
            return new ParametrosUrl(timestamp, BuildConfig.PUBLIC_KEY, hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    //checa conexao
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
