package com.example.wheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //Para que se pantalla completa

        //Agregar animaciones
        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.expancion);
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);

        //objetos
        TextView textoInicioWheels = findViewById(R.id.textoInicioWheels);
        ImageView logoInicio = findViewById(R.id.logoInicio);

        //Asignar animacion
        logoInicio.setAnimation(animacion1);
        textoInicioWheels.setAnimation(animacion2);

        //Pasar siguient actividad

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);




    }
}