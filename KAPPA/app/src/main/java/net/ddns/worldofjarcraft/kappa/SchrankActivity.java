package net.ddns.worldofjarcraft.kappa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SchrankActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schrank);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.neuerSchrank);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSchrank();
            }
        });
        FloatingActionButton akt = (FloatingActionButton) findViewById(R.id.reloadSchraenke);
        akt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aktualisieren();
            }
        });
        aktualisieren();
    }
    List<Pair<Integer,String>> liste;
    public void aktualisieren(){
        HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/getSchrank.php?mail="+data.mail+"&pw="+data.pw,2);
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                LinearLayout schraenke = (LinearLayout) findViewById(R.id.schraenkeListe);
                schraenke.removeAllViews();
                liste = new ArrayList<>();
                if(!output.isEmpty()){
                String[] boxen = output.split("\\|");
                for(String box:boxen){
                    String[] attribute = box.split(";");
                    liste.add(new Pair<Integer, String>(new Integer(attribute[0]), attribute[1]));
                    TextView v = new TextView(SchrankActivity.this);
                    v.setText(attribute[1]);
                    v.setTextSize(24);
                    v.setTextColor(getResources().getColor(R.color.colorPrimary));
                    v.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            final View v = view;
                            AlertDialog.Builder builder = new AlertDialog.Builder(SchrankActivity.this);
                            builder.setTitle(R.string.schrank_loeschen_titel);
                            builder.setMessage(R.string.schrank_loeschen);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    delete(v);
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.show();
                            return false;
                        }
                    });
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open(view);
                        }
                    });
                    Space s = new Space(SchrankActivity.this);
                    s.setMinimumHeight(30);
                    schraenke.addView(v);
                    schraenke.addView(s);
                }
                }
                ProgressBar prog = (ProgressBar) findViewById(R.id.prog_schrank);
                prog.setVisibility(View.GONE);
            }
        };
        ProgressBar prog = (ProgressBar) findViewById(R.id.prog_schrank);
        prog.setVisibility(View.VISIBLE);
        conn.execute("params");
    }
    public void delete(View v){
        System.out.println("Lösche Kühlschrank...");
        int index = identify(v, (LinearLayout) findViewById(R.id.schraenkeListe));
        if(index==-1){
            aktualisieren();
        }
        else{
        Pair<Integer,String> werte = liste.get(index);
            HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/delete_Schrank.php?mail="+data.mail+"&pw="+data.pw+"&id="+werte.first);
            conn.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String url) {
                    aktualisieren();
                }
            };
            conn.execute("params");
        }
    }

    public void open(View v){
        System.out.println("Öffne Kühlschrank...");
        int index = identify(v, (LinearLayout) findViewById(R.id.schraenkeListe));
        if(index==-1){
            Toast.makeText(this, R.string.fehler_schrank,Toast.LENGTH_LONG).show();
            aktualisieren();
        }
        else{
            Pair<Integer,String> werte = liste.get(index);
            Intent intent = new Intent(SchrankActivity.this, InhaltActivity.class);
            Bundle b = new Bundle();
            b.putInt("schrank", werte.first);
            b.putString("name",werte.second);
            //Your id
            intent.putExtras(b); //Put your id to your next Intent
            startActivity(intent);
        }
    }

    public int identify(View view, LinearLayout layout){
        int nummer = -1;
        int spaces = 0;
        for(int i=0;i<layout.getChildCount();i++){
            View v = layout.getChildAt(i);
            if(v==view){
                nummer=i-spaces;
            }
            else if (v instanceof Space)
                spaces++;
        }
        return  nummer;
    }
    public void addSchrank(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.neuer_Schrank);
// Set up the input
        final TextView info_Name= new TextView(this);
        info_Name.setText(R.string.info_Name);
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_CORRECT);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(info_Name);
        layout.addView(input);

        builder.setView(layout);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = "https://worldofjarcraft.ddns.net/kappa/neuer_Schrank.php?mail="+data.mail+"&pw="+data.pw+"&name="+input.getText().toString().replaceAll(" ","%20")+"&faecher=0";
                HTTP_Connection login = new HTTP_Connection(url,2);
                AsyncResponse response = new AsyncResponse() {
                    @Override
                    public void processFinish(String output, String url) {
                        ProgressBar prog = (ProgressBar) findViewById(R.id.prog_schrank);
                        prog.setVisibility(View.GONE);
                        if(output.equals("Erfolg")){
                            aktualisieren();
                        }
                        else{
                            Toast.makeText(SchrankActivity.this,"Es ist ein Netzwerkfehler aufgetreten.",Toast.LENGTH_LONG).show();
                            prog.setVisibility(View.GONE);
                        }
                    }
                };
                login.delegate = response;
                login.execute();
                ProgressBar prog = (ProgressBar) findViewById(R.id.prog_schrank);
                prog.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
