package com.example.pfe_jobz_recherche_emploi;

public class ADMIN_DEM_ITEM {
    private String nom;
    private String email;
    private String status;

    public ADMIN_DEM_ITEM(String nom,String email, String status) {
        this.nom = nom;
        this.email = email;
        this.status = status;
    }

    public String getNom() {return nom;}

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }


}

