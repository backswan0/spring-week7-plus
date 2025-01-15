package org.example.expert;

import java.time.LocalDateTime;
import org.example.expert.common.dto.AuthUserDto;
import org.example.expert.common.entity.Todo;
import org.example.expert.domain.user.dto.response.UserResponseDto;
import org.example.expert.common.entity.User;
import org.example.expert.common.enums.UserRole;
import org.springframework.test.util.ReflectionTestUtils;

public class SharedData {

    public static final AuthUserDto AUTH_USER_DTO = new AuthUserDto(
        1L,
        "user1@test.com",
        UserRole.USER,
        "사용자1"
    );
    public static final User USER = User.fromAuthUser(AUTH_USER_DTO);
    public static final UserResponseDto USER_RESPONSE_DTO = new UserResponseDto(USER);
    public static final Todo TODO = new Todo(
        "일정 제목",
        "일정 내용",
        "날씨",
        USER
    );

    static {
        ReflectionTestUtils.setField(
            TODO,
            "id",
            1L
        );

        ReflectionTestUtils.setField(
            TODO,
            "createdAt",
            LocalDateTime.of(
                2025,
                1,
                15,
                10,
                0
            )
        );

        ReflectionTestUtils.setField(
            TODO,
            "modifiedAt",
            LocalDateTime.of(2025,
                1,
                15,
                10,
                0
            )
        );
    }
}
