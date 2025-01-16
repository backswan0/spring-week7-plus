package org.example.expert.domain.comment.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.dto.request.CommentSaveRequestDto;
import org.example.expert.domain.comment.dto.response.CommentResponseDto;
import org.example.expert.domain.comment.dto.response.CommentSaveResponseDto;
import org.example.expert.domain.comment.service.CommentService;
import org.example.expert.common.annotation.Auth;
import org.example.expert.common.dto.AuthUserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/todos/{todoId}/comments")
    public ResponseEntity<CommentSaveResponseDto> saveComment(
        @Auth AuthUserDto authUser,
        @PathVariable("todoId") long todoId,
        @Valid @RequestBody CommentSaveRequestDto requestDto
    ) {
        CommentSaveResponseDto responseDto = commentService.saveComment(
            authUser,
            todoId,
            requestDto
        );

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/todos/{todoId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getAllComments(
        @PathVariable("todoId") long todoId
    ) {
        List<CommentResponseDto> responseDtoList = new ArrayList<>();

        responseDtoList = commentService.getAllComments(todoId);

        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }
}
