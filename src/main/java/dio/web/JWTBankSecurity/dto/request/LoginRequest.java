package dio.web.JWTBankSecurity.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty(message = "Email é obrigatório") String email, @NotEmpty(message = "Senha é obrigatória") String password) {
}
