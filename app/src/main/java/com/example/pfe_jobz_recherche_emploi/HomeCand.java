package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeCand extends AppCompatActivity {
    _AcceuilFragmentCand acceuilFragment = new _AcceuilFragmentCand();
    _MesCandidaturesFragmentCand mesCandidaturesFragment = new _MesCandidaturesFragmentCand();
    _AlertesFragmentCand alertesFragment = new _AlertesFragmentCand();
    _MesOffresFragmentCand mesoffresFragment = new _MesOffresFragmentCand();
    _ProfileFragmentCand profileFragment = new _ProfileFragmentCand();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cand);
        Toolbar toolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navbar = findViewById(R.id.navbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,acceuilFragment).commit();

        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.acceuil:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,acceuilFragment).commit();
                        break;
                    case R.id.mes_candidatures:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mesCandidaturesFragment).commit();
                        break;
                    case R.id.alertes:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,alertesFragment).commit();
                        break;
                    case R.id.mes_offres:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mesoffresFragment).commit();
                        break;
                    case R.id.profil_cand:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,profileFragment).commit();
                        break;
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_menu,menu);
        return true;
    }


}