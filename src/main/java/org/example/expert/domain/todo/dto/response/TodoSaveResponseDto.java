package org.example.expert.domain.todo.dto.response;

import lombok.Getter;
import org.example.expert.common.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponseDto;

@Getter
public class TodoSaveResponseDto {

    private final Long id;
    private final String title;
    private final String contents;
    private final String weather;
    private final UserResponseDto user;

    public TodoSaveResponseDto(
        Todo todo,
        UserResponseDto user
    ) {
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.contents = todo.getContents();
        this.weather = todo.getWeather();
        this.user = user;
    }
}
