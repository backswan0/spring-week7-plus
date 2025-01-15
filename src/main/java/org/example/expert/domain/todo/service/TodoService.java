package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUserDto;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequestDto;
import org.example.expert.domain.todo.dto.response.TodoResponseDto;
import org.example.expert.domain.todo.dto.response.TodoSaveResponseDto;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

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
            new UserResponseDto(user));
    }

    @Transactional(readOnly = true)
    public Page<TodoResponseDto> getAllTodos(
        String weather,
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

        Page<Todo> todoPage = todoRepository.findAllByOrderByUpdatedAtDesc(pageable);

        return todoPage.map(
            todo -> new TodoResponseDto(
                todo,
                new UserResponseDto(todo.getUser())
            )
        );
    }

    @Transactional(readOnly = true)
    public TodoResponseDto getTodoById(long todoId) {
        Todo foundTodo = todoRepository.findByIdWithUser(todoId)
            .orElseThrow(
                () -> new InvalidRequestException("Todo not found")
            );

        return new TodoResponseDto(
            foundTodo,
            new UserResponseDto(foundTodo.getUser())
        );
    }
}
