package dio.web.JWTBankSecurity.exception;

import dio.web.JWTBankSecurity.dto.erro.ErroBalanceInsufficient;
import dio.web.JWTBankSecurity.dto.erro.ErroExistsEmail;
import dio.web.JWTBankSecurity.dto.erro.ErroNotFound;
import dio.web.JWTBankSecurity.dto.erro.ErroRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRequest> handlerCampoInvalidoRegister(MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erro -> error.put(erro.getField(), erro.getDefaultMessage()));

        ErroRequest response = new ErroRequest(
                "Invalid or missing fields",
                HttpStatus.BAD_REQUEST.value(),
                error);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErroExistsEmail> handlerUnauthorizedException(UnauthorizedException ex){
        ErroExistsEmail erroExistsEmail = new ErroExistsEmail(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.badRequest().body(erroExistsEmail);
    }
}

