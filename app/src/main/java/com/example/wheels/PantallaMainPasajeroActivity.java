package com.example.wheels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PantallaMainPasajeroActivity extends AppCompatActivity{

    ImageButton imageGruposP;
    ImageButton imagePerfilP;
    ImageButton imageBuscarGrupoP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_main_pasajero);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imageGruposP = findViewById(R.id.imageGruposP);
        imagePerfilP = findViewById(R.id.imagePerfilP);
        imageBuscarGrupoP = findViewById(R.id.imageBuscarGrupoP);


        imageGruposP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaMainPasajeroActivity.this, GruposActivity.class));
            }
        });

        imagePerfilP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaMainPasajeroActivity.this, PerfilPasajeroActivity.class));
            }
        });

        imageBuscarGrupoP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PantallaMainPasajeroActivity.this, BuscarGrupoPActivity.class));
            }
        });
    }
}