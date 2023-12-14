package com.saksonik;

import com.saksonik.properties.ApplicationProperties;

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
import java.util.Arrays;
import java.util.Base64;

public class Main {
    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] secretKey = "9mng65v8jf4lxn93nabf981m".getBytes();

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "TripleDES");
        byte[] iv = "a76nb5h9".getBytes();
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        String secretMessage = "Baeldung secret message";
        Cipher encryptCipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);

        byte[] secretMessagesBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessagesBytes);

        String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);

        System.out.println("message = " + secretMessage);
        System.out.println("messageBytes = " + Arrays.toString(secretMessagesBytes));
        System.out.println("encryptedMessageBytes = " + Arrays.toString(encryptedMessageBytes));
        System.out.println(encodedMessage);


        System.out.println("_________");
//        System.out.println(ApplicationProperties.getSharedKeyBetweenClientAndGrantedServer());
//        System.out.println(ApplicationProperties.getSharedKeyBetweenAuthAndGrantedServers());
    }
}
