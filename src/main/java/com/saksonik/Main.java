package com.saksonik;

import com.saksonik.client.Client;
import com.saksonik.properties.ApplicationProperties;
import com.saksonik.server.AuthenticationServer;
import com.saksonik.server.GrantedServer;

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
    public static void main(String[] args) throws InterruptedException {
        AuthenticationServer authenticationServer = new AuthenticationServer();
        GrantedServer grantedServer = new GrantedServer();
        Thread authThread = new Thread(authenticationServer::start);
        Thread grantedThread = new Thread(grantedServer::start);

        authThread.start();
        grantedThread.start();

        Thread.sleep(1000);

        Client client = new Client();
        Thread clientThread = new Thread(client::authenticate);

        clientThread.start();
        clientThread.join();



    }
}
