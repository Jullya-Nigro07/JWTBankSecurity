package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.config.JWTUserData;
import dio.web.JWTBankSecurity.entity.User;
import dio.web.JWTBankSecurity.exception.UnauthorizedException;
import dio.web.JWTBankSecurity.exception.UserNotFound;
import dio.web.JWTBankSecurity.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private UserRepository userRepository;
    public AuthorizationService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getAuthenticatedUser() {
        Authentication userAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (userAuthentication == null || !userAuthentication.isAuthenticated()) {
            throw new UnauthorizedException("| ERROR: Unauthenticated user |");
        }
        JWTUserData userResponse = (JWTUserData) userAuthentication.getPrincipal();
        String emailUser = userResponse.email();

        return userRepository.findUserByEmail(emailUser).orElseThrow(() -> new UserNotFound("| ERROR: User not found! |"));
    }
}