package dio.web.JWTBankSecurity.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //Preenchendo o tipo de conteudo para o response
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        //Preenchendo o status http do AuthenticationEntryPoint
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        //Montando a resposta
        Map<String, Object> body = new HashMap<>();
        body.put("status", response.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}