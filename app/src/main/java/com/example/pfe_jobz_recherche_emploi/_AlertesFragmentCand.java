package com.example.pfe_jobz_recherche_emploi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class _AlertesFragmentCand extends Fragment {

    private Button creerAlerte, appliquer_alerte, annuler_alerte;
    private Dialog alerte_dialog;
    private int resultSet;
    private SwitchCompat alerteSwitch;
    private int accepteEmails = 0;
    private EditText titre, poste;
    private TextView experience;
    private AutoCompleteTextView lieu,discipline;
    private ImageButton gerer_alertes;

    private AlerteAdapter alerteAdapter;
    private RecyclerView recyclerViewalertes;
    private List<AlerteItem> alerteItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alertes_cand, container, false);



        recyclerViewalertes = view.findViewById(R.id.recycler_view_alertes_cand);
        recyclerViewalertes.setLayoutManager(new LinearLayoutManager(getActivity()));
        alerteItemList = getAlertesFromDatabase();
        alerteAdapter = new AlerteAdapter(alerteItemList, getActivity().getIntent().getStringExtra("ID_Candidat"));
        recyclerViewalertes.setAdapter(alerteAdapter);


        alerte_dialog = new Dialog(getActivity());
        alerte_dialog.setContentView(R.layout.dialogue_alerte);
        alerte_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        creerAlerte = view.findViewById(R.id.ajouter_alerte_button);
        appliquer_alerte = alerte_dialog.findViewById(R.id.appliquer_alerte);
        annuler_alerte = alerte_dialog.findViewById(R.id.annuler_alerte);
        titre = alerte_dialog.findViewById(R.id.titre_alerte);
        poste = alerte_dialog.findViewById(R.id.poste_alerte);
        experience = alerte_dialog.findViewById(R.id.experience_demande_alerte);
        discipline = alerte_dialog.findViewById(R.id.discipline_alerte);
        lieu = alerte_dialog.findViewById(R.id.lieu_residence_alerte);
        alerteSwitch = alerte_dialog.findViewById(R.id.alerte_switch);


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
        ArrayAdapter<String> adapter_dis = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, arrWithoutDuplicates);
        discipline.setAdapter(adapter_dis);


        String[] wilayas = {
                "Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaïa", "Biskra", "Béchar", "Blida", "Bouira",
                "Tamanrasset", "Tébessa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saïda",
                "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma", "Constantine", "Médéa", "Mostaganem", "M'Sila", "Mascara",
                "Ouargla", "Oran", "El Bayadh", "Illizi", "Bordj Bou Arreridj", "Boumerdès", "El Tarf", "Tindouf",
                "Tissemsilt", "El Oued", "Khenchela", "Souk Ahras", "Tipaza", "Mila", "Aïn Defla", "Naâma", "Aïn Témouchent",
                "Ghardaïa", "Relizane"
        };




        //endregion wilayas


        ArrayAdapter<String> adapter_lieu = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, wilayas);
        lieu.setAdapter(adapter_lieu);

        experience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), experience);
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




        creerAlerte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerte_dialog.show();
            }
        });

        annuler_alerte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerte_dialog.dismiss();
            }
        });

        appliquer_alerte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connection connection = new ___ConnectionClass().SQLServerConnection();
                if (connection != null){
                    try{
                        if(alerteSwitch.isChecked()){
                            accepteEmails = 1;
                        }
                        String insertSQL = "INSERT INTO Alerte_Emploi(ID_Candidat, Titre, Poste_Recherche, Lieu_Residence, Experience, Discipline, Accepte_Emails) VALUES ('"+getActivity().getIntent().getStringExtra("ID_Candidat")+"','"+titre.getText()+"','"+poste.getText()+"','"+lieu.getText()+"','"+experience.getText()+"','"+discipline.getText()+ "','"+ String.valueOf(accepteEmails)+"');";
                        Statement statement = connection.createStatement();
                        resultSet = statement.executeUpdate(insertSQL);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if (resultSet>0){
                        Toast.makeText(getContext(), "Alerte d'emplois crée avec succès",Toast.LENGTH_LONG).show();
                        alerte_dialog.dismiss();
                    }
                    else Toast.makeText(getContext(), "Erreur, alerte non ajoutée",Toast.LENGTH_LONG).show();

                }
            }
        });


        return view;
    }

    public List<AlerteItem> getAlertesFromDatabase() {
        List<AlerteItem> alertes = new ArrayList<>();

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection != null) {
            try {
                String selectSQL = "SELECT ID_Alerte, Titre, Poste_Recherche, Lieu_Residence FROM Alerte_Emploi WHERE ID_Candidat = "+getActivity().getIntent().getStringExtra("ID_Candidat");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                while (resultSet.next()) {
                    String idAlerte = resultSet.getString("ID_Alerte");
                    String titre = resultSet.getString("Titre");
                    String poste = resultSet.getString("Poste_Recherche");
                    String lieu = resultSet.getString("Lieu_Residence");

                    AlerteItem alerteItem = new AlerteItem(idAlerte, titre, poste, lieu);
                    alertes.add(alerteItem);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            Exception exception = new Exception();
            exception.printStackTrace();
        }
        Collections.reverse(alertes);
        return alertes;
    }

    public Dialog getAlerteDialog(){
        return alerte_dialog;
    }




}

