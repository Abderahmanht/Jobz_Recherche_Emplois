package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class PostezCV extends AppCompatActivity {

    AutoCompleteTextView secteur;
    Button button,valider;
    private static final int PICKFILE_CV_RESULT_CODE = 1;
    String fileName;
    private byte[] CVfileBytes;
    private Toolbar toolbar;
    private TextView etudes;
    private TextView experience;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postez_cv);



        secteur = findViewById(R.id.postez_secteurCvCANDedittext);
        button = findViewById(R.id.postez_cv_charger_cv);
        toolbar = findViewById(R.id.postez_cv_toolbar);
        valider = findViewById(R.id.postez_valider);

        etudes = findViewById(R.id.postez_etudesCvCANDedittext);
        experience = findViewById(R.id.postez_experienceCvCANDedittext);

        //region disciciplines
        String[] disciplines = {"Informatique", "Droit", "Marketing", "Médecine", "Ingénierie", "Finance", "Design", "Éducation", "Communication", "Arts",
                "Administration publique",
                "Anthropologie",
                "Archéologie",
                "Architecture",
                "Artisanat",
                "Biologie",
                "Chimie",
                "Cinématographie",
                "Communication",
                "Création littéraire",
                "Danse",
                "Design graphique",
                "Droit",
                "Économie",
                "Éducation",
                "Enseignement des langues",
                "Génie civil",
                "Génie électrique",
                "Génie mécanique",
                "Géographie",
                "Géologie",
                "Gestion",
                "Histoire",
                "Informatique",
                "Ingénierie",
                "Journalisme",
                "Langues étrangères",
                "Linguistique",
                "Marketing",
                "Mathématiques",
                "Médecine",
                "Météorologie",
                "Musique",
                "Neurosciences",
                "Nutrition",
                "Optométrie",
                "Philosophie",
                "Physique",
                "Psychologie",
                "Publicité",
                "Relations internationales",
                "Relations publiques",
                "Sciences de l'environnement",
                "Sciences de la Terre",
                "Sciences politiques",
                "Sociologie",
                "Théâtre",
                "Tourisme",
                "Traduction",
                "Astronomie",
                "Bio-informatique",
                "Biostatistique",
                "Chimie analytique",
                "Chirurgie plastique",
                "Cybersécurité",
                "Design d'intérieur",
                "Design industriel",
                "Éducation physique",
                "Études de genre",
                "Génétique",
                "Gérontologie",
                "Gestion des ressources humaines",
                "Graphisme",
                "Hydrologie",
                "Immunologie",
                "Informatique décisionnelle",
                "Ingénierie biomédicale",
                "Ingénierie environnementale",
                "Mécatronique",
                "Météorologie",
                "Neurobiologie",
                "Océanographie",
                "Odontologie",
                "Optique",
                "Orthophonie",
                "Pédagogie",
                "Pharmacologie",
                "Planification urbaine",
                "Robotique"
        };



        //endregion disciplines
        HashSet<String> setWithoutDuplicates = new HashSet<>(Arrays.asList(disciplines));
        String[] arrWithoutDuplicates = setWithoutDuplicates.toArray(new String[setWithoutDuplicates.size()]);
        ArrayAdapter<String> adapter_dis = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrWithoutDuplicates);
        secteur.setAdapter(adapter_dis);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_CV_RESULT_CODE);

            }
        });

        experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(PostezCV.this, experience);
                popupMenu.getMenuInflater().inflate(R.menu.experience_alerte, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        experience.setText(item.getTitle());
                        return true;
                    }
                });
                popupMenu.show();
            }

        });

        etudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(PostezCV.this, etudes);
                popupMenu.getMenuInflater().inflate(R.menu.etudes, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        etudes.setText(item.getTitle());
                        return true;
                    }
                });
                popupMenu.show();
            }

        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connection connection = new ___ConnectionClass().SQLServerConnection();
                int resultSet = 0;
                if (connection != null) {
                    try {
                        String SQLinsert = "INSERT INTO CV (ID_Candidat, CV_fichier, CV_nom_fichier, Secteur, Experience, Etudes) VALUES ("+getIntent().getStringExtra("ID_Candidat")+",?,?,?,?,?);";
                        PreparedStatement statement = connection.prepareStatement(SQLinsert);
                        statement.setString(3,secteur.getText().toString());
                        statement.setBytes(1, CVfileBytes);
                        statement.setString(2, fileName);
                        statement.setString(4,experience.getText().toString());
                        statement.setString(5,etudes.getText().toString());
                        resultSet = statement.executeUpdate();


                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                if (resultSet > 0) {
                    Toast.makeText(PostezCV.this,"Cv déposé avec succès",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_CV_RESULT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                fileName = null;
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    fileName = cursor.getString(nameIndex);
                    button.setText(fileName);
                    button.setTextColor(Color.parseColor("#0CAA41"));
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


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}