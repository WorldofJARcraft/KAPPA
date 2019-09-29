package net.ddns.worldofjarcraft.kappa.Model;

import com.google.gson.annotations.SerializedName;

import net.ddns.worldofjarcraft.kappa.Constants;
import net.ddns.worldofjarcraft.kappa.Utils.StringUtils;

public class Fach {
    private int lNummer;

    @SerializedName("kuehlschrank")
    private Kuehlschrank Kuehlschrank;

    @SerializedName("name")
    private String Name;

    public Fach() {
    }

    public Fach(Kuehlschrank kuehlschrank, String name) {
        Kuehlschrank = kuehlschrank;
        Name = name;
    }

    public int getlNummer() {
        return lNummer;
    }

    public void setlNummer(int lNummer) {
        this.lNummer = lNummer;
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


    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Fach)){
            return false;
        }
        Fach fach = (Fach) obj;
        return fach.getlNummer() == this.getlNummer();
    }

    public net.ddns.worldofjarcraft.kappa.Model.Kuehlschrank getKuehlschrank() {
        return Kuehlschrank;
    }
}
