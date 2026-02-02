package dio.web.JWTBankSecurity.repository;

import dio.web.JWTBankSecurity.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findById(Long userId);
}
