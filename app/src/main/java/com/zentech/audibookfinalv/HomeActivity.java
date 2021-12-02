package com.zentech.audibookfinalv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

public class HomeActivity extends AppCompatActivity {
    private Button button_sched, button_settings, button_sched2, button_settings2;

    ConstraintLayout nav,nav2, main, settings, sched;
    Switch aSwitch;

    @Override
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
        setContentView(R.layout.activity_home);
        settings = findViewById(R.id.activity_settings);
        sched = findViewById(R.id.activity_schedule);
        main = findViewById(R.id.main);
        nav = findViewById(R.id.navbar);
        nav2 = findViewById(R.id.navbar2);
        aSwitch = findViewById(R.id.switch1);

        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (aSwitch.isChecked()){
                nav.setVisibility(View.GONE);
                nav2.setVisibility(View.VISIBLE);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) main.getLayoutParams();
                params.setMarginStart(0);
                params.setMarginEnd(150);
                main.setLayoutParams(params);
            }else{
                nav.setVisibility(View.VISIBLE);
                nav2.setVisibility(View.GONE);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) main.getLayoutParams();
                params.setMarginStart(150);
                params.setMarginEnd(0);

                main.setLayoutParams(params);
            }
        });

        button_sched = (Button) findViewById(R.id.schedbttn);
        button_sched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleActivity();
            }
        });
        button_settings = (Button) findViewById(R.id.settingsbttn);
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity();
            }
        });
        button_sched2 = (Button) findViewById(R.id.schedbttn2);
        button_sched2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleActivity();
            }
        });
        button_settings2 = (Button) findViewById(R.id.settingsbttn2);
        button_settings2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity();
            }
        });
    }
    public void ScheduleActivity(){
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
    public void SettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

}

