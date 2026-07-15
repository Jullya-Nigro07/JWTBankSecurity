package dio.web.JWTBankSecurity.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dio.web.JWTBankSecurity.enums.TipoTransaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(Long id, TipoTransaction type, BigDecimal amount, @JsonFormat(pattern = "yyyy-MM-dd | HH:mm:ss")LocalDateTime dateTime) {
}