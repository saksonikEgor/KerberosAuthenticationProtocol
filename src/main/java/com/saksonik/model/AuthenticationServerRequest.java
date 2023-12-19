package com.saksonik.model;

import java.io.Serializable;
import java.util.Arrays;

public record AuthenticationServerRequest(
        byte[] clientIdFromClient,
        byte[] timeStampFromClient,
        byte[] validPeriodFromClient,
        byte[] clientAndAuthServerSharesKeyFromGrantedServer,
        byte[] clientIdFromGrantedServer,
        byte[] validPeriodFromGrantedServer
) implements Serializable {
    @Override
    public String toString() {
        return "{"
                + "clientIdFromClient=" + Arrays.toString(clientIdFromClient) + ", \n"
                + "timeStampFromClient=" + Arrays.toString(timeStampFromClient) + ", \n"
                + "validPeriodFromClient=" + Arrays.toString(validPeriodFromClient) + ", \n"
                + "clientAndAuthServerSharesKeyFromGrantedServer=" + Arrays.toString(clientAndAuthServerSharesKeyFromGrantedServer) + ", \n"
                + "clientIdFromGrantedServer=" + Arrays.toString(clientIdFromGrantedServer) + ", \n"
                + "validPeriodFromGrantedServer=" + Arrays.toString(validPeriodFromGrantedServer) + "}";
    }
}
