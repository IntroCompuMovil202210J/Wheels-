package com.example.wheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterDatosPersonalesActivity extends AppCompatActivity {

    Button botonDatosPersonales;
    TextView textViewDatosPersonalesNombre;
    TextView textViewDatosPersonalesTelefono;
    TextView textViewDatosPersonalesCorreo;
    TextView textViewDatosPersonalesContra;
    FirebaseAuth fAuth;
    FirebaseFirestore fFirestore;
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
        fAuth = FirebaseAuth.getInstance();
        fFirestore = FirebaseFirestore.getInstance();


        tipoUsuario = getIntent().getIntExtra("tipoUsuario", 2);

        //Register tipo conductor

            botonDatosPersonales.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Falta poner logica para confirmar los datos personales
                    if(!textViewDatosPersonalesNombre.getText().toString().isEmpty()){
                        if(!textViewDatosPersonalesTelefono.getText().toString().isEmpty()){
                            if(!textViewDatosPersonalesCorreo.getText().toString().isEmpty()){
                                if(!textViewDatosPersonalesContra.getText().toString().isEmpty()){
                                    if(textViewDatosPersonalesContra.getText().toString().length() >= 6) {

                                        createUser(textViewDatosPersonalesCorreo.getText().toString(), textViewDatosPersonalesContra.getText().toString(), String.valueOf(tipoUsuario), textViewDatosPersonalesTelefono.getText().toString());

                                    } else textViewDatosPersonalesContra.setError("Ingrese una contraseña mas larga");
                                }else textViewDatosPersonalesContra.setError("Ingrese su contraseña");
                            }else textViewDatosPersonalesCorreo.setError("Ingrese su correo");
                        }else textViewDatosPersonalesTelefono.setError("Ingrese su telefono");
                    }else textViewDatosPersonalesNombre.setError("Ingrese su nombre");
                }
            });
    }

    private void createUser(String email, String password, String tipoUsuario, String telefono){
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String id = fAuth.getCurrentUser().getUid(); //Retorna la sesión del usuario actual
                    Map<String, Object> map = new HashMap<>();
                    map.put("email", email);
                    map.put("password", password);
                    map.put("tipoUsuario", tipoUsuario);
                    map.put("telefono", telefono);
                    fFirestore.collection("Usuarios").document(id).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                if(Integer.parseInt(tipoUsuario) == 1){
                                    startActivity(new Intent(RegisterDatosPersonalesActivity.this, RegisterVehiculoActivity.class));
                                    finish();
                                }

                                else if(Integer.parseInt(tipoUsuario) == 2){
                                    startActivity(new Intent(RegisterDatosPersonalesActivity.this, PantallaMainPasajeroActivity.class));
                                    finish();
                                }

                                //Añadir informacion de carros

                                Toast.makeText(RegisterDatosPersonalesActivity.this, "exito", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    //Usuario creado en firebase

                }
                else{
                    //No se pudo registrar
                    Toast.makeText(RegisterDatosPersonalesActivity.this, "FRACASO", Toast.LENGTH_LONG).show();
                }
            }
        });

        }
}
