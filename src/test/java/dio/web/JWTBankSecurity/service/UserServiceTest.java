package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.dto.request.RegisterUserRequest;
import dio.web.JWTBankSecurity.dto.response.UserResponse;
import dio.web.JWTBankSecurity.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import java.util.Set;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private Validator validator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Inicializa o validador correto do Jakarta/Hibernate
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    @Test
    void shouldInvalidDto(){
        RegisterUserRequest userDto = new RegisterUserRequest(" ", "testdtoteste.com", "123456");
        Set<ConstraintViolation<RegisterUserRequest>> violacoes = validator.validate(userDto);
        assertEquals(3, violacoes.size(), "Existem violações de validação.");
    }

    @Test
    void shouldValidDto(){
        RegisterUserRequest userDto = new RegisterUserRequest("Teste", "testdto@teste.com", "123456789");
        Set<ConstraintViolation<RegisterUserRequest>> violacoes = validator.validate(userDto);
        assertEquals(0, violacoes.size(), "Não existem violações de validação.");
    }

    @Test
    void shouldRegisterUser() {
        RegisterUserRequest request = new RegisterUserRequest("Jullya", "jullya@email.com", "1234567");

        when(userRepository.findUserByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.password())).thenReturn("senha-hash");
        ResponseEntity<UserResponse> response = userService.register(request);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
                () -> assertNotNull(response.getBody())
        );

        verify(userRepository).findUserByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
    }
}
