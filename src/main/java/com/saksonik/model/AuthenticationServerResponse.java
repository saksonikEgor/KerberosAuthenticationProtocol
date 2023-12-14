package com.saksonik.model;

import java.io.Serializable;
import java.util.Arrays;

public record AuthenticationServerResponse(
        byte[] timeStamp
) implements Serializable {
    @Override
    public String toString() {
        return "{"
                + "timeStamp=" + Arrays.toString(timeStamp) + "}";
    }
}
