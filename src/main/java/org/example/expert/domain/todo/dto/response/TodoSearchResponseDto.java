package org.example.expert.domain.todo.dto.response;

import lombok.Getter;
import org.example.expert.common.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponseDto;

@Getter
public class TodoSearchResponseDto {

    private final String title;
    private final UserResponseDto user;

    public TodoSearchResponseDto(
        Todo todo,
        UserResponseDto user
    ) {
        this.title = todo.getTitle();
        this.user = user;
    }
}
