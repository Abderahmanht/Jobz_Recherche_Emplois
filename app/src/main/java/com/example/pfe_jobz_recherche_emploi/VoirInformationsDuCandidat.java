package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.Objects;

public class VoirInformationsDuCandidat extends AppCompatActivity {
    Toolbar toolbar;
    Button telechargerCV, envoyerEMAIL, envoyerSMS;
    TextView nom, prenom, dateN, dateC, poste;
    String num, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_informations_du_candidat);

        toolbar = findViewById(R.id.voir_info_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        poste = findViewById(R.id.voir_info_candidature_poste_v);
        nom = findViewById(R.id.voir_info_candidature_nom_v);
        prenom = findViewById(R.id.voir_info_candidature_prenom_v);
        dateN = findViewById(R.id.voir_info_candidature_daten_v);
        dateC = findViewById(R.id.voir_info_candidature_date_candidature_v);

        telechargerCV = findViewById(R.id.voir_info_telecharger_cv_button);
        envoyerEMAIL = findViewById(R.id.voir_info_envoyer_email_btn);
        envoyerSMS = findViewById(R.id.voir_info_envoyer_sms_btn);

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection!=null){
            try{
                String sqlQuery = "SELECT Candidat.Nom, Candidat.Prenom, Candidat.Email, Candidat.Num_Tel, Candidat.Date_Naissance, Candidature.Date_Candidature, Offre.Titre_Poste "+
                        "FROM Candidat JOIN Candidature ON Candidature.ID_Candidat = Candidat.ID_Candidat "+
                        "JOIN Offre ON Candidature.ID_Offre = Offre.ID_Offre "+
                        "WHERE ID_Candidature = "+getIntent().getStringExtra("ID_Candidature");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);

                if (resultSet.next()){
                    poste.setText(resultSet.getString("Titre_Poste"));
                    nom.setText(resultSet.getString("Nom"));
                    prenom.setText(resultSet.getString("Prenom"));
                    dateN.setText(resultSet.getString("Date_Naissance"));
                    dateC.setText(resultSet.getString("Date_Candidature"));

                    num = resultSet.getString("Num_Tel");
                    email = resultSet.getString("Email");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        envoyerEMAIL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail(email);
            }
        });

        envoyerSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeSmsMessage(num);
            }
        });

    }
    public void composeEmail(String address) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + Uri.encode(address)));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        intent.putExtra(Intent.EXTRA_SUBJECT,"Jobz - Candidature pour : "+poste.getText().toString() );
        intent.putExtra(Intent.EXTRA_TEXT, "Bonjour "+nom.getText().toString()+" "+prenom.getText().toString()+ "\nNous avons bien reçu votre candidature pour le poste "+poste.getText().toString());
        startActivity(intent);


    }
    public void composeSmsMessage(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("smsto:"+phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
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
}