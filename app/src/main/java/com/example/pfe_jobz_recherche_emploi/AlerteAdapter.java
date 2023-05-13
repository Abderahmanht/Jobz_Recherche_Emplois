package com.example.pfe_jobz_recherche_emploi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class AlerteAdapter extends RecyclerView.Adapter<AlerteAdapter.ViewHolder> {
    private String idc,ida;
    private List<AlerteItem> alertes;

    public AlerteAdapter(List<AlerteItem> alerteItemList, String ida) {
        this.alertes = alerteItemList;
        this.idc = ida;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alerte, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlerteItem alerteItem = alertes.get(position);
        final int[] accepteEmails = {0};
        final int[] resultSet= new int[1];
        final int[] resultSet2 = new int[1];

        holder.textViewTitre.setText(alerteItem.getTitre());
        holder.textViewPoste.setText(alerteItem.getPoste());
        holder.textViewLieu.setText(alerteItem.getLieu());
        holder.imageButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog modifier_alerte_dialog = new Dialog(view.getContext());
                modifier_alerte_dialog.setContentView(R.layout.dialogue_alerte);
                modifier_alerte_dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                Button appliquer_alerte,annuler_alerte;
                TextView titre_dialog,description_dialog,titre,poste,experience;
                AutoCompleteTextView discipline,lieu;
                SwitchCompat alerteSwitch;
                titre_dialog = modifier_alerte_dialog.findViewById(R.id.dialog_alerte_title);
                titre_dialog.setText("Modifier alerte d'emploi");
                description_dialog = modifier_alerte_dialog.findViewById(R.id.dialog_alerte_description);
                description_dialog.setText("");

                titre = modifier_alerte_dialog.findViewById(R.id.titre_alerte);
                poste = modifier_alerte_dialog.findViewById(R.id.poste_alerte);
                experience = modifier_alerte_dialog.findViewById(R.id.experience_demande_alerte);
                discipline = modifier_alerte_dialog.findViewById(R.id.discipline_alerte);
                lieu = modifier_alerte_dialog.findViewById(R.id.lieu_residence_alerte);
                alerteSwitch = modifier_alerte_dialog.findViewById(R.id.alerte_switch);
                appliquer_alerte = modifier_alerte_dialog.findViewById(R.id.appliquer_alerte);
                annuler_alerte = modifier_alerte_dialog.findViewById(R.id.annuler_alerte);

                //region disciciplines
                String[] disciplines = {"Informatique", "Droit", "Marketing", "Médecine", "Ingénierie", "Finance", "Design", "Éducation", "Communication", "Arts",
                        "Administration publique",
                        "Anthropologie",
                        "Archéologie",
                        "Architecture",
                        "Artisanat",
                        "Biologie",
                        "Chimie",
                        "Cinématographie",
                        "Communication",
                        "Création littéraire",
                        "Danse",
                        "Design graphique",
                        "Droit",
                        "Économie",
                        "Éducation",
                        "Enseignement des langues",
                        "Génie civil",
                        "Génie électrique",
                        "Génie mécanique",
                        "Géographie",
                        "Géologie",
                        "Gestion",
                        "Histoire",
                        "Informatique",
                        "Ingénierie",
                        "Journalisme",
                        "Langues étrangères",
                        "Linguistique",
                        "Marketing",
                        "Mathématiques",
                        "Médecine",
                        "Météorologie",
                        "Musique",
                        "Neurosciences",
                        "Nutrition",
                        "Optométrie",
                        "Philosophie",
                        "Physique",
                        "Psychologie",
                        "Publicité",
                        "Relations internationales",
                        "Relations publiques",
                        "Sciences de l'environnement",
                        "Sciences de la Terre",
                        "Sciences politiques",
                        "Sociologie",
                        "Théâtre",
                        "Tourisme",
                        "Traduction",
                        "Astronomie",
                        "Bio-informatique",
                        "Biostatistique",
                        "Chimie analytique",
                        "Chirurgie plastique",
                        "Cybersécurité",
                        "Design d'intérieur",
                        "Design industriel",
                        "Éducation physique",
                        "Études de genre",
                        "Génétique",
                        "Gérontologie",
                        "Gestion des ressources humaines",
                        "Graphisme",
                        "Hydrologie",
                        "Immunologie",
                        "Informatique décisionnelle",
                        "Ingénierie biomédicale",
                        "Ingénierie environnementale",
                        "Mécatronique",
                        "Météorologie",
                        "Neurobiologie",
                        "Océanographie",
                        "Odontologie",
                        "Optique",
                        "Orthophonie",
                        "Pédagogie",
                        "Pharmacologie",
                        "Planification urbaine",
                        "Robotique"
                };



                //endregion disciplines
                HashSet<String> setWithoutDuplicates = new HashSet<>(Arrays.asList(disciplines));
                String[] arrWithoutDuplicates = setWithoutDuplicates.toArray(new String[setWithoutDuplicates.size()]);
                ArrayAdapter<String> adapter_dis = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, arrWithoutDuplicates);
                discipline.setAdapter(adapter_dis);


                String[] wilayas = {
                        "Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaïa", "Biskra", "Béchar", "Blida", "Bouira",
                        "Tamanrasset", "Tébessa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saïda",
                        "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma", "Constantine", "Médéa", "Mostaganem", "M'Sila", "Mascara",
                        "Ouargla", "Oran", "El Bayadh", "Illizi", "Bordj Bou Arreridj", "Boumerdès", "El Tarf", "Tindouf",
                        "Tissemsilt", "El Oued", "Khenchela", "Souk Ahras", "Tipaza", "Mila", "Aïn Defla", "Naâma", "Aïn Témouchent",
                        "Ghardaïa", "Relizane"
                };
                experience.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(view.getContext(), experience);
                        popupMenu.getMenuInflater().inflate(R.menu.experience_alerte, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                experience.setText(item.getTitle());
                                return true;
                            }
                        });
                        popupMenu.show();
                    }

                });



                //endregion wilayas


                ArrayAdapter<String> adapter_lieu = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, wilayas);
                lieu.setAdapter(adapter_lieu);

                PopupMenu popupMenu = new PopupMenu(view.getContext(), holder.imageButtonMore);
                popupMenu.getMenuInflater().inflate(R.menu.more_alerte, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.modfier_alerte:
                                modifier_alerte_dialog.show();
                                annuler_alerte.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        modifier_alerte_dialog.dismiss();
                                    }
                                });

                                appliquer_alerte.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Connection connection = new ___ConnectionClass().SQLServerConnection();
                                        if (connection != null){
                                            try{
                                                if(alerteSwitch.isChecked()){
                                                    accepteEmails[0] = 1;
                                                }
                                                String updateSQL = "UPDATE Alerte_Emploi SET Titre = '" + titre.getText() + "', Poste_Recherche = '" + poste.getText() + "', Lieu_Residence = '" + lieu.getText() + "', Experience = '" + experience.getText() + "', Discipline = '" + discipline.getText() + "', Accepte_Emails = '" + accepteEmails[0] + "' WHERE Titre = '" + alerteItem.getTitre() + "';";
                                                Statement statement = connection.createStatement();
                                                resultSet[0] = statement.executeUpdate(updateSQL);

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            if (resultSet[0] >0){
                                                Toast.makeText(view.getContext(), "Alerte d'emploi modifiée avec succès",Toast.LENGTH_LONG).show();
                                                modifier_alerte_dialog.dismiss();
                                            }
                                            else Toast.makeText(view.getContext(), "Erreur, alerte non ajoutée",Toast.LENGTH_LONG).show();

                                        }
                                    }
                                });

                                break;
                            case R.id.supprimer_alerte:
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setMessage("Êtes-vous sûr de vouloir supprimer cette alerte ?")
                                        .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Connection connection = new ___ConnectionClass().SQLServerConnection();
                                                if (connection != null){
                                                    try {
                                                        String deleteSQL = "DELETE FROM Alerte_Emploi WHERE Titre ='" + alerteItem.getTitre() + "';";
                                                        Statement statement = connection.createStatement();
                                                        resultSet2[0] = statement.executeUpdate(deleteSQL);
                                                    }catch (Exception e){
                                                        e.printStackTrace();
                                                    }
                                                    if (resultSet2[0]>0){
                                                        Toast.makeText(view.getContext(), "Alerte d'emploi supprimée",Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                                break;
                        }
                    return true;
                    }
                });
                popupMenu.show();
            }
        });




    }

    @Override
    public int getItemCount() {
        return alertes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTitre;
        public TextView textViewPoste;
        public TextView textViewLieu;
        public ImageButton imageButtonMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitre = itemView.findViewById(R.id.titre_alerte_item);
            textViewPoste = itemView.findViewById(R.id.poste_alerte_item);
            textViewLieu = itemView.findViewById(R.id.lieu_alerte_item);
            imageButtonMore = itemView.findViewById(R.id.more_alerte_item);

        }
    }
}




