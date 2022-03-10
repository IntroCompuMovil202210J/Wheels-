package com.example.wheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PantallaMainConductorActivity extends AppCompatActivity {

    Button buttonCrearGrupo;
    ImageButton imagePerfilC;
    ImageButton buttonIniciarViajeC;
    ImageButton buttonGruposC;
    ImageView imagePerfilCon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_main_conductor);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        buttonCrearGrupo = findViewById(R.id.buttonCrearGrupoC);
        imagePerfilC = findViewById(R.id.imagePerfilC);
        buttonIniciarViajeC = findViewById(R.id.buttonIniciarViajeC);
        buttonGruposC = findViewById(R.id.imageGruposC);
        imagePerfilCon2 = findViewById(R.id.imagePerfilCon2);

        Glide.with(getBaseContext()).load("https://cdn.pixabay.com/photo/2016/11/29/13/14/attractive-1869761_960_720.jpg").apply(RequestOptions.circleCropTransform()).into(imagePerfilCon2);

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