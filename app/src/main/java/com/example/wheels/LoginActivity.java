package com.example.wheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    TextView textRegistroB;
    TextView textViewLoginCorreo;
    TextView textViewLoginContra;
    Button buttonLoginEntrar;
    FirebaseAuth fAuth;
    FirebaseFirestore fFireStore;
    DatabaseReference ref;

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
        fAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();

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

                        //Logica para saber que tipo de usuario es.
                        login(textViewLoginCorreo.getText().toString(), textViewLoginContra.getText().toString());

                    } else textViewLoginContra.setError("Ingrese su contraseña");
                } else textViewLoginCorreo.setError("Ingrese su correo");
            }
        });
    }

    private void login(String email, String contrasena){

        fAuth.signInWithEmailAndPassword(email, contrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Exito", Toast.LENGTH_SHORT).show();

                    //buttonLoginEntrar.setText("fsdfsfew");

                   /* ref.child("Usuario").child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String tipoUsuario = snapshot.child("tipoUsuario").getValue(String.class);
                            buttonLoginEntrar.setText(tipoUsuario);

                            if (Integer.parseInt(tipoUsuario) == 1){ //Tipo conductor
                                startActivity(new Intent(LoginActivity.this, PantallaMainConductorActivity.class));
                                finish();
                            }

                            else if(Integer.parseInt(tipoUsuario) == 2){ //Tipo pasajero

                                startActivity(new Intent(LoginActivity.this, PantallaMainPasajeroActivity.class));
                                finish();
                            }
                            buttonLoginEntrar.setText("hola");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //Fallo de lectura

                        }
                    });
*/
                }

                else{
                    //Contraseña incorrecta
                }
            }
        });


    }
}