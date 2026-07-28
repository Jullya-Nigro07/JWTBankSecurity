package dio.web.JWTBankSecurity.exception;

import dio.web.JWTBankSecurity.dto.erro.ErroRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRequest> handlerInvalidFieldRegister(MethodArgumentNotValidException ex) {
        Map<String, String> error = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erro -> error.put(erro.getField(), erro.getDefaultMessage()));

        ErroRequest response = new ErroRequest(
                "Invalid or missing fields",
                HttpStatus.BAD_REQUEST.value(),
                error);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErroRequest> handlerUnauthorizedException(UnauthorizedException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ConflitInfoException.class)
    public ResponseEntity<ErroRequest> handlerConflitInfoException(ConflitInfoException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<ErroRequest> handlerUserNotFound(UserNotFound ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ValueInvalidException.class)
    public ResponseEntity<ErroRequest> handlerValueInvalidException(ValueInvalidException ex) {
        return buildError(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroRequest> handlerInvalidFormat(HttpMessageNotReadableException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<ErroRequest> buildError(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ErroRequest(message, status.value(), Map.of()));
    }
}