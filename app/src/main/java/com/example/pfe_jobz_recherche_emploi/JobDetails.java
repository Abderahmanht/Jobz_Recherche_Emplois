package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class JobDetails extends AppCompatActivity {

    private TextView name;
    private TextView title;
    private Toolbar toolbar;
    private TextView location;
    private TextView date;
    private TextView contract;
    private TextView comp_description;
    private TextView job_description;
    private TextView competences;
    private TextView experience;
    private Button postuler;
    private Button enregistrer;
    private ImageView details_logo;
    private String Offer_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        name = findViewById(R.id.details_company);
        title = findViewById(R.id.details_job_title);
        location = findViewById(R.id.details_location);
        date = findViewById(R.id.details_date);
        contract = findViewById(R.id.details_contract);
        comp_description = findViewById(R.id.details_company_description);
        job_description = findViewById(R.id.details_job_description);
        competences = findViewById(R.id.details_required_skills);
        experience = findViewById(R.id.details_experience);
        details_logo = findViewById(R.id.details_logo);
        toolbar = findViewById(R.id.detailstoolbar);

        Bundle extras = getIntent().getExtras();
        postuler = findViewById(R.id.details_apply_button);
        name.setText(extras.getString("company_name"));
        title.setText(extras.getString("job_title"));
        location.setText(extras.getString("company_location"));
        date.setText(extras.getString("date_publication"));
        contract.setText(extras.getString("type_contrat"));
        comp_description.setText(extras.getString("company_desc"));
        byte[] byteArray = getIntent().getByteArrayExtra("company_logo");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        details_logo.setImageBitmap(bitmap);

        // get data stored in table -Offre-

        Connection connection = new ___ConnectionClass().SQLServerConnection();

        if(connection!=null){
            try {
                String selectSQL = "SELECT Description_Offre, Competences, Niveau_Experience FROM Offre WHERE ID_Offre = "+ extras.getString("ID_Offre");


                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                if(resultSet.next()){
                    job_description.setText(resultSet.getString("Description_Offre"));
                    competences.setText(resultSet.getString("Competences"));
                    experience.setText(resultSet.getString("Niveau_Experience"));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        postuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(JobDetails.this, Postuler.class);
                intent.putExtra("ID_Offre",extras.getString("ID_Offre"));
                intent.putExtra("ID_Candidat",extras.getString("ID_Candidat"));
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);


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