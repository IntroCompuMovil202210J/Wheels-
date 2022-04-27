package com.example.wheelsplus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button buttonIniciarSesion, buttonRegistro;
    ConstraintLayout layout;

    FirebaseAuth auth;

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

        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();

        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        buttonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){
            updateUI();
        }
    }

    private void updateUI(){
        startActivity(new Intent(this, NavActivity.class));
    }

}