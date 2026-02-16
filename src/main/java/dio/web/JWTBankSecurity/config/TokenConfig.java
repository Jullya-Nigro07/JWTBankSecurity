package dio.web.JWTBankSecurity.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dio.web.JWTBankSecurity.entity.User;
import dio.web.JWTBankSecurity.exception.UnauthorizedException;
import dio.web.JWTBankSecurity.repository.UserRepository;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Optional;

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

    public Optional<JWTUserData> validateToken(String token) {

        try {
            DecodedJWT decode =
                    JWT.require(algorithm).build().verify(token);

            String email = decode.getSubject();
            Integer tokenVersionToken =
                    decode.getClaim("tokenVersion").asInt();

            User user = (User) userRepository.findUserByEmail(email).orElseThrow(() -> new UnauthorizedException("User not found"));

            if (!tokenVersionToken.equals(user.getTokenVersion())) {
                throw new UnauthorizedException("Token revoked");
            }

            return Optional.of(
                    JWTUserData.builder()
                            .userId(user.getId())
                            .email(email)
                            .build()
            );

        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }
}