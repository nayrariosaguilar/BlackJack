package com.example.ruletauwu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class pantallaInicial extends AppCompatActivity {
    Button botonIncio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_inicial);
        inicializarElementos();
        inicializarListener();
    }
    private void inicializarElementos(){
        botonIncio = findViewById(R.id.btletsplay);
    }
    private void inicializarListener(){
       botonIncio.setOnClickListener(this::CambioView);
    }
    private void CambioView(View view){
        Intent otraVista= new Intent(this, GameView.class);
        startActivity(otraVista);
    }
}