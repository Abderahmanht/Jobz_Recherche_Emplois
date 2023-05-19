package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Login extends AppCompatActivity {

    Button loginBtn;
    TextView signUp;
    EditText emailEdt;
    EditText passwordEdt;
    TextView logrec;
    String email;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /* Getting ID's */

        loginBtn = findViewById(R.id.login_btn);
        signUp = findViewById(R.id.sign_up);
        logrec = findViewById(R.id.sign_in_rec);
        emailEdt = findViewById(R.id.emailEditText);
        passwordEdt = findViewById(R.id.passwordEditText);

        SharedPreferences Preferences = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
        String savedEmail = Preferences.getString("email", "");
        String savedPassword = Preferences.getString("password", "");
        String savedIDcandidat = Preferences.getString("ID_Candidat","");

        if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
            // Credentials are available, proceed to the home activity
            emailEdt.setText(savedEmail);
            passwordEdt.setText(savedPassword);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connection connection = new ___ConnectionClass().SQLServerConnection();
                if (connection != null) {
                    try {


                        email = emailEdt.getText().toString();
                        password = passwordEdt.getText().toString();

                        if(email.equals("admin@xyz.com")&&password.equals("admin")){
                            startActivity(new Intent(Login.this, ADMIN.class));
                        } else {

                        // Prepare the SQL query with placeholders for the email and password parameters
                        String loginQuery = "SELECT ID_Candidat FROM Candidat WHERE Email = '"+email+"'AND Mot_De_Passe = '"+password+"';";

                        // Create a prepared statement for the login query
                        Statement statement = connection.createStatement();

                        // Set the parameter values for the email and password


                        // Execute the query and retrieve the results
                        ResultSet rs = statement.executeQuery(loginQuery);

                        if(rs.next()) {

                                String idCandidat = rs.getString("ID_Candidat");

                                SharedPreferences sharedPreferences2 = getSharedPreferences("user_credentials", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences2.edit();
                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.putString("ID_Candidat", idCandidat);
                                editor.apply();
                                Intent intent = new Intent(Login.this, HomeCand.class);
                                intent.putExtra("ID_Candidat", idCandidat);
                                startActivity(intent);

                        }}

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else{
                    Exception exception = new Exception();
                    exception.printStackTrace();
                }
            }



        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, CreateAccount.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        logrec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, SignInRec.class));

            }
        });


    }
}