package net.ddns.worldofjarcraft.kappa;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.logging.Logger;

public class LaunchActivity extends AppCompatActivity {
    /**
     * Name der Datei, in der die Logindaten gespeichert werden.
     */
    public static final String login_name="LOGIN";
    /**
     * Unter diesem Namen wird der Benutzername gespeichert.
     */
    public static final String user_preference = "mail";
    /**
     * Unter diesem Namen wird das Passwort gespeichert.
     */
    public static final String user_password = "pw";
    PendingIntent pendingIntent;
    BroadcastReceiver br;
    AlarmManager am;

    Intent mServiceIntent;
    @Override
    protected void onStart(){
        super.onStart();
        mServiceIntent = new Intent(getApplicationContext(), MHDCheckerService.class);
        mServiceIntent.setData(Uri.EMPTY);
        SharedPreferences login = getSharedPreferences(login_name,MODE_PRIVATE);
        if((login.contains(user_preference)&&login.contains(user_password))||(data.mail!=null&&data.pw!=null)){
            System.out.println("Melde mich mit gespeiucherten Daten an!");
            data.mail=login.getString(user_preference,null);
            data.pw=login.getString(user_password,null);
            //prüfen, ob Daten gültig sind
            HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/check_Passwort.php?mail="+data.mail+"&pw="+data.pw);
            conn.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String url) {
                    if(output.equals("true")){
                        System.out.println("Daten korrekt!");
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
                        builder.setTitle(R.string.unauth_titel);
                        builder.setMessage(R.string.unauth);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setTheme(R.style.AppTheme);
                                startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
                            }
                        });
                        builder.show();
                    }
                }
            };
            conn.execute("params");
        }

        else{
            System.out.println("Frage Nutzerdaten an!");
            setTheme(R.style.AppTheme);
            startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
        }

    }
    @Override
    protected void onPostResume(){
        super.onPostResume();
        SharedPreferences login = getSharedPreferences(login_name,MODE_PRIVATE);
        if((login.contains(user_preference)&&login.contains(user_password))||(data.mail!=null&&data.pw!=null)){
            System.out.println("Melde mich mit gespeiucherten Daten an!");
            data.mail=login.getString(user_preference,null);
            data.pw=login.getString(user_password,null);
            //prüfen, ob Daten gültig sind
            HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/check_Passwort.php?mail="+data.mail+"&pw="+data.pw);
            conn.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String url) {
                    if(output.equals("true")){
                        System.out.println("Daten korrekt!");
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
                        builder.setTitle(R.string.unauth_titel);
                        builder.setMessage(R.string.unauth);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setTheme(R.style.AppTheme);
                                startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
                            }
                        });
                        builder.show();
                    }
                }
            };
            conn.execute("params");
        }

        else{
            System.out.println("Frage Nutzerdaten an!");
            setTheme(R.style.AppTheme);
            startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        setTheme(R.style.SplashTheme);
        final Button logoff = (Button) findViewById(R.id.logoffButton);
        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoff();
            }
        });
        final Button einkaufButton = (Button) findViewById(R.id.einkaufButton);
        einkaufButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                einkauf();
            }
        });
        final Button button = (Button) findViewById(R.id.schraenkeButton);
        Button butt = (Button) findViewById(R.id.startDaemon);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(mServiceIntent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                schraenke();
            }
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //login-Daten privat auslesen und speichern
        SharedPreferences login = getSharedPreferences(login_name,MODE_PRIVATE);
        if((login.contains(user_preference)&&login.contains(user_password))||(data.mail!=null&&data.pw!=null)){
            System.out.println("Melde mich mit gespeiucherten Daten an!");
            data.mail=login.getString(user_preference,null);
            data.pw=login.getString(user_password,null);
            //prüfen, ob Daten gültig sind
            HTTP_Connection conn = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/check_Passwort.php?mail="+data.mail+"&pw="+data.pw);
            conn.delegate = new AsyncResponse() {
                @Override
                public void processFinish(String output, String url) {
                    if(output.equals("true")){
                        System.out.println("Daten korrekt!");
                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(LaunchActivity.this);
                        builder.setTitle(R.string.unauth_titel);
                        builder.setMessage(R.string.unauth);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setTheme(R.style.AppTheme);
                                startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
                            }
                        });
                        builder.show();
                    }
                }
            };
            conn.execute("params");
        }

        else{
            System.out.println("Frage Nutzerdaten an!");
            setTheme(R.style.AppTheme);
            startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
        }
        //beendet Anzeige des Splash
        setTheme(R.style.AppTheme);
        System.out.println("Benutzername: "+data.mail +", Passwort "+data.pw);
    }

    public void logoff(){
        SharedPreferences login = getSharedPreferences(login_name,MODE_PRIVATE);
        SharedPreferences.Editor editor = login.edit();
        editor.remove(user_preference);
        editor.remove(user_password);
        editor.commit();
        data.mail=data.pw=null;
        System.out.println("Frage Nutzerdaten an!");
        setTheme(R.style.AppTheme);
        startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
    }

    public void einkauf(){
        startActivity(new Intent(this,EinkaufActivity.class));
    }
    public void schraenke(){startActivity(new Intent(this, SchrankActivity.class));}
}
