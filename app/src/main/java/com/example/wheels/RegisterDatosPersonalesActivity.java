package com.example.wheels;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterDatosPersonalesActivity extends AppCompatActivity {

    Button botonDatosPersonales;
    TextView textViewDatosPersonalesNombre;
    TextView textViewDatosPersonalesTelefono;
    TextView textViewDatosPersonalesCorreo;
    TextView textViewDatosPersonalesContra;
    int tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_datos_personales);

        //Variables
        botonDatosPersonales = findViewById(R.id.botonDatosPersonales);
        textViewDatosPersonalesNombre = findViewById(R.id.textViewDatosPersonalesNombre);
        textViewDatosPersonalesTelefono = findViewById(R.id.textViewDatosPersonalesTelefono);;
        textViewDatosPersonalesCorreo = findViewById(R.id.textViewDatosPersonalesCorreo);;
        textViewDatosPersonalesContra = findViewById(R.id.textViewDatosPersonalesContra);;


        tipoUsuario = getIntent().getIntExtra("tipoUsuario", 2);

        //Register tipo conductor
        if(tipoUsuario == 1){
            botonDatosPersonales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Falta poner logica para confirmar los datos personales
                    if(!textViewDatosPersonalesNombre.getText().toString().isEmpty()){
                        if(!textViewDatosPersonalesTelefono.getText().toString().isEmpty()){
                            if(!textViewDatosPersonalesCorreo.getText().toString().isEmpty()){
                                if(!textViewDatosPersonalesContra.getText().toString().isEmpty()){

                                    Intent intentContinuar = new Intent(RegisterDatosPersonalesActivity.this, RegisterVehiculoActivity.class);
                                    startActivity(intentContinuar);
                                    finish();//Poner para que ponga pulsa dos veces para salir.

                                }else textViewDatosPersonalesContra.setError("Ingrese su contraseña");
                            }else textViewDatosPersonalesCorreo.setError("Ingrese su correo");
                        }else textViewDatosPersonalesTelefono.setError("Ingrese su telefono");
                    }else textViewDatosPersonalesNombre.setError("Ingrese su nombre");
                }
            });
        }

        //Register tipo pasajero
        else if (tipoUsuario == 2){
            botonDatosPersonales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Falta poner logica para confirmar los datos personales

                    if(!textViewDatosPersonalesNombre.getText().toString().isEmpty()){
                        if(!textViewDatosPersonalesTelefono.getText().toString().isEmpty()){
                            if(!textViewDatosPersonalesCorreo.getText().toString().isEmpty()){
                                if(!textViewDatosPersonalesContra.getText().toString().isEmpty()){

                                    Intent intentContinuar = new Intent(RegisterDatosPersonalesActivity.this, PantallaMainPasajeroActivity.class);
                                    startActivity(intentContinuar);
                                    finish(); //Poner para que ponga pulsa dos veces para salir.

                                }else textViewDatosPersonalesContra.setError("Ingrese su contraseña");
                            }else textViewDatosPersonalesCorreo.setError("Ingrese su correo");
                        }else textViewDatosPersonalesTelefono.setError("Ingrese su telefono");
                    }else textViewDatosPersonalesNombre.setError("Ingrese su nombre");


                }
            });
        }
    }
}