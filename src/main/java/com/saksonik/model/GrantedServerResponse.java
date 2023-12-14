package com.saksonik.model;

import java.io.Serializable;

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
}
