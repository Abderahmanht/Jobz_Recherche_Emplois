package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.provider.OpenableColumns;
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
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class CreateCANDAccount extends AppCompatActivity {

    AutoCompleteTextView secteur;
    private TextView etudes;
    private TextView experience;

    private Button continuer, telechargerCV;
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
    private RelativeLayout info, cv, compte, aperçu;
    private Toolbar toolbar;
    private int resultSet;
    private TextView noma, prenoma, numIDa, datea, villea;
    private TextView numa, emaila, mdpa;
    private static final int PICKFILE_CV_RESULT_CODE = 1;
    private byte[] CVfileBytes;
    String fileName = null;

    TextView error_nom;
    TextView error_prenom;
    TextView error_numID;
    TextView error_wilaya;
    TextView error_date;
    TextView error_email;
    TextView error_email_incorrect;
    TextView error_email_already;
    TextView error_pass;
    TextView error_pass_court;

    private String[] stateProgressData = {"Informations\npersonnelles", "CV", "Informations\ndu compte", "Finalisation"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_candaccount);

        error_nom = findViewById(R.id.error_nom);
        error_prenom = findViewById(R.id.error_prenom);
        error_numID = findViewById(R.id.error_numID);
        error_wilaya = findViewById(R.id.error_wilaya);
        error_date = findViewById(R.id.error_date);
        error_email = findViewById(R.id.error_email);
        error_email_incorrect = findViewById(R.id.error_email_incorrect);
        error_email_already = findViewById(R.id.error_email_already);
        error_pass = findViewById(R.id.error_pass);
        error_pass_court = findViewById(R.id.error_pass_court);

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
        secteur = findViewById(R.id.secteurCvCANDedittext);
        telechargerCV = findViewById(R.id.cand_cv_charger_cv);

        etudes = findViewById(R.id.etudesCvCANDedittext);
        experience = findViewById(R.id.experienceCvCANDedittext);

        experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(CreateCANDAccount.this, experience);
                popupMenu.getMenuInflater().inflate(R.menu.experience_alerte, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        experience.setText(item.getTitle());
                        return true;
                    }
                });
                popupMenu.show();
            }

        });

        etudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(CreateCANDAccount.this, etudes);
                popupMenu.getMenuInflater().inflate(R.menu.etudes, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        etudes.setText(item.getTitle());
                        return true;
                    }
                });
                popupMenu.show();
            }

        });


        //region wilayas
        String[] wilayas = {
                "Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaïa", "Biskra", "Béchar", "Blida", "Bouira",
                "Tamanrasset", "Tébessa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saïda",
                "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma", "Constantine", "Médéa", "Mostaganem", "M'Sila", "Mascara",
                "Ouargla", "Oran", "El Bayadh", "Illizi", "Bordj Bou Arreridj", "Boumerdès", "El Tarf", "Tindouf",
                "Tissemsilt", "El Oued", "Khenchela", "Souk Ahras", "Tipaza", "Mila", "Aïn Defla", "Naâma", "Aïn Témouchent",
                "Ghardaïa", "Relizane"
        };
        //region disciciplines
        String[] disciplines = {"Informatique", "Droit", "Marketing", "Médecine", "Ingénierie", "Finance", "Design", "Éducation", "Communication", "Arts",
                "Administration publique",
                "Anthropologie",
                "Archéologie",
                "Architecture",
                "Artisanat",
                "Biologie",
                "Chimie",
                "Cinématographie",
                "Communication",
                "Création littéraire",
                "Danse",
                "Design graphique",
                "Droit",
                "Économie",
                "Éducation",
                "Enseignement des langues",
                "Génie civil",
                "Génie électrique",
                "Génie mécanique",
                "Géographie",
                "Géologie",
                "Gestion",
                "Histoire",
                "Informatique",
                "Ingénierie",
                "Journalisme",
                "Langues étrangères",
                "Linguistique",
                "Marketing",
                "Mathématiques",
                "Médecine",
                "Météorologie",
                "Musique",
                "Neurosciences",
                "Nutrition",
                "Optométrie",
                "Philosophie",
                "Physique",
                "Psychologie",
                "Publicité",
                "Relations internationales",
                "Relations publiques",
                "Sciences de l'environnement",
                "Sciences de la Terre",
                "Sciences politiques",
                "Sociologie",
                "Théâtre",
                "Tourisme",
                "Traduction",
                "Astronomie",
                "Bio-informatique",
                "Biostatistique",
                "Chimie analytique",
                "Chirurgie plastique",
                "Cybersécurité",
                "Design d'intérieur",
                "Design industriel",
                "Éducation physique",
                "Études de genre",
                "Génétique",
                "Gérontologie",
                "Gestion des ressources humaines",
                "Graphisme",
                "Hydrologie",
                "Immunologie",
                "Informatique décisionnelle",
                "Ingénierie biomédicale",
                "Ingénierie environnementale",
                "Mécatronique",
                "Météorologie",
                "Neurobiologie",
                "Océanographie",
                "Odontologie",
                "Optique",
                "Orthophonie",
                "Pédagogie",
                "Pharmacologie",
                "Planification urbaine",
                "Robotique"
        };


        //endregion disciplines
        HashSet<String> setWithoutDuplicates = new HashSet<>(Arrays.asList(disciplines));
        String[] arrWithoutDuplicates = setWithoutDuplicates.toArray(new String[setWithoutDuplicates.size()]);
        ArrayAdapter<String> adapter_dis = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrWithoutDuplicates);
        secteur.setAdapter(adapter_dis);


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
        cv = (RelativeLayout) findViewById(R.id.layout_cv_cand);
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

        telechargerCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_CV_RESULT_CODE);

            }
        });


        continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateProgressBar.getCurrentStateNumber()) {
                    case 1:
                        boolean allCorrect = true;

                        if (nom.getText().toString().isEmpty()) {
                            allCorrect = false;
                            error_nom.setVisibility(View.VISIBLE);
                        }
                        if (prenom.getText().toString().isEmpty()) {
                            allCorrect = false;
                            error_prenom.setVisibility(View.VISIBLE);
                        }
                        if (numIDNational.getText().toString().isEmpty()) {
                            allCorrect = false;
                            error_numID.setVisibility(View.VISIBLE);
                        }
                        if (wilaya.getText().toString().isEmpty()) {
                            allCorrect = false;
                            error_wilaya.setVisibility(View.VISIBLE);
                        }
                        if (dateN.getText().toString().isEmpty()) {
                            allCorrect = false;
                            error_date.setVisibility(View.VISIBLE);
                        }

                        if (allCorrect) {
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                            info.setVisibility(View.INVISIBLE);
                            cv.setVisibility(View.VISIBLE);
                            break;
                        }

                    case 2:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        cv.setVisibility(View.INVISIBLE);
                        compte.setVisibility(View.VISIBLE);
                        break;

                    case 3:
                        boolean allCorrect2 = true;

                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                        if (email.getText().toString().isEmpty()) {
                            allCorrect2 = false;
                        } else if (!email.getText().toString().matches(emailPattern)) {
                            allCorrect2 = false;
                        } else if (alreadyExists(email.getText().toString())) {
                            allCorrect2 = false;
                        }

                        if (motdepasse.getText().toString().isEmpty()) {
                            allCorrect2 = false;
                        } else if (motdepasse.getText().toString().length() < 8) {
                            allCorrect2 = false;
                        }

                        if (allCorrect2) {
                            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);

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
                        } else {
                            // Display email and password errors
                            if (email.getText().toString().isEmpty()) {
                                error_email.setVisibility(View.VISIBLE);
                                error_email_incorrect.setVisibility(View.INVISIBLE);
                                error_email_already.setVisibility(View.INVISIBLE);
                            } else if (!email.getText().toString().matches(emailPattern)) {
                                error_email.setVisibility(View.INVISIBLE);
                                error_email_incorrect.setVisibility(View.VISIBLE);
                                error_email_already.setVisibility(View.INVISIBLE);
                            } else if (alreadyExists(email.getText().toString())) {
                                error_email.setVisibility(View.INVISIBLE);
                                error_email_incorrect.setVisibility(View.INVISIBLE);
                                error_email_already.setVisibility(View.VISIBLE);
                            }

                            if (motdepasse.getText().toString().isEmpty()) {
                                error_pass.setVisibility(View.VISIBLE);
                                error_pass_court.setVisibility(View.INVISIBLE);
                            } else if (motdepasse.getText().toString().length() < 8) {
                                error_pass.setVisibility(View.INVISIBLE);
                                error_pass_court.setVisibility(View.VISIBLE);
                            }
                        }
                        break;

                    case 4:
                        Connection connection = new ___ConnectionClass().SQLServerConnection();
                        if (connection != null) {
                            try {
                                String SQLinsert = "INSERT INTO Candidat (Nom, Prenom, Num_ID_National, Date_Naissance, Ville, Num_Tel, Email, Mot_de_passe, Statut_Compte) VALUES('" + noma.getText() + "','" + prenoma.getText() + "','" + numIDa.getText() + "','" + datea.getText() + "','" + villea.getText() + "','" + numa.getText() + "','" + emaila.getText() + "','" + mdpa.getText() + "',1" + ");";

                                // Execute the INSERT statement to add the new Candidat
                                PreparedStatement statement = connection.prepareStatement(SQLinsert, Statement.RETURN_GENERATED_KEYS);
                                statement.executeUpdate();

                                // Retrieve the generated ID_Candidat
                                ResultSet generatedKeys = statement.getGeneratedKeys();
                                int ID_Candidat = 0;
                                if (generatedKeys.next()) {
                                    ID_Candidat = generatedKeys.getInt(1);
                                }

                                // Insert the CVfileBytes into the CV table with the corresponding ID_Candidat
                                String SQLinsertCV = "INSERT INTO CV (ID_Candidat, CV_fichier, CV_nom_fichier, Secteur, Experience, Etudes) VALUES (?, ?, ?, ?, ?, ?)";
                                PreparedStatement statementCV = connection.prepareStatement(SQLinsertCV);
                                statementCV.setInt(1, ID_Candidat);
                                statementCV.setBytes(2, CVfileBytes);
                                statementCV.setString(3,fileName);
                                statementCV.setString(4, secteur.getText().toString());
                                statementCV.setString(5,experience.getText().toString());
                                statementCV.setString(6,etudes.getText().toString());
                                resultSet = statementCV.executeUpdate();

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            if (resultSet > 0) {
                                stateProgressBar.setAllStatesCompleted(true);
                                sendEmail(emaila.getText().toString().trim());
                                Snackbar snackbar = Snackbar.make(view, "Compte créé avec succès", Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("SE CONNECTER", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(CreateCANDAccount.this, Login.class);
                                        intent.putExtra("Secteur", secteur.getText().toString());
                                        startActivity(intent);
                                    }
                                });
                                snackbar.show();

                                break;
                            }
                        }
                }


        }

        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendEmail(String emailAddress) {
        final String username = "jobzrechercheemplois@gmail.com";
        final String password = "uynpnrhwrqzaekls";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress));
            message.setSubject("Bienvenue sur Jobz!");
            message.setText("Bonjour " + nom.getText() + " " + prenom.getText() + ",\n\nVotre compte a été crée avec succès\nMerci d'avoir crée un compte Jobz, Vous pouvez desormais rechercher, enregistrer et candidater au postes qui vous conviennent parfaitement");
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean alreadyExists(String email) {
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection != null) {
            try {
                String query = "SELECT Email FROM Candidat WHERE Email = '" + email + "'";
                Statement statement = connection.createStatement();
                ResultSet resultSet1 = statement.executeQuery(query);

                if (resultSet1.next()) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_CV_RESULT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                fileName = null;
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    fileName = cursor.getString(nameIndex);
                    telechargerCV.setText(fileName);
                    telechargerCV.setTextColor(Color.parseColor("#0CAA41"));
                    cursor.close();
                }


                // Get the file data as a byte array
                InputStream inputStream = getContentResolver().openInputStream(uri);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, length);
                }
                CVfileBytes = outputStream.toByteArray();
                inputStream.close();
                outputStream.close();

                // You now have the file name and data as a byte array (fileName, fileBytes)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}

