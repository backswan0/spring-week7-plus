package org.example.expert.domain.todo.dto.request;

import lombok.Getter;

@Getter
public class TodoDto {
    private final String title;

    public TodoDto(String title) {
        this.title = title;
    }
}