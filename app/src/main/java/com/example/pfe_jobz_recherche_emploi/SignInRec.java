package com.example.pfe_jobz_recherche_emploi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class SignInRec extends AppCompatActivity {

    Button loginBtn;
    TextView signUp;
    EditText emailEdt;
    EditText passwordEdt;
    String email;
    String password;
    private Toolbar toolbarREC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_rec);


        loginBtn = findViewById(R.id.login_btnrec);
        signUp = findViewById(R.id.sign_up_rec);
        emailEdt = findViewById(R.id.emailEditTextRec);
        passwordEdt = findViewById(R.id.passwordEditTextRec);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connection connection = new ___ConnectionClass().SQLServerConnection();
                if (connection != null) {
                    try {


                        email = emailEdt.getText().toString();
                        password = passwordEdt.getText().toString();

                        if(email.equals("admin@xyz.com")&&password.equals("admin")){
                            startActivity(new Intent(SignInRec.this, ADMIN.class));
                        } else {

                            // Prepare the SQL query with placeholders for the email and password parameters
                            String loginQuery = "SELECT ID_Recruteur FROM Recruteur WHERE Email = '"+email+"'AND Mot_De_Passe = '"+password+"';";

                            // Create a prepared statement for the login query
                            Statement statement = connection.createStatement();

                            // Set the parameter values for the email and password


                            // Execute the query and retrieve the results
                            ResultSet rs = statement.executeQuery(loginQuery);

                            if(rs.next()) {

                                String idCandidat = rs.getString("ID_Recruteur");

                                SharedPreferences sharedPreferences2 = getSharedPreferences("user_credentials_rec", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences2.edit();
                                editor.putString("email_rec", email);
                                editor.putString("password_rec", password);
                                editor.putString("ID_Recruteur", idCandidat);
                                editor.apply();
                                Intent intent = new Intent(SignInRec.this, HomeRec.class);
                                intent.putExtra("ID_Recruteur", idCandidat);
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
                Intent intent = new Intent(SignInRec.this, CreateAccount.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        toolbarREC = findViewById(R.id.toolbar_sign_in_rec);
        setSupportActionBar(toolbarREC);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);

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