package com.example.pfe_jobz_recherche_emploi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class __GererCandidaturesFragmentRec extends Fragment {

    private RecyclerView recyclerViewCandidaturesRec;
    private List<CandidatureItemRec> candidatureItemRec;
    private CandidatureRecAdapter candidatureRecAdapter;
    private ResultSet resultSet,resultSet1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gerer_candidatures_rec, container, false);

        recyclerViewCandidaturesRec = view.findViewById(R.id.recyclerViewCandidaturesRec);
        recyclerViewCandidaturesRec.setLayoutManager(new LinearLayoutManager(getContext()));

        candidatureItemRec =  getCandidaturesRecFromDatabase(getActivity().getIntent().getStringExtra("ID_Recruteur"));
        candidatureRecAdapter = new CandidatureRecAdapter(candidatureItemRec, getActivity().getIntent().getStringExtra("ID_Recruteur"));
        recyclerViewCandidaturesRec.setAdapter(candidatureRecAdapter);
        return view;
    }

    public List<CandidatureItemRec> getCandidaturesRecFromDatabase(String IDrec) {
        List<CandidatureItemRec> candidatures = new ArrayList<>();

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection!=null){
            try{
                String selectSQL = "SELECT o.Titre_Poste, c.ID_Candidature, cd.Nom, cd.Prenom " +
                        "FROM Candidature c " +
                        "JOIN Offre o ON c.ID_Offre = o.ID_Offre " +
                        "JOIN Candidat cd ON c.ID_Candidat = cd.ID_Candidat " +
                        "WHERE ID_Recruteur = " + IDrec;

                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(selectSQL);

                while (resultSet.next()){
                    String idcandidature = resultSet.getString("ID_Candidature");
                    String nomComplet = resultSet.getString("Nom")+" "+resultSet.getString("Prenom");
                    String titre = resultSet.getString("Titre_Poste");
                    CandidatureItemRec candidatureItemR = new CandidatureItemRec(idcandidature,titre,nomComplet);
                    candidatures.add(candidatureItemR);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Collections.reverse(candidatures);
        return candidatures;
    }
}