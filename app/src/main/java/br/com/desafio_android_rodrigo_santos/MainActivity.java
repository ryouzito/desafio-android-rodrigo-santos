package br.com.desafio_android_rodrigo_santos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //chama a activity que lista os personagens
        Intent intent = new Intent(this, ActivityCharacters.class);
        startActivity(intent);
    }
}