package com.zentech.audibookfinalv;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    private Button button_sched, button_settings, button_sched2, button_settings2, maddnotebtn;

    private FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder> noteAdapter;

    RecyclerView mrecyclerview;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    ConstraintLayout nav,nav2, main, settings, sched;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
/////////////////////////////APP THEME///////////////////////////////////////////////////////////////////////////
        int value2 = AppCompatDelegate.MODE_NIGHT_NO;

        SharedPreferences mPrefs = getSharedPreferences("defaultNightMode", 0);
        value2 = mPrefs.getInt("defaultNightMode", value2);

        if (value2==2) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_Dark);
        }else {
            setTheme(R.style.Theme_Light);
        }
/////////////////////////////APP THEME///////////////////////////////////////////////////////////////////////////

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        settings = findViewById(R.id.activity_settings);
        sched = findViewById(R.id.activity_schedule);
        main = findViewById(R.id.main);
        nav = findViewById(R.id.navbar);
        nav2 = findViewById(R.id.navbar2);

/////////////////////////////NAV BAR///////////////////////////////////////////////////////////////////////////
        boolean valueNav= true;
        SharedPreferences sharedPreferencesNav = getSharedPreferences("isCheckedNav", 0);
        valueNav = sharedPreferencesNav.getBoolean("isCheckedNav", valueNav);

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
/////////////////////////////CREATE NOTE///////////////////////////////////////////////////////////////////////////

        //getSupportActionBar().setTitle("All Notes");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        maddnotebtn = findViewById(R.id.add_note_btn);
        maddnotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this,CreateNote.class));

            }
        });

        Query query = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection(
                "myNotes").orderBy("title",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<firebasemodel> allusernotes = new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query, firebasemodel.class).build();

        noteAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(allusernotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {

                ImageView popupbutton = noteViewHolder.itemView.findViewById(R.id.menupopupbtn);

                int colorcode = getRandomColor();
                noteViewHolder.mnote.setBackgroundColor(noteViewHolder.itemView.getResources().getColor(colorcode,null));

                noteViewHolder.notetitle.setText(firebasemodel.getTitle());
                noteViewHolder.notecontent.setText(firebasemodel.getContent());

                String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(v.getContext(),NoteDetails.class);
                        intent.putExtra("title",firebasemodel.getTitle());
                        intent.putExtra("content",firebasemodel.getContent());
                        intent.putExtra("noteId",docId);

                        v.getContext().startActivity(intent);

                        //Toast.makeText(getApplicationContext(), "This is Clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                popupbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu popupMenu = new PopupMenu(v.getContext(),v);
                        popupMenu.setGravity(Gravity.END);
                        popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                Intent intent = new Intent(v.getContext(),EditNoteActivity.class);
                                intent.putExtra("title",firebasemodel.getTitle());
                                intent.putExtra("content",firebasemodel.getContent());
                                intent.putExtra("noteId",docId);
                                v.getContext().startActivity(intent);

                                return false;
                            }
                        });

                        popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                //Toast.makeText(v.getContext(),"Deleted Successfully", Toast.LENGTH_SHORT).show();
                                DocumentReference documentReference = firebaseFirestore.collection("notes").document(firebaseUser.getUid())
                                        .collection("myNotes").document(docId);

                                documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(v.getContext(),"Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(v.getContext(),"Failed to Delete", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                return false;
                            }
                        });

                        popupMenu.show();

                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };

        mrecyclerview = findViewById(R.id.NotesRecyclerView);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview.setAdapter(noteAdapter);

/////////////////////////////NAV BAR///////////////////////////////////////////////////////////////////////////
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
    //Firestore Connection
    public class NoteViewHolder extends RecyclerView.ViewHolder
    {

        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            notetitle = itemView.findViewById(R.id.notetitle);
            notecontent = itemView.findViewById(R.id.notecontent);
            mnote = itemView.findViewById(R.id.note);

        }
    }

//GetRANDOM COLOR
    private int getRandomColor()
    {
        List<Integer> colorcode = new ArrayList<>();
        colorcode.add(R.color.Accent);

        Random random = new Random();
        int number = random.nextInt(colorcode.size());

        return colorcode.get(number);
    }


//MENU FOR NOTES//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

//LOGOUT//
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
//NOTE ADAPTER

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter != null)
        {
            noteAdapter.stopListening();
        }
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

