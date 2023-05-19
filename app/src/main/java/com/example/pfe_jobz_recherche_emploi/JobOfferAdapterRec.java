package com.example.pfe_jobz_recherche_emploi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class JobOfferAdapterRec extends RecyclerView.Adapter<JobOfferAdapterRec.ViewHolder> {

        private String idr;
        private List<JobOfferItemRec> jobOffers;

        public JobOfferAdapterRec(List<JobOfferItemRec> jobOffers, String idr) {
            this.jobOffers = jobOffers;
            this.idr = idr;

        }

        @NonNull
        @Override
        public JobOfferAdapterRec.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_offer, parent, false);

            return new JobOfferAdapterRec.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull JobOfferAdapterRec.ViewHolder holder, int position) {
            JobOfferItemRec jobOffer = jobOffers.get(position);

            holder.textViewTitle.setText(jobOffer.getTitle());
            holder.textViewCompany.setText(jobOffer.getCompany());
            holder.textViewLocation.setText(jobOffer.getLocation());
            holder.textViewType.setText(jobOffer.getContract());
            holder.textViewDate.setText(jobOffer.getDate());
            holder.imageViewLogo.setImageBitmap(jobOffer.getLogo());
            holder.bookmark.setVisibility(View.INVISIBLE);
            holder.more.setVisibility(View.VISIBLE);
            holder.more.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.more);
                    popupMenu.getMenuInflater().inflate(R.menu.more_offre_rec, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.modifier_offre:
                                    view.getContext().startActivity(new Intent(view.getContext(), __ModifierOffre.class)
                                            .putExtra("ID_Offre",jobOffer.getID())
                                    );
                                    return true;
                                case R.id.supprimer_offre:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                    builder.setTitle("Supprimer offre");
                                    builder.setMessage("Êtes-vous sûr de vouloir supprimer cette offre ?");
                                    builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Connection connection = new ___ConnectionClass().SQLServerConnection();
                                            if(connection!=null){
                                                try{
                                                    String deleteSQL = "DELETE FROM Offre WHERE ID_Offre = "+jobOffer.getID()+";";
                                                    Statement statement = connection.createStatement();
                                                    int resultSet = statement.executeUpdate(deleteSQL);

                                                    if(resultSet>0){
                                                        Toast.makeText(view.getContext(),"Offre suprimée",Toast.LENGTH_LONG).show();
                                                        updateItems(jobOffers);
                                                    }
                                                }catch (Exception e){
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Annuler", null);

                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    return true;
                            }


                            return false;
                        }
                    });

                    // Show the popup menu
                    popupMenu.show();

                }

            });
        }



        @Override
        public int getItemCount() {
            return jobOffers.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textViewCompany;
            public TextView textViewLocation;
            public TextView textViewTitle;
            public TextView textViewType;
            public TextView textViewDate;
            public ImageView imageViewLogo;
            public ImageButton bookmark;
            public ImageButton more;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.jobTitle);
                textViewCompany = itemView.findViewById(R.id.companyName);
                textViewLocation = itemView.findViewById(R.id.companyLocation);
                textViewType = itemView.findViewById(R.id.typeContrat);
                textViewDate = itemView.findViewById(R.id.datePublication);
                imageViewLogo = itemView.findViewById(R.id.companyLogoo);
                bookmark = itemView.findViewById(R.id.saveIconOffer);
                more = itemView.findViewById(R.id.more_horiz_job_offer);
            }
        }
        public void updateItems(List<JobOfferItemRec> items) {
            this.jobOffers = items;
            notifyDataSetChanged();
        }
    }
