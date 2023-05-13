package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GestionAlertesCand extends AppCompatActivity {
    private AlerteAdapter alerteAdapter;
    private RecyclerView recyclerViewalertes;
    private List<AlerteItem> alerteItemList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_alertes_cand);

        toolbar = findViewById(R.id.gestion_alertes_toolbar);
        recyclerViewalertes = findViewById(R.id.profil_alertes_recycler_view);
        recyclerViewalertes.setLayoutManager(new LinearLayoutManager(this));
        alerteItemList = getAlertesFromDatabase();
        alerteAdapter = new AlerteAdapter(alerteItemList, getIntent().getStringExtra("ID_Candidat"));
        recyclerViewalertes.setAdapter(alerteAdapter);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

    }
    public List<AlerteItem> getAlertesFromDatabase() {
        List<AlerteItem> alertes = new ArrayList<>();

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection != null) {
            try {
                String selectSQL = "SELECT ID_Alerte, Titre, Poste_Recherche, Lieu_Residence FROM Alerte_Emploi WHERE ID_Candidat = "+getIntent().getStringExtra("ID_Candidat");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                while (resultSet.next()) {
                    String idAlerte = resultSet.getString("ID_Alerte");
                    String titre = resultSet.getString("Titre");
                    String poste = resultSet.getString("Poste_Recherche");
                    String lieu = resultSet.getString("Lieu_Residence");

                    AlerteItem alerteItem = new AlerteItem(idAlerte, titre, poste, lieu);
                    alertes.add(alerteItem);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            Exception exception = new Exception();
            exception.printStackTrace();
        }
        Collections.reverse(alertes);
        return alertes;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0,0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}