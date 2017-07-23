package net.ddns.worldofjarcraft.kappa;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Eric on 23.07.2017.
 */
public class DataHelper implements Serializable {

    private ArrayList<String[]> werte;

    public DataHelper(ArrayList<String[]> floors) {
        this.werte = floors;
    }

    public ArrayList<String[]> getList() {
        return this.werte;
    }
}
