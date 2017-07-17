package net.ddns.worldofjarcraft.kappa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        data.mail=data.pw=null;
        System.out.println("Frage Nutzerdaten an!");
        setTheme(R.style.AppTheme);
        startActivity(new Intent(LaunchActivity.this,LoginActivity.class));
    }

    public void einkauf(){
        startActivity(new Intent(this,EinkaufActivity.class));
    }
}