package org.example.expert.domain.manager.dto.response;

import lombok.Getter;
import org.example.expert.common.entity.Manager;
import org.example.expert.domain.user.dto.response.UserResponseDto;

@Getter
public class ManagerResponseDto {

    private final Long id;
    private final UserResponseDto user;

    public ManagerResponseDto(
        Manager manager,
        UserResponseDto user
    ) {
        this.id = manager.getId();
        this.user = user;
    }
}
