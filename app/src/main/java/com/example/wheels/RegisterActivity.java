package com.example.wheels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //variables
        TextView textAnteriorPantalla = findViewById(R.id.textAnteriorPantalla);
        TextView textContinuarPantalla  = findViewById(R.id.textContinuarPantalla);
        Button botonTipoConductor = findViewById(R.id.botonTipoConductor);
        Button botonTipoPasajero = findViewById(R.id.botonTipoPasajero);


        //Anterior
        textAnteriorPantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentVolver = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intentVolver);
                finish();
            }
        });

        //Boton conductor seleccionado
        botonTipoConductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        textContinuarPantalla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Colocar logica para saber si el boton fue seleccionado o no (condicional)
                Intent intentContinuar = new Intent(RegisterActivity.this, RegisterDatosPersonalesActivity.class);
                startActivity(intentContinuar);
                finish();

            }
        });




    }
}