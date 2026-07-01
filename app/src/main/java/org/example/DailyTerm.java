package org.example;

import java.time.LocalDateTime;

public class DailyTerm {
    public String term;
    public LocalDateTime created_date;
    public LocalDateTime renewed_date;
    public String bMaterial;

    DailyTerm(String term, LocalDateTime created_date, LocalDateTime renewed_date, String bMaterial) {
        this.term = term;
        this.created_date = created_date;
        this.renewed_date = renewed_date;
        this.bMaterial = bMaterial;
    }

    public String getTerm() {

        String termString = "Term: " + term + "\nDate Created: " + created_date + "\nDate Renewed: " + renewed_date;
        return termString;
    }
}
