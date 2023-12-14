package com.saksonik.model;

import java.io.Serializable;
import java.util.Arrays;

public record GrantedServerRequest(
        long clientId,
        long authServerId,
        int randomNumber
) implements Serializable {
}
