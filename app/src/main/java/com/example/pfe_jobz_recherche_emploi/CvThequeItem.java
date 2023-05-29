package com.example.pfe_jobz_recherche_emploi;

public class CvThequeItem {
    private String idCv;
    private String cvfilename;
    private String nomcomplet, secteur, experience, etudes, wilaya;


    public CvThequeItem(String idCv, String nomcomplet, String wilaya, String secteur, String etudes, String experience) {
        this.idCv = idCv;
        this.nomcomplet = nomcomplet;
        this.secteur = secteur;
        this.etudes = etudes;
        this.experience = experience;
        this.wilaya = wilaya;
    }

    public String getIdCv() {
        return idCv;
    }


    public String getSecteur() {
        return secteur;
    }
    public String getNomcomplet() {
        return nomcomplet;
    }

    public String getExperience() {
        return experience;
    }

    public String getEtudes() {
        return etudes;
    }

    public String getWilaya() {
        return wilaya;
    }
}
