package org.example.expert.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.annotation.Auth;
import org.example.expert.common.dto.AuthUserDto;
import org.example.expert.domain.user.dto.request.UserPasswordUpdateRequestDto;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDto> getUserById(
        @PathVariable("userId") long userId
    ) {
        UserResponseDto responseDto = userService.getUserById(userId);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<Void> changePassword(
        @Auth AuthUserDto authUser,
        @RequestBody UserPasswordUpdateRequestDto requestDto
    ) {

        userService.updatePassword(
            authUser.getId(),
            requestDto
        );

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
