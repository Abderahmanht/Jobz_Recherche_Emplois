package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomeCand extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cand);
        Toolbar toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navbar = findViewById(R.id.navbar);


        _EmploisFragmentCand emploisFragment = new _EmploisFragmentCand();
        _RechercherFragmentCand rechercherFragment = new _RechercherFragmentCand();
        _MesOffresFragmentCand mesoffresFragment = new _MesOffresFragmentCand();
        _ProfileFragmentCand profileFragment = new _ProfileFragmentCand();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,emploisFragment).commit();

        navbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.emplois:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,emploisFragment).commit();
                        return true;
                    case R.id.rechercher:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,rechercherFragment).commit();
                        return true;
                    case R.id.mes_offres:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mesoffresFragment).commit();
                        return true;
                    case R.id.profile_cand:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,profileFragment).commit();
                        return true;
                }
                return false;
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