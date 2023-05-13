package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Objects;

public class CreateCANDAccount extends AppCompatActivity {

    private Button continuer;
    ImageButton viewmdp;
    private EditText nom;
    private EditText prenom;
    private TextView dateN;
    private EditText email;
    private EditText motdepasse;
    private EditText num;
    private EditText numIDNational;
    private AutoCompleteTextView wilaya;
    private TextView nomt, prenomt, datet, villet;
    private TextView numt, emailt, mdpt;
    private StateProgressBar stateProgressBar;
    private RelativeLayout info, compte, aperçu;
    private Toolbar toolbar;
    private int resultSet;
    private TextView noma, prenoma,numIDa, datea, villea;
    private TextView numa, emaila, mdpa;

    private String[] stateProgressData = {"Informations", "Compte", "Finalisation"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_candaccount);

        continuer = findViewById(R.id.continueButtonCAND);
        viewmdp = findViewById(R.id.view_mdp);
        nom = findViewById(R.id.nameCANDedittext);
        nomt = findViewById(R.id.create_cand_nom);
        prenom = findViewById(R.id.prenomCANDedittext);
        prenomt = findViewById(R.id.create_cand_prenom);
        dateN = findViewById(R.id.datenaissanceCAND);
        datet = findViewById(R.id.create_cand_date);
        numIDNational = findViewById(R.id.numIDCANDedittext);
        email = findViewById(R.id.emailCANDedittext);
        emailt = findViewById(R.id.create_cand_email);
        motdepasse = findViewById(R.id.passCANDedittext);
        mdpt = findViewById(R.id.create_cand_pass);
        wilaya = findViewById(R.id.wilayaCANDedittext);
        //region wilayas
        String[] wilayas = {
                "Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaïa", "Biskra", "Béchar", "Blida", "Bouira",
                "Tamanrasset", "Tébessa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saïda",
                "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma", "Constantine", "Médéa", "Mostaganem", "M'Sila", "Mascara",
                "Ouargla", "Oran", "El Bayadh", "Illizi", "Bordj Bou Arreridj", "Boumerdès", "El Tarf", "Tindouf",
                "Tissemsilt", "El Oued", "Khenchela", "Souk Ahras", "Tipaza", "Mila", "Aïn Defla", "Naâma", "Aïn Témouchent",
                "Ghardaïa", "Relizane"
        };




        //endregion wilayas


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateCANDAccount.this, android.R.layout.simple_list_item_1, wilayas);
        wilaya.setAdapter(adapter);

        num = findViewById(R.id.numCANDedittext);
        numt = findViewById(R.id.create_cand_num);
        toolbar = findViewById(R.id.toolbarcand);
        toolbar.setTitle("Inscription");

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        //region aperçu
        noma = findViewById(R.id.aperçu_nom);
        prenoma = findViewById(R.id.aperçu_prenom);
        datea = findViewById(R.id.aperçu_date);
        villea = findViewById(R.id.aperçu_ville);
        numIDa = findViewById(R.id.aperçu_numID);
        numa = findViewById(R.id.aperçu_num);
        emaila = findViewById(R.id.aperçu_email);
        mdpa = findViewById(R.id.aperçu_mdp);
        //endregion aperçu

        info = (RelativeLayout) findViewById(R.id.layout_informations_cand);
        compte = (RelativeLayout) findViewById(R.id.layout_compte_cand);
        aperçu = (RelativeLayout) findViewById(R.id.layout_finalisation);

        stateProgressBar = findViewById(R.id.state_progress_bar);
        stateProgressBar.setStateDescriptionData(stateProgressData);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.YEAR, 2010);


        dateN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateCANDAccount.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1++;
                        String date = i + "-" + i1 + "-" + i2;
                        dateN.setText(date);
                    }
                }, year, month, day);

                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });


        continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateProgressBar.getCurrentStateNumber()) {
                    case 1:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        info.setVisibility(View.INVISIBLE);
                        compte.setVisibility(View.VISIBLE);

                        break;
                    case 2:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        compte.setVisibility(View.INVISIBLE);
                        aperçu.setVisibility(View.VISIBLE);

                        noma.setText(nom.getText().toString());
                        prenoma.setText(prenom.getText().toString());
                        numIDa.setText(numIDNational.getText().toString());
                        datea.setText(dateN.getText().toString());
                        villea.setText(wilaya.getText().toString());
                        numa.setText(num.getText().toString());
                        emaila.setText(email.getText().toString());
                        mdpa.setText(motdepasse.getText().toString());
                        viewmdp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                    mdpa.setInputType(InputType.TYPE_CLASS_TEXT);


                            }
                        });
                        continuer.setText("Confirmer");
                        break;
                    case 3:
                        stateProgressBar.setAllStatesCompleted(true);
                        Connection connection = new ___ConnectionClass().SQLServerConnection();
                        if (connection != null) {
                            try {
                                System.out.println("---------------------------------------------------------------\n\n\n\n\n\n"+noma);
                                String SQLinsert = "INSERT INTO Candidat (Nom, Prenom, Num_ID_National, Date_Naissance, Ville, Num_Tel, Email, Mot_de_passe) VALUES('" + noma.getText() + "','" + prenoma.getText() + "','" +numIDa.getText()+"','"+ datea.getText() + "','" + villea.getText() + "','" + numa.getText() + "','" + emaila.getText() + "','" + mdpa.getText() + "');";
                                Statement statement = connection.createStatement();
                                resultSet = statement.executeUpdate(SQLinsert);

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                        if (resultSet > 0) {
                            Snackbar snackbar = Snackbar.make(view, "Compte créé avec succès", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("SE CONNECTER", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(CreateCANDAccount.this, Login.class);
                                    startActivity(intent);
                                }
                            });
                            snackbar.show();

                            break;
                        }
                }


            }
        });
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
    }

