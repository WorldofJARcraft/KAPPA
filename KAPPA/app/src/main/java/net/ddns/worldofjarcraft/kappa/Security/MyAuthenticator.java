package net.ddns.worldofjarcraft.kappa.Security;

import net.ddns.worldofjarcraft.kappa.data;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * EXPERIMENT: Set global authentication for KAPPA.
 */
public class MyAuthenticator extends Authenticator {

    public PasswordAuthentication getPasswordAuthentication () {
        return new PasswordAuthentication (data.mail, data.pw.toCharArray());
    }
}
