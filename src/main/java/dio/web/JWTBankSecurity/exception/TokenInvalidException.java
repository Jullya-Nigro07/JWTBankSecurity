package dio.web.JWTBankSecurity.exception;
import org.springframework.security.core.AuthenticationException;

public class TokenInvalidException extends AuthenticationException {
        public TokenInvalidException(String message){
        super(message);
    }
}