package com.example.wheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PantallaMainConductorActivity extends AppCompatActivity {

    Button buttonCrearGrupo;
    ImageButton imagePerfilC;
    ImageButton buttonIniciarViajeC;
    ImageButton buttonGruposC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_main_conductor);

        buttonCrearGrupo = findViewById(R.id.buttonCrearGrupoC);
        imagePerfilC = findViewById(R.id.imagePerfilC);
        buttonIniciarViajeC = findViewById(R.id.buttonIniciarViajeC);
        buttonGruposC = findViewById(R.id.imageGruposC);


        buttonCrearGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaMainConductorActivity.this, crearGrupoActivity.class));
            }
        });

        imagePerfilC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaMainConductorActivity.this, PerfilConductorActivity.class));
            }
        });

        buttonIniciarViajeC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaMainConductorActivity.this, IniciarViajeActivity.class));
            }
        });

        buttonGruposC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaMainConductorActivity.this, GruposActivity.class));
            }
        });
    }
}