package dio.web.JWTBankSecurity.config;

import dio.web.JWTBankSecurity.exception.TokenInvalidException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private final TokenConfig tokenConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityFilter(TokenConfig tokenConfig, CustomAuthenticationEntryPoint customAuthenticationEntryPoint){
        this.tokenConfig = tokenConfig;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizedHeader = request.getHeader("Authorization");

        try{
            if (Strings.isNotBlank(authorizedHeader) && authorizedHeader.startsWith("Bearer ")) {
                String token = authorizedHeader.substring("Bearer ".length());

                JWTUserData user = tokenConfig.validateToken(token);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, null);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);

        }catch (TokenInvalidException ex){
            customAuthenticationEntryPoint.commence(request, response, ex);
        }
    }
}