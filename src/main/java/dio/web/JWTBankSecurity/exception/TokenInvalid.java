package dio.web.JWTBankSecurity.exception;

public class TokenInvalid extends RuntimeException {
    public TokenInvalid(String message) {
        super(message);
    }
}
