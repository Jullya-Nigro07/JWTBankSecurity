package dio.web.JWTBankSecurity.dto.erro;

import java.util.Map;

public record ErroRequest(String message, int status, Map<String, String> error) {
}

