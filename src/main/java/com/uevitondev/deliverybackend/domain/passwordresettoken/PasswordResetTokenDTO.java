package com.uevitondev.deliverybackend.domain.passwordresettoken;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PasswordResetTokenDTO(
        @NotEmpty(message = "token must not be empty")
        @Size(min = 2, max = 25, message = "token length min  and max  characters")
        String token,
        @NotEmpty(message = "new password must not be empty")
        @Size(min = 6, max = 16, message = "password length min 6 and max 16 characters")
        String newPassword
) {
}
