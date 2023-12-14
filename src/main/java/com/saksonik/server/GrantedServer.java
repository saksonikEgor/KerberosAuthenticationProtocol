package com.saksonik.server;

import com.saksonik.model.GrantedServerRequest;
import com.saksonik.model.GrantedServerResponse;
import com.saksonik.properties.ApplicationProperties;
import com.saksonik.utils.CoderUtils;
import com.saksonik.utils.RandomUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GrantedServer {
    private static final int PORT = ApplicationProperties.GRANTED_SERVER_PORT;
    private final String clientSharedKey = ApplicationProperties.SHARED_KEY_BETWEEN_CLIENT_AND_GRANTED_SERVER;
    private final String authServerSharedKey = ApplicationProperties.SHARED_KEY_BETWEEN_AUTH_AND_GRANTED_SERVERS;
    private ServerSocket serverSocket;
    private static final java.util.logging.Logger LOGGER = Logger.getLogger("com.something");
    private final int validPeriod = ApplicationProperties.VALID_PERIOD;

    public GrantedServer() {
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
        LOGGER.fine("Granted server || started");

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
            GrantedServerRequest request = getRequest(client.getInputStream());
            GrantedServerResponse response = makeResponse(request);

            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(response);


            LOGGER.fine("Granted server || sending response to the client: " + response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GrantedServerRequest getRequest(InputStream inputStream) {
        GrantedServerRequest request = null;

        try {
            ObjectInputStream oos = new ObjectInputStream(inputStream);
            request = (GrantedServerRequest) oos.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        LOGGER.fine("Granted server || receive request from the client: " + request);
        return request;
    }

    private GrantedServerResponse makeResponse(GrantedServerRequest request) throws Exception {
        String randomKey = RandomUtils.getRandomString();

        LOGGER.fine("Granted server || generate client and auth server shared key: " + randomKey);

        return new GrantedServerResponse(
                CoderUtils.encryptString(randomKey, clientSharedKey),
                CoderUtils.encryptInteger(request.randomNumber(), clientSharedKey),
                CoderUtils.encryptInteger(validPeriod, clientSharedKey),
                CoderUtils.encryptLong(request.clientId(), clientSharedKey),
                CoderUtils.encryptString(randomKey, authServerSharedKey),
                CoderUtils.encryptLong(request.clientId(), authServerSharedKey),
                CoderUtils.encryptInteger(validPeriod, authServerSharedKey)
        );
    }

    public static void main(String[] args) {
        GrantedServer grantedServer = new GrantedServer();
        grantedServer.start();
    }
}
