package net.ddns.worldofjarcraft.kappa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 16.07.2017.
 * Speichert Daten, die während des Programmablaufs konstant bleiben.
 */

public final class data {
    /**
     * E-Mail-Adresse des angemeldeten Benutzers.
     */
    public static String mail;
    /**
     * Passwort des Benutzers.
     */
    public static String pw;
    /**
     * Liste mit den Namen ablaufender Lebensmittel und weiteren Werten.
     */
    public static ArrayList<String[]> ablaufende;

    /**
     * Speichert alle Lebensmittel des Benutzers.
     * Format: zuerst Index, dann Name, dann Zahl, dann String zur Haltbarkeit, dann String mit Fachname, dann String mit Kühlschrankname
     */
    public static ArrayList<String[]> alle_lebensmittel;
}
