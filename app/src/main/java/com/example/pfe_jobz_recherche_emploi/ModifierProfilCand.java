package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Objects;

public class ModifierProfilCand extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView modifCandNom;
    private EditText mNameCANDEditText;
    private TextView modifCandPrenom;
    private EditText mPrenomCANDEditText,numtel;
    private TextView modifCandNumID;
    private AutoCompleteTextView mNumIDCANDEditText;
    private TextView modifCandVille;
    private AutoCompleteTextView mWilayaCANDEditText;
    private TextView modifCandDate;
    private TextView mDateNaissanceCAND;
    private Button appliquerChangementsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_profil_cand);

        toolbar = findViewById(R.id.modifier_profil_toolbar);
        modifCandNom = findViewById(R.id.modif_cand_nom);
        mNameCANDEditText = findViewById(R.id.mnameCANDedittext);
        modifCandPrenom = findViewById(R.id.modif_cand_prenom);
        mPrenomCANDEditText = findViewById(R.id.mprenomCANDedittext);
        modifCandNumID = findViewById(R.id.modif_cand_numID);
        mNumIDCANDEditText = findViewById(R.id.mnumIDCANDedittext);
        modifCandVille = findViewById(R.id.modif_cand_ville);
        mWilayaCANDEditText = findViewById(R.id.mwilayaCANDedittext);
        modifCandDate = findViewById(R.id.modif_cand_date);
        mDateNaissanceCAND = findViewById(R.id.mdatenaissanceCAND);
        appliquerChangementsButton = findViewById(R.id.appliquer_changements_button);
        numtel = findViewById(R.id.mnumCANDedittext);

        String[] wilayas = {
                "Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaïa", "Biskra", "Béchar", "Blida", "Bouira",
                "Tamanrasset", "Tébessa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saïda",
                "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma", "Constantine", "Médéa", "Mostaganem", "M'Sila", "Mascara",
                "Ouargla", "Oran", "El Bayadh", "Illizi", "Bordj Bou Arreridj", "Boumerdès", "El Tarf", "Tindouf",
                "Tissemsilt", "El Oued", "Khenchela", "Souk Ahras", "Tipaza", "Mila", "Aïn Defla", "Naâma", "Aïn Témouchent",
                "Ghardaïa", "Relizane"
        };




        //endregion wilayas


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ModifierProfilCand.this, android.R.layout.simple_list_item_1, wilayas);
        mWilayaCANDEditText.setAdapter(adapter);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.YEAR, 2010);


        mDateNaissanceCAND.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ModifierProfilCand.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1++;
                        String date = i + "-" + i1 + "-" + i2;
                        mDateNaissanceCAND.setText(date);
                    }
                }, year, month, day);

                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });


        fetchData();

        appliquerChangementsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);


    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void fetchData() {
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        String idCandidat = getIntent().getStringExtra("ID_Candidat");

        try {
            Statement statement = connection.createStatement();
            String query = "SELECT Nom, Prenom, Num_ID_National, Ville, Date_Naissance, Num_Tel FROM Candidat WHERE ID_Candidat = " + idCandidat;
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                String nom = resultSet.getString("Nom");
                String prenom = resultSet.getString("Prenom");
                String numID = resultSet.getString("Num_ID_National");
                String wilaya = resultSet.getString("Ville");
                String dateNaissance = resultSet.getString("Date_Naissance");
                String num = resultSet.getString("Num_Tel");

                mNameCANDEditText.setText(nom);
                mPrenomCANDEditText.setText(prenom);
                mNumIDCANDEditText.setText(numID);
                mWilayaCANDEditText.setText(wilaya);
                mDateNaissanceCAND.setText(dateNaissance);
                numtel.setText(num);
            }

            resultSet.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData() {

        String idCandidat = getIntent().getStringExtra("ID_Candidat");

        String nom = mNameCANDEditText.getText().toString();
        String prenom = mPrenomCANDEditText.getText().toString();
        String numID = mNumIDCANDEditText.getText().toString();
        String wilaya = mWilayaCANDEditText.getText().toString();
        String dateNaissance = mDateNaissanceCAND.getText().toString();
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        try {
            Statement statement = connection.createStatement();
            String query = "UPDATE Candidat SET Nom = '" + nom + "', Prenom = '" + prenom + "', Num_ID_National = '" + numID + "', Ville = '" + wilaya + "', Date_Naissance = '" + dateNaissance + "' WHERE ID_Candidat = " + idCandidat;
            int rowsAffected = statement.executeUpdate(query);

            if (rowsAffected > 0) {
                // Record updated successfully
                Toast.makeText(getApplicationContext(), "Informations modifiées avec succès !", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // Failed to update the record
                Toast.makeText(getApplicationContext(), "Erreur, veuillez réssayer", Toast.LENGTH_SHORT).show();
            }

            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}