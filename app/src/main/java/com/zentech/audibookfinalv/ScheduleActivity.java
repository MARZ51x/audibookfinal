package com.zentech.audibookfinalv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ScheduleActivity extends AppCompatActivity {
    private Button button_home, button_settings, button_home2, button_settings2;

    private Button button;
    ConstraintLayout nav,nav2, main;
    Switch aSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
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
        button_home = (Button) findViewById(R.id.homebttn);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity();
            }
        });
        button_settings = (Button) findViewById(R.id.settingsbttn);
        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity();
            }
        });
        button_home2 = (Button) findViewById(R.id.homebttn2);
        button_home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity();
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
    public void HomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    public void SettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}

