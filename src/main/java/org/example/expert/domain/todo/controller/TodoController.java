package org.example.expert.domain.todo.controller;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.common.annotation.Auth;
import org.example.expert.common.dto.AuthUserDto;
import org.example.expert.domain.todo.dto.request.TodoSaveRequestDto;
import org.example.expert.domain.todo.dto.response.TodoResponseDto;
import org.example.expert.domain.todo.dto.response.TodoSaveResponseDto;
import org.example.expert.domain.todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<TodoSaveResponseDto> saveTodo(
        @Auth AuthUserDto authUserDto,
        @Valid @RequestBody TodoSaveRequestDto requestDto
    ) {
        TodoSaveResponseDto responseDto = todoService.saveTodo(
            authUserDto,
            requestDto
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<TodoResponseDto>> getAllTodos(
        @RequestParam(required = false) String weather,
        @RequestParam(required = false) LocalDateTime startsAt,
        @RequestParam(required = false) LocalDateTime endsAt,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Page<TodoResponseDto> todoResponseDtoPage = todoService.getAllTodos(
            weather,
            startsAt,
            endsAt,
            page,
            size
        );

        return new ResponseEntity<>(todoResponseDtoPage, HttpStatus.OK);
    }

    @GetMapping("/todos/list")
    public ResponseEntity<Page<TodoResponseDto>> getTodoByConditions(
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        log.info("일정 제목으로 조회 시작");

        Page<TodoResponseDto> responseDtoPage = todoService.getTodoByConditions(
            search,
            page,
            size
        );

        log.info("일정 제목으로 조회 종료");

        return new ResponseEntity<>(responseDtoPage, HttpStatus.OK);
    }

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponseDto> getTodoById(
        @PathVariable("todoId") long todoId
    ) {
        TodoResponseDto responseDto = todoService.getTodo(todoId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}