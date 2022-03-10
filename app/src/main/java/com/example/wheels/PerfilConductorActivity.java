package com.example.wheels;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class PerfilConductorActivity extends AppCompatActivity {

    ImageView imagePerfilConductor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_conductor);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imagePerfilConductor = findViewById(R.id.imagePerfilConductor);
        Glide.with(getBaseContext()).load("https://cdn.pixabay.com/photo/2016/11/29/13/14/attractive-1869761_960_720.jpg").apply(RequestOptions.circleCropTransform()).into(imagePerfilConductor);

    }
}