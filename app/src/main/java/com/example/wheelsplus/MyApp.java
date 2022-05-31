package com.example.wheelsplus;

import android.app.Application;
import android.content.Intent;

import boot.BackgroundBootService;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(MyApp.this, BackgroundBootService.class));
    }

}
