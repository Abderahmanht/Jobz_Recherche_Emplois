package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class HomeCand extends AppCompatActivity {
    _AcceuilFragmentCand acceuilFragment = new _AcceuilFragmentCand();
    _AlertesFragmentCand alertesFragment = new _AlertesFragmentCand();
    _ProfileFragmentCand profileFragment = new _ProfileFragmentCand();

    _MesListesFragmentCand mesListesFragment = new _MesListesFragmentCand();
    private _MesOffresFragmentCand mesOffresFragment;
    BottomNavigationView navbar;
    private String currentFragmentTag = null;
    Connection connection = new ___ConnectionClass().SQLServerConnection();
    int statut;
    Button retour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (connection!=null){
            try{
                String checkAccount = "SELECT Statut_Compte FROM Candidat WHERE ID_Candidat = "+getIntent().getStringExtra("ID_Candidat")+";";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(checkAccount);

                if (resultSet.next()){
                    statut = Integer.parseInt(resultSet.getString("Statut_Compte").trim().replace(" ",""));
                    if(statut==0){
                        setContentView(R.layout.compte_attente);
                        retour = findViewById(R.id.compte_retour);
                        retour.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        });
                    }
                    else{
                        setContentView(R.layout.activity_home_cand);
                        Toolbar toolbar = findViewById(R.id.maintoolbar);
                        setSupportActionBar(toolbar);

                        navbar = findViewById(R.id.navbar);

                        if (getIntent().hasExtra("CURRENT_FRAGMENT_TAG")) {
                            currentFragmentTag = getIntent().getStringExtra("CURRENT_FRAGMENT_TAG");
                        }
                        if (currentFragmentTag != null) {
                            switch (currentFragmentTag) {
                                case "acceuilFragment":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, acceuilFragment).commit();
                                    break;
                                case "mesListesFragment":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mesListesFragment).commit();
                                    break;
                                case "alertesFragment":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, alertesFragment).commit();
                                    break;
                                case "profilFragment":
                                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                                    break;
                                default:
                                    // Handle unknown fragment tag
                                    break;
                            }
                            updateSelectedNavItem(currentFragmentTag);
                        }else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, acceuilFragment).commit();
                        }



                        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
                            @Override
                            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.acceuil:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, acceuilFragment).commit();
                                        currentFragmentTag = "acceuilFragment";
                                        break;
                                    case R.id.meslistes:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mesListesFragment).commit();
                                        currentFragmentTag = "mesListesFragment";
                                        break;
                                    case R.id.alertes:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, alertesFragment).commit();
                                        currentFragmentTag = "alertesFragment";
                                        break;
                                    case R.id.profil_cand:
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
                                        currentFragmentTag = "profileFragment";
                                        break;
                                }

                                updateSelectedNavItem(currentFragmentTag); // Update the selected item

                                return true;
                            }
                        });

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actualiser:
                restartActivity();
                return true;
            case R.id.apropos:
                startActivity(new Intent(HomeCand.this, AProposDeNous.class));
                return true;
            case R.id.sedeconnecter:
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeCand.this);
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
                        Intent intent = new Intent(HomeCand.this, Login.class);
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

    public void restartActivity() {

        Intent refreshIntent = new Intent(HomeCand.this, HomeCand.class);
            refreshIntent.putExtras(getIntent().getExtras());
            refreshIntent.putExtra("CURRENT_FRAGMENT_TAG", currentFragmentTag);
            startActivity(refreshIntent);
            overridePendingTransition(0, 0);


    }

    private void updateSelectedNavItem(String fragmentTag) {
        int itemId;
        switch (fragmentTag) {
            case "acceuilFragment":
                itemId = R.id.acceuil;
                break;
            case "mesListesFragment":
                itemId = R.id.meslistes;
                break;
            case "alertesFragment":
                itemId = R.id.alertes;
                break;
            case "profileFragment":
                itemId = R.id.profil_cand;
                break;
            default:
                itemId = 0;
                break;
        }

        if (itemId != 0) {
            navbar.getMenu().findItem(itemId).setChecked(true);
        }
    }







}