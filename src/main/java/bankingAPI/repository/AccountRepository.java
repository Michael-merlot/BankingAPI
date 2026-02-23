package bankingAPI.repository;

import bankingAPI.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByOwnerId(Long ownerId);

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByIdAndOwnerId(Long accountId, Long userId);
}
