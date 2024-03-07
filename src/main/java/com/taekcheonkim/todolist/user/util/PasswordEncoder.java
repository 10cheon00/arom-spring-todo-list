package com.taekcheonkim.todolist.user.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordEncoder {
    private final String secretKey;

    public PasswordEncoder(@Value(value = "${user.secret-key}")String secretKey) {
        this.secretKey = secretKey;
    }

    public String encode(String password) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            return convertByteToHexString(mac.doFinal());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No such algorithm. This will caused from no encode algorithm in MessageDigest.");
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            System.out.println("Invalid secret key. This will caused from your secret key in application-**.yml");
            throw new RuntimeException(e);
        }
    }

    private String convertByteToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b : bytes) {
            stringBuilder.append(String.format("%02X", b));
        }
        return stringBuilder.toString();
    }
}
