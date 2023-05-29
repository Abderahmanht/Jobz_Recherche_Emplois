package com.example.pfe_jobz_recherche_emploi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DescriptionEntrepriseDetailsFragment extends Fragment {

    TextView details_secteur_activite_vv;
    TextView details_descriptionn_vv;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description_entreprise_details, container, false);

        details_secteur_activite_vv = view.findViewById(R.id.details_secteur_activite_vv);
        details_descriptionn_vv = view.findViewById(R.id.details_descriptionn_vv);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String secteur = arguments.getString("secteur");
            String description = arguments.getString("descriptionEntreprise");

            setValues(secteur, description);
        }
    }

    public void setValues(String secteur, String description) {

        details_secteur_activite_vv.setText(secteur);
        details_descriptionn_vv.setText(description);
    }

}