package dio.web.JWTBankSecurity.dto.response;

import org.hibernate.dialect.unique.CreateTableUniqueDelegate;

public record UserResponse(String name, String email) {
}
