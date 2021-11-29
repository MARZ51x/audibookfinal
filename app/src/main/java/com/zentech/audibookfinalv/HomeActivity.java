package com.zentech.audibookfinalv;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class HomeActivity extends AppCompatActivity {
    ConstraintLayout nav,nav2, main;
    Switch aSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

    }
}

