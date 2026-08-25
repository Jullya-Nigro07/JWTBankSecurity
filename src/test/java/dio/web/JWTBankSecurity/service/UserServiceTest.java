package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.dto.request.LoginRequest;
import dio.web.JWTBankSecurity.dto.request.RegisterUserRequest;
import dio.web.JWTBankSecurity.dto.response.LoginResponse;
import dio.web.JWTBankSecurity.exception.ConflitInfoException;
import dio.web.JWTBankSecurity.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @Test
    void UserRegisterEmailsTest() {
        RegisterUserRequest user = new RegisterUserRequest("Teste", "testemail@teste.com", "1234567");
        RegisterUserRequest user2 = new RegisterUserRequest("Teste", "testemail@teste.com", "123456890");
        userService.register(user);

        Assertions.assertThrows(ConflitInfoException.class, () -> {
            userService.register(user2);
        });
    }

    @Test
    void UserLoginTest(){
        RegisterUserRequest user = new RegisterUserRequest("Teste", "testelogin@teste.com", "1234567");
        userService.register(user);

        LoginRequest loginUser = new LoginRequest("testelogin@teste.com", "1234567");
        ResponseEntity<LoginResponse> responseLoginToken = userService.login(loginUser);

        Assertions.assertEquals(HttpStatus.OK, responseLoginToken.getStatusCode());
        Assertions.assertNotNull(responseLoginToken.getBody().token());
    }
}