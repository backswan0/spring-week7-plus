package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleUpdateRequestDto;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    @Transactional
    public void updateUserRole(
        long userId,
        UserRoleUpdateRequestDto requestDto
    ) {
        User foundUser = userRepository.findById(userId)
            .orElseThrow(() -> new InvalidRequestException("User not found"));

        foundUser.updateRole(UserRole.of(requestDto.getUserRole()));
    }
}