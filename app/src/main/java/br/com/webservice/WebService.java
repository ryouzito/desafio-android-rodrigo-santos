package br.com.webservice;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import br.com.desafio_android_rodrigo_santos.model.ParametrosUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebService {

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

        //insira suas chaves da API Marvel nos dois campos abaixo
        String chavePrivada = "";
        String chavePublica = "";

        String hash = timestamp + chavePrivada + chavePublica;

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
            return new ParametrosUrl(timestamp, chavePublica, hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}
