package com.example.pfe_jobz_recherche_emploi;

import android.graphics.Bitmap;

public class AlerteItem {

    private String idAlerte;
    private String titre;
    private String poste;
    private String lieu;

    public AlerteItem (String idAlerte, String titre, String poste, String lieu) {
        this.idAlerte = idAlerte;
        this.titre = titre;
        this.poste = poste;
        this.lieu = lieu;
    }

    public String getIdAlerte() {
        return idAlerte;
    }

    public String getTitre() {
        return titre;
    }

    public String getPoste() {
        return poste;
    }

    public String getLieu() {
        return lieu;
    }
}

