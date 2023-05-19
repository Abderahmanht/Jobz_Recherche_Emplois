package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class __AjouterOffre extends AppCompatActivity {
    private AutoCompleteTextView secteur;
    private EditText titre;
    private TextView contrat,date,sec_a,titre_a,type_a,desc_a,comp_a,exp_a,data_a;
    private EditText description,comp; TextView exp;
    private StateProgressBar stateProgressBar;
    private Toolbar toolbar;
    private Button btn;
    private String[] stateProgressData = {"Étape 1", "Étape 2", "Finalisation"};
    private LinearLayout etape1,etape2;
    private ScrollView finalisation;
    private RecyclerView recyclerViewAlerte;
    private AlerteNotificationAdapter adaptateurAlertes;
    private List<AlerteNotificationItem> listeAlertes;
    private _AlertesFragmentCand fragment = new _AlertesFragmentCand();




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_offre);



        etape1 = findViewById(R.id.etape1);
        etape2 = findViewById(R.id.etape2);
        finalisation = findViewById(R.id.layout_finalisation_ajout_offre);
        secteur = findViewById(R.id.ajout_offre_secteur);
        titre = findViewById(R.id.ajout_offre_titre_poste);
        contrat = findViewById(R.id.ajout_offre_type_contrat);
        description = findViewById(R.id.ajout_offre_description_offre);
        toolbar = findViewById(R.id.ajout_offre_toolbar);
        stateProgressBar = findViewById(R.id.state_progress_bar_ajout);
        comp = findViewById(R.id.ajout_competences);
        exp = findViewById(R.id.ajout_offre_experience_requis);
        date = findViewById(R.id.ajout_offre_date_expiration);
        sec_a = findViewById(R.id.ajout_offre_apercu_secteur);
        titre_a = findViewById(R.id.ajout_offre_apercu_titre);
        type_a = findViewById(R.id.ajout_offre_apercu_type_contrat);
        desc_a = findViewById(R.id.ajout_offre_apercu_description);
        comp_a = findViewById(R.id.ajout_offre_apercu_competences_requises);
        exp_a = findViewById(R.id.ajout_offre_apercu_experience_requise);
        data_a = findViewById(R.id.ajout_offre_apercu_date_expriration);
        btn = findViewById(R.id.button_ajouter_offre_emploi);
        stateProgressBar.setStateDescriptionData(stateProgressData);
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

        contrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), contrat);
                popupMenu.getMenuInflater().inflate(R.menu.contract_type, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        contrat.setText(menuItem.getTitle());
                        return true;
                    }
                });

                // Show the popup menu
                popupMenu.show();

            }
        });

        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), exp);
                    popupMenu.getMenuInflater().inflate(R.menu.experience_alerte, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            exp.setText(menuItem.getTitle());
                            return true;
                        }
                    });

                    // Show the popup menu
                    popupMenu.show();

            }
        });


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        __AjouterOffre.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        i1++;
                        String dat = i + "-" + i1 + "-" + i2;
                        date.setText(dat);
                    }
                }, year, month, day);

                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            String wilaya;
            @Override
            public void onClick(View view) {
                switch (stateProgressBar.getCurrentStateNumber()) {
                    case 1:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        etape1.setVisibility(View.INVISIBLE);
                        etape2.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
                        etape2.setVisibility(View.INVISIBLE);
                        finalisation.setVisibility(View.VISIBLE);

                        titre_a.setText(titre.getText().toString());
                        sec_a.setText(secteur.getText().toString());
                        type_a.setText(contrat.getText().toString());
                        desc_a.setText(description.getText().toString());
                        data_a.setText(date.getText().toString());
                        comp_a.setText(comp.getText().toString());
                        exp_a.setText(exp.getText().toString());
                        btn.setText("Confirmer");
                        break;
                    case 3:
                        stateProgressBar.setAllStatesCompleted(true);
                        Connection connection = new ___ConnectionClass().SQLServerConnection();
                        int resultSet = 0;
                        if (connection != null) {
                            try {
                                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                String SQLinsert = "INSERT INTO Offre (Discipline_Metier, Titre_Poste, Type_Contrat, Description_Offre, Competences, Niveau_Experience, Date_Publication, Date_Limite, ID_Recruteur) VALUES('" + sec_a.getText() + "','" + titre_a.getText().toString().replace("'", "''") + "','" + type_a.getText().toString().replace("'", "''")+"','"+ desc_a.getText().toString().replace("'", "''") + "','" + comp_a.getText().toString().replace("'", "''") + "','" + exp_a.getText().toString().replace("'", "''") + "','" + currentDate + "','" + data_a.getText() +"',"+getIntent().getStringExtra("ID_Recruteur") +");";
                                Statement statement = connection.createStatement();
                                resultSet = statement.executeUpdate(SQLinsert);

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                        if (resultSet > 0) {
                            try {
                                String query = "SELECT Wilaya_Entreprise FROM Recruteur WHERE ID_Recruteur = " + getIntent().getStringExtra("ID_Recruteur");
                                Statement statement2 = connection.createStatement();
                                ResultSet resultSet1 = statement2.executeQuery(query);
                                if(resultSet1.next()){
                                    wilaya = resultSet1.getString("Wilaya_Entreprise");
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }



                            Snackbar snackbar = Snackbar.make(view, "Offre Ajoutée aves succès", Snackbar.LENGTH_INDEFINITE);
                            matchingAlgorithm(wilaya,exp_a.getText().toString(), sec_a.getText().toString(), titre_a.getText().toString());
                            snackbar.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(__AjouterOffre.this, HomeRec.class);
                                    startActivity(intent);
                                }
                            });
                            snackbar.show();

                            break;
                        }
                }
            }
        });


        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

    }

    public void matchingAlgorithm(String lieu, String exp, String dis, String poste){
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection!=null){
            try {
                String selectAlertesSQL = "SELECT a.Poste_Recherche, a.Lieu_Residence, a.Titre, a.Experience, a.Discipline, c.Email, c.Nom, c.Prenom FROM Alerte_Emploi a JOIN Candidat c ON c.ID_Candidat = a.ID_Candidat";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectAlertesSQL);

                while (resultSet.next()){
                    System.out.println("---------------------------------------------------------------------------------\n\n"+resultSet.getString("Poste_Recherche"));
                        if(lieu.toLowerCase().contains(resultSet.getString("Lieu_Residence").toLowerCase()) &&
                                exp.toLowerCase().contains(resultSet.getString("Experience").toLowerCase()) &&
                                dis.toLowerCase().contains(resultSet.getString("Discipline").toLowerCase()) &&
                                poste.toLowerCase().contains(resultSet.getString("Poste_Recherche").toLowerCase())
                        ){
                            envoyerAlerteEmail(resultSet.getString("Email"),resultSet.getString("Nom")+" "+resultSet.getString("Prenom"));
                        }
                    }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void envoyerAlerteEmail(String recipient, String nomComplet){
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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Alerte Email");
            message.setText("Bonjour "+nomComplet+",\n\n\nde nouvelles offres d'emplois correspondates à votre alerte crée sont disponibles,\nVeuillez retrouver les nouvelles offres dans vos alertes sur l'application");
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }

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