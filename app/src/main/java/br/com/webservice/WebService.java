package br.com.webservice;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebService {
    static String url = "https://gateway.marvel.com:443/v1/public";

    public static Response getListaCharacters() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url + "") //url omitida para que seja feita criptografia depois
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        return response;
    }

    public static Response getCharacter() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url + "") //url omitida para que seja feita criptografia depois
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();

        return response;
    }
}
