package bankingAPI.controller;

import bankingAPI.dto.request.DepositRequest;
import bankingAPI.dto.request.TransferRequest;
import bankingAPI.model.Account;
import bankingAPI.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> createAccount(Authentication authentication){
        String email = authentication.getName();
        Account account = accountService.createAccount(email);
        return ResponseEntity.status(201).body(Map.of(
                "id", account.getId(),
                "accountNumber", account.getAccountNumber(),
                "balance", account.getBalance(),
                "currency", account.getCurrency(),
                "status", account.getStatus()
        ));
    }

    @GetMapping
    public ResponseEntity<?> getMyAccounts(Authentication authentication){
        String email = authentication.getName();
        List<Account> accountList = accountService.getUserAccounts(email);
        return ResponseEntity.ok(accountList.stream()
                .map(a -> Map.of(
                        "id", a.getId(),
                        "accountNumber", a.getAccountNumber(),
                        "balance", a.getBalance(),
                        "status", a.getStatus()
                ))
                .toList()
        );
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<?> deposit(@PathVariable Long id, @RequestBody DepositRequest request, Authentication authentication){
        String email = authentication.getName();
        Account account = accountService.deposit(id, request.getAmount(), email);
        return ResponseEntity.ok(Map.of(
                "accountNumber", account.getAccountNumber(),
                "balance", account.getBalance()
        ));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequest request, Authentication authentication){
        String email = authentication.getName();
        accountService.transfer(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount(),
                email
        );
        return ResponseEntity.ok(Map.of("message", "Перевод выполнен успешно"));
    }
}
