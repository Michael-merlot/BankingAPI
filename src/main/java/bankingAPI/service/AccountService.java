package bankingAPI.service;

import bankingAPI.entity.AccountStatus;
import bankingAPI.model.Account;
import bankingAPI.model.User;
import bankingAPI.repository.AccountRepository;
import bankingAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public Account createAccount(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Account account = Account.builder()
                .owner(user)
                .build();

        return accountRepository.save(account);
    }

    public List<Account> getUserAccounts(String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        return accountRepository.findByOwnerId(user.getId());
    }

    @Transactional
    public Account deposit(Long accountId, BigDecimal amount, String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Счёт не найден"));

        if (!account.getOwner().getId().equals(user.getId())){
            throw new RuntimeException("Это не ваш счёт");
        }
        if (account.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("Счёт недоступен для операций");
        }

        account.deposit(amount);
        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount, String email){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (fromId.equals(toId)){
            throw new RuntimeException("Нельзя переводить на тот же счёт");
        }

        Account from = accountRepository.findById(fromId).orElseThrow(() -> new RuntimeException("Счёт отправителя не найден"));
        Account to = accountRepository.findById(toId).orElseThrow(() -> new RuntimeException("Счёт получателя не найден"));

        if (!from.getOwner().getId().equals(user.getId())){
            throw new RuntimeException("Это не ваш счёт");
        }
        if (from.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("Счёт отправителя недоступен");
        }
        if (to.getStatus() != AccountStatus.ACTIVE){
            throw new RuntimeException("Счёт получателя недоступен");
        }

        from.withdraw(amount);
        to.deposit(amount);

        accountRepository.save(from);
        accountRepository.save(to);
    }
}
