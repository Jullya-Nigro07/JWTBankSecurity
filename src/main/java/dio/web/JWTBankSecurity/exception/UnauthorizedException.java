package dio.web.JWTBankSecurity.exception;


public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message){
        super(message);
    }
}
