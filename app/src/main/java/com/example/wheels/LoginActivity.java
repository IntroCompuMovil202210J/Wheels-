package com.example.wheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    TextView textRegistroB;
    TextView textViewLoginCorreo;
    TextView textViewLoginContra;
    Button buttonLoginEntrar;
    ImageView entrarMicrosoft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Variables
        textRegistroB = findViewById(R.id.textRegistroB);
        textViewLoginCorreo = findViewById(R.id.textViewLoginCorreo);
        textViewLoginContra = findViewById(R.id.textViewLoginContra);
        buttonLoginEntrar = findViewById(R.id.buttonLoginEntrar);
        entrarMicrosoft = findViewById(R.id.imageViewMicrosoft);

        textRegistroB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonLoginEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!textViewLoginCorreo.getText().toString().isEmpty()){
                    if(!textViewLoginContra.getText().toString().isEmpty()){

                        startActivity(new Intent(LoginActivity.this, PantallaMainPasajeroActivity.class));


                    } else textViewLoginContra.setError("Ingrese su contraseña");
                } else textViewLoginCorreo.setError("Ingrese su correo");
            }
        });

        entrarMicrosoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, PantallaMainConductorActivity.class));
            }
        });
    }

}