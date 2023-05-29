package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class HomeRec extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    __AcceuilFragmentRec acceuilFragmentRec = new __AcceuilFragmentRec();
    __GererOffreFragmentRec gererOffreFragmentRec = new __GererOffreFragmentRec();
    __GererCandidaturesFragmentRec gererCandidaturesFragmentRec = new __GererCandidaturesFragmentRec();

    __ModifierProfilFragmentRec modifierProfilFragmentRec = new __ModifierProfilFragmentRec();

    __CvThequeFragmentRec cvThequeFragmentRec = new __CvThequeFragmentRec();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_rec);

        Toolbar toolbar = findViewById(R.id.maintoolbarrec);
        setSupportActionBar(toolbar);


        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.dark_green));
        navigationView = findViewById(R.id.navigation_view_rec);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec,acceuilFragmentRec).commit();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // Handle menu item selection here
                int itemId = menuItem.getItemId();

                switch (itemId) {
                    case R.id.tableaudebord:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec,acceuilFragmentRec).commit();
                        break;
                    case R.id.vosoffres:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec,gererOffreFragmentRec).commit();
                        break;
                    case R.id.candidatures:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec,gererCandidaturesFragmentRec).commit();
                        break;
                    case R.id.modifierprofil:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec,modifierProfilFragmentRec).commit();
                        break;
                    case R.id.cvtheque:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_rec,cvThequeFragmentRec).commit();


                }

                // Close the drawer after handling the item click
                drawerLayout.closeDrawer(GravityCompat.START);

                // Return true to indicate that the item click has been handled
                return true;
            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar_menu_rec,menu);
        return true;
    }
}