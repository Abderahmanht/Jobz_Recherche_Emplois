package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgorPassword extends AppCompatActivity {

    Button continuer1,continuer2,confirmer;
    EditText email, code, newpassword;

    String emailstr, codestr, newpasswordstr;
    int randomNumber;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgor_password);



        continuer1 = findViewById(R.id.forgor_continuer_1);
        email = findViewById(R.id.forgor_email);
        continuer2 = findViewById(R.id.forgor_continuer_2);
        code = findViewById(R.id.foror_code);
        confirmer = findViewById(R.id.forgor_continuer_3);
        newpassword = findViewById(R.id.foror_new);

        continuer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailstr = email.getText().toString();
                email.setVisibility(View.GONE);
                code.setVisibility(View.VISIBLE);
                continuer2.setVisibility(View.VISIBLE);
                randomNumber = randomNumberGenerator();
                sendEmail(emailstr,randomNumber);
                continuer1.setVisibility(View.GONE);

            }
        });
        continuer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codestr = code.getText().toString();


                if(Integer.parseInt(codestr)==randomNumber){
                    code.setVisibility(View.GONE);
                    confirmer.setVisibility(View.VISIBLE);
                    newpassword.setVisibility(View.VISIBLE);
                    continuer2.setVisibility(View.GONE);
                }else{
                    Toast.makeText(ForgorPassword.this,"Code incorrect",Toast.LENGTH_LONG).show();
                }

            }
        });
        confirmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newpasswordstr = newpassword.getText().toString();
                if(newpasswordstr.length()<8){
                    Toast.makeText(ForgorPassword.this, "Mot de passe doit contenir au moins 8 caractères",Toast.LENGTH_LONG).show();
                }else{
                    Connection connection = new ___ConnectionClass().SQLServerConnection();
                    if(connection!=null){
                        try {
                            String updatePassword = "UPDATE Candidat SET Mot_de_passe ='" + newpasswordstr + "' WHERE Email = '" + emailstr + "';";
                            Statement statement = connection.createStatement();
                            int resultSet = statement.executeUpdate(updatePassword);
                            if (resultSet>0){
                                Toast.makeText(ForgorPassword.this,"Mot de passe modifié avec succès",Toast.LENGTH_LONG).show();
                                sendPasswordChangedEmail(emailstr);
                                startActivity(new Intent(ForgorPassword.this,Login.class));
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        toolbar = findViewById(R.id.mdpoublietoolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
    }

    private void sendEmail(String emailAddress, int num) {
        final String username = "jobzrechercheemplois@gmail.com";
        final String password = "uynpnrhwrqzaekls";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress));
            message.setSubject("Code pour modification de mot de passe");
            message.setText("Bonjour, \n\nVoici votre code :"+num);
            new SendEmailTask(message).execute();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendPasswordChangedEmail(String emailAddress){
        final String username = "jobzrechercheemplois@gmail.com";
        final String password = "uynpnrhwrqzaekls";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress));
            message.setSubject("Mot de passe modifié");
            message.setText("Bonjour, \n\nVotre mot de passe a été modifié avec succès");
            new SendEmailTask(message).execute();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }



    public int randomNumberGenerator() {
            int min = 100000;
            int max = 999999;

            Random random = new Random();
        return random.nextInt(max - min + 1) + min;
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
