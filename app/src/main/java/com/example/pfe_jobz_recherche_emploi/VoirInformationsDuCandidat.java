package com.example.pfe_jobz_recherche_emploi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.Objects;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.webkit.URLUtil;

public class VoirInformationsDuCandidat extends AppCompatActivity {
    Toolbar toolbar;
    Button telechargerCV, envoyerEMAIL, envoyerSMS;
    TextView nom, prenom, dateN, dateC, poste;
    String num, email;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_informations_du_candidat);

        toolbar = findViewById(R.id.voir_info_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

        poste = findViewById(R.id.voir_info_candidature_poste_v);
        nom = findViewById(R.id.voir_info_candidature_nom_v);
        prenom = findViewById(R.id.voir_info_candidature_prenom_v);
        dateN = findViewById(R.id.voir_info_candidature_daten_v);
        dateC = findViewById(R.id.voir_info_candidature_date_candidature_v);

        telechargerCV = findViewById(R.id.voir_info_telecharger_cv_button);
        envoyerEMAIL = findViewById(R.id.voir_info_envoyer_email_btn);
        envoyerSMS = findViewById(R.id.voir_info_envoyer_sms_btn);

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection!=null){
            try{
                String sqlQuery = "SELECT Candidat.Nom, Candidat.Prenom, Candidat.Email, Candidat.Num_Tel, Candidat.Date_Naissance, Candidature.Date_Candidature, Offre.Titre_Poste "+
                        "FROM Candidat JOIN Candidature ON Candidature.ID_Candidat = Candidat.ID_Candidat "+
                        "JOIN Offre ON Candidature.ID_Offre = Offre.ID_Offre "+
                        "WHERE ID_Candidature = "+getIntent().getStringExtra("ID_Candidature");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlQuery);

                if (resultSet.next()){
                    poste.setText(resultSet.getString("Titre_Poste"));
                    nom.setText(resultSet.getString("Nom"));
                    prenom.setText(resultSet.getString("Prenom"));
                    dateN.setText(resultSet.getString("Date_Naissance"));
                    dateC.setText(resultSet.getString("Date_Candidature"));
                    num = resultSet.getString("Num_Tel");
                    email = resultSet.getString("Email");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        telechargerCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the WRITE_EXTERNAL_STORAGE permission is granted
                if (ContextCompat.checkSelfPermission(VoirInformationsDuCandidat.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Permission not granted, request it
                    ActivityCompat.requestPermissions(VoirInformationsDuCandidat.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
                } else {
                    // Permission already granted, proceed with file operations
                    // Call the downloadCV() method here
                    downloadCV(getIntent().getStringExtra("ID_Candidature"));
                }

            }
        });
        envoyerEMAIL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail(email);
            }
        });

        envoyerSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeSmsMessage(num);
            }
        });

    }
    public void composeEmail(String address) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + Uri.encode(address)));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
        intent.putExtra(Intent.EXTRA_SUBJECT,"Jobz - Candidature pour : "+poste.getText().toString() );
        intent.putExtra(Intent.EXTRA_TEXT, "Bonjour "+nom.getText().toString()+" "+prenom.getText().toString()+ "\nNous avons bien reçu votre candidature pour le poste "+poste.getText().toString());
        startActivity(intent);


    }
    public void composeSmsMessage(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("smsto:"+phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void downloadCV(String id) {
        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection != null) {
            try {
                String sqlQuery = "SELECT CV FROM Candidature WHERE ID_Candidature = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
                preparedStatement.setString(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    byte[] cvData = resultSet.getBytes("CV");

                    String fileName = "cv.pdf";
                    File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(dir, fileName);
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(cvData);
                    fileOutputStream.close();

                    // Show a message to the user that the CV has been downloaded.
                    Toast.makeText(this, "CV Téléchargée.", Toast.LENGTH_SHORT).show();

                    // Create a notification to inform the user that the download is completed
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    String channelId = "download_channel";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel(channelId, "Download", NotificationManager.IMPORTANCE_DEFAULT);
                        notificationManager.createNotificationChannel(channel);
                    }

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                            .setContentTitle("Téléchargement du CV")
                            .setContentText("CV téléchargé.")
                            .setSmallIcon(R.drawable.baseline_download_done_24);


                    // Open the downloaded file when the user taps the notification
                    Uri fileUri = FileProvider.getUriForFile(this, "com.example.pfe_jobz_recherche_emploi.fileprovider", file);
                    Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                    openFileIntent.setDataAndType(fileUri, "application/pdf");
                    openFileIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, openFileIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                    builder.setContentIntent(pendingIntent);

                    // Notify the user
                    notificationManager.notify(0, builder.build());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }











    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with file operations
                // Call the downloadCV() method here
                downloadCV(getIntent().getStringExtra("ID_Candidature"));
            } else {
                // Permission denied, handle the scenario accordingly (e.g., show an error message)
                Toast.makeText(this, "Permission denied. Cannot save file.", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(0, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}