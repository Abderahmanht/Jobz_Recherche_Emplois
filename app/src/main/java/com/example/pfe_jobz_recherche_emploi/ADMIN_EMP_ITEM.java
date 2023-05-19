package com.example.pfe_jobz_recherche_emploi;

public class ADMIN_EMP_ITEM {
    private String title;
    private String company;
    private String email;
    private String status;

    public ADMIN_EMP_ITEM(String title, String company, String email, String status) {
        this.title = title;
        this.company = company;
        this.email = email;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }


}

