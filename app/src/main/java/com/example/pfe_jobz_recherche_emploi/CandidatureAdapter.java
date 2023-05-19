package com.example.pfe_jobz_recherche_emploi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class CandidatureAdapter extends RecyclerView.Adapter<CandidatureAdapter.ViewHolder>{

    private String idc;
    private List<CandidatureItem> candidatures;

    public CandidatureAdapter(List<CandidatureItem> candidatureItemList) {
        this.candidatures = candidatureItemList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidature, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidatureAdapter.ViewHolder holder, int position) {
        CandidatureItem candidatureItem = candidatures.get(position);
        final int[] resultSet= new int[1];

        holder.textViewTitre.setText(candidatureItem.getTitre());
        holder.textViewEntreprise.setText(candidatureItem.getEntreprise());
        holder.textViewLieu.setText(candidatureItem.getLieu());
        holder.imageButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Êtes-vous sûr de vouloir supprimer cette alerte ?")
                        .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Connection connection = new ___ConnectionClass().SQLServerConnection();
                                if (connection != null){
                                    try {
                                        String deleteSQL = "DELETE FROM Candidature WHERE ID_Candidature ='" + candidatureItem.getIdCandidature() + "';";
                                        Statement statement = connection.createStatement();
                                        resultSet[0] = statement.executeUpdate(deleteSQL);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    if (resultSet[0]>0){
                                        Toast.makeText(view.getContext(), "Candidature supprimée",Toast.LENGTH_LONG).show();
                                        notifyDataSetChanged();
                                    }
                                }

                            }
                        })
                        .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }





    @Override
    public int getItemCount() {
        return candidatures.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTitre;
        public TextView textViewEntreprise;
        public TextView textViewLieu;
        public ImageButton imageButtonMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitre = itemView.findViewById(R.id.titre_candidature_item);
            textViewEntreprise = itemView.findViewById(R.id.entreprise_candidature_item);
            textViewLieu = itemView.findViewById(R.id.lieu_candidature_item);
            imageButtonMore = itemView.findViewById(R.id.delete_candidature_item);



        }
    }
}
