package com.zentech.audibookfinalv;

import android.content.Intent;
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
        setContentView(R.layout.activity_settings);
        main = findViewById(R.id.main);
        nav = findViewById(R.id.navbar);
        nav2 = findViewById(R.id.navbar2);
        aSwitch = findViewById(R.id.switch1);

        //Assign Variable
        switchCompat =findViewById(R.id.bt_switch);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Check condition
                if(isChecked){
                    //when swtich button is checked
                    // set night mde
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else{
                    //when switch button is checked
                    //set light mode
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });



        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (aSwitch.isChecked()) {
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
        });
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
    }

    public void HomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}

