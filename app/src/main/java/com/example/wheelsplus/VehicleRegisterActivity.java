package com.example.wheelsplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.Vehiculo;

public class VehicleRegisterActivity extends AppCompatActivity {

    public static final String FB_VEHICLES_PATH = "vehicles/";

    Button buttonVehicleRegister;
    TextInputEditText editPlaca, editCapacidad, editModelo, editMarca, editAnno;

    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    String urlAux = "N/A";
    Vehiculo vehiculo;

    RequestQueue requestQueue;

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

        requestQueue = Volley.newRequestQueue(VehicleRegisterActivity.this);

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

                if(flag)
                    requestUrl();
            }
        });
    }

    public void requestUrl(){

        String url="https://my-json-server.typicode.com/galavism/carros/cars";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                int tam = response.length();
                for (int i=0; i<tam; i++){
                    try {
                        JSONObject object = new JSONObject(response.get(i).toString());
                        String marca = object.getString("maker");
                        String modelo = object.getString("model");
                        String urlA = object.getString("imgURL");

                        if(marca.equals(editMarca.getText().toString()) && modelo.equals(editModelo.getText().toString())){
                            urlAux = urlA;
                            break;
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                String key = myRef.push().getKey();
                myRef = database.getReference(FB_VEHICLES_PATH + auth.getCurrentUser().getUid()).child(key);
                vehiculo = new Vehiculo(key, editPlaca.getText().toString(), Integer.parseInt(editCapacidad.getText().toString()), editMarca.getText().toString(), editModelo.getText().toString(), Integer.parseInt(editAnno.getText().toString()));

                if(!urlAux.equals("N/A")){
                    vehiculo.setUrlImagen(urlAux);
                    vehiculo.setUrlImagen(urlAux);
                    myRef.setValue(vehiculo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(VehicleRegisterActivity.this, "Vehiculo añadido con exito", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), DriverNavActivity.class));
                        }
                    });
                }

                else{
                    vehiculo.setUrlImagen(urlAux);

                    myRef.setValue(vehiculo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                                Toast.makeText(VehicleRegisterActivity.this, "Vehiculo añadido con exito", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getApplicationContext(), DriverNavActivity.class));
                        }
                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError)
                    Log.i("TAG","SERVER ERROR");

                if(error instanceof NoConnectionError)
                    Log.i("TAG","There is no internet conecction");

                if(error instanceof NetworkError)
                    Log.i("TAG","Network");

                Toast.makeText(VehicleRegisterActivity.this, "FSDFSDFSf", Toast.LENGTH_LONG).show();

                String key = myRef.push().getKey();
                myRef = database.getReference(FB_VEHICLES_PATH + auth.getCurrentUser().getUid()).child(key);
                vehiculo = new Vehiculo(key, editPlaca.getText().toString(), Integer.parseInt(editCapacidad.getText().toString()), editMarca.getText().toString(), editModelo.getText().toString(), Integer.parseInt(editAnno.getText().toString()));

                vehiculo.setUrlImagen("N/A");
                myRef.setValue(vehiculo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                            Toast.makeText(VehicleRegisterActivity.this, "Vehiculo añadido con exito", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(), DriverNavActivity.class));
                    }
                });


            }
        });

        requestQueue.add(request);
    }
}