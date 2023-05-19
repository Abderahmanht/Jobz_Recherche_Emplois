package com.example.pfe_jobz_recherche_emploi;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class CandidatureRecAdapter extends RecyclerView.Adapter<CandidatureRecAdapter.ViewHolder>{

    private String idrec;
    private List<CandidatureItemRec> candidatures;

    public CandidatureRecAdapter(List<CandidatureItemRec> candidatureItemList, String idrec) {
        this.candidatures = candidatureItemList;
        this.idrec = idrec;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidature_rec, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidatureRecAdapter.ViewHolder holder, int position) {
        CandidatureItemRec candidatureItem = candidatures.get(position);
        ResultSet resultSet,resultSet2;

        holder.textViewNomComplet.setText(candidatureItem.getNomComplet());
        holder.textViewTitre.setText(candidatureItem.getTitre());
        holder.textViewNomComplet.setText(candidatureItem.getNomComplet());
       /* holder.imageButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.inflate(R.menu.menu_candidature_rec);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_view_information:
                                Intent intent = new Intent(view.getContext(), VoirInformationsDuCandidat.class);
                                view.getContext().startActivity(intent);
                                return true;
                            case R.id.menu_delete_submission:

                                return true;
                            default:
                                return false;
                        }
                    }

                });
                popupMenu.show();

            }

        });*/
        holder.itemView.setForegroundGravity(Gravity.BOTTOM);
        holder.itemView.setFocusable(true);
        holder.itemView.setClickable(true);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), VoirInformationsDuCandidat.class);
                intent.putExtra("ID_Candidature",candidatureItem.getIdCandidature());
                view.getContext().startActivity(intent);
            }
        });

    }
















    @Override
    public int getItemCount() {
        return candidatures.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTitre;
        public TextView textViewNomComplet;
        public ImageButton imageButtonMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewNomComplet = itemView.findViewById(R.id.nom_complet_candidat_candidatures);
            textViewTitre = itemView.findViewById(R.id.titre_poste_candidatures);
            imageButtonMore = itemView.findViewById(R.id.more_candidatures);



        }
    }
}
