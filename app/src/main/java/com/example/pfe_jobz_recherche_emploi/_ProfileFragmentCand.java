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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class _ProfileFragmentCand extends Fragment {

    private Button logoutBtn, modifierProfil, apropos,deleteaccount;
    private RelativeLayout deposerCV;
    private ImageView imageView,delete;
    private TextView textView,votreCV, nomcomplet, email;
    private boolean available = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_cand, container, false);

        logoutBtn = view.findViewById(R.id.logout_button_cand);
        modifierProfil = view.findViewById(R.id.modifier_profil_cand);
        deposerCV = view.findViewById(R.id.deposer_cv_cand);
        imageView = view.findViewById(R.id.postez_cv_icon);
        delete = view.findViewById(R.id.supprimer_cv);
        textView = view.findViewById(R.id.cv_file_name_profil);
        votreCV = view.findViewById(R.id.profil_votre_cv_text);
        apropos = view.findViewById(R.id.a_propos_profil);
        deleteaccount = view.findViewById(R.id.delete_button_cand);

        nomcomplet = view.findViewById(R.id.profil_nom_complet);
        email = view.findViewById(R.id.profil_email);

        Connection con = new ___ConnectionClass().SQLServerConnection();
        if(con!=null){
            try {
                String selectSQL = "SELECT Nom, Prenom, Email FROM Candidat WHERE ID_Candidat = "+getActivity().getIntent().getStringExtra("ID_Candidat")+";";
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);
                if(resultSet.next()){
                    nomcomplet.setText(resultSet.getString("Nom")+" "+resultSet.getString("Prenom"));
                    email.setText(resultSet.getString("Email"));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builders = new AlertDialog.Builder(getContext());
                builders.setTitle("Supprimer Compte ?");
                builders.setMessage("Êtes-vous sûr de vouloir supprimer votre compte ?");
                builders.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // another builder

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Confirmer");
                        builder.setMessage("Cette action est irréversible\nVotre compte sera supprimé définitivement");
                        builder.setPositiveButton("Confirmer suppression", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (con != null) {
                                    try {
                                        String deleteRecAccountSQL = "DELETE FROM Candidat WHERE ID_Candidat = " + getActivity().getIntent().getStringExtra("ID_Candidat");
                                        String deleteCandidature = "DELETE FROM Candidature WHERE ID_Candidat = " + getActivity().getIntent().getStringExtra("ID_Candidat");
                                        String deleteCV = "DELETE FROM CV WHERE ID_Candidat = " + getActivity().getIntent().getStringExtra("ID_Candidat");
                                        String deleteOffree = "DELETE FROM Offre_Enregistree WHERE ID_Candidat = " + getActivity().getIntent().getStringExtra("ID_Candidat");
                                        String deleteAlerte = "DELETE FROM Alerte_Emploi WHERE ID_Candidat = "+getActivity().getIntent().getStringExtra("ID_Candidat");

                                        try {
                                            Statement statement1 = con.createStatement();
                                            Statement statement2 = con.createStatement();
                                            Statement statement3 = con.createStatement();
                                            Statement statement4 = con.createStatement();
                                            Statement statement5 = con.createStatement();

                                            // Check if the Candidature exists for the Candidat
                                            String checkCandidatureExists = "SELECT 1 FROM Candidature WHERE ID_Candidat = " + getActivity().getIntent().getStringExtra("ID_Candidat");
                                            ResultSet resultSetCandidature = statement2.executeQuery(checkCandidatureExists);
                                            boolean candidatureExists = resultSetCandidature.next();

                                            // Check if the CV exists for the Candidat
                                            String checkCVExists = "SELECT 1 FROM CV WHERE ID_Candidat = " + getActivity().getIntent().getStringExtra("ID_Candidat");
                                            ResultSet resultSetCV = statement3.executeQuery(checkCVExists);
                                            boolean cvExists = resultSetCV.next();

                                            // Check if the Offre_Enregistree exists for the Candidat
                                            String checkOffreeExists = "SELECT 1 FROM Offre_Enregistree WHERE ID_Candidat = " + getActivity().getIntent().getStringExtra("ID_Candidat");
                                            ResultSet resultSetOffree = statement4.executeQuery(checkOffreeExists);
                                            boolean offreeExists = resultSetOffree.next();

                                            String checkAlerteExists = "SELECT 1 FROM Alerte_Emploi WHERE ID_Candidat = " + getActivity().getIntent().getStringExtra("ID_Candidat");
                                            ResultSet resultSetAlerte = statement5.executeQuery(checkAlerteExists);
                                            boolean alerteExists = resultSetAlerte.next();


                                            if(candidatureExists){
                                                statement2.executeUpdate(deleteCandidature);
                                            }
                                            if(cvExists){
                                                statement3.executeUpdate(deleteCV);
                                            }
                                            if(offreeExists){
                                                statement4.executeUpdate(deleteOffree);
                                            }
                                            if(alerteExists){
                                                statement5.executeUpdate(deleteAlerte);
                                            }

                                                int resultSet1 = statement1.executeUpdate(deleteRecAccountSQL);

                                                if (resultSet1 > 0) {
                                                    Toast.makeText(getContext(), "Compte supprimé", Toast.LENGTH_LONG).show();
                                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.remove("email");
                                                    editor.remove("password");
                                                    editor.apply();

                                                    Intent intent = new Intent(getContext(), Login.class);
                                                    startActivity(intent);
                                                }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            // End of the second builder

                    });

                builder.setNegativeButton("Annuler", null);

                    AlertDialog dialog = builder.create();
                dialog.show();

                    // End of the first builder
                }
            });

        builders.setNegativeButton("Annuler", null);

            AlertDialog dialogs = builders.create();
        dialogs.show();
        }
    });



        apropos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AProposDeNous.class));
            }
        });


        // verifier si cv existe dans la base de donnees
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection != null) {
            try {
                String selectSQL = "SELECT CV_nom_fichier FROM CV WHERE ID_Candidat = " + getActivity().getIntent().getStringExtra("ID_Candidat") + ";";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);
                if (resultSet.next() && resultSet.getString("CV_nom_fichier") != null) {
                    deposerCV.setBackgroundResource(R.drawable.container_black);
                    imageView.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    textView.setText(resultSet.getString("CV_nom_fichier"));
                    textView.setTextColor(getResources().getColor(R.color.black));
                    votreCV.setVisibility(View.VISIBLE);
                    available = false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostezCV.class).putExtra("ID_Candidat", requireActivity().getIntent().getStringExtra("ID_Candidat")));
            }
        });


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Supprimer CV");
                builder.setMessage("Êtes-vous sûr de vouloir supprimer votre CV ?");
                builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Connection connection = new ___ConnectionClass().SQLServerConnection();

                        if (connection != null) {
                            try {
                                String updateSQL = "DELETE FROM CV WHERE ID_Candidat = " + getActivity().getIntent().getStringExtra("ID_Candidat");
                                Statement statement = connection.createStatement();
                                int resultSet = statement.executeUpdate(updateSQL);
                                if (resultSet > 0) {
                                    Toast.makeText(getContext(), "CV Supprimée", Toast.LENGTH_LONG).show();
                                    deposerCV.setBackgroundResource(R.color.light_blue);
                                    imageView.setVisibility(View.VISIBLE);
                                    delete.setVisibility(View.INVISIBLE);
                                    textView.setTextColor(getResources().getColor(R.color.white));
                                    textView.setText("Postez votre CV");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Annuler", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        modifierProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ModifierProfilCand.class).putExtra("ID_Candidat", getActivity().getIntent().getStringExtra("ID_Candidat")));
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


        return view;
    }
}
