package org.example.expert.domain.comment.dto.response;

import lombok.Getter;
import org.example.expert.common.entity.Comment;
import org.example.expert.domain.user.dto.response.UserResponseDto;

@Getter
public class CommentResponseDto {

    private final Long id;
    private final String contents;
    private final UserResponseDto user;

    public CommentResponseDto(
        Comment comment,
        UserResponseDto user
    ) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.user = user;
    }
}
