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

    /// chave usada para gerar a assinatura. A secret funciona como uma senha que só o servidor conhece,
    ///  e é usada para impedir que alguém modifique o header ou o payload do JWT.

    private String secret = "secret-chave-de-teste-para-app-jwt-config-jullya";
    Algorithm algorithm = Algorithm.HMAC256(secret);

    public String generateToken(User user){

        return JWT.create()
                .withClaim("userId", user.getId())
                .withSubject(user.getEmail()) // define a quem pertence
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(String token){

        try{
            /// Qual algorithm o token deve ter, constrói o verificador, verifica e decodifica.
            DecodedJWT decode = JWT.require(algorithm).build().verify(token);

            /// O @Builder no DTO JWTUserData permite a construção explícita dos atributos, para evitar erros e construtores longos.
            /// .asLong() - as claim são retornadas sem tipo, então definimos o que foi buscado no get é do tipo Long.
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
