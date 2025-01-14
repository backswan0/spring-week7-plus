package org.example.expert.domain.user.dto.request;

import lombok.Getter;

@Getter
public class UserRoleChangeRequestDto {

    private final String userRole;

    public UserRoleChangeRequestDto(String userRole) {
        this.userRole = userRole;
    }
}
