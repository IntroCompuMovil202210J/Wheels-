package com.example.wheels;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PerfilPasajeroActivity extends AppCompatActivity {

    ImageView imagePerfilPasajero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pasajero);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imagePerfilPasajero = findViewById(R.id.imagePerfilPasajero);
        Glide.with(getBaseContext()).load("https://cdn.pixabay.com/photo/2016/11/18/19/07/happy-1836445_960_720.jpg").apply(RequestOptions.circleCropTransform()).into(imagePerfilPasajero);


    }
}