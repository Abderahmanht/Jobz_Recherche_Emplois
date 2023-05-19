package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class ADMIN extends AppCompatActivity {

    Button comptesEmployeurs, comptesDemandeursEmploi, envoieEmail;
    TextView countOffresPubliees, countCandidatures, countDemandeurs, countEmployeurs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        comptesEmployeurs = findViewById(R.id.admin_gestion_recruteurs_btn);
        comptesDemandeursEmploi = findViewById(R.id.admin_gestion_candidats_btn);
        envoieEmail = findViewById(R.id.admin_envoie_emails_btn);

        countOffresPubliees = findViewById(R.id.count_offres);
        countCandidatures = findViewById(R.id.count_candidatures);
        countDemandeurs = findViewById(R.id.count_dem);
        countEmployeurs = findViewById(R.id.count_emp);

        countOffresPubliees.setText(String.valueOf(countOffres()));
        countCandidatures.setText(String.valueOf(countCandidature()));
        countEmployeurs.setText(String.valueOf(countEmployeurs()));
        countDemandeurs.setText(String.valueOf(countDemandeurs()));

        comptesEmployeurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ADMIN.this, ADMIN_COMPTES_REC.class));
                overridePendingTransition(0,0);
            }
        });

        comptesDemandeursEmploi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ADMIN.this, ADMIN_COMPTES_CAND.class));
                overridePendingTransition(0,0);
            }
        });

        envoieEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ADMIN.this, ADMIN_EMAILS.class));
                overridePendingTransition(0,0);
            }
        });
    }

    public int countDemandeurs() {
        int count = 0;
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection != null) {
            try {
                String countDemandeursSQL = "SELECT COUNT(*) FROM Candidat";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(countDemandeursSQL);

                if (resultSet.next()) {
                    count = resultSet.getInt(1); // Retrieve the count value from the first column
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public int countEmployeurs() {
        int count = 0;
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection != null) {
            try {
                String countDemandeursSQL = "SELECT COUNT(*) FROM Recruteur";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(countDemandeursSQL);

                if (resultSet.next()) {
                    count = resultSet.getInt(1); // Retrieve the count value from the first column
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public int countCandidature() {
        int count = 0;
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection != null) {
            try {
                String countDemandeursSQL = "SELECT COUNT(*) FROM Candidature";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(countDemandeursSQL);

                if (resultSet.next()) {
                    count = resultSet.getInt(1); // Retrieve the count value from the first column
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    public int countOffres() {
        int count = 0;
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection != null) {
            try {
                String countDemandeursSQL = "SELECT COUNT(*) FROM Offre";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(countDemandeursSQL);

                if (resultSet.next()) {
                    count = resultSet.getInt(1); // Retrieve the count value from the first column
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }


}