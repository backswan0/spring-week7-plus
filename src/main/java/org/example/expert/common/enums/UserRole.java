package org.example.expert.common.enums;

import java.util.Arrays;
import org.example.expert.common.exception.InvalidRequestException;

public enum UserRole {
    ADMIN, USER;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
            .filter(r -> r.name().equalsIgnoreCase(role))
            .findFirst()
            .orElseThrow(() -> new InvalidRequestException("유효하지 않은 UerRole"));
    }
}
