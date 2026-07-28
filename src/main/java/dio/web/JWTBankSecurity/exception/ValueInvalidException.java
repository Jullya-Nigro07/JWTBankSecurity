package dio.web.JWTBankSecurity.exception;

public class ValueInvalidException extends RuntimeException {
    public ValueInvalidException(String message) {
        super(message);
    }
}
