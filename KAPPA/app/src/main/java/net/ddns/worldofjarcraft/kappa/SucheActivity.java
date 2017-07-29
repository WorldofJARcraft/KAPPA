package net.ddns.worldofjarcraft.kappa;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SucheActivity extends Activity {
    ProgressBar bar;
    String schrank = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suche);
         bar = findViewById(R.id.progressBarSuche);
        Button search = findViewById(R.id.search);
        schrank = getIntent().getExtras().getString("name");
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        bar.setVisibility(View.GONE);
    }
    HashMap<Integer,String> faecher;
    void search(){
        bar.setVisibility(View.VISIBLE);
        final EditText text = findViewById(R.id.sterm);
        HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/get_Fach.php?mail="+data.mail+"&pw="+data.pw+"&schrank="+schrank);
        AsyncResponse response = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                faecher = new HashMap<>();
                String[] daten = output.split("\\|");
                for(String fach:daten){
                    String[] werte = fach.split(";");
                    try{
                        int i= new Integer(werte[0]);
                        faecher.put(i,werte[1]);
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
        HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/search.php?mail="+data.mail+"&pw="+data.pw+"&sterm="+text.getText().toString().replaceAll(" ","%20"));
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                String[] lebensmittel = output.split("\\|");
                TableLayout tabelle = findViewById(R.id.ergebnisse);
                tabelle.removeAllViews();
                TableRow kopfzeile = new TableRow(SucheActivity.this);
                TextView v1= new TextView(SucheActivity.this);
                v1.setText(R.string.name);
                v1.setTextSize(24);
                v1.setTypeface(v1.getTypeface(), Typeface.BOLD);
                TextView v2= new TextView(SucheActivity.this);
                v2.setText(R.string.anzahl);
                v2.setTextSize(24);
                v2.setTypeface(v2.getTypeface(), Typeface.BOLD);
                TextView v3= new TextView(SucheActivity.this);
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
                for(String lm:lebensmittel){
                    String[] werte = lm.split(";");
                    if(werte.length>4) {
                        TableRow zeile = new TableRow(SucheActivity.this);
                        TextView w1 = new TextView(SucheActivity.this);
                        w1.setText(werte[1]);
                        w1.setTextSize(24);
                        TextView w2 = new TextView(SucheActivity.this);
                        w2.setText(werte[2]);
                        w2.setTextSize(24);
                        TextView i3 = new TextView(SucheActivity.this);
                        i3.setTextSize(24);
                        i3.setText(R.string.keine_Angabe);
                        try {
                            Long mhd = new Long(werte[3]);
                            if(mhd>0){
                                long rest = mhd - Calendar.getInstance().getTimeInMillis();
                                long aktzeit = Calendar.getInstance().getTimeInMillis();
                                if(rest>0){
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTimeInMillis(rest);
                                    String text="";
                                    long year = cal.get(Calendar.YEAR), month = cal.get(Calendar.MONTH), day= Calendar.DAY_OF_MONTH;
                                    System.out.println(year+","+month+","+day);
                                    if(cal.get(Calendar.YEAR)-1970>0)
                                        text+=(cal.get(Calendar.YEAR)-1970)+" " + getResources().getString(R.string.jahre)+", ";
                                    if(cal.get(Calendar.MONTH)>0)
                                        text+=cal.get(Calendar.MONTH)+" " + getResources().getString(R.string.monate)+", ";

                                    text+=cal.get(Calendar.DAY_OF_MONTH)+" " + getResources().getString(R.string.tage)+".";
                                    i3.setText(text);
                                    if(cal.get(Calendar.YEAR)-1970==0&&!text.contains(getResources().getString(R.string.monate))&&cal.get(Calendar.DAY_OF_MONTH)<8){
                                        i3.setTextColor(Color.YELLOW);
                                    }
                                    else
                                        i3.setTextColor(Color.GREEN);
                                }
                                else{
                                    i3.setText(R.string.abgelaufen);

                                    i3.setTextColor(Color.RED);
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        TextView w4 = new TextView(SucheActivity.this);
                        String fach = faecher.get(new Integer(werte[4]));
                         space = new Space(SucheActivity.this);
                        space.setMinimumWidth(30);
                         space2 = new Space(SucheActivity.this);
                        space2.setMinimumWidth(30);
                         space3 = new Space(SucheActivity.this);
                        space3.setMinimumWidth(30);
                         space4 = new Space(SucheActivity.this);
                        space4.setMinimumWidth(30);
                        w4.setText(fach);
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
                        if(faecher.containsKey(new Integer(werte[4])))
                        tabelle.addView(zeile);
                    }
                }

                bar.setVisibility(View.GONE);
            }
        };
        conn.execute("params");
            }
        };
        conn.delegate=response;
        conn.execute("params");
    }

    public void back(View v){
        this.finish();
    }
}
