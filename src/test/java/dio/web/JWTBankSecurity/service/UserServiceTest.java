package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.dto.request.LoginRequest;
import dio.web.JWTBankSecurity.dto.request.RegisterUserRequest;
import dio.web.JWTBankSecurity.dto.request.UpdateUserRequest;
import dio.web.JWTBankSecurity.dto.response.LoginResponse;
import dio.web.JWTBankSecurity.dto.response.UserResponse;
import dio.web.JWTBankSecurity.entity.User;
import dio.web.JWTBankSecurity.exception.ConflitInfoException;
import dio.web.JWTBankSecurity.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @MockitoBean
    private AuthorizationService authorizationService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Validator validator;

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
    void shouldSaveUser() {
        RegisterUserRequest userRequest = new RegisterUserRequest("Teste", "testemailsave@teste.com", "1234567");
        ResponseEntity<UserResponse> response = userService.register(userRequest);
        User user = userRepository.findUserByEmail(response.getBody().email()).orElseThrow();

        assertAll(
                () -> assertNotNull(user),
                () -> assertEquals(HttpStatus.CREATED, response.getStatusCode())
        );
    }

    @Test
    void shouldHasePassword(){
        String password = "1234567";
        RegisterUserRequest userRequest = new RegisterUserRequest("Teste", "testemailpass@teste.com", password);
        userService.register(userRequest);

        User user = userRepository.findUserByEmail(userRequest.email()).orElseThrow();

        assertAll(
                () -> assertNotEquals(password, user.getPassword()),
                () -> assertTrue(passwordEncoder.matches(password, user.getPassword()))
        );
    }

    @Test
    void shouldRejectDuplicateEmail() {
        RegisterUserRequest user = new RegisterUserRequest("Teste", "testemail@teste.com", "1234567");
        RegisterUserRequest user2 = new RegisterUserRequest("Teste", "testemail@teste.com", "1234567");
        userService.register(user);

        assertThrows(ConflitInfoException.class, () -> {
            userService.register(user2);
        });
    }

    @Test
    void shouldGenerateTokenUser(){
        RegisterUserRequest user = new RegisterUserRequest("Teste", "testelogin@teste.com", "1234567");
        userService.register(user);

        LoginRequest loginUser = new LoginRequest(user.email(), user.password());
        ResponseEntity<LoginResponse> responseLogin = userService.login(loginUser);

        assertAll(
                () -> assertNotNull(responseLogin.getBody()),
                () -> assertEquals(HttpStatus.OK, responseLogin.getStatusCode())
        );
    }

    @Test
    void shouldUpdateUser(){
        RegisterUserRequest userRequest = new RegisterUserRequest("Teste01", "teste01@teste.com", "1234567");
        userService.register(userRequest);

        User user = userRepository.findUserByEmail(userRequest.email()).orElseThrow();
        when(authorizationService.getAuthenticatedUser()).thenReturn(user);

        UpdateUserRequest userRequest2 = new UpdateUserRequest("Teste02", "teste02@teste.com", "7654321");
        ResponseEntity<UserResponse> response = userService.updateUser(userRequest2);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("Teste02", user.getName()),
                () -> assertEquals("teste02@teste.com", user.getEmail()),
                () -> assertTrue(passwordEncoder.matches("7654321", user.getPassword()))
        );
    }

    @Test
    void shouldDeleteUser(){
        RegisterUserRequest userRequest = new RegisterUserRequest("Teste", "teste@teste.com", "1234567");
        userService.register(userRequest);

        User userRepo = userRepository.findUserByEmail(userRequest.email()).orElseThrow();
        when(authorizationService.getAuthenticatedUser()).thenReturn(userRepo);

        ResponseEntity<UserResponse> user = userService.deleteUser();

        assertAll(
                () -> assertTrue(userRepository.findUserByEmail(user.getBody().email()).isEmpty()),
                () -> assertEquals(HttpStatus.OK, user.getStatusCode())
        );
    }
}