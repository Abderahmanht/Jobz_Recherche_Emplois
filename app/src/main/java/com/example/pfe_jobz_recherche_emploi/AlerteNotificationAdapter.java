package com.example.pfe_jobz_recherche_emploi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlerteNotificationAdapter extends RecyclerView.Adapter<AlerteNotificationAdapter.AlertesViewHolder> {

    private Context context;
    private List<AlerteNotificationItem> notificationItemList;

    public AlerteNotificationAdapter(Context context, List<AlerteNotificationItem> notificationItemList) {
        this.context = context;
        this.notificationItemList = notificationItemList;
    }

    @NonNull
    @Override
    public AlertesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.alert_notification, parent, false);
        return new AlertesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertesViewHolder holder, int position) {
        AlerteNotificationItem alerte = notificationItemList.get(position);

        holder.txtNomAlerte.setText(alerte.getNomAlerte());
        holder.txtCity.setText(alerte.getLieuResidence());
        holder.txtDiscipline.setText(alerte.getDiscipline());

    }

    @Override
    public int getItemCount() {
        return notificationItemList.size();
    }

    public static class AlertesViewHolder extends RecyclerView.ViewHolder {

        public TextView txtNomAlerte, txtDiscipline, txtCity;


        public AlertesViewHolder(View itemView) {
            super(itemView);

            txtNomAlerte = itemView.findViewById(R.id.textAlertenom);
            txtDiscipline = itemView.findViewById(R.id.textDiscipline);
            txtCity = itemView.findViewById(R.id.textCity);
        }
    }
}
