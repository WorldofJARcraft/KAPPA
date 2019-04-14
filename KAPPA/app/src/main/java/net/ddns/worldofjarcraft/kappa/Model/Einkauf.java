package net.ddns.worldofjarcraft.kappa.Model;

import com.google.gson.annotations.SerializedName;

public class Einkauf {

    @SerializedName(value = "lebensmittel")
    private String Lebensmittel;

    private Benutzer nutzer;

    private int id;

    public String getLebensmittel() {
        return Lebensmittel;
    }

    public void setLebensmittel(String lebensmittel) {
        Lebensmittel = lebensmittel;
    }

    public Benutzer getNutzer() {
        return nutzer;
    }

    public void setNutzer(Benutzer nutzer) {
        this.nutzer = nutzer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public Einkauf(){}

    public Einkauf(String lebensmittel, Benutzer nutzer) {
        Lebensmittel = lebensmittel;
        this.nutzer = nutzer;
    }

    public Einkauf(String lebensmittel, Benutzer nutzer, int id) {
        Lebensmittel = lebensmittel;
        this.nutzer = nutzer;
        this.id = id;
    }
}
