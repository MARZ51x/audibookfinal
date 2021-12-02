package com.zentech.audibookfinalv;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText mforgotpassword;
    private Button mpasswordrecoverbutton;
    private TextView mgobacktologin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        setContentView(R.layout.activity_forgot_password);

        //getSupportActionBar().hide();

        mforgotpassword=findViewById(R.id.forgotpassword);
        mpasswordrecoverbutton=findViewById(R.id.passwordrecoverbutton);
        mgobacktologin=findViewById(R.id.gobacktologin);

        firebaseAuth = FirebaseAuth.getInstance();

        mgobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mpasswordrecoverbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mforgotpassword.getText().toString().trim();
                if(mail.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter your mail first",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //we have to send mail password recover email
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Mail Sent! You can now recover your Account",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this,MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"E-mail is Invalid or Account NOT Exist",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}