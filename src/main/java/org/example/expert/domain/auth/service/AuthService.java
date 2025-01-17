package org.example.expert.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.common.encoder.PasswordEncoder;
import org.example.expert.common.entity.User;
import org.example.expert.common.enums.UserRole;
import org.example.expert.common.exception.InvalidRequestException;
import org.example.expert.common.exception.mismatch.PasswordMismatchException;
import org.example.expert.common.exception.notfound.UserNotFoundException;
import org.example.expert.common.utils.JwtUtil;
import org.example.expert.domain.auth.dto.request.SignInRequestDto;
import org.example.expert.domain.auth.dto.request.SignUpRequestDto;
import org.example.expert.domain.auth.dto.response.SignInResponseDto;
import org.example.expert.domain.auth.dto.response.SignUpResponseDto;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto requestDto) {

        boolean isEmailRegistered = userRepository.existsByEmail(requestDto.getEmail());

        if (isEmailRegistered) {
            throw new InvalidRequestException("이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        UserRole userRole = UserRole.of(requestDto.getUserRole());

        User userToSave = new User(
            requestDto.getEmail(),
            encodedPassword,
            userRole,
            requestDto.getNickname()
        );

        User savedUser = userRepository.save(userToSave);

        String bearerToken = jwtUtil.createToken(
            savedUser.getId(),
            savedUser.getEmail(),
            userRole,
            savedUser.getNickname()
        );

        return new SignUpResponseDto(bearerToken);
    }

    @Transactional
    public SignInResponseDto signIn(SignInRequestDto requestDto) {

        User foundUser = userRepository.findByEmail(requestDto.getEmail())
            .orElseThrow(UserNotFoundException::new);

        boolean isPasswordMismatch = !passwordEncoder.matches(
            requestDto.getPassword(),
            foundUser.getPassword()
        );

        if (isPasswordMismatch) {
            throw new PasswordMismatchException();
        }

        String bearerToken = jwtUtil.createToken(
            foundUser.getId(),
            foundUser.getEmail(),
            foundUser.getUserRole(),
            foundUser.getNickname()
        );

        return new SignInResponseDto(bearerToken);
    }
}
