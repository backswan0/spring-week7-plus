package org.example.expert.domain.comment.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.comment.dto.request.CommentSaveRequestDto;
import org.example.expert.domain.comment.dto.response.CommentResponseDto;
import org.example.expert.domain.comment.dto.response.CommentSaveResponseDto;
import org.example.expert.common.entity.Comment;
import org.example.expert.domain.comment.repository.CommentRepository;
import org.example.expert.common.dto.AuthUserDto;
import org.example.expert.common.exception.InvalidRequestException;
import org.example.expert.common.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.common.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final TodoRepository todoRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentSaveResponseDto saveComment(AuthUserDto authUser, long todoId,
        CommentSaveRequestDto commentSaveRequestDto) {
        User user = User.fromAuthUser(authUser);
        Todo todo = todoRepository.findById(todoId).orElseThrow(() ->
            new InvalidRequestException("Todo not found"));

        Comment newComment = new Comment(
            commentSaveRequestDto.getContents(),
            user,
            todo
        );

        Comment savedComment = commentRepository.save(newComment);

        return new CommentSaveResponseDto(
            savedComment,
            new UserResponseDto(user)
        );
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComments(long todoId) {
        List<Comment> commentList = new ArrayList<>();

        commentList = commentRepository.findByTodoIdWithUser(todoId);

        List<CommentResponseDto> dtoList = new ArrayList<>();

        dtoList = commentList.stream()
            .map(comment -> new CommentResponseDto(
                    comment,
                    new UserResponseDto(comment.getUser())
                )
            ).toList();

        return dtoList;
    }
}
