package dio.web.JWTBankSecurity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(@NotBlank String name, @NotBlank @Email String email, @NotBlank @Size(min = 7, max = 15) String password) {
}