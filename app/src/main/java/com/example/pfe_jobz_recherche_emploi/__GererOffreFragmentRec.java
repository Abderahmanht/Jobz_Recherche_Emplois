package com.example.pfe_jobz_recherche_emploi;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class __GererOffreFragmentRec extends Fragment {

    RecyclerView recyclerViewJobOffersRec;
    List<JobOfferItemRec> JobOffersRec;
    JobOfferAdapterRec jobOfferAdapterRec;
    ResultSet resultSet,resultSet1;
    Bitmap bitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_gerer_offre_rec, container, false);

        recyclerViewJobOffersRec = view.findViewById(R.id.recyclerViewJobOffersRec);
        recyclerViewJobOffersRec.setLayoutManager(new LinearLayoutManager(getContext()));

        JobOffersRec =  getJobOffersRecFromDatabase(getActivity().getIntent().getStringExtra("ID_Recruteur"));
        jobOfferAdapterRec = new JobOfferAdapterRec(JobOffersRec, getActivity().getIntent().getStringExtra("ID_Recruteur"));
        recyclerViewJobOffersRec.setAdapter(jobOfferAdapterRec);

        return view;
    }

    public List<JobOfferItemRec> getJobOffersRecFromDatabase(String IDRec) {
        List<JobOfferItemRec> jobs = new ArrayList<>();

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection!=null){
            try{
                String selectSQL = "SELECT * FROM Offre WHERE ID_Recruteur = "+IDRec;
                String selectSQLrec = "SELECT Entreprise, Description_Entreprise, Wilaya_Entreprise, Logo_Entreprise FROM Recruteur WHERE ID_Recruteur = "+IDRec;
                Statement statement = connection.createStatement();
                Statement statement1 = connection.createStatement();
                resultSet = statement.executeQuery(selectSQL);

                while (resultSet.next()){
                    String idoffre = resultSet.getString("ID_Offre");
                    String secteur = resultSet.getString("Discipline_Metier");
                    String titre = resultSet.getString("Titre_Poste");
                    String contrat = resultSet.getString("Type_Contrat");
                    String description = resultSet.getString("Description_Offre");
                    String competences = resultSet.getString("Competences");
                    String experience = resultSet.getString("Niveau_Experience");
                    String date = resultSet.getString("Date_Publication");
                    String datelimite = resultSet.getString("Date_Limite");

                    resultSet1 = statement1.executeQuery(selectSQLrec);
                    if(resultSet1.next()){
                        String entreprise = resultSet1.getString("Entreprise");
                        String description_en = resultSet1.getString("Description_Entreprise");
                        String wilaya = resultSet1.getString("Wilaya_Entreprise");
                        byte[] imageData = resultSet1.getBytes("Logo_Entreprise");
                        bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        JobOfferItemRec jobOffer = new JobOfferItemRec(idoffre,entreprise,description_en,titre,contrat,wilaya,date,bitmap,secteur,experience);
                        jobs.add(jobOffer);

                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        Collections.reverse(jobs);
        return jobs;
    }
}