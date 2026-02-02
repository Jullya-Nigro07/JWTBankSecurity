package dio.web.JWTBankSecurity.controller;

import dio.web.JWTBankSecurity.dto.response.TransactionResponse;
import dio.web.JWTBankSecurity.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> findByAccount() {
        return ResponseEntity.ok(transactionService.findMyTransactions());
    }

}
