package dio.web.JWTBankSecurity.dto.erro;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ErroBalanceInsufficient(String message, int status, @JsonFormat(pattern = "yyyy-MM-dd | HH:mm:ss") LocalDateTime dateTime) {
}
