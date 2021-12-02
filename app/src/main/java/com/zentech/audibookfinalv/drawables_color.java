package com.zentech.audibookfinalv;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class drawables_color extends AppCompatActivity {

    @SuppressLint("ResourceType")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Check condition
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            //When night mode is equal to yes
            //set dark theme
            setTheme(R.style.Theme_Dark);
        }else {
            //When night mode is equal to no
            //Set light theme
            setTheme(R.style.Theme_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.drawable.background_rounded_text_fields);
    }

}
