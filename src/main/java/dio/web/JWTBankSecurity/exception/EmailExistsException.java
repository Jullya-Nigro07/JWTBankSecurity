package dio.web.JWTBankSecurity.exception;


public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String message){
        super(message);
    }
}
