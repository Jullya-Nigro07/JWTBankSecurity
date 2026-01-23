package dio.web.JWTBankSecurity.config;

import lombok.Builder;

@Builder
public record JWTUserData(Long userId, String email) {
}
