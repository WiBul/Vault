package com.example.thevault.support.hashing;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptWachtwoordHash {
    private final static int RONDES = 12;

    public static String hashWachtwoord(String wachtwoord){
        return BCrypt.hashpw(wachtwoord, BCrypt.gensalt(RONDES));
    }

    public static boolean verifyHash(String wachtwoord, String hash) {
        return BCrypt.checkpw(wachtwoord, hash);
    }


}
