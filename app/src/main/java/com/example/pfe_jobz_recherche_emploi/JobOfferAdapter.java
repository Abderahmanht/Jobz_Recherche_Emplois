package com.example.pfe_jobz_recherche_emploi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class JobOfferAdapter extends RecyclerView.Adapter<JobOfferAdapter.ViewHolder> {
    private String idc;
    private List<JobOfferItem> jobOffers;
    private Context context;
    private Activity activity;

    public JobOfferAdapter(List<JobOfferItem> jobOffers, String idc, Context context, Activity activity) {
        this.jobOffers = jobOffers;
        this.idc = idc;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_offer, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobOfferItem jobOffer = jobOffers.get(position);

        holder.textViewTitle.setText(jobOffer.getTitle());
        holder.textViewCompany.setText(jobOffer.getCompany());
        holder.textViewLocation.setText(jobOffer.getLocation());
        holder.textViewType.setText(jobOffer.getContract());
        String formattedTimeAgo = getFormattedTimeAgo(jobOffer.getDate());
        holder.textViewDate.setText("il y a "+formattedTimeAgo);
        holder.imageViewLogo.setImageBitmap(jobOffer.getLogo());

        __OffreEnregistree OffreEnregistee = new __OffreEnregistree();


        boolean isJobOfferSaved = OffreEnregistee.isJobOfferSaved(jobOffer.getID(), JobOfferAdapter.this.idc);
        holder.updateBookmarkIcon(isJobOfferSaved);

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String IDOffre = jobOffer.getID();
                String IDCandidat = JobOfferAdapter.this.idc;
                boolean isJobOfferSaved = OffreEnregistee.isJobOfferSaved(IDOffre, IDCandidat);

                if (isJobOfferSaved) {
                    OffreEnregistee.deleteSavedJobOffer(IDOffre, IDCandidat);
                    holder.updateBookmarkIcon(false);
                } else {
                    OffreEnregistee.insertSavedJobOffer(IDOffre, IDCandidat);
                    holder.updateBookmarkIcon(true);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), JobDetails.class);
                intent.putExtras(activity.getIntent().getExtras());
                intent.putExtra("ID_Offre", jobOffer.getID());
                intent.putExtra("ID_Candidat", JobOfferAdapter.this.idc);
                Bitmap bitmap = jobOffer.getLogo();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("company_logo", byteArray);
                intent.putExtra("company_name", jobOffer.getCompany());
                intent.putExtra("secteur", jobOffer.getSecteur());
                intent.putExtra("job_title", jobOffer.getTitle());
                intent.putExtra("company_location", jobOffer.getLocation());
                intent.putExtra("type_contrat", jobOffer.getContract());
                intent.putExtra("date_publication", jobOffer.getDate());
                intent.putExtra("company_desc", jobOffer.getCompanyDesc());
                view.getContext().startActivity(intent);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.jobTitle);
            textViewCompany = itemView.findViewById(R.id.companyName);
            textViewLocation = itemView.findViewById(R.id.companyLocation);
            textViewType = itemView.findViewById(R.id.typeContrat);
            textViewDate = itemView.findViewById(R.id.datePublication);
            imageViewLogo = itemView.findViewById(R.id.companyLogoo);
            bookmark = itemView.findViewById(R.id.saveIconOffer);
        }

        public void updateBookmarkIcon(boolean isSaved) {
            if (isSaved) {
                bookmark.setImageResource(R.drawable.baseline_bookmark_filled_24_blue);
            } else {
                bookmark.setImageResource(R.drawable.ic_baseline_bookmark_border_24_blue);
            }
        }
    }
    public void updateItems(List<JobOfferItem> items) {
        this.jobOffers = items;
        notifyDataSetChanged();
    }

    private String getFormattedTimeAgo(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date jobDate = format.parse(dateString);
            long currentTime = System.currentTimeMillis();
            long jobTime = jobDate.getTime();
            long timeDiff = currentTime - jobTime;

            if (timeDiff < 0) {
                return "à l'instant";
            } else if (timeDiff < TimeUnit.HOURS.toMillis(1)) {
                long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
                return minutes + (minutes == 1 ? " minute" : " minutes");
            } else if (timeDiff < TimeUnit.DAYS.toMillis(1)) {
                long hours = TimeUnit.MILLISECONDS.toHours(timeDiff);
                return hours + (hours == 1 ? " heure" : " heures");
            } else if (timeDiff < TimeUnit.DAYS.toMillis(7)) {
                long days = TimeUnit.MILLISECONDS.toDays(timeDiff);
                return days + (days == 1 ? " jour" : " jours");
            } else if (timeDiff < TimeUnit.DAYS.toMillis(30)) {
                long weeks = TimeUnit.MILLISECONDS.toDays(timeDiff) / 7;
                return weeks + (weeks == 1 ? " semaine" : " semaines");
            } else if (timeDiff < TimeUnit.DAYS.toMillis(365)) {
                long months = TimeUnit.MILLISECONDS.toDays(timeDiff) / 30;
                return months + (" mois");
            } else {
                long years = TimeUnit.MILLISECONDS.toDays(timeDiff) / 365;
                return years + (years == 1 ? " an" : " ans");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ""; // Return empty string if parsing fails
    }




}



