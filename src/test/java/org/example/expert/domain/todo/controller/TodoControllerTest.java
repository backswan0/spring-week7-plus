package org.example.expert.domain.todo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import org.example.expert.SharedData;
import org.example.expert.common.exception.notfound.TodoNotFoundException;
import org.example.expert.domain.todo.dto.response.TodoResponseDto;
import org.example.expert.domain.todo.service.TodoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @Test
    void todo_단건_조회에_성공한다() throws Exception {
        // given
        TodoResponseDto responseDto = new TodoResponseDto(
            SharedData.TODO,
            SharedData.USER_RESPONSE_DTO
        );

        long todoId = responseDto.getId();
        String title = responseDto.getTitle();
        String contents = responseDto.getContents();
        String weather = responseDto.getWeather();
        long userId = responseDto.getUser().getId();
        String email = responseDto.getUser().getEmail();
        String createdAt = responseDto.getCreatedAt().toString();
        String updatedAt = responseDto.getUpdatedAt().toString();

        // when
        when(todoService.getTodoById(todoId)).thenReturn(responseDto);

        // then
        mockMvc.perform(get("/todos/{todoId}", todoId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(todoId))
            .andExpect(jsonPath("$.title").value(title))
            .andExpect(jsonPath("$.title").value(title))
            .andExpect(jsonPath("$.contents").value(contents))
            .andExpect(jsonPath("$.weather").value(weather))
            .andExpect(jsonPath("$.user.id").value(userId))
            .andExpect(jsonPath("$.user.email").value(email))
            .andExpect(jsonPath("$.createdAt").value(createdAt))
            .andExpect(jsonPath("$.updatedAt").value(updatedAt));
    }

    @Test
    void todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() throws Exception {
        // given
        long todoId = 2L;

        // when
        when(todoService.getTodoById(todoId))
            .thenThrow(new TodoNotFoundException());

        // then
        mockMvc.perform(get("/todos/{todoId}", todoId))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorCode").value("ERRNF02"))
            .andExpect(jsonPath("$.errorMessage").value("Todo is not found."));
    }
}
