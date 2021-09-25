package uz.tm.tashman.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class AES {
    private static final String secretKey = "teledocandxmed";
    private static final String algorithm = "AES/ECB/PKCS5PADDING";
    private static SecretKeySpec secretKeySpec;

    public static void main(String[] args) {
        String originalString = "9860150101533464";//"10/24";//
        String encryptedString = uz.tm.tashman.util.AES.encrypt(originalString);
        String decryptedString = uz.tm.tashman.util.AES.decrypt(encryptedString);

        System.out.println(originalString);
        assert encryptedString != null;
        System.out.println(encryptedString + ", length: " + encryptedString.length());
        System.out.println(decryptedString);
        System.out.println("3223: " + uz.tm.tashman.util.AES.decrypt("sZnIR7n7+MmlPe6Tuwq6DA=="));
    }

    public static void setKey(String myKey) {
        MessageDigest sha;
        try {
            byte[] key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt) {
        try {
            setKey(secretKey);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt) {
        try {
            setKey(secretKey);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
