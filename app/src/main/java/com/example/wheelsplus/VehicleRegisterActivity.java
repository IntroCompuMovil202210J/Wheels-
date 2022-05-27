package com.example.wheelsplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import model.Vehiculo;

public class VehicleRegisterActivity extends AppCompatActivity {

    public static final String FB_VEHICLES_PATH = "vehicles/";

    Button buttonVehicleRegister;
    TextInputEditText editPlaca, editCapacidad, editModelo, editMarca, editAnno;

    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_vehicle_register);

        buttonVehicleRegister = findViewById(R.id.buttonVehicleRegister);
        editPlaca = findViewById(R.id.editPlaca);
        editCapacidad = findViewById(R.id.editCapacidad);
        editModelo = findViewById(R.id.editModelo);
        editMarca = findViewById(R.id.editMarca);
        editAnno = findViewById(R.id.editAnno);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        myRef = database.getReference(FB_VEHICLES_PATH + auth.getCurrentUser().getUid());
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.getResult().exists()){
                    new MaterialAlertDialogBuilder(VehicleRegisterActivity.this)
                            .setTitle("Bienvenido al modo conductor")
                            .setNegativeButton("Volver al modo pasajero", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplicationContext(), NavActivity.class);
                                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            })
                            .setMessage("Hola, bienvenido al modo conductor, mediante este podrás ofrecer el servicio de Wheels, pero antes, debes registrar un vehiculo. ")
                            .setPositiveButton("Entendido", null)
                            .show();
                }
            }
        });

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
                if(TextUtils.isEmpty(editModelo.getText())){
                    editModelo.setError("Este campo no puede estar vacío");
                    flag = false;
                }
                if(TextUtils.isEmpty(editMarca.getText())){
                    editMarca.setError("Este campo no puede estar vacío");
                    flag = false;
                }
                if(TextUtils.isEmpty(editAnno.getText())){
                    editAnno.setError("Este campo no puede estar vacío");
                    flag = false;
                }
                if(flag){
                    String key = myRef.push().getKey();
                    myRef = database.getReference(FB_VEHICLES_PATH + auth.getCurrentUser().getUid()).child(key);
                    Vehiculo vehiculo = new Vehiculo(key, editPlaca.getText().toString(), Integer.parseInt(editCapacidad.getText().toString()), editMarca.getText().toString(), editModelo.getText().toString(), Integer.parseInt(editAnno.getText().toString()));
                    myRef.setValue(vehiculo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(), DriverNavActivity.class));
                            }
                        }
                    });
                }
            }
        });

    }

}