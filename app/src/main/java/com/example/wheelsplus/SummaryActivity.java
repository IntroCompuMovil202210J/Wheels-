package com.example.wheelsplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Grupo;
import model.Usuario;
import services.DownloadImageTask;

public class SummaryActivity extends AppCompatActivity {

    TextView tvSummaryTime, tvSummaryDistance, tvSummaryFee;
    Button buttonEndPassengerTrip;
    RatingBar ratingDriver;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    public static final String FB_USERS_PATH = "users/";
    public static final String FB_TRIPS_PATH = "trips/";
    float rating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_summary);

        String time = getIntent().getStringExtra("timeTrip");
        String distance = getIntent().getStringExtra("distanceTrip");
        Grupo grupo = PassengerTripActivity.intentGroup;

        tvSummaryTime = findViewById(R.id.tvSummaryTime);
        tvSummaryDistance = findViewById(R.id.tvSummaryDistance);
        tvSummaryFee = findViewById(R.id.tvSummaryFee);
        buttonEndPassengerTrip = findViewById(R.id.buttonEndPassengerTrip);
        ratingDriver = findViewById(R.id.ratingSummary);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(FB_USERS_PATH + grupo.getIdConductor());
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    Usuario conductor = task.getResult().getValue(Usuario.class);
                    new DownloadImageTask((CircleImageView) findViewById(R.id.profilePicSummary))
                            .execute(conductor.getUrlFoto());
                }
            }
        });

        tvSummaryTime.setText(time);
        tvSummaryDistance.setText(distance);
        tvSummaryFee.setText(String.valueOf(grupo.getTarifa()));

        ratingDriver.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
            }
        });

        buttonEndPassengerTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef = database.getReference(FB_TRIPS_PATH + auth.getCurrentUser().getUid());
                myRef.push().setValue(grupo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        myRef = database.getReference(FB_TRIPS_PATH + grupo.getIdConductor());
                        myRef.push().setValue(grupo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(view.getContext(), NavActivity.class));
                            }
                        });
                    }
                });
            }
        });

    }
}