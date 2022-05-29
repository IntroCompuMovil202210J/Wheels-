package com.example.wheelsplus;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    Button buttonIniciarSesion, buttonRegistro;
    ConstraintLayout layout;

    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        buttonIniciarSesion = findViewById(R.id.buttonIniciarSesión);
        buttonRegistro = findViewById(R.id.buttonRegistro);

        layout = findViewById(R.id.homeLayout);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference();

        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();

        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){
            myRef.child(RegisterActivity.FB_FINGERPRINT_PATH).child(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if(task.isSuccessful()){
                        boolean b = (boolean) task.getResult().getValue();
                        if(b){
                            Executor executor = ContextCompat.getMainExecutor(MainActivity.this);

                            biometricPrompt=new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                                @Override
                                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                    super.onAuthenticationError(errorCode, errString);
                                }

                                @Override
                                public void onAuthenticationSucceeded(@NonNull androidx.biometric.BiometricPrompt.AuthenticationResult result) {
                                    super.onAuthenticationSucceeded(result);
                                    Toast.makeText(getBaseContext(), "Bienvenido de nuevo", Toast.LENGTH_SHORT).show();
                                    updateUI();
                                }

                                @Override
                                public void onAuthenticationFailed() {
                                    super.onAuthenticationFailed();
                                }
                            });

                            promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("WheelsPlus").setDescription("Usa tu huella para ingresar").setDeviceCredentialAllowed(true).build();
                            biometricPrompt.authenticate(promptInfo);
                        }else{
                            updateUI();
                        }
                    }
                }
            });
        }
    }

    private void updateUI(){
        startActivity(new Intent(this, NavActivity.class));
        onStop();
    }


    @Override
    public void onBackPressed() {

        if (contador == 0){
            Toast.makeText(getApplicationContext(), "Presione nuevamente para salir", Toast.LENGTH_SHORT).show();
            contador++;
        }

        else super.onBackPressed();


        new CountDownTimer(3000,1000){

            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                contador = 0;
            }
        }.start();
    }

}