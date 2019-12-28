package net.ddns.worldofjarcraft.kappa.Model;

import com.google.gson.annotations.SerializedName;

import net.ddns.worldofjarcraft.kappa.Constants;
import net.ddns.worldofjarcraft.kappa.Utils.StringUtils;

public class Kuehlschrank {

    private int laufNummer;
    @SerializedName(value = "name")
    private String Name;

    @SerializedName(value = "zahl_faecher")
    private int Zahl_Faecher;

    @SerializedName(value="besitzer")
    private Benutzer Besitzer;

    public int getLaufNummer() {
        return laufNummer;
    }

    public void setLaufNummer(int laufNummer) {
        this.laufNummer = laufNummer;
    }

    public String getName() {
        if(Name.startsWith(Constants.BASE64Prefix)){
            return StringUtils.fromBase64(Name.substring(Constants.BASE64Prefix.length()));
        }
        return Name;
    }

    public int getZahl_Faecher() {
        return Zahl_Faecher;
    }

    public void setZahl_Faecher(int zahl_Faecher) {
        Zahl_Faecher = zahl_Faecher;
    }

    public Benutzer getBesitzer() {
        return Besitzer;
    }

    public void setBesitzer(Benutzer besitzer) {
        Besitzer = besitzer;
    }

    public Kuehlschrank(String name, int zahl_Faecher, Benutzer besitzer) {

        Name = name;
        Zahl_Faecher = zahl_Faecher;
        Besitzer = besitzer;
    }

    public Kuehlschrank() {

    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Kuehlschrank)){
            return false;
        }
        Kuehlschrank schrank = (Kuehlschrank) obj;
        return schrank.getLaufNummer() == this.getLaufNummer();
    }
}
