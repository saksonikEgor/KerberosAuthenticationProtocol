package com.saksonik.properties;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class ApplicationProperties {
    public static final int AUTHENTICATION_SERVER_PORT = 12000;
    public static final int GRANTED_SERVER_PORT = 12001;
    public static final long AUTH_SERVER_ID = 722L;
    public static final long CLIENT_ID = 322L;
    public static Cipher SHARED_KEY_BETWEEN_CLIENT_AND_GRANTED_SERVER;
    public static Cipher SHARED_KEY_BETWEEN_AUTH_AND_GRANTED_SERVERS;
    public static final int VALID_PERIOD = 10;

    private ApplicationProperties() {
//
//        byte[] clientAndGrantedServerShared = "9mng65v8jf4lxn93nabf981m".getBytes();
//
//        byte[] iv = "a76nb5h9".getBytes();
//        IvParameterSpec ivSpec = new IvParameterSpec(iv);
//
//        SHARED_KEY_BETWEEN_CLIENT_AND_GRANTED_SERVER = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
//        SHARED_KEY_BETWEEN_CLIENT_AND_GRANTED_SERVER
//                .init(Cipher.ENCRYPT_MODE, new SecretKeySpec(clientAndGrantedServerShared, "TripleDES"), ivSpec);
//
//
//        byte[] authAndGrantedServerShared = "9mng65v8jf4lxn93nabf981m".getBytes();
//
//        SHARED_KEY_BETWEEN_AUTH_AND_GRANTED_SERVERS = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
//        SHARED_KEY_BETWEEN_AUTH_AND_GRANTED_SERVERS
//                .init(Cipher.ENCRYPT_MODE, new SecretKeySpec(authAndGrantedServerShared, "TripleDES"), ivSpec);
    }

    public static Cipher getSharedKeyBetweenClientAndGrantedServer() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        if (SHARED_KEY_BETWEEN_CLIENT_AND_GRANTED_SERVER == null) {
            byte[] clientAndGrantedServerShared = "9mng65v8jf4lxn93nabf981m".getBytes();

            byte[] iv = "a76nb5h9".getBytes();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            SHARED_KEY_BETWEEN_CLIENT_AND_GRANTED_SERVER = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
            SHARED_KEY_BETWEEN_CLIENT_AND_GRANTED_SERVER
                    .init(Cipher.ENCRYPT_MODE, new SecretKeySpec(clientAndGrantedServerShared, "TripleDES"), ivSpec);
        }
        return SHARED_KEY_BETWEEN_CLIENT_AND_GRANTED_SERVER;
    }

    public static Cipher getSharedKeyBetweenAuthAndGrantedServers() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        if (SHARED_KEY_BETWEEN_AUTH_AND_GRANTED_SERVERS == null) {
            byte[] authAndGrantedServerShared = "9mng65v8jf4lxn93nabf981m".getBytes();

            byte[] iv = "a76nb5h9".getBytes();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);


            SHARED_KEY_BETWEEN_AUTH_AND_GRANTED_SERVERS = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
            SHARED_KEY_BETWEEN_AUTH_AND_GRANTED_SERVERS
                    .init(Cipher.ENCRYPT_MODE, new SecretKeySpec(authAndGrantedServerShared, "TripleDES"), ivSpec);
        }
        return SHARED_KEY_BETWEEN_AUTH_AND_GRANTED_SERVERS;
    }
}
