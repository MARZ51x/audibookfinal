package com.zentech.audibookfinalv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class CreateNote extends AppCompatActivity {

    private EditText mcreatetitleofnote, mcreatecontentofnote;
    private ImageButton msavenote, backbttn;
    private ImageView micBtn;
    private GifImageView audioGif;
    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    ///////////////////for pdf///////////////////////
    private static final String TAG = "PdfCreatorActivity";
    private ImageView generatePDF;
    private File pdfFile;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    ///////////////////for pdf///////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        msavenote = findViewById(R.id.savenote);
        mcreatetitleofnote = findViewById(R.id.createtitleofnote);
        mcreatecontentofnote = findViewById(R.id.createcontentofnote);

        //////////////////pdf////////////////////
        generatePDF = findViewById(R.id.imagePDF);
        generatePDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcreatetitleofnote.getText().toString().isEmpty()){
                    mcreatetitleofnote.setError("Please input title");
                    mcreatetitleofnote.requestFocus();
                    return;
                }
                try {
                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
        /////////////////pdf/////////////////////

        View someView = findViewById(R.id.createNote);
        View root = someView.getRootView();
        if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.AppTheme);
            root.setBackgroundColor(Color.parseColor("#FF001219"));
            mcreatetitleofnote.setTextColor(ContextCompat.getColor(this, R.color.White));
        }else {
            setTheme(R.style.Theme_Light);
            root.setBackgroundColor(getResources().getColor(android.R.color.white));
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            checkPermission();
        }

        audioGif = findViewById(R.id.wave);
        micBtn = findViewById(R.id.imageSpeak);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        audioGif.setVisibility(View.INVISIBLE);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        backbttn = findViewById(R.id.backbttnC);

        backbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackButton();
            }
        });


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

        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                audioGif.setVisibility(View.VISIBLE);
                micBtn.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                audioGif.setVisibility(View.INVISIBLE);
                micBtn.setVisibility(View.VISIBLE);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                mcreatecontentofnote.append(data.get(0));
                mcreatecontentofnote.append(". ");
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });

        micBtn.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });

    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    public void BackButton(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0,0);
        finish();
    }
    //////////////////////pdf/////////////////////
    private void createPdfWrapper() throws FileNotFoundException,DocumentException{
        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            createPdf();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(this, "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Exit", null)
                .create()
                .show();
    }
    private void createPdf() throws FileNotFoundException, DocumentException {
        File docsFolder = new File(Environment.getExternalStorageDirectory() +
                "/Documents");

        String title=mcreatetitleofnote.getText().toString();
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Create New PDF Folder");
        }

        Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD
                | Font.UNDERLINE, BaseColor.BLACK);
        Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.GRAY);

        Paragraph prTitle = new Paragraph();
        prTitle.setFont(titleFont);
        prTitle.add(mcreatetitleofnote.getText().toString());

        Paragraph prText = new Paragraph();
        prText.setFont(smallBold);
        prText.add(mcreatecontentofnote.getText().toString());

        int num=0;
        String save = title+".pdf";

        pdfFile = new File(docsFolder.getAbsolutePath(),save);
        while(pdfFile.exists()) {
            save = title+ (num++) +".pdf";
            pdfFile = new File(docsFolder.getAbsolutePath(), save);
        }
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        //document.add(new Paragraph(inputNoteTitle.getText().toString()));
        //document.add(new Paragraph(inputNoteSubtitle.getText().toString()));
        //document.add(new Paragraph(inputNoteText.getText().toString()));

        document.add(prTitle);
        document.add(new Paragraph("\n\n"));
        document.add(prText);
        document.close();
        previewPdf();
    }
    private void previewPdf() {
        PackageManager packageManager = getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            startActivity(intent);
        }else{
            Toast.makeText(this,"PDF generated",Toast.LENGTH_SHORT).show();
        }
    }
    /////////////////////pdf//////////////////////

}