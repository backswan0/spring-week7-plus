package org.example.expert.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TodoSaveRequestDto {

    @NotBlank
    private final String title;
    @NotBlank
    private final String contents;

    public TodoSaveRequestDto(
        String title,
        String contents
    ) {
        this.title = title;
        this.contents = contents;
    }
}