package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ADMIN_COMPTES_REC extends AppCompatActivity{

    RecyclerView recyclerViewEmployeurs;
    List<ADMIN_EMP_ITEM> employeurs;
    ADMIN_EMP_ADAPTER adminEmpAdapter;
    Button enr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_comptes_rec);


        recyclerViewEmployeurs = findViewById(R.id.recyclerViewEmployeurs);
        recyclerViewEmployeurs.setLayoutManager(new LinearLayoutManager(this));

        employeurs = getComptesEmployeursFromDatabase();

        adminEmpAdapter = new ADMIN_EMP_ADAPTER(employeurs, this);
        recyclerViewEmployeurs.setAdapter(adminEmpAdapter);




    }



    public List<ADMIN_EMP_ITEM> getComptesEmployeursFromDatabase(){

        List<ADMIN_EMP_ITEM> employeurs = new ArrayList<>();

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection!=null){
            try{
                String selectSQL = "SELECT Nom, Prenom, Email, Entreprise, Statut_Compte FROM Recruteur";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                while (resultSet.next()){
                    String nomcomplet = resultSet.getString("Nom") + " " + resultSet.getString("Prenom");
                    String email = resultSet.getString("Email");
                    String entreprise = resultSet.getString("Entreprise");
                    String statut = resultSet.getString("Statut_Compte");
                    ADMIN_EMP_ITEM adminEmpItem = new ADMIN_EMP_ITEM(nomcomplet, entreprise, email, statut);
                    employeurs.add(adminEmpItem);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Collections.reverse(employeurs);
        return employeurs;
    }
}