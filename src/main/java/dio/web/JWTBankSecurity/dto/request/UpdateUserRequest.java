package dio.web.JWTBankSecurity.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(String name, @Email String email, @Size(min = 7, max = 15) String password){
}
