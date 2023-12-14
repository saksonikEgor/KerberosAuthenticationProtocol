package com.saksonik.server;

import com.saksonik.model.AuthenticationServerRequest;
import com.saksonik.model.AuthenticationServerResponse;
import com.saksonik.properties.ApplicationProperties;
import com.saksonik.utils.CoderUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationServer {
    private static final int PORT = ApplicationProperties.AUTHENTICATION_SERVER_PORT;
    private final long authServerId = ApplicationProperties.AUTH_SERVER_ID;
    private long clientId;
    private final String grantedServerSharedKey = ApplicationProperties.SHARED_KEY_BETWEEN_AUTH_AND_GRANTED_SERVERS;
    private String clientSharedKey;
    private ServerSocket serverSocket;
    private static final java.util.logging.Logger LOGGER = Logger.getLogger("com.something");

    public AuthenticationServer() {
        try {
            ConsoleHandler handler = new ConsoleHandler();
            handler.setLevel(Level.ALL);
            LOGGER.addHandler(handler);
            LOGGER.setLevel(Level.ALL);

            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        LOGGER.fine("Auth server || started");

        while (true) {
            try {
                Socket client = serverSocket.accept();

                sendResponse(client);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    private void sendResponse(Socket client) {
        try {
            AuthenticationServerRequest request = getRequest(client.getInputStream());
            extractClientSharedKey(request);

            LOGGER.fine("Auth server || received shared key from the client: " + clientSharedKey);

            AuthenticationServerResponse response = makeResponse(request);

            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(response);

            LOGGER.fine("Auth server || sending response to the client: " + response);
        } catch (IOException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException |
                 NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthenticationServerRequest getRequest(InputStream inputStream) {
        AuthenticationServerRequest request = null;

        try {
            ObjectInputStream oos = new ObjectInputStream(inputStream);
            request = (AuthenticationServerRequest) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        LOGGER.fine("Auth server || receive request from the client: " + request);
        return request;
    }

    private AuthenticationServerResponse makeResponse(AuthenticationServerRequest request) throws
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException,
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

//        LOGGER.fine("timeStamp = " + Arrays.toString(request.timeStampFromClient()));
//        LOGGER.fine("decrypted timeStamp = " + CoderUtils.decryptToLong(request.timeStampFromClient(), clientSharedKey));
//        LOGGER.fine("encrypted timeStamp = " + Arrays.toString(CoderUtils.encryptLong(
//                CoderUtils.decryptToLong(request.timeStampFromClient(), clientSharedKey),
//                clientSharedKey
//        )));
        return new AuthenticationServerResponse(
                CoderUtils.encryptLong(
                        CoderUtils.decryptToLong(request.timeStampFromClient(), clientSharedKey),
                        clientSharedKey
                )
        );
    }

    private void extractClientSharedKey(AuthenticationServerRequest request) throws IllegalBlockSizeException,
            BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
        clientId = CoderUtils.decryptToLong(request.clientIdFromGrantedServer(), grantedServerSharedKey);
        clientSharedKey = CoderUtils.decryptToKey(request.clientAndAuthServerSharesKeyFromGrantedServer(), grantedServerSharedKey);
    }

    public static void main(String[] args) {
        AuthenticationServer authenticationServer = new AuthenticationServer();
        authenticationServer.start();
    }
}
