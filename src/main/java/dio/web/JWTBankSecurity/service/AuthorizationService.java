package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.config.JWTUserData;
import dio.web.JWTBankSecurity.entity.User;
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
            throw new RuntimeException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof JWTUserData jwtUserData)) {
            throw new RuntimeException("Token inválido ou usuário não autenticado");
        }

        User userFromToken = (User) userRepository.findUserByEmail(jwtUserData.email())
                .orElseThrow(() ->
                        new RuntimeException("Usuário do token não encontrado")
                );

        return userFromToken.getId().equals(id);
    }

    public User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof JWTUserData jwtUserData)) {
            throw new RuntimeException("Token inválido");
        }

        return (User) userRepository.findUserByEmail(jwtUserData.email())
                .orElseThrow(() ->
                        new RuntimeException("Usuário não encontrado")
                );
    }
}
