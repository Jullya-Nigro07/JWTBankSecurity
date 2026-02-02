package dio.web.JWTBankSecurity.controller;

import dio.web.JWTBankSecurity.dto.request.LoginRequest;
import dio.web.JWTBankSecurity.dto.request.RegisterUserRequest;
import dio.web.JWTBankSecurity.dto.request.UpdateUserRequest;
import dio.web.JWTBankSecurity.dto.response.LoginResponse;
import dio.web.JWTBankSecurity.dto.response.UserResponse;
import dio.web.JWTBankSecurity.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        return userService.register(request);
    }

    @PatchMapping("/updateRegister/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/deleteRegister/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
