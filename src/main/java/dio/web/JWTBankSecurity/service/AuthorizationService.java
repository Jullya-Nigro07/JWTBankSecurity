package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.config.JWTUserData;
import dio.web.JWTBankSecurity.entity.User;
import dio.web.JWTBankSecurity.exception.NotFoundException;
import dio.web.JWTBankSecurity.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {

    private final UserRepository userRepository;

    public AuthorizationService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public Boolean testAuthorization(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotFoundException("Unauthenticated user");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof JWTUserData jwtUserData)) {
            throw new NotFoundException("Invalid Token");
        }

        User userFromToken = (User) userRepository.findUserByEmail(jwtUserData.email())
                .orElseThrow(() ->
                        new NotFoundException("User token not found")
                );

        return userFromToken.getId().equals(id);
    }

    public User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotFoundException("Unauthenticated user");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof JWTUserData jwtUserData)) {
            throw new NotFoundException("Invalid Token");
        }

        return (User) userRepository.findUserByEmail(jwtUserData.email())
                .orElseThrow(() ->
                        new NotFoundException("User not found")
                );
    }
}
