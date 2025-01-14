package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserChangePasswordRequestDto {

    @NotBlank
    private final String oldPassword;
    @NotBlank
    private final String newPassword;

    public UserChangePasswordRequestDto(
        String oldPassword,
        String newPassword
    ) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}
