package com.example.pfe_jobz_recherche_emploi;

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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.List;

public class JobOfferAdapter extends RecyclerView.Adapter<JobOfferAdapter.ViewHolder> {
    private String idc;
    private List<JobOfferItem> jobOffers;

    public JobOfferAdapter(List<JobOfferItem> jobOffers, String idc) {
        this.jobOffers = jobOffers;
        this.idc = idc;

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
        holder.textViewDate.setText(jobOffer.getDate());
        holder.imageViewLogo.setImageBitmap(jobOffer.getLogo());
        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                __OffreEnregistree OffreEnregistee = new __OffreEnregistree();

                String IDOffre = jobOffer.getID();
                String IDCandidat = JobOfferAdapter.this.idc;
                // Step 2: Perform a database query to check if the job offer is already saved
                boolean isJobOfferSaved = OffreEnregistee.isJobOfferSaved(IDOffre, IDCandidat);

                // Step 3: Delete or insert a record based on the query result
                if (isJobOfferSaved) {
                    // Job offer is already saved, delete the corresponding record
                    OffreEnregistee.deleteSavedJobOffer(IDOffre, IDCandidat);
                    // Update the bookmark button's appearance (e.g., remove bookmark icon)
                    holder.bookmark.setImageResource(R.drawable.ic_bookmark_empty);
                } else {
                    // Job offer is not saved, insert a new record
                    OffreEnregistee.insertSavedJobOffer(IDOffre, IDCandidat);
                    // Update the bookmark button's appearance (e.g., set bookmark icon)
                    holder.bookmark.setImageResource(R.drawable.ic_bookmark_filled);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), JobDetails.class);
                intent.putExtra("ID_Offre",jobOffer.getID());
                intent.putExtra("ID_Candidat",JobOfferAdapter.this.idc);
                Bitmap bitmap = jobOffer.getLogo();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                intent.putExtra("company_logo", byteArray);
                intent.putExtra("company_name", jobOffer.getCompany());
                intent.putExtra("job_title", jobOffer.getTitle());
                intent.putExtra("company_location", jobOffer.getLocation());
                intent.putExtra("type_contrat", jobOffer.getContract());
                intent.putExtra("date_publication", jobOffer.getDate());
                intent.putExtra("company_desc",jobOffer.getCompanyDesc());
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
    }
    public void updateItems(List<JobOfferItem> items) {
        this.jobOffers = items;
        notifyDataSetChanged();
    }
}



