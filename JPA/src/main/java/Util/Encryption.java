package Util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class Encryption {

    private static final String key = "MyEncryptionKey";

    public static String encryptData(String text) {
        byte[] decodedKey = Base64.getDecoder().decode(key);

        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, originalKey);
            byte[] cipherText = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static String decryptData(String text) {
        byte[] decodedKey = Base64.getDecoder().decode(key);

        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
            cipher.init(Cipher.DECRYPT_MODE, originalKey);
            byte[] cipherText = cipher.doFinal(Base64.getDecoder().decode(text));
            return new String(cipherText);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
