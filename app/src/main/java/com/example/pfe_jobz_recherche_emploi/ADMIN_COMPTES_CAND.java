package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ADMIN_COMPTES_CAND extends AppCompatActivity {

    RecyclerView recyclerViewDemandeurs;
    List<ADMIN_DEM_ITEM> demandeurs;
    ADMIN_DEM_ADAPTER adminDemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_comptes_cand);

        recyclerViewDemandeurs = findViewById(R.id.recyclerViewCandAdmin);
        recyclerViewDemandeurs.setLayoutManager(new LinearLayoutManager(this));

        demandeurs = getComptesEmployeursFromDatabase();

        adminDemAdapter = new ADMIN_DEM_ADAPTER(demandeurs, this);
        recyclerViewDemandeurs.setAdapter(adminDemAdapter);
    }

    public List<ADMIN_DEM_ITEM> getComptesEmployeursFromDatabase(){

        List<ADMIN_DEM_ITEM> demandeurs = new ArrayList<>();

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection!=null){
            try{
                String selectSQL = "SELECT Nom, Prenom, Email, Statut_Compte FROM Candidat";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                while (resultSet.next()){
                    String nomcomplet = resultSet.getString("Nom") + " " + resultSet.getString("Prenom");
                    String email = resultSet.getString("Email");
                    String statut = resultSet.getString("Statut_Compte");
                    boolean isActive = (statut.equals("1"));
                    ADMIN_DEM_ITEM adminDemItem = new ADMIN_DEM_ITEM(nomcomplet, email, statut);
                    demandeurs.add(adminDemItem);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Collections.reverse(demandeurs);
        return demandeurs;
    }
}