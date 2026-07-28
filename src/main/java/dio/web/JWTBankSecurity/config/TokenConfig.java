package dio.web.JWTBankSecurity.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dio.web.JWTBankSecurity.entity.User;
import dio.web.JWTBankSecurity.exception.TokenInvalidException;
import dio.web.JWTBankSecurity.repository.UserRepository;
import org.springframework.stereotype.Component;
import java.time.Instant;

@Component
public class TokenConfig {
    private final UserRepository userRepository;
    public TokenConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final String secret = "secret-chave-de-teste-para-app-jwt-config-jullya";
    Algorithm algorithm = Algorithm.HMAC256(secret);

    public String generateToken(User user) {
        return JWT.create()
                .withClaim("userId", user.getId())
                .withClaim("tokenVersion", user.getTokenVersion())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public JWTUserData validateToken(String token) throws TokenInvalidException {
        try {
            DecodedJWT decode =
                    JWT.require(algorithm).build().verify(token);

            String email = decode.getSubject();
            Integer tokenVersionToken =
                    decode.getClaim("tokenVersion").asInt();

            User user = userRepository.findUserByEmail(email).orElseThrow(() -> new TokenInvalidException("| ERROR: Token user not found! |"));

            if (!tokenVersionToken.equals(user.getTokenVersion())) {
                throw new TokenInvalidException("| ERROR: Token revoked |");
            }

            return JWTUserData.builder()
                            .userId(user.getId())
                            .email(email)
                            .build();

        } catch (JWTVerificationException ex) {
            throw new TokenInvalidException("| ERROR validating the token! Invalid token |");
        }
    }
}