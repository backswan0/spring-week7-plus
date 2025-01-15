package org.example.expert.domain.manager.controller;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.expert.common.annotation.Auth;
import org.example.expert.common.dto.AuthUserDto;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequestDto;
import org.example.expert.domain.manager.dto.response.ManagerResponseDto;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponseDto;
import org.example.expert.domain.manager.service.ManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @PostMapping("/todos/{todoId}/managers")
    public ResponseEntity<ManagerSaveResponseDto> saveManager(
        @Auth AuthUserDto authUserDto,
        @PathVariable("todoId") long todoId,
        @Valid @RequestBody ManagerSaveRequestDto requestDto
    ) {

        ManagerSaveResponseDto responseDto = managerService.saveManager(
            authUserDto,
            todoId,
            requestDto
        );

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/todos/{todoId}/managers")
    public ResponseEntity<List<ManagerResponseDto>> readAllManagers(
        @PathVariable("todoId") long todoId
    ) {
        List<ManagerResponseDto> responseDtoList = new ArrayList<>();

        responseDtoList = managerService.getAllManagers(todoId);

        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/todos/{todoId}/managers/{managerId}")
    public ResponseEntity<Void> deleteManager(
        @Auth AuthUserDto authUser,
        @PathVariable("todoId") long todoId,
        @PathVariable("managerId") long managerId
    ) {
        managerService.deleteManager(
            authUser,
            todoId,
            managerId
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
