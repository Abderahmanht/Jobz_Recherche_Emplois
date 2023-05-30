package com.example.pfe_jobz_recherche_emploi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class _MesOffresFragmentCand extends Fragment {
    private RecyclerView recyclerViewSavedJobOffers;
    public SavedJobOfferAdapter savedJobOfferAdapter;
    public List<JobOfferItem> savedJobOffers;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mes_offres_cand, container, false);

        recyclerViewSavedJobOffers = view.findViewById(R.id.recyclerViewSavedJobs);
        recyclerViewSavedJobOffers.setLayoutManager(new LinearLayoutManager(getContext()));

        savedJobOffers = getSavedJobOffersFromDatabase(getActivity().getIntent().getStringExtra("ID_Candidat"));
        String idc = getActivity().getIntent().getStringExtra("ID_Candidat");

        savedJobOfferAdapter = new SavedJobOfferAdapter(savedJobOffers,getActivity(), idc);
        recyclerViewSavedJobOffers.setAdapter(savedJobOfferAdapter);

        return view;
    }

    public List<JobOfferItem> getSavedJobOffersFromDatabase(String IDc) {
        List<JobOfferItem> savedJobOffers = new ArrayList<>();


        // Step 2: Establish a connection to your database
        Connection connection = new ___ConnectionClass().SQLServerConnection(); // Replace with your database connection code

        if (connection != null) {
            try {
                // Step 3: Construct the SQL query
                String query = "SELECT j.ID_Offre, j.Titre_Poste, j.Type_Contrat, j.Date_Publication, r.Entreprise, r.Wilaya_Entreprise, r.Logo_Entreprise, r.Description_Entreprise " +
                        "FROM Offre_Enregistree oe " +
                        "JOIN Offre j ON oe.ID_Offre = j.ID_Offre " +
                        "JOIN Recruteur r ON j.ID_Recruteur = r.ID_Recruteur " +
                        "WHERE oe.ID_Candidat = ?";

                // Step 4: Prepare the SQL statement
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, IDc);

                // Step 5: Execute the SQL query and retrieve the result set
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    // Step 6: Extract the relevant job offer details
                    String offerId = resultSet.getString("ID_Offre");
                    String title = resultSet.getString("Titre_Poste");
                    String contract = resultSet.getString("Type_Contrat");
                    String date = resultSet.getString("Date_Publication");
                    String company = resultSet.getString("Entreprise");
                    String location = resultSet.getString("Wilaya_Entreprise");
                    String compDesc = resultSet.getString("Description_Entreprise");
                    byte[] imageData = resultSet.getBytes("Logo_Entreprise");
                    Bitmap logo = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                    // Step 7: Create a new JobOfferItem object and add it to the savedJobOffers list
                    JobOfferItem jobOffer = new JobOfferItem(offerId, company, compDesc, title, contract, location, date, logo,"","","","","");
                    savedJobOffers.add(jobOffer);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return savedJobOffers;
    }

    public void updateSavedJobOffers(String idc) {
        List<JobOfferItem> updatedJobOffers = getSavedJobOffersFromDatabase(idc);
        savedJobOffers.clear();  // Clear the existing list
        savedJobOffers.addAll(updatedJobOffers);  // Add the new job offers to the list
        savedJobOfferAdapter.notifyDataSetChanged();  // Notify the adapter about the data change
        recyclerViewSavedJobOffers.getLayoutManager();
        recyclerViewSavedJobOffers.requestLayout();

    }

    public void refreshFragment() {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }




}

