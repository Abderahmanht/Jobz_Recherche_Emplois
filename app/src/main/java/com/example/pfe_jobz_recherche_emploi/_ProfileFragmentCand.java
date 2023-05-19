package com.example.pfe_jobz_recherche_emploi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class _ProfileFragmentCand extends Fragment {

    private Button gestion_alertes,logoutBtn, modifierProfil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_cand,container,false);

        logoutBtn = view.findViewById(R.id.logout_button_cand);
        modifierProfil = view.findViewById(R.id.modifier_profil_cand);

        modifierProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ModifierProfilCand.class).putExtra("ID_Candidat",getActivity().getIntent().getStringExtra("ID_Candidat")));
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Se déconnecter");
                builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?");
                builder.setPositiveButton("Se déconnecter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete the saved credentials from SharedPreferences
                        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("email");
                        editor.remove("password");
                        editor.apply();

                        // Start the Login activity
                        Intent intent = new Intent(getContext(), Login.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Annuler", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


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
