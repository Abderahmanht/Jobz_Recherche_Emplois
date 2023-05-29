package com.example.pfe_jobz_recherche_emploi;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import kotlinx.coroutines.Job;

public class _AcceuilFragmentCand extends Fragment {
    private RecyclerView recyclerViewJobs;
    private JobOfferAdapter jobOfferAdapter;
    private List<JobOfferItem> jobOffers;
    private String company;
    private String location;
    private Bitmap bitmap;
    private String comp_desc;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    private Dialog  filters_dialog;
    private TextView filtersSecteur;
    private TextView filtersTypeContrat;
    private TextView filtersExperience;
    private TextView filtersWilaya;
    private TextView effacerTout;
    private String savedSecteur;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acceuil_cand, container, false);
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
        String[] secteurs = setWithoutDuplicates.toArray(new String[setWithoutDuplicates.size()]);


        String[] wilayas = {
                "Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaïa", "Biskra", "Béchar", "Blida", "Bouira",
                "Tamanrasset", "Tébessa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saïda",
                "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma", "Constantine", "Médéa", "Mostaganem", "M'Sila", "Mascara",
                "Ouargla", "Oran", "El Bayadh", "Illizi", "Bordj Bou Arreridj", "Boumerdès", "El Tarf", "Tindouf",
                "Tissemsilt", "El Oued", "Khenchela", "Souk Ahras", "Tipaza", "Mila", "Aïn Defla", "Naâma", "Aïn Témouchent",
                "Ghardaïa", "Relizane"
        };




        recyclerViewJobs = view.findViewById(R.id.recyclerViewJobs);
        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences Preferences = requireActivity().getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        jobOffers = getJobOffersFromDatabase();
        jobOfferAdapter = new JobOfferAdapter(jobOffers, getActivity().getIntent().getStringExtra("ID_Candidat"));
        recyclerViewJobs.setAdapter(jobOfferAdapter);
        shimmerFrameLayout = view.findViewById(R.id.shimmerView);
        shimmerFrameLayout.setVisibility(View.INVISIBLE);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.dark_green);
        swipeRefreshLayout.setDistanceToTriggerSync((int) (200 * getResources().getDisplayMetrics().density));
        EditText barRechercheOffres = view.findViewById(R.id.bar_recherche_offres);

        filters_dialog = new Dialog(getActivity());
        filters_dialog.setContentView(R.layout.filters_dialogue);
        filters_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button buttonFiltres = view.findViewById(R.id.button_filtres_recherche);

        Button appliquer = filters_dialog.findViewById(R.id.appliquer_filters);
        Button annuler = filters_dialog.findViewById(R.id.annuler_filters);
        filtersSecteur = filters_dialog.findViewById(R.id.filters_secteur);
        filtersTypeContrat = filters_dialog.findViewById(R.id.filters_type_contrat);
        filtersExperience = filters_dialog.findViewById(R.id.filters_experience);
        filtersWilaya = filters_dialog.findViewById(R.id.filters_wilaya);
        effacerTout = filters_dialog.findViewById(R.id.effacer_tout_filters);

        filtersSecteur.setText("Tout");
        filtersTypeContrat.setText("Tout");
        filtersExperience.setText("Tout");
        filtersWilaya.setText("Tout");

        effacerTout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filtersSecteur.setText("Tout");
                filtersTypeContrat.setText("Tout");
                filtersExperience.setText("Tout");
                filtersWilaya.setText("Tout");
            }
        });



        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection != null) {
            try {

                String secteurCandSQL = "SELECT Secteur FROM Candidat WHERE ID_Candidat=" + requireActivity().getIntent().getStringExtra("ID_Candidat") + ";";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(secteurCandSQL);
                if (rs.next()) {
                    filtersSecteur.setText(rs.getString("Secteur"));
                    String secteur = filtersSecteur.getText().toString();
                    String contrat = filtersTypeContrat.getText().toString();
                    String experience = filtersExperience.getText().toString();
                    String wilaya = filtersWilaya.getText().toString();
                    performFilter(secteur,contrat,experience,wilaya);
                    appliquer.performClick();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }



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

        filtersTypeContrat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), filtersExperience);
                popupMenu.getMenuInflater().inflate(R.menu.contract_type, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        filtersTypeContrat.setText(menuItem.getTitle());
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
                String contrat = filtersTypeContrat.getText().toString();
                String experience = filtersExperience.getText().toString();
                String wilaya = filtersWilaya.getText().toString();
                performFilter(secteur,contrat,experience,wilaya);
                filters_dialog.dismiss();
            }
        });



        barRechercheOffres.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Perform the search operation here
                    String query = textView.getText().toString();
                    performSearch(query);
                    return true;
                }
                return false;
            }
        });

        buttonFiltres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filters_dialog.show();
            }
        });




        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                recyclerViewJobs.setVisibility(View.GONE);
                shimmerFrameLayout.startShimmer();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        jobOffers.clear(); // Clear the current list
                        jobOffers.addAll(getJobOffersFromDatabase()); // Get the updated list
                        jobOfferAdapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false); // Stop the refreshing animation
                        recyclerViewJobs.setVisibility(View.VISIBLE);
                    }
                },1000);




            }
        });



        return view;
    }
    private List<JobOfferItem> getJobOffersFromDatabase() {
        List<JobOfferItem> jobOffers = new ArrayList<>();
        String secteur="";

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if(connection != null) {
            try {

                String secteurCandSQL = "SELECT Secteur FROM Candidat WHERE ID_Candidat="+requireActivity().getIntent().getStringExtra("ID_Candidat")+";";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(secteurCandSQL);
                if(rs.next()){
                    secteur = rs.getString("Secteur");
                }



                String selectSQL = "SELECT ID_Offre, Titre_Poste, Type_Contrat, Date_Publication,Niveau_Experience,Discipline_Metier, Competences, Date_Limite FROM Offre;";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                    while (resultSet.next()) {
                        String offerId = resultSet.getString("ID_Offre");
                        String title = resultSet.getString("Titre_Poste");
                        String contract = resultSet.getString("Type_Contrat");
                        String date = resultSet.getString("Date_Publication");
                        String comp = resultSet.getString("Competences");
                        String experience = resultSet.getString("Niveau_Experience");
                        String discipline = resultSet.getString("Discipline_Metier");
                        String datel = resultSet.getString("Date_Limite");

                        String RecSelectSQL = "SELECT rec.Entreprise, rec.Wilaya_Entreprise, rec.Logo_Entreprise, rec.Description_Entreprise " +
                                "FROM Recruteur rec " +
                                "JOIN Offre offre ON rec.ID_Recruteur = offre.ID_Recruteur " +
                                "WHERE offre.ID_Offre = "+ offerId;
                        Statement statement2 = connection.createStatement();
                        ResultSet resultSet2 = statement2.executeQuery(RecSelectSQL);

                        if (resultSet2.next()) {
                            company = resultSet2.getString("Entreprise");
                            location = resultSet2.getString("Wilaya_Entreprise");
                            comp_desc = resultSet2.getString("Description_Entreprise");
                            byte[] imageData = resultSet2.getBytes("Logo_Entreprise");
                            bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        }
                        JobOfferItem jobOffer = new JobOfferItem(offerId,company,comp_desc,title,contract,location,date,bitmap,discipline,experience,comp,datel);
                        jobOffers.add(jobOffer);




                    }



            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            Exception exception = new Exception();
            exception.printStackTrace();
        }
        Collections.reverse(jobOffers);
        return jobOffers;
    }

    private void performSearch(String query) {
        // Filter the data in your RecyclerView's adapter based on the search query
        List<JobOfferItem> searchResultItems = new ArrayList<>();
        for (JobOfferItem item : jobOffers) {
            if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                searchResultItems.add(item);
            }
        }

        // Update the adapter with the filtered data
        jobOfferAdapter.updateItems(searchResultItems);
    }

    private void performFilter(String secteur, String typeContrat, String niveauExperience, String wilaya){
        List<JobOfferItem> filteredItems = new ArrayList<>();
        for(JobOfferItem item : jobOffers){
            if(secteur.equals("Tout")){
                secteur = "";
            }
            if (typeContrat.equals("Tout")){
                typeContrat = "";
            }
            if(niveauExperience.equals("Tout")){
                niveauExperience = "";
            }
            if(wilaya.equals("Tout")){
                wilaya = "";
            }
            if(item.getSecteur().toLowerCase().contains(secteur.toLowerCase()) && item.getContract().toLowerCase().contains(typeContrat.toLowerCase()) && item.getExperience().toLowerCase().contains(niveauExperience.toLowerCase()) && item.getLocation().toLowerCase().contains(wilaya.toLowerCase()) ){
                filteredItems.add(item);
            }
        }

        jobOfferAdapter.updateItems(filteredItems);
    }





}
