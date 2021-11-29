package com.zentech.audibookfinalv;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    Button  schedule_btn, settings_btn, notes_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        schedule_btn = findViewById(R.id.schedbttn);
        settings_btn = findViewById(R.id.settingsbttn);
        notes_btn = findViewById(R.id.add_note_btn);
    }
}
