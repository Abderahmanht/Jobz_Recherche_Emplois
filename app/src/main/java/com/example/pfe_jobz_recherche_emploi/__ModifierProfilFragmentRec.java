package com.example.pfe_jobz_recherche_emploi;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class __ModifierProfilFragmentRec extends Fragment {


    private Button continuer,upload;
    private StateProgressBar stateProgressBarrec;
    private RelativeLayout informations, entreprise;
    private String[] descriptionDataRec = {"Informations", "Entreprise"};

    private EditText nom,prenom,num,nomentreprise,sect,desc;
    private AutoCompleteTextView wilaya;

    private ImageView logo;
    private Uri imageFilePath;
    private Bitmap imageToStore;
    private int resultSet;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_modifier_profil_rec, container, false);


        //region wilayas
        String[] wilayas = {
                "Adrar", "Chlef", "Laghouat", "Oum El Bouaghi", "Batna", "Béjaïa", "Biskra", "Béchar", "Blida", "Bouira",
                "Tamanrasset", "Tébessa", "Tlemcen", "Tiaret", "Tizi Ouzou", "Alger", "Djelfa", "Jijel", "Sétif", "Saïda",
                "Skikda", "Sidi Bel Abbès", "Annaba", "Guelma", "Constantine", "Médéa", "Mostaganem", "M'Sila", "Mascara",
                "Ouargla", "Oran", "El Bayadh", "Illizi", "Bordj Bou Arreridj", "Boumerdès", "El Tarf", "Tindouf",
                "Tissemsilt", "El Oued", "Khenchela", "Souk Ahras", "Tipaza", "Mila", "Aïn Defla", "Naâma", "Aïn Témouchent",
                "Ghardaïa", "Relizane"
        };
        //endregion wilayas





        nom = view.findViewById(R.id.mnameRECedittext);
        prenom = view.findViewById(R.id.mprenomRECedittext);
        num = view.findViewById(R.id.mnumRECedittext);
        nomentreprise = view.findViewById(R.id.mentrepriseRECedittext);
        sect = view.findViewById(R.id.msecactiviteRECedittext);
        wilaya = view.findViewById(R.id.mlieuactiviteRECedittext);
        desc = view.findViewById(R.id.mdescriptionRECedittext);
        logo = view.findViewById(R.id.mcompanyLogoRec);

        continuer = view.findViewById(R.id.mcontinueButtonREC);
        upload = view.findViewById(R.id.muploadlogoREC);

        informations = (RelativeLayout) view.findViewById(R.id.mlayout_informations_rec);
        entreprise = (RelativeLayout) view.findViewById(R.id.mlayout_compte_rec);

        stateProgressBarrec = view.findViewById(R.id.mstate_progress_bar_rec);
        stateProgressBarrec.setStateDescriptionData(descriptionDataRec);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, wilayas);
        wilaya.setAdapter(adapter);

        String recruteurId = getActivity().getIntent().getStringExtra("ID_Recruteur");

        // Rest of your code...

        // Populate the fields with existing data from the Recruteur table
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection != null) {
            try {
                String SQLselect = "SELECT Nom, Prenom, Entreprise, Description_Entreprise, Wilaya_Entreprise, Secteur_Activite, Num_Tel FROM Recruteur WHERE ID_Recruteur = ?";
                PreparedStatement statement = connection.prepareStatement(SQLselect);
                statement.setString(1, recruteurId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    // Retrieve data from the result set and populate the fields
                    nom.setText(resultSet.getString("Nom"));
                    prenom.setText(resultSet.getString("Prenom"));
                    nomentreprise.setText(resultSet.getString("Entreprise"));
                    desc.setText(resultSet.getString("Description_Entreprise"));
                    wilaya.setText(resultSet.getString("Wilaya_Entreprise"));
                    sect.setText(resultSet.getString("Secteur_Activite"));
                    num.setText(resultSet.getString("Num_Tel"));
                    // Set other fields accordingly
                }

                resultSet.close();
                statement.close();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }


        continuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (stateProgressBarrec.getCurrentStateNumber()) {
                    case 1:
                        stateProgressBarrec.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
                        informations.setVisibility(View.INVISIBLE);
                        entreprise.setVisibility(View.VISIBLE);
                        continuer.setText("Confirmer");
                        break;
                    case 2:
                        stateProgressBarrec.setAllStatesCompleted(true);
                        Connection connection = new ___ConnectionClass().SQLServerConnection();
                        if (connection != null) {
                            try {
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                imageToStore.compress(Bitmap.CompressFormat.PNG, 0, stream);
                                byte[] byteArray = stream.toByteArray();

                                String SQLupdate = "UPDATE Recruteur SET Nom = ?, Prenom = ?, Entreprise = ?, Description_Entreprise = ?, Wilaya_Entreprise = ?, Secteur_Activite = ?, Num_Tel = ?, Logo_Entreprise = ? WHERE ID_Recruteur = "+getActivity().getIntent().getStringExtra("ID_Recruteur");
                                PreparedStatement statement = connection.prepareStatement(SQLupdate);
                                statement.setString(1, nom.getText().toString());
                                statement.setString(2, prenom.getText().toString());
                                statement.setString(3, nomentreprise.getText().toString());
                                statement.setString(4, desc.getText().toString());
                                statement.setString(5, wilaya.getText().toString());
                                statement.setString(6, sect.getText().toString());
                                statement.setString(7, num.getText().toString());
                                statement.setBytes(8, byteArray);
                                int rowsAffected = statement.executeUpdate();

                                resultSet = statement.executeUpdate();


                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }

                            if (resultSet > 0) {
                                Snackbar snackbar = Snackbar.make(view, "Compte modifié avec succès", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }

                            break;
                        }
                }

            }

        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, 1000);
            }
        });

        return view;

    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK){
            if(requestCode==1000){
                imageFilePath = data.getData();
                logo.setImageURI(imageFilePath);
                try {
                    imageToStore = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}