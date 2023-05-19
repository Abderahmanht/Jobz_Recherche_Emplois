package com.example.pfe_jobz_recherche_emploi;

public class AlerteNotificationItem {
    private String lieuResidence;
    private String discipline;
    private String nomAlerte;

    public AlerteNotificationItem(String nomAlerte, String lieuResidence, String discipline ) {
        this.lieuResidence = lieuResidence;
        this.discipline = discipline;
        this.nomAlerte = nomAlerte;
    }

    public String getLieuResidence() {
        return lieuResidence;
    }

    public String getDiscipline() {
        return discipline;
    }

    public String getNomAlerte() {
        return nomAlerte;
    }
}

