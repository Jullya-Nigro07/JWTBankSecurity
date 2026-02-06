package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.config.JWTUserData;
import dio.web.JWTBankSecurity.entity.User;
import dio.web.JWTBankSecurity.exception.UnauthorizedException;
import dio.web.JWTBankSecurity.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = Objects.requireNonNull(authentication).getPrincipal();

        if (!authentication.isAuthenticated()) {
            throw new UnauthorizedException("Unauthenticated user");
        }

        if (!(principal instanceof JWTUserData jwtUserData)) {
            throw new UnauthorizedException("Invalid Token");
        }

        return (User) userRepository.findUserByEmail(jwtUserData.email())
                .orElseThrow(() ->
                        new UnauthorizedException("User not found")
                );
    }
}
