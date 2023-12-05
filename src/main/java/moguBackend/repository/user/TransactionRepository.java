package moguBackend.repository.user;

import moguBackend.domain.admin.AdminEntity;
import moguBackend.domain.user.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
