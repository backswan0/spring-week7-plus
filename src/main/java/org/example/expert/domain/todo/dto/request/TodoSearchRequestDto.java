package org.example.expert.domain.todo.dto.request;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class TodoSearchRequestDto {

    private final String title;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private final String nickname;

    public TodoSearchRequestDto(
        String title,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        String nickname
    ) {
        this.title = title;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.nickname = nickname;
    }
}