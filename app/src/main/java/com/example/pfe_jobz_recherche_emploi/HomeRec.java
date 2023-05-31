package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HomeRec extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    __AcceuilFragmentRec acceuilFragmentRec = new __AcceuilFragmentRec();
    __GererOffreFragmentRec gererOffreFragmentRec = new __GererOffreFragmentRec();
    __GererCandidaturesFragmentRec gererCandidaturesFragmentRec = new __GererCandidaturesFragmentRec();

    __ModifierProfilFragmentRec modifierProfilFragmentRec = new __ModifierProfilFragmentRec();

    __CvThequeFragmentRec cvThequeFragmentRec = new __CvThequeFragmentRec();

    Connection connection = new ___ConnectionClass().SQLServerConnection();
    private String currentFragmentTag = null;


    int statut;
    Button retour;
    TextView entrepriseTextView;
    TextView nomCompletTextView;
    TextView emailTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (connection != null) {
            try {
                String checkAccount = "SELECT Nom, Prenom, Entreprise,Email, Statut_Compte FROM Recruteur WHERE ID_Recruteur = " + getIntent().getStringExtra("ID_Recruteur") + ";";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(checkAccount);

                if (resultSet.next()) {
                    statut = Integer.parseInt(resultSet.getString("Statut_Compte").trim().replace(" ", ""));
                    if (statut == 0) {
                        setContentView(R.layout.compte_attente);
                        retour = findViewById(R.id.compte_retour);
                        retour.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                    } else {
                        setContentView(R.layout.activity_home_rec);
                        Toolbar toolbar = findViewById(R.id.maintoolbarrec);
                        setSupportActionBar(toolbar);


                        NavigationView navigationView = findViewById(R.id.navigation_view_rec);

                        View headerView = navigationView.getHeaderView(0);

                        entrepriseTextView = headerView.findViewById(R.id.entrepriseemployeur_header);
                        nomCompletTextView = headerView.findViewById(R.id.nomcomplet_employeur);
                        emailTextView = headerView.findViewById(R.id.email_employeur_header);

                        entrepriseTextView.setText(resultSet.getString("Entreprise"));
                        nomCompletTextView.setText(resultSet.getString("Nom") + " " + resultSet.getString("Prenom"));
                        emailTextView.setText(resultSet.getString("Email"));



                        drawerLayout = findViewById(R.id.drawer);
                        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
                        drawerLayout.addDrawerListener(toggle);
                        toggle.syncState();
                        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.dark_green));




                        navigationView = findViewById(R.id.navigation_view_rec);
                        if (getIntent().hasExtra("CURRENT_FRAGMENT_TAG")) {
                            currentFragmentTag = getIntent().getStringExtra("CURRENT_FRAGMENT_TAG");
                        }
                        if (currentFragmentTag != null) {
                            switch (currentFragmentTag) {
                                case "acceuilFragment":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, acceuilFragmentRec).commit();
                                    break;
                                case "offresFragment":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, gererOffreFragmentRec).commit();
                                    break;
                                case "candFragment":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, gererCandidaturesFragmentRec).commit();
                                    break;
                                case "modifierProfilFragment":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, modifierProfilFragmentRec).commit();
                                    break;
                                case "cvthequeFragment":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, cvThequeFragmentRec).commit();
                                    break;
                                default:
                                    // Handle unknown fragment tag
                                    break;
                            }
                        }else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, acceuilFragmentRec).commit();
                        }


                        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                                // Handle menu item selection here
                                int itemId = menuItem.getItemId();

                                switch (itemId) {
                                    case R.id.tableaudebord:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, acceuilFragmentRec).commit();
                                        currentFragmentTag = "acceuilFragment";
                                        break;
                                    case R.id.vosoffres:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, gererOffreFragmentRec).commit();
                                        currentFragmentTag = "offresFragment";
                                        break;
                                    case R.id.candidatures:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, gererCandidaturesFragmentRec).commit();
                                        currentFragmentTag = "candFragment";
                                        break;
                                    case R.id.modifierprofil:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, modifierProfilFragmentRec).commit();
                                        currentFragmentTag = "modifierProfilFragment";
                                        break;
                                    case R.id.cvtheque:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec, cvThequeFragmentRec).commit();
                                        currentFragmentTag = "cvthequeFragment";
                                        break;
                                    case R.id.sedeconnecterrec:
                                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeRec.this);
                                        builder.setTitle("Se déconnecter");
                                        builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?");
                                        builder.setPositiveButton("Se déconnecter", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                // Delete the saved credentials from SharedPreferences
                                                SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.remove("email");
                                                editor.remove("password");
                                                editor.apply();

                                                // Start the Login activity
                                                Intent intent = new Intent(HomeRec.this, Login.class);
                                                startActivity(intent);
                                            }
                                        });
                                        builder.setNegativeButton("Annuler", null);

                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                        break;

                                    case R.id.supprimercompterec:
                                        AlertDialog.Builder builders = new AlertDialog.Builder(HomeRec.this);
                                        builders.setTitle("Supprimer Compte ?");
                                        builders.setMessage("Êtes-vous sûr de vouloir supprimer votre compte ?");
                                        builders.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //another builder

                                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeRec.this);
                                                builder.setTitle("Confirmer");
                                                builder.setMessage("Cette action est irréversible\nVotre compte, vos offres, les candidatures à vos offres... seront supprimées définitivement");
                                                builder.setPositiveButton("Confirmer suppression", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        if(connection!=null){
                                                            try{
                                                                String deleteRecAccountSQL = "DELETE FROM Recruteur WHERE ID_Recruteur = "+getIntent().getStringExtra("ID_Recruteur");
                                                                String deleteOffre = "DELETE FROM Offre WHERE ID_Recruteur = "+getIntent().getStringExtra("ID_Recruteur");
                                                                Statement statement1 = connection.createStatement();
                                                                Statement statement2 = connection.createStatement();
                                                                int resultSet = statement1.executeUpdate(deleteRecAccountSQL);

                                                                String checkExists = "SELECT 1 FROM Alerte_Emploi WHERE ID_Candidat = " + getIntent().getStringExtra("ID_Candidat");
                                                                ResultSet resultSetoffre = statement2.executeQuery(checkExists);
                                                                boolean alerteExists = resultSetoffre.next();

                                                                if(alerteExists) {
                                                                    statement2.executeUpdate(deleteOffre);
                                                                }
                                                                if(resultSet>0){
                                                                    Toast.makeText(HomeRec.this,"Compte supprimé",Toast.LENGTH_LONG).show();
                                                                    SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
                                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                    editor.remove("email");
                                                                    editor.remove("password");
                                                                    editor.apply();

                                                                    Intent intent = new Intent(HomeRec.this, Login.class);
                                                                    startActivity(intent);
                                                                }
                                                            }catch (Exception e){
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    }
                                                });
                                                builder.setNegativeButton("Annuler", null);

                                                AlertDialog dialog = builder.create();
                                                dialog.show();

                                                //end another builder
                                            }
                                        });
                                        builders.setNegativeButton("Annuler", null);

                                        AlertDialog dialogs = builders.create();
                                        dialogs.show();
                                        break;

                                }

                                drawerLayout.closeDrawer(GravityCompat.START);
                                return true;
                            }
                        });


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.tool_bar_menu_rec, menu);
            return true;
        }

    public void restartActivity() {

        Intent refreshIntent = new Intent(HomeRec.this, HomeRec.class);
        refreshIntent.putExtras(getIntent().getExtras());
        refreshIntent.putExtra("CURRENT_FRAGMENT_TAG", currentFragmentTag);
        startActivity(refreshIntent);
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actualiser:
                restartActivity();
                return true;
            case R.id.apropos:
                startActivity(new Intent(HomeRec.this, AProposDeNous.class));
                return true;
            case R.id.sedeconnecter:
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeRec.this);
                builder.setTitle("Se déconnecter");
                builder.setMessage("Êtes-vous sûr de vouloir vous déconnecter ?");
                builder.setPositiveButton("Se déconnecter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete the saved credentials from SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("email");
                        editor.remove("password");
                        editor.apply();

                        // Start the Login activity
                        Intent intent = new Intent(HomeRec.this, Login.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("Annuler", null);

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}