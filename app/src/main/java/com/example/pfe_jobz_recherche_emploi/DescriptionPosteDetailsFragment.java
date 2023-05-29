package com.example.pfe_jobz_recherche_emploi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DescriptionPosteDetailsFragment extends Fragment {

    TextView details_secteur;
    TextView details_descriptionn;
    TextView details_competences;
    TextView details_exp;
    TextView details_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description_poste_details, container, false);

        details_secteur = view.findViewById(R.id.details_secteur_vp);
        details_descriptionn = view.findViewById(R.id.details_descriptionn_vp);
        details_competences = view.findViewById(R.id.details_competences_vp);
        details_exp = view.findViewById(R.id.details_exp_vp);
        details_date = view.findViewById(R.id.details_date_vp);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String disciplineMetier = arguments.getString("disciplineMetier");
            String descriptionOffre = arguments.getString("descriptionOffre");
            String competences = arguments.getString("competences");
            String niveauExperience = arguments.getString("niveauExperience");
            String dateLimite = arguments.getString("dateLimite");

            setValues(disciplineMetier, descriptionOffre, competences, niveauExperience, dateLimite);
        }
    }

    public void setValues(String disciplineMetier, String descriptionOffre, String competences, String niveauExperience, String dateLimite) {
        details_secteur.setText(disciplineMetier);
        details_descriptionn.setText(descriptionOffre);
        details_competences.setText(competences);
        details_exp.setText(niveauExperience);
        details_date.setText(dateLimite);
    }
}


