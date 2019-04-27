package net.ddns.worldofjarcraft.kappa.Model;

import android.provider.SyncStateContract;
import android.util.Base64;

import com.google.gson.annotations.SerializedName;

import net.ddns.worldofjarcraft.kappa.Constants;
import net.ddns.worldofjarcraft.kappa.Utils.StringUtils;

public class Einkauf {

    @SerializedName(value = "lebensmittel")
    private String Lebensmittel;

    private Benutzer nutzer;

    private int id;

    public String getLebensmittel() {
        if(Lebensmittel.startsWith(Constants.BASE64Prefix)){
            return StringUtils.fromBase64(Lebensmittel.substring(Constants.BASE64Prefix.length()));
        }
        return Lebensmittel;
    }

    public void setLebensmittel(String lebensmittel) {
        Lebensmittel = lebensmittel.startsWith(Constants.BASE64Prefix)? lebensmittel : Constants.BASE64Prefix+StringUtils.toBase64(lebensmittel);
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
        this.setLebensmittel(lebensmittel);
        this.nutzer = nutzer;
    }

    public Einkauf(String lebensmittel, Benutzer nutzer, int id) {
        this.setLebensmittel(lebensmittel);
        this.nutzer = nutzer;
        this.id = id;
    }
}
