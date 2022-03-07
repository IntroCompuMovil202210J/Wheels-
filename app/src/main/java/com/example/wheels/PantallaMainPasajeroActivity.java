package com.example.wheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PantallaMainPasajeroActivity extends AppCompatActivity {

    ImageButton imageGruposP;
    ImageButton imagePerfilP;
    Button buttonBuscarGrupoP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_main_pasajero);

        imageGruposP = findViewById(R.id.imageGruposP);
        imagePerfilP = findViewById(R.id.imagePerfilP);
        buttonBuscarGrupoP = findViewById(R.id.buttonBuscarGrupoP);

        imageGruposP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaMainPasajeroActivity.this, GruposActivity.class));
            }
        });

        imagePerfilP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaMainPasajeroActivity.this, PerfilPasajeroActivity.class));
            }
        });

        buttonBuscarGrupoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaMainPasajeroActivity.this, BuscarGrupoPActivity.class));
            }
        });
    }
}