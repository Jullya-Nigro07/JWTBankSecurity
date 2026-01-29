package dio.web.JWTBankSecurity.service;

import dio.web.JWTBankSecurity.dto.request.AccountRequest;
import dio.web.JWTBankSecurity.dto.response.AccountResponse;
import dio.web.JWTBankSecurity.entity.Account;
import dio.web.JWTBankSecurity.entity.Transaction;
import dio.web.JWTBankSecurity.entity.User;
import dio.web.JWTBankSecurity.repository.AccountRepository;
import dio.web.JWTBankSecurity.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AuthorizationService authorizationService;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, AuthorizationService authorizationService) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public AccountResponse deposit(AccountRequest request) {
        User user = authorizationService.getAuthenticatedUser();

        Account account = user.getAccount();

        BigDecimal value = request.value();

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor inválido! Precisa ser maior que zero");
        }

        account.setBalance(account.getBalance().add(value));

        Transaction transaction = new Transaction();
        transaction.setType("DEPOSIT");
        transaction.setAmount(value);
        transaction.setAccount(account);

        transactionRepository.save(transaction);
        accountRepository.save(account);

        return new AccountResponse(account.getId(), account.getBalance());
    }

    @Transactional
    public AccountResponse withdraw(AccountRequest request){
        User user = authorizationService.getAuthenticatedUser();

        Account account = user.getAccount();

        BigDecimal value = request.value();;
        BigDecimal balance = account.getBalance();

        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor inválido! Precisa ser maior que 0");
        }

        if (balance.compareTo(value) < 0) {
            throw  new RuntimeException("Saldo insuficiente!");
        }

        Transaction transaction = new Transaction();
        transaction.setType("WITHDRAW");
        transaction.setAmount(value);
        transaction.setAccount(account);

        account.setBalance(account.getBalance().subtract(value));

        accountRepository.save(account);
        transactionRepository.save(transaction);

        return new AccountResponse(account.getId(), account.getBalance());
    }

    public AccountResponse extract(){
        User user = authorizationService.getAuthenticatedUser();
        Account account = user.getAccount();

        return new AccountResponse(account.getId(), account.getBalance());
    }
}