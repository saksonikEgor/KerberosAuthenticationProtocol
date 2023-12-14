package com.saksonik.model;

import java.io.Serializable;
import java.util.Arrays;

public record GrantedServerResponse(
        byte[] clientAndAuthServerSharedKeyForClient,
        byte[] clientRandomNumberForClient,
        byte[] validPeriodForClient,
        byte[] authServerIdForClient,
        byte[] clientAndAuthServerSharedKeyForServer,
        byte[] clientIdForServer,
        byte[] validPeriodForServer
//        String encodedClientPart,
//        String encodedAuthServerPart
) implements Serializable {
    @Override
    public String toString() {
        return "{"
                + "clientAndAuthServerSharedKeyForClient=" + Arrays.toString(clientAndAuthServerSharedKeyForClient) + ", "
                + "clientRandomNumberForClient=" + Arrays.toString(clientRandomNumberForClient) + ", "
                + "validPeriodForClient=" + Arrays.toString(validPeriodForClient) + ", "
                + "authServerIdForClient=" + Arrays.toString(authServerIdForClient) + ", "
                + "clientAndAuthServerSharedKeyForServer=" + Arrays.toString(clientAndAuthServerSharedKeyForServer) + ", "
                + "clientIdForServer=" + Arrays.toString(clientIdForServer) + ", "
                + "validPeriodForServer=" + Arrays.toString(validPeriodForServer) + "}";
    }
}
