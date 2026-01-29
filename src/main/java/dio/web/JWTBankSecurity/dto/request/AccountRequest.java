package dio.web.JWTBankSecurity.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record AccountRequest (@NotNull @Positive BigDecimal value){
}
