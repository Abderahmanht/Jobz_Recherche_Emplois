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

import java.io.ByteArrayOutputStream;
import java.util.List;

public class SavedJobOfferAdapter extends RecyclerView.Adapter<SavedJobOfferAdapter.ViewHolder> {
    private String idc;
    private List<JobOfferItem> jobOffers;

    public SavedJobOfferAdapter(List<JobOfferItem> jobOffers, String idc) {
        this.jobOffers = jobOffers;
        this.idc = idc;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_job_offer, parent, false);

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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), JobDetails.class);
                intent.putExtra("ID_Offre",jobOffer.getID());
                intent.putExtra("ID_Candidat",SavedJobOfferAdapter.this.idc);
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
            textViewTitle = itemView.findViewById(R.id.sjobTitle);
            textViewCompany = itemView.findViewById(R.id.scompanyName);
            textViewLocation = itemView.findViewById(R.id.scompanyLocation);
            textViewType = itemView.findViewById(R.id.stypeContrat);
            textViewDate = itemView.findViewById(R.id.sdatePublication);
            imageViewLogo = itemView.findViewById(R.id.scompanyLogoo);
            bookmark = itemView.findViewById(R.id.ssaveIconOffer);
        }
    }
}


