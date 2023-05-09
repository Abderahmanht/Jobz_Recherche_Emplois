package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Step2AjoutOffre extends AppCompatActivity {

    TextView dateLimite;
    Button retour,saveOffre;
    EditText jobdesc,competences,exp;
    int resultSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step2_ajout_offre);

        dateLimite = findViewById(R.id.datelimite);
        retour = findViewById(R.id.retourOffre);
        saveOffre = findViewById(R.id.saveOffre);
        jobdesc = findViewById(R.id.jobDescription);
        competences = findViewById(R.id.competences);
        exp = findViewById(R.id.experience);

        Bundle extras = getIntent().getExtras();
        String discipline = extras.getString("discipline"),
                titreposte = extras.getString("titreposte"),
               typecontrat = extras.getString("type"),
               salaire = extras.getString("salaire");

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dateLimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Step2AjoutOffre.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1++;
                        String date = i +"-"+i1+"-"+i2;
                        dateLimite.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        saveOffre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String datelimite = dateLimite.getText().toString(),
                       description = jobdesc.getText().toString(),
                       comp = competences.getText().toString(),
                       ex = exp.getText().toString();
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                Connection connection = new ___ConnectionClass().connectionClass();
                if(connection != null) {
                    try {
                        String insertSQL = "INSERT INTO Offre (Discipline_Metier, Titre_Poste, Type_Contrat, Description_Offre, Competences, Niveau_Experience, Salaire, Date_Publication, Date_Limite, ID_Recruteur) VALUES ('"+discipline+"','" + titreposte + "','" + typecontrat + "','" + description + "','" + comp + "','" + ex.replace("'", "''") + "','" + salaire + "','" + currentDate + "','" + datelimite + "','"+extras.getString("ID_Recruteur")+"');";

                        Statement statement = connection.createStatement();
                        resultSet = statement.executeUpdate(insertSQL);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    if(resultSet>0){
                        RelativeLayout relativeLayout = findViewById(R.id.relativeLayoutAjoutOffre);
                        Snackbar snackbar = Snackbar.make(relativeLayout, "Offre ajoutée avec succès !", Snackbar.LENGTH_LONG);
                        snackbar.setAction("GO LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Step2AjoutOffre.this, Login.class));
                            }
                        });
                        snackbar.show();
                    }
                }


            }
        });



        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);

            }
        });

}
}