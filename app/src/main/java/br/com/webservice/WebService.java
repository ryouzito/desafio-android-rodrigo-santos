package br.com.webservice;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebService {
    private static String url = "https://gateway.marvel.com:443/v1/public";

    public static Response getListaCharacters(String timestamp, String hash) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url + "/characters?ts=" + timestamp + "&apikey=306eb231e4603390b106800cff2e5b54&hash=" + hash)
                .method("GET", null)
                .build();

        return client.newCall(request).execute();
    }
}
