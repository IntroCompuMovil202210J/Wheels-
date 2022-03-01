package com.example.wheels;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    TextView textAnteriorPantalla;
    TextView textContinuarPantalla;
    Button botonTipoConductor;
    Button botonTipoPasajero;
    int tipoUsuario; //1 - Conductor    2 - Pasajero

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //variables
        textAnteriorPantalla = findViewById(R.id.textAnteriorPantalla);
        textContinuarPantalla  = findViewById(R.id.textContinuarPantalla);
        botonTipoConductor = findViewById(R.id.botonTipoConductor);
        botonTipoPasajero = findViewById(R.id.botonTipoPasajero);
        tipoUsuario = 0;

        //Anterior
        textAnteriorPantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVolver = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intentVolver);
            }
        });

        //Boton conductor seleccionado
        botonTipoConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonTipoConductor.setBackgroundColor(Color.parseColor("#c33332"));
                botonTipoConductor.setAlpha(Float.parseFloat("0.9"));
                botonTipoConductor.setTextColor(Color.WHITE);
                botonTipoPasajero.setBackgroundColor(Color.WHITE);
                botonTipoPasajero.setTextColor(Color.BLACK);
                textContinuarPantalla.setTextColor(Color.parseColor("#c33332"));
                tipoUsuario = 1;
            }
        });

        //Boton pasajero seleccionado
        botonTipoPasajero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                botonTipoPasajero.setBackgroundColor(Color.parseColor("#c33332"));
                botonTipoPasajero.setAlpha(Float.parseFloat("0.9"));
                botonTipoPasajero.setTextColor(Color.WHITE);
                botonTipoConductor.setBackgroundColor(Color.WHITE);
                botonTipoConductor.setTextColor(Color.BLACK);
                textContinuarPantalla.setTextColor(Color.parseColor("#c33332"));
                tipoUsuario = 2;
            }
        });

        //Boton continuar
        textContinuarPantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Colocar logica para saber si el boton fue seleccionado o no (condicional)
                Intent intentContinuar = new Intent(RegisterActivity.this, RegisterDatosPersonalesActivity.class);
                if(tipoUsuario == 1) intentContinuar.putExtra("tipoUsuario", 1);
                else if(tipoUsuario == 2) intentContinuar.putExtra("tipoUsuario", 2);
                startActivity(intentContinuar);
            }
        });
    }
}