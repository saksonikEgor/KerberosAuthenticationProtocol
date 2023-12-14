package com.saksonik.model;

import java.io.Serializable;

public record GrantedServerRequest(
        long clientId,
        long authServerId,
        int randomNumber
) implements Serializable {
}
