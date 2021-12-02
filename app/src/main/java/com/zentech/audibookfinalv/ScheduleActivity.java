package com.zentech.audibookfinalv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import static com.zentech.audibookfinalv.ui.AddEditAlarmActivity.ADD_ALARM;
import static com.zentech.audibookfinalv.ui.AddEditAlarmActivity.buildAddEditAlarmActivityIntent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zentech.audibookfinalv.util.AlarmUtils;

public class ScheduleActivity extends AppCompatActivity {
    private Button button_home, button_settings, button_home2, button_settings2;

    private Button button;
    ConstraintLayout nav,nav2, main;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
/////////////////////////////APP THEME///////////////////////////////////////////////////////////////////////////
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
/////////////////////////////APP THEME///////////////////////////////////////////////////////////////////////////

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        main = findViewById(R.id.main);
        nav = findViewById(R.id.navbar);
        nav2 = findViewById(R.id.navbar2);

        final FloatingActionButton fab = findViewById(R.id.fab2);
        fab.setOnClickListener((View view) -> {
            AlarmUtils.checkAlarmPermissions(this);
            Intent intent =new Intent(buildAddEditAlarmActivityIntent(this, ADD_ALARM));
            startActivity(intent);
            overridePendingTransition(0, 0);

        });

/////////////////////////////NAV BAR///////////////////////////////////////////////////////////////////////////
        boolean valueNav= true;
        SharedPreferences sharedPreferencesNav = getSharedPreferences("isCheckedNav", 0);
        valueNav = sharedPreferencesNav.getBoolean("isCheckedNav", valueNav);

        if (valueNav==true) {
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
/////////////////////////////NAV BAR///////////////////////////////////////////////////////////////////////////

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

