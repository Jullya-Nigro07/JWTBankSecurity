package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.config.TokenConfig;
import dio.web.JWTBankSecurity.dto.request.LoginRequest;
import dio.web.JWTBankSecurity.dto.request.RegisterUserRequest;
import dio.web.JWTBankSecurity.dto.request.UpdateUserRequest;
import dio.web.JWTBankSecurity.dto.response.LoginResponse;
import dio.web.JWTBankSecurity.dto.response.UserResponse;
import dio.web.JWTBankSecurity.entity.Account;
import dio.web.JWTBankSecurity.entity.User;
import dio.web.JWTBankSecurity.exception.UnauthorizedException;
import dio.web.JWTBankSecurity.repository.AccountRepository;
import dio.web.JWTBankSecurity.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final PasswordEncoder passwordEncoder;
    private final AuthorizationService authorizationService;

    public UserService(
            AuthorizationService authorizationService,
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            TokenConfig tokenConfig,
            PasswordEncoder passwordEncoder,
            AccountRepository accountRepository
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
        this.passwordEncoder = passwordEncoder;
        this.authorizationService = authorizationService;
        this.accountRepository = accountRepository;
    }

    /// UsernamePasswordAuthenticationToken - Quero autenticar esse email com essa senha.
    /// getPrincipal() - Pego o usuário autenticado.
    public ResponseEntity<LoginResponse> login(LoginRequest request) {

        try {
            UsernamePasswordAuthenticationToken userAndPass =
                    new UsernamePasswordAuthenticationToken(
                            request.email(), request.password());

            Authentication authentication =
                    authenticationManager.authenticate(userAndPass);

            User user = (User) authentication.getPrincipal();

            String token = tokenConfig.generateToken(Objects.requireNonNull(user));
            return ResponseEntity.ok(new LoginResponse(token));

        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid email or password");
        }
    }

    public ResponseEntity<UserResponse> register(RegisterUserRequest request) {

        if (userRepository.findUserByEmail(request.email()).isPresent()) {
            throw new UnauthorizedException("Email already registered. Please use a different email address!");
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        Account account = new Account();
        account.setUser(user);

        userRepository.save(user);
        accountRepository.save(account);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponse(user.getName(), user.getEmail()));
    }


    public ResponseEntity<UserResponse> updateUser(UpdateUserRequest userRequest) {

        User user = authorizationService.getAuthenticatedUser();

        if (userRequest.name() != null) {
            user.setName(userRequest.name());
        }
        if (userRequest.email() != null) {
            user.setEmail(userRequest.email());
        }
        if (userRequest.password() != null) {
            user.setPassword(passwordEncoder.encode(userRequest.password()));
        }

        userRepository.save(user);

        return ResponseEntity.ok(
                new UserResponse(user.getName(), user.getEmail())
        );
    }

    public ResponseEntity<UserResponse> deleteUser() {

        User user = authorizationService.getAuthenticatedUser();
        User userExisting = userRepository.findById(user.getId())
                .orElseThrow(() ->
                        new UnauthorizedException("User not found")
                );

        userRepository.delete(userExisting);

        return ResponseEntity.ok(
                new UserResponse(user.getName(), user.getEmail())
        );
    }
}
