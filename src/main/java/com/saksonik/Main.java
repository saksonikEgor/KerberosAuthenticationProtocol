package com.saksonik;

import com.saksonik.client.Client;
import com.saksonik.server.AuthenticationServer;
import com.saksonik.server.GrantedServer;

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
