package com.example.pfe_jobz_recherche_emploi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class __AcceuilFragmentRec extends Fragment {

    Button btn;
    FloatingActionButton fab;

    JobOfferAdapterRec jobOfferAdapterRec;
    CandidatureRecAdapter candidatureRecAdapter;

    List<JobOfferItemRec> jobOfferItemRecList;
    List<CandidatureItemRec> candidatureItemRecList;
    TextView nombreCandidaturesText, nombreOffresText;
    __GererOffreFragmentRec gererOffreFragmentRec = new __GererOffreFragmentRec();
    __GererCandidaturesFragmentRec gererCandidaturesFragmentRec = new __GererCandidaturesFragmentRec();

    RelativeLayout canRecues,offresPub;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_acceuil_rec, container, false);

        jobOfferItemRecList = gererOffreFragmentRec.getJobOffersRecFromDatabase(getActivity().getIntent().getStringExtra("ID_Recruteur"));
        candidatureItemRecList = gererCandidaturesFragmentRec.getCandidaturesRecFromDatabase(getActivity().getIntent().getStringExtra("ID_Recruteur"));

        jobOfferAdapterRec = new JobOfferAdapterRec(jobOfferItemRecList, getActivity().getIntent().getStringExtra("ID_Recruteur"));
        candidatureRecAdapter = new CandidatureRecAdapter(candidatureItemRecList, getActivity().getIntent().getStringExtra("ID_Recruteur"));


        int nombreOffres = jobOfferAdapterRec.getItemCount();
        int nombreCandidatures = candidatureRecAdapter.getItemCount();



        btn = view.findViewById(R.id.publier_offre_button);
        fab = view.findViewById(R.id.publier_offre_fab);
        nombreCandidaturesText = view.findViewById(R.id.stats_nombre_candidatures);
        nombreOffresText = view.findViewById(R.id.stats_nombre_offres);
        canRecues = view.findViewById(R.id.candidatures_recue_layout);
        offresPub = view.findViewById(R.id.offres_publiees_layout);

        offresPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment gererOffres = new __GererOffreFragmentRec();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_rec, gererOffres);
                fragmentTransaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                fragmentTransaction.commit();
            }
        });

        canRecues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment gererCand= new __GererCandidaturesFragmentRec();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container_rec, gererCand);
                fragmentTransaction.addToBackStack(null); // Optional: Add the transaction to the back stack
                fragmentTransaction.commit();
            }
        });

        nombreCandidaturesText.setText(String.valueOf(nombreCandidatures));
        nombreOffresText.setText(String.valueOf(nombreOffres));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),__AjouterOffre.class).putExtra("ID_Recruteur",getActivity().getIntent().getStringExtra("ID_Recruteur")));
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),__AjouterOffre.class).putExtra("ID_Recruteur",getActivity().getIntent().getStringExtra("ID_Recruteur")));
            }
        });

        return view;
    }
}