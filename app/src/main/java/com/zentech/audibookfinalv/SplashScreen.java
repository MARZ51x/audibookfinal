package com.zentech.audibookfinalv;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
