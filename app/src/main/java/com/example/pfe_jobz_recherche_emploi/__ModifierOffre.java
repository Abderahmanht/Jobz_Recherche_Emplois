package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

public class __ModifierOffre extends AppCompatActivity {
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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_offre);

        etape1 = findViewById(R.id.etape1);
        etape2 = findViewById(R.id.etape2);
        finalisation = findViewById(R.id.layout_finalisation_m_offre);
        secteur = findViewById(R.id.m_offre_secteur);
        titre = findViewById(R.id.m_offre_titre_poste);
        contrat = findViewById(R.id.m_offre_type_contrat);
        description = findViewById(R.id.m_offre_description_offre);
        toolbar = findViewById(R.id.m_offre_toolbar);
        stateProgressBar = findViewById(R.id.state_progress_bar_m);
        comp = findViewById(R.id.m_competences);
        exp = findViewById(R.id.m_offre_experience_requis);
        date = findViewById(R.id.m_offre_date_expiration);
        sec_a = findViewById(R.id.m_offre_apercu_secteur);
        titre_a = findViewById(R.id.m_offre_apercu_titre);
        type_a = findViewById(R.id.m_offre_apercu_type_contrat);
        desc_a = findViewById(R.id.m_offre_apercu_description);
        comp_a = findViewById(R.id.m_offre_apercu_competences_requises);
        exp_a = findViewById(R.id.m_offre_apercu_experience_requise);
        data_a = findViewById(R.id.m_offre_apercu_date_expriration);
        btn = findViewById(R.id.button_m_offre_emploi);
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

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection!=null){
            try{
                String selectSQL = "SELECT Discipline_Metier, Titre_Poste, Type_Contrat, Description_Offre, Competences, Niveau_Experience, Date_Publication, Date_Limite, ID_Recruteur FROM Offre WHERE ID_Offre = '"+getIntent().getStringExtra("ID_Offre")+"';";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);
                if (resultSet.next()){
                    secteur.setText(resultSet.getString("Discipline_Metier"));
                    titre.setText(resultSet.getString("Titre_Poste"));
                    contrat.setText(resultSet.getString("Type_Contrat"));
                    description.setText(resultSet.getString("Description_Offre"));
                    comp.setText(resultSet.getString("Competences"));
                    exp.setText(resultSet.getString("Niveau_Experience"));
                    date.setText(resultSet.getString("Date_Limite"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

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
                        __ModifierOffre.this, new DatePickerDialog.OnDateSetListener() {
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
                                String updateSQL = "UPDATE Offre SET " +
                                        "Discipline_Metier = '" + sec_a.getText().toString() + "', " +
                                        "Titre_Poste = '" + titre_a.getText().toString().replace("'", "''") + "', " +
                                        "Type_Contrat = '" + type_a.getText().toString().replace("'", "''") + "', " +
                                        "Description_Offre = '" + desc_a.getText().toString().replace("'", "''") + "', " +
                                        "Competences = '" + comp_a.getText().toString().replace("'", "''") + "', " +
                                        "Niveau_Experience = '" + exp_a.getText().toString().replace("'", "''") + "', " +
                                        "Date_Publication = '" + currentDate + "', " +
                                        "Date_Limite = '" + data_a.getText().toString() + "' " +
                                        "WHERE ID_Offre = " + getIntent().getStringExtra("ID_Offre") + ";";


                                Statement statement = connection.createStatement();
                                resultSet = statement.executeUpdate(updateSQL);

                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                        if (resultSet > 0) {
                            Snackbar snackbar = Snackbar.make(view, "Modifications appliquées aves succès", Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(__ModifierOffre.this, HomeRec.class);
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