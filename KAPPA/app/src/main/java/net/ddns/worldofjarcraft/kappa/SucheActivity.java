package net.ddns.worldofjarcraft.kappa;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.Pair;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;

import net.ddns.worldofjarcraft.kappa.Model.Fach;
import net.ddns.worldofjarcraft.kappa.Model.Lebensmittel;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SucheActivity extends Activity {
    ProgressBar bar;
    String schrank = "";
    int schrank_nr = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suche);
        bar = findViewById(R.id.progressBarSuche);
        final Button search = findViewById(R.id.search);
        if(getIntent().getExtras() == null){
            schrank = "?";
            schrank_nr = -1;
        }
        else {
            schrank = getIntent().getExtras().getString("name");
            schrank_nr = getIntent().getExtras().getInt("schrank");
        }
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText text = findViewById(R.id.sterm);
                search(text.getText().toString());
            }
        });
        bar.setVisibility(View.GONE);
        ImageButton butt = findViewById(R.id.back_Suche);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back(view);
            }
        });
        final EditText text = findViewById(R.id.sterm);
        text.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                search(s.toString());
            }
        });

    }

    SparseArray<Fach> faecher;

    void search(final String sterm) {
        bar.setVisibility(View.VISIBLE);

        HTTP_Connection conn = new HTTP_Connection(Constants.Server_Adress + "/schrank/" + schrank_nr + "/getAll");
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                faecher = new SparseArray<>();
                Fach[] neuefaecher = new Gson().fromJson(output, Fach[].class);
                if(neuefaecher == null){
                    neuefaecher = new Fach[0];
                }
                for(Fach fach : neuefaecher){
                    faecher.put(fach.getlNummer(),fach);
                }
                HashMap<String, String> params = new HashMap<>();
                params.put("query",sterm);
                HTTP_Connection conn1 = null;
                try {
                    conn1 = new HTTP_Connection(Constants.Server_Adress+"/search",2, params,"GET");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return;
                }
                conn1.delegate = new AsyncResponse() {
                    @Override
                    public void processFinish(String output, String url) {
                        Lebensmittel[] lebensmittel = new Gson().fromJson(output, Lebensmittel[].class);
                        if(lebensmittel == null){
                            lebensmittel = new Lebensmittel[0];
                        }
                        TableLayout tabelle = findViewById(R.id.ergebnisse);
                        tabelle.removeAllViews();
                        TableRow kopfzeile = new TableRow(SucheActivity.this);
                        TextView v1 = new TextView(SucheActivity.this);
                        v1.setText(R.string.name);
                        v1.setTextSize(24);
                        v1.setTypeface(v1.getTypeface(), Typeface.BOLD);
                        TextView v2 = new TextView(SucheActivity.this);
                        v2.setText(R.string.anzahl);
                        v2.setTextSize(24);
                        v2.setTypeface(v2.getTypeface(), Typeface.BOLD);
                        TextView v3 = new TextView(SucheActivity.this);
                        v3.setText(R.string.haltbar);
                        v3.setTextSize(24);
                        v3.setTypeface(v3.getTypeface(), Typeface.BOLD);
                        TextView v4 = new TextView(SucheActivity.this);
                        v4.setText(R.string.fach);
                        v4.setTextSize(24);
                        v4.setTypeface(v3.getTypeface(), Typeface.BOLD);
                        Space space = new Space(SucheActivity.this);
                        space.setMinimumWidth(30);
                        Space space2 = new Space(SucheActivity.this);
                        space2.setMinimumWidth(30);
                        Space space3 = new Space(SucheActivity.this);
                        space3.setMinimumWidth(30);
                        Space space4 = new Space(SucheActivity.this);
                        space4.setMinimumWidth(30);
                        kopfzeile.addView(v1);
                        kopfzeile.addView(space);
                        kopfzeile.addView(v2);
                        kopfzeile.addView(space2);
                        kopfzeile.addView(v3);
                        kopfzeile.addView(space3);
                        kopfzeile.addView(v4);
                        kopfzeile.addView(space4);
                        tabelle.addView(kopfzeile);
                        for (Lebensmittel lm : lebensmittel) {
                                TableRow zeile = new TableRow(SucheActivity.this);
                                TextView w1 = new TextView(SucheActivity.this);
                                w1.setText(lm.getName());
                                w1.setTextSize(24);
                                TextView w2 = new TextView(SucheActivity.this);
                                w2.setText(lm.getAnzahl());
                                w2.setTextSize(24);
                                TextView i3 = new TextView(SucheActivity.this);
                                i3.setTextSize(24);
                                i3.setText(R.string.keine_Angabe);
                                try {
                                    long mhd = lm.getHaltbarkeitsdatum();
                                    if (mhd > 0) {
                                        long rest = mhd - Calendar.getInstance().getTimeInMillis();
                                        long aktzeit = Calendar.getInstance().getTimeInMillis();
                                        if (rest > 0) {
                                            Calendar cal = Calendar.getInstance();
                                            cal.setTimeInMillis(rest);
                                            String text = "";
                                            long year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day = Calendar.DAY_OF_MONTH;
                                            System.out.println(year + "," + month + "," + day);
                                            if (cal.get(Calendar.YEAR) - 1970 > 0)
                                                text += (cal.get(Calendar.YEAR) - 1970) + " " + getResources().getString(R.string.jahre) + ", ";
                                            if (cal.get(Calendar.MONTH) > 0)
                                                text += cal.get(Calendar.MONTH) + " " + getResources().getString(R.string.monate) + ", ";

                                            text += cal.get(Calendar.DAY_OF_MONTH) + " " + getResources().getString(R.string.tage) + ".";
                                            i3.setText(text);
                                            if (cal.get(Calendar.YEAR) - 1970 == 0 && !text.contains(getResources().getString(R.string.monate)) && cal.get(Calendar.DAY_OF_MONTH) < 8) {
                                                i3.setTextColor(getResources().getColor(R.color.ORANGE));
                                            } else
                                                i3.setTextColor(Color.GREEN);
                                        } else {
                                            i3.setText(R.string.abgelaufen);

                                            i3.setTextColor(Color.RED);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                TextView w4 = new TextView(SucheActivity.this);
                                Fach fach = lm.getFach();
                                space = new Space(SucheActivity.this);
                                space.setMinimumWidth(30);
                                space2 = new Space(SucheActivity.this);
                                space2.setMinimumWidth(30);
                                space3 = new Space(SucheActivity.this);
                                space3.setMinimumWidth(30);
                                space4 = new Space(SucheActivity.this);
                                space4.setMinimumWidth(30);
                                w4.setText(fach.getName());
                                w4.setTextSize(24);
                                zeile.addView(w1);
                                zeile.addView(space);
                                zeile.addView(w2);
                                zeile.addView(space2);
                                zeile.addView(i3);
                                zeile.addView(space3);
                                zeile.addView(w4);
                                zeile.addView(space4);
                                //nur Suchergebnisse, welche im aktuellen Kühlschrank liegen
                                if (lm.getFach()!=null && faecher.get(lm.getFach().getlNummer()) != null)
                                    tabelle.addView(zeile);
                        }

                        bar.setVisibility(View.GONE);
                    }
                };
                conn1.execute("params");
            }
        };
        conn.execute("params");
    }

    public void back(View v) {
        this.finish();
    }
}
