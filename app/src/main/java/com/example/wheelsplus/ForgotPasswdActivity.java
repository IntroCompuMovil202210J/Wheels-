package com.example.wheelsplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswdActivity extends AppCompatActivity {

    Button buttonRecover;
    TextInputEditText editFPmail;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot_passwd);

        String mail = getIntent().getStringExtra("email");

        buttonRecover = findViewById(R.id.buttonRecover);
        editFPmail = findViewById(R.id.editFPmail);

        auth = FirebaseAuth.getInstance();

        if(mail != null){
            editFPmail.setText(mail);
        }

        buttonRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgotPasswrd();
            }
        });

    }

    private void forgotPasswrd(){
        auth.sendPasswordResetEmail(editFPmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPasswdActivity.this, "Correo de recuperación enviado", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ForgotPasswdActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

}