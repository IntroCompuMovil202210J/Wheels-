package com.example.wheelsplus;

import static com.google.android.material.textfield.TextInputLayout.END_ICON_NONE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Usuario;

public class RegisterActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PSSWD_REGEX = Pattern.compile("^(?i)(?=.*[a-z])(?=.*[0-9])[a-z0-9#.!@$*&_]{6,12}$", Pattern.CASE_INSENSITIVE);
    boolean emailValid, psswdValid;
    public static final String DEFAULT_PROFILE_PIC = "https://firebasestorage.googleapis.com/v0/b/wheelsplus-9b510.appspot.com/o/profilePics%2FdefaultProfilePic.png?alt=media&token=f578e2b6-53d3-424f-94fa-1aeaefb873fa";
    public static final String FB_USERS_PATH = "users/";
    public static final String FB_FINGERPRINT_PATH = "finger/";

    Button buttonRegister;
    TextInputEditText editMailRegister, editPsswdRegister, editPhoneRegister, editNameRegister, editLastnameRegister;
    SwitchMaterial driverSwitch;
    TextInputLayout textInputLayoutPasReg;

    FirebaseAuth auth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        buttonRegister = findViewById(R.id.buttonRegister);
        editMailRegister = findViewById(R.id.editMailRegister);
        editPsswdRegister = findViewById(R.id.editPsswdRegister);
        editPhoneRegister = findViewById(R.id.editTelefonoRegister);
        editNameRegister = findViewById(R.id.editNombreRegister);
        editLastnameRegister = findViewById(R.id.editLastnameRegister);
        textInputLayoutPasReg = findViewById(R.id.textInputLayoutPasReg);
        driverSwitch = findViewById(R.id.driverSwitch);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean dt = validarDatosPrincipales(editNameRegister.getText().toString(), editLastnameRegister.getText().toString(), editPhoneRegister.getText().toString());

                emailValid = validateEmail(editMailRegister.getText().toString());
                if(!emailValid)
                    editMailRegister.setError("Correo invalido");

                psswdValid = validatePsswd(editPsswdRegister.getText().toString());
                if(!psswdValid){

                    textInputLayoutPasReg.setEndIconMode(END_ICON_NONE);
                    textInputLayoutPasReg.setPasswordVisibilityToggleEnabled(false);

                    if(editPsswdRegister.getText().toString().length() < 6)
                        editPsswdRegister.setError("La contraseña debe ser mayor a 6 caracteres");

                    else if(editPsswdRegister.getText().toString().length() > 12)
                        editPsswdRegister.setError("La contraseña debe ser menor a 12 caracteres");

                    else
                        editPsswdRegister.setError("Su contraseña debe tener al menos un número");

                }

                if(dt && emailValid && psswdValid)
                    registerWithFB();
            }
        });


        editPsswdRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                textInputLayoutPasReg.setPasswordVisibilityToggleEnabled(true);
            }
        });

    }

    public boolean validarDatosPrincipales(String nombre, String apellido, String numero){

        boolean r = true;

        if (numero.length() != 10){
            editPhoneRegister.setError("Ingrese un numero telefonico valido");
            r = false;
        }

        return r;
    }

    public void registerWithFB(){
        String mail = editMailRegister.getText().toString();
        String psswd = editPsswdRegister.getText().toString();
        if(emailValid && psswdValid){
            auth.createUserWithEmailAndPassword(mail, psswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String name = editNameRegister.getText().toString();
                        String lastname = editLastnameRegister.getText().toString();
                        String phone = editPhoneRegister.getText().toString();
                        double latitude = 0;
                        double longitude = 0;
                        Usuario user = new Usuario(firebaseUser.getUid(), name, lastname, phone, DEFAULT_PROFILE_PIC, latitude, longitude);
                        myRef = database.getReference(FB_FINGERPRINT_PATH + auth.getCurrentUser().getUid());
                        myRef.setValue(false);
                        myRef = database.getReference(FB_USERS_PATH + firebaseUser.getUid());
                        myRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                                    upcrb.setDisplayName(name + " " + lastname);
                                    upcrb.setPhotoUri(Uri.parse(DEFAULT_PROFILE_PIC));
                                    firebaseUser.updateProfile(upcrb.build()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                if(driverSwitch.isChecked()){
                                                    updateUIDriver();
                                                }else{
                                                    updateUIPassenger();
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }else{
                        Log.i("FirebaseRegister", task.getException().getMessage());
                    }
                }
            });
        }
    }

    private boolean validateEmail(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    private boolean validatePsswd(String psswd){
        Matcher matcher = VALID_PSSWD_REGEX.matcher(psswd);
        return matcher.find();
    }

    private void updateUIDriver(){
        startActivity(new Intent(this, DriverNavActivity.class));
    }

    private void updateUIPassenger(){
        startActivity(new Intent(this, NavActivity.class));
    }

}