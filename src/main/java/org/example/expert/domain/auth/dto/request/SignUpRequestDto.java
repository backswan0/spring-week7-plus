package org.example.expert.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String password;

    @NotBlank
    private final String userRole;

    public SignUpRequestDto(
        String email,
        String password,
        String userRole
    ) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }
}
