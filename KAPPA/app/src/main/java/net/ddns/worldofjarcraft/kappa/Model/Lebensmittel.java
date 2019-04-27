package net.ddns.worldofjarcraft.kappa.Model;

import com.google.gson.annotations.SerializedName;

import net.ddns.worldofjarcraft.kappa.Constants;
import net.ddns.worldofjarcraft.kappa.Utils.StringUtils;

public class Lebensmittel {
    @SerializedName("nummer")
    private int Nummer;

    @SerializedName("name")
    private String Name;

    @SerializedName("anzahl")
    private String Anzahl;

    @SerializedName("haltbarkeitsdatum")
    private long Haltbarkeitsdatum;

    @SerializedName("fach")
    private Fach Fach;

    @SerializedName("besitzer")
    private Benutzer Besitzer;

    private long eingelagert;

    public Lebensmittel() {
    }

    public Lebensmittel(String name, String anzahl, long haltbarkeitsdatum, Fach fach, Benutzer besitzer, long eingelagert) {
        Name = name;
        Anzahl = anzahl;
        Haltbarkeitsdatum = haltbarkeitsdatum;
        Fach = fach;
        Besitzer = besitzer;
        this.eingelagert = eingelagert;
    }

    public int getNummer() {
        return Nummer;
    }

    public void setNummer(int nummer) {
        Nummer = nummer;
    }

    public String getName() {
        if(Name.startsWith(Constants.BASE64Prefix)){
            return StringUtils.fromBase64(Name.substring(Constants.BASE64Prefix.length()));
        }
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAnzahl() {
        if(Anzahl.startsWith(Constants.BASE64Prefix)){
            return StringUtils.fromBase64(Anzahl.substring(Constants.BASE64Prefix.length()));
        }
        return Anzahl;
    }

    public void setAnzahl(String anzahl) {
        Anzahl = anzahl;
    }

    public long getHaltbarkeitsdatum() {
        return Haltbarkeitsdatum;
    }

    public void setHaltbarkeitsdatum(long haltbarkeitsdatum) {
        Haltbarkeitsdatum = haltbarkeitsdatum;
    }

    public net.ddns.worldofjarcraft.kappa.Model.Fach getFach() {
        return Fach;
    }

    public void setFach(net.ddns.worldofjarcraft.kappa.Model.Fach fach) {
        Fach = fach;
    }

    public Benutzer getBesitzer() {
        return Besitzer;
    }

    public void setBesitzer(Benutzer besitzer) {
        Besitzer = besitzer;
    }

    public long getEingelagert() {
        return eingelagert;
    }

    public void setEingelagert(long eingelagert) {
        this.eingelagert = eingelagert;
    }


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Lebensmittel)){
            return false;
        }
        Lebensmittel mittel = (Lebensmittel) obj;
        return mittel.getNummer() == this.getNummer();
    }
}
