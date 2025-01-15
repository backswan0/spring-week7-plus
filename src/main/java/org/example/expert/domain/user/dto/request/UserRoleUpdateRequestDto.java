package org.example.expert.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserRoleUpdateRequestDto {

    private final String userRole;

    public UserRoleUpdateRequestDto(String userRole) {
        this.userRole = userRole;
    }
}
