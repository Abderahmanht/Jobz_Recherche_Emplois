package com.example.pfe_jobz_recherche_emploi;

public class CandidatureItem {
    private String idCandidature;
    private String titre;
    private String entreprise;
    private String lieu;

    public CandidatureItem (String idCandidature, String titre, String entreprise, String lieu) {
        this.idCandidature = idCandidature;
        this.titre = titre;
        this.entreprise = entreprise;
        this.lieu = lieu;
    }

    public String getIdCandidature() {
        return idCandidature;
    }

    public String getTitre() {
        return titre;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public String getLieu() {
        return lieu;
    }
}
