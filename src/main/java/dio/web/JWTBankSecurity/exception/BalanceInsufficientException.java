package dio.web.JWTBankSecurity.exception;

public class BalanceInsufficientException extends RuntimeException{
    public BalanceInsufficientException(String message){
        super(message);
    }
}
