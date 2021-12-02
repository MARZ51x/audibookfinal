package com.zentech.audibookfinalv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SettingsActivity extends AppCompatActivity {
    //Initialize variable
    SwitchCompat switchCompat;
    private Button button_sched, button_home, button_sched2, button_home2;
    ConstraintLayout nav, nav2, main;
    Switch aSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        main = findViewById(R.id.main);
        nav = findViewById(R.id.navbar);
        nav2 = findViewById(R.id.navbar2);
        aSwitch = findViewById(R.id.switch1);
        switchCompat = (SwitchCompat) findViewById(R.id.bt_switch);

/////////////////////////////APP THEME///////////////////////////////////////////////////////////////////////////
        boolean value = true; // default value if no value was found
        int value2 = AppCompatDelegate.MODE_NIGHT_NO;
        final SharedPreferences sharedPreferences = getSharedPreferences("isChecked", 0);
        final SharedPreferences sharedPreferences2 = getSharedPreferences("defaultNightMode", 0);
        value = sharedPreferences.getBoolean("isChecked", value); // retrieve the value of your key
        value2 = sharedPreferences2.getInt("defaultNightMode", value2);
        switchCompat.setChecked(value);
        AppCompatDelegate.setDefaultNightMode(value2);

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sharedPreferences.edit().putBoolean("isChecked", true).apply();
                    sharedPreferences2.edit().putInt("defaultNightMode",AppCompatDelegate.MODE_NIGHT_YES).apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else {
                    sharedPreferences.edit().putBoolean("isChecked", false).apply();
                    sharedPreferences2.edit().putInt("defaultNightMode",AppCompatDelegate.MODE_NIGHT_NO).apply();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
/////////////////////////////APP THEME///////////////////////////////////////////////////////////////////////////


/////////////////////////////NAV BAR///////////////////////////////////////////////////////////////////////////
        boolean valueNav = true;
        final SharedPreferences sharedPreferencesNav = getSharedPreferences("isCheckedNav", 0);
        valueNav = sharedPreferencesNav.getBoolean("isCheckedNav", valueNav); // retrieve the value of your key
        aSwitch.setChecked(valueNav);

        if (valueNav) {
            nav.setVisibility(View.GONE);
            nav2.setVisibility(View.VISIBLE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) main.getLayoutParams();
            params.setMarginStart(0);
            params.setMarginEnd(150);
            main.setLayoutParams(params);
        } else {
            nav.setVisibility(View.VISIBLE);
            nav2.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) main.getLayoutParams();
            params.setMarginStart(150);
            params.setMarginEnd(0);
            main.setLayoutParams(params);

        }

        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (aSwitch.isChecked()) {
                sharedPreferencesNav.edit().putBoolean("isCheckedNav", true).apply();
                nav.setVisibility(View.GONE);
                nav2.setVisibility(View.VISIBLE);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) main.getLayoutParams();
                params.setMarginStart(0);
                params.setMarginEnd(150);
                main.setLayoutParams(params);
            } else {
                sharedPreferencesNav.edit().putBoolean("isCheckedNav", false).apply();
                nav.setVisibility(View.VISIBLE);
                nav2.setVisibility(View.GONE);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) main.getLayoutParams();
                params.setMarginStart(150);
                params.setMarginEnd(0);
                main.setLayoutParams(params);
            }
        });
/////////////////////////////NAV BAR///////////////////////////////////////////////////////////////////////////

        button_sched = (Button) findViewById(R.id.schedbttn);
        button_sched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleActivity();
            }
        });
        button_home = (Button) findViewById(R.id.homebttn);
        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity();
            }
        });
        button_sched2 = (Button) findViewById(R.id.schedbttn2);
        button_sched2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleActivity();
            }
        });
        button_home2 = (Button) findViewById(R.id.homebttn2);
        button_home2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity();
            }
        });
    }

    public void ScheduleActivity() {
        Intent intent = new Intent(this, ScheduleActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }

    public void HomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}

