package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Objects;

public class Postuler extends AppCompatActivity {

    private Button chargerCV, chargerLettre, valider;
    private TextView cvFileName, lettreFileName;
    private Toolbar toolbar;
    private static final int PICKFILE_CV_RESULT_CODE= 1, PICKFILE_LETTRE_RESULT_CODE = 2;
    private int resultSet;
    private byte[] CVfileBytes, LETTREfileBytes;

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


        chargerCV = findViewById(R.id.postuler_charger_cv);
        chargerLettre = findViewById(R.id.postuler_charger_lettre);
        valider = findViewById(R.id.postuler_valider);

        cvFileName = findViewById(R.id.cv_file_name);
        lettreFileName = findViewById(R.id.lettre_file_name);

        toolbar = findViewById(R.id.postuler_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        chargerCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, PICKFILE_CV_RESULT_CODE);

            }
        });

        chargerLettre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_LETTRE_RESULT_CODE);
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connection connection = new ___ConnectionClass().connectionClass();
                if(connection!=null){
                    try{
                        String insertSQL = "INSERT INTO Candidature VALUES ('"+getIntent().getStringExtra("ID_Candidat")+"','"+getIntent().getStringExtra("ID_Offre")+"',?,?);";
                        PreparedStatement statement = connection.prepareStatement(insertSQL);
                        statement.setBytes(1, CVfileBytes);
                        statement.setBytes(2, LETTREfileBytes);
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
                    cvFileName.setText(fileName);
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



        if(requestCode == PICKFILE_LETTRE_RESULT_CODE && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                // Get the file name from the content URI
                String fileName = null;
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    fileName = cursor.getString(nameIndex);
                    lettreFileName.setText(fileName);
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
                LETTREfileBytes = outputStream.toByteArray();
                inputStream.close();
                outputStream.close();

                // You now have the file name and data as a byte array (fileName, fileBytes)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }
    }