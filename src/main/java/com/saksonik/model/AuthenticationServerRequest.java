package com.saksonik.model;

import java.io.Serializable;

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
}
