package com.example.pfe_jobz_recherche_emploi;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class __CvThequeFragmentRec extends Fragment {

    private RecyclerView recyclerViewCv;
    private List<CvThequeItem> cvThequeItems;
    private CvThequeAdapter cvThequeAdapter;
    private Dialog filters_dialog;
    private TextView filtersSecteur, filtersEtudes, filtersExperience, filtersWilaya,effacerTout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cv_theque_rec, container, false);

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
        String[] secteurs = setWithoutDuplicates.toArray(new String[setWithoutDuplicates.size()]);

        filters_dialog = new Dialog(getActivity());
        filters_dialog.setContentView(R.layout.filters_dialog_rec);
        filters_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button buttonFiltres = view.findViewById(R.id.button_filtres_cvtheque);

        Button appliquer = filters_dialog.findViewById(R.id.appliquer_filters_rec);
        Button annuler = filters_dialog.findViewById(R.id.annuler_filters_rec);
        filtersSecteur = filters_dialog.findViewById(R.id.filters_secteur_rec);
        filtersEtudes = filters_dialog.findViewById(R.id.filters_etudes_rec);
        filtersExperience = filters_dialog.findViewById(R.id.filters_experience_rec);
        filtersWilaya = filters_dialog.findViewById(R.id.filters_wilaya_rec);
        effacerTout = filters_dialog.findViewById(R.id.effacer_tout_filters_rec);

        filtersSecteur.setText("Tout");
        filtersEtudes.setText("Tout");
        filtersExperience.setText("Tout");
        filtersWilaya.setText("Tout");

        String[] wilayas = {
                "Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaïa", "Biskra", "Béchar", "Blida", "Bouira",
                "Tamanrasset", "Tébessa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saïda",
                "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma", "Constantine", "Médéa", "Mostaganem", "M'Sila", "Mascara",
                "Ouargla", "Oran", "El Bayadh", "Illizi", "Bordj Bou Arreridj", "Boumerdès", "El Tarf", "Tindouf",
                "Tissemsilt", "El Oued", "Khenchela", "Souk Ahras", "Tipaza", "Mila", "Aïn Defla", "Naâma", "Aïn Témouchent",
                "Ghardaïa", "Relizane"
        };

        effacerTout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersSecteur.setText("Tout");
                filtersEtudes.setText("Tout");
                filtersExperience.setText("Tout");
                filtersWilaya.setText("Tout");
            }
        });

        filtersSecteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), filtersWilaya);

                // Inflate the menu resource
                popupMenu.getMenuInflater().inflate(R.menu.secteur, popupMenu.getMenu());
                Arrays.sort(secteurs);

                // Add menu items dynamically from the array
                for (String secteur : secteurs) {
                    popupMenu.getMenu().add(secteur);
                }

                // Set a click listener for the menu items
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        filtersSecteur.setText(menuItem.getTitle());
                        return true;
                    }
                });

                // Show the popup menu
                popupMenu.show();
            };
        });

        filtersEtudes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), filtersEtudes);
                popupMenu.getMenuInflater().inflate(R.menu.etudes, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        filtersEtudes.setText(menuItem.getTitle());
                        return true;
                    }
                });

                // Show the popup menu
                popupMenu.show();

            }
        });

        filtersExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), filtersExperience);
                popupMenu.getMenuInflater().inflate(R.menu.experience_alerte, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        filtersExperience.setText(menuItem.getTitle());
                        return true;
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }
        });

        filtersWilaya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), filtersWilaya);

                // Inflate the menu resource
                popupMenu.getMenuInflater().inflate(R.menu.wilayas, popupMenu.getMenu());

                // Add menu items dynamically from the array
                for (String wilaya : wilayas) {
                    popupMenu.getMenu().add(wilaya);
                }

                // Set a click listener for the menu items
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        filtersWilaya.setText(menuItem.getTitle());
                        return true;
                    }
                });

                // Show the popup menu
                popupMenu.show();
            }
        });


        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters_dialog.dismiss();
            }
        });

        appliquer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String secteur = filtersSecteur.getText().toString();
                String contrat = filtersEtudes.getText().toString();
                String experience = filtersExperience.getText().toString();
                String wilaya = filtersWilaya.getText().toString();
                performFilter(secteur,contrat,experience,wilaya);
                filters_dialog.dismiss();
            }
        });


        buttonFiltres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters_dialog.show();
            }
        });

        recyclerViewCv = view.findViewById(R.id.recyclerViewCvTheque);
        recyclerViewCv.setLayoutManager(new LinearLayoutManager(getActivity()));

        cvThequeItems = getCVsFromDatabase();

        cvThequeAdapter = new CvThequeAdapter(cvThequeItems, getActivity());
        recyclerViewCv.setAdapter(cvThequeAdapter);

        return view;
    }

    public List<CvThequeItem> getCVsFromDatabase(){
        List<CvThequeItem> cvThequeItemList = new ArrayList<>();

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection!=null){
            try{
                String selectSQL = "SELECT c.Nom, c.Prenom, c.Ville, cv.ID_CV, cv.Secteur, cv.Experience, cv.Etudes FROM CV cv JOIN Candidat c ON c.ID_Candidat = cv.ID_Candidat";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                while (resultSet.next()){
                    String nomcomplet = resultSet.getString("Nom") + " " + resultSet.getString("Prenom");
                    String idcv = resultSet.getString("ID_CV");
                    String secteur = resultSet.getString("Secteur");
                    String etudes = resultSet.getString("Etudes");
                    String experience = resultSet.getString("Experience");
                    String wilaya = resultSet.getString("Ville");
                    CvThequeItem cvThequeItem = new CvThequeItem(idcv, nomcomplet,wilaya, secteur, etudes, experience);
                    cvThequeItemList.add(cvThequeItem);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Collections.reverse(cvThequeItemList);
        return cvThequeItemList;
    }

    private void performFilter(String secteur, String etudes, String niveauExperience, String wilaya){
        List<CvThequeItem> filteredItems = new ArrayList<>();
        for(CvThequeItem item : cvThequeItems){
            if(secteur.equals("Tout")){
                secteur = "";
            }
            if (etudes.equals("Tout")){
                etudes = "";
            }
            if(niveauExperience.equals("Tout")){
                niveauExperience = "";
            }
            if(wilaya.equals("Tout")){
                wilaya = "";
            }
            if(item.getSecteur().toLowerCase().contains(secteur.toLowerCase()) && item.getEtudes().toLowerCase().contains(etudes.toLowerCase()) && item.getExperience().toLowerCase().contains(niveauExperience.toLowerCase()) && item.getWilaya().toLowerCase().contains(wilaya.toLowerCase()) ){
                filteredItems.add(item);
            }

        }
        CvThequeAdapter adapter = new CvThequeAdapter(cvThequeItems, getContext());

        recyclerViewCv.setAdapter(adapter);
        adapter.updateItems(filteredItems);


    }
}