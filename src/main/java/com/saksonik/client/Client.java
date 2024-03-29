package com.saksonik.client;

import com.saksonik.model.AuthenticationServerRequest;
import com.saksonik.model.AuthenticationServerResponse;
import com.saksonik.model.GrantedServerRequest;
import com.saksonik.model.GrantedServerResponse;
import com.saksonik.properties.ApplicationProperties;
import com.saksonik.utils.CoderUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
    private static final Logger LOGGER = Logger.getLogger("com.something");
    private final long clientId = ApplicationProperties.CLIENT_ID;
    private final String grantedServerSharedKey = ApplicationProperties.SHARED_KEY_BETWEEN_CLIENT_AND_GRANTED_SERVER;
    private final Socket authServerSocket;
    private final Socket grantedServerSocket;
    private String authServerSharedKey;
    private long timeStamp;
//    private static final Logger LOGGER = LogManager.getLogger();

    public Client() {
        try {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            LOGGER.addHandler(handler);
            LOGGER.setLevel(Level.ALL);

            authServerSocket = new Socket(InetAddress.getLocalHost(), ApplicationProperties.AUTHENTICATION_SERVER_PORT);
            grantedServerSocket = new Socket(InetAddress.getLocalHost(), ApplicationProperties.GRANTED_SERVER_PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.authenticate();
    }

    public void authenticate() {
        // Client must know authServerId
        final long authServerId = ApplicationProperties.AUTH_SERVER_ID;

        sendGrantedServerRequest(authServerId);
        GrantedServerResponse grantedServerResponse = getGrantedServerResponse();

        authServerSharedKey = extractAuthServerSharedKeyFromResponse(grantedServerResponse);
        LOGGER.fine("Client || getting Auth server shared key for Grand server = " + authServerSharedKey);

        sendAuthServerRequest(grantedServerResponse);
        AuthenticationServerResponse authenticationServerResponse = getAuthServerResponse();

        long encryptedTimeStamp = extractTimeStamp(authenticationServerResponse);
        LOGGER.fine("Client || encrypted Auth server timeStamp = " + encryptedTimeStamp);
    }

    private void sendGrantedServerRequest(long authServerId) {
        LOGGER.fine("Client || sending request to the Granted server");

        GrantedServerRequest request = new GrantedServerRequest(
                clientId,
                authServerId,
                ThreadLocalRandom.current().nextInt()
        );

        try {
            ObjectOutputStream oos = new ObjectOutputStream(grantedServerSocket.getOutputStream());
            oos.writeObject(request);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GrantedServerResponse getGrantedServerResponse() {
        GrantedServerResponse response = null;

        try {
            ObjectInputStream oos = new ObjectInputStream(grantedServerSocket.getInputStream());
            response = (GrantedServerResponse) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        LOGGER.fine("Client || getting response form the Granted server: " + response);

        return response;
    }

    private String extractAuthServerSharedKeyFromResponse(GrantedServerResponse response) {
        try {
            return CoderUtils.decryptToKey(response.clientAndAuthServerSharedKeyForClient(), grantedServerSharedKey);
        } catch (IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException
                 | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    private void setTimeStamp() {
        timeStamp = Instant.now().toEpochMilli();
    }

    private void sendAuthServerRequest(GrantedServerResponse response) {
        setTimeStamp();
        LOGGER.fine("Client || setting time stamp: " + timeStamp);
        LOGGER.fine("Client || sending request to the Auth server");

        try {
            ObjectOutputStream oos = new ObjectOutputStream(authServerSocket.getOutputStream());

            AuthenticationServerRequest request = new AuthenticationServerRequest(
                    CoderUtils.encryptLong(clientId, authServerSharedKey),
                    CoderUtils.encryptLong(timeStamp, authServerSharedKey),
                    CoderUtils.encryptInteger(
                            CoderUtils.decryptToInteger(response.validPeriodForClient(), grantedServerSharedKey),
                            authServerSharedKey
                    ),
                    response.clientAndAuthServerSharedKeyForServer(),
                    response.clientIdForServer(),
                    response.validPeriodForServer()
            );

            oos.writeObject(request);
        } catch (IOException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException |
                 NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthenticationServerResponse getAuthServerResponse() {
        AuthenticationServerResponse response = null;

        try {
            ObjectInputStream oos = new ObjectInputStream(authServerSocket.getInputStream());
            response = (AuthenticationServerResponse) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        LOGGER.fine("Client || getting response form the Auth server: " + response);
        return response;
    }

    private long extractTimeStamp(AuthenticationServerResponse response) {
        try {
            return CoderUtils.decryptToLong(response.timeStamp(), authServerSharedKey);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException |
                 NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
