package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JobDetails extends AppCompatActivity {

    private TextView name;
    private TextView title;
    private Toolbar toolbar;
    private TextView location;
    private TextView date;
    private TextView contract;
    private AppCompatButton postuler;
    private Button enregistrer;
    private ImageView details_logo;
    private String Offer_ID;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private String Discipline_Metier, Description_Offre, Competences, Niveau_Experience, Date_Limite;
    private DescriptionEntrepriseDetailsFragment descriptionEntrepriseDetailsFragment;
    private boolean alreadyApplied = false;
    private String idcandidature = null;
    private __OffreEnregistree OffreEnregistee = new __OffreEnregistree();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        name = findViewById(R.id.details_company);
        title = findViewById(R.id.details_job_title);
        location = findViewById(R.id.details_location);
        date = findViewById(R.id.details_date);
        contract = findViewById(R.id.details_contract);
        enregistrer = findViewById(R.id.details_save_button);

        String IDOffre = getIntent().getStringExtra("ID_Offre");
        String IDCandidat = getIntent().getStringExtra("ID_Candidat");
        boolean isJobOfferSaved = OffreEnregistee.isJobOfferSaved(IDOffre, IDCandidat);

        if (isJobOfferSaved) {
            enregistrer.setText("Enregistré");
            enregistrer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_bookmark_filled_24_blue, 0, 0, 0);
        } else {
            enregistrer.setText("Enregistrer");
            enregistrer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_bookmark_border_24_blue, 0, 0, 0);
        }

        descriptionEntrepriseDetailsFragment = new DescriptionEntrepriseDetailsFragment();

        enregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String IDOffre = getIntent().getStringExtra("ID_Offre");
                String IDCandidat = getIntent().getStringExtra("ID_Candidat");
                boolean isJobOfferSaved = OffreEnregistee.isJobOfferSaved(IDOffre, IDCandidat);

                if (isJobOfferSaved) {
                    OffreEnregistee.deleteSavedJobOffer(IDOffre, IDCandidat);
                    enregistrer.setText("Enregistrer");
                    enregistrer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_bookmark_border_24_blue, 0, 0, 0);
                } else {
                    OffreEnregistee.insertSavedJobOffer(IDOffre, IDCandidat);
                    enregistrer.setText("Enregistré");
                    enregistrer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_bookmark_filled_24_blue, 0, 0, 0);
                }
            }
        });




        details_logo = findViewById(R.id.details_logo);
        toolbar = findViewById(R.id.detailstoolbar);

        Bundle extras = getIntent().getExtras();
        postuler = findViewById(R.id.details_apply_button);
        name.setText(extras.getString("company_name"));
        title.setText(extras.getString("job_title"));
        location.setText(extras.getString("company_location"));
        date.setText(extras.getString("date_publication"));
        contract.setText(extras.getString("type_contrat"));
        byte[] byteArray = getIntent().getByteArrayExtra("company_logo");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        details_logo.setImageBitmap(bitmap);

        // get data stored in table -Offre-

        Connection connection = new ___ConnectionClass().SQLServerConnection();

        if(connection!=null){
            try {
                String selectSQL = "SELECT Discipline_Metier, Description_Offre, Competences, Niveau_Experience, Date_Limite FROM Offre WHERE ID_Offre = "+ extras.getString("ID_Offre");

                String checkIfPostuleSQL = "SELECT ID_Candidature FROM Candidature WHERE ID_Offre = "+getIntent().getStringExtra("ID_Offre")+" AND ID_Candidat = "+getIntent().getStringExtra("ID_Candidat")+";";

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                if (resultSet.next()) {
                    String disciplineMetier = resultSet.getString("Discipline_Metier");
                    String descriptionOffre = resultSet.getString("Description_Offre");
                    String competences = resultSet.getString("Competences");
                    String niveauExperience = resultSet.getString("Niveau_Experience");
                    String dateLimite = resultSet.getString("Date_Limite");

                    // Pass the values to the fragment
                    DescriptionPosteDetailsFragment fragment = new DescriptionPosteDetailsFragment();
                    Bundle args = new Bundle();
                    args.putString("disciplineMetier", disciplineMetier);
                    args.putString("descriptionOffre", descriptionOffre);
                    args.putString("competences", competences);
                    args.putString("niveauExperience", niveauExperience);
                    args.putString("dateLimite", dateLimite);
                    fragment.setArguments(args);
                }

                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(checkIfPostuleSQL);

                if(resultSet2.next()){
                    alreadyApplied = true;
                    idcandidature = resultSet2.getString("ID_Candidature");
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        if(!alreadyApplied) {

            postuler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(JobDetails.this, Postuler.class);
                    intent.putExtra("ID_Offre", extras.getString("ID_Offre"));
                    intent.putExtra("ID_Candidat", extras.getString("ID_Candidat"));
                    intent.putExtra("Titre", title.getText().toString());
                    intent.putExtra("Entreprise", extras.getString("company_name"));
                    intent.putExtra("Lieu", extras.getString("company_location"));
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
        }
        else{
            postuler.setText("Annuler candidature");
            postuler.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_red));
            postuler.invalidate();
            postuler.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobDetails.this);
                    builder.setTitle("Annuler Candidature");
                    builder.setMessage("Êtes-vous sûr de vouloir annuler votre candidature?");
                    builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Connection connection = new ___ConnectionClass().SQLServerConnection();

                            if(connection!=null){
                                try {
                                    String updateSQL = "DELETE FROM Candidature WHERE ID_Candidature = "+idcandidature+";";
                                    Statement statement = connection.createStatement();
                                    int resultSet = statement.executeUpdate(updateSQL);
                                    if(resultSet>0){
                                        Toast.makeText(JobDetails.this,"Candidature annulée",Toast.LENGTH_LONG).show();
                                        postuler.setBackgroundTintList(ContextCompat.getColorStateList(JobDetails.this, R.color.dark_blue));
                                        postuler.setText("Postuler");
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    builder.setNegativeButton("Non", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            });
        }



        viewPager = findViewById(R.id.view_pager_details);
        setupViewPager(viewPager);

        // Attach the ViewPager to the TabLayout
        tabLayout = findViewById(R.id.tab_layout_details);
        tabLayout.setupWithViewPager(viewPager);


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);


    }

    private void setupViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());

        // Pass the arguments to the DescriptionPosteDetailsFragment
        Connection connection = new ___ConnectionClass().SQLServerConnection();

        if (connection != null) {
            try {
                String selectSQL = "SELECT Discipline_Metier, Description_Offre, Competences, Niveau_Experience, Date_Limite FROM Offre WHERE ID_Offre = " + getIntent().getStringExtra("ID_Offre");

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                if (resultSet.next()) {
                    String disciplineMetier = resultSet.getString("Discipline_Metier");
                    String descriptionOffre = resultSet.getString("Description_Offre");
                    String competences = resultSet.getString("Competences");
                    String niveauExperience = resultSet.getString("Niveau_Experience");
                    String dateLimite = resultSet.getString("Date_Limite");

                    // Pass the values to the fragment
                    DescriptionPosteDetailsFragment descriptionPosteDetailsFragment1 = new DescriptionPosteDetailsFragment();

                    DescriptionEntrepriseDetailsFragment descriptionEntrepriseDetailsFragment1 = new DescriptionEntrepriseDetailsFragment();

                    Bundle args = new Bundle();
                    args.putString("disciplineMetier", disciplineMetier);
                    args.putString("descriptionOffre", descriptionOffre);
                    args.putString("competences", competences);
                    args.putString("niveauExperience", niveauExperience);
                    args.putString("dateLimite", dateLimite);
                    descriptionPosteDetailsFragment1.setArguments(args);

                    Bundle args2 = new Bundle();
                    args2.putString("secteur",getIntent().getStringExtra("secteur"));
                    args2.putString("descriptionEntreprise",getIntent().getStringExtra("company_desc"));
                    descriptionEntrepriseDetailsFragment1.setArguments(args2);

                    adapter.addFragment(descriptionEntrepriseDetailsFragment1, "Entreprise");
                    adapter.addFragment(descriptionPosteDetailsFragment1, "Poste");
                    viewPager.setAdapter(adapter);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragments = new ArrayList<>();
        private final List<String> fragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0,0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

