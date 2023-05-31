package com.example.pfe_jobz_recherche_emploi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

        holder.imageButtonMore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.imageButtonMore);
                popupMenu.getMenuInflater().inflate(R.menu.more_candidature_rec, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.modifier_offre:
                                view.getContext().startActivity(new Intent(view.getContext(), __ModifierOffre.class)
                                        .putExtra("ID_Offre",candidatureItem.getIdCandidature())
                                );
                                return true;
                            case R.id.supprimer_offre:
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle("Supprimer candidature");
                                builder.setMessage("Êtes-vous sûr de vouloir rejeter cette candidature ?");
                                builder.setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Connection connection = new ___ConnectionClass().SQLServerConnection();
                                        if(connection!=null){
                                            try{
                                                String deleteSQL = "DELETE FROM Candidature WHERE ID_Candidature = "+candidatureItem.getIdCandidature()+";";
                                                Statement statement = connection.createStatement();
                                                int resultSet = statement.executeUpdate(deleteSQL);

                                                if(resultSet>0){
                                                    Toast.makeText(view.getContext(),"Candiadture suprimée",Toast.LENGTH_LONG).show();
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
            imageButtonMore = itemView.findViewById(R.id.more_candidatures_rec);



        }
    }
}
