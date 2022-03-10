package com.example.wheels;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class RegisterVehiculoActivity extends AppCompatActivity {

    Button botonContinuarVehicular;
    Button buttonRegistrarAñadirOtro;
    TextView textViewResgistrarVehiculoPlaca;
    TextView textViewResgistrarVehiculoCapacidad;
    TextView textViewResgistrarVehiculoColor;
    TextView textViewResgistrarVehiculoMarca;
    TextView textViewResgistrarVehiculoModelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vehiculo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Variables
        botonContinuarVehicular = findViewById(R.id.botonContinuarVehiculo);
        buttonRegistrarAñadirOtro = findViewById(R.id.buttonRegistrarAñadirOtro);
        textViewResgistrarVehiculoPlaca = findViewById(R.id.textViewRegistrarVehiculoPlaca);
        textViewResgistrarVehiculoCapacidad = findViewById(R.id.textViewRegistrarVehiculoCapacidad);
        textViewResgistrarVehiculoColor = findViewById(R.id.textViewRegistrarVehiculoColor);
        textViewResgistrarVehiculoMarca = findViewById(R.id.textViewRegistrarVehiculoMarca);;
        textViewResgistrarVehiculoModelo = findViewById(R.id.textViewRegistrarVehiculoModelo);;


        botonContinuarVehicular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Falta logica para validar los datos
                if (!textViewResgistrarVehiculoPlaca.getText().toString().isEmpty()) {
                    if (!textViewResgistrarVehiculoCapacidad.getText().toString().isEmpty()) {
                        if (!textViewResgistrarVehiculoColor.getText().toString().isEmpty()) {
                            if (!textViewResgistrarVehiculoMarca.getText().toString().isEmpty()) {
                                if (!textViewResgistrarVehiculoModelo.getText().toString().isEmpty()) {

                                    Intent intentContinuarVehiculo = new Intent(RegisterVehiculoActivity.this, PantallaMainConductorActivity.class);
                                    startActivity(intentContinuarVehiculo);
                                    finish();

                                }else textViewResgistrarVehiculoModelo.setError("Ingrese el modelo");
                            }else textViewResgistrarVehiculoMarca.setError("Ingrese la marca");
                        }else textViewResgistrarVehiculoColor.setError("Ingrese el color");
                    }else textViewResgistrarVehiculoCapacidad.setError("Ingrese la capacidad");
                }else textViewResgistrarVehiculoPlaca.setError("Ingrese la placa");

            }
        });



    }


}