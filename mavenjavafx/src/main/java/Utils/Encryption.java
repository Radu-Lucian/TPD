package Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class Encryption {

    private static final String key = "MyEncryptionKey";

    public static String encryptData(byte[] bytes) {
        byte[] decodedKey = Base64.getDecoder().decode(key);

        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, originalKey);
            byte[] cipherText = cipher.doFinal(bytes);
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    public static byte[] decryptData(String text) {
        byte[] decodedKey = Base64.getDecoder().decode(key);

        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey originalKey = new SecretKeySpec(Arrays.copyOf(decodedKey, 16), "AES");
            cipher.init(Cipher.DECRYPT_MODE, originalKey);
            return cipher.doFinal(Base64.getDecoder().decode(text));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new byte[] {};
    }

    public static byte[] decodeBase64(String text) {
        Base64.Decoder base64Decoder = Base64.getUrlDecoder();
        return base64Decoder.decode(text.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static String encodeBase64(byte[] byteFile) {
        Base64.Encoder base64Encoder = Base64.getUrlEncoder();
        return new String(base64Encoder.encode(byteFile), StandardCharsets.ISO_8859_1);
    }

    public static byte[] decode(String text, String cypher) {
        if (cypher.equals("false"))
            return decodeBase64(text);
        return decryptData(text);
    }

    public static String encode(byte[] byteFile, String cypher) {
        if (cypher.equals("Base64"))
            return encodeBase64(byteFile);
        return encryptData(byteFile);
    }
}