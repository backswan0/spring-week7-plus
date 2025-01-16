package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.config.PasswordEncoder;
import org.example.expert.common.entity.User;
import org.example.expert.common.exception.InvalidRequestException;
import org.example.expert.common.exception.mismatch.PasswordMismatchException;
import org.example.expert.common.exception.notfound.UserNotFoundException;
import org.example.expert.domain.user.dto.request.UserPasswordUpdateRequestDto;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponseDto getUserById(long userId) {
        User foundUser = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        return new UserResponseDto(foundUser);
    }

    @Transactional
    public void updatePassword(
        long userId,
        UserPasswordUpdateRequestDto requestDto
    ) {
        validateNewPassword(requestDto);

        User foundUser = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        boolean isSameAsCurrentPassword = passwordEncoder.matches(
            requestDto.getNewPassword(),
            foundUser.getPassword()
        );

        if (isSameAsCurrentPassword) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        boolean isPasswordMismatch = !passwordEncoder.matches(
            requestDto.getOldPassword(),
            foundUser.getPassword()
        );

        if (isPasswordMismatch) {
            throw new PasswordMismatchException();
        }

        foundUser.updatePassword(
            passwordEncoder.encode(requestDto.getNewPassword())
        );
    }

    private static void validateNewPassword(
        UserPasswordUpdateRequestDto requestDto
    ) {
        if (requestDto.getNewPassword().length() < 8 ||
            !requestDto.getNewPassword().matches(".*\\d.*") ||
            !requestDto.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }
}
