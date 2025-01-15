package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.config.PasswordEncoder;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserPasswordUpdateRequestDto;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.domain.user.entity.User;
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
            .orElseThrow(() -> new InvalidRequestException("User not found"));

        return new UserResponseDto(foundUser);
    }

    @Transactional
    public void updatePassword(
        long userId,
        UserPasswordUpdateRequestDto requestDto
    ) {
        validateNewPassword(requestDto);

        User foundUser = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(requestDto.getNewPassword(),
            foundUser.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(requestDto.getOldPassword(),
            foundUser.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        foundUser.updatePassword(
            passwordEncoder.encode(requestDto.getNewPassword()));
    }

    private static void validateNewPassword(
        UserPasswordUpdateRequestDto userPasswordUpdateRequestDto) {
        if (userPasswordUpdateRequestDto.getNewPassword().length() < 8 ||
            !userPasswordUpdateRequestDto.getNewPassword().matches(".*\\d.*") ||
            !userPasswordUpdateRequestDto.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }
}
