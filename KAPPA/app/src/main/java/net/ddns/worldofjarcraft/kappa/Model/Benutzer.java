package net.ddns.worldofjarcraft.kappa.Model;

public class Benutzer {

    private String EMail;

    private String Passwort;

    /**
     *
     * @return Name of the user in lower case.
     */
    public String getEMail() {
        return EMail.toLowerCase();
    }

    public String getPasswort() {
        return Passwort;
    }

    public Benutzer(){}



    public Benutzer(String EMail, String passwort) {

        this.EMail = EMail;
        Passwort = passwort;
    }
    @Override
    public String toString() {
        return String.format(
                "Customer[EMail=%s, Passwort='%s']",
                EMail, Passwort);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Benutzer)){
            return false;
        }
        Benutzer benutzer = (Benutzer) obj;
        return benutzer.getEMail().equals(this.getEMail());
    }
}
