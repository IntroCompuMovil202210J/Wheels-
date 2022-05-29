package com.example.wheelsplus;

import static com.google.android.material.textfield.TextInputLayout.END_ICON_NONE;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
    TextView forgotPasswd;
    TextInputLayout textInputLayoutPassword;

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
        forgotPasswd = findViewById(R.id.tvForgotPasswd);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPas);

        auth = FirebaseAuth.getInstance();

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailValid = validateEmail(editMailLogin.getText().toString());
                if(!emailValid)
                    editMailLogin.setError("Correo invalido");


                psswdValid = validatePsswd(editPsswdLogin.getText().toString());
                if(!psswdValid){

                    textInputLayoutPassword.setEndIconMode(END_ICON_NONE);
                    textInputLayoutPassword.setPasswordVisibilityToggleEnabled(false);

                    if(editPsswdLogin.getText().toString().length() < 6)
                        editPsswdLogin.setError("La contraseña debe ser mayor a 6 caracteres");

                    else if(editPsswdLogin.getText().toString().length() > 12)
                        editPsswdLogin.setError("La contraseña debe ser menor a 12 caracteres");

                    else
                        editPsswdLogin.setError("Su contraseña debe tener al menos un número");
                }


                if (emailValid & psswdValid)
                    authenticateWithFB();
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
                textInputLayoutPassword.setPasswordVisibilityToggleEnabled(true);
            }
        });

        forgotPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ForgotPasswdActivity.class);
                emailValid = validateEmail(editMailLogin.getText().toString());

                if(emailValid){
                    intent.putExtra("email", editMailLogin.getText().toString());
                    startActivity(intent);
                }
                else{
                    editMailLogin.setError("Ingrese un correo valido");
                    Toast.makeText(getApplicationContext(), "Ingrese su correo para continuar", Toast.LENGTH_SHORT).show();
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
        if(emailValid && psswdValid){
            auth.signInWithEmailAndPassword(mail, psswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        updateUI();
                    }else{
                        String message = task.getException().getMessage();
                        Log.i("Login", message);
                        Toast.makeText(LoginActivity.this, "Ingreso invalido, intente de nuevo", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void updateUI(){
        startActivity(new Intent(this, NavActivity.class));
    }

}