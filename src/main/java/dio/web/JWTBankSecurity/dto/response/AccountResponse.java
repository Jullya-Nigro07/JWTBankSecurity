package dio.web.JWTBankSecurity.dto.response;

import java.math.BigDecimal;

public record AccountResponse(Long accountId, BigDecimal balance) {
}
