package com.saksonik.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CoderUtils {
    private CoderUtils() {

    }

    public static byte[] encryptString(String message, String key) throws Exception {
        Cipher cipher = getEncryptCipher(key);

        byte[] secretMessagesBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = cipher.doFinal(secretMessagesBytes);

        return Base64.getEncoder().encode(encryptedMessageBytes);
    }

    public static byte[] encryptLong(long number, String key)
            throws IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] secretMessagesBytes = Long.toBinaryString(number).getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = getEncryptCipher(key).doFinal(secretMessagesBytes);

        return Base64.getEncoder().encode(encryptedMessageBytes);
    }

    public static byte[] encryptInteger(int number, String key) throws IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException {
        byte[] secretMessagesBytes = Integer.toBinaryString(number).getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = getEncryptCipher(key).doFinal(secretMessagesBytes);

        return Base64.getEncoder().encode(encryptedMessageBytes);
    }

    public static String decryptToString(byte[] message, String key) throws IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException {
        byte[] decryptedMessageBytes = getDencryptCipher(key).doFinal(Base64.getDecoder().decode(message));
        return new String(decryptedMessageBytes, StandardCharsets.UTF_8);
    }

    public static long decryptToLong(byte[] number, String key) throws IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException {
        byte[] decryptNumberBytes = getDencryptCipher(key).doFinal(Base64.getDecoder().decode(number));
        return Long.parseLong(new String(decryptNumberBytes, StandardCharsets.UTF_8), 2);
    }

    public static int decryptToInteger(byte[] number, String key) throws IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException {
        byte[] decryptNumberBytes = getDencryptCipher(key).doFinal(Base64.getDecoder().decode(number));
        return Integer.parseInt(new String(decryptNumberBytes, StandardCharsets.UTF_8), 2);
    }

    public static String decryptToKey(byte[] encodedKey, String key)
            throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        return decryptToString(encodedKey, key);
    }

    private static Cipher getEncryptCipher(String key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] secretKey = key.getBytes();

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");
        byte[] iv = "a76nb5h9".getBytes();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher encryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

        return encryptCipher;
    }

    private static Cipher getDencryptCipher(String key) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] secretKey = key.getBytes();

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");
        byte[] iv = "a76nb5h9".getBytes();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher decryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

        return decryptCipher;
    }
}
