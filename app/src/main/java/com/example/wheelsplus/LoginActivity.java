package com.example.wheelsplus;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static final Pattern VALID_PSSWD_REGEX = Pattern.compile("^(?i)(?=.*[a-z])(?=.*[0-9])[a-z0-9#.!@$*&_]{6,12}$", Pattern.CASE_INSENSITIVE);
    boolean emailValid, psswdValid;

    Button buttonSignIn;
    TextInputEditText editMailLogin, editPsswdLogin;

    ConstraintLayout layout;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        layout = findViewById(R.id.loginLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) layout.getBackground();
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        buttonSignIn = findViewById(R.id.buttonSignIn);
        editMailLogin = findViewById(R.id.editEmailLogin);
        editPsswdLogin = findViewById(R.id.editPsswdLogin);

        auth = FirebaseAuth.getInstance();

        //Pruebita
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(view.getContext(), NavActivity.class));
                authenticateWithFB();
            }
        });

        editMailLogin.addTextChangedListener(new TextWatcher() {
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
                    editMailLogin.setError("Correo invalido");
                }
            }
        });

        editPsswdLogin.addTextChangedListener(new TextWatcher() {
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
                        editPsswdLogin.setError("La contraseña debe ser mayor a 6 caracteres");
                    }else if(editable.toString().length() > 12){
                        editPsswdLogin.setError("La contraseña debe ser menor a 12 caracteres");
                    }else{
                        editPsswdLogin.setError("Su contraseña debe tener al menos un número");
                    }
                }
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

    private boolean validateEmail(String email){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }

    private boolean validatePsswd(String psswd){
        Matcher matcher = VALID_PSSWD_REGEX.matcher(psswd);
        return matcher.find();
    }

    private void authenticateWithFB(){
        String mail = editMailLogin.getText().toString();
        String psswd = editPsswdLogin.getText().toString();
        if(validateForm(mail, psswd)){
            auth.signInWithEmailAndPassword(mail, psswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        updateUI();
                    }else{
                        String message = task.getException().getMessage();
                        Log.i("FirebaseTest", message);
                    }
                }
            });
        }
    }

    private boolean validateForm(String email, String password){
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.find();
    }

    private void updateUI(){
        startActivity(new Intent(this, NavActivity.class));
    }

}