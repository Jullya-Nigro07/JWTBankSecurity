package dio.web.JWTBankSecurity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest(@NotEmpty(message = "Nome é obrigatório")String name, @Email @NotEmpty(message = "Email é obrigatório")String email,@Size(min = 8) @NotEmpty(message = "Senha é obrigatória")String password) {
}
