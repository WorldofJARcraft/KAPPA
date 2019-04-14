package net.ddns.worldofjarcraft.kappa;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import net.ddns.worldofjarcraft.kappa.Model.Benutzer;

import org.springframework.http.HttpMethod;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static net.ddns.worldofjarcraft.kappa.data.pw;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        Button signin = (Button) findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        Button signup = (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });
        final Button forgot = (Button) findViewById(R.id.vergessen);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forgot_pw();
            }
        });
    }
    public void login(){
        AsyncResponse response = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                System.out.println("Antwort \""+output+"\" von \""+url+"\"");
                ProgressBar prog = (ProgressBar) findViewById(R.id.progress);
                prog.setVisibility(View.GONE);
                if(output.equals("true")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.logged_in);
                    DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CheckBox box = (CheckBox) findViewById(R.id.save);
                            if(box.isChecked()){
                                SharedPreferences settings = getSharedPreferences(LaunchActivity.login_name, MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString(LaunchActivity.user_preference, data.mail);
                                editor.putString(LaunchActivity.user_password, data.pw);
                                // Commit the edits!
                                editor.apply();
                            }
                            dialogInterface.dismiss();
                            startActivity(new Intent(LoginActivity.this,LaunchActivity.class));
                        }
                    };
                    builder.setNeutralButton("OK",posListener);
                    builder.setCancelable(true);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.login_impossible);
                    DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    };
                    builder.setPositiveButton("OK",posListener);
                    builder.show();
                }
            }
        };
        EditText mail = (EditText) findViewById(R.id.mail);
        String email = mail.getText().toString();
        data.mail=email;
        EditText pw = (EditText) findViewById(R.id.pw);
        String passw = pw.getText().toString();
        data.pw = passw;
        HTTP_Connection login = new HTTP_Connection(Constants.Server_Adress+"/user/"+email,2);
        login.delegate = response;
        login.execute();
        ProgressBar prog = (ProgressBar) findViewById(R.id.progress);
        prog.setVisibility(View.VISIBLE);
    }
    public void signup(){
        AsyncResponse response = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                System.out.println("Antwort \""+output+"\" von \""+url+"\"");
                ProgressBar prog = (ProgressBar) findViewById(R.id.progress);
                prog.setVisibility(View.GONE);
                if(output.equals("true")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.signed_up);
                    DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CheckBox box = (CheckBox) findViewById(R.id.save);
                            if(box.isChecked()){
                                SharedPreferences settings = getSharedPreferences(LaunchActivity.login_name, MODE_PRIVATE);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString(LaunchActivity.user_preference, data.mail);
                                editor.putString(LaunchActivity.user_password, data.pw);
                                // Commit the edits!
                                editor.commit();
                            }
                            dialogInterface.dismiss();
                            startActivity(new Intent(LoginActivity.this,LaunchActivity.class));
                        }
                    };
                    builder.setNeutralButton("OK",posListener);
                    builder.setCancelable(true);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(output.equals("User schon vorhanden.")){
                    Toast.makeText(LoginActivity.this,R.string.user_schon_vorhanden,Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(LoginActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                }
            }
        };
        EditText mail = (EditText) findViewById(R.id.mail);
        String email = mail.getText().toString();
        data.mail=email;
        EditText pw = (EditText) findViewById(R.id.pw);
        String passw = pw.getText().toString();
        data.pw = passw;
        HashMap<String,String> map = new HashMap<>();
        map.put("EMail",email);
        map.put("Password",passw);
        HTTP_Connection login = null;
        try {
            login = new HTTP_Connection(Constants.Server_Adress+"/user/create",2,map, "GET");
            login.setAuthenticated(false);
            login.delegate = response;
            login.execute();
            ProgressBar prog = (ProgressBar) findViewById(R.id.progress);
            prog.setVisibility(View.VISIBLE);
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(LoginActivity.this,R.string.can_not_encode_mail_or_pw,Toast.LENGTH_LONG).show();
        }

    }
    private String passwort="";
    public void forgot_pw(){
        final AsyncResponse response = new AsyncResponse() {
            @Override
            public void processFinish(String output, String url) {
                System.out.println("Antwort \""+output+"\" von \""+url+"\"");
                ProgressBar prog = (ProgressBar) findViewById(R.id.progress);
                prog.setVisibility(View.GONE);
                if(output.equals("Erfolg")){
                    data.mail=null;
                    data.pw=null;
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.resetted);
                    DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CheckBox box = (CheckBox) findViewById(R.id.save);

                            dialogInterface.dismiss();
                        }
                    };
                    builder.setNeutralButton("OK",posListener);
                    builder.setCancelable(true);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if (output.equals("User existiert nicht.")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.user_nicht_da);
                    DialogInterface.OnClickListener posListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    };
                    builder.setPositiveButton("OK",posListener);
                    builder.show();
                }
                else{
                    Toast.makeText(LoginActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
                }
            }
        };
        EditText mail = (EditText) findViewById(R.id.mail);
        final  String email = mail.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.forgot_PW);
        builder.setMessage(R.string.neues_PW);
// Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                passwort = input.getText().toString();
                HTTP_Connection login = new HTTP_Connection("https://worldofjarcraft.ddns.net/kappa/reset_PW.php?mail="+email+"&pw="+passwort+"",2);
                login.delegate = response;
                login.execute();
                ProgressBar prog = (ProgressBar) findViewById(R.id.progress);
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
        data.pw = null;
    }
}
