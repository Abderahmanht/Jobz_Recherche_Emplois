package com.example.pfe_jobz_recherche_emploi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class CvThequeAdapter extends RecyclerView.Adapter<com.example.pfe_jobz_recherche_emploi.CvThequeAdapter.CardViewHolder> {

    private List<CvThequeItem> cvThequeItems;
    private Context context;

    public CvThequeAdapter(List<CvThequeItem> cvThequeItems, Context context) {
        this.cvThequeItems = cvThequeItems;
        this.context = context;
    }

    @NonNull
    @Override
    public CvThequeAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cvtheque, parent, false);
        return new CvThequeAdapter.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CvThequeAdapter.CardViewHolder holder, int position) {
        CvThequeItem cvThequeItem = cvThequeItems.get(position);
        holder.bind(cvThequeItem);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),VoirProfilCV.class);
                intent.putExtra("Type","cvtheque");
                intent.putExtra("ID_CV",cvThequeItem.getIdCv());
                intent.putExtra("NomComplet",holder.NomComplet.getText().toString());
                intent.putExtra("Secteur",holder.secteur.getText().toString());
                intent.putExtra("Experience",cvThequeItem.getExperience());
                intent.putExtra("Etudes",cvThequeItem.getEtudes());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cvThequeItems.size();
    }

    public void updateItems(List<CvThequeItem> items) {
        this.cvThequeItems = items;
        notifyDataSetChanged();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView secteur;
        private TextView NomComplet;
        private Button button;



        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            secteur = itemView.findViewById(R.id.secteur_cvtheque_item);
            NomComplet = itemView.findViewById(R.id.nom_cvtheque_item);
            button = itemView.findViewById(R.id.voir_profil_cv_cvtheque);


        }

        public void bind(CvThequeItem cvThequeItem) {
            secteur.setText(cvThequeItem.getSecteur());
            NomComplet.setText(cvThequeItem.getNomcomplet());

        }
    }


}



