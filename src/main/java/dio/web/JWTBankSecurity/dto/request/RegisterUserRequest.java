package dio.web.JWTBankSecurity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(@NotEmpty(message = "Name is required")String name, @Email @NotEmpty(message = "E-mail is required")String email,@Size(min = 7, max = 15) @NotEmpty(message = "Password is required")String password) {
}
