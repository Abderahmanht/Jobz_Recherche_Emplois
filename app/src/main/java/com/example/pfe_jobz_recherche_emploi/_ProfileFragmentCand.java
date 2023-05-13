package com.example.pfe_jobz_recherche_emploi;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class _ProfileFragmentCand extends Fragment {

    private Button gestion_alertes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_cand,container,false);

        gestion_alertes = view.findViewById(R.id.profil_alerte);
        gestion_alertes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GestionAlertesCand.class).putExtra("ID_Candidat", getActivity().getIntent().getStringExtra("ID_Candidat")));
                getActivity().overridePendingTransition(0,0);
            }
        });

        return view;
    }
}
