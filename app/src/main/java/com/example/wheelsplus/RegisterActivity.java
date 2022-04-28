package com.example.wheelsplus;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PSSWD_REGEX = Pattern.compile("^(?i)(?=.*[a-z])(?=.*[0-9])[a-z0-9#.!@$*&_]{6,12}$", Pattern.CASE_INSENSITIVE);
    boolean emailValid, psswdValid;
    public static final String DEFAULT_PROFILE_PIC = "https://firebasestorage.googleapis.com/v0/b/wheelsplus-9b510.appspot.com/o/profilePics%2FdefaultProfilePic.png?alt=media&token=c8c71720-fd0b-402e-9b0f-e0317148efef";

    Button buttonRegister;
    TextInputEditText editMailRegister, editPsswdRegister, editPhoneRegister, editFullNameRegister;

    FirebaseAuth auth;

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
        editFullNameRegister = findViewById(R.id.editNombreRegister);

        auth = FirebaseAuth.getInstance();

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerWithFB();
            }
        });

        editMailRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                emailValid = validateEmail(editable.toString());
                if(!emailValid){
                    editMailRegister.setError("Correo invalido");
                }
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
                psswdValid = validatePsswd(editable.toString());
                if(!psswdValid){
                    if(editable.toString().length() < 6){
                        editPsswdRegister.setError("La contraseña debe ser mayor a 6 caracteres");
                    }else if(editable.toString().length() > 12){
                        editPsswdRegister.setError("La contraseña debe ser menor a 12 caracteres");
                    }else{
                        editPsswdRegister.setError("Su contraseña debe tener al menos un número");
                    }
                }
            }
        });

    }

    public void registerWithFB(){
        String mail = editMailRegister.getText().toString();
        String psswd = editPsswdRegister.getText().toString();
        if(emailValid && psswdValid){
            auth.createUserWithEmailAndPassword(mail, psswd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        if(user != null){ //Update user Info
                            UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                            upcrb.setDisplayName(editFullNameRegister.getText().toString());
                            upcrb.setPhotoUri(Uri.parse(DEFAULT_PROFILE_PIC));
                            user.updateProfile(upcrb.build());
                            updateUI();
                        }
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

    private void updateUI(){
        startActivity(new Intent(this, NavActivity.class));
    }

}