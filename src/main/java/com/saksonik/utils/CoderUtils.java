package com.saksonik.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CoderUtils {
    private CoderUtils() {

    }

    public static byte[] encryptString(String message, Cipher cipher) throws Exception {
//        byte[] secretKey = "9mng65v8jf4lxn93nabf981m".getBytes();
//        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");
//
//        byte[] iv = "a76nb5h9".getBytes();
//        IvParameterSpec ivSpec = new IvParameterSpec(iv);

//        Cipher encryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
//        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

        byte[] secretMessagesBytes = message.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = cipher.doFinal(secretMessagesBytes);


        return Base64.getEncoder().encode(encryptedMessageBytes);
    }

    public static byte[] encryptLong(long number, Cipher cipher)
            throws IllegalBlockSizeException, BadPaddingException {
        byte[] secretMessagesBytes = Long.toBinaryString(number).getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = cipher.doFinal(secretMessagesBytes);

        return Base64.getEncoder().encode(encryptedMessageBytes);
    }

    public static byte[] encryptInteger(int number, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        byte[] secretMessagesBytes = Integer.toBinaryString(number).getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = cipher.doFinal(secretMessagesBytes);

        return Base64.getEncoder().encode(encryptedMessageBytes);
    }

    public static String decryptToString(byte[] message, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
//        Cipher decryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
//        decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);

        byte[] decryptedMessageBytes = cipher.doFinal(Base64.getDecoder().decode(message));

        return new String(decryptedMessageBytes, StandardCharsets.UTF_8);
    }

    public static long decryptToLong(byte[] number, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        byte[] decryptNumberBytes = cipher.doFinal(Base64.getDecoder().decode(number));

        return ByteBuffer.wrap(decryptNumberBytes).getLong();
    }

    public static int decryptToInteger(byte[] number, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        byte[] decryptNumberBytes = cipher.doFinal(Base64.getDecoder().decode(number));

        return ByteBuffer.wrap(decryptNumberBytes).getInt();
    }

    public static Cipher decryptToKey(byte[] key, Cipher cipher)
            throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException {
        String decryptedKey = decryptToString(key, cipher);

        byte[] secretKey = decryptedKey.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");

//        byte[] iv = "a76nb5h9".getBytes();
//        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher encryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        return encryptCipher;
    }

    public static Cipher createKeyByString(String seed) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] secretKey = seed.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");

        Cipher encryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        return encryptCipher;
    }
}
