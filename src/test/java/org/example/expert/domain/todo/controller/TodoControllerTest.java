package org.example.expert.domain.todo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.expert.domain.common.dto.AuthUserDto;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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
        long todoId = 1L;
        String title = "title";
        AuthUserDto authUser = new AuthUserDto(1L, "email", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        UserResponseDto userResponseDto = new UserResponseDto(user.getId(), user.getEmail());
//        TodoResponseDto response = new TodoResponseDto(
//                todoId,
//                title,
//                "contents",
//                "Sunny",
//                userResponse,
//                LocalDateTime.now(),
//                LocalDateTime.now()
//        );

        // when
//        when(todoService.getTodo(todoId)).thenReturn(response);
        // todo

        // then
        mockMvc.perform(get("/todos/{todoId}", todoId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(todoId))
            .andExpect(jsonPath("$.title").value(title));
    }

    @Test
    void todo_단건_조회_시_todo가_존재하지_않아_예외가_발생한다() throws Exception {
        // given
        long todoId = 1L;

        // when
        when(todoService.getTodoById(todoId))
            .thenThrow(new InvalidRequestException("Todo not found"));

        // then
        mockMvc.perform(get("/todos/{todoId}", todoId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
            .andExpect(jsonPath("$.message").value("Todo not found"));
    }
}
