package com.example.kantorwalut;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class PasswordUtils {
    public static String encode(String s) {
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(m).update(s.getBytes(), 0, s.length());
        return new BigInteger(1, m.digest()).toString(16);
    }
}
