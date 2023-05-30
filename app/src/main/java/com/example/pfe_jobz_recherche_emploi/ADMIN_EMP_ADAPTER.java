package com.example.pfe_jobz_recherche_emploi;

import static java.security.AccessController.getContext;

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
import java.sql.ResultSet;
import java.sql.Statement;
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

public class ADMIN_EMP_ADAPTER extends RecyclerView.Adapter<ADMIN_EMP_ADAPTER.CardViewHolder> {

    private List<ADMIN_EMP_ITEM> admin_emp_items;
    private Context context;

    public ADMIN_EMP_ADAPTER(List<ADMIN_EMP_ITEM> admin_emp_items, Context context) {
        this.admin_emp_items = admin_emp_items;
        this.context = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_employeurs, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ADMIN_EMP_ITEM empItem = admin_emp_items.get(position);
        holder.bind(empItem);
    }

    @Override
    public int getItemCount() {
        return admin_emp_items.size();
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
            titleTextView = itemView.findViewById(R.id.nomcompletemployerADMIN);
            companyTextView = itemView.findViewById(R.id.entrepriseemployeurADMIN);
            emailTextView = itemView.findViewById(R.id.emailemployeurADMIN);
            desactiveStatusTextView = itemView.findViewById(R.id.statutemployeurdesactive);
            activeStatusTextView = itemView.findViewById(R.id.statutemployeuractive);
            accountSwitch = itemView.findViewById(R.id.switchcompteemployeurADMIN);
        }

        public void bind(ADMIN_EMP_ITEM admin_emp_item) {
            titleTextView.setText(admin_emp_item.getTitle());
            companyTextView.setText(admin_emp_item.getCompany());
            emailTextView.setText(admin_emp_item.getEmail());

            if(admin_emp_item.getStatus().trim().equals("1")){
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
                            String update = "UPDATE Recruteur SET Statut_Compte = "+newStatus+" WHERE Email = '"+email+"';";
                            Statement statement = connection.createStatement();
                            int resultSet = statement.executeUpdate(update);

                            if(resultSet>0){
                                if(isChecked){
                                    activeStatusTextView.setVisibility(View.VISIBLE);
                                    desactiveStatusTextView.setVisibility(View.INVISIBLE);
                                }else {desactiveStatusTextView.setVisibility(View.VISIBLE);activeStatusTextView.setVisibility(View.INVISIBLE);}

                                Toast.makeText(context,"Statut de compte modifié avec succès",Toast.LENGTH_LONG).show();
                                if (newStatus==1)
                                    sendEmail(email);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            });
        }
    }

    private void sendEmail(String emailAddress) {
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
            message.setSubject("Compte activé!");
            message.setText("Bonjour"+ ",\n\nVotre compte a été activé avec succès\nVous pouvez desormais publier vos offres, consulter les candidatures et voir les CVs déposés");
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

