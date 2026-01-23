package dio.web.JWTBankSecurity.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dio.web.JWTBankSecurity.entity.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class TokenConfig {

    private String secret = "secret-chave-de-teste-para-app-jwt-config-jullya";

    public String generateToken(User user){

        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("userId", user.getId())
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(String token){

        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decode = JWT.require(algorithm).build().verify(token);

            return Optional.of(JWTUserData.builder()
                    .userId(decode.getClaim("userId").asLong())
                    .email(decode.getSubject())
                    .build());
        }
        catch (JWTVerificationException ex){
            return Optional.empty();
        }
    }
}

