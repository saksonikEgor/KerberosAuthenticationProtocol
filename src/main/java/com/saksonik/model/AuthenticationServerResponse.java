package com.saksonik.model;

import java.io.Serializable;

public record AuthenticationServerResponse(
        byte[] timeStamp
//        String encodedTimeStamp
) implements Serializable {
}
