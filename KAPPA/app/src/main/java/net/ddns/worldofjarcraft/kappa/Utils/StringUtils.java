package net.ddns.worldofjarcraft.kappa.Utils;

import android.util.Base64;

import java.nio.charset.Charset;

public class StringUtils {

    public static String toBase64(String input){
        return Base64.encodeToString(input.getBytes(Charset.forName("utf-8")),Base64.DEFAULT);
    }

    public static String fromBase64(String  input){
        return new String(Base64.decode(input,Base64.DEFAULT),Charset.forName("utf-8"));
    }
}
