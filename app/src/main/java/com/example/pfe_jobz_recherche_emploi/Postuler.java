package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Postuler extends AppCompatActivity {

    private Button  valider;
    private TextView cvorupload, lettreFileName, poste, entreprise, lieu;
    private Toolbar toolbar;
    private static final int PICKFILE_CV_RESULT_CODE= 1, PICKFILE_LETTRE_RESULT_CODE = 2;
    private int resultSet;
    private byte[] CVfileBytes, LETTREfileBytes;
    private RelativeLayout chargerCV;
    private ImageButton upload, close;
    private boolean cvAlready = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postuler);

        String[] permissionsStorage = {Manifest.permission.READ_EXTERNAL_STORAGE};
        int requestExternalStorage = 1;

        int permission = ActivityCompat.checkSelfPermission(Postuler.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Postuler.this, permissionsStorage, requestExternalStorage);

        }

        chargerCV = (RelativeLayout) findViewById(R.id.postuler_charger_cv);

        valider = findViewById(R.id.postuler_valider);

        cvorupload = findViewById(R.id.title_cv_or_upload);

        upload = findViewById(R.id.postuler_upload);
        close = findViewById(R.id.postuler_enlever_cv);

        poste = findViewById(R.id.postuler_nom_poste);
        entreprise = findViewById(R.id.postuler_entreprise);
        lieu = findViewById(R.id.postuler_localisation);

        poste.setText(getIntent().getStringExtra("Titre"));
        entreprise.setText(getIntent().getStringExtra("Entreprise"));
        lieu.setText(getIntent().getStringExtra("Lieu"));


        toolbar = findViewById(R.id.postuler_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection!=null){
            try{
                String checkIfCvPostedSQL = "SELECT ID_CV, CV_fichier, CV_nom_fichier FROM CV WHERE ID_Candidat = "+getIntent().getStringExtra("ID_Candidat")+";";
                Statement statement = connection.createStatement();
                ResultSet resultSet1 = statement.executeQuery(checkIfCvPostedSQL);

                if (resultSet1.next()){
                    cvorupload.setEnabled(false);
                    upload.setVisibility(View.INVISIBLE);
                    close.setVisibility(View.VISIBLE);
                    cvorupload.setText(resultSet1.getString("CV_nom_fichier"));
                    CVfileBytes = resultSet1.getBytes("CV_fichier");
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvAlready = false;
                upload.setVisibility(View.VISIBLE);
                close.setVisibility(View.GONE);
                cvorupload.setEnabled(true);
                cvorupload.setText("Télécharger le fichier \n (.doc, .docx, .pdf.)");
                CVfileBytes = null;
            }
        });




            cvorupload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, PICKFILE_CV_RESULT_CODE);

                }
            });





        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connection connection = new ___ConnectionClass().SQLServerConnection();
                if(connection!=null){
                    try{
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        String insertSQL = "INSERT INTO Candidature VALUES ('"+getIntent().getStringExtra("ID_Candidat")+"','"+getIntent().getStringExtra("ID_Offre")+"',?,'"+currentDate+"');";
                        PreparedStatement statement = connection.prepareStatement(insertSQL);
                        statement.setBytes(1, CVfileBytes);
                        resultSet = statement.executeUpdate();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if(resultSet>0){
                        Snackbar snackbar = Snackbar.make(view, "Candidature transmise avec succès !", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Postuler.this, HomeCand.class);
                                startActivity(intent);
                            }
                        });
                        snackbar.show();
                    }
                }
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_CV_RESULT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                // Get the file name from the content URI
                String fileName = null;
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    fileName = cursor.getString(nameIndex);
                    cvorupload.setText(fileName);
                    cvorupload.setTextColor(Color.parseColor("#0CAA41"));
                    cursor.close();
                }


                // Get the file data as a byte array
                InputStream inputStream = getContentResolver().openInputStream(uri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                CVfileBytes = outputStream.toByteArray();
                inputStream.close();
                outputStream.close();

                // You now have the file name and data as a byte array (fileName, fileBytes)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

                // Get the file data as a byte arr
        }
    }