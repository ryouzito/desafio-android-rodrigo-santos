package br.com.desafio_android_rodrigo_santos.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import br.com.desafio_android_rodrigo_santos.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //chama a activity que organiza a lista de personagens
        Intent intent = new Intent(this, ActivityCharacters.class);
        startActivity(intent);

        /*
        nada mais foi adicionado aqui para que a MainActivity possa ser
        utilizada posteriormente, talvez numa splash screen, login ou launcher
         */
    }
}