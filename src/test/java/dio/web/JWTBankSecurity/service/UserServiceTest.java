package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.dto.request.LoginRequest;
import dio.web.JWTBankSecurity.dto.request.RegisterUserRequest;
import dio.web.JWTBankSecurity.dto.response.LoginResponse;
import dio.web.JWTBankSecurity.exception.ConflitInfoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest{

    @Autowired
    private UserService userService;

    @Test
    void shouldValidateDto(){
        RegisterUserRequest user = new RegisterUserRequest("Teste", "testdto@teste.com", "1234567");
        assertAll(
                () -> assertFalse(user.name().isBlank()),
                () -> assertTrue(user.email().contains("@")),
                () -> assertTrue(user.password().length() >= 7 && user.password().length() <= 15)
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
        RegisterUserRequest user = new RegisterUserRequest("Teste01", "teste1@teste.com", "1234567");
        userService.register(user);

        //UpdateUserRequest userUpdate = new UpdateUserRequest("Teste02", null, null);
        //<UserResponse> response = userService.updateUser(userUpdate);

        //assertAll(
        //        () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
        //        () -> assertTrue(userUpdate.name().equals("Teste02"))
        //);
    }
}