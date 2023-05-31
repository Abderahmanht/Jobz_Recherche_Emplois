package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.snackbar.Snackbar;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;


public class CreateRECAccount extends AppCompatActivity {

    private Button continuer,upload; ImageButton viewmdp;
    private StateProgressBar stateProgressBarrec;
    private RelativeLayout informations, entreprise; ScrollView finalisation;
    private String[] descriptionDataRec = {"Informations", "Entreprise", "Finalisation"};

    private EditText nom,prenom,num,email,mdp,nomentreprise,sect,desc;
    private AutoCompleteTextView wilaya;
    private TextView noma,prenoma,numa,emaila,mdpa,nomentreprisea,secta,wilayaa,desca;

    private ImageView logo,logoa;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    private int resultSet;
    private Toolbar toolbarREC;

    TextView error_nom;
    TextView error_prenom;
    TextView error_wilaya;
    TextView error_entreprise;
    TextView error_email;
    TextView error_email_incorrect;
    TextView error_email_already;
    TextView error_pass;
    TextView error_pass_court;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recaccount);


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

        error_nom = findViewById(R.id.error_nomr);
        error_prenom = findViewById(R.id.error_prenomr);
        error_wilaya = findViewById(R.id.error_lieur);
        error_entreprise = findViewById(R.id.error_entrepriser);
        error_email = findViewById(R.id.error_emailr);
        error_email_incorrect = findViewById(R.id.error_email_incorrectr);
        error_email_already = findViewById(R.id.error_email_alreadyr);
        error_pass = findViewById(R.id.error_passr);
        error_pass_court = findViewById(R.id.error_pass_courtr);




        nom = findViewById(R.id.nameRECedittext);
        noma = findViewById(R.id.aperçu_nom_rec);
        prenom = findViewById(R.id.prenomRECedittext);
        prenoma = findViewById(R.id.aperçu_prenom_rec);
        num = findViewById(R.id.numRECedittext);
        numa = findViewById(R.id.aperçu_num_rec);
        email = findViewById(R.id.emailRECedittext);
        emaila = findViewById(R.id.aperçu_email_rec);
        mdp = findViewById(R.id.mdpRECedittext);
        mdpa = findViewById(R.id.aperçu_mdp_rec);
        nomentreprise = findViewById(R.id.entrepriseRECedittext);
        nomentreprisea = findViewById(R.id.aperçu_nom_entreprise_rec);
        sect = findViewById(R.id.secactiviteRECedittext);
        secta = findViewById(R.id.aperçu_sect_entreprise_rec);
        wilaya = findViewById(R.id.lieuactiviteRECedittext);
        wilayaa = findViewById(R.id.aperçu_wilaya_entreprise_rec);
        desc = findViewById(R.id.descriptionRECedittext);
        desca = findViewById(R.id.aperçu_description_rec);
        logo = findViewById(R.id.companyLogoRec);
        logoa = findViewById(R.id.aperçu_logo);
        viewmdp = findViewById(R.id.view_mdp_rec);

        continuer = findViewById(R.id.continueButtonREC);
        upload = findViewById(R.id.uploadlogoREC);

        informations = (RelativeLayout) findViewById(R.id.layout_informations_rec);
        entreprise = (RelativeLayout) findViewById(R.id.layout_compte_rec);
        finalisation = findViewById(R.id.layout_finalisation_rec);

        stateProgressBarrec = findViewById(R.id.state_progress_bar_rec);
        stateProgressBarrec.setStateDescriptionData(descriptionDataRec);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateRECAccount.this, android.R.layout.simple_list_item_1, wilayas);
        wilaya.setAdapter(adapter);

        toolbarREC = findViewById(R.id.toolbarrec);
        setSupportActionBar(toolbarREC);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);





        continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateProgressBarrec.getCurrentStateNumber()) {
                    case 1:
                        boolean allCorrect = true;
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                        if(nom.getText().toString().isEmpty()){
                            allCorrect = false;
                            error_nom.setVisibility(View.VISIBLE);
                        }
                        if(prenom.getText().toString().isEmpty()){
                            allCorrect = false;
                            error_prenom.setVisibility(View.VISIBLE);
                        }
                        if(email.getText().toString().isEmpty()){
                            allCorrect = false;
                            error_email.setVisibility(View.VISIBLE);
                        }else if (!email.getText().toString().matches(emailPattern)) {
                            allCorrect = false;
                        } else if (alreadyExists(email.getText().toString())) {
                            allCorrect = false;
                            error_email_incorrect.setVisibility(View.INVISIBLE);
                            error_email_already.setVisibility(View.VISIBLE);
                        }
                        if(mdp.getText().toString().isEmpty()){
                            allCorrect = false;
                            error_pass.setVisibility(View.VISIBLE);
                        } else if (mdp.getText().toString().length() < 8) {
                            error_pass.setVisibility(View.INVISIBLE);
                            error_pass_court.setVisibility(View.VISIBLE);
                        }


                        if(allCorrect) {
                            stateProgressBarrec.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                            informations.setVisibility(View.INVISIBLE);
                            entreprise.setVisibility(View.VISIBLE);
                            break;
                        }

                        break;
                    case 2:
                        boolean allCorrect2 = true;
                        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                        if(nomentreprise.getText().toString().isEmpty()){
                            error_entreprise.setVisibility(View.VISIBLE);
                            allCorrect2 = false;
                        }
                        if(wilaya.getText().toString().isEmpty()){
                            error_wilaya.setVisibility(View.VISIBLE);
                            allCorrect2 = false;
                        }

                        if(allCorrect2){
                            stateProgressBarrec.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                            entreprise.setVisibility(View.INVISIBLE);
                            finalisation.setVisibility(View.VISIBLE);

                            noma.setText(nom.getText().toString());
                            prenoma.setText(prenom.getText().toString());
                            nomentreprisea.setText(nomentreprise.getText().toString());
                            secta.setText(sect.getText().toString());
                            desca.setText(desc.getText().toString());
                            wilayaa.setText(wilaya.getText().toString());
                            numa.setText(num.getText().toString());
                            emaila.setText(email.getText().toString());
                            mdpa.setText(mdp.getText().toString());

                            viewmdp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mdpa.setInputType(InputType.TYPE_CLASS_TEXT);
                                }


                            });

                            continuer.setText("Confirmer");
                            break;
                        }
                        else{

                            if (email.getText().toString().isEmpty()) {
                                error_email.setVisibility(View.VISIBLE);
                                error_email_incorrect.setVisibility(View.INVISIBLE);
                                error_email_already.setVisibility(View.INVISIBLE);
                            } else if (!email.getText().toString().matches(emailPattern2)) {
                                error_email.setVisibility(View.INVISIBLE);
                                error_email_incorrect.setVisibility(View.VISIBLE);
                                error_email_already.setVisibility(View.INVISIBLE);
                            } else if (alreadyExists(email.getText().toString())) {
                                error_email.setVisibility(View.INVISIBLE);
                                error_email_incorrect.setVisibility(View.INVISIBLE);
                                error_email_already.setVisibility(View.VISIBLE);
                            }

                            if (mdp.getText().toString().isEmpty()) {
                                error_pass.setVisibility(View.VISIBLE);
                                error_pass_court.setVisibility(View.INVISIBLE);
                            } else if (mdp.getText().toString().length() < 8) {
                                error_pass.setVisibility(View.INVISIBLE);
                                error_pass_court.setVisibility(View.VISIBLE);
                            }
                        }
                        break;


                    case 3:
                        byte[] byteArray = null;
                        stateProgressBarrec.setAllStatesCompleted(true);
                        Connection connection = new ___ConnectionClass().SQLServerConnection();
                        if (connection != null) {
                            try {
                                if(imageToStore!=null) {
                                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    imageToStore.compress(Bitmap.CompressFormat.PNG, 0, stream);
                                    byteArray = stream.toByteArray();
                                }
                                // Insert data into database
                                String SQLinsert = "INSERT INTO Recruteur (Nom, Prenom, Entreprise, Description_Entreprise, Wilaya_Entreprise, Secteur_Activite, Num_Tel, Email, Mot_de_passe, Logo_Entreprise, Statut_Compte) VALUES('" + noma.getText() + "','" + prenoma.getText() + "','" + nomentreprisea.getText() + "','" + desca.getText().toString().replace("'", "''") + "','" + wilayaa.getText() + "','" + secta.getText() + "','" + numa.getText() + "','" + emaila.getText() + "','" + mdpa.getText() + "', ?,0);";
                                PreparedStatement statement = connection.prepareStatement(SQLinsert);
                                statement.setBytes(1, byteArray);
                                resultSet = statement.executeUpdate();


                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            if (resultSet > 0) {
                                Snackbar snackbar = Snackbar.make(view, "Compte créé avec succès", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("SE CONNECTER", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(CreateRECAccount.this, Login.class);
                                        startActivity(intent);
                                    }
                                });
                                snackbar.show();
                            }

                            break;
                        }
                }

            }

        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 1000);
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

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==1000){
                imageFilePath = data.getData();
                logo.setImageURI(imageFilePath);
                logoa.setImageURI(imageFilePath);
                try {
                    imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean alreadyExists(String email){
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection!=null){
            try{
                String query = "SELECT Email FROM Recruteur WHERE Email = '"+email+"'";
                Statement statement = connection.createStatement();
                ResultSet resultSet1 = statement.executeQuery(query);

                if(resultSet1.next()){
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

}