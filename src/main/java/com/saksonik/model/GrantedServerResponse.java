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
) implements Serializable {
    @Override
    public String toString() {
        return "{"
                + "clientAndAuthServerSharedKeyForClient=" + Arrays.toString(clientAndAuthServerSharedKeyForClient) + ", \n"
                + "clientRandomNumberForClient=" + Arrays.toString(clientRandomNumberForClient) + ", \n"
                + "validPeriodForClient=" + Arrays.toString(validPeriodForClient) + ", \n"
                + "authServerIdForClient=" + Arrays.toString(authServerIdForClient) + ", \n"
                + "clientAndAuthServerSharedKeyForServer=" + Arrays.toString(clientAndAuthServerSharedKeyForServer) + ", \n"
                + "clientIdForServer=" + Arrays.toString(clientIdForServer) + ", \n"
                + "validPeriodForServer=" + Arrays.toString(validPeriodForServer) + "}";
    }
}
