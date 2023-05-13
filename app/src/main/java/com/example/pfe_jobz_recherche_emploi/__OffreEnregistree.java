package com.example.pfe_jobz_recherche_emploi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class __OffreEnregistree {

    public boolean isJobOfferSaved(String IDOffre, String IDCandidat){
        Connection connection = new ___ConnectionClass().SQLServerConnection();

        if(connection != null){
            try {
                String query = "SELECT * FROM Offre_Enregistree WHERE ID_Offre = " + IDOffre + " AND ID_Candidat = " + IDCandidat + " ;";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                if(resultSet.next()){
                    return true;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return false;

    }

    public void deleteSavedJobOffer(String IDOffre, String IDCandidat) {
        Connection connection = new ___ConnectionClass().SQLServerConnection();

        if (connection != null) {
            try {
                String deleteQuery = "DELETE FROM Offre_Enregistree WHERE ID_Offre = " + IDOffre + " AND ID_Candidat = " + IDCandidat + ";";
                Statement statement = connection.createStatement();
                int resultSet = statement.executeUpdate(deleteQuery);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void insertSavedJobOffer(String IDOffre, String IDCandidat) {
        Connection connection = new ___ConnectionClass().SQLServerConnection();

        if (connection != null) {
            try {
                String insertQuery = "INSERT INTO Offre_Enregistree (ID_Offre, ID_Candidat) VALUES (" + IDOffre + ", " + IDCandidat + ");";
                Statement statement = connection.createStatement();
                int rowsAffected = statement.executeUpdate(insertQuery);

                if (rowsAffected > 0) {
                    // Insertion successful
                } else {
                    // No rows inserted, handle accordingly
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
