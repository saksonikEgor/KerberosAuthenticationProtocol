package com.saksonik.server;

import com.saksonik.model.AuthenticationServerRequest;
import com.saksonik.model.AuthenticationServerResponse;
import com.saksonik.properties.ApplicationProperties;
import com.saksonik.utils.CoderUtils;
import org.apache.logging.log4j.LogManager;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
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
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthenticationServer {
    private static final int PORT = ApplicationProperties.AUTHENTICATION_SERVER_PORT;
    private final long authServerId = ApplicationProperties.AUTH_SERVER_ID;
    private long clientId;
//    private final Cipher grantedServerSharedKey = ApplicationProperties.SHARED_KEY_BETWEEN_AUTH_AND_GRANTED_SERVERS;
private final Cipher grantedServerSharedKey = ApplicationProperties.getSharedKeyBetweenAuthAndGrantedServers();
    private Cipher clientSharedKey;
    private ServerSocket serverSocket;
    private static final java.util.logging.Logger LOGGER =  Logger.getLogger("com.something");
    public AuthenticationServer() throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
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
        LOGGER.info("Auth server || started");

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
            AuthenticationServerResponse response = makeResponse(request);

            try (ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream())) {
                oos.writeObject(response);
            }

            LOGGER.info("Auth server || sending response to the client: " + response);
        } catch (IOException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException |
                 NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthenticationServerRequest getRequest(InputStream inputStream) {
        AuthenticationServerRequest request = null;

        try (ObjectInputStream oos = new ObjectInputStream(inputStream)) {
            request = (AuthenticationServerRequest) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Auth server || receive request from the client: " + request);
        return request;
    }

    private AuthenticationServerResponse makeResponse(AuthenticationServerRequest request) throws
            IllegalBlockSizeException, BadPaddingException {
        return new AuthenticationServerResponse(
                CoderUtils.encryptLong(
                        CoderUtils.decryptToLong(request.timeStampFromClient(), clientSharedKey),
                        clientSharedKey
                )
        );
    }

    private void extractClientSharedKey(AuthenticationServerRequest request) throws IllegalBlockSizeException,
            BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        clientId = CoderUtils.decryptToLong(request.clientIdFromGrantedServer(), grantedServerSharedKey);
        clientSharedKey = CoderUtils.decryptToKey(request.clientAndAuthServerSharesKeyFromGrantedServer(), grantedServerSharedKey);
    }

    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        AuthenticationServer authenticationServer = new AuthenticationServer();
        authenticationServer.start();
    }
}