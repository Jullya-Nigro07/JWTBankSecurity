package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.config.TokenConfig;
import dio.web.JWTBankSecurity.dto.request.LoginRequest;
import dio.web.JWTBankSecurity.dto.request.RegisterUserRequest;
import dio.web.JWTBankSecurity.dto.request.UpdateUserRequest;
import dio.web.JWTBankSecurity.dto.response.LoginResponse;
import dio.web.JWTBankSecurity.dto.response.UserResponse;
import dio.web.JWTBankSecurity.entity.User;
import dio.web.JWTBankSecurity.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    public UserService(
            AuthorizationService authorizationService,
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            TokenConfig tokenConfig,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
    }

    public ResponseEntity<LoginResponse> login(LoginRequest request) {

        try {
            UsernamePasswordAuthenticationToken userAndPass =
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    );

            Authentication authentication =
                    authenticationManager.authenticate(userAndPass);

            User user = (User) authentication.getPrincipal();

            if (user == null) {
                throw new RuntimeException("Usuário não autenticado");
            }

            String token = tokenConfig.generateToken(user);

            return ResponseEntity.ok(new LoginResponse(token));

        } catch (BadCredentialsException ex) {
            // cai no GlobalExceptionHandler → 401 ou 400
            throw new RuntimeException("Email ou senha inválidos");
        }
    }

    public ResponseEntity<UserResponse> register(RegisterUserRequest request) {

        if (userRepository.findUserByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(user.getName(), user.getEmail()));
    }

    public ResponseEntity<UserResponse> updateUser(Long id, @Valid UpdateUserRequest userRequest) {

        if (!authorizationService.testAuthorization(id)) {
            throw new RuntimeException("Acesso negado");
        }

        User existing = userRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Usuário não encontrado")
                );

        if (userRequest.name() != null) {
            existing.setName(userRequest.name());
        }
        if (userRequest.email() != null) {
            existing.setEmail(userRequest.email());
        }
        if (userRequest.password() != null) {
            existing.setPassword(passwordEncoder.encode(userRequest.password()));
        }

        userRepository.save(existing);

        return ResponseEntity.ok(
                new UserResponse(existing.getName(), existing.getEmail())
        );
    }

    public ResponseEntity<UserResponse> deleteUser(Long id) {

        if (!authorizationService.testAuthorization(id)) {
            throw new RuntimeException("Acesso negado");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Usuário não encontrado")
                );

        userRepository.delete(user);

        return ResponseEntity.ok(
                new UserResponse(user.getName(), user.getEmail())
        );
    }
}
