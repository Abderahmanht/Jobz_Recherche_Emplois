package com.example.pfe_jobz_recherche_emploi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class ADMIN_DEM_ADAPTER extends RecyclerView.Adapter<ADMIN_DEM_ADAPTER.CardViewHolder> {

    private List<ADMIN_DEM_ITEM> admin_dem_items;
    private Context context;

    public ADMIN_DEM_ADAPTER(List<ADMIN_DEM_ITEM> admin_dem_items, Context context) {
        this.admin_dem_items = admin_dem_items;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_demandeur, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ADMIN_DEM_ITEM dem_item = admin_dem_items.get(position);
        holder.bind(dem_item);
    }

    @Override
    public int getItemCount() {
        return admin_dem_items.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView companyTextView;
        private TextView emailTextView;
        private TextView activeStatusTextView;
        private TextView desactiveStatusTextView;
        private Switch accountSwitch;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.nomcompletdemandeurADMIN);
            emailTextView = itemView.findViewById(R.id.emaildemandeurADMIN);
            desactiveStatusTextView = itemView.findViewById(R.id.statutdemandeurdesactive);
            activeStatusTextView = itemView.findViewById(R.id.statutdemandeuractive);
            accountSwitch = itemView.findViewById(R.id.switchcomptedemandeurADMIN);
        }

        public void bind(ADMIN_DEM_ITEM admin_dem_item) {
            titleTextView.setText(admin_dem_item.getNom());
            emailTextView.setText(admin_dem_item.getEmail());

            if(admin_dem_item.getStatus().trim().equals("1")){
                accountSwitch.setChecked(true);
                activeStatusTextView.setVisibility(View.VISIBLE);
                desactiveStatusTextView.setVisibility(View.INVISIBLE);
            }else{
                accountSwitch.setChecked(false);
                activeStatusTextView.setVisibility(View.INVISIBLE);
                desactiveStatusTextView.setVisibility(View.VISIBLE);
            }
            accountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    int newStatus = isChecked ? 1 : 0;
                    String email = emailTextView.getText().toString();
                    Connection connection = new ___ConnectionClass().SQLServerConnection();
                    if(connection!=null){
                        try{
                            String update = "UPDATE Candidat SET Statut_Compte = "+newStatus+" WHERE Email = '"+email+"';";
                            Statement statement = connection.createStatement();
                            int resultSet = statement.executeUpdate(update);

                            if(resultSet>0){
                                if(isChecked){
                                    activeStatusTextView.setVisibility(View.VISIBLE);
                                    desactiveStatusTextView.setVisibility(View.INVISIBLE);
                                }else {desactiveStatusTextView.setVisibility(View.VISIBLE);activeStatusTextView.setVisibility(View.INVISIBLE);}

                                Toast.makeText(context,"Statut de compte modifié avec succès",Toast.LENGTH_LONG).show();
                            }else{

                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            });
        }
    }
}

