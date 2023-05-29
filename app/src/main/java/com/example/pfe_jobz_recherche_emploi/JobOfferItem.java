package com.example.pfe_jobz_recherche_emploi;

import android.graphics.Bitmap;

public class JobOfferItem {
    private String company;
    private String title;
    private String contract;
    private String secteur;
    private String location;
    private String date;
    private Bitmap logo;
    private String companyDesc;
    private String ID;
    private String experience;
    private String competences, datel;

    public String getSecteur() {
        return secteur;
    }

    public String getExperience() {
        return experience;
    }

    public JobOfferItem(String ID, String company, String companyDesc, String title, String contract, String location, String date, Bitmap logo, String secteur, String experience, String competences, String datel) {
        this.company = company;
        this.title = title;
        this.contract = contract;
        this.location = location;
        this.date = date;
        this.logo = logo;
        this.ID = ID;
        this.companyDesc = companyDesc;
        this.secteur = secteur;
        this.experience = experience;
        this.competences = competences;
        this.datel = datel;
    }

    public String getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }

    public String getContract() {
        return contract;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public Bitmap getLogo() {
        return logo;
    }

    public String getCompanyDesc() { return companyDesc; }

    public String getID() { return ID;}
}

