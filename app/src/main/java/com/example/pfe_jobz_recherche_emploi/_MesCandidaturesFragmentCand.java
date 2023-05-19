package com.example.pfe_jobz_recherche_emploi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class _MesCandidaturesFragmentCand extends Fragment {

    private RecyclerView recyclerViewCandidatures;
    private CandidatureAdapter candidaturesAdapter;
    private List<CandidatureItem> candidatures;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mes_candidatures_cand,container,false);

        recyclerViewCandidatures = view.findViewById(R.id.recycler_view_candidatures);
        recyclerViewCandidatures.setLayoutManager(new LinearLayoutManager(getContext()));

        candidatures = getCandidaturesFromDatabase();
        candidaturesAdapter = new CandidatureAdapter(candidatures);
        recyclerViewCandidatures.setAdapter(candidaturesAdapter);

        return view;
    }

    public List<CandidatureItem> getCandidaturesFromDatabase(){

        List<CandidatureItem> candidatures = new ArrayList<>();

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection != null) {
            try {
                String selectSQL = "SELECT r.Entreprise, r.Wilaya_Entreprise, o.Titre_Poste, c.ID_Candidature " +
                        "FROM Candidature c " +
                        "JOIN Offre o ON c.ID_Offre = o.ID_Offre " +
                        "JOIN Recruteur r ON o.ID_Recruteur = r.ID_Recruteur "+
                        "WHERE ID_Candidat = "+getActivity().getIntent().getStringExtra("ID_Candidat");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                while (resultSet.next()) {
                    String idCandidature = resultSet.getString("ID_Candidature");
                    String titre = resultSet.getString("Titre_Poste");
                    String entreprise = resultSet.getString("Entreprise");
                    String lieu = resultSet.getString("Wilaya_Entreprise");

                    CandidatureItem candidatureItem = new CandidatureItem(idCandidature, titre, entreprise, lieu);
                    candidatures.add(candidatureItem);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            Exception exception = new Exception();
            exception.printStackTrace();
        }
        Collections.reverse(candidatures);
        return candidatures;

    }

}