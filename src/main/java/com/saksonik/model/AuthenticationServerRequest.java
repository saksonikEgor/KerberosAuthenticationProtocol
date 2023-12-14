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
//        String encodedAuthServerPart,
//        String encodedClientPart
) implements Serializable {
    @Override
    public String toString() {
        return "{"
                + "clientIdFromClient=" + Arrays.toString(clientIdFromClient) + ", "
                + "timeStampFromClient=" + Arrays.toString(timeStampFromClient) + ", "
                + "validPeriodFromClient=" + Arrays.toString(validPeriodFromClient) + ", "
                + "clientAndAuthServerSharesKeyFromGrantedServer=" + Arrays.toString(clientAndAuthServerSharesKeyFromGrantedServer) + ", "
                + "clientIdFromGrantedServer=" + Arrays.toString(clientIdFromGrantedServer) + ", "
                + "validPeriodFromGrantedServer=" + Arrays.toString(validPeriodFromGrantedServer) + "}";
    }
}
