package net.ddns.worldofjarcraft.kappa;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class AblaufendActivity extends Activity {
    ArrayList<String[]> lebensmittel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ablaufend);
        DataHelper helper = (DataHelper) getIntent().getSerializableExtra("lebensmittel");
        if(helper!=null){
            lebensmittel = helper.getList();
        }
        showLebensmittel(lebensmittel);
    }

    private void showLebensmittel(ArrayList<String[]> lebensmittel) {
        TableLayout tabelle = findViewById(R.id.ablaufend);
        TableRow kopfzeile = new TableRow(this);
        TextView v1 = new TextView(this);
        v1.setText(R.string.name);
        v1.setTextSize(24);
        v1.setTypeface(v1.getTypeface(), Typeface.BOLD);
        TextView v2 = new TextView(this);
        v2.setText(R.string.anzahl);
        v2.setTextSize(24);
        v2.setTypeface(v2.getTypeface(), Typeface.BOLD);
        TextView v3 = new TextView(this);
        v3.setText(R.string.haltbar);
        v3.setTextSize(24);
        v3.setTypeface(v3.getTypeface(), Typeface.BOLD);
        TextView v4 = new TextView(this);
        v4.setText(R.string.fach);
        v4.setTextSize(24);
        v4.setTypeface(v3.getTypeface(), Typeface.BOLD);
        TextView v5 = new TextView(this);
        v5.setText(R.string.schrank);
        v5.setTextSize(24);
        v5.setTypeface(v3.getTypeface(), Typeface.BOLD);

        Space space = new Space(this);
        space.setMinimumWidth(30);
        Space space2 = new Space(this);
        space2.setMinimumWidth(30);
        Space space3 = new Space(this);
        space3.setMinimumWidth(30);
        Space space4 = new Space(this);
        space4.setMinimumWidth(30);
        Space space5 = new Space(this);
        space5.setMinimumWidth(30);
        kopfzeile.addView(v1);
        kopfzeile.addView(space);
        kopfzeile.addView(v2);
        kopfzeile.addView(space2);
        kopfzeile.addView(v3);
        kopfzeile.addView(space3);
        kopfzeile.addView(v4);
        kopfzeile.addView(space4);
        kopfzeile.addView(v5);
        kopfzeile.addView(space5);
        tabelle.addView(kopfzeile);
        for (String[] attribute : lebensmittel) {
            if (attribute.length > 4) {

                TableRow zeile = new TableRow(this);
                TextView i1 = new TextView(this);
                i1.setText(attribute[0]);
                i1.setTextSize(24);
                TextView i2 = new TextView(this);
                i2.setText(attribute[1]);
                i2.setTextSize(24);
                TextView i3 = new TextView(this);
                i3.setText(attribute[2]);
                i3.setTextSize(24);
                if (attribute[2].equals(getResources().getString(R.string.abgelaufen)))
                    i3.setTextColor(Color.RED);
                else
                    i3.setTextColor(Color.YELLOW);
                TextView i4 = new TextView(this);
                i4.setText(attribute[3]);
                i4.setTextSize(24);
                TextView i5 = new TextView(this);
                i5.setText(attribute[4]);
                i5.setTextSize(24);
                space = new Space(this);
                space.setMinimumWidth(30);
                space2 = new Space(this);
                space2.setMinimumWidth(30);
                space3 = new Space(this);
                space3.setMinimumWidth(30);
                space4 = new Space(this);
                space4.setMinimumWidth(30);
                space5 = new Space(this);
                space5.setMinimumWidth(30);
                zeile.addView(i1);
                zeile.addView(space);
                zeile.addView(i2);

                zeile.addView(space2);
                zeile.addView(i3);

                zeile.addView(space3);
                zeile.addView(i4);

                zeile.addView(space4);
                zeile.addView(i5);

                zeile.addView(space5);
                tabelle.addView(zeile);
            }
        }

    }

}
