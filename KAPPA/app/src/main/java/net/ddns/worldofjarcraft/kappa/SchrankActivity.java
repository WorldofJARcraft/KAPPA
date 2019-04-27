package net.ddns.worldofjarcraft.kappa;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Pair;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import net.ddns.worldofjarcraft.kappa.Model.Kuehlschrank;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
        ImageButton b = findViewById(R.id.back_Schrank);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back(view);
            }
        });
        aktualisieren();
    }
    List<Kuehlschrank> liste;
    public void aktualisieren(){
        HTTP_Connection conn = new HTTP_Connection(Constants.Server_Adress+"/schrank/getAll");
        conn.delegate = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                LinearLayout schraenke = (LinearLayout) findViewById(R.id.schraenkeListe);
                schraenke.removeAllViews();
                liste = new ArrayList<>();
                if(!output.isEmpty()){
                    Kuehlschrank[] schränke = new Gson().fromJson(output,Kuehlschrank[].class);
                for(Kuehlschrank schrank:schränke){
                    try {
                        liste.add(schrank);
                        TextView v = new TextView(SchrankActivity.this);
                        v.setText(schrank.getName());
                        v.setTextSize(24);
                        v.setTextColor(getResources().getColor(R.color.black));
                        v.setBackgroundResource(R.drawable.textviewstyle);
                        v.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                final View v = view;
                                PopupMenu popup = new PopupMenu(SchrankActivity.this, v);
                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem item) {
                                        switch (item.getItemId()){
                                            case R.id.delete:
                                                delete(v);
                                                break;
                                            case R.id.update:
                                                update(v);
                                        }
                                        return false;
                                    }
                                });
                                MenuInflater inflater = popup.getMenuInflater();
                                inflater.inflate(R.menu.update_delete_menu, popup.getMenu());
                                popup.show();
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
                    }catch (Exception e){e.printStackTrace();}

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

    private void update(View v) {
        System.out.println("Updating...");
    }

    public void delete(View v){
        final View view = v;
        AlertDialog.Builder builder = new AlertDialog.Builder(SchrankActivity.this);
        builder.setTitle(R.string.schrank_loeschen_titel);
        builder.setMessage(R.string.schrank_loeschen);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int index = identify(view, (LinearLayout) findViewById(R.id.schraenkeListe));
                if(index==-1){
                    aktualisieren();
                }
                else{
                    Kuehlschrank kuehlschrank = liste.get(index);
                    HTTP_Connection conn = new HTTP_Connection(Constants.Server_Adress+"/schrank/"+kuehlschrank.getLaufNummer()+"/delete");
                    conn.delegate = new AsyncResponse() {
                        @Override
                        public void processFinish(String output, String url) {
                            aktualisieren();
                        }
                    };
                    conn.execute("params");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
        System.out.println("Lösche Kühlschrank...");

    }

    public void open(View v){
        System.out.println("Öffne Kühlschrank...");
        int index = identify(v, (LinearLayout) findViewById(R.id.schraenkeListe));
        if(index==-1){
            Toast.makeText(this, R.string.fehler_schrank,Toast.LENGTH_LONG).show();
            aktualisieren();
        }
        else{
            Kuehlschrank schrank = liste.get(index);
            Intent intent = new Intent(SchrankActivity.this, InhaltActivity.class);
            Bundle b = new Bundle();
            b.putInt("schrank", schrank.getLaufNummer());
            b.putString("name",schrank.getName());
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
                String url = Constants.Server_Adress+"/schrank/new";

                HashMap<String,String> params = new HashMap<>();
                params.put("name",input.getText().toString());
                params.put("faecher","0");
                HTTP_Connection login = null;
                try {
                    login = new HTTP_Connection(url,2,params,"GET");
                } catch (UnsupportedEncodingException e) {
                    return;
                }
                login.delegate = new AsyncResponse() {
                    @Override
                    public void processFinish(String output, String url1) {
                        ProgressBar prog1 = (ProgressBar) findViewById(R.id.prog_schrank);
                        prog1.setVisibility(View.GONE);
                        if(!output.equals("Unauthorized!")){
                            aktualisieren();
                        }
                        else{
                            Toast.makeText(SchrankActivity.this,"Es ist ein Netzwerkfehler aufgetreten.",Toast.LENGTH_LONG).show();
                            prog1.setVisibility(View.GONE);
                        }
                    }
                };
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

    public void back(View v){
        this.finish();
    }
}
