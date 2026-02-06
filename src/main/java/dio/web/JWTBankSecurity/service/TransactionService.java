package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.dto.response.TransactionResponse;
import dio.web.JWTBankSecurity.entity.Account;
import dio.web.JWTBankSecurity.entity.User;
import dio.web.JWTBankSecurity.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;

    public TransactionService(TransactionRepository transactionRepository,
                              AuthorizationService authorizationService) {
        this.transactionRepository = transactionRepository;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public List<TransactionResponse> findMyTransactions() {

        User user = authorizationService.getAuthenticatedUser();
        Account account = user.getAccount();

        return transactionRepository.findByAccountId(account.getId())
                .stream()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getType(),
                        transaction.getAmount(),
                        transaction.getTime()
                ))
                .toList();
    }
}
