package com.example.pfe_jobz_recherche_emploi;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class ADMIN_EMAILS extends AppCompatActivity {

    private Spinner recipientTypeSpinner;
    private EditText emailContentEditText, emailSubjectEditText;
    private Button sendEmailButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_emails);

        recipientTypeSpinner = findViewById(R.id.spinner_recipient_type);
        emailContentEditText = findViewById(R.id.edit_text_content);
        emailSubjectEditText = findViewById(R.id.edit_text_subject);
        sendEmailButton = findViewById(R.id.button_send_email);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.recipient_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        recipientTypeSpinner.setAdapter(adapter);

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }
    private void sendEmail() {
        String recipientType = recipientTypeSpinner.getSelectedItem().toString();
        String emailContent = emailContentEditText.getText().toString();
        String emailSubject = emailSubjectEditText.getText().toString();

        // Logic to determine the recipient table name based on the recipient type
        String tableName = getRecipientTableName(recipientType);

        // Retrieve emails from the database based on the recipient table
        List<String> recipientEmails = getEmailsFromTable(tableName);

        // Call the sendEmailToRecipient method for each recipient email
        for (String recipientEmail : recipientEmails) {
            sendEmailToRecipient(recipientEmail, emailContent, emailSubject);
        }
    }

    private String getRecipientTableName(String recipientType) {
        // Add your logic here to map the recipient type to the table name
        if (recipientType.equals("Demandeurs demploi")) {
            return "Candidat";
        } else if (recipientType.equals("Employeurs")) {
            return "Recruteur";
        } else {
            return "";
        }
    }

    private List<String> getEmailsFromTable(String tableName) {
        List<String> emails = new ArrayList<>();

        Connection connection = new ___ConnectionClass().SQLServerConnection();
        if (connection != null) {
            try {
                String getEmailsQuery = "SELECT Email FROM " + tableName;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(getEmailsQuery);

                while (resultSet.next()) {
                    String email = resultSet.getString("Email");
                    emails.add(email);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return emails;
    }

    private void sendEmailToRecipient(String emailAddress, String emailContent, String emailSubject) {
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
            message.setSubject(emailSubject);
            message.setText(emailContent);
            Transport.send(message);

            Toast.makeText(this, "Email sent to " + emailAddress, Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
