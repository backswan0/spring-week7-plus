package org.example.expert.domain.manager.dto.response;

import lombok.Getter;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.user.dto.response.UserResponseDto;

@Getter
public class ManagerSaveResponseDto {

    private final Long id;
    private final UserResponseDto user;

    public ManagerSaveResponseDto(
        Manager manager,
        UserResponseDto user
    ) {
        this.id = manager.getId();
        this.user = user;
    }
}
