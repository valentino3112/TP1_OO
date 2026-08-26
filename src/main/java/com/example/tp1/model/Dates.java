package com.example.tp1.model;

/**
 * Periode de location envoyee dans le corps de la requete PUT :
 * { "begin": "11/11/2017", "end": "1/1/2018" }
 *
 * Les dates sont gardees en String car le format du sujet (jour/mois/annee)
 * n'est pas le format ISO attendu par defaut par Jackson pour un LocalDate.
 * La validation du format est faite dans CarService.
 */
public class Dates {

    private String begin;
    private String end;

    /** Constructeur vide obligatoire pour Jackson. */
    public Dates() {
    }

    public Dates(String begin, String end) {
        this.begin = begin;
        this.end = end;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Dates{" + begin + " -> " + end + "}";
    }
}
