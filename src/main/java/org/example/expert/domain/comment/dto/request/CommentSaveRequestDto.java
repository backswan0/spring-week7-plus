package org.example.expert.domain.comment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentSaveRequestDto(@NotBlank String contents) {

    public CommentSaveRequestDto(String contents) {
        this.contents = contents;

    }
}
