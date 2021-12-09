package com.zentech.audibookfinalv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    private EditText mcreatetitleofnote, mcreatecontentofnote;
    private ImageButton msavenote, backbttn;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        msavenote = findViewById(R.id.savenote);
        mcreatetitleofnote = findViewById(R.id.createtitleofnote);
        mcreatecontentofnote = findViewById(R.id.createcontentofnote);
        backbttn = findViewById(R.id.backbttn);
        backbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackActivity();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        msavenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mcreatetitleofnote.getText().toString();
                String content = mcreatecontentofnote.getText().toString();
                if(title.isEmpty() || content.isEmpty())
                {

                    Toast.makeText(getApplicationContext(),"Both fields are required.",Toast.LENGTH_SHORT).show();

                }
                else
                {

                    DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid())
                            .collection("myNotes").document();
                    Map<String, Object> note = new HashMap<>();
                    note.put("title",title);
                    note.put("content",content);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Note Created Successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateNote.this,HomeActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed to Create Note",Toast.LENGTH_SHORT).show();
                           // startActivity(new Intent(CreateNote.this,HomeActivity.class));
                        }
                    });

                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
    public void BackActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
}