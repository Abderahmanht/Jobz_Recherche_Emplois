package com.example.pfe_jobz_recherche_emploi;

public class CandidatureItemRec {
    private String idCandidature;
    private String titre;
    private String nomComplet;


    public CandidatureItemRec (String idCandidature, String titre, String nomComplet) {
        this.idCandidature = idCandidature;
        this.titre = titre;
        this.nomComplet = nomComplet;
    }

    public String getIdCandidature() {
        return idCandidature;
    }

    public String getTitre() {
        return titre;
    }

    public String getNomComplet() {
        return nomComplet;
    }
}
