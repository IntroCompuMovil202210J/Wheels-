package com.example.wheelsplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class VehicleRegisterActivity extends AppCompatActivity {

    Button buttonVehicleRegister;
    TextInputEditText editPlaca, editCapacidad, editColor, editMarca, editModelo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_vehicle_register);

        buttonVehicleRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = true;
                if(TextUtils.isEmpty(editPlaca.getText())){
                    editPlaca.setError("Este campo no puede estar vacío");
                    flag = false;
                }
                if(TextUtils.isEmpty(editCapacidad.getText())){
                    editCapacidad.setError("Este campo no puede estar vacío");
                    flag = false;
                }
                if(TextUtils.isEmpty(editColor.getText())){
                    editColor.setError("Este campo no puede estar vacío");
                    flag = false;
                }
                if(TextUtils.isEmpty(editMarca.getText())){
                    editMarca.setError("Este campo no puede estar vacío");
                    flag = false;
                }
                if(TextUtils.isEmpty(editModelo.getText())){
                    editModelo.setError("Este campo no puede estar vacío");
                    flag = false;
                }
                if(flag){
                    registerVehicle();
                }
            }
        });

    }

    private void registerVehicle(){

    }

}