package org.example.expert.domain.todo.service;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.client.WeatherClient;
import org.example.expert.common.dto.AuthUserDto;
import org.example.expert.common.entity.Todo;
import org.example.expert.common.entity.User;
import org.example.expert.common.exception.notfound.TodoNotFoundException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequestDto;
import org.example.expert.domain.todo.dto.request.TodoSearchRequestDto;
import org.example.expert.domain.todo.dto.response.TodoResponseDto;
import org.example.expert.domain.todo.dto.response.TodoSaveResponseDto;
import org.example.expert.domain.todo.dto.response.TodoSearchResponseDto;
import org.example.expert.domain.todo.repository.TodoQueryRepository;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;
    private final TodoQueryRepository todoQueryRepository;

    @Transactional
    public TodoSaveResponseDto saveTodo(
        AuthUserDto authUserDto,
        TodoSaveRequestDto requestDto
    ) {
        User user = User.fromAuthUser(authUserDto);

        String weather = weatherClient.getTodayWeather();

        Todo TodoToSave = new Todo(
            requestDto.getTitle(),
            requestDto.getContents(),
            weather,
            user
        );
        Todo savedTodo = todoRepository.save(TodoToSave);

        return new TodoSaveResponseDto(
            savedTodo,
            new UserResponseDto(savedTodo.getUser()));
    }

    @Transactional(readOnly = true)
    public Page<TodoResponseDto> getAllTodos(
        String weather,
        LocalDateTime startsAt,
        LocalDateTime endsAt,
        int page,
        int size
    ) {
        Pageable pageable = PageRequest.of(
            Math.max(page - 1, 0),
            size
        );

        if (weather != null) {
            Page<Todo> todoPage = todoRepository.findAllByWeather(
                weather,
                pageable
            );

            return todoPage.map(
                todo -> new TodoResponseDto(
                    todo,
                    new UserResponseDto(todo.getUser())
                )
            );
        }

        if (startsAt != null || endsAt != null) {
            Page<Todo> todoPage = todoRepository.findAllByUpdatedAtBetween(
                startsAt,
                endsAt,
                pageable
            );

            return todoPage.map(
                todo -> new TodoResponseDto(
                    todo,
                    new UserResponseDto(todo.getUser())
                )
            );
        }

        Page<Todo> todoPage = todoRepository.findAllByOrderByUpdatedAtDesc(pageable);

        return todoPage.map(
            todo -> new TodoResponseDto(
                todo,
                new UserResponseDto(todo.getUser())
            )
        );
    }

    @Transactional(readOnly = true)
    public Page<TodoSearchResponseDto> getTodoByConditions(
        TodoSearchRequestDto todoDto,
        int page,
        int size
    ) {
        log.info("페이지네이션 시작");

        Pageable pageable = PageRequest.of(
            Math.max(page - 1, 0),
            size
        );

        log.info("페이지네이션 종료 및 일정 목록 조회 처리");

        return todoQueryRepository.search(
            todoDto,
            pageable
        );
    }

    @Transactional(readOnly = true)
    public TodoResponseDto getTodo(long todoId) {
        Todo foundTodo = todoQueryRepository.findByIdWithUser(todoId)
            .orElseThrow(TodoNotFoundException::new);

        return new TodoResponseDto(
            foundTodo,
            new UserResponseDto(foundTodo.getUser())
        );
    }
}