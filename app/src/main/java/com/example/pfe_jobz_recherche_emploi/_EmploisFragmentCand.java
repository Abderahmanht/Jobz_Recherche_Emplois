package com.example.pfe_jobz_recherche_emploi;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class _EmploisFragmentCand extends Fragment {
    private RecyclerView recyclerViewJobs;
    private JobOfferAdapter jobOfferAdapter;
    private List<JobOfferItem> jobOffers;
    private String company;
    private String location;
    private Bitmap bitmap;
    private String comp_desc;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emplois_cand, container, false);

        recyclerViewJobs = view.findViewById(R.id.recyclerViewJobs);
        recyclerViewJobs.setLayoutManager(new LinearLayoutManager(getContext()));

        jobOffers = getJobOffersFromDatabase();
        jobOfferAdapter = new JobOfferAdapter(jobOffers, getActivity().getIntent().getStringExtra("ID_Candidat"));
        recyclerViewJobs.setAdapter(jobOfferAdapter);
        shimmerFrameLayout = view.findViewById(R.id.shimmerView);
        shimmerFrameLayout.setVisibility(View.INVISIBLE);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.dark_green);
        swipeRefreshLayout.setDistanceToTriggerSync((int) (200 * getResources().getDisplayMetrics().density));



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

        Connection connection = new ___ConnectionClass().connectionClass();
        if(connection != null) {
            try {
                String selectSQL = "SELECT ID_Offre, Titre_Poste, Type_Contrat, Date_Publication FROM Offre";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(selectSQL);

                    while (resultSet.next()) {
                        String offerId = resultSet.getString("ID_Offre");
                        String title = resultSet.getString("Titre_Poste");
                        String contract = resultSet.getString("Type_Contrat");
                        String date = resultSet.getString("Date_Publication");

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

                        JobOfferItem jobOffer = new JobOfferItem(offerId,company,comp_desc,title,contract,location,date,bitmap);
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
        return jobOffers;
    }




}
