package org.example.expert.domain.manager.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.common.dto.AuthUserDto;
import org.example.expert.common.entity.Manager;
import org.example.expert.common.entity.Todo;
import org.example.expert.common.entity.User;
import org.example.expert.common.exception.InvalidRequestException;
import org.example.expert.common.exception.mismatch.ManagerMismatchException;
import org.example.expert.common.exception.mismatch.UserMismatchException;
import org.example.expert.common.exception.notfound.ManagerNotFoundException;
import org.example.expert.common.exception.notfound.TodoNotFoundException;
import org.example.expert.common.exception.notfound.UserNotFoundException;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequestDto;
import org.example.expert.domain.manager.dto.response.ManagerResponseDto;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponseDto;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final TodoRepository todoRepository;

    @Transactional
    public ManagerSaveResponseDto saveManager(
        AuthUserDto authUser,
        long todoId,
        ManagerSaveRequestDto requestDto
    ) {
        User user = User.fromAuthUser(authUser);

        Todo foundTodo = todoRepository.findById(todoId)
            .orElseThrow(TodoNotFoundException::new);

        boolean isUserMismatch = foundTodo.getUser() == null
            || !ObjectUtils.nullSafeEquals(
            user.getId(),
            foundTodo.getUser().getId()
        );

        if (isUserMismatch) {
            throw new UserMismatchException();
        }

        User managerToRegister = userRepository.findById(requestDto.getManagerUserId())
            .orElseThrow(UserNotFoundException::new);

        if (ObjectUtils.nullSafeEquals(user.getId(), managerToRegister.getId())) {
            throw new InvalidRequestException("일정 작성자는 본인을 담당자로 등록할 수 없습니다.");
        }

        Manager ManagerUserToSave = new Manager(
            managerToRegister,
            foundTodo
        );

        Manager savedManagerUser = managerRepository.save(ManagerUserToSave);

        return new ManagerSaveResponseDto(
            savedManagerUser,
            new UserResponseDto(managerToRegister)
        );
    }

    @Transactional(readOnly = true)
    public List<ManagerResponseDto> getAllManagers(long todoId) {
        Todo foundTodo = todoRepository.findById(todoId)
            .orElseThrow(TodoNotFoundException::new);

        List<Manager> managerList = new ArrayList<>();

        managerList = managerRepository.findByTodoIdWithUser(foundTodo.getId());

        List<ManagerResponseDto> dtoList = new ArrayList<>();

        dtoList = managerList.stream()
            .map(manager -> new ManagerResponseDto(
                    manager,
                    new UserResponseDto(manager.getUser())
                )
            ).toList();

        return dtoList;
    }

    @Transactional
    public void deleteManager(
        AuthUserDto authUser,
        long todoId,
        long managerId
    ) {
        User user = User.fromAuthUser(authUser);

        Todo foundTodo = todoRepository.findById(todoId)
            .orElseThrow(TodoNotFoundException::new);

        boolean isUserMismatch = foundTodo.getUser() == null
            || !ObjectUtils.nullSafeEquals(
            user.getId(),
            foundTodo.getUser().getId()
        );

        if (isUserMismatch) {
            throw new UserMismatchException();
        }

        Manager foundManager = managerRepository.findById(managerId)
            .orElseThrow(ManagerNotFoundException::new);

        boolean isManagerMismatch = !ObjectUtils.nullSafeEquals(
            foundTodo.getId(),
            foundManager.getTodo().getId());

        if (isManagerMismatch) {
            throw new ManagerMismatchException();
        }

        managerRepository.delete(foundManager);
    }
}
