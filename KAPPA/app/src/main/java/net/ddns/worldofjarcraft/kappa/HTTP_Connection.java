package net.ddns.worldofjarcraft.kappa;

import android.content.Context;
import android.os.AsyncTask;

import net.ddns.worldofjarcraft.kappa.Security.MyAuthenticator;
import net.ddns.worldofjarcraft.kappa.Utils.StringUtils;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;

import static android.R.attr.duration;

/**
 * Created by Eric on 23.07.2016 nach http://stefan-draeger-software.de/blog/android-app-mit-mysql-datenbank-verbinden/.
 * Android lässt keine Netzwerkaktionen auf dem Mainthread zu, deshalb wird AsyncTask hier beerbt. Dieser erzeugt einen zweiten Thread, der alle Scriptaufrufe ausführen kann.
 * Die Parameter in den Winkelklammern stehen für die Rückgabetypen der folgenden Methoden.
 */
public class HTTP_Connection extends AsyncTask<Object, Void, String> {
    /**
     * Möglichkeit, das Abfrageergebnis aus dieser Klasse in eine andere Klasse zu übertragen. Darf nicht null sein.
     */
    public AsyncResponse delegate = null;

    /**
     * speichert die aufzurufende URL
     */
    private String url = "";

    private String method = "GET";
    private String postContent = "";

    //maximale Wiederholungen. Default 10.
    int mMaxRetries = 3;

    /**
     * Hauptkonstruktor der Klasse. Maximalzahl an Verbindungsversuchen ist 3.
     *
     * @param nachricht die komplette aufzurufende Web-Adresse
     */
    public HTTP_Connection(String nachricht) {
        //übergebene Werte in globalen Variablen speichern
        this.url = nachricht;

    }

    public static int timeoutmillis = 1000;
    /**
     * Zusätzlicher Konstruktor, in dem man eine Maximalzahl an Versuchen übergeben kann.
     *
     * @param nachricht die komplette aufzurufende Web-Adresse
     */
    public HTTP_Connection(String nachricht, int mMaxRetries) {
        //übergebene Werte in globalen Variablen speichern
        this.url = nachricht;
        this.mMaxRetries = mMaxRetries;
    }

    public HTTP_Connection(String url, int mMaxRetries, HashMap<String,String> postParams, String method) throws UnsupportedEncodingException {
        this.url = url;
        this.mMaxRetries = mMaxRetries;
        this.method = method;
        StringBuilder builder = new StringBuilder();
        for(String key : postParams.keySet()){
            String value = postParams.get(key);
            if (builder.length() != 0) builder.append('&');
            builder.append(URLEncoder.encode(key, "UTF-8"));
            builder.append('=');
            //Content might be Integer. In this case, do not encode in base64.
            try{
                Double.parseDouble(value != null ? value : "");
                builder.append(value);
            }
            catch (Exception e){
                builder.append(Constants.BASE64Prefix).append(URLEncoder.encode(StringUtils.toBase64(value != null ? value : ""),"ASCII"));
            }

        }
        this.postContent = builder.toString();
    }
    private Boolean authenticated = true;

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**
     * Methode, welche die Verbindung zur Webseite herstellt und den Content ausliest. Wird wiederholt, bis die Maximalzahl an versuchen überschritten wird oder eine Antwort eintrifft.
     *
     * @param params unnötige Parameter
     * @return Inhalt der Webseite, wenn eine Antwort eingetroffen ist, sonst null
     * @author Quelle: http://stackoverflow.com/questions/18359039/repeat-asynctask
     */
    private String repeatInBackground(Object... params) throws IOException {
        //Annahme, alles wäre gut gegangen
        //Initialisierung eines StringBuilders
        //versuchen, Script zu starten und Daten davon auszulesen
        if(authenticated) {
            Authenticator.setDefault(new MyAuthenticator());
        }
        //append GET params to URL
        if(method.equals("GET")&&!postContent.isEmpty()){
            url+="?"+postContent;
        }
        URL url = new URL(this.url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod(method);
        try {
            if(method.equals("POST")){
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(postContent);
                writer.close();
            }
            urlConnection.connect();
            if(urlConnection.getResponseCode() / 100 != 2){
                System.out.println("Got response code "+urlConnection.getResponseCode()+" for URL "+url);
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while(( line = reader.readLine())!=null){
                sb.append(line);
            }
            return sb.toString();
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * erster, durch "HTTP_Connection.execute" aufgerufener Void. Startet die Wiederholten Anfragen, bricht sie nach einer Anzahl an Versuchen ab.
     *
     * @param params Parameter für die Ausführung
     * @return Inhalt der eingelesenen Website als String, oder die Notiz "Es ist ein Netzwerkfehler aufgetreten!" bei 10 sinnlosen Versuchen.
     */
    @Override
    protected String doInBackground(Object... params) {
        int tries = 0;
        String result = null;

        /* This is the main loop, repeatInBackground will be repeated until result will not be null */
        while (tries++ < mMaxRetries && result == null) {
            try {
                result = repeatInBackground(params);
            } catch (Exception exception) {
                /* You might want to log the exception everytime, do it here. */
                //mException = exception;
                exception.printStackTrace();
                result="";
            }
        }

        return result;
    }

    /**
     * Wird nach der Abfrage aufgerufen.
     *
     * @param result Resultat der Abfrage = Inhalt der Website
     */
    @Override
    protected void onPostExecute(String result) {
        //prüfen, ob das Resultat weiterzugeben ist
        if (delegate != null) {
            delegate.processFinish(result, url);
            System.out.println("Ergebnis:" + result);
        }
        System.out.println("Ergebnis: " + result);
    }

}
