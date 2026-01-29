package dio.web.JWTBankSecurity.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(Long id, String type, BigDecimal amount, @JsonFormat(pattern = "yyyy-MM-dd | HH:mm:ss")LocalDateTime time) {
}
