package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserPasswordUpdateRequestDto {

    @NotBlank
    private final String oldPassword;
    @NotBlank
    private final String newPassword;

    public UserPasswordUpdateRequestDto(
        String oldPassword,
        String newPassword
    ) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
